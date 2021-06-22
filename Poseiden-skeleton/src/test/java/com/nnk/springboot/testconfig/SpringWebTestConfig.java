package com.nnk.springboot.testconfig;

import java.util.Arrays;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;


//Configuration classes annotated with @TestConfiguration are excluded from component scanning, therefore we need to import it
//explicitly in every test where we want to @Autowire it. We can do that with the @Import annotation.
@TestConfiguration
public class SpringWebTestConfig {

	//Need to create a UserDetailsService in SpringSecurityWebTestConfig.class because @Service are not loaded by @WebMvcTest
	@Bean
	public UserDetailsService userDetailsService() {
		User basicUser = new User("user@company.com", "password", Arrays.asList(
				new SimpleGrantedAuthority("ROLE_USER")
				));
		return new InMemoryUserDetailsManager(Arrays.asList(
				basicUser /*, managerActiveUser*/
				));
	}
	
	
}
