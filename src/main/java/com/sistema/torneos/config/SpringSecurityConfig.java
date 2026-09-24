package com.sistema.torneos.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import com.sistema.torneos.app.service.JpaUserDetailsService;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.sistema.torneos.config.filter.JwtValidationFilter;

@Configuration
public class SpringSecurityConfig {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    private JpaUserDetailsService userDetailsService;

        @Autowired
        private WebCorsConfig webCorsConfig;

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new CustomPasswordEncoder();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(webCorsConfig))
            .sessionManagement(management ->
                management.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth

                // Permitir acceso libre
                .requestMatchers("/", "/error").permitAll()

                .requestMatchers(HttpMethod.GET,
                        "/api/users",
                        "/api/users/page/**")
                .permitAll()

                // Login público
                .requestMatchers("/api/auth/login").permitAll()

                // Rutas protegidas para usuarios
                .requestMatchers(HttpMethod.GET,
                        "/api/usuarios",
                        "/api/usuarios/**")
                .authenticated()

                .requestMatchers(HttpMethod.POST,
                        "/api/usuarios")
                .hasRole("ADMIN")

                .requestMatchers(HttpMethod.PUT,
                        "/api/usuarios/**")
                .hasRole("ADMIN")

                .requestMatchers(HttpMethod.DELETE,
                        "/api/usuarios/**")
                .hasRole("ADMIN")

                // Sanciones/Suspensiones: consulta administrativa por jugador
                .requestMatchers(HttpMethod.GET,
                        "/api/sanciones/buscar-jugadores",
                        "/api/sanciones/buscar-por-jugador-id")
                .hasAnyRole("ADMIN", "USER")

                // Suspensiones: administradas desde Sanciones/Suspensiones
                .requestMatchers("/api/suspensiones/**")
                .hasAnyRole("ADMIN", "USER")

                // Partidos de la Jornada: registro de goles/sanciones del partido
                .requestMatchers("/api/sanciones/**", "/api/goles/**")
                .hasAnyRole("ADMIN", "REFEREE")

                // Lectura de catálogos para armar el filtro de Partidos de la Jornada
                .requestMatchers(HttpMethod.GET,
                        "/api/torneos/**",
                        "/api/categoria-torneo/**")
                .hasAnyRole("ADMIN", "REFEREE")

                // Partidos de la Jornada
                .requestMatchers(
                        "/api/partidos/**",
                        "/api/presencias/**")
                .hasAnyRole("ADMIN", "REFEREE")

                // Catálogos administrativos: todo lo demás requiere ADMIN
                .anyRequest().hasRole("ADMIN")
            )

            .authenticationProvider(authenticationProvider())
            .addFilter(new JwtValidationFilter(authenticationManager()));

        return http.build();
    }

    @Bean
    CorsConfigurationSource configurationSource() {
        return webCorsConfig;
    }

    @Bean
    FilterRegistrationBean<CorsFilter> corsFilter() {

        FilterRegistrationBean<CorsFilter> corsBean =
                new FilterRegistrationBean<>(
                        new CorsFilter(this.configurationSource()));

        corsBean.setOrder(Ordered.HIGHEST_PRECEDENCE);

        return corsBean;
    }
}