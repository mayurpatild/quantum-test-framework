package com.frmwrk.test.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;
import com.frmwrk.pages.CalculatorPage;
import com.frmwrk.base.TestBaseSetup;
import org.testng.Reporter;

public class CalculatorSteps {

CalculatorPage CP;
 
@Given("^user opened calculator$")
public void user_opened_calculator() throws Throwable {
    // Write code here that turns the phrase above into concrete actions
    CP = new CalculatorPage(TestBaseSetup.getDriver());
    //CP.SelectOperation("CLR");
    Reporter.log("Given user opened calculator ::--> Completed");
}

@When("^user enters the number as \"([^\"]*)\"$")
public void user_enters_the_number_as(String arg1) throws Throwable {
    // Write code here that turns the phrase above into concrete actions
    CP.EnterNumber(arg1);
    Reporter.log("When user enters the number as " + arg1 + "::--> Completed");
}

@When("^user performs \"([^\"]*)\" operation$")
public void user_performs_operation(String arg1) throws Throwable {
    // Write code here that turns the phrase above into concrete actions
    CP.SelectOperation(arg1);
    Reporter.log("When user performs" + arg1 + "operation ::--> Completed");
}

@Then("^user should see the result as \"([^\"]*)\"$")
public void user_should_see_the_result_as(String arg1) throws Throwable {
    // Write code here that turns the phrase above into concrete actions
	CP.SelectOperation("=");
	CP.VerifyResult(arg1);
	Reporter.log("Then user should see the result as" + arg1 + "::--> Completed");
}

 
}
