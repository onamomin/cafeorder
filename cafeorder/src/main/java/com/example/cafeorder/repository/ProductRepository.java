package com.example.cafeorder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cafeorder.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	// カテゴリで商品を検索するカスタムメソッド 
	List<Product> findByCategory(String category);
}
