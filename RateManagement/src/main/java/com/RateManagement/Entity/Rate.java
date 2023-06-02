package com.RateManagement.Entity;
import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import com.RateManagement.Entity.Bungalow;



@Entity
public class Rate 
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private Date stayDateFrom;
	private int nights;
	private double value;
	private Date stayDateTo;
	private Date closeDate;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getStayDateFrom() {
		return stayDateFrom;
	}
	public void setStayDateFrom(Date stayDateFrom) {
		this.stayDateFrom = stayDateFrom;
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
	public Date getStayDateTo() {
		return stayDateTo;
	}
	public void setStayDateTo(Date stayDateTo) {
		this.stayDateTo = stayDateTo;
	}
	public Date getCloseDate() {
		return closeDate;
	}
	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}
	public Rate(Long id, Date stayDateFrom, int nights, double value, Date stayDateTo, Date closeDate) {
		super();
		this.id = id;
		this.stayDateFrom = stayDateFrom;
		this.nights = nights;
		this.value = value;
		this.stayDateTo = stayDateTo;
		this.closeDate = closeDate;
	}
	public Rate() {
		super();
		// TODO Auto-generated constructor stub
	}
	@ManyToOne(fetch=FetchType.LAZY,cascade={CascadeType.ALL})
	@JoinColumn(name="bungalow_id")
	private Bungalow mybungalow;
}
