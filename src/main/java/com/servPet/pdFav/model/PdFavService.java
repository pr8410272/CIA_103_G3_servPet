package com.servPet.pdFav.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.servPet.meb.model.MebVO;
import com.servPet.pdDetails.model.PdDetailsRepository;
import com.servPet.pdDetails.model.PdDetailsVO;
import com.servPet.pg.model.PgVO;
import com.servPet.pgFav.model.PgFavVO;

@Service ("pdFavService")
public class PdFavService {

    @Autowired
    PdFavRepository pdFavRepository;
    
    @Autowired
    PdDetailsRepository pdDetailsRepository;  // 此行檢查商品是否存在收藏列表中

 // 取得所有收藏商品列表
    public List<PdFavVO> getAllFavorites() {
        return pdFavRepository.findAll(); // 使用 JpaRepository 的內建方法
    }

    // 檢查是否已收藏商品
    public Optional<PdFavVO> checkIfFavorite(Integer mebId, Integer pdId) {
        return pdFavRepository.findByMebVO_MebIdAndPdDetailsVO_PdId(mebId, pdId);
    }

//    public String addFavorite(MebVO mebVO, PdDetailsVO pdDetailsVO) {
//        Optional<PdFavVO> existingFavorite = pdFavRepository.findByMebVO_MebIdAndPdVO_PdId(mebVO.getMebId(), pdDetailsVO.getPdId());
//        if (existingFavorite.isPresent()) {
//            return "商品已經收藏過囉！";
//        }
//        PdFavVO pdFavVO = new PdFavVO();
//        pdFavVO.setMebVO(mebVO);
//        pdFavVO.setPdDetailsVO(pdDetailsVO);
//        pdFavRepository.save(pdFavVO);
//        return "收藏成功";
//    }
    
 // 新增收藏
    @Transactional
    public String addFavorite(Integer mebId, Integer pdId) {
        // 確認商品是否存在
    	PdDetailsVO pdDetailsVO = pdDetailsRepository.findByPdId(pdId);
        if (pdDetailsVO == null) {
            return "商品不存在，無法收藏！";
        }

        // 檢查是否已經收藏
        Optional<PdFavVO> existingFavorite = pdFavRepository.findByMebVO_MebIdAndPdDetailsVO_PdId(mebId, pdId);
        if (existingFavorite.isPresent()) {
            return "商品已經收藏過囉！";
        }

        // 3. 創建新的收藏紀錄
        PdFavVO pdFavVO = new PdFavVO();
        MebVO mebVO = new MebVO();
        mebVO.setMebId(mebId); // 設定會員編號

        pdFavVO.setMebVO(mebVO);
        pdFavVO.setPdDetailsVO(pdDetailsVO);

        // 4. 儲存收藏紀錄
        pdFavRepository.save(pdFavVO);

        return "收藏成功！";
    }

    
    // 刪除收藏
    @Transactional
    public void deleteFavoriteById(Integer pdFavId) {
        pdFavRepository.deleteById(pdFavId);
    }
}