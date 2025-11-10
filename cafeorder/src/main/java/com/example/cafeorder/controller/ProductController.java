package com.example.cafeorder.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.cafeorder.entity.Product;
import com.example.cafeorder.repository.ProductRepository;

@Controller
@RequestMapping("/admin/products")
public class ProductController {
	@Autowired
	private ProductRepository productRepository;

	@GetMapping
	public String listProduct(Model model) {
		List<Product> pro = productRepository.findAll();
		model.addAttribute("products", pro);
		return "admin/product_list"; // admin/product_list.html を表示
	}

	@GetMapping("/new")
	public String showProductForm(Model model) {
		model.addAttribute("product", new Product());
		return "admin/product_form"; // admin/product_form.html を表示
	}

	@PostMapping("/save")
	public String saveProduct(@ModelAttribute Product pro, RedirectAttributes redi) {
		productRepository.save(pro);
		redi.addFlashAttribute("message", "商品が正常に保存されました！");
		return "redirect:/admin/products";

	}

	@GetMapping("/{id}/edit")
	public String showEditForm(@PathVariable Long id, Model mod, RedirectAttributes redi) {
		Optional<Product> pro = productRepository.findById(id);
		if (pro.isPresent()) {
			mod.addAttribute("product", pro.get());
			return "admin/product_form";
		} else {
			redi.addFlashAttribute("errorMessage", "商品が見つかりませんでした。");
			return "redirect:/admin/products";
		}

	}

	@GetMapping("/{id}/delete")
	public String deleteProduct(@PathVariable Long id, RedirectAttributes redi) {
		if (productRepository.existsById(id)) {
			productRepository.deleteById(id);
			redi.addFlashAttribute("message", "商品が正常に削除されました！");
		} else {
			redi.addFlashAttribute("errorMessage", "商品が見つかりませんでした。");
		}
		return "redirect:/admin/products";
	}

}
