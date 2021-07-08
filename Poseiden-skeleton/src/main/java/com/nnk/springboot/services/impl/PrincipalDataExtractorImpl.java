package com.nnk.springboot.services.impl;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

import com.nnk.springboot.services.PrincipalDataExtractor;

@Service
public class PrincipalDataExtractorImpl implements PrincipalDataExtractor {

	//@Autowired
	//OAuth2AuthorizedClientService oAuth2AuthorizedClientService;


	@Override
	public String getUserName(Principal principal) {

		StringBuilder userInfo= new StringBuilder();

		//in the case of "classic" authentication login/pwd
		if(principal instanceof UsernamePasswordAuthenticationToken){
			userInfo.append(getUsernamePasswordLoginInfo(principal));
		}
		//in the case of "oauth2" authentication
		else if(principal instanceof OAuth2AuthenticationToken){
			userInfo.append(getOauth2LoginInfo(principal));
		}
		return userInfo.toString();
	}

	/**
	 * This method cast a Principal to UsernamePasswordAuthenticationToken to get the user name.
	 * @param principal of the current request
	 * @return String user name 
	 */
	private String getUsernamePasswordLoginInfo(Principal principal)
	{
		UsernamePasswordAuthenticationToken token = ((UsernamePasswordAuthenticationToken) principal);
		if(token.isAuthenticated()){
			User u = (User) token.getPrincipal();
			return u.getUsername();
		}
		else{
			return "NA";
		}
	}

	/**
	 * This method cast a Principal to OAuth2AuthenticationToken to get the user name.
	 * @param principal of the current request
	 * @return String user name 
	 */
	private StringBuffer getOauth2LoginInfo(Principal principal){
		StringBuffer protectedInfo = new StringBuffer();

		//The OAuth2AuthenticationToken class has methods to use on resources in the user object:
		OAuth2AuthenticationToken authToken = ((OAuth2AuthenticationToken) principal);

		if(authToken.isAuthenticated()){

			Map<String,Object> userAttributes = ((DefaultOAuth2User) authToken.getPrincipal()).getAttributes();

			protectedInfo.append(authToken.getAuthorizedClientRegistrationId() + "-");
			protectedInfo.append(userAttributes.get("login"));
			
			/*
			//The authorized client has permission to access the protected resources like the access token:
			OAuth2AuthorizedClient oAuth2AuthorizedClient = this.oAuth2AuthorizedClientService.loadAuthorizedClient(
					authToken.getAuthorizedClientRegistrationId(), 
					authToken.getName()
					);
			String userToken = oAuth2AuthorizedClient.getAccessToken().getTokenValue();
			protectedInfo.append("Access Token: " + userToken+"<br><br>");
			*/
		}
		else{
			protectedInfo.append("NA");
		}
		return protectedInfo;
	}

}
