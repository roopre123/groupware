package com.jun.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jun.model.Board;

public interface BoardRepository extends JpaRepository<Board, Long>{
	
	//Board List 띄우기 
	public Page<Board> findAllByUser_code(String code, Pageable page);
	
}
