package com.craigd.lmsmaterial.app;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlayQueueTest {
    @Test
    public void startsJustBeforeCurrentTrack() {
        assertEquals(2, PlayQueue.windowStart(3));
        assertEquals(499, PlayQueue.windowStart(500));
        assertEquals(994, PlayQueue.windowStart(995));
    }

    @Test
    public void firstTrackStartsAtTop() {
        assertEquals(0, PlayQueue.windowStart(0));
        assertEquals(0, PlayQueue.windowStart(1));
    }
}
