package com.servPet.pg.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.servPet.pgPic.model.PgPicRepository;
import com.servPet.pgPic.model.PgPicService;

//import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery;

@Service
public class PgService {

	@Autowired
	private PgRepository pgRepository;

	@Autowired
	PgPicRepository pgPicRepository;

	@Autowired
	PgPicService pgPicSvc;



	// 修改
	public void updatePg(PgVO pgVO) {
		pgRepository.save(pgVO);
	}

	// 根據美容師 ID 查詢
	public PgVO getOnePg(Integer pgId) { // 可更安全清晰的處理返回值為空的情況
		Optional<PgVO> optional = pgRepository.findById(pgId);
		return optional.orElse(null); // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	// 查詢所有美容師
	public List<PgVO> getAll() {
		List<PgVO> list = pgRepository.findAll();
		return list;
	}

	// 查看"營業中"的所有美容師
	public List<PgVO> getAllOnDuty() {
		return pgRepository.findByPgStatus("1");
	}

	// 根據美容師姓名查詢
	public List<PgVO> getOnePg(String pgName) {
		return pgRepository.findByNameLikeOrderByPgId(pgName);
	}

	// 管理員登錄(學雍
	public PgVO login(Integer integer, String pgPw) {
		PgVO pg = pgRepository.findByPgId(integer);
		if (pg != null && pgPw.equals(pg.getPgPw())) { // 直接比對密碼
			return pg; // 驗證成功
		} else {
			return null; // 登入失敗
		}
	}
}
