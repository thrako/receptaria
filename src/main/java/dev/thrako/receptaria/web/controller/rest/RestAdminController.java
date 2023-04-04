package dev.thrako.receptaria.web.controller.rest;

import dev.thrako.receptaria.common.error.exception.IllegalTargetException;
import dev.thrako.receptaria.common.message.Message;
import dev.thrako.receptaria.model.security.CurrentUser;
import dev.thrako.receptaria.model.entity.user.dto.UserVM;
import dev.thrako.receptaria.common.error.exception.NotAuthorizedException;
import dev.thrako.receptaria.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RestAdminController {

    private final UserService userService;

    @Autowired
    public RestAdminController (UserService userService) {

        this.userService = userService;
    }

    @GetMapping("api/admin/users")
    public List<UserVM> getUsers () {

        return this.userService.getAllUsersVM();
    }

    @PostMapping("/api/admin/users/{userId}/activate")
    public ResponseEntity<Object> activateUser (@AuthenticationPrincipal CurrentUser requester,
                                                @PathVariable Long userId) {

        this.userService.activate(userId, requester);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PostMapping("/api/admin/users/{userId}/inactivate")
    public ResponseEntity<Object> inactivateUser (@AuthenticationPrincipal CurrentUser requester,
                                                  @PathVariable Long userId) {

        this.userService.inactivate(userId, requester);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PostMapping("/api/admin/moderators/{userId}/promote")
    public ResponseEntity<Object> promoteModerator (@AuthenticationPrincipal CurrentUser requester,
                                                    @PathVariable Long userId) {

        this.userService.promoteModerator(userId, requester);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PostMapping("/api/admin/moderators/{userId}/demote")
    public ResponseEntity<Object> demoteModerator (@AuthenticationPrincipal CurrentUser requester,
                                                   @PathVariable Long userId) {

        this.userService.demoteModerator(userId, requester);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @ExceptionHandler({NotAuthorizedException.class, IllegalTargetException.class})
    public ResponseEntity<Object> handleUnauthorized (RuntimeException e) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Message.from(e.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleConflict (IllegalStateException e) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Message.from(e.getMessage()));
    }

}
