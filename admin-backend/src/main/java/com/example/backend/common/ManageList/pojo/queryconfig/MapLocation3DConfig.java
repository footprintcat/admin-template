package com.example.backend.common.ManageList.pojo.queryconfig;

import com.example.backend.common.ManageList.pojo.queryconfig.base.BaseQueryConfig;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 3D地图位置查询配置类
 * 用于配置3D地图位置类型查询条件的额外选项
 */
@Data
@Accessors(chain = true)
public class MapLocation3DConfig extends BaseQueryConfig<MapLocation3DConfig> {
    // 地图中心点纬度
    private double lat;
    // 地图中心点经度
    private double lng;
    // 地图中心点高度
    private double alt;
    // 地图缩放级别
    private int zoom = 12;

    @Override
    public boolean getIsValid() {
        // 检查经纬度是否在有效范围内
        if (lat < -90 || lat > 90) {
            return false;
        }
        if (lng < -180 || lng > 180) {
            return false;
        }
        if (zoom < 0 || zoom > 20) {
            return false;
        }
        return true;
    }

    @Override
    protected MapLocation3DConfig self() {
        return this; // 返回当前子类实例
    }
}