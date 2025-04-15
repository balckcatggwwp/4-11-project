package com.example.demo.tick.usercontroller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.tick.bean.BookTypeBean;
import com.example.demo.tick.bean.BookticketBean;
import com.example.demo.tick.bean.OrderBean;
import com.example.demo.tick.repo.BooktickRepository;
import com.example.demo.tick.repo.BooktickvuRepository;
import com.example.demo.tick.service.BookvuService;
import com.example.demo.tick.service.TickorderService;
import com.example.demo.tick.service.TypeService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class OrderController {

	@Autowired
	private TypeService typeService;
	@Autowired
	private TickorderService orderService;
	@Autowired
	private BookvuService bookvuService;

	@GetMapping("/orderset")
	public String ordercar(HttpServletRequest request) {
		HttpSession session = request.getSession();
		// 假設你在登入時將會員 ID 存放在 session 中，key 為 "memberId"
		Long memberId = (Long) session.getAttribute("memberId");
		System.out.println(memberId);
		if (memberId == null) {
			return "member/login";
		}
		return "order/tickcar";
//		return "redirect:/";
	}

	@GetMapping("/ordercheck")
	public String ordercheck(@RequestParam String selectedSeatsJson, HttpServletRequest request,
			@RequestParam Integer showtimeid, @RequestParam Integer hallid, @RequestParam Integer tickettypeid,
			@RequestParam Integer movieid, Model model) {
		HttpSession session = request.getSession();
		// 假設你在登入時將會員 ID 存放在 session 中，key 為 "memberId"
		Long memberId = (Long) session.getAttribute("memberId");
		ObjectMapper objectMapper = new ObjectMapper();
		List<String> selectedSeats = null;
		OrderBean orderBean = new OrderBean();

		orderBean.setUserid(memberId);
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		DateTimeFormatter formattera = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        System.out.println("現在時間是 (預設格式): " + );
		Long order = Long.parseLong(formatter.format(now));
		orderBean.setOrderid(order);
		orderBean.setOrderdate(formattera.format(now));
		BookTypeBean aBean = typeService.findmony(tickettypeid);
		int typemo = aBean.getMoneytype();

		try {
			selectedSeats = objectMapper.readValue(selectedSeatsJson, new TypeReference<List<String>>() {
			});
			// 現在 selectedSeats 就是包含選取座位 ID 的 List
			System.out.println(selectedSeats);
			int sum = 0;
			

			
			sum = typemo * selectedSeats.size();
			orderBean.setSumpay(sum);
			
	        orderService.inser(orderBean);
			for (String stringgs : selectedSeats) {
				
				BookticketBean itemBean = new BookticketBean();
				itemBean.setMemberId(memberId);
				itemBean.setShowtimeid(showtimeid);
				itemBean.setHallid(hallid);
				itemBean.setTickettypeid(tickettypeid);
				itemBean.setMovieid(movieid);
				itemBean.setOnemoney(typemo);
				itemBean.setPayout("N");
				itemBean.setOrderid(order);
				itemBean.setSeatid(stringgs);
//				System.out.println(cont + ":" + stringgs);
				bookvuService.inser(itemBean);
			}
			// 在這裡處理你的訂單邏輯
		} catch (IOException e) {
			System.err.println("解析 JSON 失敗: " + e.getMessage());
			// 處理 JSON 解析錯誤
			// 可以返回錯誤頁面或者拋出異常
		}
//	    model.addAttribute("seat", selectedSeats);
//	    model.addAttribute("order", itemBean);
//		return "order/testecpay";
		return "redirect:/";
	}

	// 綠界回傳結果
	@PostMapping("/pay")
	public String paychech(@RequestParam Integer RtnCode, @RequestParam String MerchantTradeNo,
			@RequestParam String CustomField1 // userid
			, @RequestParam String CustomField2 // showid
			, @RequestParam String CustomField3 // hallid
			, @RequestParam String CustomField4 // typeid

	) {
		// TODO: process POST request
		if (RtnCode == 1) {
			System.out.println("交易成功");
			System.out.println(MerchantTradeNo);
			System.out.println(CustomField1);
			System.out.println();
			String useropenString = CustomField1;
			String[] aftStrings = useropenString.split("\\s+");
			System.out.println("userid" + aftStrings[0]);
			System.out.println("timeid" + aftStrings[1]);

			System.out.println("le" + aftStrings.length);

			// 解析 CustomField2 ([A10, A9])
			String seatString = CustomField2.substring(1, CustomField2.length() - 1); // 移除首尾的 []
			String[] seatCodes = seatString.split(",\\s*"); // 使用逗號和零或多個空白字元分割
			System.out.println("seat[]" + seatCodes[0]);
			System.out.println("seat[]" + seatCodes[1]);
			System.out.println(CustomField3);
			System.out.println(CustomField4);

		} else {
			System.out.println("你好爛");
		}
		return "redirect:/ticktable";
	}

}
