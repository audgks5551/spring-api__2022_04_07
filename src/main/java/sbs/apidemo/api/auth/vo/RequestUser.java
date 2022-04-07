package sbs.apidemo.api.auth.vo;


import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RequestUser {
    @NotNull(message = "Email cannot be null")
    private String email;

    @NotNull(message = "Name cannot be null")
    private String name;

    @NotNull(message = "password cannot be null")
    private String password;
}
