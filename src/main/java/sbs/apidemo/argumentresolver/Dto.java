package sbs.apidemo.argumentresolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Dto {

    /**
     * 필수조건
     * vo class 입력
     */
    Class<?> vo();

    boolean required() default true;
}
