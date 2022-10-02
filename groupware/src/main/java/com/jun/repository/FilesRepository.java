package com.jun.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jun.model.Files;

public interface FilesRepository extends JpaRepository<Files, Long>{

	public List<Files> findAllByBoard_id(Long board_id);
	
	public Files findOrigFileNameByPyscFileName(String pyscFileName);
	
	public void deleteAllByBoard_id(Long board_id);
}
