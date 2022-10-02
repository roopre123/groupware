package com.jun.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jun.model.Files;
import com.jun.repository.FilesRepository;

@Service
public class FilesService {

	private final FilesRepository filesRepository;
	public FilesService(FilesRepository filesRepository) {
		this.filesRepository = filesRepository;
	}
	
	public Files savefile(Files file) {
		return filesRepository.save(file);
	}
	
	public List<Files> findAllByBoard_id(Long board_id){
		return filesRepository.findAllByBoard_id(board_id);
	}
	
	public Files findOrigFileNameByPyscFileName(String pyscFileName) {
		return filesRepository.findOrigFileNameByPyscFileName(pyscFileName);
	}
}
