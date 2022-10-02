package com.jun.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
	
	private static final String Path = "/Users/roopre/data/projectPath/";
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/boardList", method = RequestMethod.GET)
	public String boardList(@PageableDefault (size =10, sort="id", direction= Sort.Direction.DESC ) Pageable pageable,
			Model model, Board board, User user) {
		System.out.println("boardListController");
		
		UserDetails userDetail = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User userCode = userService.findCodeByUsername(userDetail.getUsername());
		
		Page<Board> boardList = boardService.boardList(userCode.getCode(),pageable);
		model.addAttribute("boardList", boardList);
		
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
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/boardWrite", method = RequestMethod.POST)
	public ResponseEntity<?> boardWrite(@RequestParam("filess") List<MultipartFile> multiFiles,
			Model model,Board board,@RequestParam("username") String username){
		
		Map<String, Object> result = new HashMap<>();
		
		System.out.println("boardWrite()");
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
					
					uuid = UUID.randomUUID().toString();
					orginFileName = file.getOriginalFilename();
					fileExtension = orginFileName.substring(orginFileName.lastIndexOf("."), orginFileName.length());
					pyscFileName = uuid + fileExtension;
					physicalPath = Path;
						
					files = new Files(orginFileName, pyscFileName, file.getSize());
					fileList.add(files);
					files.setBoard(test);
					
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
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value =  "/boardView", method = RequestMethod.GET)
	public String boardView(Board board, Model model) {
		Board boardView = boardService.boardView(board.getId());
		List<Files> files = filesService.findAllByBoard_id(board.getId());
		
		UserDetails userDetail = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = userService.findByUsername(userDetail.getUsername());
		
		model.addAttribute("user",user);
		model.addAttribute("board",boardView);
		model.addAttribute("files",files);
		
		return "board/boardView";
		
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/fileDown", method = RequestMethod.POST)
	public void download(HttpServletResponse response, Files files) throws IOException{
		String downPath = Path + files.getPyscFileName();
		String origName = filesService.findOrigFileNameByPyscFileName(files.getPyscFileName()).getOrigFileName();
		byte[] fileByte = FileUtils.readFileToByteArray(new File(downPath));
		
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(origName, "UTF-8")+"\";");
	    response.setHeader("Content-Transfer-Encoding", "binary");

	    response.getOutputStream().write(fileByte);
	    response.getOutputStream().flush();
	    response.getOutputStream().close();
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/boardUpdateForm", method = RequestMethod.POST)
	public String boardUpdate(Board board, Model model) {
		Board boardView = boardService.boardView(board.getId());
		List<Files> files = filesService.findAllByBoard_id(board.getId());
		
		UserDetails userDetail = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = userService.findByUsername(userDetail.getUsername());
		
		model.addAttribute("user",user);
		model.addAttribute("board",boardView);
		model.addAttribute("fileList",files);
		return "board/boardForm";
	}
	
	@PreAuthorize("isAuthenticated()")
	@ResponseBody
	@RequestMapping(value = "/boardUpdate", method = RequestMethod.POST)
	public Long boardUPdate(Board board, User user, @RequestParam("filess") List<MultipartFile> multiFiles, Model model) throws IllegalStateException, IOException {
		
		UserDetails userDetail = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User userSession = userService.findByUsername(userDetail.getUsername());
		
		board.setUser(userSession);
		board.setCode(userSession.getCode());
		board.setWdate(new Date());
		board.setContent(board.getContent().replace("\r\n", "<br>"));
		List<Files> boardFileList = filesService.findAllByBoard_id(board.getId());
		Board test = boardService.write(board);
		
		
		Files files = null;
		String uuid, orginFileName, pyscFileName, physicalPath, fileExtension;
		List<Files> fileList = new ArrayList<>();
		
		for(MultipartFile file : multiFiles) {
			if( file == null || file.isEmpty() ) {
				System.out.println("여기가 널인데? ");
				for(Files f : boardFileList) {
					filesService.savefile(f);
				}
			}else {
				System.out.println("................-------->?");
				uuid = UUID.randomUUID().toString();
				orginFileName = file.getOriginalFilename();
				fileExtension = orginFileName.substring(orginFileName.lastIndexOf("."), orginFileName.length());
				pyscFileName = uuid + fileExtension;
				physicalPath = Path;
					
				files = new Files(orginFileName, pyscFileName, file.getSize());
				fileList.add(files);
				files.setBoard(test);
				
				File dest = new File(physicalPath + pyscFileName);
				file.transferTo(dest);
				filesService.savefile(files);
			}
		}
		
		model.addAttribute("user",user);
		model.addAttribute("board",board);
		model.addAttribute("files",fileList);
		
		return test.getId();
	}
	
	@PreAuthorize("isAuthenticated()")
	@Transactional
	@RequestMapping(value = "/boardDelete" , method = RequestMethod.POST)
	public ResponseEntity<?> boardDelete(@RequestBody Board board) {
		List<Files> boardFileList = filesService.findAllByBoard_id(board.getId());
		System.out.println(board.toString());
		File file;
		for (Files myFile : boardFileList) {
			file = new File( Path + myFile.getPyscFileName() );
			file.delete();
		}
		filesService.deleteAllByBoard_id(board.getId());
		boardService.boardDelete(board.getId());
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
		
		
	
}
