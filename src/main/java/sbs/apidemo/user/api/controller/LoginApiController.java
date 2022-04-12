package sbs.apidemo.user.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sbs.apidemo.base.argumentresolver.dtotovo.Response;
import sbs.apidemo.user.api.dto.UserDto;
import sbs.apidemo.user.api.vo.LoginUser;
import sbs.apidemo.user.api.vo.ResponseUser;
import sbs.apidemo.base.argumentresolver.votodto.VoToDto;
import sbs.apidemo.user.service.UserService;
import sbs.apidemo.base.session.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LoginApiController {

    private final ModelMapper mapper;
    private final UserService userService;

    /**
     * 로그인
     */
    @PostMapping
    public ResponseEntity<?> doLogin(
            @Valid @VoToDto(vo = LoginUser.class) UserDto userDto,
            HttpServletRequest request,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(BAD_REQUEST).body(bindingResult.getAllErrors());
        }

        UserDto loginUser = userService.doLogin(userDto);
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginUser);

        ResponseUser responseUser = mapper.map(loginUser, ResponseUser.class);

        log.info("{} 회원님이 로그인되었습니다.", responseUser.getEmail());
        return ResponseEntity.status(OK).body(
                Response.put(responseUser, null)
        );
    }

}
