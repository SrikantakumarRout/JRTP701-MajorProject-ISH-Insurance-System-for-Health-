package com.nt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.entity.DcEducationEntity;

public interface IDcEducationRepository extends JpaRepository<DcEducationEntity, Integer> {
	public DcEducationEntity findByCaseNo(int caseNo);

}
