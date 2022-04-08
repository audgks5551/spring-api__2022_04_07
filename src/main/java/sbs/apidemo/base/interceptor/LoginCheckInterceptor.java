package sbs.apidemo.base.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import sbs.apidemo.base.session.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("로그인 여부 확인 시작");

        HttpSession session = request.getSession(false);

        /**
         * 세션이 없거나 로그인 세션이 없을 때 false
         */
        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            log.info("로그인 필요");
            return false;
        }

        return true;
    }
}
