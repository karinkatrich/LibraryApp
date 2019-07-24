package com.example.library.config

import com.example.library.service.UserService
import com.example.library.util.BookReactiveUserDetailsService
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain


@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

    @Bean
    fun securityWebFilterChain(http : ServerHttpSecurity) : SecurityWebFilterChain {
        http.authorizeExchange()
                .pathMatchers("/books/**")
                .authenticated()
                .and()
                .httpBasic()
                .and()
                .csrf()
                .disable();

        return http.build();
    }

    @Bean
    fun authenticationManager(bookReactiveUserDetailsService: BookReactiveUserDetailsService): UserDetailsRepositoryReactiveAuthenticationManager {
        val userDetailsRepositoryReactiveAuthenticationManager = UserDetailsRepositoryReactiveAuthenticationManager(bookReactiveUserDetailsService)
        userDetailsRepositoryReactiveAuthenticationManager.setPasswordEncoder(passwordEncoder())

        return userDetailsRepositoryReactiveAuthenticationManager
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    @Profile("default")
    fun applicationRunner(userService : UserService): ApplicationRunner {
        return ApplicationRunner {
            userService.save(com.example.library.model.User(1, "user", passwordEncoder().encode("password"), "USER", "User of Book")).subscribe();
            userService.save(com.example.library.model.User(2, "admin", passwordEncoder().encode("password"), "ADMIN", "Admin of Book")).subscribe()
        }
    }

}