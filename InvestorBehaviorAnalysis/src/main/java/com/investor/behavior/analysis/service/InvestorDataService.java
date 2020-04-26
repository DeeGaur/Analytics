package com.investor.behavior.analysis.service;

import java.util.List;
import java.util.Map;

import com.investor.behavior.analysis.model.InvestorData;

public interface InvestorDataService {

	public Map<Integer, InvestorData> getLargestTradeForAll();
	public Map<Integer, InvestorData> getSmallestTradeForAll();
	public void printSmallestAndLargestTradeForAll();
	public void printActiveInvestors();
	public List<Integer> getActiveInvestors();
	public List<Integer> getBuyAndHoldInvestors();
	public String getMostBoughtTicker();
	public String getMostSoldTicker();
	
	
}
