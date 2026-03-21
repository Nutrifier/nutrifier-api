package fi.nutrifier.config;

import fi.nutrifier.utils.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JwtFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtTokenUtil;

    public JwtFilter(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            final String token = request.getHeader("Authorization");

            if (token != null) {
                String jwtToken = token.substring(7); // Remove "Bearer " part

                if (jwtTokenUtil.validateToken(jwtToken)) {
                    String username = jwtTokenUtil.extractUserId(jwtToken);
                    List<String> roles = jwtTokenUtil.extractRole(jwtToken);

                    List<GrantedAuthority> authorities = roles.stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // Spring expects roles to be prefixed with "ROLE_"
                            .collect(Collectors.toList());

                    Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    System.out.println("JWT validated successfully. User: " + username + ", Roles: " + roles);
                } else {
                    System.out.println("JWT token validation failed: " + jwtToken);
                }
            } else {
                System.out.println("No Authorization header present");
            }
        } catch (Exception e) {
            System.out.println("Error processing JWT filter: " + e);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
