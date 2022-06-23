package com.whatalk.memberservice.auth.config;

import com.whatalk.memberservice.auth.filter.CustomAuthenticationFilter;
import com.whatalk.memberservice.auth.provider.CustomAuthenticationProvider;
import com.whatalk.memberservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final MemberService memberService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        http.
                csrf().disable()
                .authorizeRequests()
                .antMatchers("/members").anonymous()
                .anyRequest().authenticated();


        return http.build();
    }

    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() {
        // TODO: 2022/06/23  authenticationManager bean 등록
        return new CustomAuthenticationFilter("/login", authenticationManager());
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider(){
        return new CustomAuthenticationProvider(memberService, passwordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
