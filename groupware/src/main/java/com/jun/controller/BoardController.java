package com.jun.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.jun.model.Board;
import com.jun.model.Files;
import com.jun.model.User;
import com.jun.service.BoardService;
import com.jun.service.FilesService;
import com.jun.service.UserService;

@Controller
public class BoardController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private FilesService filesService;
	
	private static final String Path = "/Users/roopre/data/projectPath";
	
	@RequestMapping(value = "/boardList", method = RequestMethod.GET)
	public String boardList() {
		System.out.println("boardListController");
		return "board/boardList";
	}
	
	@PreAuthorize("isAuthenticated()") //로그인한 사람이면 누구나 접근 가능
	@RequestMapping(value = "/boardForm", method = RequestMethod.GET)
	public String boardForm() {
		return "board/boardForm";
	}
//	@RequestMapping(value = "/boardWrite", method = RequestMethod.POST)
//	public ResponseEntity<?> boardWrite(@RequestParam("files") MultipartFile[] multiFiles,
//			Model model, Board board){
//		Map<String, Object> result = new HashMap<>();
//		System.out.println("test.....");
//		
//		try {
//			Files files = null;
//			String uuid, orginFileName, pyscFileName, physic alPath, fileExtension;
//			List<Files> fileList = new ArrayList<>();
//			
//			for (int i = 0; i< multiFiles.length; i++) {
//				if(!multiFiles[i].isEmpty()) {
//					
//					System.out.println("Path : " + Path);
//					System.out.println("file.getOriginalFilename : " + multiFiles[i].getOriginalFilename());
//					System.out.println("file size : " + multiFiles[i].getSize());
//					
//					uuid = UUID.randomUUID().toString();
//					orginFileName = multiFiles[i].getOriginalFilename();
//					fileExtension = orginFileName.substring(orginFileName.lastIndexOf("."), orginFileName.length());
//					pyscFileName = uuid + fileExtension;
//					physicalPath = Path + "/";
//					
//					files = new Files(orginFileName, pyscFileName, multiFiles[i].getSize());
//					fileList.add(files);
//					
//					System.out.println("dest : " + physicalPath + pyscFileName );
//					File dest = new File(physicalPath + pyscFileName);
//					multiFiles[i].transferTo(dest);
//					
//				}
//			}
//			result.put("success", true);
//			result.put("fileList", fileList);
//		}catch (Exception e){
//			result.put("success", false);
//			result.put("message", e.getMessage());
//		}
//		
//		return null;
//	}
	
	@RequestMapping(value = "/boardWrite", method = RequestMethod.POST)
	public ResponseEntity<?> boardWrite(@RequestParam("filess") List<MultipartFile> multiFiles,
			Model model,Board board,@RequestParam("username") String username){
		Map<String, Object> result = new HashMap<>();
		System.out.println("test.....");
		System.out.println(username);
		User user = userService.findByUsername(username);
		board.setUser(user);
		board.setCode(user.getCode());
		board.setContent(board.getContent().replace("\r\n", "<br>"));
		Board test = boardService.write(board);
		
		try {
			Files files = null;
			String uuid, orginFileName, pyscFileName, physicalPath, fileExtension;
			List<Files> fileList = new ArrayList<>();
			
			for (MultipartFile file : multiFiles) {
				if(!file.isEmpty()) {
					
					System.out.println("Path : " + Path);
					System.out.println("file.getOriginalFilename : " + file.getOriginalFilename());
					System.out.println("file size : " + file.getSize());
					
					uuid = UUID.randomUUID().toString();
					orginFileName = file.getOriginalFilename();
					fileExtension = orginFileName.substring(orginFileName.lastIndexOf("."), orginFileName.length());
					pyscFileName = uuid + fileExtension;
					physicalPath = Path + "/";
						
					files = new Files(orginFileName, pyscFileName, file.getSize());
					fileList.add(files);
					files.setBoard(test);
					
					System.out.println("dest : " + physicalPath + pyscFileName );
					File dest = new File(physicalPath + pyscFileName);
					file.transferTo(dest);
					filesService.savefile(files);
					
				}
			}
			//System.out.println("t	q"+test.toString());
			result.put("success", true);
			result.put("fileList", fileList);
		}catch (Exception e){
			result.put("success", false);
			result.put("message", e.getMessage());
		}	
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
