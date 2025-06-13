package com.welab.k8s_backend_user.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SiteUserLoginDto {
    @NotBlank(message = "Enter userId")
    private String userId;

    @NotBlank(message = "Enter password")
    private String password;
}
