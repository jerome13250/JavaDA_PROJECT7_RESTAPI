package com.nnk.springboot.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * BusinessResourceException is the exception type we'll use in the application to handle all errors that are not simple form validations. 
 * 
 * @author jerome
 *
 */

@Getter
@Setter
public class BusinessResourceException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private Long resourceId;
    private String errorCode;
    private HttpStatus status;

    public BusinessResourceException(String message) {
        super(message);
    }
    
    public BusinessResourceException(Long resourceId, String message) {
        super(message);
        this.resourceId = resourceId;
    }
    public BusinessResourceException(Long resourceId, String errorCode, String message) {
        super(message);
        this.resourceId = resourceId;
        this.errorCode = errorCode;
    }
    
    public BusinessResourceException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public BusinessResourceException(String errorCode, String message, HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }


}