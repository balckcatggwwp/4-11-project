package com.example.demo.usersmenu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/ecpay")
public class EcpaySuccessController {

    @GetMapping("/success")
    public String handleClientBack(HttpSession session) {
        System.out.println("🧹 ClientBackURL：清除 localhost 購物車 cart");
        session.removeAttribute("cart");  
        return "redirect:/usersmenu/page/1";  
    }
}