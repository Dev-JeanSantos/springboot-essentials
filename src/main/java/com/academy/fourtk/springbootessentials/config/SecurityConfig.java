package com.academy.fourtk.springbootessentials.config;

import com.academy.fourtk.springbootessentials.services.DevUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final DevUserDetailsService devUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http.csrf().disable()
//               .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
               .authorizeHttpRequests()
               .antMatchers("/api/v1/animes/admin/**").hasRole("ADMIN")
               .antMatchers("/api/v1/animes/**").hasRole("USER")
               .anyRequest()
               .authenticated()
               .and()
               .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        auth.inMemoryAuthentication()
                .withUser("devjs2")
                .password(passwordEncoder.encode("jean"))
                .roles("USER, ADMIN")
                .and()
                .withUser("jean2")
                .password(passwordEncoder.encode("jean"))
                .roles("USER");
        auth.userDetailsService(devUserDetailsService).passwordEncoder(passwordEncoder);

    }
}
