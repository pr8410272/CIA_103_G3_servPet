package com.servPet.pgSvc.model;
//美容師服務清單

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PgSvcService {

	@Autowired
	private PgSvcRepository pgSvcRepository;
	

	// 新增服務項目
	public void addService(PgSvcVO pgSvcVO) {
		if (pgSvcVO.getSvcId() == null) {
			throw new IllegalArgumentException("SVC_ID 不能為空");
		}
		pgSvcRepository.save(pgSvcVO);
	}

	// 根據 PgSvcId 查詢單一服務項目
	public PgSvcVO getPgSvcById(Integer pgSvcId) {
		return pgSvcRepository.findById(pgSvcId).orElseThrow(() -> new IllegalArgumentException("查無此服務項目"));
	}

	// 刪除服務項目
	public void deletePgSvcByPgSvcId(Integer pgSvcId) {
		if (pgSvcRepository.existsById(pgSvcId)) {
			pgSvcRepository.deleteById(pgSvcId);
		} else {
			throw new IllegalArgumentException("資料不存在，無法刪除");
		}
	}

	// 指定服務清單編號的詳細資訊
	public PgServiceDetailsDTO getById(Integer pgSvcId) {
		List<Object[]> result = pgSvcRepository.findServicesByPgSvcId(pgSvcId);
		PgServiceDetailsDTO pgServiceDetailsDTO = new PgServiceDetailsDTO();

		for (Object[] o : result) {
			pgServiceDetailsDTO.setPgSvcId((Integer) o[0]);
			pgServiceDetailsDTO.setPgId((Integer) o[1]);
			pgServiceDetailsDTO.setSvcId((Integer) o[2]);
			pgServiceDetailsDTO.setSvcType((String) o[3]);
			pgServiceDetailsDTO.setSvcPrice((Integer) o[4]);
			pgServiceDetailsDTO.setSvcName((String) o[5]);
			pgServiceDetailsDTO.setSvcDescr((String) o[6]);
		}

		return pgServiceDetailsDTO;
	}

	// 更新服務項目
	public void update(PgSvcVO pgSvcVO) {
		// 直接使用 JpaRepository 的 save 方法更新數據
		pgSvcRepository.save(pgSvcVO);
	}

	// 根據美容師ID查詢服務清單及其詳細資料
	public List<PgServiceDetailsDTO> getServicesWithDetails(Integer pgId) {
		List<Object[]> resultList = pgSvcRepository.findServicesByPgIdWithItemDetails(pgId);
		List<PgServiceDetailsDTO> serviceDetailsList = new ArrayList<>();

		// 將查詢結果處理為 DTO
		for (Object[] result : resultList) {
			PgSvcVO pgSvc = (PgSvcVO) result[0];
			String svcName = (String) result[1];
			String svcDescr = (String) result[2];

			PgServiceDetailsDTO dto = new PgServiceDetailsDTO(pgSvc, svcName, svcDescr);
			serviceDetailsList.add(dto);
		}

		return serviceDetailsList;
	}
}



