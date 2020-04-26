package com.investor.behavior.analysis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.investor.behavior.analysis.model.InvestorData;
import com.investor.behavior.analysis.repo.InvestorDataSource;
import com.investor.behavior.analysis.repo.InvestorDataSourceImpl;
import com.investor.behavior.analysis.service.InvestorDataService;
import com.investor.behavior.analysis.service.InvestorDataServiceImpl;

@Configuration
@PropertySource("config.properties")
public class AppConfig {

	@Value("${fileName}")
	String fileName;
	
	@Bean
	public InvestorData investorData(){
		return new InvestorData();
	}
	
	@Bean
	public InvestorDataSource investorDataSource(){
		return new InvestorDataSourceImpl(fileName);
	}
	
	@Bean
	public InvestorDataService investorDataService(){
		return new InvestorDataServiceImpl(investorDataSource());
		//return new InvestorDataServiceImpl();
	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
	    return new PropertySourcesPlaceholderConfigurer();
	}
}
