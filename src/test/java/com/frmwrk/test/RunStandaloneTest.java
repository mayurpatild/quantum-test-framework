package com.frmwrk.test;

import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.frmwrk.base.TestBaseSetup;
import com.frmwrk.base.exceptions.DatabaseConnectivityException;
import com.frmwrk.base.utils.*;

/**
 * This TestNG class will be used for any standalone testing that needs to be performed for the functions or classes 
 * that are getting created new or getting updated
 * @author Mayur Patil
 *
 */
public class RunStandaloneTest {
	TestBaseSetup ts;
	private static RemoteWebDriver driver;
	String browserType;
	
	@BeforeClass
	public void beforeclass() {
		ts = new TestBaseSetup();
	}
	
	//@Test
	/*public void Execute_Test() throws DatabaseConnectivityException, SQLException {
		DatabaseUtil DBU = new DatabaseUtil(ts.databaseType,ts.driverClassName,ts.databaseUserName,
				                           ts.databasePassword,ts.databaseServerIP);
		DBU.getConnection();
		String sqlQuery = "SELECT * FROM salarydetails";
		String[][] dbData = DBU.getDataFromDatabase("salarydetails",sqlQuery);
		System.out.println("\n");
		for (int i = 0; i <= dbData.length - 1; i++) {
			for (int j = 0; j <= dbData[i].length - 1; j++) {
				System.out.println(dbData[i][j]);
			}
		}
		DBU.closeDatabaseConnectivity();
	}*/
	
	@Test
	public void testCal() throws Exception {
   //locate the Text on the calculator by using By.name()
		URL remoteServerUrl = new URL("http://" + ts.webdriverServerHostName
					+ ":" + ts.webdriverServerPortName + "/wd/hub");
		browserType="appiumApp";
		ts.initializeBrowser(browserType,remoteServerUrl);
		driver=TestBaseSetup.getDriver();
		WebElement two=driver.findElement(By.name("2"));
		two.click();
		WebElement plus=driver.findElement(By.name("+"));
		plus.click();
		WebElement four=driver.findElement(By.name("4"));
		four.click();
		WebElement equalTo=driver.findElement(By.name("="));
		equalTo.click();
   //locate the edit box of the calculator by using By.tagName()
		WebElement results=driver.findElement(By.tagName("EditText"));
	//Check the calculated value on the edit box
		assert results.getText().equals("6"):"Actual value is : "+results.getText()+" did not match with expected value: 6";
	}
}
