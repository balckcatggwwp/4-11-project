package com.example.demo.tick.bean;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Table(name = "Bookorder")
@Setter
@Getter
@NoArgsConstructor
public class OrderBean {

	@Id
	private Long orderid; 
	private Long userid;
	private int sumpay;
	private String orderdate;
}
