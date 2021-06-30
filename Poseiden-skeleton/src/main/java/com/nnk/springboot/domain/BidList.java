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
	Integer BidListId;
	
	@NotBlank
	@NonNull String account;
	
	@NotBlank
	@NonNull String type;
	
	@NotNull
	@Digits(fraction = 0, integer = 22)
	@NonNull Double bidQuantity;
	
	Double askQuantity;
	Double bid;
	Double ask;
	String benchmark;
	Timestamp bidListDate;
	String commentary;
	String security;
	String status;
	String trader;
	String book;
	String creationName;
	Timestamp creationDate;
	String revisionName;
	Timestamp revisionDate;
	String dealName;
	String dealType;
	String sourceListId;
	String side;

}
