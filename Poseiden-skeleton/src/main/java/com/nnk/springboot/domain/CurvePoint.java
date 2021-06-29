package com.nnk.springboot.domain;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Lombok
@Getter
@Setter
@NoArgsConstructor
//JPA
@Entity
@Table(name = "curvepoint")
public class CurvePoint {

	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	Integer id;
	
	@NotNull
	@Min(value = -128)
	@Max(value = 127)
	Integer curveId;
	
	Timestamp asofDate;
	@NotNull
	Double term;
	@NotNull
	Double value;
	Timestamp creationDate;
	
	public CurvePoint (Integer curveId, Double term, Double value) {
		this.curveId = curveId;
		this.term = term;
		this.value = value;
	}

}
