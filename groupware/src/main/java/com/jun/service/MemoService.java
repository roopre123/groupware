package com.jun.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jun.model.Memo;
import com.jun.repository.MemoRepository;

@Service
public class MemoService {

	
	private final MemoRepository memoRepository;
	public MemoService(MemoRepository memoRepository) {
		this.memoRepository = memoRepository;
	}
	
	public Memo memoWrite(Memo memo) {
		Memo memoRe = memoRepository.save(memo);
		return memoRe;
	}
	
	public List<Memo> findAllByUser_code(String code){
		return memoRepository.findAllByUser_code(code);
	} 
	
	public void deleteMemo(Long id) {
		memoRepository.deleteById(id);
	}
	
}
