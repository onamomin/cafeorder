package com.example.cafeorder.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private int quantity;
	private double priceAtOrder; // 注文時の商品の価格

	// 注文詳細アイテムは一つの注文に属する (ManyToOne リレーション) 
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

	// 注文詳細アイテムは一つの商品に属する (ManyToOne リレーション) 
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

}
