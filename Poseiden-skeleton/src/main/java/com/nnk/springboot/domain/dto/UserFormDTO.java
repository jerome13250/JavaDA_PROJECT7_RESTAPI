package com.nnk.springboot.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This is the DTO version of the JPA Entity User.
 * <p>Note that beside the well-known vulnerability :
 * <a href="https://jira.sonarsource.com/browse/RSPEC-4684">
 * Persistent entities should not be used as arguments of "@RequestMapping" methods.
 * </a> that leads to use a DTO instead of directly an Entity
 * ,in this user object we get a password from web form that must respect all our validations but we store in the Entity
 *  an encrypted password that can not follow the same validations.
 * For this reason, we absolutely need to split the Entity and DTO.
 * @author jerome
 *
 */
//Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserFormDTO {
	
	private Integer id;
    @NotBlank(message = "Username is mandatory")
    private String username;
    
    @NotBlank(message = "Password is mandatory")
    @Pattern(regexp = ".*[0-9].*", message = "Must contain at least 1 digit.")
	@Pattern(regexp = ".*[a-z].*", message = "Must contain at least 1 lower case letter")
	@Pattern(regexp = ".*[A-Z].*", message = "Must contain at least 1 capital letter")	
    //Note that "-" is at the end (because otherwise it'd be a range):
    //https://stackoverflow.com/questions/10173460/regular-expression-include-and-exclude-special-characters
    @Pattern(regexp = ".*[@#$%^&_()+*=-].*", message = "Must contain at least 1 special character: @#$%^&_()+*=-")
    @Size(min=8,max=20)
    private String password;
    
    @NotBlank(message = "FullName is mandatory")
    private String fullname;
    
    @NotBlank(message = "Role is mandatory")
    @Pattern(regexp = "USER|ADMIN", message = "Must be USER or ADMIN.")
    private String role;
	
}
