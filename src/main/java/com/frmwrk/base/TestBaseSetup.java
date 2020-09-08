
package com.frmwrk.base;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
//import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.slf4j.Logger;
import org.testng.Assert;

import com.frmwrk.base.utils.TestLogger;

import cucumber.api.Scenario;

/**
 * <pre>
 * This is the test setup class which reads the different property files, sets the remote selenium
 * webdriver for launching the browser.
 * </pre>
 * @author Mayur Patil
 *
 */

public class TestBaseSetup {
	//private static WebDriver driver;
	private static RemoteWebDriver driver;
	//private RemoteWebDriver driver;
	public static int DataIteration;
	public static String ScenarioNameDataSeqNo; 
	public static String driverPath;
	static Logger tLog;
	public static String driverPathIE;
	public static String baseURL;
	public static String driverClassName;
	public static String databasePassword;
	public static String databaseServerIP;
	public static String databaseType;
	public static String databaseUserName;
	public static String testDataSource;
	public static String externalSheetPath;
	public static String sheetname;
	public static String webdriverServerHostName;
	public static String webdriverServerPortName;
	public static String webdriverUrl;
	public static String testDataSheetPath;
	public static String PHANTOM_JS_DRIVER_PATH;
	public static ResourceBundle globalProperties;
	public static ResourceBundle gridProperties;
	public static ResourceBundle DBProperties;
	public static String deviceName;
	public static String platformName;
    public static String platformVersion;
    public static String mobileBrowserName;
	public static String appPackage;
	public static String appActivity;
	private static boolean headless;
	public static int timeOut;

	/**
	 * This function is used for returning the RemoteWebDriver
	 * @return RemoteWebDriver
	 */
	public static RemoteWebDriver getDriver() {
		return driver;
	}
	
	/**
	 * <pre>
	 * This function is used for returning the data iteration count.
	 * The data iteration count is used by the scenarios in the before block for which data is provided
	 * using example tables in a feature file. The scenario name is appended with the data iteration count 
	 * to point to the particular excel row in the testdata.xls sheet for fetching the data for the iteration.
	 * </pre> 
	 * @return int - data iteration count in the input data excel sheet
	 */
	public static int getDataIeration() {
		return DataIteration;
	}

	/**
	 * This function is used for returning the browserType for headless testing
	 * @return Boolean - headless set to true for headless testing in the TestNG test
	 */	
	public static boolean getBrowserType() {
		return headless;
	}
	
	/**
	 * <pre>
	 * This function is used reading the global properties file which contain all the dynamic values that
	 * might change during the course of the automation script development
	 * </pre>
	 */	
	protected void getGlobalProperties() {

		globalProperties = ResourceBundle.getBundle("global");

		driverPath = System.getProperty("user.dir") + "/" + globalProperties.getString("webdriver_chrome_driver");
		driverPathIE = System.getProperty("user.dir") + "/" + globalProperties.getString("webdriver_ie_driver");
		PHANTOM_JS_DRIVER_PATH = System.getProperty("user.dir") + "/" + globalProperties.getString("phantomjs_binary_path");
		baseURL = globalProperties.getString("base_url");
		deviceName = globalProperties.getString("deviceName");
		platformName = globalProperties.getString("platformName");
		platformVersion = globalProperties.getString("platformVersion");
		mobileBrowserName = globalProperties.getString("mobileBrowserName");
		appPackage = globalProperties.getString("appPackage");
		appActivity = globalProperties.getString("appActivity");

		timeOut = Integer.parseInt(globalProperties.getString("time_out"));
		if (globalProperties.containsKey("test_data_source")) {
			testDataSource = globalProperties.getString("test_data_source");
		}
		if(testDataSource.equalsIgnoreCase("excel")){
			externalSheetPath = globalProperties.getString("external_sheet_path");
			sheetname = globalProperties.getString("sequence_flow_sheet");
			testDataSheetPath = globalProperties.getString("test_data_source_path");
			if(externalSheetPath.equals(""))
			{
				tLog.error("Please provide a valid sheet path");
				Assert.fail();
			}
		}
		else{
			tLog.error("Please provide a valid test data source value");
			Assert.fail();
		}
	}


	/**
	 * This function reads Grid properties for using Selenium grid for test execution
	 */
	protected void getGridProperties() {
		gridProperties = ResourceBundle.getBundle("grid");
		webdriverServerHostName = gridProperties.getString("webdriver_hostname");
		webdriverServerPortName = gridProperties.getString("webdriver_port");
	}
	
	/**
	 * This function reads DB Related properties which will be used for database connection
	 */
	protected void getDBProperties() {
		DBProperties= ResourceBundle.getBundle("db");
		databaseType=DBProperties.getString("database_type");
		databaseServerIP=DBProperties.getString("jdbc_url");
		driverClassName=DBProperties.getString("jdbc_driverClassName");
		databaseUserName=DBProperties.getString("database_username");
		databasePassword=DBProperties.getString("database_password");
	}

	/**
	 * <pre>
	 * This function is used for setting the Selenium webdriver based on the browsertype
	 * which is passed as a runtime parameter using maven command. For complete syntax for execution refer
	 * the POM.xml file. If no browsertype is provided then by default firefox driver is initiated
	 * </pre>
	 * @param String browsertype - chrome, ie, firefox and headless to initiate phantomjs driver
	 * @param RemoteServerURL which is normally in the form http://<selenium hub ip>:<selenium hub port>/wd/hub
	 */	
	private void setDriver(String browserType, URL remoteServerUrl) {
		if (driver == null) {
			switch (browserType) {
			case "chrome":
				driver = initChromeDriver(remoteServerUrl);
				break;
			case "firefox":
				driver = initFirefoxDriver(remoteServerUrl);
				break;
			case "ie":
				driver = initIEDriver(remoteServerUrl);
				break;
			case "headless":
				headless = true;
				driver = initPhantomJsDriver(remoteServerUrl);
				break;
			case "appium":
				driver = initAppiumDriver(browserType,remoteServerUrl);
				break;
			case "appiumApp":
				driver = initAppiumDriver(browserType,remoteServerUrl);
				break;
			default:
				System.out.println("browser : " + browserType
						+ " is invalid, Launching Firefox as browser of choice..");
				driver = initFirefoxDriver(remoteServerUrl);
				break;
			}
		}
	}

	/**
	 * <pre>
	 * This function is used for embedding the screenshot in the cucumber steps at the end of execution of an scenario
	 * so that it appears in the Cucumber.json report
	 * </pre>
	 * @param Scenario - cucumber scenario that is got executed
	 */		
	public static void embedScreenshot(final Scenario result) {
		try {
			final byte[] screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
			result.embed(screenshot, "image/png");
		} catch (final UnsupportedOperationException somePlatformsDontSupportScreenshots) {
			System.err.println(somePlatformsDontSupportScreenshots.getMessage());
		} catch (final WebDriverException e) {
			result.write("WARNING. Failed take screenshots with exception:" + e.getMessage());
		}
	}

	/**
	 * This function is used for invoking the chrome driver
	 * @param URL remoteServerUrl - which is normally in the form http://<selenium hub ip>:<selenium hub port>/wd/hub
	 */	
	private static RemoteWebDriver initChromeDriver(URL remoteServerUrl) {
		System.out.println("Launching google chrome with new profile..");
		System.setProperty("webdriver.chrome.driver", driverPath);
		DesiredCapabilities caps = DesiredCapabilities.chrome();
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.BROWSER, Level.SEVERE);
        caps.setCapability("browserName", "chrome");
        caps.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
        caps.setCapability("acceptSslCerts", "true");
		caps.setCapability("javascriptEnabled", "true");
        try {
        	driver = new RemoteWebDriver(remoteServerUrl, caps);
        	driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver.manage().window().maximize();
			tLog.info("Chromedriver launched");
		} catch (Exception e) {
        	e.printStackTrace();
			tLog.info("Chromeriver launched failed");
        }
		//driver.navigate().to(appURL);
		return driver;
	}

	/**
	 * This function is used for invoking the appium driver
	 * @param URL remoteServerUrl - which is normally in the form http://<selenium hub ip>:<selenium hub port>/wd/hub
	 */	
	private static RemoteWebDriver initAppiumDriver(String browserType,URL remoteServerUrl) {
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("deviceName", deviceName);
        caps.setCapability("platformName", platformName);
        caps.setCapability("platformVersion", platformVersion); 
		switch (browserType) {
			case "appium":
				System.out.println("Launching appium browser with new profile..");
				caps.setCapability("browserName", mobileBrowserName);
				break;
			case "appiumApp":
				System.out.println("Launching appium App with new profile..");
				caps.setCapability("appPackage", appPackage);
				caps.setCapability("appActivity",appActivity);
				break;
			}
		try {
			driver = new RemoteWebDriver(remoteServerUrl, caps);
			tLog.info("Appiumdriver launched");
			//driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(300, TimeUnit.SECONDS);
		} catch (Exception e) {
			 e.printStackTrace();
			 tLog.info("Appiumdriver launched failed"); 
		}
			return driver;
		}
		
	/**
	 * This function is used for invoking the ie driver
	 * @param URL remoteServerUrl - which is normally in the form http://<selenium hub ip>:<selenium hub port>/wd/hub
	 */		
	private static RemoteWebDriver initIEDriver(URL remoteServerUrl) {
		System.out.println("Launching IE with new profile..");
		System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+"/src/test/resources/IEDriverServer.exe");
		//System.setProperty("webdriver.ie.driver", "user.dir"+"src/test/resources/IEDriverServer.exe");
		DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.BROWSER, Level.SEVERE);
        caps.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
        caps.setCapability("browserName", "internet explorer");
		caps.setCapability("acceptSslCerts", "true");
		caps.setCapability("javascriptEnabled", "true");
		caps.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,	true);
		caps.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION,	true);
		System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+"/src/test/resources/IEDriverServer.exe");
		try {
			driver = new InternetExplorerDriver(caps);
			//driver = new RemoteWebDriver(remoteServerUrl, caps);
	        tLog.info("IEDriver launched");	
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        } catch (Exception e) {
        	e.printStackTrace();
			tLog.info("IEDriver launched failed");
        }
		//driver.navigate().to(appURL);
		return driver;
	}

	/**
	 * This function is used for invoking the firefox driver
	 * @param URL remoteServerUrl - which is normally in the form http://<selenium hub ip>:<selenium hub port>/wd/hub
	 */		
	private static RemoteWebDriver initFirefoxDriver(URL remoteServerUrl) {
		System.out.println("Launching Firefox browser..");
		DesiredCapabilities caps = DesiredCapabilities.firefox();
		caps.setCapability("browserName", "firefox");
		try {
			driver = new RemoteWebDriver(remoteServerUrl, caps);
			System.out.println(driver.getWindowHandle());
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			tLog.info("FirefoxDriver launched");
        } catch (Exception e) {
        	e.printStackTrace();
			tLog.info("FirefoxDriver launched failed");
        }
		return driver;
	}

	/**
	 * <pre>
	 * This function is used for invoking the phantomjs driver. This instantiates remotewebdriver 
	 * with phantomjs driver because there is an open issue where phantomjs driver is not getting
	 * registered with the selenium hub. Incase the issue gets solved then this function needs to 
	 * be invoked using the remoteserver url similar to chrome, firefox, ie etc.
	 * </pre>
	 * @param URL remoteServerUrl - which is normally in the form http://<phantomjs ip>:6666
	 */		
	private static RemoteWebDriver initPhantomJsDriver(URL remoteServerUrl) {
		System.out.println("Headless Testing with PhantomJS..");
		ArrayList<String> cliArgsCap = new ArrayList<String>();
        DesiredCapabilities caps = DesiredCapabilities.phantomjs();
        cliArgsCap.add("--webdriver=6666");
        cliArgsCap.add("--web-security=false");
        cliArgsCap.add("--ssl-protocol=any");
        cliArgsCap.add("--ignore-ssl-errors=true");
        caps.setJavascriptEnabled(true); 
        caps.setCapability("takesScreenshot", true);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,PHANTOM_JS_DRIVER_PATH);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_CLI_ARGS,new String[] { "--logLevel=DEBUG" });
	    try {
	    	driver = new PhantomJSDriver(caps);
	    } catch (UnreachableBrowserException e) {
	    	try {
	    		//e.printStackTrace();
	    		driver = new PhantomJSDriver(caps);
	    	} catch (UnreachableBrowserException e1) {
	    		e1.printStackTrace(); 
	    	}
	    }
		//driver.navigate().to(appURL);
		return driver;
	}
	
	//@Parameters({ "browserType", "appURL" })
	//@BeforeClass
	/**
	 * This function is used for initializing the browser using remote webdriver by calling the SetDriver function
	 * @param String browsertype ie, chrome, firefox, headless
	 * @param URL remoteServerUrl - which is normally in the form http://<selenium hub ip>:<selenium hub port>/wd/hub 
	 * except for phantomjs
	 */		
	public void initializeBrowser(String browserType, URL remoteServerUrl) {
		try {
			setDriver(browserType, remoteServerUrl);
		} catch (Exception e) {
			System.out.println("Error....." + e.getStackTrace());
		}
	}
	
	/**
	 * This is the constructor clss for TestBaseSetup which calls the different functions to read values from the
	 * properties file that can be utilized throughout the automation scripts
	 */
	public TestBaseSetup() {
		try {
			tLog = TestLogger.createLogger();
			getGridProperties();
			getGlobalProperties();
			getDBProperties();
		} catch (Exception e) {
			System.out.println("Error....." + e.getStackTrace());
		}
	}
	
	/**
	 * This method is used for creating the html log files
	 */	
	public void analyzeLog() {
        LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
        for (LogEntry entry : logEntries) {
            System.out.println(new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage());
            //do something useful with the data
        }
    }
	
//	@AfterClass
	/**
	 * This method is used for teardown activities once the testing is completed for the TestNG test class
	 */	
	public void tearDown() {
		//analyzeLog();
		//driver.quit();
	}

	/**
	 * <pre>
	 * This method is used generating the consolidated cucumber JSON report once the TestNG tests for all the
	 * features are completed provided in the Driverdata sheets. The consolidated report is generated in 
	 * /target/cucumber-report in the project root directory.
	 * </pre>
	 * @param int JsonCount - this is the number of tests that were provided in the driverdata sheet
	 */		
	//public void generateJsonReport(int JsonCount) {
	public void generateJsonReport() {
		File reportOutputDirectory = new File("target/cucumber-report");
		File reportDirectory = new File(System.getProperty("user.dir") + "/target");
		File[] files = reportDirectory.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.toLowerCase().endsWith(".json");
		    }
		});
		List<String> jsonFiles = new ArrayList<>();
		for (int iTestCnt=0;iTestCnt<files.length;iTestCnt++)
		{
			//jsonFiles.add(System.getProperty("user.dir") + "/target/cucumber-report" + (iTestCnt  + 1) + ".json");
			System.out.println(files[iTestCnt].getName());
			jsonFiles.add(System.getProperty("user.dir") + "/target/" + files[iTestCnt].getName());
		}
		String jenkinsBasePath = "";
		String buildNumber = "1";
		String projectName = "cucumber-jvm";
		boolean skippedFails = true;
		boolean pendingFails = false;
		boolean undefinedFails = true;
		boolean missingFails = true;
		boolean runWithJenkins = false;
		boolean parallelTesting = false;

		Configuration configuration = new Configuration(reportOutputDirectory, projectName);
		
		configuration.setStatusFlags(skippedFails, pendingFails, undefinedFails, missingFails);
		configuration.setParallelTesting(parallelTesting);
		configuration.setJenkinsBasePath(jenkinsBasePath);
		configuration.setRunWithJenkins(runWithJenkins);
		configuration.setBuildNumber(buildNumber);

		ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
		reportBuilder.generateReports();
	}
	
}
