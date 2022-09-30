package com.jun.service;

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
}
