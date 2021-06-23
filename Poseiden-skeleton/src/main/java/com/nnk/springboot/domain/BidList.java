package com.nnk.springboot.domain;

import org.springframework.beans.factory.annotation.Required;

import lombok.Getter;
import lombok.NoArgsConstructor;
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
//JPA
@Entity
@Table(name = "bidlist")
public class BidList {

	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	Integer BidListId;
	
	@NotBlank
	String account;
	@NotBlank
	String type;
	@NotNull
	@Digits(fraction = 0, integer = 22)
	Double bidQuantity;
	
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


	public BidList(String account​, String type, Double bidQuantity) {
		this.account= account​;
		this.type = type;
		this.bidQuantity = bidQuantity;
	}

}
