/*
package com.frmwrk.base.hp;


import com.hp.lft.report.ModifiableReportConfiguration;
import com.hp.lft.report.ReportConfigurationFactory;
import com.hp.lft.report.ReportException;
import com.hp.lft.report.Reporter;
import com.hp.lft.report.Status;
import com.hp.lft.sdk.GeneralLeanFtException;
import com.hp.lft.sdk.ModifiableSDKConfiguration;
import com.hp.lft.sdk.SDK;
import com.hp.lft.sdk.web.Browser;
import com.hp.lft.sdk.web.BrowserFactory;
import com.hp.lft.sdk.web.BrowserType;


//For LeanFT

public class TestBaseSetup {
	
	private static Browser browser;
	
	public TestBaseSetup() {
        ModifiableSDKConfiguration objConfig;
		try {
			objConfig = new ModifiableSDKConfiguration();
			SDK.init(objConfig);

		} catch (Exception e) {
			e.printStackTrace();
		}
        
	}
	public Browser getBrowser() {
		return browser;
	}

	private void setDriver(String browserType) {
		if (browser == null) {
			switch (browserType) {
			case "CHROME":
				browser = initChromeDriver();
				break;
			case "FF":
				browser = initFirefoxDriver();
				break;
			case "IE":
				browser = initIEDriver();
				break;
			default:
				System.out.println("browser : " + browserType
						+ " is invalid, Launching Chrome as browser of choice..");
				browser = initChromeDriver();
			}
			try {
		        ModifiableReportConfiguration reportConfig = ReportConfigurationFactory.createDefaultReportConfiguration();
		        String workingDir = System.getProperty("user.dir");
		        reportConfig.setTargetDirectory(workingDir + "\\Target"); // The folder must exist under C:\
		        reportConfig.setReportFolder("LeanFTCucumberResults");
		        reportConfig.setTitle("Cucumber Test Execution Report");
		        reportConfig.setDescription("Cucumber Test Execution Report");
		        Reporter.init(reportConfig);
		        Reporter.startTest("Testing Started");
		        //System.out.println("Only Executed Once");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
	}
	
	private Browser initChromeDriver() {
		System.out.println("Launching google chrome with new profile..");
		try {
			browser = BrowserFactory.launch(BrowserType.CHROME);
			//browser.fullScreen();
			browser.resizeTo(1364, 728);
		} catch (GeneralLeanFtException e) {
			// TODO Auto-generated catch block
			try {
				Reporter.reportEvent("initChromeDriver","Chrome browser launch error",Status.Failed, e);
			} catch (ReportException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return browser;
	}

	private Browser initFirefoxDriver() {
		System.out.println("Launching Firefox browser..");
		try {
			browser = BrowserFactory.launch(BrowserType.FIREFOX);
			browser.fullScreen();
		} catch (GeneralLeanFtException e) {
			// TODO Auto-generated catch block
			try {
				Reporter.reportEvent("initFirefoxDriver","Firefox browser launch error",Status.Failed, e);
			} catch (ReportException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return browser;
	}
	
	private Browser initIEDriver() {
		System.out.println("Launching IE browser..");
		try {
			browser = BrowserFactory.launch(BrowserType.INTERNET_EXPLORER);
			browser.fullScreen();
		} catch (GeneralLeanFtException e) {
			// TODO Auto-generated catch block
			try {
				Reporter.reportEvent("initIEDriver","IE browser launch error",Status.Failed, e);
			} catch (ReportException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return browser;
	}
	//@Parameters({ "browserType", "appURL" })
	//@BeforeClass
	public void initializeTestBaseSetup(String browserType) {
		try {
			setDriver(browserType);
		} catch (Exception e) {
			System.out.println("Error....." + e.getStackTrace());
		}
	}
	
	public void CloseTest() throws InterruptedException, ReportException{
	   	try {
	   			browser.close();
			} catch (GeneralLeanFtException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	//the following ends the test node in the report
	   	    Reporter.reportEvent("Ending Test", "All scenarios executed");
	        Reporter.generateReport();
	        SDK.cleanup();
	 }
	

}
*/