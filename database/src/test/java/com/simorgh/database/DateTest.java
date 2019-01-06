package com.simorgh.database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DateTest {
    private Date date;

    @Before
    public void setUp() {
        date = new Date(2019, 0, 6, 5, 6);
    }

    @After
    public void tearDown() {
        System.out.println(date);
    }

    @Test
    public void getMilli() {
        assertEquals(201900060506L, date.getMilli());
    }

}