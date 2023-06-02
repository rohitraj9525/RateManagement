package com.RateManagement.Entity;
import java.time.LocalDate;
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
	private LocalDate stayDateFrom;
	private int nights;
	private double value;
	private LocalDate stayDateTo;
	private LocalDate closeDate;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LocalDate getStayDateFrom() {
		return stayDateFrom;
	}
	public void setStayDateFrom(LocalDate stayDateFrom) {
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
	public LocalDate getStayDateTo() {
		return stayDateTo;
	}
	public void setStayDateTo(LocalDate stayDateTo) {
		this.stayDateTo = stayDateTo;
	}
	public LocalDate getCloseDate() {
		return closeDate;
	}
	public void setCloseDate(LocalDate closeDate) {
		this.closeDate = closeDate;
	}
	public Rate( LocalDate stayDateFrom, int nights, double value, LocalDate stayDateTo, LocalDate closeDate, Bungalow bungalow) {
		super();
		this.stayDateFrom = stayDateFrom;
		this.nights = nights;
		this.value = value;
		this.stayDateTo = stayDateTo;
		this.closeDate = closeDate;
		this.mybungalow=bungalow;
	}
	public Rate() {
		super();
		// TODO Auto-generated constructor stub
	}
	@ManyToOne(fetch=FetchType.LAZY,cascade={CascadeType.ALL})
	@JoinColumn(name="bungalow_id")
	private Bungalow mybungalow;
	public Bungalow getMybungalow() {
		return mybungalow;
	}
	public void setMybungalow(Bungalow mybungalow) {
		this.mybungalow = mybungalow;
	}
	
	
}
