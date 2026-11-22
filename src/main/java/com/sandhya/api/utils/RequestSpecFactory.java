package com.sandhya.api.utils;

import com.sandhya.api.config.ConfigReader;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/** Builds the reusable RequestSpecification shared by all API calls. */
public final class RequestSpecFactory {

    private RequestSpecFactory() {
    }

    public static RequestSpecification baseSpec() {
        return baseBuilder()
                .addFilter(new AllureRestAssured())
                .build();
    }

    private static RequestSpecification sensitiveSpec() {
        return baseBuilder().build();
    }

    private static RequestSpecBuilder baseBuilder() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigReader.baseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .log(io.restassured.filter.log.LogDetail.URI);
    }

    /** Auth requests bypass Allure capture to avoid recording credentials or tokens. */
    public static RequestSpecification credentialSpec() {
        return sensitiveSpec();
    }

    /** Spec with the auth token sent as a cookie (needed for PUT, PATCH, DELETE). */
    public static RequestSpecification authSpec(String token) {
        return new RequestSpecBuilder()
                .addRequestSpecification(sensitiveSpec())
                .addCookie("token", token)
                .build();
    }
}
