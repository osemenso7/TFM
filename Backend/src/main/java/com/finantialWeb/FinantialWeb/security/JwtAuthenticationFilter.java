package com.finantialWeb.FinantialWeb.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.finantialWeb.FinantialWeb.module_users.service.UserSecurityData;
import com.finantialWeb.FinantialWeb.module_users.service.UsersService;
import com.finantialWeb.FinantialWeb.module_users.utils.exceptions.UserNotFoundException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsersService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);

        try {
            Long userId = jwtService.extractUserId(token);
            Long tokenVersion = jwtService.extractTokenVersion(token);

            if ((SecurityContextHolder.getContext().getAuthentication() == null)
                    && jwtService.isTokenValid(userId, token)
                    && (tokenVersion != null)) {

                UserSecurityData securityData = userService.findSecurityDataById(userId);
                
                if (tokenVersion.equals(securityData.getTokenVersion())) {

                    var userDetails = User.withUsername(securityData.getUsername())
                        .password("")
                        .authorities("ROLE_USER")
                        .build();

                    var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException | UserNotFoundException ex) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}