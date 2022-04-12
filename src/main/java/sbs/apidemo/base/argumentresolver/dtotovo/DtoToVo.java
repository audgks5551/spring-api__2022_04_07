package sbs.apidemo.base.argumentresolver.dtotovo;

import org.springframework.http.HttpStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.springframework.http.HttpStatus.OK;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DtoToVo {

    /**
     * 필수조건
     * vo class 입력
     */
    Class<?> vo();

    /**
     * 필수 조건
     * 상태 입력
     */
    HttpStatus status() default OK;
}
