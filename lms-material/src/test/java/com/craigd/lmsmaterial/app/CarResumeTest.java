package com.craigd.lmsmaterial.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CarResumeTest {
    @Test
    public void serializeAndParse() {
        CarResume resume = CarResume.parse(new CarResume("cd:ef:d8:ee:07:69", 1234, 5, 61500).serialize());
        assertEquals("cd:ef:d8:ee:07:69", resume.player);
        assertEquals(1234, resume.trackId);
        assertEquals(5, resume.index);
        assertEquals(61500, resume.time);
    }

    @Test
    public void parseInvalid() {
        assertNull(CarResume.parse(null));
        assertNull(CarResume.parse(""));
        assertNull(CarResume.parse("p|1|2"));
        assertNull(CarResume.parse("p|x|2|3"));
        assertNull(CarResume.parse("p|0|2|3"));
        assertNull(CarResume.parse("p|1|-1|3"));
    }

    @Test
    public void findIndexAtSavedIndex() {
        assertEquals(2, CarResume.findIndex(new long[]{7, 8, 9, 9}, 9, 2));
    }

    @Test
    public void findIndexNearestToSavedIndex() {
        assertEquals(3, CarResume.findIndex(new long[]{9, 1, 2, 9, 5, 6}, 9, 5));
        assertEquals(1, CarResume.findIndex(new long[]{1, 9, 2, 3}, 9, 0));
    }

    @Test
    public void findIndexGone() {
        assertEquals(-1, CarResume.findIndex(new long[]{1, 2, 3}, 9, 1));
        assertEquals(-1, CarResume.findIndex(new long[0], 9, 0));
        assertEquals(-1, CarResume.findIndex(null, 9, 0));
    }
}
