package com.nnk.springboot.testconfig;

import java.util.Arrays;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.nnk.springboot.config.security.oauth.CustomOAuth2UserService;


//Configuration classes annotated with @TestConfiguration are excluded from component scanning, therefore we need to import it
//explicitly in every test where we want to @Autowire it. We can do that with the @Import annotation.
@TestConfiguration
public class SpringWebUnitTestConfig {

	/**
	 * Need to create a UserDetailsService in SpringSecurityWebTestConfig.class because @Service are not loaded by @WebMvcTest
	 * @return a UserDetailsService ( InMemoryUserDetailsManager )
	 */
	@Bean
	public UserDetailsService userDetailsService() {
		User basicUser = new User("user@company.com", "password", Arrays.asList(
				new SimpleGrantedAuthority("ROLE_USER")
				));
		return new InMemoryUserDetailsManager(Arrays.asList(
				basicUser /*, managerActiveUser*/
				));
	}


	/**
	 * This MockBean is needed because when we add .oauth2Login() to WebSecurityConfig.configure, we can't execute any Controller tests (unit or integration) because of :<br>
	 * <p><i>NoSuchBeanDefinitionException: No qualifying bean of type 'org.springframework.security.oauth2.client.registration.ClientRegistrationRepository' available</i></p>
	 * For our unit test we are using @WebMvcTest, the solution is to declare a mock ClientRegistrationRepository.
	 * Solution comes from this 
	 * <a href ="https://medium.com/@mark.hoogenboom/testing-a-spring-boot-application-secured-by-oauth-e40d1e9a6f60">
	 * medium article.
	 * </a>
	 * Original code on 
	 * <a href ="https://github.com/mark-hoogenboom/spring-boot-oauth-testing/blob/master/src/test/java/com/robinfinch/oslo/web/MyControllerTests.java">
	 * github
	 * </a>
	 */
	@MockBean
	private ClientRegistrationRepository clientRegistrationRepository;

	/**
	 * We have introduced a custom class in WebSecurityConfig : .oauth2Login().userInfoEndpoint().userService(customOAuth2UserService).
	 * We need a mock to be able to create bean webSecurityConfig.
	 */
	@MockBean
	private CustomOAuth2UserService customOAuth2UserService;


}
