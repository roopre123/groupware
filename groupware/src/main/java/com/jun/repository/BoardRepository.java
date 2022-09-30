package com.jun.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jun.model.Board;

public interface BoardRepository extends JpaRepository<Board, Long>{

}
