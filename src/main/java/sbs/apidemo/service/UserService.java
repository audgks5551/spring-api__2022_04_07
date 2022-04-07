package sbs.apidemo.service;


import sbs.apidemo.apiv1.dto.UserDto;

public interface UserService {
    UserDto doJoin(UserDto userDto);
}
