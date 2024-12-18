package com.servPet.pgPic.model;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PgPicService {

	@Autowired
	private PgPicRepository pgPicRepository;

	// 根據 picId 刪除圖片
	public void deletePictureById(Integer picId) {
		pgPicRepository.deleteById(picId);
	}

	// 根據美容師 ID 查詢所有作品集圖片
	public List<Integer> getPictureIdsByPgId(Integer pgId) {
		return pgPicRepository.getPictureIdsByPgId(pgId);
	}

	// 從資料庫或文件系統讀取圖片數據
	public byte[] getPictureById(Integer pgId) {

		return pgPicRepository.findPictureById(pgId);
	}

	// 新增多張圖片
	public void savePictures(Integer pgId, List<byte[]> pictures) throws IOException {
		for (byte[] picture : pictures) {
			PgPicVO pgPicVO = new PgPicVO();
			pgPicVO.setPgId(pgId); // 設定美容師 ID
			pgPicVO.setPgSvcPic(picture);
			pgPicRepository.save(pgPicVO);
		}

	}

}
