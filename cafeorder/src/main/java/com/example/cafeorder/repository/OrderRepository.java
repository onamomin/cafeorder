package com.example.cafeorder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cafeorder.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	// 特定の顧客の注文を検索する 
	List<Order> findByCustomerId(Long customerId);

	// 特定のステータスの注文を検索する 
	List<Order> findByStatus(String status);
}
