package sbs.apidemo.user.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sbs.apidemo.base.api.Response;
import sbs.apidemo.base.argumentresolver.Vo.DtoToVo;
import sbs.apidemo.user.api.dto.UserDto;
import sbs.apidemo.user.api.vo.JoinUser;
import sbs.apidemo.base.argumentresolver.dto.VoToDto;
import sbs.apidemo.user.api.vo.ResponseUser;
import sbs.apidemo.user.service.UserService;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@Controller
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class JoinApiController {

    private final ModelMapper mapper;
    private final UserService userService;

    /**
     * 회원가입
     */
    @PostMapping
    public ResponseEntity doJoin(
            @Valid @VoToDto(vo = JoinUser.class) UserDto userDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return null;
        }

        UserDto savedUserDto = userService.doJoin(userDto);

        ResponseUser responseUser = mapper.map(savedUserDto, ResponseUser.class);

        log.info("유저가 생성되었습니다. 유저 email ={}", responseUser.getEmail());
        return ResponseEntity.status(CREATED)
                .body(Response.put(responseUser, null));

//        return responseUser;
    }

}
