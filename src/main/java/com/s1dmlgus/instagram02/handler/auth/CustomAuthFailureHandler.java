package com.s1dmlgus.instagram02.handler.auth;


import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

public class CustomAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final String DEFAULT_FAILURE_URL = "/auth/signin";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String errorMessage = "";

        if (exception instanceof InternalAuthenticationServiceException) {
            errorMessage = "아이디나 패스워드가 일치하지 않습니다";
        }

        super.setDefaultFailureUrl(DEFAULT_FAILURE_URL + "?error=true&exception=" + URLEncoder.encode(errorMessage, "UTF-8"));

        super.onAuthenticationFailure(request, response, exception);
    }

}
