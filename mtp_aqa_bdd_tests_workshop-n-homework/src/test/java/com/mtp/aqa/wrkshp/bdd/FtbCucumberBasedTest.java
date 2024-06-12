package com.mtp.aqa.wrkshp.bdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features")
public class FtbCucumberBasedTest {
    //TODO: Consider usage of Cucumber's Before/After etc.
}
