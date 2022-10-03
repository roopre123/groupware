package com.jun.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Memo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long mid;
	
	@ManyToOne(cascade = CascadeType.MERGE, targetEntity = User.class)
	@JoinColumn(name = "username", updatable = false)
	@JsonBackReference
	@JsonIgnore
	private User user;
	
	private String mcontent;
	
	private Date mdate;
	
	@PrePersist
	public void beforeCreate() {
		mdate = new Date();
	}
	
	private String code;

	public Memo(String mcontent) {
		super();
		this.mcontent = mcontent;
	}
	
	
	
}
