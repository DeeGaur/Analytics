package com.investor.behavior.analysis.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.investor.behavior.analysis.config.AppConfig;
import com.investor.behavior.analysis.model.InvestorData;
import com.investor.behavior.analysis.model.TradeType;
import com.investor.behavior.analysis.repo.InvestorDataSource;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
@TestPropertySource("classpath:testconfig.properties")
class InvestorDataServiceImplTest {

	@Autowired
	private InvestorDataSource investorDataSource;
	
	private static Map<Integer, Map<String, List<InvestorData>>> investorDataMap = new HashMap<>();
	
	@Autowired
	private InvestorDataService investorDataService;
	
	@BeforeEach
	void init() {
		investorDataMap = investorDataSource.investorDataMapRepo();
	}
	
	@Test
	@DisplayName("Test BuyAndHoldInvestors")
	void testGetBuyAndHoldInvestors() {
		List<Integer> actual = investorDataService.getBuyAndHoldInvestors();
		List<Integer> expected = Arrays.asList(1, 2);
		assertEquals(expected, actual, "should return BuyAndHoldInvestors");
	}

	@Test 
	void testGetMostSoldTicker() {
		String mostSoldTicker = investorDataService.getMostSoldTicker();
		assertTrue(mostSoldTicker.equals("AAPL"), "should return the most sold ticker");
	}

	@Test
	void testGetMostBoughtTicker() {
		String mostBoughtTicker = investorDataService.getMostBoughtTicker();
		assertTrue(mostBoughtTicker.equals("AAPL"), "should return the most bought ticker");;
	}

	@Disabled
	@Test
	void testPrintSmallestAndLargestTradeForAll() {
		fail("Not yet implemented");
	}

	@Test
	void testGetSmallestTradeForAll() {
		Map<Integer, InvestorData> actual = investorDataService.getSmallestTradeForAll();
		InvestorData expected1 = new InvestorData();
		expected1.setInvestorId(1);
		expected1.setTicker("BR");
		expected1.setType(TradeType.BUY);
		expected1.setAmount(3845.0);
		expected1.setDate(LocalDate.parse("2016-09-25"));
		
		InvestorData expected2 = new InvestorData();
		expected2.setInvestorId(2);
		expected2.setTicker("BR");
		expected2.setType(TradeType.BUY);
		expected2.setAmount(2458.0);
		expected2.setDate(LocalDate.parse("2017-12-03"));
		
		InvestorData expected3 = new InvestorData();
		expected3.setInvestorId(3);
		expected3.setTicker("CBPO");
		expected3.setType(TradeType.BUY);
		expected3.setAmount(245.0);
		expected3.setDate(LocalDate.parse("2017-12-03"));
		
		assertEquals(expected1.getInvestorId(), actual.get(1).getInvestorId(), "should return the smallest trade for investor id 1");
		assertEquals(expected1.getTicker(), actual.get(1).getTicker(), "should return the ticker of the smallest trade for investor id 1");
		assertEquals(expected1.getType(), actual.get(1).getType(), "should return the trade type of the smallest trade for investor id 1");
		assertEquals(expected1.getAmount(), actual.get(1).getAmount(), "should return the amount of the smallest trade for investor id 1");
		
		assertEquals(expected2.getInvestorId(), actual.get(2).getInvestorId(), "should return the smallest trade for investor id 2");
		assertEquals(expected2.getTicker(), actual.get(2).getTicker(), "should return the ticker of the smallest trade for investor id 2");
		assertEquals(expected2.getType(), actual.get(2).getType(), "should return the trade type of the smallest trade for investor id 2");
		assertEquals(expected2.getAmount(), actual.get(2).getAmount(), "should return the amount of the smallest trade for investor id 2");
		
		assertEquals(expected3.getInvestorId(), actual.get(3).getInvestorId(), "should return the smallest trade for investor id 3");
		assertEquals(expected3.getTicker(), actual.get(3).getTicker(), "should return the ticker of the smallest trade for investor id 3");
		assertEquals(expected3.getType(), actual.get(3).getType(), "should return the trade type of the smallest trade for investor id 3");
		assertEquals(expected3.getAmount(), actual.get(3).getAmount(), "should return the amount of the smallest trade for investor id 3");
		
	}

	@Test
	void testGetLargestTradeForAll() {
		Map<Integer, InvestorData> actual = investorDataService.getLargestTradeForAll();
		InvestorData expected1 = new InvestorData();
		expected1.setInvestorId(1);
		expected1.setTicker("MO");
		expected1.setType(TradeType.BUY);
		expected1.setAmount(22575.0);
		
		InvestorData expected2 = new InvestorData();
		expected2.setInvestorId(2);
		expected2.setTicker("LVS");
		expected2.setType(TradeType.BUY);
		expected2.setAmount(6243.0);
		
		InvestorData expected3 = new InvestorData();
		expected3.setInvestorId(3);
		expected3.setTicker("PVH");
		expected3.setType(TradeType.SELL);
		expected3.setAmount(12483.0);
		
		assertEquals(expected1.getInvestorId(), actual.get(1).getInvestorId(), "should return the largest trade for investor id 1");
		assertEquals(expected1.getTicker(), actual.get(1).getTicker(), "should return the ticker of the largest trade for investor id 1");
		assertEquals(expected1.getType(), actual.get(1).getType(), "should return the trade type of the largest trade for investor id 1");
		assertEquals(expected1.getAmount(), actual.get(1).getAmount(), "should return the amount of the largest trade for investor id 1");
		
		assertEquals(expected2.getInvestorId(), actual.get(2).getInvestorId(), "should return the largest trade for investor id 2");
		assertEquals(expected2.getTicker(), actual.get(2).getTicker(), "should return the ticker of the largest trade for investor id 2");
		assertEquals(expected2.getType(), actual.get(2).getType(), "should return the trade type of the largest trade for investor id 2");
		assertEquals(expected2.getAmount(), actual.get(2).getAmount(), "should return the amount of the largest trade for investor id 2");
		
		assertEquals(expected3.getInvestorId(), actual.get(3).getInvestorId(), "should return the largest trade for investor id 3");
		assertEquals(expected3.getTicker(), actual.get(3).getTicker(), "should return the ticker of the largest trade for investor id 3");
		assertEquals(expected3.getType(), actual.get(3).getType(), "should return the trade type of the largest trade for investor id 3");
		assertEquals(expected3.getAmount(), actual.get(3).getAmount(), "should return the amount of the largest trade for investor id 3");
		
	}

	@Disabled
	@Test
	void testGetActiveInvestors() {
		fail("Not yet implemented");
	}

	@Disabled
	@Test
	void testPrintActiveInvestors() {
		fail("Not yet implemented");
	}

}
