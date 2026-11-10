package com.sandhya.api.endpoints;

import com.sandhya.api.models.Booking;
import com.sandhya.api.utils.RequestSpecFactory;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

/** One method per API operation. Tests call these and stay free of HTTP details. */
public final class BookingApi {

    private static final String BOOKING = "/booking";
    private static final String BOOKING_ID = "/booking/{id}";

    private BookingApi() {
    }

    @Step("POST /booking")
    public static Response create(Object body) {
        return given().spec(RequestSpecFactory.baseSpec())
                .body(body)
                .when().post(BOOKING);
    }

    @Step("GET /booking")
    public static Response getAllIds() {
        return given().spec(RequestSpecFactory.baseSpec())
                .when().get(BOOKING);
    }

    @Step("GET /booking?firstname={firstname}")
    public static Response searchByFirstname(String firstname) {
        return given().spec(RequestSpecFactory.baseSpec())
                .queryParam("firstname", firstname)
                .when().get(BOOKING);
    }

    @Step("GET /booking/{id}")
    public static Response get(Object id) {
        return given().spec(RequestSpecFactory.baseSpec())
                .pathParam("id", id)
                .when().get(BOOKING_ID);
    }

    @Step("PUT /booking/{id}")
    public static Response update(int id, Booking body, String token) {
        return given().spec(RequestSpecFactory.authSpec(token))
                .pathParam("id", id)
                .body(body)
                .when().put(BOOKING_ID);
    }

    @Step("PATCH /booking/{id}")
    public static Response partialUpdate(int id, Map<String, Object> fields, String token) {
        return given().spec(RequestSpecFactory.authSpec(token))
                .pathParam("id", id)
                .body(fields)
                .when().patch(BOOKING_ID);
    }

    @Step("DELETE /booking/{id}")
    public static Response delete(int id, String token) {
        return given().spec(RequestSpecFactory.authSpec(token))
                .pathParam("id", id)
                .when().delete(BOOKING_ID);
    }

    /** DELETE without any auth, used for negative tests. */
    @Step("DELETE /booking/{id} (no token)")
    public static Response deleteWithoutToken(int id) {
        return given().spec(RequestSpecFactory.baseSpec())
                .pathParam("id", id)
                .when().delete(BOOKING_ID);
    }
}
