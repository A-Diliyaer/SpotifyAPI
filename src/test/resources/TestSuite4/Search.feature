Feature: Search spotify

  Background: I am authenticated
    Given I am authenticated as "User"

  Scenario: Search for a given artist by name
    Given search for artist by name "Eminem"
    And retrieve artist from the result
    And retrieve artist by id
    Then verify artist details match expected

  Scenario: Search for a given track by name
    Given search for track by name "Astronaut In The Ocean" by artist "Masked Wolf" in album "Astronaut In The Ocean"
    And retrieve track from the result
    And retrieve a track by id
    Then verify searched track details match expected

  Scenario: Search for a given album by name
    Given search for album by name "Music To Be Murdered By"
    And retrieve album from the result
    And retrieve album by id
    Then verify searched album details match expected

  Scenario: Search for a given playlist by name
    Given search for playlist by name "Demo"
    And retrieve playlist from the result
    And retrieve playlist by id
    Then verify searched playlist details match expected


