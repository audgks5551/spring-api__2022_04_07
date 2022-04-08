package sbs.apidemo.user.service;


import sbs.apidemo.user.api.dto.UserDto;

public interface UserService {
    UserDto doJoin(UserDto userDto);
    UserDto doLogin(UserDto userDto);
}
