package com.champlain.ateliermecaniquews.authenticationsubdomain.businesslayer;

import com.nimbusds.jose.JOSEException;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;

public interface TokenService {
    String verifyGoogleToken(String jwtToken) throws JOSEException, ParseException;
    String verifyFacebookToken(String accessToken);
    String verifyInstagramToken(String accessToken);
}
