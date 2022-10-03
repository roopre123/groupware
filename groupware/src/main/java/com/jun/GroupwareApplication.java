package com.jun;

import java.util.Date;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jun.model.Board;
import com.jun.model.User;
import com.jun.repository.BoardRepository;
import com.jun.repository.UserRepository;

@SpringBootApplication
public class GroupwareApplication {//implements CommandLineRunner{
	
	@Autowired
	BoardRepository board;
	
	@Autowired
	UserRepository user;
	

	public static void main(String[] args) {
		SpringApplication.run(GroupwareApplication.class, args);
	}
	
//	@Override
//	public void run(String... args) throws Exception {
//		User userr = user.findById("roopre123").get();
//		
//		IntStream.rangeClosed(1,150).forEach(index -> board.save(Board.builder()
//				.title("test" + index)
//				.content("Content" + index)
//				.wdate(new Date())
//				.user(userr).build()));
//		
//	}

}


