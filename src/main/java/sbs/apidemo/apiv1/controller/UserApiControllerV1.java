package sbs.apidemo.apiv1.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sbs.apidemo.apiv1.ResponseV1;
import sbs.apidemo.apiv1.dto.UserDto;
import sbs.apidemo.apiv1.vo.RequestUser;
import sbs.apidemo.apiv1.vo.ResponseUser;
import sbs.apidemo.service.UserService;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserApiControllerV1 {

    private final ModelMapper mapper;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ResponseV1> doJoin(@RequestBody RequestUser user) {

        UserDto savedUserDto = userService.doJoin(mapper.map(user, UserDto.class)); // 유저 생성 서비스에 전달

        ResponseUser responseUser = mapper.map(savedUserDto, ResponseUser.class); // UserDto -> ResponseUser

        log.info("유저가 생성되었습니다. 유저 email ={}", responseUser.getEmail());
        return ResponseEntity.status(CREATED).body(
                ResponseV1.put(responseUser, null)
        );
    }
}
