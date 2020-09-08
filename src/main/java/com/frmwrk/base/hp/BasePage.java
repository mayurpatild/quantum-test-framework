/*
package com.frmwrk.base.hp;

import java.awt.image.RenderedImage;
import com.hp.lft.report.ReportException;
import com.hp.lft.report.Reporter;
import com.hp.lft.report.Status;
import com.hp.lft.sdk.GeneralLeanFtException;
import com.hp.lft.sdk.web.Browser;

// for leanFT

public class BasePage {
	protected String URL;
	protected Browser browser;
	
	public BasePage(Browser browser) throws ReportException {
		  this.browser = browser; 
	}

	public void TakeSnapshot(String strStepName, String msg, Status status) throws ReportException, GeneralLeanFtException {
    	RenderedImage img = browser.getSnapshot();
    	Reporter.reportEvent(strStepName, msg, status, img);
	}
	
	public void LaunchPage(String appURL) throws InterruptedException {
		try {
			browser.navigate(appURL);
			browser.sync();
			System.out.println(browser.getTitle());
		}catch (GeneralLeanFtException e) {
			e.printStackTrace();
		}
	}
}

*/