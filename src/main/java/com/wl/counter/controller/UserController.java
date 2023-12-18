package com.wl.counter.controller;

import com.wl.counter.datamodel.dto.UserResponse;
import com.wl.counter.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{login}")
    public ResponseEntity<UserResponse> getUserByLogin(@PathVariable String login) {
        UserResponse response = userService.getUserByLogin(login);
        return ResponseEntity.ok(response);
    }
}
