package com.sandhya.api.tests;

import com.sandhya.api.base.BaseTest;
import com.sandhya.api.endpoints.BookingApi;
import com.sandhya.api.models.Booking;
import com.sandhya.api.utils.RequestSpecFactory;
import com.sandhya.api.utils.TestDataBuilder;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class NegativeTests extends BaseTest {

    @Test(description = "GET with a non-existent id returns 404")
    public void getInvalidIdReturns404() {
        BookingApi.get(99999999).then().statusCode(404);
    }

    @Test(description = "Creating a booking with missing required fields fails")
    public void createWithMissingFieldsFails() {
        // Restful-Booker answers 500 when mandatory fields are missing
        BookingApi.create(Map.of("firstname", "OnlyName"))
                .then().statusCode(500);
    }

    @Test(description = "DELETE without a token is forbidden")
    public void deleteWithoutTokenReturns403() {
        int id = BookingApi.create(TestDataBuilder.randomBooking())
                .then().statusCode(200).extract().path("bookingid");

        BookingApi.deleteWithoutToken(id).then().statusCode(403);

        // booking must still exist
        BookingApi.get(id).then().statusCode(200);
    }

    @Test(description = "PUT with an invalid token is forbidden")
    public void updateWithInvalidTokenReturns403() {
        int id = BookingApi.create(TestDataBuilder.randomBooking())
                .then().statusCode(200).extract().path("bookingid");
        Booking update = TestDataBuilder.defaultBooking();

        BookingApi.update(id, update, "invalid-token").then().statusCode(403);
    }

    @Test(description = "Updating a booking that does not exist is rejected")
    public void updateNonExistentBooking() {
        Booking update = TestDataBuilder.defaultBooking();
        // Restful-Booker returns 405 for a PUT on a missing id
        BookingApi.update(99999999, update, token).then().statusCode(405);
    }

    @Test(description = "Unsupported content type is rejected")
    public void wrongContentTypeIsRejected() {
        given().spec(RequestSpecFactory.baseSpec())
                .contentType(ContentType.TEXT)
                .body("this is not json")
                .when().post("/booking")
                .then().statusCode(org.hamcrest.Matchers.anyOf(
                        org.hamcrest.Matchers.is(400),
                        org.hamcrest.Matchers.is(415),
                        org.hamcrest.Matchers.is(500)));
    }
}
