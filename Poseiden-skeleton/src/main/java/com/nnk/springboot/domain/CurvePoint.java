package com.nnk.springboot.domain;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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
@Table(name = "curvepoint")
public class CurvePoint {

	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull
	@Min(value = -128)
	@Max(value = 127)
	@NonNull private Integer curveId;
	
	private Timestamp asofDate;
	
	@NotNull
	@NonNull private Double term;
	
	@NotNull
	@NonNull private Double value;
	
	private Timestamp creationDate;
	
}
