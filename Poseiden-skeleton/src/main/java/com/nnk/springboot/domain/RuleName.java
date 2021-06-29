package com.nnk.springboot.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

//Lombok
@Getter
@Setter
@NoArgsConstructor
//JPA
@Entity
@Table(name = "rulename")
public class RuleName {

	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	Integer id;
	@NotBlank
	@Size(max=125)
	String name;
	@NotBlank
	@Size(max=125)
	String description;
	@NotBlank
	@Size(max=125)
	String json;
	@NotBlank
	@Size(max=512)
	String template;
	@NotBlank
	@Size(max=125)
	String sqlStr;
	@NotBlank
	@Size(max=125)
	String sqlPart;
	
	public RuleName(String name, String description, String json, String template, String sqlStr, String sqlPart) {
		super();
		this.name = name;
		this.description = description;
		this.json = json;
		this.template = template;
		this.sqlStr = sqlStr;
		this.sqlPart = sqlPart;
	}
	
	
}
