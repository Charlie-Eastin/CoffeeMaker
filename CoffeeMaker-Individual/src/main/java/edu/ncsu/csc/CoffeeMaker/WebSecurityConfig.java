package edu.ncsu.csc.CoffeeMaker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure ( final HttpSecurity http ) throws Exception {
        http.authorizeRequests().antMatchers( "/home" ).permitAll().anyRequest().authenticated().and().formLogin()
                .loginPage( "/login" ).permitAll().and().logout().permitAll();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService () {
        // Create encoder to store encoded password.
        final PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        // Encode password and then store it in the details (So it is not stored
        // as plain text in memory).
        final UserDetails user = User.withUsername( "user" ).password( encoder.encode( "password" ) ).roles( "USER" )
                .build();
        // Same for staff
        final UserDetails staff = User.withUsername( "staff" ).password( encoder.encode( "password" ) ).roles( "STAFF" )
                .build();

        // Store these IN MEMORY rather than in our database.
        return new InMemoryUserDetailsManager( user, staff );
    }
}
