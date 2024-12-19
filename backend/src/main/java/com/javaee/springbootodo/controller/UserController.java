package com.javaee.springbootodo.controller;

import com.javaee.springbootodo.entity.UserEntity;
import com.javaee.springbootodo.security.JwtUtils;
import com.javaee.springbootodo.service.UserService;
import com.javaee.springbootodo.security.SpringSecurityConfig;
import com.javaee.springbootodo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserEntity user) {
        System.out.println(user.toString());
        // 2. 对密码进行加密
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userService.save(user);
        // 4. 返回成功消息
        return ResponseEntity.ok("注册成功");
    }

    @PostMapping("/query")
    public UserEntity queryUser(@RequestBody int id) {
        System.out.println(id);
        return userService.findById(id);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest lrq) {
        System.out.println("登录校验");
        System.out.println(lrq.getId());

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(lrq.getId(),lrq.getPassword()));
            return JwtUtils.generateTokenAsymmetric(String.valueOf(lrq.getId()));
        } catch (Exception e) {
            System.out.println("登录校验失败");
            throw new RuntimeException("Invalid credentials");
        }

    }
}

class LoginRequest {
    private int id;
    private String password;

    // Getter 和 Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}