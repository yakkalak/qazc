package com.kiran.user.management.filter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.Base64;

public class TokenValidationFilter extends OncePerRequestFilter {

    private final String HEADER = "Authorization";
    private final String PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if ("/login".equals(path) || (("/user-management/users").equals(path) && request.getMethod().equals("POST"))) {
            chain.doFilter(request, response);
            return;
        }

        try {
            if (checkJWTToken(request)) {
                String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");

                if (!validateToken(jwtToken)) {
                    throw new RuntimeException("Missing JWT Token");
                }
            } else {
                throw new RuntimeException("Missing JWT Token");
            }

            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("someName", null, null));

            chain.doFilter(request, response);
        } catch (RuntimeException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }

    private boolean validateToken(String jwtToken) {
        String secret = "bsdfSES34wfsdgsdfTDSD32dfsddDDerQSNCK36SOWEK5354fdkdf4";
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret), SignatureAlgorithm.HS256.getJcaName());

        try {
            Jwts.parserBuilder()
                    .setSigningKey(hmacKey)
                    .build()
                    .parseClaimsJws(jwtToken);
        } catch (RuntimeException e) {
            return false;
        }

        return true;
    }

    private boolean checkJWTToken(HttpServletRequest request) {
        String authenticationHeader = request.getHeader(HEADER);
        return authenticationHeader != null && authenticationHeader.startsWith(PREFIX);
    }
}