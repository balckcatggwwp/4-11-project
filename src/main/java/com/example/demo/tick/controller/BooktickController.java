package com.example.demo.tick.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.demo.tick.bean.BookticketBean;
import com.example.demo.tick.bean.BookticketvuBean;
import com.example.demo.tick.bean.OrderBean;
import com.example.demo.tick.service.BookvuService;
import com.example.demo.tick.service.OrderService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BooktickController {
	@Autowired
	private BookvuService bookvuService;
	@Autowired 
	private OrderService orderService;
	
	@GetMapping("/ticktable")
	public String tickall(Model model) {
		List<BookticketvuBean> vu = bookvuService.tickfindAll();
		model.addAttribute("tickall", vu);
		return "tick/table";
	}

	@GetMapping("/booktick/update")
	public String tickupdatebyid(@RequestParam Integer tickid, Model model) {
		Optional<BookticketvuBean> op = bookvuService.findtickbyid(tickid);

		model.addAttribute("tickinfo", op.get());
		return "tick/updatetick";
	}

	// 處理修改訂票的 POST 請求 (對應表單的 action="Updatea")
	@PostMapping("/booktick/Updatea")
	public String updateTick(@ModelAttribute BookticketBean updatedTick) {
		BookticketvuBean vuBean = bookvuService.findmoneybyid(updatedTick.getTickid());
		vuBean.getOnemoney();
		OrderBean oBean = new OrderBean();
		oBean.setUserid(updatedTick.getMemberId());
		oBean.setOrderid(updatedTick.getOrderid());
		 
		bookvuService.updatetick(updatedTick);
		return "redirect:/ticktable"; // 更新成功後重定向到訂票列表頁面
	}
	@PostMapping("/booktick/inser")
	public String inserTick(@ModelAttribute BookticketBean updatedTick) {
		OrderBean oBean = new OrderBean();
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formattera = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		Long order = Long.parseLong(formatter.format(now));
		oBean.setUserid(updatedTick.getMemberId());
		oBean.setOrderdate(formattera.format(now));
		oBean.setOrderid(order);
		oBean.setSumpay(updatedTick.getOnemoney());
		updatedTick.setOrderid(order);
		orderService.inser(oBean);
		bookvuService.updatetick(updatedTick);
		return "redirect:/ticktable"; // 更新成功後重定向到訂票列表頁面
	}

	@GetMapping("/booktick/inser")
	public String inser() {
		return "tick/inser";
	}

	@GetMapping("/booktick/seat")
	@ResponseBody
	public List<BookticketBean> getMethodName(@RequestParam Integer hallid, @RequestParam Integer showtimeid) {
		return bookvuService.findseatbyhallid(hallid, showtimeid);
	}

	@GetMapping("/booktick/del")
	public String dele(@RequestParam Integer id) {
		bookvuService.deltick(id);
		return "redirect:/ticktable";
	}

	///////////// other
	@GetMapping("/booktick/other")
	public String findother(@RequestParam Integer select,
			@RequestParam(required = false) Integer tickid, @RequestParam(required = false) Long orderid,
			@RequestParam(required = false) Integer memberId, @RequestParam(required = false) String startdate,
			@RequestParam(required = false) Integer hallid, @RequestParam(required = false) String findname,
			@RequestParam(required = false) String payout,Model model

	) {
		List<BookticketvuBean> vu = null;
		if (select == 1) {
			vu=bookvuService.findother(select, tickid);
			
		} else if (select == 2) {
			vu= bookvuService.findothers(select, null,orderid);
		} else if (select == 3) {
			vu= bookvuService.findother(select, memberId);
		} else if (select == 4) {
			vu= bookvuService.findothers(select, startdate,null);
		} else if (select == 5) { 
			vu= bookvuService.findother(select, hallid);
		}else if (select == 6) {
			System.out.println(findname);
			vu= bookvuService.findothers(select, findname,null);
		}else if (select == 7) {
			vu= bookvuService.findothers(select, payout,null);
		}
		
		model.addAttribute("tickall", vu);
		return "tick/table";
		
	}

}
