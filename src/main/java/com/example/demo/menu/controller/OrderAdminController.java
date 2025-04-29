package com.example.demo.menu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.menu.service.MailService;
import com.example.demo.menu.service.OrderQueryService;
import com.example.demo.usersmenu.model.OrderHeader;
import com.example.demo.usersmenu.model.OrderItem;
import com.example.demo.usersmenu.service.OrderItemService;

@Controller
@RequestMapping("/admin/orders")
public class OrderAdminController {

    @Autowired
    private OrderQueryService orderQueryService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private MailService mailService;

    @GetMapping("/search")
    public String showSearchPage() {
        return "foodmenuadmin/order_search";
    }

    @PostMapping("/result")
    public String searchByPhone(@RequestParam(name = "phone") String phone, Model model) {
        List<OrderHeader> orders = orderQueryService.findOrdersByPhone(phone);
        model.addAttribute("orders", orders);
        model.addAttribute("phone", phone);
        return "foodmenuadmin/order_result";
    }

    @GetMapping("/detail/{orderId}")
    public String orderDetail(@PathVariable("orderId") Integer orderId, Model model) {
        List<OrderItem> items = orderItemService.findByOrderId(orderId);
        model.addAttribute("items", items);
        model.addAttribute("orderId", orderId);
        return "foodmenuadmin/order_detail";
    }
    
    

    @GetMapping("/test/mail")
    @ResponseBody
    public String testMailSend() {
        String toEmail = "timmychen520@gmail.com"; // 替換成你自己的信箱測試
        String userName = "測試使用者";
        String content = "這是一封測試信件，用來驗證 Spring Boot 的寄信功能。";

        mailService.sendOrderConfirmation(toEmail, userName, content);
        return "✅ 寄信完成，請查看你的信箱！";
    }
}
