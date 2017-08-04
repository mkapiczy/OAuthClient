/*
 * Copyright 2015 Benedikt Ritter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
public class FacebookController {

    private FacebookRepository repository;
    private String clientId = "1955772338033020";
    String appDomain = "http://localhost:8080";
    private String redirectUri = appDomain + "/oauth/handleRedirect";
    private String OAuthAuthorizationUrl = "http://www.localhost:8090/oauth/authenticate?client_id=%s&redirect_uri=%s";

    @Autowired
    public FacebookController(FacebookRepository repository) {
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
