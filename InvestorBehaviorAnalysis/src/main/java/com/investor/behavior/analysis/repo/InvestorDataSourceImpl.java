package com.investor.behavior.analysis.repo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import com.investor.behavior.analysis.model.InvestorData;
import com.investor.behavior.analysis.model.TradeType;

@Repository
public class InvestorDataSourceImpl implements InvestorDataSource {

	private static Map<Integer, Map<String, List<InvestorData>>> dataMap = new HashMap<>();
	private String fileName;
	private final String CSV_DELIMITER=",";

	public InvestorDataSourceImpl(String fileName) {
		this.fileName = fileName;
		dataSourceFromCSVFile();
		dataSourceFromJSONFile();
	}

	@Override
	public Map<Integer, Map<String, List<InvestorData>>> investorDataMapRepo() {
		return dataMap;
	}

	public void dataSourceFromCSVFile() {

		String line ="";
		try(BufferedReader br = new BufferedReader(new FileReader(new ClassPathResource(fileName).getFile()))) {
			 while ((line = br.readLine()) != null) {
	                // use comma as separator
	                String[] data = line.split(CSV_DELIMITER);
	                
	                //assuming the order of the columns in the file is always maintained
	                InvestorData investorData = new InvestorData();
	                investorData.setInvestorId(Integer.parseInt(data[0].trim()));
	                investorData.setTicker(data[1].trim());
	                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
	                LocalDate date = LocalDate.parse(data[2].trim(), formatter);
	                investorData.setDate(date);
	                investorData.setType(TradeType.valueOf(data[3].trim()));
	                investorData.setAmount(Double.parseDouble(data[4].trim()));
	            
	                if(dataMap.containsKey(investorData.getInvestorId())){
	                	Map<String, List<InvestorData>> dataMapByTicker = dataMap.get(investorData.getInvestorId());
	                	if(dataMapByTicker.containsKey(investorData.getTicker())){
	                		dataMapByTicker.get(investorData.getTicker()).add(investorData);
	                	}else{
	                		List<InvestorData> list = new ArrayList<>();
	                		list.add(investorData);
	                		dataMapByTicker.put(investorData.getTicker(), list);
	                	}
	                }else{
	                	Map<String, List<InvestorData>> dataMapByTicker = new HashMap<>();
	                	//setting up the new entry
	                	List<InvestorData> list = new ArrayList<>();
	                	list.add(investorData);
	                	dataMapByTicker.put(investorData.getTicker(), list);
	                	dataMap.put(investorData.getInvestorId(), dataMapByTicker);
	                }
	            }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void dataSourceFromJSONFile() {
		// TODO Auto-generated method stub

	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public static Map<Integer, Map<String, List<InvestorData>>> getDataMapById() {
		return dataMap;
	}


	public static void setDataMapById(Map<Integer, Map<String, List<InvestorData>>> dataMapById) {
		InvestorDataSourceImpl.dataMap = dataMapById;
	}

}
