package com.investor.behavior.analysis.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class InvestorData {

	private Integer investorId;
	private String ticker;
	private LocalDate date;
	private TradeType type;
	private Double amount;
	
	public Integer getInvestorId() {
		return investorId;
	}
	public void setInvestorId(Integer investorId) {
		this.investorId = investorId;
	}
	
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public TradeType getType() {
		return type;
	}
	public void setType(TradeType type) {
		this.type = type;
	}
	
	public String getTicker() {
		return ticker;
	}
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	@Override
	public String toString(){
		return "["+this.investorId+" "+this.ticker+" "+this.date.format(DateTimeFormatter.ofPattern("M/d/yyyy"))+"]";
	}
	
	
}
