package com.qa.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.qa.util.commanUtils;
import com.relevantcodes.extentreports.LogStatus;
import io.restassured.response.Response;

public class OppenheimerTest extends commanUtils {

	@BeforeMethod()

	public void beforeMethod(Object[] param, Object[] param1) {
		test = report.startTest(param[0].toString(), param[1].toString());

	}

	// Sample set of users to be created
	org.json.JSONArray retriveData = new org.json.JSONArray();
	org.json.JSONArray createdData = new org.json.JSONArray();
	OppenheimerAPIWrapper apiUtils = new OppenheimerAPIWrapper();

	@Test(dataProvider = "UseCase1", priority = 0)
	public void insertSingleRecord_API(String TestCaseID, String TestCaseDescription, String BaseURI,
			String ContextPath, String PayloadData, String ResponseCode) throws ParseException {
		try {
			System.out.println("TestCaseID+++++++++++" + TestCaseID);
			JSONParser parser = new JSONParser();
			JSONObject json = null;
			json = (JSONObject) parser.parse(PayloadData);
			Map<String, String> defaultHeaders = new HashMap<String, String>();
			defaultHeaders.put("Content-Type", "application/json");
			Response response = postCall(BaseURI, ContextPath, json, defaultHeaders);

			int actualResponseCode = response.getStatusCode();

			if (actualResponseCode == Integer.parseInt(ResponseCode)) {
				test.log(LogStatus.PASS, "The expected response Code " + ResponseCode
						+ " matched the actual response code " + response.statusCode());

			} else {
				test.log(LogStatus.FAIL, "The expected response Code " + ResponseCode
						+ " not matched the actual response code " + response.statusCode());
			}
		} catch (NumberFormatException e) {
			test.log(LogStatus.ERROR, "insertSingleRecord_API_Error1" + e.getMessage());
		} catch (ParseException e) {
			test.log(LogStatus.ERROR, "insertSingleRecord_API_Error2" + e.getMessage());
		}
	}

	@Test(dataProvider = "UseCase2", priority = 1)
	public void insertMultipleRecord_API(String TestCaseID, String TestCaseDescription, String BaseURI,
			String ContextPath, String PayloadData, String ResponseCode) throws ParseException {
		try {
			System.out.println("TestCaseID+++++++++++" + TestCaseID);
			JSONParser parser = new JSONParser();
			JSONArray json = null;
			json = (JSONArray) parser.parse(PayloadData);
			Map<String, String> defaultHeaders = new HashMap<String, String>();
			defaultHeaders.put("Content-Type", "application/json");
			Response response = postCallwithArray(BaseURI, ContextPath, json, defaultHeaders);
			int actualResponseCode = response.getStatusCode();
			if (actualResponseCode == Integer.parseInt(ResponseCode)) {
				createdData.put(json);
				test.log(LogStatus.PASS, "The expected response Code " + ResponseCode
						+ " matched the actual response code " + response.statusCode());
			} else {
				test.log(LogStatus.FAIL, "The expected response Code " + ResponseCode
						+ " not matched the actual response code " + response.statusCode());
			}
		} catch (NumberFormatException e) {

			test.log(LogStatus.ERROR, "insertMultipleRecord_API_Error1" + e.getMessage());
		} catch (ParseException e) {

			test.log(LogStatus.ERROR, "insertMultipleRecord_API_Error2" + e.getMessage());

		}

	}

	@Test(dataProvider = "UseCase3", priority = 2)
	public void uploadSingleRecordIntoDB_UI1(String TestCaseID, String TestCaseDescription, String BrowserType,
			String appURL, String filePath, String FileName, String Result) throws InterruptedException, IOException {
		System.out.println(
				"+++++++++++++++++++++++++++++TestCaseID+++++++++++" + TestCaseID + "++++++++++++++++++++++++++++");
		try {
			initiazeBrowser(BrowserType);
			driver.get(appURL);
			clickUsingAction(By.xpath(envConfig.getProperty("uploadFile_XPath")));
			Thread.sleep(1000);
			if (filePath.contains("SingleRecord")) {
				String currentDirectory = System.getProperty("user.dir");
				System.out.println("The current working directory is " + currentDirectory);
				String fileUploadPath = currentDirectory + filePath + FileName;
				System.out.println("The current single record File directory path is " + fileUploadPath);
				performUploadFileAction(fileUploadPath);
				test.log(LogStatus.PASS, "Single Record File Location ::" + fileUploadPath);
				test.log(LogStatus.PASS, "Upload Single Record " + test.addScreenCapture((capture(driver))));
			} else {
				String currentDirectory = System.getProperty("user.dir");
				System.out.println("The current working directory is " + currentDirectory);
				String getFilePath = currentDirectory + filePath + FileName;
				System.out.println("The current Multiple record File directory path is " + getFilePath);
				performUploadFileAction(getFilePath);
				test.log(LogStatus.PASS, "Multiple Record File Location ::" + getFilePath);
				test.log(LogStatus.PASS, "Upload Multiple Record " + test.addScreenCapture((capture(driver))));
			}
		} catch (InterruptedException | IOException e) {
			test.log(LogStatus.ERROR, "uploadSingleRecordIntoDB_UI1_Error1" + e.getMessage());
		} finally {
			driver.quit();
		}

	}

	@Test(dataProvider = "UseCase4", priority = 4)
	public void BookKeeping_Manager_API(String TestCaseID, String TestCaseDescription, String BaseURI,
			String ContextPath, String PayloadData, String ResponseCode, String ExpectedResult) throws ParseException {

		Response response;
		int actualResponseCode;
		try {
			System.out.println(
					"+++++++++++++++++++++++++++++TestCaseID+++++++++++" + TestCaseID + "++++++++++++++++++++++++++++");
			/************ AC 1 get list of created items ***************/
			test.log(LogStatus.INFO, "-------------------------  Test Case AC1 ------------------------------ ");
			response = getCall(BaseURI, ContextPath);
			System.out.println(response.getBody().asString());
			actualResponseCode = response.getStatusCode();
			retriveData = new org.json.JSONArray(response.asString());
			if (actualResponseCode == Integer.parseInt(ResponseCode)) {
				test.log(LogStatus.PASS, "The expected response Code " + ResponseCode
						+ " matched the actual response code " + response.statusCode());
			} else {
				test.log(LogStatus.FAIL, "The expected response Code " + ResponseCode
						+ " not matched the actual response code " + response.statusCode());
			}
			/********* AC 2 verify natid masking ****************/
			/** Retrieving natid from the GET call results **/
			// List<String> natids = new ArrayList<String>();
			System.out.println("++++++++++retriveData++++++" + retriveData.length());
			for (int i = 0; i < retriveData.length(); i++) {

				String natidStr = retriveData.getJSONObject(i).getString("natid");
				String genderStr = "M";// retriveData.getJSONObject(i).getString("gender");
				String salaryStr = "700.00"; // retriveData.getJSONObject(i).getString("salary");
				String birthdayStr = "07071994"; // retriveData.getJSONObject(i).getString("birthday");
				String taxStr = "20.00"; // retriveData.getJSONObject(i).getString("tax");
				String nameStr = retriveData.getJSONObject(i).getString("name");
				String refliefStr = retriveData.getJSONObject(i).getString("relief");

				test.log(LogStatus.INFO,
						"-------------------------  Test Case AC2 to AC6------------------------------ ");
				boolean result = apiUtils.checkMask(natidStr);
				if (result)

					test.log(LogStatus.PASS, "....AC2 - The nat id only contains $ from 5th digit " + natidStr);
				else

					test.log(LogStatus.FAIL, "....AC2 - The nat id does not contains $ from 5th digit " + natidStr);

				double taxRelief = 0;
				try {

					test.log(LogStatus.INFO,
							"$$$$$$$$$$$$$$$$$$$$$$$$  .... Tax data for User  ....  $$$$$$$$$$$$$$$$$$$$$$$$  ->  "
									+ nameStr);

					/********* AC 3 CalculatedTaxRelief ****************/
					taxRelief = apiUtils.getCalculatedTaxRelief(Double.valueOf(salaryStr), Double.valueOf(taxStr),
							birthdayStr, genderStr);

					if (Double.valueOf(refliefStr) == taxRelief) {
						System.out.println(".......... Tax calculated result ..........." + taxRelief);
						test.log(LogStatus.PASS, "   .... AC3 - Tax Relief calculated Result ....  >>  " + "Actual :"
								+ refliefStr + " Expected :" + taxRelief);
					} else {
						System.out.println(".......... Tax calculated result ..........." + taxRelief);
						test.log(LogStatus.FAIL, "   .... AC3 - Tax Relief calculated Result ....  >>  " + "Actual :"
								+ refliefStr + " Expected :" + taxRelief);
					}

					/********* AC 4 Round off calculation ****************/
					if (Math.round(Double.valueOf(refliefStr)) == Math.round(taxRelief)) {
						test.log(LogStatus.PASS, "  ....AC4 Tax  Relief Round off calculation....  >>  " + "Actual: "
								+ Math.round(Double.valueOf(refliefStr)) + " Expected: " + Math.round(taxRelief));
					} else {
						test.log(LogStatus.FAIL, "  ....AC4 Tax  Relief Round off calculation....  >>  " + "Actual: "
								+ Math.round(Double.valueOf(refliefStr)) + " Expected: " + Math.round(taxRelief));
					}

					/********* AC 5 Final tax calculation ****************/
					boolean finalTaxResult = apiUtils.verifyFinalTaxReliefAmt(Double.valueOf(refliefStr));
					if (finalTaxResult) {

						test.log(LogStatus.PASS,
								"   .... AC5 - Verify Final tax calculation .... >>  " + "Result: In the Range");
					} else {
						test.log(LogStatus.FAIL,
								"   .... AC5 - Verify Final tax calculation .... >>  " + "Result: Not in the Range");
					}

					/********* AC 6 2 digit round round off value ****************/
					if (apiUtils.verifyTaxReliefAmtTwoDecimalPoint(refliefStr)) {
						test.log(LogStatus.PASS, " ....  AC6 Final Tax Relief with 2 digit round off ....  >>  "
								+ "Actual :" + refliefStr + " Expected :" + taxRelief);
					} else {
						test.log(LogStatus.FAIL, " ....  AC6 Final Tax Relief with 2 digit round off ....  >>  "
								+ "Actual :" + refliefStr + " Expected :" + taxRelief);
					}
				} catch (java.text.ParseException e) {

					test.log(LogStatus.ERROR, "BookKeeping_Manager_01" + e.getMessage());
				}
				test.log(LogStatus.INFO, " !!!!!!!!!!!!!!!!!!!!! .... End of user data .... !!!!!!!!!!  - > ");
			}

			System.out.println("+++++++++++Result+__________" + ExpectedResult);
			test.log(LogStatus.PASS, "I am inside UseCase4");

		} catch (NumberFormatException e) {
			test.log(LogStatus.ERROR, "BookKeeping_Manager_02" + e.getMessage());
		}
	}

//UC 5
	@Test(dataProvider = "UseCase5", priority = 5)
	public void dispense_TaxRelief_Governet_UI(String TestCaseID, String TestCaseDescription, String BrowserType,
			String appURL, String ExpectedResult) throws InterruptedException {

		try {
			initiazeBrowser(BrowserType);
			driver.get(appURL);
			// :AC1: The button on the screen must be red-colored
			if (TestCaseID.contains("OP_SIT_UC5_Test_01")) {
				String dispenseNowButtonColor = getCssValue(By.xpath(envConfig.getProperty("dispenseNowButton_Xpath")),
						"background-color");
				System.out.println("storeSignButtonRBGValue" + dispenseNowButtonColor);
				if (dispenseNowButtonColor.equals(ExpectedResult)) {
					scrollDown();
					Thread.sleep(1000);
					test.log(LogStatus.PASS, "Navigated to  OPPENHEIMER App - Dispense Section "
							+ test.addScreenCapture((capture(driver))));
					test.log(LogStatus.PASS, "The expected DispenseNow Button Color-> " + ExpectedResult
							+ "<- matched with Actual Button Red-Color-> " + dispenseNowButtonColor);
				} else {
					test.log(LogStatus.FAIL, "The expected DispenseNow Button Color " + ExpectedResult
							+ "not matched with Actual Button Red-Color" + dispenseNowButtonColor);
				}
			}
			// AC2
			else if (TestCaseID.contains("OP_SIT_UC5_Test_02")) {
				String buttonText = getText(By.xpath(envConfig.getProperty("buttonText_Xpath")));
				if (buttonText.equals(ExpectedResult)) {
					scrollDown();
					Thread.sleep(1000);
					test.log(LogStatus.PASS, "Navigated to  OPPENHEIMER App - Dispense Section "
							+ test.addScreenCapture((capture(driver))));
					test.log(LogStatus.PASS, "The expected DispenseNow Button  " + ExpectedResult
							+ " matched with Actual DispenseNow Button  " + buttonText);
				} else {
					test.log(LogStatus.FAIL, "The expected DispenseNow Button  " + ExpectedResult
							+ "does not matched with Actual DispenseNow Button" + buttonText);
				}
			}
			// Cash dispensed - AC3
			else if (TestCaseID.contains("OP_SIT_UC5_Test_03")) {
				click(By.xpath(envConfig.getProperty("buttonText_Xpath")));
				String dispenseText = getText(By.xpath(envConfig.getProperty("dispenseText")));
				if (dispenseText.equals(ExpectedResult)) {
					test.log(LogStatus.PASS, "The expected Dispensed Text-> " + ExpectedResult
							+ "<-matched with Actual Dispensed Text-> " + dispenseText);
				} else {
					test.log(LogStatus.FAIL, "The expected Dispensed Text-> " + ExpectedResult
							+ "<- does not matched with Actual Button Red-Color-> " + dispenseText);
				}

				if (getURL().contains("dispense")) {
					test.log(LogStatus.PASS, "Redirected URL " + test.addScreenCapture((capture(driver))));
				} else {
					test.log(LogStatus.FAIL, "Failed to Redirect the URL");
				}
			} else {
				test.log(LogStatus.INFO, "Test Condition failed");
			}
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			test.log(LogStatus.ERROR, "dispense_TaxRelief_Governet_UI_Error1" + e.getMessage());
		} finally {
			driver.quit();
		}
	}

}
