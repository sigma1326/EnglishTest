package com.simorgh.database;

import com.simorgh.database.model.TestLog;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testLog() {
        TestLog testLog = new TestLog(1397, 0, new Date(2019, 0, 25, 12, 0, 0), 25, 0, 0);

        System.out.println(testLog.getPercent());
    }
}