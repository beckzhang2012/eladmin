package me.zhengjie.config;

import me.zhengjie.modules.system.service.UserLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Zheng Jie
 * @date 2023-09-01
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserLockService userLockService;

    @Value("${security.login.failed-attempts-threshold:5}")
    private int failedAttemptsThreshold;

    @Value("${security.login.lock-duration:900000}")
    private long lockDuration;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/auth/login").permitAll()
                .antMatchers("/api/auth/logout").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginProcessingUrl("/api/auth/login")
                .successHandler((request, response, authentication) -> {
                    // 登录成功，重置失败尝试次数
                    userLockService.handleLoginSuccess(authentication.getName());
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"code\":0,\"message\":\"登录成功\"}");
                })
                .failureHandler((request, response, exception) -> {
                    // 登录失败，增加失败尝试次数
                    String username = request.getParameter("username");
                    if (username != null) {
                        boolean locked = userLockService.handleLoginFailure(username, failedAttemptsThreshold);
                        if (locked) {
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"code\":403,\"message\":\"登录失败次数过多，账号已锁定\"}");
                        } else {
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"code\":401,\"message\":\"用户名或密码错误\"}");
                        }
                    } else {
                        response.setContentType("application/json;charset=UTF-8");
                        response.getWriter().write("{\"code\":401,\"message\":\"用户名不能为空\"}");
                    }
                })
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/api/auth/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"code\":0,\"message\":\"注销成功\"}");
                })
                .permitAll();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        // 自定义用户状态检查，检查用户是否被锁定
        provider.setPreAuthenticationChecks(authentication -> {
            String username = authentication.getUsername();
            if (userLockService.isLocked(username)) {
                throw new RuntimeException("账号已锁定");
            }
        });
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}