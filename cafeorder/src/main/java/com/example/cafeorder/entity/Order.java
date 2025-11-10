package com.example.cafeorder.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@jakarta.persistence.Table(name = "CustomerOrder")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDateTime orderDate;
	private double totalAmount;
	private String status;

	// 注文は一人の顧客に属する (ManyToOne リレーション) 
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;

	// 一つの注文は複数の注文詳細アイテムを持つ (OneToMany リレーション) 
	// cascade = CascadeType.ALL: Order が保存/削除されると OrderItem も連動 
	// orphanRemoval = true: 親から参照がなくなった子エンティティを削除
	@OneToMany(mappedBy = "order", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
	private List<OrderItem> orderItems;

}
