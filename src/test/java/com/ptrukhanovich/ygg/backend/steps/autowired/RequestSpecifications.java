package com.ptrukhanovich.ygg.backend.steps.autowired;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({TestProperties.class})
public class RequestSpecifications {
    private static final String Q_GAMEID = "gameid";
    private static final String Q_ORG = "org";
    private static final String Q_LANG = "lang";
    private static final String Q_FN = "fn";
    private static final String Q_CURRENCY = "currency";
    private static final String Q_AMOUNT = "amount";
    private static final String Q_LINES = "lines";
    private static final String Q_COIN = "coin";
    private static final String Q_CLIENTINFO = "clientinfo";

    @Bean
    public RequestSpecification playerAuthenticatorSpec(@Value("${urlToVikingsGameService}") String urlToApi,
                                                         @Value("${fnAuthenticate}") String fnAuthenticateValue,
                                                         @Value("${lang}") String langValue,
                                                         @Value("${org}") String orgValue,
                                                         @Value("${gameid}") String gameIdValue) {
        return new RequestSpecBuilder()
                .setContentType(ContentType.URLENC)
                .setBaseUri(urlToApi)
                .addQueryParam(Q_FN, fnAuthenticateValue)
                .addQueryParam(Q_ORG, orgValue)
                .addQueryParam(Q_GAMEID, gameIdValue)
                .addQueryParam(Q_LANG, langValue)
                .setAccept(ContentType.JSON)
                .build();
    }

    @Bean
    public RequestSpecification playerPlaySpec(@Value("${urlToVikingsGameService}") String urlToApi,
                                               @Value("${fnPlay}") String fnPlayValue,
                                               @Value("${currency}") String currencyValue,
                                               @Value("${gameid}") String gameIdValue,
                                               @Value("${lines}") String linesValue,
                                               @Value("${amount}") String amountPlayValue,
                                               @Value("${coin}") String coinValue,
                                               @Value("${clientinfo}") String clientinfoValue) {
        return new RequestSpecBuilder()
                .setContentType(ContentType.URLENC)
                .setBaseUri(urlToApi)
                .addQueryParam(Q_FN, fnPlayValue)
                .addQueryParam(Q_GAMEID, gameIdValue)
                .addQueryParam(Q_CURRENCY, currencyValue)
                .addQueryParam(Q_LINES, linesValue)
                .addQueryParam(Q_AMOUNT, amountPlayValue)
                .addQueryParam(Q_COIN, coinValue)
                .addQueryParam(Q_CLIENTINFO, clientinfoValue)
                .setAccept(ContentType.JSON)
                .build();
    }

    @Bean
    public RequestSpecification playerRePlaySpec(@Value("${urlToVikingsGameService}") String urlToApi,
                                               @Value("${fnPlay}") String fnPlayValue,
                                               @Value("${currency}") String currencyValue,
                                               @Value("${gameid}") String gameIdValue,
                                               @Value("${clientinfo}") String clientinfoValue) {
        return new RequestSpecBuilder()
                .setContentType(ContentType.URLENC)
                .setBaseUri(urlToApi)
                .addQueryParam(Q_FN, fnPlayValue)
                .addQueryParam(Q_GAMEID, gameIdValue)
                .addQueryParam(Q_CURRENCY, currencyValue)
                .addQueryParam(Q_CLIENTINFO, clientinfoValue)
                .setAccept(ContentType.JSON)
                .build();
    }

}
