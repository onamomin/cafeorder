package com.example.cafeorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.cafeorder.entity.Product;
import com.example.cafeorder.repository.ProductRepository;

@Component
public class DataInitializer implements CommandLineRunner {
	@Autowired
	private ProductRepository proRepo;

	@Override
	public void run(String... args) throws Exception {
		if (proRepo.count() == 0) {
			proRepo.save(new Product(null, "ブレンドコーヒー", 350.0, "香り高いブレンドコーヒー", "ドリンク"));
			proRepo.save(new Product(null, "カフェラテ", 400.0, "ミルクとエスプレッソのハーモニー", "ドリンク"));
			proRepo.save(new Product(null, "紅茶", 300.0, "香り豊かな紅茶", "ドリンク"));
			proRepo.save(new Product(null, "チーズケーキ", 450.0, "濃厚なチーズケーキ", "フード"));
			proRepo.save(new Product(null, "チョコレートマフィン", 380.0, "しっとりとしたチョコレートマフィン", "フード"));
			System.out.println("初期商品データを登録しました。");
		}
	}

}
