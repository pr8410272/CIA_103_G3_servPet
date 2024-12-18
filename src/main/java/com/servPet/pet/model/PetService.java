package com.servPet.pet.model;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PetService {

	@Autowired
	private PetRepository petRepository;

	
	public List<PetVO> getPetsByMebId(Integer mebId) {
		return petRepository.findPetsByMebId(mebId);
	}
	
	// 根據會員 ID 查詢寵物(改方法名
	public void savePetFromDTO(PetDTO petDTO, MultipartFile petImageFile) throws IOException {
	    PetVO petVO = new PetVO();

	    // DTO to Entity 映射
	    petVO.setPetName(petDTO.getPetName());
	    petVO.setPetType(petDTO.getPetType());
	    petVO.setMebId(petDTO.getMebId());

	    // 處理圖片數據存儲
	    if (!petImageFile.isEmpty()) {
	        byte[] imageBytes = saveImageAsBytes(petImageFile);
	        petVO.setPetImage(imageBytes);
	    }

	    // 保存到數據庫
	    petRepository.save(petVO);
	}
	public List<PetVO> getPetsByMemberId(Integer mebId) {
	    return petRepository.findByMebId(mebId);
	}
	

    public Optional<PetVO> findPetById(Integer petId) {
        return petRepository.findById(petId);
    }
	// 查詢所有寵物
	public List<PetVO> getAllPets() {
		return petRepository.findAll();
	}

	// 根據 ID 查詢寵物
	public PetVO getPetById(Integer petId) {
		return petRepository.findById(petId).orElse(null);
	}

	// 更新寵物資料
	public void updatePet(PetVO pet) {
	    petRepository.save(pet);
	}


	// 刪除寵物
	public void deletePet(Integer petId) {
		petRepository.deleteById(petId);
	}
	
	
	private byte[] saveImageAsBytes(MultipartFile file) throws IOException {
	    return file.getBytes(); // 使用 Spring 提供的 MultipartFile 方法直接讀取為字節數組
	}
}
