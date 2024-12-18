package com.servPet.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .csrf(csrf -> csrf
                .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/front_end/login"))
                .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/front_end/register"))
//                .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/logout"))
            )
        .authorizeHttpRequests((requests) -> requests
                .antMatchers("/back_end/apply/**","/**","/cs-issues/list","/login","/back_end/login","/css/**", "/images/**", "/javascript/**", "/jquery/**", "/slick/**", "/front_end/**","/templates","/logout").permitAll()
                .antMatchers().authenticated()
                .anyRequest().authenticated()
            
            )
            
            .formLogin((form) -> form
                .loginPage("/front_end/login")
                .usernameParameter("mebMail") // 自定義用戶名字段
                .passwordParameter("mebPwd") // 自定義密碼字段
                .defaultSuccessUrl("/")
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
                
            )
            .exceptionHandling()
            .authenticationEntryPoint((request, response, authException) -> {
                // 返回 JSON 格式的未授權錯誤
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"status\":\"error\",\"message\":\"未授權的請求，請先登入\"}");
            })            .and()
            .sessionManagement()
            .sessionFixation().migrateSession() ;// 會話固定攻擊防護
//            .invalidSessionUrl("/"); // 會話過期處理
        
        
      
        return http.build();
    }
    
   
    
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
