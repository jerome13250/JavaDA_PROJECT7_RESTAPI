package com.nnk.springboot.testconfig;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;


//Configuration classes annotated with @TestConfiguration are excluded from component scanning, therefore we need to import it
//explicitly in every test where we want to @Autowire it. We can do that with the @Import annotation.
@TestConfiguration
public class SpringWebIntegrationTestConfig {

	/**
	 * This MockBean is needed because when we add .oauth2Login() to WebSecurityConfig.configure, we can't execute any Controller tests (unit or integration) because of :<br>
	 * <p><i>NoSuchBeanDefinitionException: No qualifying bean of type 'org.springframework.security.oauth2.client.registration.ClientRegistrationRepository' available</i></p>
	 * For our integration test we are using @SpringBootTest with @AutoConfigureMockMvc, we bypass the authentication process altogether, that's why we can use @MockBean for ClientRegistrationRepository
	 * Solution comes from this 
	 * <a href ="https://tanzu.vmware.com/content/pivotal-engineering-journal/faking-oauth2-single-sign-on-in-spring-3-ways">
	 * vmware article
	 * </a>
	 */
	@MockBean
	private ClientRegistrationRepository clientRegistrationRepository;

}
