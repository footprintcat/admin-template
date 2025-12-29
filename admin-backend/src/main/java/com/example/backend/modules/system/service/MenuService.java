package com.example.backend.modules.system.service;


import com.example.backend.modules.system.model.converter.MenuConverter;
import com.example.backend.modules.system.model.dto.MenuDto;
import com.example.backend.modules.system.model.entity.Menu;
import com.example.backend.modules.system.repository.MenuRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class MenuService {

    @Resource
    private MenuConverter menuConverter;
    @Resource
    private MenuRepository menuRepository;

    public @NotNull @Unmodifiable List<MenuDto> getMenuListById(@NotNull Collection<Long> menuIdList) {
        if (menuIdList.isEmpty()) {
            return Collections.emptyList();
        }
        List<Menu> menus = menuRepository.listByIds(menuIdList);
        return menuConverter.toDto(menus);
    }
}
