package com.lita.concurrency.annoations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface Wanglei {
    String value() default "";
}
