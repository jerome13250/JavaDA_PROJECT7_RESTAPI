package com.nnk.springboot.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.sql.Timestamp;

//Lombok
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor //note: args must be uninitialized final or annotated with lombok.NonNull
//JPA
@Entity
@Table(name = "trade")
public class Trade {

	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
	Integer tradeId;
	
	@NotBlank
	@NonNull String account;
	
	@NotBlank
	@NonNull String type;
	
	@NotNull
	@NonNull Double buyQuantity;
	
	Double sellQuantity;
	Double buyPrice;
	Double sellPrice;
	String benchmark;
	Timestamp tradeDate;
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
