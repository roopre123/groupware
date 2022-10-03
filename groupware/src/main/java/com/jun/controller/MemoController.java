package com.jun.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jun.model.Memo;
import com.jun.model.User;
import com.jun.service.MemoService;
import com.jun.service.UserService;

@Controller
public class MemoController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MemoService memoService;

	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/memoPage")
	public String memoPage() {
		return "memo/memoPage";
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/memoWrite", method = RequestMethod.POST)
	public ResponseEntity<?> memoWrite(User user,@RequestBody Memo memo, Model model) {
		UserDetails userDetail = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User userSession = userService.findByUsername(userDetail.getUsername());
		memo.setUser(userSession);
		memo.setCode(userSession.getCode());
		memo.setMcontent(memo.getMcontent().replace("\r\n", "<br>"));
		Memo memoRe = memoService.memoWrite(memo);
		model.addAttribute("memo", memoRe);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/memoList", method = RequestMethod.POST)
	public String memoList(Model model) {
		
		UserDetails userDetail = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User userSession = userService.findByUsername(userDetail.getUsername());
		
		List<Memo> memoList = memoService.findAllByUser_code(userSession.getCode());
		
		model.addAttribute("memoList", memoList);
 		
		return "memo/memo";
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/memoDel", method = RequestMethod.POST)
	public ResponseEntity<?> memoDel(@RequestBody Memo memo){
		memoService.deleteMemo(memo.getMid());
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
