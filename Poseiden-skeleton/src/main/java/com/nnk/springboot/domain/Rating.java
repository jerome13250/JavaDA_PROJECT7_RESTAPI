package com.nnk.springboot.domain;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
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
@Table(name = "rating")
public class Rating {

	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	Integer id;
	@NotBlank
	@Size(max=125)
	String moodysRating;
	@NotBlank
	@Size(max=125)
	String sandPRating;
	@NotBlank
	@Size(max=125)
	String fitchRating;
	@NotNull
	@Min(value = -128)
	@Max(value = 127)
	Integer orderNumber;

	public Rating(String moodysRating, String sandPRating, String fitchRating, Integer orderNumber) {
		this.moodysRating = moodysRating;
		this.sandPRating = sandPRating;
		this.fitchRating = fitchRating;
		this.orderNumber = orderNumber;
		
	}

}
