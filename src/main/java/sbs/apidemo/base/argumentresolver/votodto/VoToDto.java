package sbs.apidemo.base.argumentresolver.votodto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface VoToDto {

    /**
     * 필수조건
     * vo class 입력
     */
    Class<?> vo();

    boolean required() default true;
}
