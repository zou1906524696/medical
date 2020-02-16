package com.zzf.medical.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RegisterReq {
    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty("用户名")
    private String username;
    @NotBlank(message = "邮箱不能为空")
    @ApiModelProperty("邮箱")
    private String email;
    @NotBlank(message = "密码不能为空")
    @ApiModelProperty("密码")
    private String password;
}
