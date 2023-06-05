package com.RateManagement.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


/**
 * @author R.Raj
 * created bungalow Table in the database with the help of spring boot JPA 
 */

@Entity
@Table(name="bungalow")
public class Bungalow
{
	//class fields
	@Id
	
	//@Column(name="bungalow_Id")
	private Long bungalowId;
	
	@Column(name="bungalow_Name")
	private String bungalowName;
	
	@Column(name="bungalow_Type")
	private String bungalowType;
	
	//Getter and Setter method

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
	
	//Constructor with parameter

	public Bungalow(Long bungalowId, String bungalowName, String bungalowType) {
		super();
		this.bungalowId = bungalowId;
		this.bungalowName = bungalowName;
		this.bungalowType = bungalowType;
	}
	
	//Constructor with no parameter

	public Bungalow() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	//ToString Method

	@Override
	public String toString() {
		return "Bungalow [bungalowId=" + bungalowId + ", bungalowName=" + bungalowName + ", bungalowType="
				+ bungalowType + "]";
	}
	
}