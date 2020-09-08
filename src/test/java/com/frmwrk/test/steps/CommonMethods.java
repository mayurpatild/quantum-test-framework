package com.frmwrk.test.steps;

import java.net.MalformedURLException;
import java.util.Collection;
//import java.util.List;
import org.testng.Reporter;
import com.frmwrk.base.TestBaseSetup;
//import cucumber.api.PendingException;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
//import cucumber.api.java.en.Given;

/**
 * This class is for defining the common methods that would be used for Cucumber scenario execution
 * @author Mayur Patil
 *
 */
public class CommonMethods {
	Scenario scenario; 
	TestBaseSetup testsetup;
	
	/**
	 * This is called before the execution of any scenario and captures the scenario details in the Scenario parameter
	 * which can be used later for any obtaining any scenario related information.
	 * It also increments the data iteration count based on the example table row getting executed.
	 * The scenario name should be given in the following format <StoryID> <ScenarioName>
	 * The parameters in the example table should be provided as below
	 * 
	 * Below is the example for data provided in example tables
	 * <pre>
	 * Scenario Outline: StoryID SmokeScenario
     * Given user is on google search page
     * When enter "<srchString>" text into search field
     * Then the browser title should have "<outString>"
	 *
     * Examples: 
     * | srchString   | outString |
     * | Parm-1       | Parm-2    |
     * | Parm-1       | Parm-2    |
     * 	
     * Below is the example for providing data when embedded in the steps
     * 
     * Scenario: StoryID RegressionScenario
     *   Given user is on google search page
     *   When enter "Parm-1" text into search field
     *   Then the browser title should have "Parm-2"
     * </pre>
     * 
	 * @param scenario
	 * @throws MalformedURLException
	 */
	@Before
	public void setup(Scenario scenario) throws MalformedURLException {
		this.scenario = scenario;
		//testsetup = new TestBaseSetup();
		TestBaseSetup.DataIteration++;
		String[] splited = scenario.getName().split("\\s+");
		TestBaseSetup.ScenarioNameDataSeqNo = splited[1] + TestBaseSetup.getDataIeration();
		Reporter.log("Setup method");
	}

	/**
	 * This is Before block for manual step and just 
	 * @param scenario
	 * @throws MalformedURLException
	 */
	@Before("@Manual")
	public void setupManual(Scenario scenario) throws MalformedURLException {
		this.scenario = scenario;
		String[] splited = scenario.getName().split("\\s+");
		TestBaseSetup.ScenarioNameDataSeqNo = splited[1];
		Reporter.log("@ManualStep Ignored");
	}	
	
	/**
	 * After block to perform the cleanup activities after Manual scenario
	 */
	@After("@Manual")
	public void teardownManual () {
		Reporter.log("Setup method ::--> Passed");
	}

	//@Given("ManualTestScenario")
	//public void ManualTestScenario() {                      
	//      Reporter.log("This is Manual Test Scenario which will not be executed ::--> Passed");
	//      throw new PendingException();
	//      }
	//
    /**
     * After block to perform the cleanup activities after scenario execution
     */
	@After
	public void teardown () {
		atScenarioEnd();
		Reporter.log("Teardown ::--> Passed");
	}

	/**
	 * This is invoked at the scenario end to updated details in the Jira for execution status. 
	 * This function needs to be modified based on the actual need
	 */
	@SuppressWarnings("unused")
	public void atScenarioEnd() {
		//if (scenario.isFailed()) {
		Collection<String> tagsCollection = scenario.getSourceTagNames();
		String tagsList = tagsCollection.toString();
		if (!tagsList.toLowerCase().contains("manual")) {
			TestBaseSetup.embedScreenshot(scenario);
		}
		//else {
		//	scenario.write("Pending Scenario");
		//}
		//}
		String[] splited = scenario.getName().split("\\s+");
		String strIssueID = splited[0];
		String strComment = "Execution status for the story Id: " + strIssueID + "and Scenario:" + splited[1] +  ":::" + scenario.getStatus();
		//RestClientAPI.RestAddComment(strComment, strIssueID);
	}

}
