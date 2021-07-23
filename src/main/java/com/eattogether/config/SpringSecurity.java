package com.eattogether.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SpringSecurity extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/","/login","/sign-up","/login-by-email",
                        "/email-login","/check-email-login","/login-link").permitAll()
                .anyRequest().authenticated();  // 나머지는 로그인을 통해서만 접근가능

        http.formLogin()
                .loginPage("/login").permitAll(); // 커스텀 로그인 Page

        http.logout()
                .logoutSuccessUrl("/") ;   // 커스텀 로그아웃 Page
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring() // js/css/image 파일 등 보안필터를 적용할 필요없는 리소스 설정
                .mvcMatchers("/node_modules/**") // node_modules로 시작하는 플러그인 허용
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
