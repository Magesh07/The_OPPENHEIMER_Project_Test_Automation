package com.qa.test;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONObject;

import com.google.gson.Gson;

public class OppenheimerAPIWrapper {

	private static final DecimalFormat twoDecimalPointRounding = new DecimalFormat("0.00");

	public boolean checkMask(String value) {
		int size = value.length();
		boolean result = false;
		for (int i = 5; i < size; i++) {
			result = value.charAt(i) == '$';
		}
		return result;
	}

	// AC3
	public double getCalculatedTaxRelief(double salary, double taxPaid, String dob, String gender)
			throws ParseException {
		System.out.println("......" + salary + "...." + taxPaid);
		double genderBonus = 0;
		double variable = 0;
		int userAge = 0;

		if (dob != "") {
			userAge = getUserAge(dob);
		}
		if (gender == "F") {
			genderBonus = 500;
		}
		if (userAge <= 18) {
			variable = 1;
		} else if (userAge <= 35) {
			variable = 0.8;
		} else if (userAge <= 50) {
			variable = 0.5;
		} else if (userAge <= 50) {
			variable = 0.89;
		} else if (userAge >= 80) {
			variable = 0.90;
		}
		// need to write later
		System.out.println("$$$$$$$$$$$$$" + salary);
		System.out.println("$$$$$$$$$$$$$" + taxPaid);
		System.out.println("$$$$$$$$$$$$$" + variable);
		System.out.println("$$$$$$$$$$$$$" + genderBonus);
		double taxReliefCal = ((salary - taxPaid) * (variable) + genderBonus);
		return taxReliefCal;
	}

//AC5
	public boolean verifyFinalTaxReliefAmt(double taxRelief) {
		boolean result = false;
		System.out.println("Rounding value " + result);

		if (taxRelief >= 0 && taxRelief <= 50) {
			result = true;
		}
		return result;

	}

// AC 6
	public Boolean verifyTaxReliefAmtTwoDecimalPoint(String taxRelief) {
		boolean result = false;
		String[] splitter = taxRelief.toString().split("\\.");
		int decimalLength = splitter[1].length();
		if (decimalLength == 2) {
			result = true;
		}
		return result;
	}

	// Method to return the date
	public static Date StringToDate(String dob) throws ParseException {
		// Instantiating the SimpleDateFormat class
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		// Parsing the given String to Date object
		Date date = formatter.parse(dob);
		System.out.println("Date object value: " + date);
		return date;
	}

//return age
	public static int getUserAge(String dob) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
		
		Date date = formatter.parse(dob);
	
		// Converting obtained Date object to LocalDate object
		Instant instant = date.toInstant();
		ZonedDateTime zone = instant.atZone(ZoneId.systemDefault());
		LocalDate givenDate = zone.toLocalDate();
		// Calculating the difference between given date to current date.
		Period period = Period.between(givenDate, LocalDate.now());
	
		System.out.print(period.getYears() + " years " + period.getMonths() + " and " + period.getDays() + " days");
		int agevalue = period.getYears();
		return agevalue;
	}

}