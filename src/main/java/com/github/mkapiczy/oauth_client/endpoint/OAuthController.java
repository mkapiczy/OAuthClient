package com.github.mkapiczy.oauth_client.endpoint;

import com.github.mkapiczy.oauth_client.ResourceResponse;
import com.github.mkapiczy.oauth_client.TokenResponse;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

@Controller
@RequestMapping("/oauth")
public class OAuthController {

    String appDomain = "http://localhost:8080";
    private String redirectUri = appDomain + "/oauth/handleRedirect";

    //    private String oAuthAuthorizationUrl = "https://oauth-authorization-server.herokuapp.com/oauth/authenticate?client_id=%s&redirect_uri=%s";
//    private String oauthProviderDomain = "http://localhost:8090";
    private String oauthProviderDomain = "https://oauth-authorization-server.herokuapp.com";
    private String oAuthAuthorizationUrl = oauthProviderDomain + "/oauth/authenticate?client_id=%s&redirect_uri=%s";
    private String oAuthAccessTokenUrl = oauthProviderDomain + "/oauth/access_token?client_id=%s&redirect_uri=%s&client_secret=%s&code=%s";
    private String oAuthResourceUrl = oauthProviderDomain + "/oauth/resource?access_token=%s";

    private String appId = "cti431nt4kd5podulp5hvl9s1a";
    private String appSecret = "l8aj33dt8p9gn11u1i8cqne9li";


    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ModelAndView redirectToOAuthLoginPage(HttpServletRequest request, HttpServletResponse response) {
        String oAuthLoginUrl = String.format(oAuthAuthorizationUrl, appId, redirectUri);
        System.out.println(oAuthLoginUrl);
        return new ModelAndView("redirect:" + oAuthLoginUrl);
    }

    @RequestMapping(path = "/handleRedirect", method = RequestMethod.GET)
    public ModelAndView handleOAuthRedirect(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("OAuth redirect");
        String code = request.getParameter("code");
        System.out.println("Code: " + code);

        String oAuthAccessTokenUri = String.format(oAuthAccessTokenUrl, appId, redirectUri, appSecret, code);
        try {
            URL url = new URL(oAuthAccessTokenUri);
            HttpURLConnection conn = (HttpURLConnection) url
                    .openConnection();
            conn.setRequestMethod("GET");
            String line, outputString = "";
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            while ((line = reader.readLine()) != null) {
                outputString += line;
            }

            System.out.println(outputString);
            Gson gson = new Gson();
            TokenResponse tokenResponse = gson.fromJson(outputString, TokenResponse.class);

            System.out.println(tokenResponse.accessToken);

            // Normally it would be saved to database but I focus more on the authorization server implementation than client
            request.getSession().setAttribute("accessToken", tokenResponse.accessToken);
            request.getSession().setAttribute("refreshToken", tokenResponse.refreshToken);

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ModelAndView("redirect:getResource");
    }

    @RequestMapping(path = "/getResource", method = RequestMethod.GET)
    public ModelAndView getResource(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Get resource");
        String accessToken = (String) request.getSession().getAttribute("accessToken");
        System.out.println("AccessToken: " + accessToken);


        String oAuthResourceUri = String.format(oAuthResourceUrl, accessToken);
        try {
            URL url = new URL(oAuthResourceUri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            String line, outputString = "";
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            while ((line = reader.readLine()) != null) {
                outputString += line;
            }

            System.out.println(outputString);
            Gson gson = new Gson();
            ResourceResponse resourceResponse = gson.fromJson(outputString, ResourceResponse.class);

            System.out.println(resourceResponse.getAppOwner());

            ModelAndView modelAndView = new ModelAndView("loggedIn");
            modelAndView.getModelMap().addAttribute("appOwner", resourceResponse.getAppOwner());
            modelAndView.getModelMap().addAttribute("email", resourceResponse.getEmail());
            return modelAndView;
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ModelAndView("loggedIn");
    }
}
