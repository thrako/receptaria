package dev.thrako.receptaria.web.interceptor;

import dev.thrako.receptaria.common.constant.ContextAuthority;
import dev.thrako.receptaria.common.constant.ContextRole;
import dev.thrako.receptaria.model.security.CurrentUser;
import dev.thrako.receptaria.service.utility.ContextAuthChecker;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Arrays;
import java.util.Map;

public class ContextAuthorizationInterceptor implements HandlerInterceptor {

    private final ContextAuthChecker contextAuthChecker;

    public ContextAuthorizationInterceptor (ContextAuthChecker contextAuthChecker) {

        this.contextAuthChecker = contextAuthChecker;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        //noinspection rawtypes
        var pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        long recipeId;
        try {
            recipeId = Long.parseLong((String) pathVariables.get("id"));
        } catch (NumberFormatException ignored) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }

        CurrentUser currentUser = (CurrentUser) SecurityContextHolder
                                                        .getContext()
                                                        .getAuthentication()
                                                        .getPrincipal();

        Arrays.stream(ContextAuthority.values())
                .forEach(contextAuthority -> currentUser
                        .put(contextAuthority, contextAuthChecker
                                                        .check(contextAuthority, currentUser, recipeId)));

        if (Boolean.FALSE == currentUser.has(ContextAuthority.VIEW)) {

            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Arrays.stream(ContextRole.values())
                .forEach(contextRole -> currentUser
                        .put(contextRole, contextAuthChecker
                                                  .check(contextRole, currentUser, recipeId)));

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

}
