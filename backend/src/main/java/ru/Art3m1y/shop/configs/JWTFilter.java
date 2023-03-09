package ru.Art3m1y.shop.configs;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.Art3m1y.shop.services.PersonDetailsService;
import ru.Art3m1y.shop.utils.jwt.JWTUtil;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final PersonDetailsService personDetailsService;

    public JWTFilter(JWTUtil jwtUtil, PersonDetailsService personDetailsService) {
        this.jwtUtil = jwtUtil;
        this.personDetailsService = personDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getServletPath();

        if (!path.startsWith("/catalog") && (!(path.startsWith("/auth") && !path.equals("/auth/logout")) && (!path.startsWith("/image"))) && (!path.startsWith("/avatar"))) {
            checkAccessToken(request, response);
        }

        filterChain.doFilter(request, response);
    }

    private void checkAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            String JWTToken = authHeader.substring(7);
            if (JWTToken.isBlank()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                try {
                    String email = jwtUtil.getEmailFromAccessToken(JWTToken);

                    UserDetails personDetails = personDetailsService.loadUserByUsername(email);

                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(personDetails, personDetails.getPassword(), personDetails.getAuthorities());

                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(token);
                    }
                } catch (Exception e) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
        }
    }
}
