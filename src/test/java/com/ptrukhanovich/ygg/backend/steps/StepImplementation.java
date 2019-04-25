package com.ptrukhanovich.ygg.backend.steps;

import com.ptrukhanovich.ygg.backend.steps.autowired.RequestSpecifications;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.thucydides.core.annotations.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@ContextConfiguration(classes = {RequestSpecifications.class})
public class StepImplementation {

    private static final String SESSID_PATH = "data.sessid";
    private static final String WAGER_STATUS_PATH = "data.wager.status";
    private static final String WAGER_ID_PATH = "data.wager.wagerid";
    private static final String WON_AMOUNT_PATH = "data.wager.bets[0].wonamount";
    private static final String ZERO_AMOUNT_VALUE = "0.00";
    private static final String PENDING_VALUE = "Pending";
    private static final String Q_SESSID = "sessid";
    private static final String Q_WAGERID = "wagerid";
    private static final String Q_BETID = "betid";
    private static final String Q_STEP = "step";
    private static final String Q_CMD = "cmd";

    @Autowired
    private RequestSpecification playerPlaySpec;

    @Autowired
    private RequestSpecification playerAuthenticatorSpec;

    @Autowired
    private RequestSpecification playerRePlaySpec;

    private ExtractableResponse<Response> authorisationResponse;
    private LinkedList<ExtractableResponse<Response>> playHistory;

    private String sessId = "";

    @Step
    ExtractableResponse<Response> getAuthorisationResponse() {
        if (this.authorisationResponse == null) {
            this.authorisationResponse = retrieveAuthorisationResponse();
            this.sessId = this.authorisationResponse.path(SESSID_PATH);
        }

        return this.authorisationResponse;
    }

    @Step
    private ExtractableResponse<Response> retrieveAuthorisationResponse() {
        return RestAssured.given()
                .spec(playerAuthenticatorSpec)
                .get()
                .then().log().all().extract();
    }

    @Step
    ExtractableResponse<Response> getPlayResponse() {
        if(this.playHistory == null) {
            this.playHistory = new LinkedList<>();
        }

        ExtractableResponse<Response> response = retrievePlayResponse();
        int step = 2;
        while(response.jsonPath().getString(WAGER_STATUS_PATH).contains(PENDING_VALUE)) {
            response = retrieveRePlayResponse(response.path(WAGER_ID_PATH), step++);
        }

        this.playHistory.add(response);

        return response;
    }

    @Step
    private ExtractableResponse<Response> retrieveRePlayResponse(String wagerId, int step) {
        return RestAssured.given()
                .spec(playerRePlaySpec)
                .queryParam(Q_SESSID, sessId)
                .queryParam(Q_WAGERID, wagerId)
                .queryParam(Q_BETID, 1)
                .queryParam(Q_STEP, step)
                .queryParam(Q_CMD, "C")
                .get()
                .then().log().all().extract();
    }

    @Step
    private ExtractableResponse<Response> retrievePlayResponse() {
        return RestAssured.given()
                .spec(playerPlaySpec)
                .queryParam(Q_SESSID, sessId)
                .get()
                .then().log().all().extract();
    }

    @Step
    List<String> mapResponsesToNonZeroWonAmounts() {
        return this.playHistory.stream()
                .map(r -> r. jsonPath().getString(WON_AMOUNT_PATH))
                .filter(s -> !s.contains(ZERO_AMOUNT_VALUE))
                .collect(Collectors.toList());
    }
}
