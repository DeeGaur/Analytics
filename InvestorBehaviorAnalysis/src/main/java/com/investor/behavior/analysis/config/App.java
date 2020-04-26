package com.investor.behavior.analysis.config;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.investor.behavior.analysis.service.InvestorDataService;

public class App {

	public static void main(String[] args){
		AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
		InvestorDataService service = ctx.getBean(InvestorDataService.class);
		service.printSmallestAndLargestTradeForAll();
		service.printActiveInvestors();
		service.getMostBoughtTicker();
		service.getMostSoldTicker();
		
		service.getBuyAndHoldInvestors();
		
		ctx.close();
	}
}
