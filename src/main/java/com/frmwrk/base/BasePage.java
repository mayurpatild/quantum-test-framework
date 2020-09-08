package com.frmwrk.base;

//import java.util.Set;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.InvalidCookieDomainException;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.UnsupportedCommandException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import com.frmwrk.base.utils.TestLogger;

/**
 * <pre>
 * This is the base page class which contains the standard functions that would be utilized for selenium events.
 * Any new page for which automation script needs to be developed needs to extend this class using the syntax
 * given below
 * 
 * public class <PageObjectClass> extends BasePage
 * 
 * </pre>
 * @author Mayur Patil
 *
 */
public class BasePage {
	  /** Default URL */
	  protected String URL;	
	  protected JavascriptExecutor jse; 
	  
	  boolean[] boolShift = new boolean[30];
	  
	  /** This page's WebDriver */ 
	  protected RemoteWebDriver driver; 
	  
	  /**This defines the log which will be instantiated for every page using the TestLogger.createLogger() 
	   * in the constructor class */
	  protected static Logger  tLog;
	  
	  /** Expected Page Title.  This will be used in isPageLoad() 
	   * to check if page is loaded. */
	 protected String pageTitle; 
	 
	 /** Eunmerator for manipulating the locator type in selenium */
	 protected enum BY_TYPE {BY_XPATH, BY_LINKTEXT, BY_ID, BY_CLASSNAME,
			BY_NAME, BY_CSSSELECTOR, BY_PARTIALLINKTEXT, BY_TAGNAME };	 

	@SuppressWarnings("static-access")
	public BasePage(RemoteWebDriver driver) {
		  this.driver = driver; 
		  this.jse = (JavascriptExecutor)driver;
		  this.tLog = TestLogger.createLogger();
	}
	 
	 /**
	   * This function emulates the click function using the Java Script executor
	   * @param WebElement
	   * @param Element Id as String
	   */
	public void JSclick(WebElement wbElement, String element) {
		try {
		jse.executeScript("scroll(0, arguments[0])", wbElement.getLocation().y + 10);
		Thread.sleep(1000);
		String script = "var elem = document.getElementById('" + element + "');"+
				 "if( document.createEvent) {"+
				 "var evObj = document.createEvent('MouseEvents');"+
				 "evObj.initEvent( 'mouseover', true, false );"+
				 "elem.dispatchEvent(evObj);"+
				 "} else if( document.createEventObject ) {"+
				 "elem.fireEvent('onmouseover');" +
				 " } elem.click()";
		jse.executeScript(script);
		synchronized(driver) {
		driver.wait(3000);
			}
		} catch (Exception e) {
			tLog.info("Timeout while doing Java Script click for element " + element, e);
		}
	}		  

	protected By getLocator(String locator, BY_TYPE type){
		
		switch (type) {
		case BY_XPATH:
			return By.xpath(locator);
		case BY_LINKTEXT:
			return By.linkText(locator);
		case BY_ID:
			return By.id(locator);
		case BY_CSSSELECTOR:
			return By.cssSelector(locator);
		case BY_CLASSNAME:
			return By.className(locator);
		case BY_NAME:
			return By.name(locator);
		case BY_PARTIALLINKTEXT:
			return By.partialLinkText(locator);
		case BY_TAGNAME:
			return By.tagName(locator);
		
		}
		throw new IllegalArgumentException("Invalid By Type, Please provide correct locator type");
		
	}
	
	/** 
	   * This function emulates the click function using the WebElement and waits for 5000 miliseconds 
	   * @param WebElement
	   * 
	*/
	public void click( WebElement element ) {
		try {
			element.click();
			synchronized(driver) {
				driver.wait(5000);
			}
		} catch (NoSuchElementException | InterruptedException e) {
			tLog.info ("NoSuchElementException for element " , e);
		}
	}

	 /** 
	   * This function uses Java Script executor to bring the specified WebElement to the screen viewport
	   * so that it is visible before any action can be performed on the WebElement  
	   * @param WebElement
	   * @param Element Id as String
	   */
	 public void scrollPage(WebElement wbElement) {
		 jse.executeScript("scroll(0, arguments[0])",wbElement.getLocation().y);
	 }

	 /** 
	   * This function verifies the successful page load by using pagetitle
	   * @return true or false  
	   */
	 public boolean isPageLoad(){
		  return (driver.getTitle().contains(pageTitle)); 
	  }
	  
	  /** 
	   * Open the default page using the URL supplied as part of page object initialization 
	   * 
	   */ 
	  public void open(){
		  System.out.println(URL);
		  try {
			  driver.get(URL); 
		  } catch (UnreachableBrowserException e) {
			  tLog.error("Unable to load the Base URL: ",e.fillInStackTrace());
	  throw new UnreachableBrowserException("Unable to load the Base URL: "+ URL);
		  }
		  driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	  }
	  
	/**
	 * This function is to navigate the browser to a URL
	 * 
	 * @param URL
	 * 			- URL to which browser has to be navigated
	 */
	public void navigateToURL(String URL)
	{
		try
		{
			driver.navigate().to(URL);
		}
		catch(UnreachableBrowserException e)
		{
			tLog.error("Unable to launch the URL: "+URL);
			throw new UnreachableBrowserException("Unable to launch the URL: "+URL);
		}
	}	  
	  
 
	  /** Returns the default URL 
	   * @return String url
	   */ 
	  public String getURL() {
		return URL;
	  }
	  
	  /** Returns the sets the default URL 
	   *@param String URL to set
	   */
	  public void setURL(String URL) {
	       this.URL = URL;
	  }

	/**
	 * This function returns the Current Window URL
	 * 
	 * @return String 
	 * 			- returns the Current Window URL
	 */
	public String getCurrentURL() 
	{
		tLog.info("The current Browser URL is returned");
		return driver.getCurrentUrl();
	}
	
	
	/**
	 * This function returns the Current Window Title
	 * 
	 * @return String  
	 * 			- returns the Current Window Title
	 */
	public String getPageTitle() 
	{
		tLog.info("The Current Window title is " + driver.getTitle());
		return driver.getTitle();
	}
	
	
	/**
	 * This function checks whether the Current Window URL is same as the
	 * Expected
	 * 
	 * @param expectedURL
	 *           	 - URL expected in the Current Window
	 * @return boolean 
	 * 				- returns true if the CurrentWindow URL matches the
	 *         		  expectedURL, else returns false
	 */
	public boolean isURLAsExpected(String expectedURL) 
	{
		tLog.info("The Current URL:" + getCurrentURL() + "; Expected URL:"
				+ expectedURL);
		return expectedURL.equals(getCurrentURL());
	}	  

	/**
	 * This function is to load the previous URL in the browser history.
	 * 
	 */
	public void navigateBack() 
	{
		try 
		{
			driver.navigate().back();
			tLog.info("The page is navigated backwards");
		} 
		catch (WebDriverException e) 
		{
			tLog.error("The page cannot be navigated backward",e.fillInStackTrace());
			throw new WebDriverException("The page cannot be navigated backward");
		}
	}

	
	/**
	 * This function loads the URL which is forward in the browser's history.
	 * Does nothing if we are on the latest page viewed.
	 * 
	 */
	public void navigateForward() 
	{
		try 
		{
			driver.navigate().forward();
			tLog.info("The page is navigated forward");
		} 
		catch (UnreachableBrowserException e) 
		{
			tLog.error("The page cannot be navigated forward",e.fillInStackTrace());
			throw new UnreachableBrowserException("The page cannot be navigated forward");
		}
	}

	
	/**
	 * This function refresh the current page
	 * 
	 */
	public void refreshPage() 
	{
		try 
		{
			driver.navigate().refresh();
			tLog.info("The page is refreshed");
		} 
		catch (UnreachableBrowserException e) 
		{
			tLog.error("The page cannot be refreshed",e.fillInStackTrace());
			throw new UnreachableBrowserException("The page cannot be refreshed");
		}
	}	

	/**
	 * This function is to make the driver wait explicitly for a condition to be
	 * satisfied
	 * 
	 * @param locator
	 *            - By object of the element whose visibility/presence/clickability has to be checked
	 */
	public void addExplicitWait(By locator, String condition, int inttimeoutinseconds )
	{

		WebDriverWait webDriverWait = new WebDriverWait(driver, inttimeoutinseconds, 250L);
		try
		{
			if(condition.equalsIgnoreCase("visibility"))
			{
				webDriverWait.until(ExpectedConditions.visibilityOf(driver.findElement(locator)));
				tLog.info("Driver waits explicitly until the element with"+locator.
						toString().replace("By."," ")+" is visible");
			}
			else if(condition.equalsIgnoreCase("clickable"))
			{
				webDriverWait.until(ExpectedConditions.elementToBeClickable(driver.findElement(locator)));
				tLog.info("Driver waits explicitly until the element with"+locator.
						toString().replace("By."," ")+" is clickable");
			}
			else if(condition.equalsIgnoreCase("presence"))
			{
				webDriverWait.until(ExpectedConditions.presenceOfElementLocated(locator));
				tLog.info("Driver waits explicitly until the element with"+locator.
						toString().replace("By."," ")+" is present");
			}
			else
				tLog.error("Condition String should be visibility or clickable or presence");
		}
		catch(NoSuchElementException e)
		{
			tLog.error("The element with"
					+ locator.toString().replace("By.", " ")
					+ " not found",e.fillInStackTrace());
			throw new NoSuchElementException("The element with"
					+ locator.toString().replace("By.", " ")
					+ " not found");
		}
		catch(UnsupportedCommandException e)
		{
			tLog.error("The condition given to check the elemnt with"
					+ locator.toString().replace("By.", " ")
					+ " is invalid",e.fillInStackTrace());
			throw new NoSuchElementException("The condition given to check the elemnt with"
					+ locator.toString().replace("By.", " ")
					+ " is invalid",e.fillInStackTrace());
		}
	}

	/**
	 * This function is to make the driver wait explicitly for a condition to be
	 * satisfied
	 * 
	 * @param locator
	 *            - WebElement object of the element whose visibility/clickability has to be checked
	 */
	public void addExplicitWait(WebElement locator, String condition, int inttimeoutinseconds )
	{

		WebDriverWait webDriverWait = new WebDriverWait(driver, inttimeoutinseconds, 250L);
		try
		{
			if(condition.equalsIgnoreCase("visibility"))
			{
				webDriverWait.until(ExpectedConditions.visibilityOf(locator));
				tLog.info("Driver waits explicitly until the element with"+locator.
						toString()+" is visible");
			}
			else if(condition.equalsIgnoreCase("clickable"))
			{
				webDriverWait.until(ExpectedConditions.elementToBeClickable(locator));
				tLog.info("Driver waits explicitly until the element with"+locator.
						toString()+" is clickable");
			}
			else
				tLog.error("Condition String should be visibility or clickable or presence");
		}
		catch(NoSuchElementException e)
		{
			tLog.error("The element with"
					+ locator.toString().replace("By.", " ")
					+ " not found",e.fillInStackTrace());
			throw new NoSuchElementException("The element with"
					+ locator.toString().replace("By.", " ")
					+ " not found");
		}
		catch(UnsupportedCommandException e)
		{
			tLog.error("The condition given to check the elemnt with"
					+ locator.toString().replace("By.", " ")
					+ " is invalid",e.fillInStackTrace());
			throw new NoSuchElementException("The condition given to check the elemnt with"
					+ locator.toString().replace("By.", " ")
					+ " is invalid",e.fillInStackTrace());
		}
	}


	/**
	 * This function clicks on the element which can be located by the By Object
	 * 
	 * @param locator
	 *            - By object to locate the element to be clicked
	 */
	public void click(By locator)  
	{
		try
		{
			driver.findElement(locator).click();
			tLog.info("The element with"
					+ locator.toString().replace("By.", " ")
					+ " is clicked");
		}
		catch(NoSuchElementException e)
		{
			tLog.error("The element with"
					+ locator.toString().replace("By.", " ")
					+ " not found",e.fillInStackTrace());
			throw new NoSuchElementException("The element with"
					+ locator.toString().replace("By.", " ")
					+ " not found");
		}
	}
	
	  /**
	   * Verifies the presence of a text present in page.
	   * @param String text to search
	   * @return true or false 
	  */ 
	  public boolean isTextPresent(String text){
		  return driver.getPageSource().contains(text); 
	  }

	/**
	 * This function is to get the visible text of an element within UI
	 * 
	 * @param locator
	 *            - By object to locate the element from which the text has to
	 *            be taken
	 * @return String - returns the innertext of the specified element
	 */
	public String getText(By locator) 
	{
		String text = null;
		try 
		{
			tLog.info("The value on the field with"
					+ locator.toString().replace("By.", " ")
					+ " is obtained");
			text = driver.findElement(locator).getText();
		} 
		catch (NoSuchElementException e)
		{
			tLog.error("Unable to get the text. The element with"
					+ locator.toString().replace("By.", " ")
					+ " not found",e.fillInStackTrace());
			throw new NoSuchElementException("Unable to get the text. The element with"
					+ locator.toString().replace("By.", " ")
					+ " not found");
		}
		return text;
	}	  

	/**
	 * This function is to get the visible text of an element within UI
	 * 
	 * @param locator
	 *            - WebElement object to locate the element from which the text has to
	 *            be taken
	 * @return String - returns the innertext of the specified element
	 */
	public String getText(WebElement locator) 
	{
		String text = null;
		try 
		{
			tLog.info("The value on the field with"
					+ locator.toString()
					+ " is obtained");
			text = locator.getText();
		} 
		catch (NoSuchElementException e)
		{
			tLog.error("Unable to get the text. The element with"
					+ locator.toString()
					+ " not found",e.fillInStackTrace());
			throw new NoSuchElementException("Unable to get the text. The element with"
					+ locator.toString()
					+ " not found");
		}
		return text;
	}	
	
	  /**  
	   * Verifies if an element is present or not  
	   * using the By 
	   * @param by - selector to find the element
	   * @return true or false
	   */
	  public boolean isElementPresent(By by) {
			try {
				driver.findElement(by);//if it does not find the element throw NoSuchElementException, thus returns false. 
				return true;
			} catch (NoSuchElementException e) {
				tLog.info("NoSuchElementException for element ", e);
				return false;
			}
	  }

	  
	  /** 
	   * Is the Element present in the DOM. 
	   * 
	   * @param _cssSelector 		element locater
	   * @return					WebElement
	   */
	  public boolean isElementPresent(String _cssSelector){
			try {
				driver.findElement(By.cssSelector(_cssSelector));
				return true;
			} catch (NoSuchElementException e) {
				tLog.info("NoSuchElementException for element " + _cssSelector, e);
				return false;
			}
	  }
	  

	  /**
		* Checks if the elment is in the DOM and displayed. 
		* 
		* @param by - selector to find the element
		* @return true or false
		*/
	  public boolean isElementPresentAndDisplay(By by) {
			try {			
				return driver.findElement(by).isDisplayed();
			} catch (NoSuchElementException e) {
				tLog.info("NoSuchElementException for element " + by.toString(), e);
				return false;
			}
	  }
	  
	  /** 
	   * Returns the first WebElement using the given method.  	   
	   * It shortens "driver.findElement(By)". 
	   * @param by 		element locater. 
	   * @return 		the first WebElement
	   */
	  public WebElement getWebElement(By by){
		  	return driver.findElement(by); 			
	  }	  
	  
	  /** 
	   * Selects a drop down by using its value  	   
	   * 
	   * @param WebElement Element to be selected. 
	   * @param String value to be set in the dropdown
	   */
		public void selectDropDownByValue(WebElement wbElement, String value) {
		    // Write code here that turns the phrase above into concrete actions
			try {
			  Select selectVal = new Select(wbElement);
			  selectVal.selectByVisibleText(value);
			  synchronized(driver) {
				  driver.wait(500);
			  }
			} catch (NoSuchElementException | InterruptedException e){
				tLog.info("Element not found exception for " + wbElement.toString() + value, e);
			}
		}
		
	/**
	 * This function is to select a dropdown option using its index
	 * 
	 * @param locator
	 *            - By object to locate the dropdown which is to be selected
	 * @param index
	 *            - index of the dropdown option to be selected
	 */
	public void selectDropDownByIndex(By locator, int index)
	{
		try 
		{
			Select dropDown = new Select(driver.findElement(locator));
			dropDown.selectByIndex(index);
			tLog.info("The dropdown option with index " + index
					+ " is selected");
		} 
		catch (NoSuchElementException e)
		{
			tLog.error("Unable to select the dropdown; The element with"
					+ locator.toString().replace("By.", " ")
					+ " not found",e.fillInStackTrace());
			throw new NoSuchElementException("The element with"
					+ locator.toString().replace("By.", " ")
					+ " not found");
		}
	}
	
	/**
	 * This function is to select a dropdown option using its index
	 * 
	 * @param locator
	 *            - WebElement object to locate the dropdown which is to be selected
	 * @param index
	 *            - index of the dropdown option to be selected
	 */
	public void selectDropDownByIndex(WebElement locator, int index)
	{
		try 
		{
			Select dropDown = new Select(locator);
			dropDown.selectByIndex(index);
			tLog.info("The dropdown option with index " + index
					+ " is selected");
		} 
		catch (NoSuchElementException e)
		{
			tLog.error("Unable to select the dropdown; The element with"
					+ locator.toString()
					+ " not found",e.fillInStackTrace());
			throw new NoSuchElementException("The element with"
					+ locator.toString()
					+ " not found");
		}
	}
		
	/**
	 * This function is to select the dropdown options that have a value
	 * matching the argument
	 * 
	 * @param locator
	 *            - By object to locate the dropdown which is to be selected
	 * @param value
	 *            - value to match against the dropdown option to be selected
	 */
	public void selectDropDownByValue(By locator, String value)
	{
		try 
		{
			Select dropDown = new Select(driver.findElement(locator));
			dropDown.selectByValue(value);
			tLog.info("The dropdown option with value " + value
					+ " is selected");
		} 
		catch (NoSuchElementException e) 
		{
			tLog.error("Unable to select the dropdown; The element with"
					+ locator.toString().replace("By.", " ")
					+ " not found",e.fillInStackTrace());
			throw new NoSuchElementException("The element with"
					+ locator.toString().replace("By.", " ")
					+ " not found");
		}
	}
	
	
	/**
	 * This function is to select the dropdown options that displays text
	 * matching the argument
	 * 
	 * @param locator
	 *            - By object to locate the dropdown which is to be selected
	 * @param visibleText
	 *            - visible text to match against the dropdown option to be
	 *            selected
	 */
	public void selectDropDownByVisibleText(By locator, String visibleText) 
	{
		try
		{
			Select dropDown = new Select(driver.findElement(locator));
			dropDown.selectByVisibleText(visibleText);
			tLog.info("The dropdown option with text " + visibleText
					+ " is selected");
		} 
		catch (NoSuchElementException e)
		{
			tLog.error("Unable to select the dropdown; The element with"
					+ locator.toString().replace("By.", " ")
					+ " not found",e.fillInStackTrace());
			throw new NoSuchElementException("The element with"
					+ locator.toString().replace("By.", " ")
					+ " not found");
		}
	}
	
	/**
	 * This function is to select the dropdown options that displays text
	 * matching the argument
	 * 
	 * @param locator
	 *            - WebElement object to locate the dropdown which is to be selected
	 * @param visibleText
	 *            - visible text to match against the dropdown option to be
	 *            selected
	 */
	public void selectDropDownByVisibleText(WebElement locator, String visibleText) 
	{
		try
		{
			Select dropDown = new Select(locator);
			dropDown.selectByVisibleText(visibleText);
			tLog.info("The dropdown option with text " + visibleText
					+ " is selected");
		} 
		catch (NoSuchElementException e)
		{
			tLog.error("Unable to select the dropdown; The element with"
					+ locator.toString()
					+ " not found",e.fillInStackTrace());
			throw new NoSuchElementException("The element with"
					+ locator.toString()
					+ " not found");
		}
	}
		
	/**
	 * This function is to move the mouse pointer to the specified location
	 * 
	 * @param locator
	 *            - By object to locate the element to which mouse pointer has
	 *            to be moved
	 */
	public void mouseOver(By locator) 
	{
		try
		{
			new Actions(driver).moveToElement(driver.findElement(locator))
			.build().perform();
			tLog.info("Mouse hover is performed on element with"
					+ locator.toString().replace("By.", " "));
		} 
		catch (NoSuchElementException e)
		{
			tLog.error("Unable to perform MouseOver; The element with"
					+ locator.toString().replace("By.", " ")
					+ " not found",e.fillInStackTrace());
			throw new NoSuchElementException("The element with"
					+ locator.toString().replace("By.", " ")
					+ " not found");
		}
	} 
	
	/**
	 * This function is to move the mouse pointer to the specified location
	 * 
	 * @param locator
	 *            - WebElement object to locate the element to which mouse pointer has
	 *            to be moved
	 */
	public void mouseOver(WebElement locator) 
	{
		try
		{
			new Actions(driver).moveToElement(locator)
			.build().perform();
			tLog.info("Mouse hover is performed on element with"
					+ locator.toString());
		} 
		catch (NoSuchElementException e)
		{
			tLog.error("Unable to perform MouseOver; The element with"
					+ locator.toString()
					+ " not found",e.fillInStackTrace());
			throw new NoSuchElementException("The element with"
					+ locator.toString()
					+ " not found");
		}
	} 
		
	/**
	 * This function is to click on an element by moving the mouse pointer 
	 * to the specified location or to read the tip of a mouse 
	 * 
	 * @param locator 
	 * 				- By object to locate the element to which mouse pointer has 
	 * 				to be moved
	 */
	public void moveMouseTipAndClick(By locator)
	{
		try
		{
			WebElement element = driver.findElement(locator);
			Locatable hoverItem = (Locatable) element;
			Mouse mouse = ((HasInputDevices) driver).getMouse();
			mouse.mouseMove(hoverItem.getCoordinates());
			mouse.click(hoverItem.getCoordinates());
		}
		catch(NoSuchElementException e)
		{
			tLog.error("Unable to perform click; The element with"
					+ locator.toString().replace("By.", " ")
					+ " not found",e.fillInStackTrace());
			throw new NoSuchElementException("The element with"
					+ locator.toString().replace("By.", " ")
					+ " not found");
		}
	}

	/**
	 * This function is to click on an element by moving the mouse pointer 
	 * to the specified location or to read the tip of a mouse 
	 * 
	 * @param locator 
	 * 				- WebElement object to locate the element to which mouse pointer has 
	 * 				to be moved
	 */
	public void moveMouseTipAndClick(WebElement locator)
	{
		try
		{
			Locatable hoverItem = (Locatable) locator;
			Mouse mouse = ((HasInputDevices) driver).getMouse();
			mouse.mouseMove(hoverItem.getCoordinates());
			mouse.click(hoverItem.getCoordinates());
		}
		catch(NoSuchElementException e)
		{
			tLog.error("Unable to perform click; The element with"
					+ locator.toString()
					+ " not found",e.fillInStackTrace());
			throw new NoSuchElementException("The element with"
					+ locator.toString()
					+ " not found");
		}
	}
	
	/**
	 * This function performs a click-and-hold at the location of the source
	 * element; moves to the location of the target element, then releases the
	 * mouse.
	 * 
	 * @param initialElementLocator
	 *            - By object of the initial location of the source webelement
	 * @param finalElementLocator
	 *            - By object of the final location where the webelement has to
	 *              be moved
	 */
	public void dragAndDrop(By initialElementLocator, By finalElementLocator)
	{
		try 
		{
			Actions builder = new Actions(driver);
			Action dragAndDrop = builder
					.clickAndHold(driver.findElement(initialElementLocator))
					.moveToElement(driver.findElement(finalElementLocator))
					.release(driver.findElement(finalElementLocator))
					.build();
			dragAndDrop.perform();
			tLog.info("The element is draged from"
					+ initialElementLocator.toString().replace("By.", " ")
					+ " to"
					+ finalElementLocator.toString().replace("By.", " "));
		} 
		catch (NoSuchElementException e) 
		{
			tLog.error("Unable to perform dragAndDrop;The element is not draged from"
					+ initialElementLocator.toString().replace("By.", " ")
					+ " to"
					+ finalElementLocator.toString().replace("By.", " "),e.fillInStackTrace());
			throw new NoSuchElementException("Unable to perform dragAndDrop;The element is not draged from"
					+ initialElementLocator.toString().replace("By.", " ")
					+ " to"
					+ finalElementLocator.toString().replace("By.", " "));
		}
	}
	
	
	/**
	 * This function is to perform double click on a webelement
	 * 
	 * @param locator
	 *            - By object of the webelement on which double click has to be
	 *            performed
	 */
	public void doubleClick(By locator)
	{
		try
		{
			Actions builder = new Actions(driver);
			builder.doubleClick(driver.findElement(locator));
			tLog.info("The element with" + locator.toString().replace("By.", " ")
					+ " is right clicked");
		}
		catch(NoSuchElementException e)
		{
			tLog.error("Unable to perform doubleClick; The element with"
					+ locator.toString().replace("By.", " ")
					+ " not found",e.fillInStackTrace());
			throw new NoSuchElementException("The element with"
					+ locator.toString().replace("By.", " ")
					+ " not found");
		}
	}
	
	/**
	 * This function is to perform double click on a WebElement
	 * 
	 * @param locator
	 *            - object of the WebElement on which double click has to be
	 *            performed
	 */
	public void doubleClick(WebElement locator)
	{
		try
		{
			Actions builder = new Actions(driver);
			builder.doubleClick(locator);
			tLog.info("The element with" + locator.toString()
					+ " is right clicked");
		}
		catch(NoSuchElementException e)
		{
			tLog.error("Unable to perform doubleClick; The element with"
					+ locator.toString().replace("By.", " ")
					+ " not found",e.fillInStackTrace());
			throw new NoSuchElementException("The element with"
					+ locator.toString()
					+ " not found");
		}
	}	
	/**
	 * This funtion is to maximize the Current Browser Window
	 * 
	 */
	public void maximizeWindow() 
	{
		try 
		{
			driver.manage().window().maximize();
			tLog.info("Window is maximized");
		} 
		catch (UnreachableBrowserException e) 
		{
			tLog.error("Unable to maximize the window: ",e.fillInStackTrace());
			throw new NoSuchElementException("Unable to maximize the window");
		}
	}
	
	
	/**
	 * This function is to add a time delay
	 * 
	 * @param time
	 *            - time duration in MilliSeconds
	 */
	public void delay(int time) throws InterruptedException 
	{
		try 
		{
			Thread.sleep(time);
			tLog.info("Delay for " + time + " MilliSeconds is added");
		} 
		catch (InterruptedException e) 
		{
			tLog.error("Issue in adding extra delay",e.fillInStackTrace());
			throw new InterruptedException("Issue in adding extra delay");
		} 
	}
	
	
	/**
	 * This function is to get the current window handle
	 * 
	 * @return windowHandle 
	 * 				- returns the handle of current browser window
	 */
	public String getWindowHandle() 
	{
		String windowHandle = driver.getWindowHandle();
		tLog.info("The current window handle " + windowHandle
				+ " is returned");
		try 
		{
			windowHandle = driver.getWindowHandle();
			tLog.info("The current window handle "+windowHandle+" is returned");
		} 
		catch (WebDriverException e) 
		{
			tLog.error("Unable to returm the window handle: ",e.fillInStackTrace());
			throw new WebDriverException("Unable to returm the window handle");
		}
		return windowHandle;
	}
	
	
	/**
	 * This function is to switch the driver from Current Window to newly opened Window
	 */
	public void switchToWindow() 
	{
		try 
		{
			for (String windowHandle : driver.getWindowHandles()) 
			{
				driver.switchTo().window(windowHandle);
			}
			tLog.info("The window is switched");
		} 
		catch (NoSuchWindowException e) 
		{
			tLog.error("Unable to switch the window: ",e.fillInStackTrace());
			throw new NoSuchWindowException("Unable to switch the window");
		}
	}
	
	
	/**
	 * This function is to switch into a frame using frame index
	 * 
	 * @param index
	 *            - index of the frame to which driver has to be switched into
	 */
	public void switchToFrameByIndex(int index) {
		try 
		{
			driver.switchTo().frame(index);
			tLog.info("The driver is switched into frame");
		} 
		catch (NoSuchFrameException e) 
		{
			tLog.error("Unable to switch into frame: ",e.fillInStackTrace());
			throw new NoSuchFrameException("Unable to switch into frame");
		}
	}
	
	
	/**
	 * This function is to switch into a frame using the frame name
	 * 
	 * @param frameName
	 *            - name of the frame to which driver has to be switched into
	 */
	public void switchToFrameByName(String frameName) 
	{
		if (!frameName.equalsIgnoreCase(null)) 
		{
			try 
			{
				driver.switchTo().frame(frameName);
				tLog.info("The driver is switched to frame: " + frameName);
			} 
			catch (NoSuchFrameException e) 
			{
				tLog.error("Unable to switch into frame:" ,e.fillInStackTrace());
				throw new NoSuchFrameException("Unable to switch into frame");
			}
		}
		tLog.info("Unable to switch into frame as framename is null");
	}
	
	
	/**
	 * This function is to switch into a frame; frame is located as a webelemet
	 * 
	 * @param locator
	 *            - By object of the webelemet using which frame can be located
	 */
	public void switchToFrameByWebElement(By locator) 
	{
		try 
		{
			driver.switchTo().frame(driver.findElement(locator));
			tLog.info("The driver is switched to frame with"
					+ locator.toString().replace("By.", " "));
		} 
		catch (NoSuchFrameException e) 
		{
			tLog.error("Unable to switch into frame: ",e.fillInStackTrace());
			throw new NoSuchFrameException("Unable to switch into frame");
		}
	}
	  /** 
	   * Typing data value in a text field  	   
	   * 
	   * @param WebElement Element to be selected. 
	   * @param String text to be set in the textbox
	   */
	public void type( WebElement element, String text ) {
		try {
			Actions selAction = new Actions(driver);
			selAction.sendKeys( element, text ).perform();
		} catch (NoSuchElementException e) {
			tLog.info("Error in script execution" + element.toString() + " not found", e);
		}
	}
	
	/** 
	   * This function is used for taking the screenshot of the webpage   	   
	   * and stores the screen shot in the target folder using .jpg extension
	   * @param String action that is performed 
	   * 
	   */
	public void takeScreenshot(String action) throws IOException {
		action = action.replaceAll("[^a-zA-Z0-9]", "");  
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scrFile, new File("target/" + System.currentTimeMillis() + action + ".jpg"));    	
	}
	
	/** 
	   * This function is used for highlighting an element on the screen using Javascript executor
	   * @param WebElement - the element that needs to be highlighted in the page 
	   * 
	   */
	public void highlightElement(WebElement element) {
		  //Creating JavaScriptExecuter Interface
		   JavascriptExecutor js = (JavascriptExecutor)driver;
		   for (int iCnt = 0; iCnt < 5; iCnt++) {
		      //Execute javascript
		         js.executeScript("arguments[0].style.border='4px solid yellow'", element);
		         driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		         js.executeScript("arguments[0].style.border=''", element);
		   }
	}

	/** 
	   * This function is putting an explicit wait time after a click or navigate action
	   * @param Long - time duration to wait 
	   * 
	   */	
	public void PageLoadWait(long time)
	{
	   synchronized(driver) {
			try {
				driver.wait(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
	}

	/**
	 * This function is to check whether a webelement is visible or not
	 * 
	 * @param locator 
	 * 				- By object of the webelement which is to be checked
	 * @return boolean 
	 * 				- returns true if the specified webelement is visible,
	 * 				  else it will return false
	 */
	public boolean isElementVisible(By locator)
	{
		tLog.info("The element with "
					+ locator.toString().replace("By.", " ") + " is visible");
		return driver.findElement(locator).isDisplayed();
	}

	/**
	 * This function is to check whether a webelement is visible or not
	 * 
	 * @param locator 
	 * 				- By object of the webelement which is to be checked
	 * @return boolean 
	 * 				- returns true if the specified webelement is visible,
	 * 				  else it will return false
	 */
	public boolean isElementVisible(WebElement locator)
	{
		tLog.info("The element with "
					+ locator.toString().replace("By.", " ") + " is visible");
		return locator.isDisplayed();
	}
	
	/**
	 * This function is to check whether a webelement is enabled or not
	 * 
	 * @param locator
	 * 				- By object of the webelement which is to be checked
	 * @return boolean 
	 * 				- returns true if the specified webelement is enabled, 
	 * 				  else it will return false
	 */
	public boolean isElementEnabled(By locator) 
	{
		tLog.info("The element with" 
					+ locator.toString().replace("By.", " ")+ " is enabled");
		return driver.findElement(locator).isEnabled();
	}

	/**
	 * This function is to check whether a webelement is enabled or not
	 * 
	 * @param locator
	 * 				- WebElement object which is to be checked
	 * @return boolean 
	 * 				- returns true if the specified webelement is enabled, 
	 * 				  else it will return false
	 */
	public boolean isElementEnabled(WebElement locator) 
	{
		tLog.info("The element with" 
					+ locator.toString()+ " is enabled");
		return locator.isEnabled();
	}
	
	/**
	 * This function is to check whether the Current Window Title is as expected
	 * 
	 * @param expectedTitle 
	 * 				- Title expected in the Current Window
	 * @return boolean 
	 * 				- returns true if the CurrentTitle matches the expectedTitle, 
	 * 				  else it will return false
	 */
	public boolean isTitleAsExpected(String expectedTitle)
	{
		tLog.info("The current window title is "+getPageTitle()
					+" whereas the expected is "+expectedTitle);
		return expectedTitle.equals(getPageTitle());
	}
	
	/**
	 * This function is to check whether there is any alert present.
	 * 
	 * 
	 * @return boolean 
	 * 				- returns true if alert is present, 
	 * 				  else it will return false
	 */
	
	public boolean isAlertPresent()
	{
		try{
			driver.switchTo().alert();
			tLog.info("An alert box is present");
		    return true;
		}
		catch(Exception e)
		{
			tLog.error("There is no alert present ", e.fillInStackTrace());
			return false;
		}
	}

	
	/**
	 * This function is to get a cookie with a specific name
	 * 
	 * @param cookieName
	 *            	- Name of the cookie which is to be returned
	 * @return Cookie 
	 * 				- Returns the cookie value for the name specified, or null
	 *         			if no cookie found with the given name
	 */
	public Cookie getCookie(String cookieName)
	{
		tLog.info("The cookie:"+cookieName +" is obtained");
		return driver.manage().getCookieNamed(cookieName);
	}
	
	
	/**
	 * This function is to delete a cookie from the browser's "cookie jar"
	 * The domain of the cookie will be ignored.
	 * 
	 * @param cookieName 
	 * 				- name of the cookie which is to be deleted.
	 */
	public void deleteCookieNamed(String cookieName)
	{
		if(!cookieName.equalsIgnoreCase(null)) 
		{
			try 
			{
				tLog.info("The cookie:"+cookieName +" is deleted");
				driver.manage().deleteCookieNamed(cookieName);
			}
			catch (InvalidCookieDomainException e) 
			{
				tLog.error("Unable to delete the cookie: ",e.fillInStackTrace());
				throw new InvalidCookieDomainException("The cookie with name "
							+ cookieName+ " cannot be deleted");
			}
		}
		else
			tLog.info("Cookie Name is null; Unable to delete");
	}
	
	
	/**
	 * This function is to delete all the cookies for the current domain
	 * 
	 */
	public void deleteAllCookie()
	{
		try 
		{
			driver.manage().deleteAllCookies();
			tLog.info("All cookies are deleted");
		} 
		catch (InvalidCookieDomainException e)
		{
			tLog.error("Unable to delete cookies: ",e.fillInStackTrace());
			throw new InvalidCookieDomainException("Unable to delete cookies");
		}
	}
	
	
	/**
	 * This function is to perform a right click on a particular webelement
	 * @param locator - By object of the Webelement on which right click 
	 * 					operation has to be performed
	 */
	public void rightClick(By locator)
	{
		try
		{
			WebElement webElement = driver.findElement(locator);
			Actions action= new Actions(driver);
			action.contextClick(webElement).build().perform();
			tLog.info("The element with "
						+locator.toString().replace("By."," ")+" is right clicked");
		}
		catch (NoSuchElementException e) 
		{
			tLog.error("Unable to scroll: ",e.fillInStackTrace());
			throw new NoSuchElementException("The element with"
						+ locator.toString().replace("By.", " ")
						+ " not found");
		}
		catch (UnsupportedCommandException e) 
		{
			tLog.error("Unable to scroll: ",e.fillInStackTrace());
			throw new UnsupportedCommandException("Command used by the webdriver is unsupported");
		}
	} 

	/**
	 * This function is to perform a right click on a particular webelement
	 * @param locator - Webelement on which right click 
	 * 					operation has to be performed
	 */
	public void rightClick(WebElement locator)
	{
		try
		{
			Actions action= new Actions(driver);
			action.contextClick(locator).build().perform();
			tLog.info("The element with "
						+locator.toString()+" is right clicked");
		}
		catch (NoSuchElementException e) 
		{
			tLog.error("Unable to scroll: ",e.fillInStackTrace());
			throw new NoSuchElementException("The element with"
						+ locator.toString()
						+ " not found");
		}
		catch (UnsupportedCommandException e) 
		{
			tLog.error("Unable to scroll: ",e.fillInStackTrace());
			throw new UnsupportedCommandException("Command used by the webdriver is unsupported");
		}
	} 

	
	/**
	 * This function is to move the Webelement to an offset from the 
	 * top-left corner of the Webelement
	 * 
	 * @param locator 
	 * 				- By object to locate the Webelement which is to be moved
	 * @param xOffset 
	 * 				- xOffset by which the Webelement will be moved 
	 * 				  from the current position towards x-axis
	 * @param yOffset 
	 * 				- yOffset by which the Webelement will be moved 
	 * 				  from the current position towards y-axis
	 */
	public void moveToElement(By locator,int xOffset,int yOffset)
	{
		try
		{
			Actions builder = new Actions(driver);
			builder.moveToElement(driver.findElement(locator), xOffset, yOffset);
			tLog.info("Element with "+locator.toString().replace("By."," ")
						+" " +"is moved "+xOffset+" along x-axis and"
						+yOffset+" along y-axis");
		}
		catch (MoveTargetOutOfBoundsException e) 
		{
			tLog.error("Unable to move the element from current position",e.fillInStackTrace());
			throw new MoveTargetOutOfBoundsException("Target provided x:"+xOffset
						+"and y:"+yOffset+"is invalid");
		}
		catch (NoSuchElementException e) 
		{
			tLog.error("Unable to move the element from current position",e.fillInStackTrace());
			throw new NoSuchElementException("The element with"
						+ locator.toString().replace("By.", " ")
						+ " not found");
		}
		
	}
	
	/**
	 * This function is to move the Webelement to an offset from the 
	 * top-left corner of the Webelement
	 * 
	 * @param locator 
	 * 				- Webelement object to locate which is to be moved
	 * @param xOffset 
	 * 				- xOffset by which the Webelement will be moved 
	 * 				  from the current position towards x-axis
	 * @param yOffset 
	 * 				- yOffset by which the Webelement will be moved 
	 * 				  from the current position towards y-axis
	 */
	public void moveToElement(WebElement locator,int xOffset,int yOffset)
	{
		try
		{
			Actions builder = new Actions(driver);
			builder.moveToElement(locator, xOffset, yOffset);
			tLog.info("Element with "+locator.toString().replace("By."," ")
						+" " +"is moved "+xOffset+" along x-axis and"
						+yOffset+" along y-axis");
		}
		catch (MoveTargetOutOfBoundsException e) 
		{
			tLog.error("Unable to move the element from current position",e.fillInStackTrace());
			throw new MoveTargetOutOfBoundsException("Target provided x:"+xOffset
						+"and y:"+yOffset+"is invalid");
		}
		catch (NoSuchElementException e) 
		{
			tLog.error("Unable to move the element from current position",e.fillInStackTrace());
			throw new NoSuchElementException("The element with"
						+ locator.toString()
						+ " not found");
		}
		
	}
		
	/**
	 * This function is to handle the alert; Will Click on OK button
	 * First get a handle to the open alert, 
	 * prompt or confirmation and then accept the alert.
	 * 
	 */
	public void acceptAlert()
	{
		try 
		{
			Alert alertBox=driver.switchTo().alert();
			alertBox.accept();
			tLog.info("The alert is accepted");
		} 
		catch (NoAlertPresentException e) 
		{
			tLog.error("Unable to access the alert: ",e.fillInStackTrace());
			throw new NoAlertPresentException("Alert not present");
		}
	}
	
	
	/**
	 * This function is to handle the alert; Will Click on Cancel button
	 * First get a handle to the open alert, 
	 * prompt or confirmation and then dismiss the alert.
	 * 
	 */
	public void dismissAlert()
	{
		try 
		{
			Alert alertBox=driver.switchTo().alert();
			alertBox.dismiss();
			tLog.info("The alert is dismissed");
		} 
		
		catch (NoAlertPresentException e) 
		{
			tLog.error("Unable to access the alert: ",e.fillInStackTrace());
			throw new NoAlertPresentException("Alert not present");
		}
	}
	
	
	/**
	 * This function is to get the text which is present on the Alert.
	 * 
	 * @return String
	 * 			 - returns the text message which is present on the Alert.
	 */
	public String getAlertMessage()
	{	
		String alertMessage = null;
		try 
		{
			Alert alertBox=driver.switchTo().alert();
			tLog.info("The text "+alertBox.getText()+" within popup is returned");
			alertMessage = alertBox.getText();
		} 
		catch (NoAlertPresentException e) 
		{
			tLog.error("Unable to access the alert:",e.fillInStackTrace());
			throw new NoAlertPresentException("Alert not present");
		}
		return alertMessage;
	}
	
	
	/**
	 * This function closes the Current Browser Window
	 * 
	 */
	public void closeCurrentWindow()
	{
		driver.close();
		tLog.info("Driver window is closed");
	}
	
	public int getNoOfLinks(String linktxt)
	{
		List<WebElement> links = driver.findElements(By.linkText(linktxt));
		return links.size();
	} 	
	
	
	/** 
	   * This function is to send keys to the page using Robot api for elements not identifiable by selenium
	   * for example windows popup for authentication
	   * @param int - array of int leycodes to be sent to the screen 
	   * @param Robot api
	   */
	protected void enterRobotText(int strText[], Robot robot) {
		for (int i = 0; i < strText.length; i++)
        {    
			int keyInputP = strText[i];
        	if (boolShift[i]) {
        		robot.keyPress(KeyEvent.VK_SHIFT);
    			robot.keyPress(keyInputP);
    			robot.keyRelease(KeyEvent.VK_SHIFT);
    			robot.keyRelease(keyInputP);
        	} else {
        		robot.keyPress(keyInputP);
        		robot.keyRelease(keyInputP);
        	}
          robot.delay(200);
        }
	}

	/** 
	   * This function is get keycodes to be used for Robot API
	   * 
	   * @param String - the string for which keycodes needs to be generated 
	   * @param Booleen - boolean indicator to generate keycode for capital letters or special characters using Shift key
	   * @return int[] - array of int keycdes for Robot api
	   */
	protected int[] getKeyCode(String str, boolean boolID) {
		int keyCodes[] = new int[str.length()];
		boolShift = new boolean[str.length()];
		Arrays.fill(boolShift, Boolean.FALSE);
		for (int i = 0; i < str.length(); i++ ){
			switch (str.charAt(i)) {
			case ')': keyCodes[i] = KeyEvent.VK_0; 
					boolShift[i] = true; 
					break;
			case '!': keyCodes[i] = KeyEvent.VK_1; 
					boolShift[i] = true; 
					break;
			case '@': keyCodes[i] = KeyEvent.VK_2; 
					boolShift[i] = true; 
					break;			
			case '#': keyCodes[i] = KeyEvent.VK_3;
					boolShift[i] = true; 
					break;
			case '$': keyCodes[i] = KeyEvent.VK_4;
					boolShift[i] = true;
					break;
			case '%': keyCodes[i] = KeyEvent.VK_5; 
					boolShift[i] = true; 
					break;
			case '^': keyCodes[i] = KeyEvent.VK_6; 
					boolShift[i] = true; 
					break;
			case '&': keyCodes[i] = KeyEvent.VK_7;
					boolShift[i] = true; 
					break;
			case '*': keyCodes[i] = KeyEvent.VK_8; 
					boolShift[i] = true; 
					break;
			case '(': keyCodes[i] = KeyEvent.VK_9; 
					boolShift[i] = true; 
					break;
			case '0': keyCodes[i] = KeyEvent.VK_0;
					boolShift[i] = false;
					break;
			case '1': keyCodes[i] = KeyEvent.VK_1;
					boolShift[i] = false;
					break;
			case '2': keyCodes[i] = KeyEvent.VK_2;
					boolShift[i] = false;
					break;			
			case '3': keyCodes[i] = KeyEvent.VK_3;
					boolShift[i] = false;
					break;
			case '4': keyCodes[i] = KeyEvent.VK_4;
					boolShift[i] = false; 
					break;
			case '5': keyCodes[i] = KeyEvent.VK_5; 
					boolShift[i] = false; 
					break;
			case '6': keyCodes[i] = KeyEvent.VK_6;
					boolShift[i] = false;
					break;
			case '7': keyCodes[i] = KeyEvent.VK_7;
					boolShift[i] = false;
					break;
			case '8': keyCodes[i] = KeyEvent.VK_8; 
					boolShift[i] = false; 
					break;
			case '9': keyCodes[i] = KeyEvent.VK_9; 
					boolShift[i] = false; 
					break;
			case '-': keyCodes[i] = KeyEvent.VK_MINUS;
					boolShift[i] = false; 
					break;
			case '_': keyCodes[i] = KeyEvent.VK_MINUS; 
					boolShift[i] = true; 
					break;
			case '+': keyCodes[i] = KeyEvent.VK_ADD;
					boolShift[i] = false; 
					break;
			case '.': keyCodes[i] = KeyEvent.VK_PERIOD;
					boolShift[i] = false;
					break;
			case '\\': keyCodes[i] = KeyEvent.VK_BACK_SLASH;
					boolShift[i] = false;
					break;					
			case 'a': keyCodes[i] = KeyEvent.VK_A;
					boolShift[i] = false;
					break;
			case 'b': keyCodes[i] = KeyEvent.VK_B;
					boolShift[i] = false;
					break;
			case 'c': keyCodes[i] = KeyEvent.VK_C;
					boolShift[i] = false;
					break;
			case 'd': keyCodes[i] = KeyEvent.VK_D; 
					boolShift[i] = false;
					break;
			case 'e': keyCodes[i] = KeyEvent.VK_E; 
					boolShift[i] = false;
					break;
			case 'f': keyCodes[i] = KeyEvent.VK_F;
					boolShift[i] = false;
					break;
			case 'g': keyCodes[i] = KeyEvent.VK_G; 
					boolShift[i] = false;
					break;
			case 'h': keyCodes[i] = KeyEvent.VK_H;
					boolShift[i] = false;
					break;
			case 'i': keyCodes[i] = KeyEvent.VK_I; 
					boolShift[i] = false;
					break;
			case 'j': keyCodes[i] = KeyEvent.VK_J; 
					boolShift[i] = false;
					break;
			case 'k': keyCodes[i] = KeyEvent.VK_K; 
					boolShift[i] = false;
					break;
			case 'l': keyCodes[i] = KeyEvent.VK_L; 
					boolShift[i] = false;
					break;
			case 'm': keyCodes[i] = KeyEvent.VK_M; 
					boolShift[i] = false;
					break;
			case 'n': keyCodes[i] = KeyEvent.VK_N; 
					boolShift[i] = false;
					break;
			case 'o': keyCodes[i] = KeyEvent.VK_O;
					boolShift[i] = false;
					break;
			case 'p': keyCodes[i] = KeyEvent.VK_P; 
					boolShift[i] = false;
					break;
			case 'q': keyCodes[i] = KeyEvent.VK_Q; 
					boolShift[i] = false;
					break;
			case 'r': keyCodes[i] = KeyEvent.VK_R; 
					boolShift[i] = false;
					break;
			case 's': keyCodes[i] = KeyEvent.VK_S; 
					boolShift[i] = false;
					break;
			case 't': keyCodes[i] = KeyEvent.VK_T; 
					boolShift[i] = false;
					break;
			case 'u': keyCodes[i] = KeyEvent.VK_U; 
					boolShift[i] = false;
					break;
			case 'v': keyCodes[i] = KeyEvent.VK_V;
					boolShift[i] = false;
					break;
			case 'w': keyCodes[i] = KeyEvent.VK_W; 
					boolShift[i] = false;
					break;
			case 'x': keyCodes[i] = KeyEvent.VK_X; 
					boolShift[i] = false;
					break;
			case 'y': keyCodes[i] = KeyEvent.VK_Y; 
					boolShift[i] = false;
					break;
			case 'z': keyCodes[i] = KeyEvent.VK_Z; 
					boolShift[i] = false;
					break;
			case 'A': keyCodes[i] = KeyEvent.VK_A; 
					boolShift[i] = true;
					break;
			case 'B': keyCodes[i] = KeyEvent.VK_B; 
					boolShift[i] = true;
					break;
			case 'C': keyCodes[i] = KeyEvent.VK_C; 
					boolShift[i] = true;
					break;
			case 'D': keyCodes[i] = KeyEvent.VK_D; 
					boolShift[i] = true;
					break;
			case 'E': keyCodes[i] = KeyEvent.VK_E; 
					boolShift[i] = true;
					break;
			case 'F': keyCodes[i] = KeyEvent.VK_F; 
					boolShift[i] = true;
					break;
			case 'G': keyCodes[i] = KeyEvent.VK_G; 
					boolShift[i] = true;
					break;
			case 'H': keyCodes[i] = KeyEvent.VK_H; 
					boolShift[i] = true;
					break;
			case 'I': keyCodes[i] = KeyEvent.VK_I; 
					boolShift[i] = true;
					break;
			case 'J': keyCodes[i] = KeyEvent.VK_J; 
					boolShift[i] = true;
					break;
			case 'K': keyCodes[i] = KeyEvent.VK_K; 
					boolShift[i] = true;
					break;
			case 'L': keyCodes[i] = KeyEvent.VK_L; 
					boolShift[i] = true;
					break;
			case 'M': keyCodes[i] = KeyEvent.VK_M; 
					boolShift[i] = true;
					break;
			case 'N': keyCodes[i] = KeyEvent.VK_N; 
					boolShift[i] = true;
					break;
			case 'O': keyCodes[i] = KeyEvent.VK_O; 
					boolShift[i] = true;
					break;
			case 'P': keyCodes[i] = KeyEvent.VK_P; 
					boolShift[i] = true;
					break;
			case 'Q': keyCodes[i] = KeyEvent.VK_Q; 
					boolShift[i] = true;
					break;
			case 'R': keyCodes[i] = KeyEvent.VK_R; 
					boolShift[i] = true;
					break;
			case 'S': keyCodes[i] = KeyEvent.VK_S; 
					boolShift[i] = true;
					break;
			case 'T': keyCodes[i] = KeyEvent.VK_T; 
					boolShift[i] = true;
					break;
			case 'U': keyCodes[i] = KeyEvent.VK_U; 
					boolShift[i] = true;
					break;
			case 'V': keyCodes[i] = KeyEvent.VK_V; 
					boolShift[i] = true;
					break;
			case 'W': keyCodes[i] = KeyEvent.VK_W; 
					boolShift[i] = true;
					break;
			case 'X': keyCodes[i] = KeyEvent.VK_X; 
					boolShift[i] = true;
					break;
			case 'Y': keyCodes[i] = KeyEvent.VK_Y; 
					boolShift[i] = true;
					break;
			case 'Z': keyCodes[i] = KeyEvent.VK_Z; 
					boolShift[i] = true;
					break;
			}
		}
		return keyCodes;
	}

}
