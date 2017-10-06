package com.restaurant.bean;

import java.util.List;

public class PendingOrders {

	private List<FoodOrder> pendingOrders;

	public List<FoodOrder> getPendingOrders() {
		return pendingOrders;
	}

	public void setPendingOrders(List<FoodOrder> pendingOrders) {
		this.pendingOrders = pendingOrders;
	}
}
