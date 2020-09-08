Feature: Search on Google
  As an user
  I want to enter some text on Google search
  So that I get relevant links

@Smoke
  Scenario Outline: StoryID SmokeScenario
    Given user is on google search page
 #   When enter "<srchString>" text into search field
 #   Then the browser title should have "<outString>"

    Examples: 
      | srchString   | outString |
      | Mayur			       | Mayur    |
      | Facebook       | Facebook    |

      
@Regression @Functional
  Scenario: StoryID RegressionScenario
    Given user is on google search page
    When enter "Parm-1" text into search field
    Then the browser title should have "Parm-2"

@Manual
  Scenario: StoryID Scenario
#    Given ManualTestScenario
#    Given user is on google search page
#    When enter "Parm-1" text into search field
#    Then the browser title should have "Parm-2"