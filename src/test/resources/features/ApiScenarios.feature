Feature: Rolling-Winning

  Scenario: Player rolling slotmachine
    Given player is authorised
    When player play 2 times
    Then player won at least 1 time
