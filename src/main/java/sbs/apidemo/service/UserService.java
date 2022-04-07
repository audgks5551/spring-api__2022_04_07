package sbs.apidemo.service;


import sbs.apidemo.api.auth.dto.UserDto;

public interface UserService {
    UserDto doJoin(UserDto userDto);
    UserDto doLogin(UserDto userDto);
}
