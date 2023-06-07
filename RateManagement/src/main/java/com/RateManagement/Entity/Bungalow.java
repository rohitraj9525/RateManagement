package com.RateManagement.Entity;

import org.antlr.v4.runtime.misc.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


/**
 * @author R.Raj
 * created bungalow Table in the database with the help of spring boot JPA 
 */

@Entity
@Table(name="bungalow")
public class Bungalow
{
	//class fields
	@Id  //primary key
	@GeneratedValue(strategy = GenerationType.AUTO)
	
	//@Column(name="bungalow_Id")
//	@NotBlank(message="Bungalow ID is required")
//	@Size(max=100,message="Bungalow ID cannot exceed 100 charachters")

	private Long bungalowId;
	
	@NotBlank(message="bungalow name is required")
	@Size(max=100,min=1,message="Bungalow name cannot exceed 100 characters and should be positive  ")
	@Column(name="bungalow_Name")
	private String bungalowName;
	
	@Column(name="bungalow_Type")
	private String bungalowType;
	
	//Getter and Setter method

	/**
	 * @return bungalowId
	 */
	public Long getBungalowId() {
		return bungalowId;
	}

	/**
	 * @param bungalowId
	 */
	public void setBungalowId(Long bungalowId) {
		this.bungalowId = bungalowId;
	}

	/**
	 * @return bungalowName
	 */
	public String getBungalowName() {
		return bungalowName;
	}

	/**
	 * @param bungalowName
	 */
	public void setBungalowName(String bungalowName) {
		this.bungalowName = bungalowName;
	}

	/**
	 * @return bungalowType
	 */
	public String getBungalowType() {
		return bungalowType;
	}

	/**
	 * @param bungalowType
	 */
	public void setBungalowType(String bungalowType) {
		this.bungalowType = bungalowType;
	}
	
	//Constructor with parameter

	/**
	 * @param bungalowId
	 * @param bungalowName
	 * @param bungalowType
	 */
	public Bungalow(Long bungalowId, String bungalowName, String bungalowType) {
		super();
		this.bungalowId = bungalowId;
		this.bungalowName = bungalowName;
		this.bungalowType = bungalowType;
	}
	
	//Constructor with no parameter

	/**
	 * @param
	 * @return 
	 */
	public Bungalow() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	//ToString Method

	/**
	 * @param
	 * @return
	 */
	@Override
	public String toString() {
		return "Bungalow [bungalowId=" + bungalowId + ", bungalowName=" + bungalowName + ", bungalowType="
				+ bungalowType + "]";
	}
	
}