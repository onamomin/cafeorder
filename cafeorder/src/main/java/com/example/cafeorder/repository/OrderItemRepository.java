package com.example.cafeorder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cafeorder.entity.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
	// 特定の注文に属する注文詳細アイテムを検索する 
	List<OrderItem> findByOrderId(Long orderId);

}
