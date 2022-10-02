package com.jun.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
	
	public Page<Board> boardList(String code,Pageable pageable){
		pageable = PageRequest.of(
				pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() -1, 
				pageable.getPageSize(), 
				pageable.getSort());
		Page<Board> boardList = boardRepository.findAllByUser_code(code, pageable);
		return boardList;
	}
	
	public Board boardView(Long id) {
		return boardRepository.findById(id).get();
	}

}
