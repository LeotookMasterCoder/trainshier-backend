package com.trainshier.security;

import com.trainshier.enums.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

@Component
public class RoleInterceptor implements HandlerInterceptor {

    /**
     * @param request http request
     * @param response http response
     * @param handler controller handler
     * @return access result
     * @throws Exception exception
     */
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {

        if (!(handler instanceof HandlerMethod method)) {
            return true;
        }

        RequiresRole annotation =
                method.getMethodAnnotation(RequiresRole.class);

        if (annotation == null) {
            annotation =
                    method.getBeanType()
                            .getAnnotation(RequiresRole.class);
        }

        if (annotation == null) {
            return true;
        }

        Object roleObject =
                request.getAttribute("role");

        if (roleObject == null) {

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            response.setContentType("application/json");

            response.getWriter().write(
                    "{\"message\":\"Unauthorized\"}"
            );

            return false;
        }

        UserRole userRole =
                UserRole.valueOf(
                        roleObject.toString()
                );

        boolean allowed =
                Arrays.stream(annotation.value())
                        .anyMatch(role -> role == userRole);

        if (!allowed) {

            response.setStatus(
                    HttpServletResponse.SC_FORBIDDEN
            );

            response.setContentType("application/json");

            response.getWriter().write(
                    "{\"message\":\"Access denied\"}"
            );

            return false;
        }

        return true;
    }
}