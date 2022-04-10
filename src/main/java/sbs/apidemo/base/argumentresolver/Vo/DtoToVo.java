package sbs.apidemo.base.argumentresolver.Vo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;


@Retention(RetentionPolicy.RUNTIME)
public @interface DtoToVo {

    /**
     * 필수조건
     * vo class 입력
     */
    Class<?> vo();

    boolean required() default true;
}
