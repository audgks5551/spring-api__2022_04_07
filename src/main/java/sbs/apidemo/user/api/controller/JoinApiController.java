package sbs.apidemo.user.api.controller;

import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value = "고객 아이디를 통한 고객 조회", notes = "고객 아이디로 고객 정보를 조회합니다.\n삭제된 고객은 조회에서 제외됩니다.")
    @PostMapping
    @DtoToVo(vo = ResponseUser.class, status = CREATED)
    public UserDto doJoin(
            @VoToDto(vo = JoinUser.class) @Valid UserDto userDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return null;
        }

        UserDto savedUserDto = userService.doJoin(userDto);

        return savedUserDto;
    }
}
