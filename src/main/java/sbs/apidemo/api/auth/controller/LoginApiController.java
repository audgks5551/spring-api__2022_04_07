package sbs.apidemo.api.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sbs.apidemo.api.Response;
import sbs.apidemo.api.auth.dto.UserDto;
import sbs.apidemo.api.auth.vo.RequestUser;
import sbs.apidemo.api.auth.vo.ResponseUser;
import sbs.apidemo.service.UserService;
import sbs.apidemo.session.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
    public ResponseEntity<Response> doLogin(@RequestBody RequestUser user, HttpServletRequest request) {

        UserDto userDto = mapper.map(user, UserDto.class);

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
