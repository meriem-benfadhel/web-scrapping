package com.business.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity( name ="Business")
public class Business {
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	private Long id;
	private String category;
	private String name;
	private String phoneNumber;
	private String address;
	
	
	public Business() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Business(Long id, String category, String name, String phoneNumber, String address) {
		super();
		this.id = id;
		this.category = category;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.address = address;
	}




	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}



	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}



	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}


	@Override
	public String toString() {
		return "Business [id=" + id + ", category=" + category + ", name=" + name + ", phoneNumber=" + phoneNumber
				+ ", address=" + address + "]";
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	
	
	

}
