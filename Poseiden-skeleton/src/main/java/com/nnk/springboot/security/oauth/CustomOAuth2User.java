package com.nnk.springboot.security.oauth;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * The objective of this class is to modify the access to the "username" in the OAuth2AuthenticationToken, this allows an easier display of username in thymeleaf.
 * With thymeleaf sec:authentication="name" :
 * <p>
 * - UsernamePasswordAuthenticationToken : by default the display is correct<br>
 * - OAuth2AuthenticationToken : by default it displays the gitHub id number, i want the login name
 * </p>
 * <p>
 * To achieve this, this class implements OAuth2User and also wraps an instance of OAuth2User, which will be passed by Spring OAuth2 
 * upon successful OAuth authentication. We override the getName() method to return Github "login" account as username.
 * </p>
 * <p>
 * Original code from: 
 * <a href="https://www.codejava.net/frameworks/spring-boot/oauth2-login-with-github-example">
 * www.codejava.net
 * </a>
 * </p>
 * 
 */
public class CustomOAuth2User implements OAuth2User {
 
	Logger logger = LoggerFactory.getLogger(CustomOAuth2User.class);
	
    private OAuth2User oauth2User;
     
    public CustomOAuth2User(OAuth2User oauth2User) {
    	logger.debug("Constructor CustomOAuth2User");
    	
    	this.oauth2User = oauth2User;
        
    }
 
    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }
 
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oauth2User.getAuthorities();
    }
 
    @Override
    public String getName() {
    	//In the original code (from www.codejava.net) it was "name" but it can be null in github profile, so i replace by "login" attribute.
    	//Otherwise with a null "name" we get : java.lang.IllegalArgumentException: principalName cannot be empty
        return oauth2User.getAttribute("login");
    }
 
}