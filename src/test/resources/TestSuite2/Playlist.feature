Feature: PlayList

  Background: I am authenticated
    Given I am authenticated as "User"

  Scenario: Create a playlist
    Given retrieve current user profile
    And create "public" playlist "API Playlist1"
    Then verify playlist details match expected

  Scenario: Create a playlist and verify it exists under user playlists
    Given retrieve current user profile
    And create "public" playlist "API Playlist2"
    And retrieve list of user playlists
    Then verify playlist exists in the list

  Scenario:  Create a playlist and update details
    Given retrieve current user profile
    And create "public" playlist "API Playlist3"
    And update playlist details
    | name        | Playlist updated                          |
    | state       | private                                   |
    | description | this is an update to the current playlist |
    And retrieve playlist by id
    Then verify playlist details are updated as expected

  Scenario: Create a playlist and add items
    Given retrieve current user profile
    And create "public" playlist "API Playlist4"
    And add tracks to current playlist by id
    |spotify:track:3Ofmpyhv5UAQ70mENzB277|
    |spotify:track:3jM12sAVBL1xjzqbavzQDj|
    |spotify:track:0LWPIlubnprX9WzFTG9bin|
    And retrieve playlist by id
    Then verify playlist snapshot_id
    Then verify new items exist in the playlist

  Scenario: Reorder playlist items
    Given retrieve existing playlist "Demo"
    And reorder playlist items with given config
    | range_start   | 0 |
    | insert_before | 5 |
    | range_length  | 2 |
    And retrieve playlist by id
    Then verify playlist item reorder match expected

  Scenario: Remove items from a playlist
    Given retrieve existing playlist "Demo"
    And remove below tracks from the playlist
    |spotify:track:5nujrmhLynf4yMoMtj8AQF|
    |spotify:track:1M4OcYkxAtu3ErzSgDEfoi|
    Then verify deleted tracks do not exist in the playlist
