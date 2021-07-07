package com.nnk.springboot.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

//Lombok
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor //note: args must be uninitialized final or annotated with lombok.NonNull
//JPA
@Entity
@Table(name = "rulename")
public class RuleName {

	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank
	@Size(max=125)
	@NonNull private String name;
	
	@NotBlank
	@Size(max=125)
	@NonNull private String description;
	
	@NotBlank
	@Size(max=125)
	@NonNull private String json;
	
	@NotBlank
	@Size(max=512)
	@NonNull private String template;
	
	@NotBlank
	@Size(max=125)
	@NonNull private String sqlStr;
	
	@NotBlank
	@Size(max=125)
	@NonNull private String sqlPart;
	

}
