package com.restaurant.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Validator {

	public static boolean isDateValid(String couponDate) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		dateFormat.setLenient(false);
		try {
			Date expiry = dateFormat.parse(couponDate.trim());
			System.out.println(expiry);
			System.out.println(calendar.getTime());
			System.out.println(expiry.compareTo(calendar.getTime()));
			if (expiry.compareTo(calendar.getTime()) >= 0)
				return true;
			else
				return false;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean isCouponValid(String coupon) {
		if (coupon == null || coupon.trim().equals(""))
			return false;
		else
			return true;
	}

	public static boolean isOrderIdValid(String orderId) {
		try {
			Long.valueOf(orderId);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
