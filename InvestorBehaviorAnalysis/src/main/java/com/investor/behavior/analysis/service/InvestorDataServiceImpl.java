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
 
	@Override
	public List<Integer> getBuyAndHoldInvestors() {

		Map<Integer, Map<String, List<InvestorData>>> investorDataMap = investorDataSource.investorDataMapRepo();
		List<Integer> investorList = new ArrayList<>();
		
		for (Entry<Integer, Map<String, List<InvestorData>>> e : investorDataMap.entrySet()) {
			int buyOrders = 0;
			int sellOrders = 0;
			Map<String, List<InvestorData>> tickerMap = e.getValue();
			for (Entry<String, List<InvestorData>> et : tickerMap.entrySet()) {
				List<InvestorData> list = et.getValue();
				for (InvestorData d : list) {
					if (d.getType().equals(TradeType.BUY)) {
						buyOrders++;
					}
					if (d.getType().equals(TradeType.SELL)) {
						sellOrders++;
					}
				}
			}
			if (buyOrders > sellOrders) {
				investorList.add(e.getKey());
			}
		}
		System.out.println("Buy And Hold Investors are "+investorList.toString());
		return investorList;
	}

	@Override
	public String getMostSoldTicker() {
		Map<Integer, Map<String, List<InvestorData>>> investorDataMap = investorDataSource.investorDataMapRepo();
		Map<String, Integer> tickerCountMap = new HashMap<>();
		investorDataMap.entrySet().stream()
		.map(e -> e.getValue())
		.forEach(et -> et.entrySet().stream()
				.map(et1 -> et1.getValue())
				.forEach(list -> list.stream()
						.filter(d -> d.getType().equals(TradeType.SELL))
						.filter(d -> d!=null)
						.forEach(data -> {
							if (tickerCountMap.containsKey(data.getTicker())) {
								tickerCountMap.put(data.getTicker(), tickerCountMap.get(data.getTicker()) + 1);
							} else {
								tickerCountMap.put(data.getTicker(), 1);
							}
						})));
		
		
		Stack<String> stack = new Stack<>();
		tickerCountMap.forEach((ticker, count) -> {
			if (stack.isEmpty()) {
				stack.push(ticker);
			} else {
				if (count >= tickerCountMap.get(stack.peek())) {
					stack.push(ticker);
				}
			}
		});
		System.out.println("Most Sold Ticker= " + stack.peek());
		return stack.pop();
	}
	
	@Override
	public String getMostBoughtTicker() {
		Map<Integer, Map<String, List<InvestorData>>> investorDataMap = investorDataSource.investorDataMapRepo();
		Map<String, Integer> tickerCountMap = new HashMap<>();
		investorDataMap.entrySet().stream()
		.map(e -> e.getValue())
		.forEach(et -> et.entrySet().stream()
				.map(et1 -> et1.getValue())
				.forEach(list -> list.stream()
						.filter(d -> d.getType().equals(TradeType.BUY))
						.filter(d -> d!=null)
						.forEach(data -> {
							if (tickerCountMap.containsKey(data.getTicker())) {
								tickerCountMap.put(data.getTicker(), tickerCountMap.get(data.getTicker()) + 1);
							} else {
								tickerCountMap.put(data.getTicker(), 1);
							}
						})));
		
		
		Stack<String> stack = new Stack<>();
		tickerCountMap.forEach((ticker, count) -> {
			if (stack.isEmpty()) {
				stack.push(ticker);
			} else {
				if (count >= tickerCountMap.get(stack.peek())) {
					stack.push(ticker);
				}
			}
		});
		System.out.println("Most Bought Ticker= " + stack.peek());
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
		System.out.println("The active investors are "+activeInvestors.toString());;
		return activeInvestors;
	}

}
