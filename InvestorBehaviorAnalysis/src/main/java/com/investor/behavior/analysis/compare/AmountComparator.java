package com.investor.behavior.analysis.compare;

import java.util.Comparator;

import com.investor.behavior.analysis.model.InvestorData;

public class AmountComparator implements Comparator<InvestorData> {

	@Override
	public int compare(InvestorData data1, InvestorData data2) {
		return data1.getAmount().compareTo(data2.getAmount());
	}

}
