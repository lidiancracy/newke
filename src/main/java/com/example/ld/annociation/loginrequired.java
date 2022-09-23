package com.example.ld.annociation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName loginrequired
 * @Description TODO
 * @Date 2022/9/23 17:46
 */
@Target(ElementType.METHOD) //此注解可以写在方法上
@Retention(RetentionPolicy.RUNTIME) // 运行时生效
public @interface loginrequired {
}
