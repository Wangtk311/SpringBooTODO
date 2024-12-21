package com.javaee.springbootodo.security;

import com.javaee.springbootodo.security.JwtUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;



@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailService userDetailsService;

    // 通过构造函数注入UserDetailsService
    public JwtAuthenticationFilter(UserDetailService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        List<String> allowedURI = Arrays.asList("/user/login", "/user/register","/swagger-ui/","/user/register");
        //System.out.println(request.getRequestURI());
        if (allowedURI.contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            //System.out.println("request.getRequestURI()True");
            return; // 放行后直接返回
        }
        //System.out.println("header.startsWith(\"Bearer \")--------------");
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            //System.out.println("header.startsWith(\"Bearer \")");
            String token = header.substring(7); // 去除"Bearer "前缀

            if (JwtUtils.validateTokenAsymmetric(token)) {
                String username = JwtUtils.getUsernameFromTokenAsymmetric(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token无效或过期");
                return;
            }
            filterChain.doFilter(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "用户未登录");
        }
    }
}
