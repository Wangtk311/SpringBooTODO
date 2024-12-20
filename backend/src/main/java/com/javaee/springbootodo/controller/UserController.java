package com.javaee.springbootodo.controller;

import com.javaee.springbootodo.entity.UserEntity;
import com.javaee.springbootodo.security.JwtUtils;
import com.javaee.springbootodo.service.UserService;
import com.javaee.springbootodo.security.SpringSecurityConfig;
import com.javaee.springbootodo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "http://localhost:5173/")  // Allows CORS requests from the specified origin
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody UserEntity user) {
        System.out.println(user.toString());
        // 2. 对密码进行加密
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        UserEntity savedUser = userService.save(user);

        // 创建响应消息
        Map<String, Object> response = new HashMap<>();
        response.put("message", "注册成功");
        response.put("id", savedUser.getId());

        return ResponseEntity.ok().body(response);
    }

//    @PostMapping("/query")
//    public UserEntity queryUser(int id) {
//
//        System.out.println(id);
//        return userService.findById(id);
//    }

    @PostMapping("/login")
    public  ResponseEntity<String> login(@RequestBody LoginRequest lrq) {
        System.out.println("登录校验");
        System.out.println(lrq.getId());

        UserEntity user = userService.findById(lrq.getId());
        if(user == null) {
            System.out.println("用户不存在");
            return new ResponseEntity<>("用户不存在", HttpStatus.NOT_FOUND); // 状态码 404 用户不存在
        }
        String pswd = userService.getPassword(user);

        if(passwordEncoder.matches(lrq.getPassword(), pswd)) {
            System.out.println("密码正确");
            try {
                System.out.println("登录校验中");
                Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(lrq.getId(), lrq.getPassword()));
                // 生成 JWT Token
                String token = JwtUtils.generateTokenAsymmetric(String.valueOf(lrq.getId()));
                return new ResponseEntity<>(token, HttpStatus.OK);  // 状态码 200 登录成功
            }catch (Exception e) {
                System.out.println("登录校验失败");
                // 返回状态码 500 (Internal Server Error) 其他异常
                return new ResponseEntity<>("登录校验失败", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else {
            // 返回状态码 401 (Unauthorized) 密码错误
            return new ResponseEntity<>("密码错误", HttpStatus.UNAUTHORIZED);
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