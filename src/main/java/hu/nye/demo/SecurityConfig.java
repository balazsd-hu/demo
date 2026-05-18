package hu.nye.demo;

import hu.nye.demo.repository.FelhasznaloRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(FelhasznaloRepository felhasznaloRepository) {
        return username -> {
            hu.nye.demo.model.Felhasznalo felhasznalo = felhasznaloRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Nem található felhasználó: " + username));

            return org.springframework.security.core.userdetails.User
                    .withUsername(felhasznalo.getUsername()) // Átadjuk a nevet
                    .password(felhasznalo.getPassword())     // Átadjuk a titkosított jelszót
                    .authorities(felhasznalo.getRole())      // Átadjuk a jogkört (ROLE_USER vagy ROLE_ADMIN)
                    .build();
        };
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            //    .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**", "/admin.html").hasRole("ADMIN")

                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()

                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .defaultSuccessUrl("/", true) // Sikeres belépés után a főoldalra dobjon
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .permitAll()
                );

        return http.build();
    }
}