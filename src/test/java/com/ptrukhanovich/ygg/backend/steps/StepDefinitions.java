package com.ptrukhanovich.ygg.backend.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Steps;

import static org.hamcrest.MatcherAssert.assertThat;

public class StepDefinitions {
    @Steps
    StepImplementation stepImplementation;

    @Given("^player is authorised$")
    public void playerIsAuthorised() {
        assertThat("Authorisation response code is not 200",
                stepImplementation.getAuthorisationResponse().response().getStatusCode() == 200);
    }

    @When("^player play$")
    public void playerPlay() {
        assertThat("Game response for PLAY is not 200",
                stepImplementation.getPlayResponse().response().getStatusCode() == 200);
    }

    @When("^player play (\\d+) times$")
    public void playerPlayTimes(int attempts) throws Throwable{
        for (int i = 0; i < attempts; i++) {
            playerPlay();
        }
    }

    @Then("^player won at least (\\d+) time$")
    public void playerWonAtLeast(int wonTimes) {
        assertThat("Error",
                stepImplementation.mapResponsesToNonZeroWonAmounts().size() >= wonTimes);
    }
}
