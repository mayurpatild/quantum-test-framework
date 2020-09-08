package com.frmwrk.pages;

import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.frmwrk.base.BasePage;

/**
 * This is a sample page object class that is used for testing.
 * @author Mayur Patil
 *
 */
public class CalculatorPage extends BasePage {

	@FindBy(name="1") WebElement element1;
	@FindBy(name="2") WebElement element2;
	@FindBy(name="3") WebElement element3;
	@FindBy(name="4") WebElement element4;
	@FindBy(name="+") WebElement operPlus;
	@FindBy(name="-") WebElement operMinus;
	@FindBy(name="=") WebElement operEquals;
	@FindBys({
	    @FindBy(xpath = "//android.widget.Button")
	    })
	    List<WebElement> operClear;
	@FindBy(className="android.widget.EditText") WebElement Result;
	
	//@FindBy(name="btnK") WebElement btnKSearch;
	
	public CalculatorPage(RemoteWebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public void EnterNumber(String Number) {
		switch (Number) {
			case "1":
				element1.click();
				break;
			case "2":
				element2.click();
				break;
			case "3":
				element3.click();
				break;
			case "4":
				element4.click();
				break;
			}
    	tLog.info( Number + " has been clicked");
	}
	
	public void SelectOperation(String Operation) {
		switch (Operation) {
			case "+":
				operPlus.click();
				break;
			case "-":
				operMinus.click();
				break;
			case "=":
				operEquals.click();
				break;
			case "CLR":
				operClear.get(0).click();
			}
    	tLog.info( Operation + " has been initiated");	
	}
	
	public void VerifyResult(String result) {
		Assert.assertEquals(Result.getText().contains(result), true);
		tLog.info("Verified result is:" + Result.getText());
	}
}
