package com.nnk.springboot.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

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
	Integer id;
	
	@NotBlank
	@Size(max=125)
	@NonNull String name;
	
	@NotBlank
	@Size(max=125)
	@NonNull String description;
	
	@NotBlank
	@Size(max=125)
	@NonNull String json;
	
	@NotBlank
	@Size(max=512)
	@NonNull String template;
	
	@NotBlank
	@Size(max=125)
	@NonNull String sqlStr;
	
	@NotBlank
	@Size(max=125)
	@NonNull String sqlPart;
	

}
