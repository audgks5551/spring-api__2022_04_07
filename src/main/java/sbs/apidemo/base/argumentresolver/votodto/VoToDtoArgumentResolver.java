package sbs.apidemo.base.argumentresolver.votodto;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.List;

/**
 * 1. ModelMapper에 의존중이므로 ModelMapper를 빈으로 설정
 * @Bean
 *     public ModelMapper modelMapper(){
 *         ModelMapper mapper = new ModelMapper();
 *         mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
 *         return mapper;
 *     }
 *
 * 2. @Autowired 사용
 *  package org.springframework.beans.factory.annotation
 *
 * 3. DtoToVo annotation 사용중
 * @Target({ElementType.PARAMETER})
 * @Retention(RetentionPolicy.RUNTIME)
 * public @interface VoToDto {
 *
 *  Class<?> vo();
 *  boolean required() default true;
 * }
 *
 * 4. RequestResponseBodyMethodProcessor 상속받음
 *  package org.springframework.web.servlet.mvc.method.annotation
 */

@Slf4j
public class VoToDtoArgumentResolver extends RequestResponseBodyMethodProcessor {

    private ModelMapper modelMapper;

    @Autowired
    public VoToDtoArgumentResolver(List<HttpMessageConverter<?>> converters, List<Object> requestResponseBodyAdvice, ModelMapper modelMapper) {
        super(converters, requestResponseBodyAdvice);
        this.modelMapper = modelMapper;
    }

    /**
     * 파라미터에 @Dto가 있는지 확인
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(VoToDto.class);
    }

    /**
     * returnType은 사용하지 않음
     * false 설정
     */
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        parameter = parameter.nestedIfOptional();

        /**
         * custom
         * VO class를 추출하여 parameter type 대신 VO type 대입
         */
        Class<?> voClass = findVoClass(parameter); // 클래스 추출
        Object vo = readWithMessageConverters(webRequest, parameter, voClass);
        String name = voClass.getSimpleName();

        /**
         * validation 검증
         */
        if (binderFactory != null) {
            WebDataBinder binder = binderFactory.createBinder(webRequest, vo, name);
            if (vo != null) {
                validateIfApplicable(binder, parameter);
                if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
                    throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
                }
            }
            if (mavContainer != null) {
                mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());
            }
        }

        /**
         * vo -> dto
         */
        Object dto = null;
        try {
           dto = modelMapper.map(vo, parameter.getNestedGenericParameterType());
        } catch (Exception e) {
            log.error("modelMapper null");
            /**
             * TODO 예외처리 필요
             */
            return adaptArgumentIfNecessary(null, parameter);
        }

        return adaptArgumentIfNecessary(dto, parameter);
    }

    /**
     * vo class 찾기
     */
    private Class<?> findVoClass(MethodParameter parameter) {
        VoToDto dto = parameter.getParameterAnnotation(VoToDto.class);
        return dto.vo();
    }

    /**
     * required() 체크
     */
    @Override
    protected boolean checkRequired(MethodParameter parameter) {
        VoToDto dto = parameter.getParameterAnnotation(VoToDto.class);
        return (dto != null && dto.required() && !parameter.isOptional());
    }
}
