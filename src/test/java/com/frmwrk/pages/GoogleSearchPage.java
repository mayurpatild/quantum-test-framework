package com.frmwrk.pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.frmwrk.base.BasePage;

/**
 * This is a sample page object class that is used for testing.
 * @author Mayur Patil
 *
 */
public class GoogleSearchPage extends BasePage {

	@FindBy(name="q") WebElement SrchText;
	@FindBy(name="btnG") WebElement btnSearch;
	//@FindBy(name="btnK") WebElement btnKSearch;
	
	public GoogleSearchPage(RemoteWebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public void GoogleSearch(String srchText,boolean headless) {
		type(SrchText,srchText);
		PageLoadWait(500);
		tLog.info("Entered the search text.");
		if (headless) {
			Actions action = new Actions(driver);
			action.sendKeys(Keys.RETURN);
			action.perform();
		} else {
			click(btnSearch);
		}
		tLog.info("Clicked search button.");
	}
	
	public void verifyTitle(String title) {
		Assert.assertEquals(getPageTitle().contains(title), true);
		tLog.info("Verified title");
	}

}
