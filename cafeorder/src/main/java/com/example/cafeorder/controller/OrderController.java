package com.example.cafeorder.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.cafeorder.entity.Order;
import com.example.cafeorder.repository.OrderRepository;

@Controller
@RequestMapping("/admin/orders")
public class OrderController {
	@Autowired
	private OrderRepository orRepo;

	@GetMapping
	public String listOrder(Model mod) {
		List<Order> orders = orRepo.findAll();
		mod.addAttribute("orders", orders);
		return "admin/order_list";
	}

	@GetMapping("/{id}")
	public String showOrderDetail(@PathVariable Long id, Model mod, RedirectAttributes redi) {
		Optional<Order> order = orRepo.findById(id);
		if (order.isPresent()) {
			mod.addAttribute("order", order.get());
			return "admin/order_detail"; // admin/order_detail.html を表示
		} else {
			redi.addFlashAttribute("errorMessage", "注文が見つかりませんでした。");
			return "redirect:/admin/orders";
		}

	}

	// 注文ステータス更新処理
	@PostMapping("/{id}/updateStatus")
	public String updateOrderStatus(@PathVariable Long id, @RequestParam String status, RedirectAttributes redi) {
		Optional<Order> orderOptional = orRepo.findById(id);
		if (orderOptional.isPresent()) {
			Order order = orderOptional.get();
			order.setStatus(status); // ステータスを更新 
			orRepo.save(order);
			redi.addFlashAttribute("message", "注文ステータスが更新されました！");
		} else {
			redi.addFlashAttribute("errorMessage", "注文が見つかりませんでした。");
		}
		return "redirect:/admin/orders/" + id;
	}

	// 注文削除処理
	@GetMapping("/{id}/delete")
	public String deleteOrder(@PathVariable Long id, RedirectAttributes redi) {
		if (orRepo.existsById(id)) {
			orRepo.deleteById(id);
			redi.addFlashAttribute("message", "注文が正常に削除されました！");
		} else {
			redi.addFlashAttribute("errorMessage", "注文が見つかりませんでした。");
		}
		return "redirect:/admin/orders";
	}

}
