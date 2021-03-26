package com.its.light.controllers;

import com.its.light.DTO.User;
import com.its.light.tools.CountryDefiner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {

    private final CountryDefiner countryDefiner;

    UserController() {
        countryDefiner = new CountryDefiner();
    }

    @GetMapping("/api/users/current")
    User getCurrentUser(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        User user = new User();
        user.setIpAddress(ip);
        user.setCountry(countryDefiner.getCountry(ip));
        return user;
    }
}
