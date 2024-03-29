package com.RateManagement.Entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import org.hibernate.validator.constraints.Range;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;


/**
 * @author R.Raj
 *created Rate table in the database by using spring boot JPA concept
 */


/**
 * @author R.Raj
 *
 */
@Entity
@Table(name="rate")
public class Rate implements Serializable
{
	//class fields
	
	@Id  //primary key
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="rate_Id")
	private Long rateId;
	
	//@FutureOrPresent
	@Column(name = "stay_date_from")
	@NotNull(message = "Please enter correct stay date from ")
    private LocalDate stayDateFrom;

	//@NotBlank(message="Stay date to is required ")
    @Column(name = "stay_date_to")
	@NotNull(message = "Please enter correct stay date To ")
    private LocalDate stayDateTo;

	//@NotBlank(message="number of night is required")
    @Column(name = "nights")
    @Range(message = "please enter correct nights value")
    private int nights;

	//@NotBlank(message="value is required")
	//@Size(min=0,message="value should be in positive")
    @Column(name = "value")
    @Range(message = "please enter correct price value")
    private double value;
    
    @Column(name="bungalowId")
    private Long bungalowId;

    //Many-To-One relationship
    
//    @ManyToOne(targetEntity=Bungalow.class)
//    @JoinColumn(name = "bungalowId",referencedColumnName="bungalowId")
//    private Bungalow bungalow;
    

    @Column(name = "closed_date")
    private LocalDate closedDate;


	/**
	 * @return rateId
	 */
	public Long getRateId() {
		return rateId;
	}


	/**
	 * @param rateId
	 * @return rateId
	 */
	public void setRateId(Long rateId) {
		this.rateId = rateId;
	}


	/**
	 * @return stayDateFrom
	 */
	public LocalDate getStayDateFrom() {
		return stayDateFrom;
	}


	
	/**
	 * @param stayDateFrom
	 */
	public void setStayDateFrom(LocalDate stayDateFrom) {
		this.stayDateFrom = stayDateFrom;
	}


	/**
	 * @return stayDateTo
	 */
	public LocalDate getStayDateTo() {
		return stayDateTo;
	}


	/**
	 * @param stayDateTo
	 */
	public void setStayDateTo(LocalDate stayDateTo) {
		this.stayDateTo = stayDateTo;
	}


	/**
	 * @return nights
	 */
	public int getNights() {
		return nights;
	}


	/**
	 * @param nights
	 */
	public void setNights(int nights) {
		this.nights = nights;
	}


	/**
	 * @return value
	 */
	public double getValue() {
		return value;
	}


	/**
	 * @param value
	 */
	public void setValue(double value) {
		this.value = value;
	}


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


//	public Bungalow getBungalow() {
//		return bungalow;
//	}
//
//
//	public void setBungalow(Bungalow bungalow) 
//	{
//		this.bungalow = bungalow;
//   }


	/**
	 * @return closedDate
	 */
	public LocalDate getClosedDate() {
		return closedDate;
	}


	/**
	 * @param closedDate
	 */
	public void setClosedDate(LocalDate closedDate) {
		this.closedDate = closedDate;
	}


	/**
	 * @param rateId
	 * @param stayDateFrom
	 * @param stayDateTo
	 * @param nights
	 * @param value
	 * @param bungalowId
	 * @param closedDate
	 * @return constructor does not have any return type in Java.
	 */
	public Rate(Long rateId, LocalDate stayDateFrom, LocalDate stayDateTo, int nights, double value, Long bungalowId,
			 LocalDate closedDate) {
		super();
		this.rateId = rateId;
		this.stayDateFrom = stayDateFrom;
		this.stayDateTo = stayDateTo;
		this.nights = nights;
		this.value = value;
		this.bungalowId = bungalowId;
		//this.bungalow = bungalow;
		this.closedDate = closedDate;
	}
	
	

	

	public Rate(LocalDate stayDateFrom, LocalDate stayDateTo, int nights, double value, Long bungalowId) {
		super();
		this.stayDateFrom = stayDateFrom;
		this.stayDateTo = stayDateTo;
		this.nights = nights;
		this.value = value;
		this.bungalowId = bungalowId;
	}


	/**
	 * @param this is the Auto generated constructor in which there is no argument..
	 * @return this return super class.
	 */  
	public Rate() {
		super();
		// TODO Auto-generated constructor stub
	}


	
	
	/**
	 * @param this is the string method to print the all rate objects..
	 * @return return a string representation of an object
	 */
	@Override
	public String toString() 
	{
		return "Rate [rateId=" + rateId + ", stayDateFrom=" + stayDateFrom + ", stayDateTo=" + stayDateTo + ", nights="
				+ nights + ", value=" + value + ", bungalowId=" + bungalowId + ", closedDate=" + closedDate + "]";
	}


	@Override
	public int hashCode() {
		return Objects.hash(bungalowId, closedDate, nights, stayDateFrom, stayDateTo, value);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rate other = (Rate) obj;
		return Objects.equals(bungalowId, other.bungalowId) && Objects.equals(closedDate, other.closedDate)
				&& nights == other.nights && Objects.equals(stayDateFrom, other.stayDateFrom)
				&& Objects.equals(stayDateTo, other.stayDateTo)
				&& Double.doubleToLongBits(value) == Double.doubleToLongBits(other.value);
	}


	
	    
    //Getter and Setter method
	
    
    



	
}