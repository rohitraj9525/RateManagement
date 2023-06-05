package com.RateManagement.Entity;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


/**
 * @author R.Raj
 *created Rate table in the database by using spring boot JPA concept
 */

@Entity
@Table(name="rate")
public class Rate 
{
	//class fields
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="rate_Id")
	private Long rateId;
	
	@Column(name = "stay_date_from")
    private LocalDate stayDateFrom;

    @Column(name = "stay_date_to")
    private LocalDate stayDateTo;

    @Column(name = "nights")
    private int nights;

    @Column(name = "value")
    private double value;

    //Many-To-One relationship
    
    @ManyToOne(targetEntity=Bungalow.class,cascade=CascadeType.PERSIST)
    @JoinColumn(name = "bungalow_id",referencedColumnName="bungalowId")
    private Bungalow bungalow;
    

    @Column(name = "closed_date")
    private LocalDate closedDate;
    
    //Getter and Setter method



	public Long getRateId() {
		return rateId;
	}


	public void setRateId(Long rateId) {
		this.rateId = rateId;
	}


	public LocalDate getStayDateFrom() {
		return stayDateFrom;
	}


	public void setStayDateFrom(LocalDate stayDateFrom) {
		this.stayDateFrom = stayDateFrom;
	}


	public LocalDate getStayDateTo() {
		return stayDateTo;
	}


	public void setStayDateTo(LocalDate stayDateTo) {
		this.stayDateTo = stayDateTo;
	}


	public int getNights() {
		return nights;
	}


	public void setNights(int nights) {
		this.nights = nights;
	}


	public double getValue() {
		return value;
	}


	public void setValue(double value) {
		this.value = value;
	}


	public Bungalow getBungalow() {
		return bungalow;
	}


	public void setBungalow(Bungalow bungalow) {
		this.bungalow = bungalow;
	}


	public LocalDate getClosedDate() {
		return closedDate;
	}


	public void setClosedDate(LocalDate closedDate) {
		this.closedDate = closedDate;
	}
	
	//Constructor using parameter



	public Rate(Long rateId, LocalDate stayDateFrom, LocalDate stayDateTo, int nights, double value, Bungalow bungalow,
			LocalDate closedDate) {
		super();
		this.rateId = rateId;
		this.stayDateFrom = stayDateFrom;
		this.stayDateTo = stayDateTo;
		this.nights = nights;
		this.value = value;
		this.bungalow = bungalow;
		this.closedDate = closedDate;
	}

	//Constructor with no arguments

	public Rate() {
		super();
		// TODO Auto-generated constructor stub
	}

	//ToString method

	@Override
	public String toString() {
		return "Rate [rateId=" + rateId + ", stayDateFrom=" + stayDateFrom + ", stayDateTo=" + stayDateTo + ", nights="
				+ nights + ", value=" + value + ", bungalow=" + bungalow + ", closedDate=" + closedDate + "]";
	}
	
}