package sbs.apidemo.base.argumentresolver.dto;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
import sbs.apidemo.base.argumentresolver.Vo.DtoToVo;

import java.util.List;

@Slf4j
public class DtoArgumentResolver extends RequestResponseBodyMethodProcessor {

    private ModelMapper modelMapper;

    @Autowired
    public DtoArgumentResolver(
            List<HttpMessageConverter<?>> converters,
//            List<Object> requestResponseBodyAdvice,
            ModelMapper modelMapper) {
        super(converters);
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
     * 아직은 필요없음
     */
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return (AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), DtoToVo.class) ||
                returnType.hasMethodAnnotation(DtoToVo.class));
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
        Object dto = modelMapper.map(vo, parameter.getNestedGenericParameterType());

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
