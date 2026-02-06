package passwordApp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

import static passwordApp.security.JwtUtil.getSigningKey;

// filter run per request
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try { // validate the jwt using secret key
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(getSigningKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                // get subject from the token
                String username = claims.getSubject();

                if (username != null) {
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());

                    // sets user as authenticated in spring security
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }

            } catch (Exception e) {
                System.out.println("Invalid JWT: " + e.getMessage());
            }
        }

        // passes request down the filter chain, if token is valid, "authenticate"
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/auth/");
    }

}