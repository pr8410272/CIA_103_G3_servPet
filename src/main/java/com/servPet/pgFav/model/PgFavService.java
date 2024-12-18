package com.servPet.pgFav.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.servPet.meb.model.MebRepository;
import com.servPet.meb.model.MebVO;
import com.servPet.pdDetails.model.PdDetailsVO;
import com.servPet.pdFav.model.PdFavVO;
import com.servPet.pg.model.PgRepository;
import com.servPet.pg.model.PgVO;

@Service ("pgFavService")
public class PgFavService {

    @Autowired
    private PgFavRepository pgFavRepository;
    
    @Autowired
    private PgRepository pgRepository;
    
    // 取得所有收藏美容師列表
    public List<PgFavVO> getAllFavorites() {
        return pgFavRepository.findAll(); // 使用 JpaRepository 的內建方法
    }

    // 檢查是否已收藏
    public Optional<PgFavVO> checkIfFavorite(Integer mebId, Integer pgId) {
        return pgFavRepository.findByMebVO_MebIdAndPgVO_PgId(mebId, pgId);
    }

//    public String addFavorite(MebVO mebVO, PgVO pgVO) {
//        Optional<PgFavVO> existingFavorite = pgFavRepository.findByMebVO_MebIdAndPgVO_PgId(mebVO.getMebId(), pgVO.getPgId());
//        if (existingFavorite.isPresent()) {
//            return "該美容師已在收藏列表中";
//        }
//        PgFavVO pgFavVO = new PgFavVO();
//        pgFavVO.setMebVO(mebVO);
//        pgFavVO.setPgVO(pgVO);
//        pgFavRepository.save(pgFavVO);
//        return "收藏成功";
//    }
    
//    @Transactional
//    public String addFavorite(Integer mebId, Integer pgId) {
//        // 使用自定義方法
//        PgVO pgVO = pgRepository.findByPgId(pgId);
//        if (pgVO == null) {
//            return "美容師不存在，無法收藏！";
//        }
//
//        Optional<PgFavVO> existingFavorite = pgFavRepository.findByMebVO_MebIdAndPgVO_PgId(mebId, pgId);
//        if (existingFavorite.isPresent()) {
//            return "已經收藏過這位美容師囉！";
//        }
//
//        PgFavVO pgFavVO = new PgFavVO();
//        MebVO mebVO = new MebVO();
//        mebVO.setMebId(mebId);
//
//        pgFavVO.setMebVO(mebVO);
//        pgFavVO.setPgVO(pgVO);
//
//        pgFavRepository.save(pgFavVO);
//
//        return "收藏成功！";
//    }
    
    // 新增收藏
    @Transactional
    public String addFavorite(Integer mebId, Integer pgId) {
        // 1. 確認指定的美容師是否存在
        PgVO pgVO = pgRepository.findByPgId(pgId);
        if (pgVO == null) {
            return "美容師不存在，無法收藏！";
        }

        // 2. 檢查是否已經收藏
        Optional<PgFavVO> existingFavorite = pgFavRepository.findByMebVO_MebIdAndPgVO_PgId(mebId, pgId);
        if (existingFavorite.isPresent()) {
            return "該美容師已經收藏過囉！";
        }

        // 3. 創建新的收藏紀錄
        PgFavVO pgFavVO = new PgFavVO();
        MebVO mebVO = new MebVO();
        mebVO.setMebId(mebId); // 設定會員編號

        pgFavVO.setMebVO(mebVO);
        pgFavVO.setPgVO(pgVO);

        // 4. 儲存收藏紀錄
        pgFavRepository.save(pgFavVO);

        return "收藏成功！";
    }

    
    // 刪除收藏
    @Transactional
    public void deleteFavoriteById(Integer pgFavId) {
        pgFavRepository.deleteById(pgFavId);
    }
}