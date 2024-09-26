package com.minch.config;

import com.minch.SecurityDetailsImpl.CustomUserDetailsServiceImpl;
import com.minch.component.CustomAuthenticationSuccessHandler;
import com.minch.component.CustomLogoutSuccessHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig{

    private final CustomUserDetailsServiceImpl userDetailsService;
    @Autowired
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;

    public SecurityConfig(CustomUserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailsService;
    }

    // 使用 BCrypt 来对密码进行加密
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable) // 关闭 CSRF，实验中 HTTP Basic 身份认证无需防护

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/register").permitAll() // 注册接口允许所有用户访问
                        .requestMatchers("/api/topics/**").hasRole("USER") // 其他请求需要 "USER" 角色才能访问
                        .anyRequest().authenticated() // 其他未明确配置的请求需要认证
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // 登出路径
                        .logoutSuccessHandler(customLogoutSuccessHandler)  // 使用自定义的登出成功处理器
                        .invalidateHttpSession(true) // 使会话失效
                        .deleteCookies("JSESSIONID") // 删除 cookies
                        .permitAll()
                )
                // 配置认证提供者
                .authenticationProvider(authenticationProvider())
                // 配置认证异常处理
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint())
                )
                // 配置无状态会话策略，确保不依赖服务器会话
                // .sessionManagement(session -> session
                //         .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // )
                .httpBasic(Customizer.withDefaults()) // 使用 HTTP Basic 认证
                .formLogin(formLogin ->
                        formLogin
                                .loginProcessingUrl("/login")
                                .successHandler(new CustomAuthenticationSuccessHandler())  // 使用自定义的成功处理程序
                                .permitAll()  // 登录页面和登录处理 URL 允许所有用户访问
                );

        return http.build();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            log.warn("Authentication failed: {}", authException.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        };
    }
}
