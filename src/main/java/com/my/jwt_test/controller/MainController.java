package com.my.jwt_test.controller;

import com.my.jwt_test.dto.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Iterator;

@RestController
// @Controller + @ResponseBody
public class MainController {
    @GetMapping
    public String mainPage() {
        // 현재 로그인 된 객체 받아오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Role 가져오기
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        // UserDetails에서 E-mail 정보 가져오기
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String email = userDetails.getEmail();
        return "Main Controller : name = " + email + ", role = " + role;
    }
}
