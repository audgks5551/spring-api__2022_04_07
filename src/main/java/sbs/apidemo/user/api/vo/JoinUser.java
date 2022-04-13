package sbs.apidemo.user.api.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class JoinUser {
    @ApiModelProperty(position = 1, notes = "이메일", required = true)
    @NotNull(message = "Email cannot be null")
    private String email;

    @ApiModelProperty(position = 1, notes = "이메일", required = true)
    @NotNull(message = "Name cannot be null")
    private String name;

    @ApiModelProperty(position = 1, notes = "이메일", required = true)
    @NotNull(message = "password cannot be null")
    private String password;
}
