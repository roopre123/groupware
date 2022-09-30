package com.jun.service;

import org.springframework.stereotype.Service;

import com.jun.model.Board;
import com.jun.repository.BoardRepository;


@Service
public class BoardService {
	
	private final BoardRepository boardRepository;
	public BoardService(BoardRepository boardRepository) {
		this.boardRepository = boardRepository;
	}
	
	public Board write(Board board) {
		return boardRepository.save(board);
	}
	

}
