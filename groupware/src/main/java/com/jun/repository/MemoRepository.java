package com.jun.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jun.model.Memo;

public interface MemoRepository extends JpaRepository<Memo, Long>{

	public List<Memo> findAllByUser_code(String code);
	
}
