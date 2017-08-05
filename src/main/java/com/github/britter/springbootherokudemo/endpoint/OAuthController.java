package com.github.britter.springbootherokudemo.endpoint;

import com.github.britter.springbootherokudemo.FacebookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/oauth")
public class OAuthController {

    private FacebookRepository repository;
    private String clientId = "qtbmgp9cl9vtel86fcgiuicfol";
    String appDomain = "http://localhost:8080";
    private String redirectUri = appDomain + "/oauth/handleRedirect";
    private String OAuthAuthorizationUrl = "http://localhost:8090/oauth/authenticate?client_id=%s&redirect_uri=%s";

    @Autowired
    public OAuthController(FacebookRepository repository) {
        this.repository = repository;
    }

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
