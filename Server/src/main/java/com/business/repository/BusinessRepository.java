package com.business.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.business.entity.Business;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long>{
	
	Business findBusinessByName(String name);
	Page<Business> findAll(Pageable page);
}
