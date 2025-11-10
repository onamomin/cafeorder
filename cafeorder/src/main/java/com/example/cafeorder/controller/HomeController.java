package com.example.cafeorder.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.cafeorder.entity.Customer;
import com.example.cafeorder.entity.Order;
import com.example.cafeorder.entity.OrderItem;
import com.example.cafeorder.entity.Product;
import com.example.cafeorder.repository.CustomerRepository;
import com.example.cafeorder.repository.OrderRepository;
import com.example.cafeorder.repository.ProductRepository;

@Controller
public class HomeController {
	@Autowired
	private ProductRepository proRepo;
	@Autowired
	private CustomerRepository custRepo;
	@Autowired
	private OrderRepository orRepo;

	// セッションスコープでカート情報を保持する (簡易的な実装) 
	private Map<Long, Integer> cart = new HashMap<>(); // Product ID -> Quantity

	@GetMapping("/")
	public String index(Model mod) {
		mod.addAttribute("drinks", proRepo.findByCategory("ドリンク"));
		mod.addAttribute("foods", proRepo.findByCategory("フード"));
		mod.addAttribute("cartSize", cart.size());
		return "index";
	}

	//カートに商品追加
	@PostMapping("/add-to-cart")
	public String addToCart(@RequestParam("productId") Long proId, @RequestParam(name ="quantity",defaultValue = "1") int quanty,
			RedirectAttributes redi) {
		cart.put(proId, cart.getOrDefault(proId, 0) + quanty);
		redi.addFlashAttribute("message", "商品がカートに追加されました！");
		return "redirect:/";
	}

	@GetMapping("/cart")
	public String viewCart(Model mod) {
		List<OrderItem> carItms = new ArrayList<>();
		double total = 0.0;
		for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
			Optional<Product> proOp = proRepo.findById(entry.getKey());
			if (proOp.isPresent()) {
				Product pro = proOp.get();
				OrderItem item = new OrderItem();
				item.setProduct(pro);
				item.setQuantity(entry.getValue());
				item.setPriceAtOrder(pro.getPrice());
				carItms.add(item);
				total += pro.getPrice() * entry.getValue();
			}
		}
		mod.addAttribute("cartItems", carItms);
		mod.addAttribute("totalAmount", total);
		mod.addAttribute("customer", new Customer());
		return "cart";
	}

	@PostMapping("/cart/remove")
	//ここのメソッド名変なのにするよ。覚えといて。自分の理解力向上のため。
	public String remvFroCar(@RequestParam("productId") Long proId) {
		cart.remove(proId);
		return "redirect:/cart";
	}

	@PostMapping("/place-order")
	public String placeOrder(@ModelAttribute Customer cust, RedirectAttributes redi) {
		if (cart.isEmpty()) {
			redi.addFlashAttribute("errorMessage", "カートが空です。");
			return "redirect:/cart";
		}
		Customer existingCust = custRepo.findByEmail(cust.getEmail());
		if (existingCust != null) {
			cust = existingCust;
		} else {
			custRepo.save(cust);
		}

		Order or = new Order();
		or.setCustomer(cust);
		or.setOrderDate(LocalDateTime.now());
		or.setStatus("受付済"); // 初期ステータス 

		List<OrderItem> orItms = new ArrayList<>();
		double totalAmount = 0.0;

		for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
			Product pro = proRepo.findById(entry.getKey()).orElseThrow(); // 商品が存在しない場合はエラー
			OrderItem orItm = new OrderItem();
			orItm.setProduct(pro);
			orItm.setQuantity(entry.getValue());
			orItm.setPriceAtOrder(pro.getPrice()); // 注文時の価格を記録
			orItm.setOrder(or); // 注文と注文詳細を関連付け 
			orItms.add(orItm);
			totalAmount += pro.getPrice() * entry.getValue();
		}

		or.setOrderItems(orItms);
		or.setTotalAmount(totalAmount);

		orRepo.save(or);// 注文と注文詳細をまとめて保存 (cascade 設定により)
		cart.clear(); // カートをクリア 

		redi.addFlashAttribute("message", "ご注文ありがとうございます！注文 ID: " + or.getId());
		return "redirect:/order-success";
	}

	// 注文成功ページ
	@GetMapping("/order-success")
	public String orderSuccess() {
		return "order_success";
	}
}
