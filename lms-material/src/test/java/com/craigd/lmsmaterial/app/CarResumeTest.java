package com.craigd.lmsmaterial.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

    private static final CarResume SAVED = new CarResume("p", 9, 2, 60000);

    @Test
    public void notMovedOn() {
        assertNull(CarResume.movedOn(SAVED, 2, 9, 60000));
        assertNull(CarResume.movedOn(SAVED, 2, 9, 0)); // restarted player, stopped at 0:00
        assertNull(CarResume.movedOn(SAVED, 2, 9, 60000 + CarResume.PLAYED_ON_MS)); // played on until paused
    }

    @Test
    public void movedOn() {
        assertNotNull(CarResume.movedOn(SAVED, 3, 10, 0)); // next track
        assertNotNull(CarResume.movedOn(SAVED, 3, 9, 60000)); // same track, queue changed
        assertNotNull(CarResume.movedOn(SAVED, 2, 10, 60000)); // another track at that index
        assertNotNull(CarResume.movedOn(SAVED, 2, 9, 60001 + CarResume.PLAYED_ON_MS));
    }

    @Test
    public void wasPlaying() {
        assertTrue(CarResume.wasPlaying("play", -1));
        assertTrue(CarResume.wasPlaying("pause", 47000)); // car switched off: paused before the disconnect
        assertTrue(CarResume.wasPlaying("pause", CarResume.PAUSED_BEFORE_DISCONNECT_MS));
    }

    @Test
    public void wasNotPlaying() {
        assertFalse(CarResume.wasPlaying("stop", 0)); // queue played to its end (2026-11-02), Stop, Power off
        assertFalse(CarResume.wasPlaying("stop", -1));
        assertFalse(CarResume.wasPlaying("pause", CarResume.PAUSED_BEFORE_DISCONNECT_MS + 1)); // paused a while
        assertFalse(CarResume.wasPlaying("pause", -1)); // not seen playing
        assertFalse(CarResume.wasPlaying(null, 0));
    }
}
