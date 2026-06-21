package com.trainshier.security;

import com.trainshier.enums.UserRole;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresRole {

    /**
     * @param value allowed roles
     */
    UserRole[] value();
}