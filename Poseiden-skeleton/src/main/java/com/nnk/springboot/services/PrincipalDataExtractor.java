package com.nnk.springboot.services;

import java.security.Principal;

/**
 * This service class allows to extract data from Principal that can be different types of Tokens.
 * ( UsernamePasswordAuthenticationToken , OAuth2AuthenticationToken... ) 
 * <p>
 * Original code from 
 * <a href="https://openclassrooms.com/fr/courses/5683681-secure-your-web-application-with-spring-security/6695831-configure-oauth-2-0-with-openid-connect-on-a-spring-web-application">
 * openclassrooms
 * </a>
 * </p>
 * 
 * @author jerome
 *
 */
public interface PrincipalDataExtractor {

	public String getUserName(Principal principal);

}
