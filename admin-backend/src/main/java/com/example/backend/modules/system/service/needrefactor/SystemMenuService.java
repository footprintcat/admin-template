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
import com.example.backend.modules.system.model.dto.SystemMenuDto;
import com.example.backend.modules.system.model.entity.SystemMenu;
import com.example.backend.modules.system.mapper.SystemMenuMapper;
import com.example.backend.modules.system.repository.SystemMenuRepository;
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
    private SystemMenuMapper systemMenuMapper;
    @Resource
    private SystemMenuRepository systemMenuRepository;
    @Resource
    private SystemPrivilegeService systemPrivilegeService;

    /**
     * 获取 当前用户的 SystemMenu 树
     *
     * @param systemMenus
     * @param menuIdList
     * @return
     */
    public List<SystemMenuDto> getCurrentUserMenuDTOTree(List<SystemMenu> systemMenus, Set<String> menuIdList) {
        if (systemMenus == null) {
            systemMenus = getSystemMenuList(null);
        }
        // 筛选出 menuId 在 menuIdList 中的 SystemMenu 对象
        List<SystemMenu> filteredMenus = systemMenus.stream()
                .filter(menu -> menuIdList.contains(menu.getMenuCode()))
                .toList();

        return getMenuChildrenList(null, filteredMenus, new LinkedList<>());
    }

    public List<SystemMenu> getSystemMenuList(@Nullable Integer currentUserRoleId) {
        LambdaQueryWrapper<SystemMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(SystemMenu::getLevel, 0);
        if (currentUserRoleId != null && currentUserRoleId != 1) {
            queryWrapper.eq(SystemMenu::getIsHide, 0);
        }
        queryWrapper.orderByAsc(SystemMenu::getSortOrder);
        return systemMenuMapper.selectList(queryWrapper);
    }

    public List<SystemMenu> getSystemMenuListWithoutRootLevel() {
        LambdaQueryWrapper<SystemMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(SystemMenu::getSortOrder);
        return systemMenuMapper.selectList(queryWrapper);
    }

    @SneakyThrows
    private List<SystemMenuDto> getMenuChildrenList(Long parentId, List<SystemMenu> systemMenus, List<Long> foundParent) {
        if (foundParent.contains(parentId)) {
            log.error("数据库菜单配置错误，递归死循环 parentId: {}, foundParent: {}", parentId, foundParent);
            throw new BusinessException(BusinessErrorCode.FAULT_ERROR, "数据库菜单配置错误，递归死循环！" +
                    "infinite loop will lead to java.lang.StackOverflowError");
        }
        foundParent.add(parentId);
        return systemMenus.stream()
                .filter(systemMenu -> Objects.equals(systemMenu.getParentId(), parentId))
                .map(systemMenu -> {
                    // 测试死循环检测 测试用
                    // foundParent.add(systemMenu.getIdWithOrder());
                    // 获取这一项的 children
                    List<SystemMenuDto> children = getMenuChildrenList(systemMenu.getId(), systemMenus, foundParent);
                    // 转 DTO
                    SystemMenuDto systemMenuDTO = SystemMenuDto.fromEntity(systemMenu);
                    systemMenuDTO.setChildren(children);
                    return systemMenuDTO;
                })
                .collect(Collectors.toList());
    }

    /**
     * 根据菜单名称模糊查询菜单项
     *
     * @param menuName
     * @return
     */
    public SystemMenu getSystemMenuByName(String menuName) {
        LambdaQueryWrapper<SystemMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(SystemMenu::getMenuName, menuName);
        queryWrapper.last("limit 1");
        SystemMenu systemMenu = systemMenuMapper.selectOne(queryWrapper);
        return systemMenu;
    }

    /**
     * 添加菜单项
     *
     * @param systemMenu
     */
    public void addSystemMenu(SystemMenu systemMenu) {
        // 将该菜单项的后续菜单次序+1
        Integer sequence = systemMenu.getSortOrder();
        LambdaUpdateWrapper<SystemMenu> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.ge(SystemMenu::getSortOrder, sequence);
        updateWrapper.setIncrBy(SystemMenu::getSortOrder, 1);
        systemMenuMapper.update(updateWrapper);

        // // 查询当前菜单项的父级菜单（如果存在）
        // if (systemMenu.getParentId() != null) {
        //     SystemMenu parentSystemMenu = systemMenuRepository.getById(systemMenu.getParentId());
        //     // 拼接菜单项全名
        //     systemMenu.setMenuFullName(String.join("-", parentSystemMenu.getMenuFullName(), systemMenu.getMenuName()));
        // } else {
        //     systemMenu.setMenuFullName(systemMenu.getMenuName());
        // }
        // 保存插入的菜单项
        systemMenu.setUpdateTime(LocalDateTime.now());
        systemMenuMapper.insert(systemMenu);

    }

    public void updateSystemMenu(SystemMenuDto systemMenuDTO) {
        SystemMenu systemMenu = SystemMenuDto.toEntity(systemMenuDTO);

        LambdaUpdateWrapper<SystemMenu> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SystemMenu::getId, systemMenu.getId());
        updateWrapper.set(SystemMenu::getMenuCode, systemMenu.getMenuCode());
        updateWrapper.set(SystemMenu::getMenuName, systemMenu.getMenuName());
        updateWrapper.set(SystemMenu::getParentId, systemMenu.getParentId());
        updateWrapper.set(SystemMenu::getUpdateTime, new Date());
        updateWrapper.set(Objects.nonNull(systemMenu.getIsHide()), SystemMenu::getIsHide, systemMenu.getIsHide());

        if (systemMenu.getParentId() != null) {
            // 如果存在父目录，重新拼接菜单项全名
            SystemMenu parentSystemMenu = systemMenuRepository.getById(systemMenu.getParentId());
            // updateWrapper.set(SystemMenu::getMenuFullName, String.join("-", parentSystemMenu.getMenuFullName(), systemMenu.getMenuName()));
        } else {
            // 一级目录则直接将 menuFullName 更新为 menuName
            // updateWrapper.set(SystemMenu::getMenuFullName, systemMenu.getMenuName());
        }

        systemMenuMapper.update(updateWrapper);
    }

    @Transactional
    public void removeSystemMenu(SystemMenu systemMenu) {
        // 移除菜单项前更新后续菜单项的次序 次序-1
        Integer sequence = systemMenu.getSortOrder();
        LambdaUpdateWrapper<SystemMenu> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.gt(SystemMenu::getSortOrder, sequence);
        updateWrapper.setDecrBy(SystemMenu::getSortOrder, 1);
        systemMenuMapper.update(updateWrapper);

        // 删除菜单
        systemMenuMapper.deleteById(systemMenu.getId());

        // 删除该菜单相关权限
        systemPrivilegeService.removePrivilegesByModule(systemMenu.getMenuCode());
    }

    public void exchangeSystemMenu(String fromMenuId, String movingMode) throws BusinessException {
        List<SystemMenu> systemMenuList = findExchangedMenu(fromMenuId, movingMode);
        if (systemMenuList.size() != 2) {
            throw new BusinessException(BusinessErrorCode.PARAMETER_VALIDATION_ERROR, "无效id");
        }
        SystemMenu systemMenu1 = systemMenuList.get(0);
        SystemMenu systemMenu2 = systemMenuList.get(1);

        if (!Objects.equals(systemMenu1.getParentId(), systemMenu2.getParentId())) {
            throw new BusinessException(BusinessErrorCode.PARAMETER_VALIDATION_ERROR, "两个菜单不在同一层级");
        }

        LambdaUpdateWrapper<SystemMenu> updateWrapper1 = new LambdaUpdateWrapper<>();
        updateWrapper1.set(SystemMenu::getSortOrder, systemMenu2.getSortOrder());
        updateWrapper1.eq(SystemMenu::getId, systemMenu1.getId());
        systemMenuMapper.update(updateWrapper1);

        LambdaUpdateWrapper<SystemMenu> updateWrapper2 = new LambdaUpdateWrapper<>();
        updateWrapper2.set(SystemMenu::getSortOrder, systemMenu1.getSortOrder());
        updateWrapper2.eq(SystemMenu::getId, systemMenu2.getId());
        systemMenuMapper.update(updateWrapper2);
    }

    private List<SystemMenu> findExchangedMenu(@NotNull String fromMenuId, @NotNull String movingMode) {
        List<SystemMenu> resultMenus = new ArrayList<>();
        SystemMenu currentSystemMenu = systemMenuRepository.getById(fromMenuId);
        resultMenus.add(currentSystemMenu);

        List<SystemMenu> systemMenus;
        if (currentSystemMenu.getParentId() != null) {
            // 查询父目录菜单列表
            Long parentId = currentSystemMenu.getParentId();
            LambdaQueryWrapper<SystemMenu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SystemMenu::getParentId, parentId);
            queryWrapper.orderByAsc(SystemMenu::getSortOrder);
            systemMenus = systemMenuMapper.selectList(queryWrapper);
        } else {
            LambdaQueryWrapper<SystemMenu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SystemMenu::getLevel, 1);
            queryWrapper.orderByAsc(SystemMenu::getSortOrder);
            systemMenus = systemMenuMapper.selectList(queryWrapper);
        }

        if ("up".equals(movingMode)) {
            // 菜单上移
            SystemMenu previousMenu = null;
            for (SystemMenu menu : systemMenus) {
                if (menu.getId().equals(NumberUtils.parseLong(fromMenuId))) {
                    break;
                }
                previousMenu = menu;
            }
            resultMenus.add(previousMenu);
        } else if ("down".equals(movingMode)) {
            // 菜单下移
            SystemMenu nextMenu = null;
            boolean foundCurrent = false;
            for (SystemMenu menu : systemMenus) {
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
    public List<SystemMenu> getUpLevelSystemMenuList(String id) throws BusinessException {
        SystemMenu systemMenu = systemMenuRepository.getById(id);
        if (systemMenu == null || systemMenu.getLevel() - 1 == 0) {
            throw new BusinessException(BusinessErrorCode.PARAMETER_VALIDATION_ERROR);
        }

        LambdaQueryWrapper<SystemMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemMenu::getLevel, systemMenu.getLevel() - 1);
        queryWrapper.orderByAsc(SystemMenu::getSortOrder);
        List<SystemMenu> systemMenuList = systemMenuMapper.selectList(queryWrapper);

        return systemMenuList;

    }

    /**
     * 根据menuId获取菜单项
     *
     * @param menuId
     * @return
     * @throws BusinessException 业务异常
     */
    public SystemMenu getSystemMenuByMenuId(String menuId) throws BusinessException {
        if (StringUtils.isEmpty(menuId)) {
            throw new BusinessException(BusinessErrorCode.PARAMETER_VALIDATION_ERROR);
        }
        LambdaQueryWrapper<SystemMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemMenu::getMenuCode, menuId);
        queryWrapper.last("LIMIT 1");
        SystemMenu systemMenu = systemMenuRepository.getOne(queryWrapper);
        return systemMenu;
    }

    /**
     * 获取系统基准权限（所有用户均拥有的权限）
     *
     * @return
     */
    public List<SystemMenu> getZeroLevelMenuList() {
        LambdaQueryWrapper<SystemMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemMenu::getLevel, 0);
        queryWrapper.orderByAsc(SystemMenu::getSortOrder);
        List<SystemMenu> systemMenus = systemMenuMapper.selectList(queryWrapper);
        return systemMenus;
    }

    /**
     * 检测是否存在子菜单
     *
     * @param id
     * @return
     */
    public Boolean hasChildren(@NotNull Long id) {
        LambdaQueryWrapper<SystemMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemMenu::getParentId, id);
        long count = systemMenuRepository.count(queryWrapper);
        return count > 0;
    }

    /**
     * 根据菜单id获取其最后一个子菜单项
     *
     * @param id
     * @return
     */
    public SystemMenu getLastChild(@Nullable Long id) {
        LambdaQueryWrapper<SystemMenu> queryWrapper = new LambdaQueryWrapper<>();
        if (id == null) {
            queryWrapper.isNull(SystemMenu::getParentId);
        } else {
            queryWrapper.eq(SystemMenu::getParentId, id);
        }
        queryWrapper.orderByDesc(SystemMenu::getSortOrder);
        queryWrapper.last("LIMIT 1");
        SystemMenu systemMenu = systemMenuRepository.getOne(queryWrapper);
        return systemMenu;
    }

    /**
     * 导出菜单
     * <p>
     * （用于跨系统间的系统菜单同步，仅包含菜单属性及层级结构，不含主键id）
     *
     * @return
     */
    public String exportJson() {
        LambdaQueryWrapper<SystemMenu> qw = new LambdaQueryWrapper<>();
        // 按照 menuId 排序，尽量保证相同菜单比对时前后顺序一致
        qw.orderByAsc(SystemMenu::getMenuCode);
        List<SystemMenu> list = systemMenuRepository.list(qw);

        // 构造 id -> menuId 字典
        HashMap<Long, String> menuIdMap = new HashMap<>();
        for (SystemMenu menu : list) {
            menuIdMap.put(menu.getId(), menu.getMenuCode());
        }

        // parentMenuId -> [ {sequence, childId}, {sequence, childId}, ... ]
        HashMap<String, List<Pair<Integer, String>>> menuSequencePairMap = new HashMap<>();
        List<JSONObject> menuList = new ArrayList<>(list.size());
        for (SystemMenu menu : list) {
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
