package com.restaurant.bean;

import java.util.Date;

public class FoodOrder {

	private long id;
	private String hashTag;
	private String orderMessage;
	private String customerId;
	private Date createdTime;

	public FoodOrder(long id, String hashTag, String orderMessage, String customerId, Date createdTime) {
		this.id = id;
		this.hashTag = hashTag;
		this.orderMessage = orderMessage;
		this.customerId = customerId;
		this.createdTime = createdTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getHashTag() {
		return hashTag;
	}

	public void setHashTag(String hashTag) {
		this.hashTag = hashTag;
	}

	public String getOrderMessage() {
		return orderMessage;
	}

	public void setOrderMessage(String orderMessage) {
		this.orderMessage = orderMessage;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	
}
