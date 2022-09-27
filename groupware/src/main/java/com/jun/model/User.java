package com.jun.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Entity
@Data
public class User {
	
	@Id
	private String username;
	private String password;
	private String nickname;
	private String code;
	@CreationTimestamp
	private Timestamp createDate;
	private String role;
	
	public User() { }	
	
	
	
	
	public User(String username, String nickname, String password, String code, String role) {
		super();
		this.username = username;
		this.nickname = nickname;
		this.password = password;
		this.code = code;
		this.role = role;
	}
	
}
