package com.servPet.psPor.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service("PsPorService")
public class PsPorService {

    @Autowired
    private PsPorRepository psPorRepository;

    // 新增作品
    public void addPsPor(PsPorVO psPorVO) {
        psPorRepository.save(psPorVO);
    }

    // 刪除該美容師單一作品
    public void deletePictureByPsIdAndPicId(Integer psId, Integer picId) {
        if (psPorRepository.existsById(picId)) {
            psPorRepository.deletePictureByPsIdAndPicId(psId, picId);
        }
    }

    // 一鍵刪除所有作品集（根據美容師 ID）
    public void deleteAllByPsId(Integer psId) {
        psPorRepository.deleteAllByPsId(psId);
    }

    // 查詢所有美容師的所有作品
    public List<PsPorVO> getAll() {
        return psPorRepository.findAll();
    }

    // 根據美容師 ID 查詢作品集
    public List<PsPorVO> getPsPorByPsId(Integer psId) {
        return psPorRepository.findByPsId(psId);
    }

    // 輸入美容師 ID 查詢作品集
    public PsPorVO getOnePsPor(Integer psId) { // 通過這個工具，可以更加安全和清晰地處理返回值為空的情況
        Optional<PsPorVO> optional = psPorRepository.findById(psId);
//  return optional.get();
        return optional.orElse(null); // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
    }

    // 新增多張圖片
    public void savePictures(Integer psId, List<byte[]> pictures) throws IOException {
        for (byte[] picture : pictures) {
            PsPorVO psPorVO = new PsPorVO();
            psPorVO.setPsId(psId); // 設定美容師 ID
            psPorVO.setPsSvcPic(picture);
            psPorRepository.save(psPorVO);
        }
    }

    public List<String> getPictureIdsByPsId(Integer psId) {
        return psPorRepository.findPictureIdsByPsId(psId);
    }
    public byte[] getPictureById(String picId) {
        // 假設圖片資料儲存在資料庫中，使用 Repository 查詢
        PsPorVO picture = psPorRepository.findById(Integer.valueOf(picId))
                .orElseThrow(() -> new RuntimeException("圖片未找到，ID: " + picId));
        return picture.getPsSvcPic(); // 回傳圖片二進制內容
    }

    // 根據 picId 刪除圖片
    public void deletePictureById(Integer picId) {
        psPorRepository.deleteById(picId);
    }

}