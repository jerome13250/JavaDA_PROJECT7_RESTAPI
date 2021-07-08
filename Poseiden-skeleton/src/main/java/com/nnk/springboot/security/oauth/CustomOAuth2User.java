package com.nnk.springboot.security.oauth;

import java.util.Collection;
import java.util.Map;
 
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;


/**
 * This class implements OAuth2User and also wraps an instance of OAuth2User, which will be passed by Spring OAuth2 
 * upon successful OAuth authentication. We override the getName() method to return username associated with GitHub account.
 * <p>
 * Original code from: 
 * <a href="https://www.codejava.net/frameworks/spring-boot/oauth2-login-with-github-example">
 * www.codejava.net
 * </a>
 * </p>
 * 
 */
public class CustomOAuth2User implements OAuth2User {
 
    private OAuth2User oauth2User;
     
    public CustomOAuth2User(OAuth2User oauth2User) {
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
        return oauth2User.getAttribute("name");
    }
 
}