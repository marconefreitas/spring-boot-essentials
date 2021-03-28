package academy.devdojo.springboot.security;

import academy.devdojo.springboot.service.DevDojoUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;


@EnableWebSecurity
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String SENHA = "senha123";

    private final DevDojoUserDetailsService devDojoUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/animes/admin/**").hasRole("ADMIN")
                .antMatchers("/animes/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/actuator/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable()
                .formLogin()
                .and()
                //.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())

                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        log.info("Password encoded {} ", passwordEncoder.encode(SENHA));
        auth.inMemoryAuthentication()
                .withUser("marcone2")
                .password(passwordEncoder.encode(SENHA))
                .roles("USER", "ADMIN")
                .and()
                .withUser("user2")
                .password(passwordEncoder.encode(SENHA))
                .roles("USER");
        auth.userDetailsService(devDojoUserDetailsService)
                .passwordEncoder(passwordEncoder);
    }
}
