package com.tus.athlone.hotel.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

@Configuration
public class PayPalConfig {
  
    private static final String CLIENT_ID = "ATePs72ygJ0criT0AL8tDofOaE0ZWkdxyKfEjWk6ZMUgq7djxmRdjjaGns3qDG_Y4B1LWms8xRYCNibj";
    private static final String CLIENT_SECRET = "EIjxRrNNxLX3tJnDAVz9u3WPD9atmYPZrYmuYiX2rWuAa40grdcoWPKNjX1g84E2g4_IB8czWJYVk78D";
    private static final String MODE = "sandbox";
  
  @Bean
  public APIContext apiContext() throws PayPalRESTException {
    String mode = "sandbox"; // or "live"
    APIContext context = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);
    return context;
  }
}
