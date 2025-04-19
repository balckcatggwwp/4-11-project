package com.example.demo.usersmenu.util;

import java.util.Map;

import com.example.demo.usersmenu.model.SessionCartItem;

public class TempOrderData {
    public Integer userId;
    public Map<Integer, SessionCartItem> cart;
    public String phone;

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public Map<Integer, SessionCartItem> getCart() { return cart; }
    public void setCart(Map<Integer, SessionCartItem> cart) { this.cart = cart; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
