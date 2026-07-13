package in.swiftcart.security;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import in.swiftcart.exception.InvalidTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // If there's no Authorization header, or it doesn't start with "Bearer ", skip this filter
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Remove "Bearer " prefix to get the actual token
            final String jwt = authHeader.substring(7);

            // Extract the username from the token
            final String username = jwtService.extractUsername(jwt);

            // Proceed only if username exists and user is not already authenticated
            if (username != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                // Load user details from the database using the username
                UserDetails userDetails =
                        customUserDetailsService.loadUserByUsername(username);

                // Validate the token against the loaded user details
                if (jwtService.isTokenValid(jwt, userDetails)) {

                    // Create an authentication token for this user
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities());

                    // Attach extra request details (IP, session ID, etc.)
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request));

                    // Set the authentication in the security context, so Spring knows the user is logged in
                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authToken);
                }
            }

            // Continue with the rest of the filter chain
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException ex) {
            // Token expire ho chuka hai
            handlerExceptionResolver.resolveException(
                    request, response, null,
                    new InvalidTokenException("Token expired hai, dobara login karo"));

        } catch (MalformedJwtException | SignatureException ex) {
            // Token tamper ho gaya ya format galat hai
            handlerExceptionResolver.resolveException(
                    request, response, null,
                    new InvalidTokenException("Token invalid hai"));

        } catch (Exception ex) {
            // Koi bhi aur unexpected error
            handlerExceptionResolver.resolveException(
                    request, response, null,
                    new InvalidTokenException("Authentication fail ho gaya"));
        }
    }
}