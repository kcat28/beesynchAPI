package com.beesynch.app.rest.Security;

import com.beesynch.app.rest.Security.JwtUtil;
import com.beesynch.app.rest.Service.UserService;
import io.jsonwebtoken.ExpiredJwtException; // Import necessary exception
import io.jsonwebtoken.JwtException; // For general token issues
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7); // Extract the JWT token

            try {
                Long userId = jwtUtil.extractUserId(jwt); // Extract user ID from the token

                // Validate the JWT and check authentication
                if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    var userDetails = userService.loadUserById(userId);

                    // Check token validity
                    if (jwtUtil.isValidToken(jwt, userDetails)) {
                        // Add authentication info to the SecurityContext
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (ExpiredJwtException e) {
                // Handle expired token specifically
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401: Unauthorized
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Token expired. Please log in again.\"}");
                return; // Stop further processing
            } catch (JwtException e) {
                // Handle other token-related exceptions
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Or 403: Forbidden
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Invalid token. Access denied.\"}");
                return; // Stop further processing
            }
        }

        // Continue the filter chain if all is valid
        filterChain.doFilter(request, response);
    }
}