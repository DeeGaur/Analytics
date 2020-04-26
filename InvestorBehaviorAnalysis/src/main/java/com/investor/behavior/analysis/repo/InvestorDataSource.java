package com.investor.behavior.analysis.repo;

import java.util.List;
import java.util.Map;

import com.investor.behavior.analysis.model.InvestorData;

public interface InvestorDataSource {

	public void dataSourceFromCSVFile();
	public void dataSourceFromJSONFile();
	public List<InvestorData> investorDataRepo();
	public Map<Integer, List<InvestorData>> investorDataMap();
	
	public Map<Integer, Map<String, List<InvestorData>>> investorDataMapRepo();
}
