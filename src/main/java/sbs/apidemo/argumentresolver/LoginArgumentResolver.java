package sbs.apidemo.argumentresolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import sbs.apidemo.session.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LoginArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * @login 어노테이션 있는지 확인 및 어노테이션 클래스 확인
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
        boolean hasMemberType = LoginUser.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginAnnotation && hasMemberType;
    }

    /**
     * supportsParameter이 참일 때 실행
     * session 정보로 로그인 유저 찾기
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest(); // HttpServletRequest 가져오기
        HttpSession session = request.getSession(false); // 세션 가져오고 없을 시 생성 x
        if (session == null) {
            // 세션 없으면 null 반환
            return null;
        }

        return session.getAttribute(SessionConst.LOGIN_MEMBER); // 로그인 유저 반환
    }
}
