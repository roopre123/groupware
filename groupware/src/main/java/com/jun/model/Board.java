package com.jun.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Board {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "board_id")
	private Long id;
	
	@ManyToOne(cascade = CascadeType.MERGE, targetEntity = User.class)
	@JoinColumn(name = "username", updatable = false)
	@JsonBackReference
	@JsonIgnore
	private User user;
	
	private String title;
	
	private String content;
	
	private Date wdate;
	@PrePersist
	public void beforeCreate() {
		wdate = new Date();  
	}
	private String code;
	
	@OneToMany(
	    	   mappedBy = "board",
	    	   cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
	    	   orphanRemoval = true
	    )
	private List<Files> files = new ArrayList<>();
	
	public void update(String title, String content) {
		this.title = title;
		this.content = content;
	}

}
