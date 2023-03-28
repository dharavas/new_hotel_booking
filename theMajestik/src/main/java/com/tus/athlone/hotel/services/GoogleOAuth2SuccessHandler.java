package com.tus.athlone.hotel.services;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.tus.athlone.hotel.models.User;

@Component
public class GoogleOAuth2SuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User user = oauthToken.getPrincipal();
        String email = user.getAttribute("email");
        String name = user.getAttribute("name");
        System.out.println(email);
     // Redirect to the default success URL
        super.onAuthenticationSuccess(request, response, authentication);
        // Check if user already exists in the database
        User existingUser = userService.findByEmail(email);
        if (existingUser == null) {
            // If user does not exist, create a new user and add to the database
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername(name);
            newUser.setEnabled(true);
            userService.register(newUser);
            
            
            return;
        }else {
        	response.sendRedirect("/success");
        }

        
    }
}
