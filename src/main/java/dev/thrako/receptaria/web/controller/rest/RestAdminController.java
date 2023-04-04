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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestAdminController {

    private final UserService userService;

    @Autowired
    public RestAdminController (UserService userService) {

        this.userService = userService;
    }

    @GetMapping("api/admin/users")
    public List<UserVM> getUsers() {

        return this.userService.getAllUsersVM();
    }

    @PostMapping("/api/admin/users/{userId}/activate")
    public ResponseEntity<Object> activateUser (@AuthenticationPrincipal CurrentUser requester,
                                                @PathVariable Long userId) {

        try {

            this.userService.activate(userId, requester);
        } catch (NotAuthorizedException | IllegalTargetException e) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Message.from(e.getMessage()));

        } catch (IllegalStateException e) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Message.from(e.getMessage()));
        }

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PostMapping("/api/admin/users/{userId}/inactivate")
    public ResponseEntity<Object> inactivateUser (@AuthenticationPrincipal CurrentUser requester,
                                  @PathVariable Long userId) {

        try {

            this.userService.inactivate(userId, requester);

        } catch (NotAuthorizedException | IllegalTargetException e) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Message.from(e.getMessage()));

        } catch (IllegalStateException e) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Message.from(e.getMessage()));
        }

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PostMapping("/api/admin/moderators/{userId}/promote")
    public ResponseEntity<Object> promoteModerator (@AuthenticationPrincipal CurrentUser requester,
                                                    @PathVariable Long userId) {

        try {

            this.userService.promoteModerator(userId, requester);

        } catch (NotAuthorizedException | IllegalTargetException e) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Message.from(e.getMessage()));

        } catch (IllegalStateException e) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Message.from(e.getMessage()));
        }

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PostMapping("/api/admin/moderators/{userId}/demote")
    public ResponseEntity<Object> demoteModerator (@AuthenticationPrincipal CurrentUser requester,
                                                   @PathVariable Long userId) {

        try {

            this.userService.demoteModerator(userId, requester);

        } catch (NotAuthorizedException | IllegalTargetException e) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Message.from(e.getMessage()));

        } catch (IllegalStateException e) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Message.from(e.getMessage()));
        }

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }



}
