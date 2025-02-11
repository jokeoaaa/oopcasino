//package com.casino.democasino.security.handler;
//
//import com.casino.democasino.service.JwtService;
//
//import com.casino.democasino.service.UserService;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.core.Authentication;
//
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
//
//    private final JwtService jwtService;
//    private final UserService userService;
//
//    public OAuth2SuccessHandler(JwtService jwtService, UserService userService) {
//        this.jwtService = jwtService;
//        this.userService = userService;
//    }
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request,
//                                        HttpServletResponse response,
//                                        Authentication authentication)
//            throws IOException, ServletException {
//
//        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//
//        String email = oAuth2User.getAttribute("email");
//        String name = oAuth2User.getAttribute("name");
//
//
//        userService.processOAuthPostLogin(email, name);
//
//
//        String token = jwtService.generateToken(email);
//
//        String redirectUrl = "http://localhost:3030/oauth2/redirect?token=" + token;
//        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
//    }
//}
