package com.investor.behavior.analysis.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;

import com.investor.behavior.analysis.compare.AmountComparator;
import com.investor.behavior.analysis.exception.DataNotFoundException;
import com.investor.behavior.analysis.model.InvestorData;
import com.investor.behavior.analysis.model.TradeType;
import com.investor.behavior.analysis.repo.InvestorDataSource;

/**
 * @author Deepti
 *
 */
public class InvestorDataServiceImpl implements InvestorDataService {

	// @Autowired
	private InvestorDataSource investorDataSource;

	public InvestorDataServiceImpl(InvestorDataSource investorDataSource) {
		this.setInvestorDataSource(investorDataSource);
	}

	public InvestorDataSource getInvestorDataSource() {
		return investorDataSource;
	}

	public void setInvestorDataSource(InvestorDataSource investorDataSource) {
		this.investorDataSource = investorDataSource;
	}

	/**
	 * @return An investor is defined as buy and hold if they have more buy
	 *         orders than sell orders.
	 */
	@Override
	public List<Integer> getBuyAndHoldInvestors() {
		
		Map<Integer, Map<String, List<InvestorData>>> investorDataMap = investorDataSource.investorDataMapRepo();
		List<Integer> investorList = new ArrayList<>();
		
		for(Entry<Integer, Map<String, List<InvestorData>>> e : investorDataMap.entrySet()){
			int buyOrders = 0;
			int sellOrders = 0;
			Map<String, List<InvestorData>> tickerMap = e.getValue();
			for(Entry<String, List<InvestorData>> et : tickerMap.entrySet()){
				List<InvestorData> list = et.getValue();
				for(InvestorData d : list){
					if(d.getType().equals(TradeType.BUY)){
						buyOrders++;
					}
					if(d.getType().equals(TradeType.SELL)){
						sellOrders++;
					}
				}
			}
			if(buyOrders>sellOrders){
				investorList.add(e.getKey());
			}
		}
		System.out.print("Buy And Hold Investors are ");
		for(Integer i : investorList){
			System.out.print(i+",");
		}
		System.out.println();
		return investorList;
	}

	@Override
	public String getMostBoughtTicker() {
		Map<Integer, Map<String, List<InvestorData>>> investorDataMap = investorDataSource.investorDataMapRepo();
		Map<String, Integer> tickerCountMap = new HashMap<>();
		for (Entry<Integer, Map<String, List<InvestorData>>> e : investorDataMap.entrySet()) {
			Map<String, List<InvestorData>> tickerMap = e.getValue();
			for(Entry<String, List<InvestorData>> et : tickerMap.entrySet()){
				int boughtCount = 0;
				for (InvestorData d : et.getValue()) {
					if (d.getType().equals(TradeType.BUY)) {
						boughtCount++;
					}
				}
				if(tickerCountMap.containsKey(et.getKey())){
					tickerCountMap.put(et.getKey(), tickerCountMap.get(et.getKey())+boughtCount);
				}else{
					tickerCountMap.put(et.getKey(), boughtCount);
				}
				
			}	
		}
		Stack<String> stack = new Stack<>();
		int maxCount = 0;
		for (Entry<String, Integer> e : tickerCountMap.entrySet()) {
			if (maxCount <= e.getValue()) {
				maxCount = e.getValue();
				stack.push(e.getKey());
			}
		}

		// assuming most bought ticker count is never same for two(or more)
		// tickers
		System.out.println("Most Bought Ticker=" + stack.peek());

		return stack.pop();
	}

	@Override
	public String getMostSoldTicker() {
		Map<Integer, Map<String, List<InvestorData>>> investorDataMap = investorDataSource.investorDataMapRepo();
		Map<String, Integer> tickerCountMap = new HashMap<>();
		for (Entry<Integer, Map<String, List<InvestorData>>> e : investorDataMap.entrySet()) {
			Map<String, List<InvestorData>> tickerMap = e.getValue();
			for(Entry<String, List<InvestorData>> et : tickerMap.entrySet()){
				int sellCount = 0;
				for (InvestorData d : et.getValue()) {
					if (d.getType().equals(TradeType.SELL)) {
						sellCount++;
					}
				}
				if(tickerCountMap.containsKey(et.getKey())){
					tickerCountMap.put(et.getKey(), tickerCountMap.get(et.getKey())+sellCount);
				}else{
					tickerCountMap.put(et.getKey(), sellCount);
				}
				
			}	
		}
		Stack<String> stack = new Stack<>();
		int maxCount = 0;
		for (Entry<String, Integer> e : tickerCountMap.entrySet()) {
			if (maxCount <= e.getValue()) {
				maxCount = e.getValue();
				stack.push(e.getKey());
			}
		}

		// assuming most bought ticker count is never same for two(or more)
		// tickers
		System.out.println("Most Sold Ticker=" + stack.peek());

		return stack.pop();
	}

	@Override
	public void printSmallestAndLargestTradeForAll() {
		System.out.println("Hello trying with new repo......");
		Map<Integer, InvestorData> largestTradeMap = getLargestTradeForAll();
		Map<Integer, InvestorData> smallestTradeMap = getSmallestTradeForAll();

		Set<Integer> set = new HashSet<>();
		set.addAll(largestTradeMap.keySet());
		set.addAll(smallestTradeMap.keySet());

		for (Integer s : set) {
			System.out.println("Investor " + s + "'s largest trade was " + largestTradeMap.get(s).getType() + " "
					+ largestTradeMap.get(s).getAmount() + " of " + largestTradeMap.get(s).getTicker()
					+ " and smallest trade was " + smallestTradeMap.get(s).getType() + " "
					+ smallestTradeMap.get(s).getAmount() + " of " + smallestTradeMap.get(s).getTicker());
		}

	}

	@Override
	public Map<Integer, InvestorData> getSmallestTradeForAll() {
		Map<Integer, InvestorData> map = new HashMap<>();
		Map<Integer, Map<String, List<InvestorData>>> dataSource = investorDataSource.investorDataMapRepo();
		for (Entry<Integer, Map<String, List<InvestorData>>> e : dataSource.entrySet()) {
			Map<String, List<InvestorData>> tickerMap = e.getValue();

			for (List<InvestorData> list : tickerMap.values()) {
				Collections.sort(list, new AmountComparator());
				InvestorData investorData = list.get(0);
				if (map.containsKey(e.getKey())) {
					// then compare
					if (investorData.getAmount().compareTo(map.get(e.getKey()).getAmount()) < 0) {
						map.put(e.getKey(), investorData);
					}
				} else {
					map.put(e.getKey(), investorData);
				}
			}

		}
		return map;

	}

	@Override
	public Map<Integer, InvestorData> getLargestTradeForAll() {
		Map<Integer, InvestorData> map = new HashMap<>();
		Map<Integer, Map<String, List<InvestorData>>> dataSource = investorDataSource.investorDataMapRepo();
		for (Entry<Integer, Map<String, List<InvestorData>>> e : dataSource.entrySet()) {
			Map<String, List<InvestorData>> tickerMap = e.getValue();
			for (List<InvestorData> list : tickerMap.values()) {
				Collections.sort(list, new AmountComparator());
				InvestorData investorData = list.get(list.size() - 1);
				if (map.containsKey(e.getKey())) {
					// then compare
					if (investorData.getAmount().compareTo(map.get(e.getKey()).getAmount()) > 0) {
						map.put(e.getKey(), investorData);
					}
				} else {
					map.put(e.getKey(), investorData);
				}
			}

		}
		return map;
	}

	/**
	 * An investor is defined as active if they have made more than two trades
	 * on two or more consecutive days.
	 * 
	 * @return
	 */
	@Override
	public List<Integer> getActiveInvestors() {
		List<Integer> activeInvestors = new ArrayList<>();
		Map<Integer, Map<String, List<InvestorData>>> dataSource = investorDataSource.investorDataMapRepo();
		try {
			if (dataSource == null || dataSource.isEmpty()) {
				throw new DataNotFoundException();
			}
			for (Entry<Integer, Map<String, List<InvestorData>>> e : dataSource.entrySet()) {
				List<InvestorData> investorDataByIdList = new ArrayList<>();
				Map<String, List<InvestorData>> tickerMap = e.getValue();
				for (Entry<String, List<InvestorData>> et : tickerMap.entrySet()) {
					investorDataByIdList.addAll(et.getValue());
				}

				Map<LocalDate, Integer> dateTradeMap = new TreeMap<>();
				for (InvestorData d : investorDataByIdList) {
					if (dateTradeMap.containsKey(d.getDate())) {
						dateTradeMap.put(d.getDate(), dateTradeMap.get(d.getDate()) + 1);
					} else {
						dateTradeMap.put(d.getDate(), 1);
					}
				}

				List<LocalDate> dateList = new ArrayList<>();
				for (Entry<LocalDate, Integer> dateTrade : dateTradeMap.entrySet()) {
					if (dateTrade.getValue() >= 2) {
						dateList.add(dateTrade.getKey());
					}
				}

				for (int i = 0; i <= dateList.size() - 2; i++) {
					if (dateList.get(i).plusDays(1).compareTo(dateList.get(i + 1)) == 0) {
						activeInvestors.add(e.getKey());
					}
				}
			}

		} catch (DataNotFoundException ex) {
			ex.printStackTrace();
		}
		return activeInvestors;
	}

	@Override
	public void printActiveInvestors() {
		List<Integer> active =  getActiveInvestors();
		System.out.print("The active investors are ");
		for(Integer i : active){
			System.out.print(i+",");
		}
		System.out.println();
	}
}
