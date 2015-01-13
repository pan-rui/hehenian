package com.hhn.util.annotaion;

import java.lang.annotation.*;

/**
 * Created by lenovo on 2014/12/18.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE,ElementType.METHOD,ElementType.PARAMETER})
@Documented
public @interface ControllerLog {
    String description() default "";
}
