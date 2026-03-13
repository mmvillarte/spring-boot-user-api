package org.exercise.user.api.controller;

import org.exercise.user.api.model.UserResult;
import org.exercise.user.application.query.service.UserQueryService;
import org.exercise.user.application.query.model.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserQueryController {
    private final UserQueryService userQueryService;

    public UserQueryController(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    @GetMapping
    public ResponseEntity<UserResult<UserDTO>> findById(@RequestParam UUID id) {
        UserDTO userDTO = userQueryService.findById(id);

        if(userDTO != null) {
            return ResponseEntity.ok(new UserResult<>(200, "User Found for provided id", userDTO));
        } else {
            return ResponseEntity.ok(new UserResult<>(404, "User Not Found for provided id", null));
        }
    }
}
