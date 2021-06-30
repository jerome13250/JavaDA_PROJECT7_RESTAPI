package com.nnk.springboot.domain;

import javax.persistence.*;
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

import java.sql.Timestamp;

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
	Integer id;
	
	@NotBlank
	@Size(max=125)
	@NonNull String moodysRating;
	
	@NotBlank
	@Size(max=125)
	@NonNull String sandPRating;
	
	@NotBlank
	@Size(max=125)
	@NonNull String fitchRating;
	
	@NotNull
	@Min(value = -128)
	@Max(value = 127)
	@NonNull Integer orderNumber;

}
