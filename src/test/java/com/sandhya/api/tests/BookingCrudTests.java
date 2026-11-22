package com.sandhya.api.tests;

import com.sandhya.api.base.BaseTest;
import com.sandhya.api.endpoints.BookingApi;
import com.sandhya.api.models.Booking;
import com.sandhya.api.models.BookingResponse;
import com.sandhya.api.utils.TestDataBuilder;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

import static org.hamcrest.Matchers.*;

public class BookingCrudTests extends BaseTest {

    @Test(description = "Create a booking and read it back")
    @Description("POST /booking then GET /booking/{id} returns the same data")
    public void createAndFetchBooking() {
        Booking booking = TestDataBuilder.defaultBooking();

        BookingResponse created = BookingApi.create(booking)
                .then().statusCode(200)
                .extract().as(BookingResponse.class);

        Assert.assertTrue(created.getBookingid() > 0, "booking id should be generated");
        Assert.assertEquals(created.getBooking().getFirstname(), booking.getFirstname());

        BookingApi.get(created.getBookingid())
                .then().statusCode(200)
                .body("firstname", equalTo(booking.getFirstname()))
                .body("lastname", equalTo(booking.getLastname()))
                .body("totalprice", equalTo(booking.getTotalprice()))
                .body("depositpaid", equalTo(booking.getDepositpaid()))
                .body("bookingdates.checkin", equalTo(booking.getBookingdates().getCheckin()))
                .body("bookingdates.checkout", equalTo(booking.getBookingdates().getCheckout()))
                .body("additionalneeds", equalTo(booking.getAdditionalneeds()));
    }

    @Test(description = "Full update with PUT")
    public void updateBookingWithPut() {
        int id = createBookingAndGetId();
        Booking updated = TestDataBuilder.booking("Updated", "Name", 999, false,
                "2027-01-01", "2027-01-07", "Dinner");

        BookingApi.update(id, updated, token)
                .then().statusCode(200)
                .body("firstname", equalTo("Updated"))
                .body("lastname", equalTo("Name"))
                .body("totalprice", equalTo(999))
                .body("depositpaid", equalTo(false));

        BookingApi.get(id).then().statusCode(200)
                .body("firstname", equalTo("Updated"))
                .body("additionalneeds", equalTo("Dinner"));
    }

    @Test(description = "Partial update with PATCH")
    public void partialUpdateWithPatch() {
        Booking original = TestDataBuilder.defaultBooking();
        int id = BookingApi.create(original).then().statusCode(200).extract().path("bookingid");

        BookingApi.partialUpdate(id, Map.of("firstname", "Patched", "totalprice", 1234), token)
                .then().statusCode(200)
                .body("firstname", equalTo("Patched"))
                .body("totalprice", equalTo(1234))
                // fields not sent must stay unchanged
                .body("lastname", equalTo(original.getLastname()));
    }

    @Test(description = "Delete a booking and confirm it is gone")
    public void deleteBooking() {
        int id = createBookingAndGetId();

        // Restful-Booker returns 201 Created for a successful delete
        BookingApi.delete(id, token).then().statusCode(201);

        BookingApi.get(id).then().statusCode(404);
    }

    @Test(description = "Booking ids list is returned")
    public void getAllBookingIds() {
        BookingApi.getAllIds().then().statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].bookingid", notNullValue());
    }

    @Test(description = "Search bookings by firstname")
    public void searchByFirstname() {
        Booking booking = TestDataBuilder.booking("Zephyrine", "Searchable", 300, true,
                "2026-11-01", "2026-11-02", "None");
        int id = BookingApi.create(booking).then().statusCode(200).extract().path("bookingid");

        BookingApi.searchByFirstname("Zephyrine").then().statusCode(200)
                .body("bookingid", hasItem(id));
    }

    private int createBookingAndGetId() {
        return BookingApi.create(TestDataBuilder.randomBooking())
                .then().statusCode(200).extract().path("bookingid");
    }
}
