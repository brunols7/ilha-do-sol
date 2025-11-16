package com.senac.ilha_do_sol.infra.security;

import com.senac.ilha_do_sol.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                        .requestMatchers("/", "/home", "/login", "/cadastro", "/contato").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/quartos", "/api/quartos/**").permitAll()
                        .requestMatchers("/api/reservas", "/api/reservas/**", "/api/user/**").authenticated()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/quartos", "/reservas", "/reservar", "/perfil").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler((request, response, authentication) -> {
                            boolean isAdmin = authentication.getAuthorities().stream()
                                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

                            // Verifica se há parâmetro redirect no formulário
                            String redirectParam = request.getParameter("redirect");

                            if (isAdmin) {
                                response.sendRedirect("/admin");
                            } else if (redirectParam != null && !redirectParam.isEmpty()) {
                                // Redireciona para a URL salva no parâmetro redirect
                                response.sendRedirect(redirectParam);
                            } else {
                                // Tenta recuperar a URL salva pelo Spring Security
                                HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
                                SavedRequest savedRequest = requestCache.getRequest(request, response);

                                if (savedRequest != null) {
                                    String targetUrl = savedRequest.getRedirectUrl();
                                    response.sendRedirect(targetUrl);
                                } else {
                                    response.sendRedirect("/home");
                                }
                            }
                        })
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.sendRedirect("/login?accessDenied=true");
                        })
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}
