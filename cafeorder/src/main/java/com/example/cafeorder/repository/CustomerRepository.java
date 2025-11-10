package com.example.cafeorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cafeorder.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	// メールアドレスで顧客を検索する 
	Customer findByEmail(String email);

}
