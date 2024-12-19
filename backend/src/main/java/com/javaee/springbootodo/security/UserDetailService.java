package com.javaee.springbootodo.security;

import com.javaee.springbootodo.entity.UserEntity;
import com.javaee.springbootodo.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailService implements UserDetailsService {
    @Resource
    private UserRepository userServiceRepo;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserEntity userEntity = userServiceRepo.getUserById(Integer.parseInt(s));
        return new UserDetails() {
            // 用户被授予的所有权限集合
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                Set<GrantedAuthority> authorities = new HashSet<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                return authorities;
            }

            @Override
            public String getPassword() {
                return userEntity.getPassword();
            }

            @Override
            public String getUsername() {
                return String.valueOf(userEntity.getId());
            }

            // 检查用户账号是否过期
            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            // 检查用户账户是否锁住
            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            // 检查用户凭据是否过期
            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            // 检查用户是否被启用
            @Override
            public boolean isEnabled() {
                return true;
            }
        };
    }
}
