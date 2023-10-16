package br.com.criandoapi.projeto.configuration;

import br.com.criandoapi.projeto.filter.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("http://localhost:8080/authenticator")
                .csrf(Customizer.withDefaults())
                .authorizeRequests(authorizeRequests ->
                    authorizeRequests
                            .requestMatchers("/authenticator").permitAll()
                            .anyRequest().authenticated()
//                            .anyRequest().permitAll()
                )
                .addFilterBefore(new JwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

