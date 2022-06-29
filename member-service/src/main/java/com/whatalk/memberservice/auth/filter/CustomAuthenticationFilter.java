package com.whatalk.memberservice.auth.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatalk.memberservice.auth.dto.MemberLoginRequestDto;
import com.whatalk.memberservice.auth.exception.AuthStatus;
import com.whatalk.memberservice.exception.ErrorResponse;
import org.springframework.core.env.Environment;
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

    private final Environment environment;

    public CustomAuthenticationFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager, Environment environment) {
        super(defaultFilterProcessesUrl, authenticationManager);
        this.environment = environment;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(AuthStatus.NOT_SUPPORTED_METHOD.getMessage());
        }

        try {

            MemberLoginRequestDto loginMember = new ObjectMapper().readValue(request.getInputStream(), MemberLoginRequestDto.class);

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginMember.getEmail(), loginMember.getPassword());

            return getAuthenticationManager().authenticate(token);

        } catch (IOException e) {
            throw new AuthenticationServiceException(AuthStatus.BAD_REQUEST.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        String token = JWT.create()
                .withSubject(
                        (String) authResult.getPrincipal()
                )
                .withExpiresAt(
                        new Date(System.currentTimeMillis() + Long.parseLong(environment.getProperty("jwt.EXP")))
                )
                .sign(
                        Algorithm.HMAC256(environment.getProperty("jwt.SECRET"))
                );

        response.addHeader("Authorization", "Bearer " + token);
        response.addHeader("Email", (String) authResult.getPrincipal());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        // TODO: 2022/06/25 refactor
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpServletResponse.SC_UNAUTHORIZED)
                .message(failed.getMessage())
                .build();

        String body = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(errorResponse);

        response
                .getWriter()
                .printf(body)
                .flush();
    }
}
