package com.nnk.springboot.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration that loads a modelMapper bean. It is needed for DTO/Entity conversion.
 * 
 * @author jerome
 *
 */
@Configuration
public class SpringConfig {
    
	/**
	 * This is needed for mapping Entity to DTO or DTO to Entity.
	 * 
	 * <p>
	 * Source in this 
	 * <a href="https://www.baeldung.com/entity-to-and-from-dto-for-a-java-spring-application">
	 * Baeldung article
	 * </a>
	 * </p>
	 * 
	 * @return ModelMapper object
	 */

	@Bean
	public ModelMapper modelMapper() {
	    return new ModelMapper();
	}

	
}