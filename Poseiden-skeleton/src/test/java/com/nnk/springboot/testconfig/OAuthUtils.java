package com.nnk.springboot.testconfig;

import static java.util.Arrays.asList;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;


/**
 * Utility class that creates OAuth2User and Authentication for controller unit testing of OAuth2.
 * 
 * <p>
 * Author article on 
 * <a href="https://medium.com/@mark.hoogenboom/testing-a-spring-boot-application-secured-by-oauth-e40d1e9a6f60">
 * medium.com
 * </a>
 * <br>
 * Original code on 
 * <a href="https://github.com/mark-hoogenboom/spring-boot-oauth-testing/blob/master/src/test/java/com/robinfinch/oslo/test/OAuthUtils.java">
 * github
 * </a>
 * </p>
 * 
 * @author Mark Hoogenboom 
 *
 */
public class OAuthUtils {

    public static OAuth2User createOAuth2User(String name, String email) {

        Map<String, Object> authorityAttributes = new HashMap<>();
        authorityAttributes.put("key", "value");

        //this creates a ROLE_USER authority, authorityAttributes seems useless...
        GrantedAuthority authority = new OAuth2UserAuthority(authorityAttributes);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", "1234567890");
        attributes.put("name", name);
        attributes.put("email", email);

        return new DefaultOAuth2User(asList(authority), attributes, "name");
    }

}
