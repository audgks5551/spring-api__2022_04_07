package sbs.apidemo.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import sbs.apidemo.session.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession(false);

        /**
         * 세션이 없거나 log
         */
        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            return false;
        }

        return true;
    }
}
