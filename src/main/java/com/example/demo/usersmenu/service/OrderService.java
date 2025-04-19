package com.example.demo.usersmenu.service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.usersmenu.model.OrderHeader;
import com.example.demo.usersmenu.model.OrderHeaderRepository;
import com.example.demo.usersmenu.model.OrderItem;
import com.example.demo.usersmenu.model.OrderItemRepository;
import com.example.demo.usersmenu.model.SessionCartItem;
import com.example.demo.usersmenu.model.UsersMenu;
import com.example.demo.usersmenu.model.UsersMenuRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {
    @Autowired
    private OrderHeaderRepository orderHeaderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UsersMenuRepository usersMenuRepository;

    @Transactional
    public void processOrder(Integer userId, Map<Integer, SessionCartItem> cart, String phone) {
        try {
            System.out.println("🧾 [開始建立訂單] userId=" + userId + ", 商品數量=" + cart.size());

            OrderHeader header = new OrderHeader();
            header.setUserId(userId);
            
            header.setOrderTime(LocalDateTime.now());

            List<Integer> menuIds = cart.values().stream()
                    .map(SessionCartItem::getMenuId)
                    .distinct()
                    .toList();

            List<UsersMenu> menus = usersMenuRepository.findAllById(menuIds);
            Map<Integer, UsersMenu> menuMap = menus.stream()
                    .collect(Collectors.toMap(UsersMenu::getMenuId, m -> m));

            List<OrderItem> orderItems = new ArrayList<>();
            int total = 0;

            for (SessionCartItem sessionItem : cart.values()) {
                UsersMenu menu = menuMap.get(sessionItem.getMenuId());
                if (menu == null) {
                    System.out.println("⚠️ 找不到商品 menuId=" + sessionItem.getMenuId() + "，略過");
                    continue;
                }

                OrderItem item = new OrderItem();
                item.setOrder(header);
                item.setMenuId(menu.getMenuId());
                item.setQuantity(sessionItem.getQuantity());
                item.setUnitPrice(menu.getUnitPrice().intValue());

                int subtotal = item.getUnitPrice() * item.getQuantity();
                item.setSubtotal(subtotal);

                orderItems.add(item);
                total += subtotal;
            }
            header.setPhone(phone);
            header.setTotalAmount(total);
            header.setItems(orderItems);

            orderHeaderRepository.save(header);

            System.out.println("✅ [訂單儲存成功] 金額總計=" + total);
        } catch (Exception e) {
            System.out.println("❌ [訂單儲存失敗]：" + e.getMessage());
            e.printStackTrace();
        }
    }
}
