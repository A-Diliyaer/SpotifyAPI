package com.spotify.api.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        glue = "com.spotify.api.steps",
        features = "src/test/resources/TestSuite1",
        plugin = {"html:target/cucumber-html",
                  "json:target/cucumber-json/TestSuite1/api.json",
                  "rerun:target/rerun.txt"}
)
public class TestSuite1 {
}
