package com.nnk.springboot.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;

/**
 * This is the DTO object to display the error to the user
 * 
 * @author jerome
 *
 */

@Data
public class BusinessResourceExceptionDTO {
 
    private String errorCode;
    private String errorMessage;
	private String requestURL;
	private HttpStatus status;

}
