package dev.thrako.receptaria.web.interceptor;

import dev.thrako.receptaria.common.constant.ContextAuthority;
import dev.thrako.receptaria.common.constant.ContextRole;
import dev.thrako.receptaria.common.security.CurrentUser;
import dev.thrako.receptaria.service.RecipeService;
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

    private final RecipeService recipeService;
    private final ContextAuthChecker contextAuthChecker;

    public ContextAuthorizationInterceptor (RecipeService recipeService, ContextAuthChecker contextAuthChecker) {

        this.recipeService = recipeService;
        this.contextAuthChecker = contextAuthChecker;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        var pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        long recipeId;
        try {
            recipeId = Long.parseLong((String) pathVariables.get("id"));
        } catch (NumberFormatException ignored) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }

        var currentUser = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Arrays.stream(ContextAuthority.values())
                .forEach(authority -> currentUser
                        .getContextAuthorities()
                        .put(authority, contextAuthChecker.check(authority, currentUser, recipeId)));

        if (Boolean.FALSE == currentUser.has(ContextAuthority.VIEW)) {

            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        //TODO refactor with stream
        for (final ContextRole contextRole : ContextRole.values()) {
            currentUser.getContextRoles().put(contextRole, contextAuthChecker.check(contextRole, currentUser, recipeId));
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

}
