Feature: PlayBack

  Background: I am authenticated
    Given I am authenticated as "User"

  Scenario: Add a track to playback and verify details
    Given a device is available
    And search request for album "Speed Racer"
    Then play request for the searched album
    And retrieve current playback details
    Then playback details match expected
    |album  |Speed Racer  |
    |artist |Masked Wolf  |

  Scenario: Get user current playback and verify active device
    Given a device is available
    And play request for existing track
    And retrieve current playback details
    And retrieve available devices
    Then active device matches expected

  Scenario: Get user current playback and verify track details
    Given a device is available
    And play request for existing track
    And retrieve current playback details
    And retrieve currently playing track
    Then current track album details match expected
    Then current track artist details match expected

  Scenario: Transfer current playback to another device
    Given a device is available
    And play request for existing track
    And retrieve available devices
    And transfer playback to device to "Aji's iPhone"
    Then playback should start playing on "Aji's iPhone"

  Scenario: Start playback with playlist Demo with track offset
    Given a device is available
    And play request for playlist offset track by 5
    And retrieve currently playing track
    Then verify current track details match expected
    | artist1       |  Tuna Kiremitçi                |
    | artist2       |  Öykü Gürman                   |
    | track         |  İyi Şeyler                    |
    | track_number  |  4                             |
    | album_name    |  Tuna Kiremitçi ve Arkadaşları |


  Scenario: Start PlayBack with playlist Demo with position offset
    Given a device is available
    And play request for playlist offset track by 2
    And skip to position 60 seconds in currently playing track
    And retrieve currently playing track
    Then verify current track play position match expected

  Scenario: Start PlayBack with playlist Demo with invalid position offset
    Given a device is available
    And play request for playlist offset track by 2
    And retrieve currently playing track
    And skip to position greater than the length of the track
    Then verify playBack skipped to next song

  Scenario: pause playback, and verify
    Given a device is available
    And play request for existing track
    And pause playBack
    And retrieve currently playing track
    Then verify playBack is paused

  Scenario: skip playback to next track
    Given a device is available
    And play request for playlist offset track by 0
    And skip playBack to "next" track
    Then retrieve currently playing track
    Then verify current track is "The Business"

  Scenario: skip playback to previous track
    Given a device is available
    And play request for playlist offset track by 1
    And skip playBack to "previous" track
    Then retrieve currently playing track
    Then verify current track is "Astronaut In The Ocean"

  Scenario: set repeat mode on current playBack
    Given a device is available
    And play request for playlist offset track by 0
    And set repeat Mode on "off"
    And retrieve current playback details
    Then verify current playback repeat_state "off"

  Scenario: set volume for playBack
    Given a device is available
    And play request for playlist offset track by 2
    And set playBack volume to 50%
    And retrieve current playback details
    Then verify current playBack volume is 50%

  Scenario: set shuffle mode to playBack
    Given a device is available
    And play request for playlist offset track by 1
    And toggle shuffle mode to "true"
    And retrieve current playback details
    Then verify current playBack shuffle mode is "true"

  Scenario: add an item to queue
    Given a device is available
    And play request for existing track
    And add item to queue
    And skip playBack to "next" track
    And retrieve currently playing track
    Then verify current track is "Birden Geldin Aklıma"
