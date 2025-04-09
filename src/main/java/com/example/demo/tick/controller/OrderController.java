package com.example.demo.tick.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.tick.bean.BookticketBean;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


@Controller
public class OrderController {

	@GetMapping("/orderset")
	public String getMethodName() {
		return "order/tickcar";
	}
	
	@GetMapping("/ordercheck")
	public String getMethodName(@RequestParam String selectedSeatsJson
			,@RequestParam Integer userid
			,@RequestParam Integer showtimeid
			,@RequestParam Integer hallid
			,@RequestParam Integer tickettypeid
			,@RequestParam Integer movieid
			,Model model
			) {
		
		ObjectMapper objectMapper = new ObjectMapper();
	    List<String> selectedSeats = null;
	    BookticketBean itemBean = new BookticketBean();
	    itemBean.setUserid(userid);
	    itemBean.setShowtimeid(showtimeid);
	    itemBean.setHallid(hallid);
	    itemBean.setTickettypeid(tickettypeid);
	    itemBean.setMovieid(movieid);
	    try {
	        selectedSeats = objectMapper.readValue(selectedSeatsJson, new TypeReference<List<String>>() {});
	        // 現在 selectedSeats 就是包含選取座位 ID 的 List
	        for(String a:selectedSeats) {
	        	System.out.println(a);
	        	itemBean.setSeatid(a);
	        }
	        
	        // 在這裡處理你的訂單邏輯
	    } catch (IOException e) {
	        System.err.println("解析 JSON 失敗: " + e.getMessage());
	        // 處理 JSON 解析錯誤
	        // 可以返回錯誤頁面或者拋出異常
	    }
	    model.addAttribute("order", itemBean);
		return "order/testecpay";
	}
	
	
}
