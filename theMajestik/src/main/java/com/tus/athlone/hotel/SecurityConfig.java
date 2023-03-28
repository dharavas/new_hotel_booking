package com.tus.athlone.hotel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

import com.tus.athlone.hotel.services.CustomOAuth2UserService;
import com.tus.athlone.hotel.services.GoogleOAuth2SuccessHandler;
import com.tus.athlone.hotel.services.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter  {

    @Autowired
    private UserService userService;
    

    @Autowired
    private GoogleOAuth2SuccessHandler successHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        
            .authorizeRequests()
                .antMatchers("/", "/rooms/**", "/common.css","/socials/**","/kitchen/**", "/styles/**", "/oauth/**","/login/google/**", "/login", "/register", "/room/checkAvailability", "/showAvailableRooms", "/showAvailableRooms", "/resources/**", "/static/**", "/css/**", "/js/**", "/img/**", "/icon/**").permitAll()
                .anyRequest().authenticated()
                .and()
            
            .formLogin()
                .loginPage("/login")
                .successHandler(successHandler)
                .permitAll()
                .and()
            .logout()
                .permitAll()
                .and()
            .oauth2Login()
                .loginPage("/login")
                .successHandler(successHandler)
                .userInfoEndpoint()
                    .userService(oAuth2UserService())
                    .and()
                .and()
            .csrf().disable();
        	
            

        http.formLogin().defaultSuccessUrl("/success", true);  
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        return new CustomOAuth2UserService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }
}



