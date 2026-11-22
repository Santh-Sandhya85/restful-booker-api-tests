package com.sandhya.api.tests;

import com.sandhya.api.base.BaseTest;
import com.sandhya.api.endpoints.BookingApi;
import com.sandhya.api.utils.TestDataBuilder;
import org.testng.annotations.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class SchemaValidationTests extends BaseTest {

    @Test(description = "GET /booking/{id} response matches the JSON schema")
    public void bookingResponseMatchesSchema() {
        int id = BookingApi.create(TestDataBuilder.randomBooking())
                .then().statusCode(200).extract().path("bookingid");

        BookingApi.get(id).then().statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/booking-schema.json"));
    }
}
