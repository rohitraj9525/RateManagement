package com.RateManagement.Entity;

import java.time.LocalDate;

public class BookingFilter 
{
	private LocalDate startDate;
	private LocalDate endDate;
	private long bungalowId;
	//private int nights;
	private LocalDate closedDate;
	@Override
	public String toString() {
		return "BookingFilter [startDate=" + startDate + ", endDate=" + endDate + ", bungalowId=" + bungalowId
				+ "]";
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	public long getBungalowId() {
		return bungalowId;
	}
	public void setBungalowId(long bungalowId) {
		this.bungalowId = bungalowId;
	}
	
	public LocalDate getClosedDate() {
		return closedDate;
	}
	public void setClosedDate(LocalDate closedDate) {
		this.closedDate = closedDate;
	}
	public BookingFilter() {
		super();
		// TODO Auto-generated constructor stub
	}
	public BookingFilter(LocalDate startDate, LocalDate endDate, long bungalowId, int nights) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.bungalowId = bungalowId;
		
		//this.closedDate = closedDate;
	}
	
	
	

}
