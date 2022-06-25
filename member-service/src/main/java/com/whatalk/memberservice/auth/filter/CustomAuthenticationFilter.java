package com.whatalk.memberservice.auth.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.whatalk.memberservice.auth.Jwt;
import com.whatalk.memberservice.controller.dto.MemberLoginRequestDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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

            MemberLoginRequestDto loginMember = new ObjectMapper().readValue(request.getInputStream(), MemberLoginRequestDto.class);

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginMember.getEmail(), loginMember.getPassword());

            return getAuthenticationManager().authenticate(token);

        } catch (IOException e) {
            throw new AuthenticationServiceException("잘못된 입력입니다.");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        String token = JWT.create()
                .withSubject(
                        (String) authResult.getPrincipal()
                )
                .withExpiresAt(
                        new Date(System.currentTimeMillis() + Jwt.EXP)
                )
                .sign(
                        Algorithm.HMAC256(Jwt.SECRET)
                );

        response.addHeader("Authorization", "Bearer " + token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        // TODO: 2022/06/25 refactor
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode content = mapper.createObjectNode();

        content.put("code", HttpServletResponse.SC_UNAUTHORIZED);
        content.put("message", failed.getMessage());

        String body = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(content);

        response
                .getWriter()
                .printf(body)
                .flush();
    }
}
