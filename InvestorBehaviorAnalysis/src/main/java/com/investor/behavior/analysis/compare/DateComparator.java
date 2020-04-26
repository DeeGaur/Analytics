package com.investor.behavior.analysis.compare;

import java.util.Comparator;

import com.investor.behavior.analysis.model.InvestorData;

public class DateComparator implements Comparator<InvestorData> {

	@Override
	public int compare(InvestorData data1, InvestorData data2) {
		// TODO Auto-generated method stub
		return data1.getDate().compareTo(data2.getDate());
	}

}
