package com.nnk.springboot.domain;

import org.springframework.beans.factory.annotation.Required;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.sql.Date;
import java.sql.Timestamp;

//Lombok
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor //note: args must be uninitialized final or annotated with lombok.NonNull
//JPA
@Entity
@Table(name = "bidlist")
public class BidList {

	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer BidListId;
	
	@NotBlank
	@NonNull private String account;
	
	@NotBlank
	@NonNull private String type;
	
	@NotNull
	@Digits(fraction = 0, integer = 22)
	@NonNull private Double bidQuantity;
	
	private Double askQuantity;
	private Double bid;
	private Double ask;
	private String benchmark;
	private Timestamp bidListDate;
	private String commentary;
	private String security;
	private String status;
	private String trader;
	private String book;
	private String creationName;
	private Timestamp creationDate;
	private String revisionName;
	private Timestamp revisionDate;
	private String dealName;
	private String dealType;
	private String sourceListId;
	private String side;

}
