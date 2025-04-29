package com.example.demo.usersmenu.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.usersmenu.model.CartDisplayItem;
import com.example.demo.usersmenu.model.CartViewResponse;
import com.example.demo.usersmenu.model.SessionCartItem;
import com.example.demo.usersmenu.model.UsersMenu;
import com.example.demo.usersmenu.model.UsersMenuRepository;

import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/cart")
public class CartController {
	@Autowired
	private UsersMenuRepository usersMenuRepository;

	@PostMapping("/add")

	public String addToCart(@RequestParam("menuId") Integer menuId,
			@RequestParam("quantity") Integer quantity,
			HttpSession session) {

		Integer userId = (Integer) session.getAttribute("userId");
		if (userId == null) {
			userId = 1;
			session.setAttribute("userId", userId);
		}

		Map<Integer, SessionCartItem> cart = (Map<Integer, SessionCartItem>) session.getAttribute("cart");

		if (cart == null) {
			cart = new HashMap<>();
		}
		// 加入或更新商品數量
		SessionCartItem item = cart.getOrDefault(menuId, new SessionCartItem(menuId, 0));
		item.setQuantity(item.getQuantity() + quantity);
		cart.put(menuId, item);

		session.setAttribute("cart", cart);

		return "已將商品加入購物車（menuId=" + menuId + "，數量=" + item.getQuantity() + "）";
	}

	@GetMapping("/view")
	public CartViewResponse viewCart(HttpSession session) {
		Map<Integer, SessionCartItem> cart = (Map<Integer, SessionCartItem>) session.getAttribute("cart");
		List<CartDisplayItem> displayList = new ArrayList<>();
		int total = 0;
		if (cart == null || cart.isEmpty()) {
			CartViewResponse emptyResponse = new CartViewResponse();
			emptyResponse.setItems(displayList);
			emptyResponse.setTotal(0);
			return emptyResponse;
		}
		for (Map.Entry<Integer, SessionCartItem> entry : cart.entrySet()) {
			Integer menuId = entry.getKey();
			SessionCartItem sessionItem = entry.getValue();

			// 從資料庫查出商品資訊
			Optional<UsersMenu> optionalMenu = usersMenuRepository.findById(menuId);
			if (optionalMenu.isPresent()) {
				UsersMenu menu = optionalMenu.get();

				CartDisplayItem displayItem = new CartDisplayItem();
				displayItem.setMenuId(menuId);
				displayItem.setMenuName(menu.getMenuName());
				displayItem.setQuantity(sessionItem.getQuantity());
				displayItem.setImageUrl(menu.getImageUrl());
				displayItem.setUnitPrice(menu.getUnitPrice().intValue());

				// 小計 = 價格 * 數量
				int subtotal = BigDecimal.valueOf(menu.getUnitPrice().intValue())
						.multiply(BigDecimal.valueOf(sessionItem.getQuantity()))
						.intValue();

				displayItem.setSubtotal(subtotal);

				total += subtotal;
				displayList.add(displayItem);
			}
		}

		CartViewResponse response = new CartViewResponse();
		response.setItems(displayList);
		response.setTotal(total);
		return response;
	}

	@GetMapping("/delete")
	public String deleteCartItem(@RequestParam("menuId") Integer menuId, HttpSession session) {
		Map<Integer, SessionCartItem> cart = (Map<Integer, SessionCartItem>) session.getAttribute("cart");

		if (cart == null || !cart.containsKey(menuId)) {
			return "購物車中沒有這個商品（menuId=" + menuId + "）";
		}

		cart.remove(menuId);
		session.setAttribute("cart", cart);
		return "已刪除商品 menuId=" + menuId + "，目前剩餘 " + cart.size() + " 項商品";
	}

	@GetMapping("/clear")
	public String clearCart(HttpSession session) {
		session.removeAttribute("cart");
		return "購物車已清空";
	}

	@GetMapping("/success")
	public String handleClientBack(HttpSession session) {
		session.removeAttribute("cart");
		System.out.println("🧹 回到商店時清除購物車");
		return "redirect:/usersmenu/1";
	}
}
