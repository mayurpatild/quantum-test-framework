Feature: Appium calculator test
     As a user of Android
	 I want to open calculator
	 So that I can perform some calculation

@Smoke
Scenario: StoryID SmokeScenario
    Given user opened calculator
    When user enters the number as "2"
	And user performs "+" operation
    And user enters the number as "3"
	Then user should see the result as "5"

