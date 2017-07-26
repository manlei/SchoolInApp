package cn.edu.nju.cs.seg.schooledinapp.util;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.Map;

import static org.junit.Assert.*;


public class JsonRequestBodyBuilderTest {

    private static final int TEST_COUNT = 10;

    private JsonRequestBodyBuilder builder;

    @Before
    public void setUp() throws Exception {
        builder = new JsonRequestBodyBuilder();
    }

    @Test
    public void count_isOk() throws Exception {
        for (int i = 0; i < TEST_COUNT; i ++) {
            builder.append(Integer.toString(i), new Object());
        }
        assertEquals(builder.count(), TEST_COUNT);
    }

    @Test
    public void append_isOk() throws Exception {
        for (int i = 0; i < TEST_COUNT; i ++) {
            builder.append(Integer.toString(i), new Integer(i));
        }

        Date d = new Date();
        builder.append("Date", d);

        Map<String, Object> buffer = builder.getBuffer();

        for (int i = 0; i < TEST_COUNT; i ++) {
            assertTrue(buffer.containsKey(Integer.toString(i)));
            assertEquals(buffer.get(Integer.toString(i)), new Integer(i));
        }

        assertTrue(buffer.containsKey("Date"));
        assertEquals(buffer.get("Date"), d);
    }

}