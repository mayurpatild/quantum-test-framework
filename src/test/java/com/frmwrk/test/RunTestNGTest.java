package com.frmwrk.test;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import jxl.read.biff.BiffException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import cucumber.runtime.ClassFinder;
import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;
import org.testng.annotations.Listeners;
import atu.testng.reports.ATUReports;
import atu.testng.reports.listeners.ATUReportsListener;
import atu.testng.reports.listeners.ConfigurationListener;
import atu.testng.reports.listeners.MethodListener;
import atu.testng.reports.logging.LogAs;
import atu.testng.reports.utils.Utils;
import atu.testng.selenium.reports.CaptureScreen;
import atu.testng.selenium.reports.CaptureScreen.ScreenshotOf;
import com.frmwrk.base.TestBaseSetup;
import com.frmwrk.base.exceptions.DataSheetException;
import com.frmwrk.base.utils.DataSheet;

/**
 * <pre>
 * This class is a TestNG test class to invoke browser testing.
 * It is invoked from command line using Maven command as follows:
 * mvn test -DskipJasmine -DskipHeadless -Dbrowsertype=chrome -DdataSheetName=<DataSheet Name in the driver spreadsheet>
 * mvn test -DskipJasmine -DskipHeadless -Dbrowsertype=ie -DdataSheetName=<DataSheet Name in the driver spreadsheet>
 * mvn test -DskipJasmine -DskipHeadless -Dbrowsertype=firefox -DdataSheetName=<DataSheet Name in the driver spreadsheet>
 * 
 * Below are the details of the BDD Automation Framework
 * 
 * The framework is created using standard Maven project structure and below are the components.
 * -- The src/main/java folder contains the reusable functions and has 3 packages
 *    1) com.frmwrk.base - It has the following classes 
 *       - The BasePage class which contains the selenium reusable functions
 *       - The RestClientAPI class which contains basic functions for connecting to Jira and updating status in Jira
 *       - The TestBaseSetup class which is the main initialization class for running the TestNG test
 *    2) com.frmwrk.base.exceptions
 *       - This contains the 4 exception classes for appending customized exception message in the reports
 *    3) com.frmwrk.base.utils
 *       - This contains the utility functions for interacting with Excel sheet using jxl api
 *       - Database related functions like connection, disconnection and data fetch
 *       - The TestLogger class contains the slf4j log initialization 
 * 
 * -- The src/main/resources folder contains the log4j properties which can be customized based on need. There are 2 files,
 *    first is for html log file customization and second is for text log file customization. In the framework we are using
 *    html file for providing the log
 * 
 * -- The src/test/java contains the test related functions
 *    1) com.frmwrk.pages - It will contain the selenium page objects. The page objects extend the base page and the elements of 
 *      the pages are defined using {@literal @}FindBy annotation and initialized using the PageFactory notiation and sample is 
 *      given below
 *      	{@Literal @}FindBy(name="q") WebElement SrchText;
 *          {@literal @}FindBy(name="btnG") WebElement btnSearch;
 *          public GoogleSearchPage(RemoteWebDriver driver) {
 *          super(driver);
 *          PageFactory.initElements(driver, this);
 *          }
 *      Refer the GoogleSearchPage sample class
 *     2) com.frmwrk.test - This contains the cucucumber runner classes (browser and headless) and standalone TestNG test class
 *        
 *        For selenium webdriver tests (i.e.browser) option we are using selenium grid for execution. So before executing the tests, 
 *        the tester has to ensure that the selenium hub has been started. For that we have to download the selenium  standalone
 *        server jar corresponding to the version that is being used in the POM file for selenium and then  open command prompt and
 *        
 *        java -jar <Path>\selenium-server-standalone-2.44.0.jar -role hub -port 4447. For the nodes use the following commands
 *        
 *        java -jar selenium-server-standalone-2.44.0.jar -role node -hub http://localhost:4447/grid/register �port 5555
 *        
 *        Similarly for Chrome node update the browserName=chrome and for firefox update is to firefox
 *     3) com.frmwrk.test.steps - This class will contain the glue codes or step definitions and there is CommonMethods class
 *        which contains the hooks for Cucumber
 *-- The src/test/resources as the name suggests contains the test resources 
 *     1) Feature folder contains the BDD feature files and tester can create subfolders within that to organize the BDD Feature files
 *     2) InputData folder 
 *        - contains the DriverData sheet which contains the execution sequence. It has 3 columns Execution Status, FeatureName and 
 *          Tags. 
 *          Execution Status can be Y/N, where Y means execute the row and N means skip the row for execution and is a mandatory field.
 *          FeatureName - Name of the feature to be executed which is relative path from src as given below. It is a mandatory field.
 *                       src/test/resources/features/GoogleSearch.feature
 *          Tags are optional but if specified then tester has ensure the tag is specified in the FeatureFile being executed else runtime
 *          exception will occur. It can be left blank and it means all the scenarios withing the feature file have to be executed
 *          
 *          The Driversheet can contain multiple sheets for execution and we can provide the sheetname dynamically through Maven command
 *          line. If no sheetname is specified then the defaul sheetname present in the global properties file will be used for execution
 *          sequence.
 *        - The second sheet is the testdata sheet which contains ScenarioDataSequenceNo which corresponds to the data sequence given in
 *          the featurefile and samples are given below. The scenario name should follow this syntax given the sample below
 *          
 *          Scenario Outline: StoryID SmokeScenario
 * 				Given user is on google search page
 * 				When enter "<srchString>" text into search field
 * 				Then the browser title should have "<outString>"
 *
 * 			Examples: 
 * 				| srchString   | outString |
 * 				| Parm-1       | Parm-2    |
 * 				| Parm-1       | Parm-2    |
 * 	
 * 			Below is the example for providing data when embedded in the steps
 * 
 * 			Scenario: StoryID RegressionScenario
 *   			Given user is on google search page
 *   			When enter "Parm-1" text into search field
 *   			Then the browser title should have "Parm-2" 
 *         
 *         The data in the testdata sheet is provided as below
 *         
 *         ScenarioName-DataSequenceNo
 *			SmokeScenario1
 *			RegressionScenario1
 *			SmokeScenario2
 *         
 *         The ParmCount field in the test data sheet is used to provide the parameter count in the scenario.
 *         
 *         There are 20 parameters defined but more can be provided per scenario based on requirement.
 *     3) atu.properties - for providing ATU properties
 *     4) db.properties - for database connectivity related things
 *     5) global.properties - for global configurations
 *     6) grid.properties - for selenium grid host ip, port 
 *     7) The Chrome, IE and PhantomJs drivers
 *     8) ATU Reports Jar and logo which is used in the ATU reports
 *         
 * </pre>
 * @author Mayur Patil
 *
 */
	@Listeners({ ATUReportsListener.class, ConfigurationListener.class,	MethodListener.class })
	public class RunTestNGTest {
		
		{
			/** 
			 * This block is used for ATU report configurations
			 */
			System.setProperty("atu.reporter.config", System.getProperty("user.dir") + "/src/test/resources/atu.properties" );
					//"src/test/resources/atu.properties");
		}
		
		static int iTestCnt; 
		TestBaseSetup testsetup;

		/**
		 * <pre>
		 * This is the before class method of TestNG to initialization required for testing
		 * It sets the testcount to 1 and which is incremented based on the number of rows provided in the 
		 * dataprovider sheet
		 * </pre>
		 */
		@BeforeClass
		public void beforeclass() {
			iTestCnt = 1;
			testsetup = new TestBaseSetup();
			//System.out.println("Executed");
		}
		/**
		 * <pre>
		 * This is the TestNG test method which gets the feature name and the tags to be executed from the
		 * driverdata excel sheet and invokes the cucumber runtime.
		 * It sets the browser type based on the parameter value provided in the Maven command and the 
		 * remote server url for remote selenium driver which is http://<selenium hub ip>:<selenium hub port>/wd/hub
		 * It also sets the ATU reports
		 * </pre>
		 * @param featureName
		 * @param tags
		 * @throws MalformedURLException
		 */		
		@Test(dataProvider = "dataSheet")
		public void Execute_test(String featureName, String tags) throws MalformedURLException {
			testsetup.DataIteration=0;
			//String[] splittedTags;
			List<String> CucumberOpts = new ArrayList<String>(); 
			URL remoteServerUrl = new URL("http://localhost:4447/wd/hub");
			String browserType = "chrome";
			System.out.println("Executing Test for: " + browserType);
			testsetup.initializeBrowser(browserType,remoteServerUrl);
			ATUReports.setWebDriver(TestBaseSetup.getDriver());
			ATUReports.setAuthorInfo("Mayur Patil", Utils.getCurrentTime(), "1.0");
			//System.out.println(featureName);
			//System.out.println(tags);
			String timepattern = "yyyyMMdd-hhmmss";
			String timestamp = new SimpleDateFormat(timepattern).format(new Date());
			if (tags.isEmpty()) {
				CucumberOpts.add ("-p");
				//CucumberOpts.add("json:target/cucumber-report" + iTestCnt + ".json");
				CucumberOpts.add("json:target/cucumber-report-" + timestamp + ".json");
				CucumberOpts.add("--glue");
				CucumberOpts.add("com.frmwrk.test.steps");
				CucumberOpts.add(featureName);
			} else {
				CucumberOpts.add ("-p");
				//CucumberOpts.add("json:target/cucumber-report" + iTestCnt + ".json");
				CucumberOpts.add("json:target/cucumber-report-" + timestamp + ".json");
				CucumberOpts.add("--glue");
				CucumberOpts.add("com.frmwrk.test.steps");
				CucumberOpts.add(featureName);
				//splittedTags = tags.split(":");
				//if (splittedTags.length > 1) {
				//	for (int iTagsCnt = 0; iTagsCnt < splittedTags.length - 1;iTagsCnt++ ) {
				//		CucumberOpts.add("--tags");
				//		CucumberOpts.add(splittedTags[iTagsCnt]);
				//	}
				//} else {
					CucumberOpts.add("--tags");
					CucumberOpts.add(tags);
				//}
			}
			String[] argv = (String[]) CucumberOpts.toArray( new String[CucumberOpts.size()] );
			/*String[] argv = {//"-p", "html:target/cucumber-html-report" + iTestCnt, 
			                 "-p", "json:target/cucumber-report" + iTestCnt + ".json", 
			                 "--glue", "com.frmwrk.test.steps", 
			                 featureName
			                 ,"--tags", tags};*/
	 		RuntimeOptions runtimeOptions = new RuntimeOptions(new ArrayList<String>(asList(argv)));
	 		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	 		ResourceLoader resourceLoader = new MultiLoader(classLoader);
	 		ClassFinder classFinder = new ResourceLoaderClassFinder(resourceLoader,classLoader);
	 		Runtime runtime = new Runtime(resourceLoader, classFinder, classLoader, runtimeOptions);
	 		//runtime.printSummary();
	 		try {
				runtime.run();
				if (browserType!="appiumApp" || browserType!="appium" ) {
					ATUReports.add(featureName, LogAs.PASSED, new CaptureScreen(
						ScreenshotOf.BROWSER_PAGE));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 		iTestCnt++;
		}

		/**
		 * </pre>
		 * This method is used for providing the dataprovider for the TestNG test method by retrieving the
		 * datasheet path from the global properties and the sheetname from the maven command line parameters.
		 * If the datasheet name is empty then it uses the default one provided in the global properties file
		 * </pre>
		 * @return
		 * @throws BiffException
		 * @throws IOException
		 * @throws DataSheetException
		 */		
		@DataProvider(name = "dataSheet")
		public Object[][] getTestDataFromExcel() throws BiffException, IOException,
				 DataSheetException {

			Object[][] dataMap = (Object[][]) null;

			DataSheet dataSheetObj = new DataSheet();
			String dataSheetNM = "FeatureSequence";//System.getProperty("dataSheetName");
			if (dataSheetNM.isEmpty()) {
				dataMap = dataSheetObj.readFromDriverSheet(testsetup.externalSheetPath, testsetup.sheetname);
			} else {
				dataMap = dataSheetObj.readFromDriverSheet(System.getProperty("user.dir")+"/src/test/resources/InputData/DriverData.xls", dataSheetNM);
			}
			return dataMap;
		}		
		
		/**
		 * This is the afterclass method that performs the cleanup activities once the all the tests are completed
		 */
		@AfterClass
		public void afterclass() {
			//testsetup.generateJsonReport(iTestCnt);
			//testsetup.generateJsonReport();
			testsetup.tearDown();
		}

	}

