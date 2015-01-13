package com.hhn.util.annotaion;

import java.lang.annotation.*;

/**
 * Created by lenovo on 2014/12/18.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER,ElementType.METHOD,ElementType.ANNOTATION_TYPE})
@Documented
public @interface ServiceLog {
    String description() default "";
}
