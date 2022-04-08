package sbs.apidemo.argumentresolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import sbs.apidemo.session.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

@Slf4j
public class DtoArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * @login 어노테이션 있는지 확인 및 어노테이션 클래스 확인
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Dto.class);
        boolean hasObjectType = Object.class.isAssignableFrom(parameter.getParameterType());
        return hasLoginAnnotation && hasObjectType;
    }

    /**
     * supportsParameter이 참일 때 실행
     * session 정보로 로그인 유저 찾기
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Class<?> DtoType = parameter.getParameterType();
        log.info(String.valueOf(DtoType));
        AnnotatedElement annotatedElement = parameter.getAnnotatedElement();

        return null;
    }
}
