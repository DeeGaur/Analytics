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

	private static List<InvestorData> list = new ArrayList<>();
	private static Map<Integer, List<InvestorData>> investorDataMap= new HashMap<Integer, List<InvestorData>>();

	private static Map<Integer, Map<String, List<InvestorData>>> dataMapById = new HashMap<>();
	
	private String fileName;
	private final String CSV_DELIMITER=",";

	public InvestorDataSourceImpl(String fileName) {
		this.fileName = fileName;
		dataSourceFromCSVFile();
		dataSourceFromJSONFile();
	}


	@Override
	public Map<Integer, Map<String, List<InvestorData>>> investorDataMapRepo() {
		return dataMapById;
	}

	@Override
	public List<InvestorData> investorDataRepo() {
		return list;
	}

	@Override
	public Map<Integer, List<InvestorData>> investorDataMap() {
		return investorDataMap;
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
	                list.add(investorData);  
	                
	                //data setup in map
	                if(investorDataMap.containsKey(investorData.getInvestorId())){
        				investorDataMap.get(investorData.getInvestorId()).add(investorData);
        			}else {
        				List<InvestorData> l = new ArrayList<>();
        				l.add(investorData);
        				investorDataMap.put(investorData.getInvestorId(), l);
        			}
	                
	                //organized map
	                //Map<String, Map<TradeType, InvestorData>> dataMapByTicker = new HashMap<String, Map<TradeType,InvestorData>>();
                	//Map<TradeType, InvestorData> dataMapByTradeType = new EnumMap<>(TradeType.class);
                	
	                if(dataMapById.containsKey(investorData.getInvestorId())){
	                	Map<String, List<InvestorData>> dataMapByTicker = dataMapById.get(investorData.getInvestorId());
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
	                	dataMapById.put(investorData.getInvestorId(), dataMapByTicker);
	                }
	                
	            }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void dataSourceFromJSONFile() {
		// TODO Auto-generated method stub

	}

	public static List<InvestorData> getList() {
		return list;
	}

	public static void setList(List<InvestorData> list) {
		InvestorDataSourceImpl.list = list;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	

	public static Map<Integer, List<InvestorData>> getInvestorDataMap() {
		return investorDataMap;
	}

	public static void setInvestorDataMap(Map<Integer, List<InvestorData>> investorDataMap) {
		InvestorDataSourceImpl.investorDataMap = investorDataMap;
	}


	public static Map<Integer, Map<String, List<InvestorData>>> getDataMapById() {
		return dataMapById;
	}


	public static void setDataMapById(Map<Integer, Map<String, List<InvestorData>>> dataMapById) {
		InvestorDataSourceImpl.dataMapById = dataMapById;
	}

}
