package com.sandhya.api.tests;

import com.sandhya.api.base.BaseTest;
import com.sandhya.api.endpoints.BookingApi;
import com.sandhya.api.models.Booking;
import com.sandhya.api.utils.TestDataBuilder;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class DataDrivenBookingTests extends BaseTest {

    @DataProvider(name = "bookingData")
    public Object[][] bookingData() {
        return new Object[][]{
                {"Asha", "Reddy", 150, true, "2026-11-01", "2026-11-03", "Breakfast"},
                {"Ravi", "Sharma", 999, false, "2026-12-10", "2026-12-15", "Airport pickup"},
                {"Meena", "Iyer", 1, true, "2027-01-05", "2027-01-06", "None"},
                {"Kiran", "Rao", 5000, false, "2027-02-01", "2027-02-28", "Extra bed"}
        };
    }

    @Test(dataProvider = "bookingData", description = "Create bookings with multiple data sets")
    public void createBookingWithVariousData(String first, String last, int price, boolean deposit,
                                             String checkin, String checkout, String needs) {
        Booking booking = TestDataBuilder.booking(first, last, price, deposit, checkin, checkout, needs);

        int id = BookingApi.create(booking).then().statusCode(200)
                .body("booking.firstname", equalTo(first))
                .body("booking.totalprice", equalTo(price))
                .extract().path("bookingid");

        BookingApi.get(id).then().statusCode(200)
                .body("lastname", equalTo(last))
                .body("depositpaid", equalTo(deposit))
                .body("bookingdates.checkin", equalTo(checkin))
                .body("additionalneeds", equalTo(needs));
    }
}
