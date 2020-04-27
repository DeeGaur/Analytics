package com.investor.behavior.analysis.service;

import java.util.List;
import java.util.Map;

import com.investor.behavior.analysis.model.InvestorData;

/**
 * InvestorDataService provides the services for Investor Behavior
 * and analysis of the buying and selling habits of investors
 * 
 * @author Deepti
 *
 */
public interface InvestorDataService {

	/**
	 * @return mapping of investor with their largest trade
	 */
	public Map<Integer, InvestorData> getLargestTradeForAll();
	
	/**
	 * @return mapping of investor with their smallest trade
	 */
	public Map<Integer, InvestorData> getSmallestTradeForAll();
	
	/**
	 * prints the mapping investor with their 
	 * largest and smallest trade
	 */
	public void printSmallestAndLargestTradeForAll();
	
	/**
	 * prints the active investor id
	 * 
	 * An investor is defined as active if they have made more than two trades
	 * on two or more consecutive days.
	 */
	public void printActiveInvestors();
	
	/**
	 * An investor is defined as active if they have made more than two trades
	 * on two or more consecutive days.
	 * 
	 * @return list of active investors id
	 * 
	 */
	public List<Integer> getActiveInvestors();
	
	/**
	 * An investor is defined as buy and hold if they have more buy
	 *         orders than sell orders.
	 * 
	 * @return list of BuyAndHoldInvestors
	 * 
	 */
	public List<Integer> getBuyAndHoldInvestors();
	
	/**
	 * @return Most Bought Ticker
	 */
	public String getMostBoughtTicker();
	
	/**
	 * @return Most Sold Ticker
	 */
	public String getMostSoldTicker();

}
