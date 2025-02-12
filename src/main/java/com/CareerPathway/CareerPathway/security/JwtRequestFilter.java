package com.CareerPathway.CareerPathway.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final Logger logger = Logger.getLogger(JwtRequestFilter.class.getName());

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            try {
                DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(jwtSecret))
                        .build()
                        .verify(token);

                Integer userId = decodedJWT.getClaim("userId").asInt();
                String role = decodedJWT.getClaim("role").asString();

                if (userId != null && role != null) {
                    request.setAttribute("userId", userId.toString());
                    request.setAttribute("role", role);

                    List<SimpleGrantedAuthority> authorities =
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
                    logger.info("JWT Role Claim: " + role);
                    logger.info("Assigned Authority: ROLE_" + role.toUpperCase());
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userId.toString(), null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    logger.warning("Missing required claims: userId or role");
                    throw new RuntimeException("Missing required claims");
                }

            } catch (Exception e) {
                logger.severe("Token verification failed: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized: Token verification failed.");
                return;
            }
        }
        chain.doFilter(request, response);
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getRequestURI().equals("/api/auth/login");
    }
}
