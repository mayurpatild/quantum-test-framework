
package com.frmwrk.test.steps;

import java.io.IOException;

import jxl.read.biff.BiffException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import com.frmwrk.base.TestBaseSetup;
import com.frmwrk.base.exceptions.DataSheetException;
import com.frmwrk.base.utils.DataSheet;
import com.frmwrk.pages.GoogleSearchPage;

import org.testng.Reporter;

/** 
 * This is sample glue/step definition class for reference. To be utilized for testing purpose only.
 * @author Mayur Patil
 *
 */

public class GoogleSearchStep {

	GoogleSearchPage GSP;

	@Given("user is on google search page")
	public void The_user_is_on_google_search_page() {        
		System.out.println("Launching URL..");
	      GSP = new GoogleSearchPage(TestBaseSetup.getDriver());
	      GSP.setURL("www.google.co.in");
	      Reporter.log("The_user_is_on_google_search_page ::--> Passed");
	}
	     
	@When("^enter \"([^\"]*)\" text into search field$")
	public void User_enters_text_into_field(String text) throws BiffException, DataSheetException, IOException {
		  String parmValue = DataSheet.readTestDataSheet(TestBaseSetup.testDataSheetPath, "TestData", TestBaseSetup.ScenarioNameDataSeqNo, text); 
		  GSP.GoogleSearch(parmValue,TestBaseSetup.getBrowserType());
	      Reporter.log("User_enters_text_into_field ::--> Passed");
	}
	
	
	@Then("^the browser title should have \"([^\"]*)\"$")
	public void theBrowserTitleShouldsrchString(String msg) throws BiffException, DataSheetException, IOException {
		String parmValue = DataSheet.readTestDataSheet(TestBaseSetup.testDataSheetPath, "TestData", TestBaseSetup.ScenarioNameDataSeqNo, msg);
		GSP.verifyTitle(parmValue);
		GSP.PageLoadWait(TestBaseSetup.timeOut);
		Reporter.log("theBrowserTitleShouldsrchString ::--> Passed");
	}

	
}
