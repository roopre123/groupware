package com.jun.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jun.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	
	public User findByUsername(String username);
	public boolean existsByCode(String code);
	
	public User findCodeByUsername(String username);

}
