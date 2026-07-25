package in.swiftcart.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import in.swiftcart.security.CustomUserDetailsService;
import in.swiftcart.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    	http
        .csrf(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())
        .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        .authenticationProvider(authenticationProvider())

        .addFilterBefore(jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class)

         
        .authorizeHttpRequests(auth -> auth

        	    // Public
        	    .requestMatchers("/api/auth/login", 
        	    		"/api/auth/register",
        	    		"/swagger-ui/**",
        	            "/swagger-ui.html",
        	            "/v3/api-docs/**"
        	    		).permitAll()

        	    // Products GET — for everyone
        	    .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()

        	    // Admin only
        	    .requestMatchers(HttpMethod.POST, "/api/products/**").hasRole("ADMIN")
        	    .requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")
        	    .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")

        	    // Authenticated users
        	    .requestMatchers("/api/orders/**").hasAnyRole("USER", "ADMIN")
        	    .requestMatchers("/api/cart/**").hasAnyRole("USER", "ADMIN")
        	    .requestMatchers("/api/users/**").hasAnyRole("USER", "ADMIN")
        	    .requestMatchers("/api/ai/**").hasAnyRole("USER", "ADMIN")

        	 // All other requests require authentication
        	    .anyRequest().authenticated()
        	);
        return http.build();

       
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(customUserDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }
}