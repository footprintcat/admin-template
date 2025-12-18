package com.example.backend.controller.manage.v1.system.dto.request.identity;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ManageSystemIdentitySwitchRequest {

    @NotNull(message = "身份id为空")
    private Long identityId;

}
