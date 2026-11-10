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
        return new RequestSpecBuilder()
                .setBaseUri(ConfigReader.baseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addFilter(new AllureRestAssured())
                .log(io.restassured.filter.log.LogDetail.URI)
                .build();
    }

    /** Spec with the auth token sent as a cookie (needed for PUT, PATCH, DELETE). */
    public static RequestSpecification authSpec(String token) {
        return new RequestSpecBuilder()
                .addRequestSpecification(baseSpec())
                .addCookie("token", token)
                .build();
    }
}
