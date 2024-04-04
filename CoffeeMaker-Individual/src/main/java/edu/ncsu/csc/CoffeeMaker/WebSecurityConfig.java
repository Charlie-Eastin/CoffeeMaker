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

/**
 * Spring Security configuration manager. Controls valid login information and
 * whitelisted pages.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Configure which pages are permitted to access without being logged in and
     * the default login page.
     */
    @Override
    protected void configure ( final HttpSecurity http ) throws Exception {
        http.authorizeRequests().antMatchers( "/css/app.css", "/css/bootstrap.css" ).permitAll().anyRequest()
                .authenticated().and().formLogin().loginPage( "/login" ).permitAll().and().logout().permitAll();
    }

    /**
     * Controls valid login information for users. In a production environment,
     * this information would be encrypted ahead of time and stored in a large
     * file or database. Make sure to ALWAYS user the encoder, or the password
     * will not be secure.
     */
    @Bean
    @Override
    public UserDetailsService userDetailsService () {
        // Create encoder to store encoded password.
        final PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        // Encode password and then store it in the details (So it is not stored
        // as plain text in memory).
        final UserDetails user = User.withUsername( "customer" ).password( encoder.encode( "password" ) )
                .roles( "CUSTOMER" ).authorities( "CUSTOMER" ).build();
        // Same for staff
        final UserDetails staff = User.withUsername( "staff" ).password( encoder.encode( "password" ) ).roles( "STAFF" )
                .authorities( "STAFF" ).build();

        // Store these IN MEMORY rather than in our database.
        return new InMemoryUserDetailsManager( user, staff );
    }
}
