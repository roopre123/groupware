package com.jun.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jun.model.User;
import com.jun.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = {"","/"}, method = RequestMethod.GET)
	public String hello() {
		return "index";
	}
	
	@RequestMapping(value="/loginForm", method=RequestMethod.GET)
	public String loginForm() {
		return "loginForm";
	}
	
	@RequestMapping(value="/joinForm", method = RequestMethod.GET)
	public String joinForm() {
		return "joinForm";
		
	}
	@ResponseBody
	@RequestMapping(value = "/userjoin", method = RequestMethod.POST)
	public User join(@RequestBody User user) {
		
		user.setRole("ROLE_USER");
		
		/* 코드가 없으면
		 * 8자리 랜덤한 글을 생성해 고유 코드로 사용함  
		 * 데이터 베이스에서 확인 해 코드가 있으면 회원가입 진행하고 
		 * 코드가 없으면 객체를 null로 변경 후 폼으로 전송*/
		if(user.getCode() == null) {
			UUID temp = UUID.randomUUID();
			String strTemp = temp.toString();
			strTemp = strTemp.substring(0,8);
			user.setCode(strTemp);
			User userRe = userService.join(user);
			return userRe;
		}
		else {
			if(userService.existsByCode(user.getCode())) {
				User userRe = userService.join(user);
				return userRe;
			}
			else {
				userNullCode(user);
				return user;
			}
		}
	}
	
	public User userNullCode(User user) {
		user.setCode(null);
		return user;
	}

}
