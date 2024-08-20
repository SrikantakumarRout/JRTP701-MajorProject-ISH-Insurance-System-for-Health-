package com.nt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.entity.DcCaseEntity;

public interface IDcCaseRepository extends JpaRepository<DcCaseEntity, Integer> {
	public Integer findByCaseNo(int caseNo);

}
