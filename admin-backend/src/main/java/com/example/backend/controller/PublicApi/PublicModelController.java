package com.example.backend.controller.PublicApi;

import com.example.backend.common.Response.CommonReturnType;
import com.example.backend.controller.base.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 模型公开接口
 */
@Tag(name = "模型公开接口")
@CrossOrigin
@RestController
@RequestMapping("/v3/public/model")
public class PublicModelController extends BaseController {

    @Value("${project-config.site-id}")
    private String siteId;

    // @Resource
    // private ModelInfoService modelInfoService;
    //
    // /**
    //  * 获取 Cesium 默认三维模型地址 (primitivesUrl)
    //  * <p>
    //  * 示例: //cos-model.mine.footprintcat.com/chilong/20250325-b3dm/tileset.json
    //  *
    //  * @return
    //  */
    // @GetMapping("/getDefaultPrimitivesUrl")
    // public CommonReturnType getDefaultPrimitivesUrl() {
    //     @Nullable ModelInfo modelInfo = modelInfoService.getScreenDefaultModelInfo();
    //     if (modelInfo == null) {
    //         return CommonReturnType.success(null);
    //     }
    //
    //     String url = "//" + modelHost + "/" + siteId + "/" + modelInfo.getPath() + "/tileset.json";
    //     return CommonReturnType.success(url);
    // }

}
