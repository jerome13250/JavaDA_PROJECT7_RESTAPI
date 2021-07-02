package com.nnk.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Spring Security configuration :  
 * <a href="https://www.marcobehler.com/guides/spring-security#_how_to_configure_spring_security_websecurityconfigureradapter">
 * how_to_configure_spring_security
 * </a>
 * 
 * @author jerome
 *
 */

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Spring Security needs to have a PasswordEncoder defined.
     * @return PasswordEncoder that uses the BCrypt 
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.authorizeRequests()
    	.antMatchers("/js/**","/css/**", "/img/**","/favicon.ico").permitAll() //css, js and images allowed for all users
    	.antMatchers("/", "/user/**", "/app/login").permitAll() //Homepage, user pages, app/login are allowed for all users
    	.antMatchers("/bidList/**", "/curvePoint/**", "/rating/**", "/ruleName/**", "/trade/**", "/app/secure/**").authenticated()
    	.antMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN","ADMIN") //admin path reserved to ADMIN
    	.anyRequest().denyAll()
    	.and()
    	.formLogin().permitAll().defaultSuccessUrl("/bidList/list")
    	.and()
    	.logout().logoutUrl("/app-logout").logoutSuccessUrl("/").permitAll()
         ;
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }
}
