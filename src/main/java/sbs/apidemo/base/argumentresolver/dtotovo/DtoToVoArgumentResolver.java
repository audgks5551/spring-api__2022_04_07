package sbs.apidemo.base.argumentresolver.dtotovo;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.HttpEntityMethodProcessor;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 1. ModelMapper에 의존중이므로 ModelMapper를 빈으로 설정
 * @Bean
 *     public ModelMapper modelMapper(){
 *         ModelMapper mapper = new ModelMapper();
 *         mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
 *         return mapper;
 *     }
 *
 * 2. @Autowired
 *  package org.springframework.beans.factory.annotation
 *
 * 3. DtoToVo annotation 사용중
 * @Target({ElementType.METHOD})
 * @Retention(RetentionPolicy.RUNTIME)
 * public @interface DtoToVo {
 *
 *  Class<?> vo();
 *  HttpStatus status();
 * }
 *
 * 4. HttpEntityMethodProcessor 상속받음
 *  package org.springframework.web.servlet.mvc.method.annotation
 */

@Slf4j
public class DtoToVoArgumentResolver extends HttpEntityMethodProcessor {

    private ModelMapper modelMapper;

    @Autowired
    public DtoToVoArgumentResolver(List<HttpMessageConverter<?>> converters, ContentNegotiationManager manager, List<Object> requestResponseBodyAdvice, ModelMapper modelMapper) {
        super(converters, manager, requestResponseBodyAdvice);
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return false;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        if (returnType.getExecutable().getAnnotation(DtoToVo.class) != null) {
            return true;
        }

        return false;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {

        /**
         * ResponseEntity로 변형해서 넣기
         */
        Class<?> vo = returnType.getExecutable().getAnnotation(DtoToVo.class).vo();
        HttpStatus status = returnType.getExecutable().getAnnotation(DtoToVo.class).status();

        Object body = null;
        try {
            body = modelMapper.map(returnValue, vo);
        } catch (Exception e) {
            /**
             * TODO 예외처리 필요
             */
            log.error("modelMapper null");
            return;
        }

        /**
         * ResponseEntity 타입으로 변환
         */
        returnValue = ResponseEntity.status(status).body(Response.put(body, null));

        mavContainer.setRequestHandled(true);
        if (returnValue == null) {
            return;
        }

        ServletServerHttpRequest inputMessage = createInputMessage(webRequest);
        ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);

        Assert.isInstanceOf(HttpEntity.class, returnValue);
        HttpEntity<?> responseEntity = (HttpEntity<?>) returnValue;

        HttpHeaders outputHeaders = outputMessage.getHeaders();
        HttpHeaders entityHeaders = responseEntity.getHeaders();
        if (!entityHeaders.isEmpty()) {
            entityHeaders.forEach((key, value) -> {
                if (HttpHeaders.VARY.equals(key) && outputHeaders.containsKey(HttpHeaders.VARY)) {
                    List<String> values = getVaryRequestHeadersToAdd(outputHeaders, entityHeaders);
                    if (!values.isEmpty()) {
                        outputHeaders.setVary(values);
                    }
                }
                else {
                    outputHeaders.put(key, value);
                }
            });
        }

        if (responseEntity instanceof ResponseEntity) {
            int returnStatus = ((ResponseEntity<?>) responseEntity).getStatusCodeValue();
            outputMessage.getServletResponse().setStatus(returnStatus);
            if (returnStatus == 200) {
                HttpMethod method = inputMessage.getMethod();
                if ((HttpMethod.GET.equals(method) || HttpMethod.HEAD.equals(method))
                        && isResourceNotModified(inputMessage, outputMessage)) {
                    outputMessage.flush();
                    return;
                }
            }
            else if (returnStatus / 100 == 3) {
                String location = outputHeaders.getFirst("location");
                if (location != null) {
                    saveFlashAttributes(mavContainer, webRequest, location);
                }
            }
        }

        // Try even with null body. ResponseBodyAdvice could get involved.
        writeWithMessageConverters(responseEntity.getBody(), returnType, inputMessage, outputMessage);

        // Ensure headers are flushed even if no body was written.
        outputMessage.flush();
    }

    private List<String> getVaryRequestHeadersToAdd(HttpHeaders responseHeaders, HttpHeaders entityHeaders) {
        List<String> entityHeadersVary = entityHeaders.getVary();
        List<String> vary = responseHeaders.get(HttpHeaders.VARY);
        if (vary != null) {
            List<String> result = new ArrayList<>(entityHeadersVary);
            for (String header : vary) {
                for (String existing : StringUtils.tokenizeToStringArray(header, ",")) {
                    if ("*".equals(existing)) {
                        return Collections.emptyList();
                    }
                    for (String value : entityHeadersVary) {
                        if (value.equalsIgnoreCase(existing)) {
                            result.remove(value);
                        }
                    }
                }
            }
            return result;
        }
        return entityHeadersVary;
    }

    private boolean isResourceNotModified(ServletServerHttpRequest request, ServletServerHttpResponse response) {
        ServletWebRequest servletWebRequest =
                new ServletWebRequest(request.getServletRequest(), response.getServletResponse());
        HttpHeaders responseHeaders = response.getHeaders();
        String etag = responseHeaders.getETag();
        long lastModifiedTimestamp = responseHeaders.getLastModified();
        if (request.getMethod() == HttpMethod.GET || request.getMethod() == HttpMethod.HEAD) {
            responseHeaders.remove(HttpHeaders.ETAG);
            responseHeaders.remove(HttpHeaders.LAST_MODIFIED);
        }

        return servletWebRequest.checkNotModified(etag, lastModifiedTimestamp);
    }

    private void saveFlashAttributes(ModelAndViewContainer mav, NativeWebRequest request, String location) {
        mav.setRedirectModelScenario(true);
        ModelMap model = mav.getModel();
        if (model instanceof RedirectAttributes) {
            Map<String, ?> flashAttributes = ((RedirectAttributes) model).getFlashAttributes();
            if (!CollectionUtils.isEmpty(flashAttributes)) {
                HttpServletRequest req = request.getNativeRequest(HttpServletRequest.class);
                HttpServletResponse res = request.getNativeResponse(HttpServletResponse.class);
                if (req != null) {
                    RequestContextUtils.getOutputFlashMap(req).putAll(flashAttributes);
                    if (res != null) {
                        RequestContextUtils.saveOutputFlashMap(location, req, res);
                    }
                }
            }
        }
    }
}
