package sbs.apidemo.api.auth.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginUser {
    private String email;

    private String password;
}
