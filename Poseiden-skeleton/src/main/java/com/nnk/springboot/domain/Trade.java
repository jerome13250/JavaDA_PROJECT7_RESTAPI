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
	private Integer tradeId;
	
	@NotBlank
	@NonNull private String account;
	
	@NotBlank
	@NonNull private String type;
	
	@NotNull
	@NonNull private Double buyQuantity;
	
	private Double sellQuantity;
	private Double buyPrice;
	private Double sellPrice;
	private String benchmark;
	private Timestamp tradeDate;
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
