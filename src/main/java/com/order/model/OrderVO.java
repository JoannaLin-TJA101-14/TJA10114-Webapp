package com.order.model;
import java.sql.Date;
import java.time.LocalDateTime;

public class OrderVO implements java.io.Serializable{
	private Integer orderNo;
	private Integer memNo;
	private Integer couponWalletNo;
	private LocalDateTime orderDatetime;
	private String orderStatus;
	private Integer orderTotal;
	private Integer pointUsed;
	
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	public Integer getMemNo() {
		return memNo;
	}
	public void setMemNo(Integer memNo) {
		this.memNo = memNo;
	}
	public Integer getCouponWalletNo() {
		return couponWalletNo;
	}
	public void setCouponWalletNo(Integer couponWalletNo) {
		this.couponWalletNo = couponWalletNo;
	}
	public LocalDateTime getOrderDatetime() {
		return orderDatetime;
	}
	public void setOrderDatetime(LocalDateTime orderDatetime) {
		this.orderDatetime = orderDatetime;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public Integer getOrderTotal() {
		return orderTotal;
	}
	public void setOrderTotal(Integer orderTotal) {
		this.orderTotal = orderTotal;
	}
	public Integer getPointUsed() {
		return pointUsed;
	}
	public void setPointUsed(Integer pointUsed) {
		this.pointUsed = pointUsed;
	}
	
	
	
	
	
}