package com.example.backend.modules.system.service.needrefactor;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.backend.common.error.BusinessErrorCode;
import com.example.backend.common.error.BusinessException;
import com.example.backend.common.utils.NumberUtils;
import com.example.backend.common.utils.StringUtils;
import com.example.backend.modules.system.mapper.MenuMapper;
import com.example.backend.modules.system.model.converter.MenuConverter;
import com.example.backend.modules.system.model.dto.MenuDto;
import com.example.backend.modules.system.model.entity.Menu;
import com.example.backend.modules.system.repository.MenuRepository;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SystemMenuService {

    @Value("${project-config.site-id}")
    private String siteId;

    @Resource
    MenuConverter menuConverter;
    @Resource
    private MenuMapper menuMapper;
    @Resource
    private MenuRepository systemMenuRepository;
    // @Resource
    // private SystemPrivilegeService systemPrivilegeService;

    /**
     * 获取 当前用户的 SystemMenu 树
     *
     * @param menus
     * @param menuIdList
     * @return
     */
    public List<MenuDto> getCurrentUserMenuDTOTree(List<Menu> menus, Set<String> menuIdList) {
        if (menus == null) {
            menus = getSystemMenuList(null);
        }
        // 筛选出 menuId 在 menuIdList 中的 SystemMenu 对象
        List<Menu> filteredMenus = menus.stream()
                .filter(menu -> menuIdList.contains(menu.getMenuCode()))
                .toList();

        return getMenuChildrenList(null, filteredMenus, new LinkedList<>());
    }

    public List<Menu> getSystemMenuList(@Nullable Integer currentUserRoleId) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(Menu::getLevel, 0);
        if (currentUserRoleId != null && currentUserRoleId != 1) {
            queryWrapper.eq(Menu::getIsHide, 0);
        }
        queryWrapper.orderByAsc(Menu::getSortOrder);
        return menuMapper.selectList(queryWrapper);
    }

    public List<Menu> getSystemMenuListWithoutRootLevel() {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Menu::getSortOrder);
        return menuMapper.selectList(queryWrapper);
    }

    @SneakyThrows
    private List<MenuDto> getMenuChildrenList(Long parentId, List<Menu> menus, List<Long> foundParent) {
        if (foundParent.contains(parentId)) {
            log.error("数据库菜单配置错误，递归死循环 parentId: {}, foundParent: {}", parentId, foundParent);
            throw new BusinessException(BusinessErrorCode.FAULT_ERROR,
                    "数据库菜单配置错误，递归死循环！" + "infinite loop will lead to java.lang.StackOverflowError");
        }
        foundParent.add(parentId);
        return menus.stream()
                .filter(menu -> Objects.equals(menu.getParentId(), parentId))
                .map(menu -> {
                    // 测试死循环检测 测试用
                    // foundParent.add(systemMenu.getIdWithOrder());
                    // 获取这一项的 children
                    List<MenuDto> children = getMenuChildrenList(menu.getId(), menus, foundParent);
                    // 转 DTO
                    MenuDto menuDTO = menuConverter.toDto(menu);
                    menuDTO.setChildren(children);
                    return menuDTO;
                })
                .collect(Collectors.toList());
    }

    /**
     * 根据菜单名称模糊查询菜单项
     *
     * @param menuName
     * @return
     */
    public Menu getSystemMenuByName(String menuName) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Menu::getMenuName, menuName);
        queryWrapper.last("limit 1");
        Menu menu = menuMapper.selectOne(queryWrapper);
        return menu;
    }

    /**
     * 添加菜单项
     *
     * @param menu
     */
    public void addSystemMenu(Menu menu) {
        // 将该菜单项的后续菜单次序+1
        Integer sequence = menu.getSortOrder();
        LambdaUpdateWrapper<Menu> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.ge(Menu::getSortOrder, sequence);
        updateWrapper.setIncrBy(Menu::getSortOrder, 1);
        menuMapper.update(updateWrapper);

        // // 查询当前菜单项的父级菜单（如果存在）
        // if (systemMenu.getParentId() != null) {
        //     SystemMenu parentSystemMenu = systemMenuRepository.getById(systemMenu.getParentId());
        //     // 拼接菜单项全名
        //     systemMenu.setMenuFullName(String.join("-", parentSystemMenu.getMenuFullName(), systemMenu.getMenuName()));
        // } else {
        //     systemMenu.setMenuFullName(systemMenu.getMenuName());
        // }
        // 保存插入的菜单项
        menu.setUpdateTime(LocalDateTime.now());
        menuMapper.insert(menu);

    }

    public void updateSystemMenu(MenuDto menuDTO) {
        Menu menu = menuConverter.toEntity(menuDTO);

        LambdaUpdateWrapper<Menu> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Menu::getId, menu.getId());
        updateWrapper.set(Menu::getMenuCode, menu.getMenuCode());
        updateWrapper.set(Menu::getMenuName, menu.getMenuName());
        updateWrapper.set(Menu::getParentId, menu.getParentId());
        updateWrapper.set(Menu::getUpdateTime, new Date());
        updateWrapper.set(Objects.nonNull(menu.getIsHide()), Menu::getIsHide, menu.getIsHide());

        if (menu.getParentId() != null) {
            // 如果存在父目录，重新拼接菜单项全名
            Menu parentMenu = systemMenuRepository.getById(menu.getParentId());
            // updateWrapper.set(SystemMenu::getMenuFullName, String.join("-", parentSystemMenu.getMenuFullName(), systemMenu.getMenuName()));
        } else {
            // 一级目录则直接将 menuFullName 更新为 menuName
            // updateWrapper.set(SystemMenu::getMenuFullName, systemMenu.getMenuName());
        }

        menuMapper.update(updateWrapper);
    }

    @Transactional
    public void removeSystemMenu(Menu menu) {
        // 移除菜单项前更新后续菜单项的次序 次序-1
        Integer sequence = menu.getSortOrder();
        LambdaUpdateWrapper<Menu> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.gt(Menu::getSortOrder, sequence);
        updateWrapper.setDecrBy(Menu::getSortOrder, 1);
        menuMapper.update(updateWrapper);

        // 删除菜单
        menuMapper.deleteById(menu.getId());

        // 删除该菜单相关权限
        // TODO
        // systemPrivilegeService.removePrivilegesByModule(systemMenu.getMenuCode());
    }

    public void exchangeSystemMenu(String fromMenuId, String movingMode) throws BusinessException {
        List<Menu> menuList = findExchangedMenu(fromMenuId, movingMode);
        if (menuList.size() != 2) {
            throw new BusinessException(BusinessErrorCode.PARAMETER_VALIDATION_ERROR, "无效id");
        }
        Menu menu1 = menuList.get(0);
        Menu menu2 = menuList.get(1);

        if (!Objects.equals(menu1.getParentId(), menu2.getParentId())) {
            throw new BusinessException(BusinessErrorCode.PARAMETER_VALIDATION_ERROR, "两个菜单不在同一层级");
        }

        LambdaUpdateWrapper<Menu> updateWrapper1 = new LambdaUpdateWrapper<>();
        updateWrapper1.set(Menu::getSortOrder, menu2.getSortOrder());
        updateWrapper1.eq(Menu::getId, menu1.getId());
        menuMapper.update(updateWrapper1);

        LambdaUpdateWrapper<Menu> updateWrapper2 = new LambdaUpdateWrapper<>();
        updateWrapper2.set(Menu::getSortOrder, menu1.getSortOrder());
        updateWrapper2.eq(Menu::getId, menu2.getId());
        menuMapper.update(updateWrapper2);
    }

    private List<Menu> findExchangedMenu(@NotNull String fromMenuId, @NotNull String movingMode) {
        List<Menu> resultMenus = new ArrayList<>();
        Menu currentMenu = systemMenuRepository.getById(fromMenuId);
        resultMenus.add(currentMenu);

        List<Menu> menus;
        if (currentMenu.getParentId() != null) {
            // 查询父目录菜单列表
            Long parentId = currentMenu.getParentId();
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Menu::getParentId, parentId);
            queryWrapper.orderByAsc(Menu::getSortOrder);
            menus = menuMapper.selectList(queryWrapper);
        } else {
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Menu::getLevel, 1);
            queryWrapper.orderByAsc(Menu::getSortOrder);
            menus = menuMapper.selectList(queryWrapper);
        }

        if ("up".equals(movingMode)) {
            // 菜单上移
            Menu previousMenu = null;
            for (Menu menu : menus) {
                if (menu.getId().equals(NumberUtils.parseLong(fromMenuId))) {
                    break;
                }
                previousMenu = menu;
            }
            resultMenus.add(previousMenu);
        } else if ("down".equals(movingMode)) {
            // 菜单下移
            Menu nextMenu = null;
            boolean foundCurrent = false;
            for (Menu menu : menus) {
                if (foundCurrent) {
                    nextMenu = menu;
                    break;
                }
                if (menu.getId().equals(NumberUtils.parseLong(fromMenuId))) {
                    foundCurrent = true;
                }
            }
            resultMenus.add(nextMenu);
        }

        return resultMenus;
    }

    /**
     * 获取上一级的菜单列表
     *
     * @param id
     */
    public List<Menu> getUpLevelSystemMenuList(String id) throws BusinessException {
        Menu menu = systemMenuRepository.getById(id);
        if (menu == null || menu.getLevel() - 1 == 0) {
            throw new BusinessException(BusinessErrorCode.PARAMETER_VALIDATION_ERROR);
        }

        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getLevel, menu.getLevel() - 1);
        queryWrapper.orderByAsc(Menu::getSortOrder);
        List<Menu> menuList = menuMapper.selectList(queryWrapper);

        return menuList;

    }

    /**
     * 根据menuId获取菜单项
     *
     * @param menuId
     * @return
     * @throws BusinessException 业务异常
     */
    public Menu getSystemMenuByMenuId(String menuId) throws BusinessException {
        if (StringUtils.isEmpty(menuId)) {
            throw new BusinessException(BusinessErrorCode.PARAMETER_VALIDATION_ERROR);
        }
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getMenuCode, menuId);
        queryWrapper.last("LIMIT 1");
        Menu menu = systemMenuRepository.getOne(queryWrapper);
        return menu;
    }

    /**
     * 获取系统基准权限（所有用户均拥有的权限）
     *
     * @return
     */
    public List<Menu> getZeroLevelMenuList() {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getLevel, 0);
        queryWrapper.orderByAsc(Menu::getSortOrder);
        List<Menu> menus = menuMapper.selectList(queryWrapper);
        return menus;
    }

    /**
     * 检测是否存在子菜单
     *
     * @param id
     * @return
     */
    public Boolean hasChildren(@NotNull Long id) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId, id);
        long count = systemMenuRepository.count(queryWrapper);
        return count > 0;
    }

    /**
     * 根据菜单id获取其最后一个子菜单项
     *
     * @param id
     * @return
     */
    public Menu getLastChild(@Nullable Long id) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        if (id == null) {
            queryWrapper.isNull(Menu::getParentId);
        } else {
            queryWrapper.eq(Menu::getParentId, id);
        }
        queryWrapper.orderByDesc(Menu::getSortOrder);
        queryWrapper.last("LIMIT 1");
        Menu menu = systemMenuRepository.getOne(queryWrapper);
        return menu;
    }

    /**
     * 导出菜单
     * <p>
     * （用于跨系统间的系统菜单同步，仅包含菜单属性及层级结构，不含主键id）
     *
     * @return
     */
    public String exportJson() {
        LambdaQueryWrapper<Menu> qw = new LambdaQueryWrapper<>();
        // 按照 menuId 排序，尽量保证相同菜单比对时前后顺序一致
        qw.orderByAsc(Menu::getMenuCode);
        List<Menu> list = systemMenuRepository.list(qw);

        // 构造 id -> menuId 字典
        HashMap<Long, String> menuIdMap = new HashMap<>();
        for (Menu menu : list) {
            menuIdMap.put(menu.getId(), menu.getMenuCode());
        }

        // parentMenuId -> [ {sequence, childId}, {sequence, childId}, ... ]
        HashMap<String, List<Pair<Integer, String>>> menuSequencePairMap = new HashMap<>();
        List<JSONObject> menuList = new ArrayList<>(list.size());
        for (Menu menu : list) {
            @Nullable String parentMenuId = menuIdMap.get(menu.getParentId());
            @NotNull String parentMenuIdNotNull = parentMenuId == null ? "" : parentMenuId;

            JSONObject item = new JSONObject();
            item.put("menuId", menu.getMenuCode());
            // item.put("parentMenuId", parentMenuId);
            item.put("parentMenuId", parentMenuIdNotNull);
            item.put("menuName", menu.getMenuName());
            // item.put("menuFullName", menu.getMenuFullName());
            item.put("level", menu.getLevel());
            // item.put("sequence", menu.getSortOrder());
            item.put("isHide", menu.getIsHide());
            menuList.add(item);

            List<Pair<Integer, String>> childMenuIdList = menuSequencePairMap.getOrDefault(parentMenuIdNotNull, new ArrayList<>());
            Pair<Integer, String> pair = Pair.of(menu.getSortOrder(), menu.getMenuCode());
            childMenuIdList.add(pair);
            menuSequencePairMap.put(parentMenuIdNotNull, childMenuIdList);
        }

        // parentMenuId -> [ childId, childId, ... ]
        HashMap<String, List<String>> menuSequenceMap = new HashMap<>();
        menuSequencePairMap.forEach((menuId, pairList) -> {
            // pairList.sort(Map.Entry.comparingByKey());
            pairList.sort((a, b) -> a.getKey().compareTo(b.getKey()));
        });

        for (Map.Entry<String, List<Pair<Integer, String>>> entry : menuSequencePairMap.entrySet()) {
            List<String> childMenuIdList = entry.getValue().stream().map(Pair::getRight).toList();
            menuSequenceMap.put(entry.getKey(), childMenuIdList);
        }

        // LocalDate currentDate = LocalDate.now(); // 获取当前日期
        // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 定义格式
        // String formattedDate = currentDate.format(formatter); // 格式化输出

        LocalDateTime currentDateTime = LocalDateTime.now(); // 获取当前日期和时间（精确到纳秒）
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // 定义格式，包含时分秒
        String formattedDateTime = currentDateTime.format(formatter); // 格式化输出

        JSONObject info = new JSONObject();
        info.put("siteId", siteId);
        info.put("exportTime", formattedDateTime);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("info", info);
        jsonObject.put("list", menuList);
        jsonObject.put("order", menuSequenceMap);

        return JSON.toJSONString(jsonObject,
                // 保证每次导出时顺序相同
                JSONWriter.Feature.SortMapEntriesByKeys,
                // 导出时保留 null 值
                JSONWriter.Feature.WriteMapNullValue
                // 美化输出（改为前端 JSON.parse 美化输出）
                // JSONWriter.Feature.PrettyFormat
        );
    }
}
