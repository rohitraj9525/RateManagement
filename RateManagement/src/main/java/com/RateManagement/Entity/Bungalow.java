package com.RateManagement.Entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import com.RateManagement.Entity.Rate;



@Entity

public class Bungalow 
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long bungalowId;
	private String bungalowName;
	private String bungalowType;
	
	
	
	public List<Rate> getRates() {
		return rates;
	}
	public void setRates(List<Rate> rates) {
		this.rates = rates;
	}
	public Long getBungalowId() {
		return bungalowId;
	}
	public void setBungalowId(Long bungalowId) {
		this.bungalowId = bungalowId;
	}
	public String getBungalowName() {
		return bungalowName;
	}
	public void setBungalowName(String bungalowName) {
		this.bungalowName = bungalowName;
	}
	public String getBungalowType() {
		return bungalowType;
	}
	public void setBungalowType(String bungalowType) {
		this.bungalowType = bungalowType;
	}
	
	
	public Bungalow(String bungalowName, String bungalowType) {
		super();
		
		this.bungalowName = bungalowName;
		this.bungalowType = bungalowType;
	}
		public Bungalow() {
		super();
		// TODO Auto-generated constructor stub
	}
	@OneToMany(mappedBy ="mybungalow",cascade={CascadeType.ALL})
	private List<Rate> rates;
	
	

}
