# Hybrid Automation Framework ⚡️ 🚀

## Technologies used 🛠️
<!-- markdownlint-enable -->
<!-- prettier-ignore-end -->
<!-- ALL-CONTRIBUTORS-LIST:END -->
   1) Appium
   2) Selenium Webdriver/Grid
   3) Java
   4) JavaScript
   5) TestNG
   6) Maven
   7) ATU Reports
   8) Cucumber-BDD
   9) Log4j


<!-- markdownlint-enable -->
<!-- prettier-ignore-end -->
<!-- ALL-CONTRIBUTORS-LIST:END -->

---

  The framework is created using standard Maven project structure and below are the components.
  -- The src/main/java folder contains the reusable functions and has 3 packages
  
     1) com.frmwrk.base - It has the following classes 
        - The BasePage class which contains the selenium reusable functions
        - The RestClientAPI class which contains basic functions for connecting to Jira and updating status in Jira
        - The TestBaseSetup class which is the main initialization class for running the TestNG test
     2) com.frmwrk.base.exceptions
        - This contains the 4 exception classes for appending customized exception message in the reports
     3) com.frmwrk.base.utils
        - This contains the utility functions for interacting with Excel sheet using jxl api
        - Database related functions like connection, disconnection and data fetch
        - The TestLogger class contains the slf4j log initialization 
  
  -- The src/main/resources folder contains the log4j properties which can be customized based on need. 
     There are 2 files, first is for html log file customization and second is for text log file customization. In the framework we are using
     html file for providing log
  
  -- The src/test/java contains the test related functions
     
     1) com.frmwrk.pages - It will contain the selenium page objects. The page objects extend the base page and the elements of 
        the pages are defined using @FindBy annotation and initialized using the PageFactory notiation and sample is 
        given below
       	  @FindBy(name="q") WebElement SrchText;
             @FindBy(name="btnG") WebElement btnSearch;
             public GoogleSearchPage(RemoteWebDriver driver) {
                super(driver);
                PageFactory.initElements(driver, this);
             }
        Refer the GoogleSearchPage sample class
           
      2) com.frmwrk.test - This contains the cucucumber runner classes (browser and headless) and standalone TestNG test class
         
         For selenium webdriver tests (i.e.browser) option we are using selenium grid for execution. So before executing the tests, 
         the tester has to ensure that the selenium hub has been started. For that we have to download the selenium  standalone
         server jar corresponding to the version that is being used in the POM file for selenium and then  open command prompt and
         
         java -jar <Path>\selenium-server-standalone-2.53.0.jar -role hub -port 4447. For the nodes use the following commands
         
         java -jar selenium-server-standalone-2.53.0.jar -role node -hub http://localhost:4447/grid/register -port 5555
         
         Similarly for Chrome node update the browserName=chrome and for firefox update is to firefox
         
      3) com.frmwrk.test.steps - This class will contain the glue codes or step definitions and there is CommonMethods class
         which contains the hooks for Cucumber
 
 
 -- The src/test/resources as the name suggests contains the test resources
      
      1) Feature folder contains the BDD feature files and tester can create subfolders within that to organize the BDD Feature files
      
      2) InputData folder 
         - contains the DriverData sheet which contains the execution sequence. 
         It has 3 columns Execution Status, FeatureName and Tags. 
           Execution Status - can be Y/N, where Y means execute the row and N means skip the row for execution and is a mandatory field.
           FeatureName - Name of the feature to be executed which is relative path from src as given below. It is a mandatory field.
                        src/test/resources/features/GoogleSearch.feature
           Tags are optional but if specified then tester has ensure the tag is specified in the FeatureFile being executed else runtime
           exception will occur. It can be left blank and it means all the scenarios withing the feature file have to be executed
           
           The Driversheet can contain multiple sheets for execution and we can provide the sheetname dynamically through Maven command
           line. If no sheetname is specified then the defaul sheetname present in the global properties file will be used for execution
           sequence.
         - The second sheet is the testdata sheet which contains ScenarioDataSequenceNo which corresponds to the data sequence given in
           the featurefile and samples are given below. The scenario name should follow this syntax given the sample below
           
        Scenario Outline: StoryID SmokeScenario
  				Given user is on google search page
  				When enter "<srchString>" text into search field
  				Then the browser title should have "<outString>"
 
  			Examples: 
  				| srchString   | outString |
  				| Parm-1       | Parm-2    |
  				| Parm-1       | Parm-2    |
  	
  			Below is the example for providing data when embedded in the steps
  
  			Scenario: StoryID RegressionScenario
    			Given user is on google search page
    			When enter "Parm-1" text into search field
    			Then the browser title should have "Parm-2" 
          
          The data in the testdata sheet is provided as below
          
          ScenarioName-DataSequenceNo
 			    SmokeScenario1
 			    RegressionScenario1
 			    SmokeScenario2
          
          The ParmCount field in the test data sheet is used to provide the parameter count in the scenario.
          There are 20 parameters defined but more can be provided per scenario based on requirement.
          
      3) atu.properties - for providing ATU properties
      4) db.properties - for database connectivity related things
      5) global.properties - for global configurations
      6) grid.properties - for selenium grid host ip, port 
      7) The Chrome, IE and PhantomJs drivers
      8) ATU Reports Jar and logo which is used in the ATU reports
 
 Setup required for marking scenarios as manual scenarios******************
 
      1) Use the tag @Manual for the scenario which needs to be executed manually
      2) Code the scenario as given below:
 <!-- markdownlint-enable -->
<!-- prettier-ignore-end -->
<!-- ALL-CONTRIBUTORS-LIST:END -->
    @Manual
     Scenario: StoryID ManualScenario
        Given user is on google search page
        When enter "Parm-1" text into search field
        Then the browser title should have "Parm-2"     
 
 Just include the Scenario line and comment the steps in the scenario using #. This is will ensure that the manual
 tags appear in the cucumber report
 
 *****************************************************************************************************************


 Setup required for Appium tests
<!-- markdownlint-enable -->
<!-- prettier-ignore-end -->
<!-- ALL-CONTRIBUTORS-LIST:END -->
    1) Download and install node.js
    2) npm install -g appium
 *****************************************************************************************************************
