package com.restaurant.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.restaurant.bean.DailyCoupon;
import com.restaurant.bean.FoodOrder;
import com.restaurant.bean.OrderIdentifier;
import com.restaurant.bean.PendingOrders;
import com.restaurant.dao.DBConnector;
import com.restaurant.twitter.TwitterConnector;
import com.restaurant.validation.ActionResponse;
import com.restaurant.validation.Feedback;
import com.restaurant.validation.Validator;

@RestController
@RequestMapping(value = "/wokInHere")
public class RestaurantController {

	@Autowired
	TwitterConnector connector;

	@Autowired
	DBConnector dbConnector;

	@RequestMapping(method = RequestMethod.POST, value = "/publishCoupon")
	public ResponseEntity<ActionResponse> publishCoupon(@RequestBody DailyCoupon dailyCoupon) {
		ActionResponse response = new ActionResponse();
		List<Feedback> feedbacks = new ArrayList<Feedback>();
		boolean isOperationSuccess = false;
		if (dailyCoupon.getExpiry() != null && dailyCoupon.getCoupon() != null) {
			boolean isValidCoupon = Validator.isCouponValid(dailyCoupon.getCoupon());
			boolean isValidDate = Validator.isDateValid(dailyCoupon.getExpiry());
			if (isValidCoupon && isValidDate) {
				connector.postMessage(dailyCoupon.getCoupon() + "\nCoupon expires on: " + dailyCoupon.getExpiry());
				feedbacks.add(new Feedback("operationSuccess", "Coupon posted successfully."));
				isOperationSuccess = true;
			} else {
				if (!isValidCoupon) {
					feedbacks.add(new Feedback("InvalidCoupon", "Coupon is Empty."));
				}
				if (!isValidDate) {
					feedbacks.add(new Feedback("InvalidExpiryDate",
							"Expected Date format:(MM/DD/YYYY). The date is invalid (or) is in the past."));
				}
			}
		} else {
			feedbacks.add(new Feedback("InvalidCoupon", "Coupon is Invalid."));
		}
		response.setResponse(feedbacks);
		ResponseEntity<ActionResponse> responseEntity = new ResponseEntity<ActionResponse>(response,
				isOperationSuccess ? HttpStatus.OK : HttpStatus.UNPROCESSABLE_ENTITY);
		return responseEntity;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/getOrders")
	public ResponseEntity<PendingOrders> getPendingOrders() {
		PendingOrders pendingOrders = new PendingOrders();
		List<FoodOrder> orders = dbConnector.getOrders();
		pendingOrders.setPendingOrders(orders);
		ResponseEntity<PendingOrders> responseEntity = new ResponseEntity<PendingOrders>(pendingOrders,
				orders.size() > 0 ? HttpStatus.OK : HttpStatus.NO_CONTENT);
		return responseEntity;
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/deleteOrder/{orderIdentifier}")
	public ResponseEntity<ActionResponse> CompleteOrder(@PathVariable String orderIdentifier) {
		OrderIdentifier orderId = new OrderIdentifier(orderIdentifier);
		ActionResponse response = new ActionResponse();
		List<Feedback> feedbacks = new ArrayList<Feedback>();
		boolean isOperationSuccess = false;
		if (orderId.getId() != null) {
			if (Validator.isOrderIdValid(orderId.getId())) {
				int deletes = dbConnector.deleteOrder(Long.valueOf(orderId.getId()));
				if (deletes == 1) {
					feedbacks.add(new Feedback("operationSuccess", "Order is deleted."));
					isOperationSuccess = true;
				}else {
					feedbacks.add(new Feedback("InvalidOrderId", "Enter a valid ID from the pending list."));
				}
			} else {
				feedbacks.add(new Feedback("InvalidOrderId", "Order should be number."));
			}
		} else {
			feedbacks.add(new Feedback("InvalidOrderId", "Order id is not specified."));
		}
		response.setResponse(feedbacks);
		ResponseEntity<ActionResponse> responseEntity = new ResponseEntity<ActionResponse>(response,
				isOperationSuccess ? HttpStatus.OK : HttpStatus.UNPROCESSABLE_ENTITY);
		return responseEntity;
	}

}
