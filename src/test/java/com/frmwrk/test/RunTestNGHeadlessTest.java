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
//import org.testng.annotations.BeforeMethod;
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
 * This class is a TestNG test class to invoke headless testing.
 * It is invoked from command line using Maven command as follows:
 * mvn test -DskipJasmine -DskipBrowser -Dbrowsertype=headless -DdataSheetName=<DataSheet Name in the driver spreadsheet>
 * 
 * For running headless test the selenium grid server is not required to be started.
 * </pre>
 * @author Mayur Patil
 *
 */
	@Listeners({ ATUReportsListener.class, ConfigurationListener.class,	MethodListener.class })
	public class RunTestNGHeadlessTest {
		
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
		 * This is the before class method of TestNG to initialization required for testing
		 * It sets the testcount to 1 and which is incremented based on the number of rows provided in the 
		 * dataprovider sheet
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
		 * remote server url for phantomjs which is http://<driver host ip>:6666
		 * It also sets the ATU reports
		 * </pre>
		 * @param featureName
		 * @param tags
		 * @throws MalformedURLException
		 */
		@Test(dataProvider = "dataSheet")
		public void Execute_test(String featureName, String tags) throws MalformedURLException {
			testsetup.DataIteration=0;
			List<String> CucumberOpts = new ArrayList<String>(); 
			String browserType = System.getProperty("browsertype");
			System.out.println("Executed Headless test");
			System.out.println(browserType);
			URL remoteServerUrl = new URL("http://" + testsetup.webdriverServerHostName	+ ":" + "6666");
			testsetup.initializeBrowser(browserType,remoteServerUrl);
			ATUReports.setWebDriver(TestBaseSetup.getDriver());
			ATUReports.setAuthorInfo("Mayur Patil", Utils.getCurrentTime(), "1.0");
			String timepattern = "yyyyMMdd-hhmmss";
			String timestamp = new SimpleDateFormat(timepattern).format(new Date());
			if (tags.isEmpty()) {
				CucumberOpts.add ("-p");
				CucumberOpts.add("json:target/cucumber-report-" + timestamp + ".json");
				CucumberOpts.add("--glue");
				CucumberOpts.add("com.frmwrk.test.steps");
				CucumberOpts.add(featureName);
			} else {
				CucumberOpts.add ("-p");
				CucumberOpts.add("json:target/cucumber-report-" + timestamp + ".json");
				CucumberOpts.add("--glue");
				CucumberOpts.add("com.frmwrk.test.steps");
				CucumberOpts.add(featureName);
				CucumberOpts.add("--tags");
				CucumberOpts.add(tags);
			}
			String[] argv = (String[]) CucumberOpts.toArray( new String[CucumberOpts.size()] );
	 		RuntimeOptions runtimeOptions = new RuntimeOptions(new ArrayList<String>(asList(argv)));
	 		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	 		ResourceLoader resourceLoader = new MultiLoader(classLoader);
	 		ClassFinder classFinder = new ResourceLoaderClassFinder(resourceLoader,classLoader);
	 		Runtime runtime = new Runtime(resourceLoader, classFinder, classLoader, runtimeOptions);
	 		try {
				runtime.run();
				ATUReports.add("Smoke_test", LogAs.PASSED, new CaptureScreen(
						ScreenshotOf.BROWSER_PAGE));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 		iTestCnt++;
		}

		/**
		 * <pre>
		 * This method is used for providing the dataprovider for the TestNG test method by retrieving the
		 * datasheet path from the global properties and the sheetname from the maven command line parameters.
		 * If the datasheet name is empty then it uses the default one provided in the global properties file.
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
			String dataSheetNM = System.getProperty("dataSheetName");
			if (dataSheetNM.isEmpty()) {
				dataMap = dataSheetObj.readFromDriverSheet(testsetup.externalSheetPath, testsetup.sheetname);
			} else {
				dataMap = dataSheetObj.readFromDriverSheet(testsetup.externalSheetPath, dataSheetNM);
			}
			//System.out.println("Data Provider Executed");
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
