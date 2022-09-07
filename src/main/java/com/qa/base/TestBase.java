package com.qa.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.qa.util.ExcelUtil;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

public class TestBase {

	public static ExtentTest test;
	protected static ExtentReports report;
	protected static WebDriver driver;
	public static Properties envConfig;
	WebDriverWait wait;
	public String testCaseName, testDescription, category, authors;
	public static ExtentHtmlReporter html;

//	private static final String BROWSER = System.getProperty("browser", "Chrome");

	@BeforeSuite
	public void suiteSetup() throws Exception {
		// Environment specific properties file loading
		InputStream configFile = new FileInputStream(
				System.getProperty("user.dir") + "\\src\\main\\java\\com\\qa\\properties\\" + "test.properties");
		envConfig = new Properties();
		envConfig.load(configFile);
		FileUtils.cleanDirectory(new File(System.getProperty("user.dir") + "\\report"));
		report = new ExtentReports(System.getProperty("user.dir") + "\\report\\ExtentReportResults.html");
	}

	@AfterMethod
	public void screenshotAndDeleteCookies(ITestResult testResult) throws IOException {
		
		// Deleting cookies
		if (driver != null) {
			//driver.manage().deleteAllCookies();
			System.out.println("I am inside After Method");
			driver.quit();
		}
		report.endTest(test);
	}

	@AfterSuite
	public void suiteTearDown() {
		report.flush();
		// driver.quit();
	}

	@DataProvider(name = "UseCase1")

	public Object[][] UseCase1() throws Exception {

		String[][] testData = ExcelUtil.getExcelDataIn2DArray("src//main//resources//testData//TestData.xlsx",
				"UseCase1");
		return testData;
	}

	@DataProvider(name = "UseCase2")

	public Object[][] UseCase2() throws Exception {

		String[][] testData = ExcelUtil.getExcelDataIn2DArray("src//main//resources//testData//TestData.xlsx",
				"UseCase2");
		return testData;
	}

	@DataProvider(name = "UseCase3")

	public Object[][] UseCase3() throws Exception {

		System.out.println("I am inside UseCase3");

		String[][] testData = ExcelUtil.getExcelDataIn2DArray("src//main//resources//testData//TestData.xlsx",
				"UseCase3");
		return testData;
	}

	@DataProvider(name = "UseCase4")

	public Object[][] UseCase4() throws Exception {

		String[][] testData = ExcelUtil.getExcelDataIn2DArray("src//main//resources//testData//TestData.xlsx",
				"UseCase4");
		return testData;
	}

	@DataProvider(name = "UseCase5")

	public Object[][] UseCase5() throws Exception {

		String[][] testData = ExcelUtil.getExcelDataIn2DArray("src//main//resources//testData//TestData.xlsx",
				"UseCase5");
		return testData;
	}


	public static String capture(WebDriver driver) throws IOException {
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		File Dest = new File("src/../report/" + System.currentTimeMillis() + ".png");
		String errflpath = Dest.getAbsolutePath();
		FileUtils.copyFile(scrFile, Dest);
		return errflpath;
	}

}
