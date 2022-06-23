package com.whatalk.memberservice.auth.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatalk.memberservice.domain.Member;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public CustomAuthenticationFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager) {
        super(defaultFilterProcessesUrl, authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("잘못된 요청입니다.");
        }
        try {

            Member member = new ObjectMapper().readValue(request.getInputStream(), Member.class);

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(member.getEmail(), member.getPassword());

            return getAuthenticationManager().authenticate(token);

        } catch (IOException e) {
            throw new AuthenticationServiceException("잘못된 입력입니다.");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String token = JWT.create()
                .withSubject((String) authResult.getCredentials())
                // TODO: 2022/06/23 property 생성
                .withExpiresAt(new Date(System.currentTimeMillis() + Environment))
                .sign(Algorithm.HMAC256(Environment));

        response.addHeader("Authorization", "Bearer " + token);
    }
}
