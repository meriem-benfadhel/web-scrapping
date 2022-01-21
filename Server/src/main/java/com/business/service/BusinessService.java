package com.business.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.business.entity.Business;
import com.business.repository.BusinessRepository;

@Service
public class BusinessService {
	
	private final BusinessRepository repos ;
	
	public BusinessService(BusinessRepository repos) {
        this.repos = repos;
    }
	
	public List<Business> findAllBusinesses() {
		Pageable p=Pageable.unpaged();
		int count = (int) repos.count();
		Page<Business> users = repos.findAll(new PageRequest(0, count));

        return repos.findAll(p).getContent();
    }

    public Business findBusinessByName(String name) {
        return repos.findBusinessByName(name);
    }

    public Business addBusiness(Business business) {
    	List<Business> liste = findAllBusinesses();
    	
        return liste.contains(business)? null : repos.save(business);
    }

    public void removeBusiness(Long businessId) {
       repos.deleteById(businessId);
    }

}
