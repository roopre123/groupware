package com.jun.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Files{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "file_id")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "board_id")
	@JsonBackReference
	@JsonIgnore
	private Board board;
	
	private String origFileName;
	
	private String pyscFileName;
	
	private Long fileSize;
	
	public void setBoard(Board board) {
		this.board = board;
		
	}

	public Files(Board board, String origFileName,  Long fileSize) {
		super();
		this.board = board;
		this.origFileName = origFileName;
		this.fileSize = fileSize;
	}
	
	public Files(String origFileName,String pyscFileName,  Long fileSize) {
		super();
		this.origFileName = origFileName;
		this.pyscFileName = pyscFileName;
		this.fileSize = fileSize;
	}
	
	
}
