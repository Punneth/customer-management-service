package com.albanero.controller;

import com.albanero.constants.Endpoint;
import com.albanero.pojo.User;
import com.albanero.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Endpoint.USER_MAPPING)
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public String addUser(@RequestBody User user) {

        userService.addUser(user);
        return null;
    }
}
