package com.se330.coffee_shop_management_backend.config;

import com.se330.coffee_shop_management_backend.security.JwtAuthenticationEntryPoint;
import com.se330.coffee_shop_management_backend.security.JwtAuthenticationFilter;
import com.se330.coffee_shop_management_backend.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Profile("!mvcIt")
public class WebSecurityConfig {
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Configure Spring Security.
     *
     * @param http HttpSecurity
     * @return SecurityFilterChain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .exceptionHandling(configurer -> configurer
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            )
            .sessionManagement(configurer -> configurer
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .headers(configurer -> configurer
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(requests -> requests
                .requestMatchers(
                    "/",
                    "/auth/**",
                    "/public/**",
                    "/assets/**",
                    "/api-docs/**",
                    "/swagger-ui/**",
                    "/webjars/**",
                    "/ws/**",
                    "/payment/paypal/**",
                    "/payment/vnpay/**",
                    "/payment/success/**",
                    "/payment/cancel/**"
                ).permitAll()
                .requestMatchers("/admin/**").hasAuthority(Constants.RoleEnum.ADMIN.name())
                .anyRequest().authenticated()
            )
            .build();
    }
}
