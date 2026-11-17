package com.craigd.lmsmaterial.app;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StuckTrackTest {
    private static final long DURATION = 162000;

    @Test
    public void aReadAtTheEndOfTheSameTrackForTwentySecondsMovesOnOnce() {
        StuckTrack stuck = new StuckTrack();
        assertFalse(stuck.check("phone", 6, 5, true, DURATION, DURATION, true, 0));
        assertEquals(StuckTrack.STUCK_MS, stuck.msUntilCheck(0));
        assertFalse(stuck.check("phone", 6, 5, true, DURATION, DURATION, false, 10000));
        assertTrue(stuck.check("phone", 6, 5, true, DURATION, DURATION, false, 20000));
        // once per stuck track
        assertFalse(stuck.check("phone", 6, 5, true, DURATION, DURATION, false, 60000));
        assertEquals(-1, stuck.msUntilCheck(60000));
    }

    @Test
    public void pushesAloneDontCount() {
        // LMS pushes an extrapolated end-of-track position after an unpause and may not push the correction.
        StuckTrack stuck = new StuckTrack();
        stuck.check("phone", 6, 5, true, DURATION, DURATION, true, 0);
        assertFalse(stuck.check("phone", 6, 5, true, DURATION, DURATION, true, 60000));
        assertTrue(stuck.check("phone", 6, 5, true, DURATION, DURATION, false, 61000));
    }

    @Test
    public void aPositionBeforeTheEndPauseOrNewTrackStartsAgain() {
        StuckTrack stuck = new StuckTrack();
        stuck.check("phone", 6, 5, true, DURATION, DURATION, false, 0);
        stuck.check("phone", 6, 5, true, 30000, DURATION, false, 15000); // corrected by a read
        stuck.check("phone", 6, 5, true, DURATION, DURATION, false, 16000);
        assertFalse(stuck.check("phone", 6, 5, true, DURATION, DURATION, false, 30000));
        assertTrue(stuck.check("phone", 6, 5, true, DURATION, DURATION, false, 36000));

        stuck.check("phone", 7, 6, true, 186000, 186000, false, 40000);
        stuck.check("phone", 7, 6, false, 186000, 186000, false, 50000); // paused
        assertFalse(stuck.check("phone", 7, 6, true, 186000, 186000, false, 61000));
        assertTrue(stuck.check("phone", 7, 6, true, 186000, 186000, false, 81000));

        // the same track at another queue index is another track
        stuck.check("phone", 7, 6, true, 186000, 186000, false, 90000);
        assertFalse(stuck.check("phone", 7, 9, true, 186000, 186000, false, 115000));
    }

    @Test
    public void notForRemoteTracksOrUnknownDurations() {
        StuckTrack stuck = new StuckTrack();
        stuck.check("phone", -3, 0, true, 60000, 60000, false, 0);
        assertFalse(stuck.check("phone", -3, 0, true, 60000, 60000, false, 60000));
        stuck.check("phone", 6, 0, true, 0, 0, false, 0);
        assertFalse(stuck.check("phone", 6, 0, true, 0, 0, false, 60000));
        assertEquals(-1, stuck.msUntilCheck(60000));
    }
}
