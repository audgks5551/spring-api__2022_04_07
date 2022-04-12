package sbs.apidemo.user.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import sbs.apidemo.base.argumentresolver.dtotovo.DtoToVo;
import sbs.apidemo.base.argumentresolver.votodto.VoToDto;
import sbs.apidemo.user.api.dto.UserDto;
import sbs.apidemo.user.api.vo.JoinUser;
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
    @DtoToVo(vo = ResponseUser.class, status = CREATED)
    public UserDto doJoin(
            @Valid @VoToDto(vo = JoinUser.class) UserDto userDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return null;
        }

        UserDto savedUserDto = userService.doJoin(userDto);

        return savedUserDto;
    }
}
