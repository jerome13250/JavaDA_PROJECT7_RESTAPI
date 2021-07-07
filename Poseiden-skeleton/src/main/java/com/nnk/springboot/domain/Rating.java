package com.nnk.springboot.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
@Table(name = "rating")
public class Rating {

	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank
	@Size(max=125)
	@NonNull private String moodysRating;
	
	@NotBlank
	@Size(max=125)
	@NonNull private String sandPRating;
	
	@NotBlank
	@Size(max=125)
	@NonNull private String fitchRating;
	
	@NotNull
	@Min(value = -128)
	@Max(value = 127)
	@NonNull private Integer orderNumber;

}
