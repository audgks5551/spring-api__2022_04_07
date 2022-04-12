package sbs.apidemo.user.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import sbs.apidemo.base.argumentresolver.dtotovo.DtoToVo;
import sbs.apidemo.base.argumentresolver.dtotovo.Response;
import java.lang.annotation.Annotation;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class TestRequestBodyAdvice implements ResponseBodyAdvice {

    private final ModelMapper modelMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        Annotation[] declaredAnnotations = returnType.getExecutable().getDeclaredAnnotations();
        for (Annotation declaredAnnotation : declaredAnnotations) {
            if (declaredAnnotation.annotationType() == DtoToVo.class) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        /**
         * ResponseEntity로 변형해서 넣기
         */
        Class<?> voClass = returnType.getExecutable().getAnnotation(DtoToVo.class).vo();
        HttpStatus status = returnType.getExecutable().getAnnotation(DtoToVo.class).status();

        Object vo = null;
        try {
            vo = modelMapper.map(body, voClass);
        } catch (Exception e) {
            /**
             * TODO 예외처리 필요
             */
            log.error("modelMapper null");
            return body;
        }

        /**
         * ResponseEntity 타입으로 변환
         */

        return new ResponseEntity<>("hello", status);
    }
}
