package sbs.apidemo.argumentresolver;

import javax.validation.constraints.NotNull;

public class LoginUser {

    @NotNull(message = "Email cannot be null")
    private String email;

    @NotNull(message = "password cannot be null")
    private String password;
}
