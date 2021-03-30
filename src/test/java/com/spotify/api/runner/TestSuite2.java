package com.spotify.api.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        glue = "com.spotify.api.steps",
        features = "src/test/resources/TestSuite2",
        plugin = {"html:target/cucumber-html",
                "json:target/cucumber-json/TestSuite2/api.json",
                "rerun:target/rerun.txt"}
)
public class TestSuite2 {
}
