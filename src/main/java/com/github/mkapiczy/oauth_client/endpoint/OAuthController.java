package com.github.mkapiczy.oauth_client.endpoint;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/oauth")
public class OAuthController {

    String appDomain = "http://localhost:8080";
    private String redirectUri = appDomain + "/oauth/handleRedirect";
    private String OAuthAuthorizationUrl = "https://oauth-authorization-server.herokuapp.com/oauth/authenticate?client_id=%s&redirect_uri=%s";

    private String clientId = "qu6gupg94k6qtil03p92ilnqkr";


    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ModelAndView redirectToOAuthLoginPage(HttpServletRequest request, HttpServletResponse response) {
        String oAuthLoginUrl = String.format(OAuthAuthorizationUrl, clientId, redirectUri);
        System.out.println(oAuthLoginUrl);
        return new ModelAndView("redirect:" + oAuthLoginUrl);
    }

    @RequestMapping(path = "/handleRedirect", method = RequestMethod.GET)
    public ModelAndView handleOAuthRedirect(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("OAuth redirect");
        String code = request.getParameter("code");
        System.out.println("Code: " + code);
        return new ModelAndView("loggedIn");
    }
}
