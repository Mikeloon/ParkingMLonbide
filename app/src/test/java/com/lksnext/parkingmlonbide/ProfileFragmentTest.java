package com.lksnext.parkingmlonbide;

import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

import com.lksnext.parkingmlonbide.NavFragments.ProfileFragment;

public class ProfileFragmentTest {
    private ProfileFragment profileFragment;

    @Before
    public void setUp(){
        profileFragment = new ProfileFragment();
    }

    @Test
    public void testParseBookingDate() throws ParseException {
        String inputDate = "05/07/2023";
        String expectedOutput = "05/07/2023";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

        Date expectedDate = sdf.parse(expectedOutput);

        Date parsedDate = profileFragment.parseBookingDate(inputDate);

        assertEquals(expectedDate, parsedDate);
    }
}
