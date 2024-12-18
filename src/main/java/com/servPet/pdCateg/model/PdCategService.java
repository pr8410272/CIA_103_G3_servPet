package com.servPet.pdCateg.model;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("pdCategService")
public class PdCategService {
	
	@Autowired
	PdCategRepository repository;
	
//	@Autowired
//	private SessionFactory sessionFactory;

	public PdCategService() {
	}

	public void addPdCateg(PdCategVO pdCategVO) {
		this.repository.save(pdCategVO);
	}

	public void updatePdCateg(PdCategVO pdCategVO) {
		this.repository.save(pdCategVO);
	}

	public void deletePdCateg(Integer pdCategory) {
		if (this.repository.existsById(pdCategory)) {
			this.repository.deleteById(pdCategory);
		}
	}

	public PdCategVO getOneEmp(Integer pdCategory) {
		Optional<PdCategVO> optional = this.repository.findById(pdCategory);
		return optional.orElse(null);
	}

	public List<PdCategVO> getAll() {
		return this.repository.findAll();
	}
	
	public PdCategVO getOneCategory(Integer pdCategoryId) {
	    return this.repository.findById(pdCategoryId).orElse(null);
	}


}