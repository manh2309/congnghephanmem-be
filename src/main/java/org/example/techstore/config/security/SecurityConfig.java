package org.example.techstore.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    private static final String[] PUBLIC_URLS = {
            "/api/auth/login",
            "/api/auth/refresh",
            "/api/auth/register",
            "/swagger-ui/**",
            "/swagger-api/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // ⭐ BẮT BUỘC để fix CORS
                .cors(cors -> {})

                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .requestMatchers(PUBLIC_URLS).permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/brands/all").hasAnyRole("ADMIN", "STAFF")
                                .requestMatchers(HttpMethod.PUT, "/api/brands/{id}/restore").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/brands").hasAnyRole("ADMIN", "STAFF", "USER")
                                .requestMatchers(HttpMethod.GET, "/api/brands/{id}").hasAnyRole("ADMIN", "STAFF", "USER")
                                .requestMatchers(HttpMethod.POST, "/api/brands").hasAnyRole("ADMIN", "STAFF")
                                .requestMatchers(HttpMethod.PUT, "/api/brands/{id}").hasAnyRole("ADMIN", "STAFF")
                                .requestMatchers(HttpMethod.DELETE, "/api/brands/{id}").hasAnyRole("ADMIN", "STAFF")
                                // ROLE
                                .requestMatchers(HttpMethod.GET, "/api/roles/all").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/roles/{id}/restore").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/roles").hasAnyRole("ADMIN", "STAFF")
                                .requestMatchers(HttpMethod.GET, "/api/roles/{id}").hasAnyRole("ADMIN", "STAFF")
                                .requestMatchers(HttpMethod.POST, "/api/roles").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/api/roles/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/roles/{id}").hasRole("ADMIN")
                                // IMAGE
                                .requestMatchers(HttpMethod.GET, "/api/images").hasAnyRole("ADMIN", "STAFF", "USER")
                                .requestMatchers(HttpMethod.GET, "/api/images/product/{productId}").hasAnyRole("ADMIN", "STAFF", "USER")
                                .requestMatchers(HttpMethod.POST, "/api/images/upload").hasAnyRole("ADMIN", "STAFF")
                                .requestMatchers(HttpMethod.DELETE, "/api/images/{id}").hasRole("ADMIN")
                                // ACCOUNT
                                .requestMatchers(HttpMethod.GET, "/api/accounts/", "/api/accounts/statistics", "/api/accounts/all").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/accounts").hasAnyRole("ADMIN", "STAFF", "USER")
                                .requestMatchers(HttpMethod.PATCH, "/api/accounts/{id}").hasAnyRole("ADMIN", "STAFF", "USER")
                                .requestMatchers(HttpMethod.DELETE, "/api/accounts/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/accounts/{id}/restore").hasRole("ADMIN")
                                // ORDER
                                .requestMatchers(HttpMethod.GET, "/api/orders").hasAnyRole("ADMIN", "STAFF")
                                .requestMatchers(HttpMethod.GET, "/api/orders/{id}").hasAnyRole("ADMIN", "STAFF", "USER")
                                .requestMatchers(HttpMethod.GET, "/api/orders/account/{accountId}").hasAnyRole("ADMIN", "STAFF", "USER")
                                .requestMatchers(HttpMethod.POST, "/api/orders").hasRole("USER")
                                .requestMatchers(HttpMethod.PATCH, "/api/orders/{id}").hasAnyRole("ADMIN", "STAFF")
                                .requestMatchers(HttpMethod.PATCH, "/api/orders/{id}/status").hasAnyRole("ADMIN", "STAFF")
                                //ORDER_DETAIL
                                .requestMatchers(HttpMethod.GET, "/api/order-details").hasAnyRole("ADMIN", "STAFF")
                                .requestMatchers(HttpMethod.GET, "/api/order-details/all").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/order-details/{id}").hasAnyRole("ADMIN", "STAFF", "USER")
                                .requestMatchers(HttpMethod.GET, "/api/order-details/{id}/any").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/order-details").hasAnyRole("ADMIN", "STAFF")
                                .requestMatchers(HttpMethod.PATCH, "/api/order-details/{id}").hasAnyRole("ADMIN", "STAFF")
                                .requestMatchers(HttpMethod.DELETE, "/api/order-details/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/order-details/{id}/restore").hasRole("ADMIN")
                                // CATEGORY
                                .requestMatchers(HttpMethod.GET, "/api/categories").hasAnyRole("ADMIN", "STAFF", "USER")
                                .requestMatchers(HttpMethod.GET, "/api/categories/all").hasAnyRole("ADMIN", "STAFF")
                                .requestMatchers(HttpMethod.GET, "/api/categories/{id}").hasAnyRole("ADMIN", "STAFF", "USER")
                                .requestMatchers(HttpMethod.GET, "/api/categories/{id}/any").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/categories").hasAnyRole("ADMIN", "STAFF")
                                .requestMatchers(HttpMethod.PATCH, "/api/categories/{id}").hasAnyRole("ADMIN", "STAFF")
                                .requestMatchers(HttpMethod.DELETE, "/api/categories/{id}").hasAnyRole("ADMIN", "STAFF")
                                .requestMatchers(HttpMethod.PUT, "/api/categories/{id}/restore").hasRole("ADMIN")
                                // PRODUCT
                                .requestMatchers(HttpMethod.GET, "/api/products/all").hasAnyRole("ADMIN", "STAFF")
                                .requestMatchers(HttpMethod.PUT, "/api/products/{id}/restore").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/products", "/api/products/{id}").hasAnyRole("ADMIN", "STAFF", "USER")
                                .requestMatchers(HttpMethod.POST, "/api/products").hasAnyRole("ADMIN", "STAFF")
                                .requestMatchers(HttpMethod.PUT, "/api/products/{id}").hasAnyRole("ADMIN", "STAFF")
                                .requestMatchers(HttpMethod.DELETE, "/api/products/{id}").hasAnyRole("ADMIN", "STAFF")

                                // PRODUCT DETAIL
                                .requestMatchers(HttpMethod.GET, "/api/product-details/all").hasAnyRole("ADMIN", "STAFF")
                                .requestMatchers(HttpMethod.PUT, "/api/product-details/{id}/restore").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/product-details", "/api/product-details/{id}").hasAnyRole("ADMIN", "STAFF", "USER")
                                .requestMatchers(HttpMethod.POST, "/api/product-details").hasAnyRole("ADMIN", "STAFF")
                                .requestMatchers(HttpMethod.PUT, "/api/product-details/{id}").hasAnyRole("ADMIN", "STAFF")
                                .requestMatchers(HttpMethod.DELETE, "/api/product-details/{id}").hasAnyRole("ADMIN", "STAFF")

                                // SPECIFICATION
                                .requestMatchers(HttpMethod.GET, "/api/specifications/all").hasAnyRole("ADMIN", "STAFF")
                                .requestMatchers(HttpMethod.PUT, "/api/specifications/{id}/restore").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/specifications", "/api/specifications/{id}", "/api/specifications/configuration/{configurationId}").hasAnyRole("ADMIN", "STAFF", "USER")
                                .requestMatchers(HttpMethod.POST, "/api/specifications").hasAnyRole("ADMIN", "STAFF")
                                .requestMatchers(HttpMethod.PUT, "/api/specifications/{id}").hasAnyRole("ADMIN", "STAFF")
                                .requestMatchers(HttpMethod.DELETE, "/api/specifications/{id}").hasAnyRole("ADMIN", "STAFF")
                                // CONFIGURATIONS
                                .requestMatchers(HttpMethod.GET, "/api/configurations").hasAnyRole("ADMIN", "STAFF", "USER")
                                .requestMatchers(HttpMethod.GET, "/api/configurations/all").hasAnyRole("ADMIN", "STAFF")
                                .requestMatchers(HttpMethod.GET, "/api/configurations/deleted").hasAnyRole("ADMIN", "STAFF")
                                .requestMatchers(HttpMethod.GET, "/api/configurations/{id}").hasAnyRole("ADMIN", "STAFF", "USER")
                                .requestMatchers(HttpMethod.GET, "/api/configurations/any/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/configurations").hasAnyRole("ADMIN", "STAFF")
                                .requestMatchers(HttpMethod.PATCH, "/api/configurations/{id}").hasAnyRole("ADMIN", "STAFF")
                                .requestMatchers(HttpMethod.DELETE, "/api/configurations/{id}").hasAnyRole("ADMIN", "STAFF")
                                .requestMatchers(HttpMethod.PUT, "/api/configurations/{id}/restore").hasRole("ADMIN")
                                //AI
                        .requestMatchers(HttpMethod.GET, "/api/analytics/smart-inventory").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .sessionManagement(sess ->
                        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                );
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {

        return config.getAuthenticationManager();
    }

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        return new DefaultMethodSecurityExpressionHandler();
    }
}