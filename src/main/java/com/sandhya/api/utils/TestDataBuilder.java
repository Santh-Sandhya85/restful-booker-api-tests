package com.sandhya.api.utils;

import com.sandhya.api.models.Booking;
import com.sandhya.api.models.BookingDates;

import java.util.concurrent.ThreadLocalRandom;

public final class TestDataBuilder {

    private TestDataBuilder() {
    }

    public static Booking defaultBooking() {
        return booking("Sandhya", "Santh", 500, true, "2026-11-01", "2026-11-05", "Breakfast");
    }

    public static Booking randomBooking() {
        String[] first = {"Asha", "Ravi", "Meena", "Kiran", "Priya", "Arjun"};
        String[] last = {"Reddy", "Sharma", "Iyer", "Rao", "Khan", "Das"};
        ThreadLocalRandom r = ThreadLocalRandom.current();
        return booking(first[r.nextInt(first.length)], last[r.nextInt(last.length)],
                r.nextInt(100, 2000), r.nextBoolean(), "2026-12-01", "2026-12-10", "Late checkout");
    }

    public static Booking booking(String firstname, String lastname, int price, boolean deposit,
                                  String checkin, String checkout, String needs) {
        return new Booking(firstname, lastname, price, deposit, new BookingDates(checkin, checkout), needs);
    }
}
