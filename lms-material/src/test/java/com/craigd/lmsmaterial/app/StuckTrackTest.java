package com.craigd.lmsmaterial.app;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StuckTrackTest {
    private static final long DURATION = 162000;
    private static final long STUCK = StuckTrack.STUCK_MS;

    @Test
    public void aReadAtTheEndOfTheSameTrackForAMinuteMovesOnOnce() {
        StuckTrack stuck = new StuckTrack();
        assertFalse(stuck.check("phone", 6, 5, true, DURATION, DURATION, true, 0));
        assertEquals(STUCK, stuck.msUntilCheck(0));
        assertFalse(stuck.check("phone", 6, 5, true, DURATION, DURATION, false, STUCK / 2));
        assertTrue(stuck.check("phone", 6, 5, true, DURATION, DURATION, false, STUCK));
        // once per stuck track
        assertFalse(stuck.check("phone", 6, 5, true, DURATION, DURATION, false, 3 * STUCK));
        assertEquals(-1, stuck.msUntilCheck(3 * STUCK));
    }

    @Test
    public void notWhileLmsIsOnlyAheadOfTheAudio() {
        // LMS can run up to 50 s ahead of the audio (player clock stopped while the phone slept): the track still
        // ends within that, and its STMs moves LMS on before the minute is up.
        StuckTrack stuck = new StuckTrack();
        stuck.check("phone", 6, 5, true, DURATION, DURATION, false, 0);
        assertFalse(stuck.check("phone", 6, 5, true, DURATION, DURATION, false, 50000));
        assertFalse(stuck.check("phone", 7, 6, true, 2000, 186000, false, 51000));
    }

    @Test
    public void pushesAloneDontCount() {
        // LMS pushes an extrapolated end-of-track position after an unpause and may not push the correction.
        StuckTrack stuck = new StuckTrack();
        stuck.check("phone", 6, 5, true, DURATION, DURATION, true, 0);
        assertFalse(stuck.check("phone", 6, 5, true, DURATION, DURATION, true, 2 * STUCK));
        assertTrue(stuck.check("phone", 6, 5, true, DURATION, DURATION, false, 2 * STUCK + 1000));
    }

    @Test
    public void aPositionBeforeTheEndPauseOrNewTrackStartsAgain() {
        StuckTrack stuck = new StuckTrack();
        stuck.check("phone", 6, 5, true, DURATION, DURATION, false, 0);
        stuck.check("phone", 6, 5, true, 30000, DURATION, false, STUCK - 5000); // corrected by a read
        long t = STUCK - 4000;
        stuck.check("phone", 6, 5, true, DURATION, DURATION, false, t);
        assertFalse(stuck.check("phone", 6, 5, true, DURATION, DURATION, false, t + STUCK - 1));
        assertTrue(stuck.check("phone", 6, 5, true, DURATION, DURATION, false, t + STUCK));

        t += 2 * STUCK;
        stuck.check("phone", 7, 6, true, 186000, 186000, false, t);
        stuck.check("phone", 7, 6, false, 186000, 186000, false, t + 10000); // paused
        assertFalse(stuck.check("phone", 7, 6, true, 186000, 186000, false, t + 20000));
        assertTrue(stuck.check("phone", 7, 6, true, 186000, 186000, false, t + 20000 + STUCK));

        // the same track at another queue index is another track
        t += 3 * STUCK;
        stuck.check("phone", 7, 6, true, 186000, 186000, false, t);
        assertFalse(stuck.check("phone", 7, 9, true, 186000, 186000, false, t + STUCK));
    }

    @Test
    public void notForRemoteTracksOrUnknownDurations() {
        StuckTrack stuck = new StuckTrack();
        stuck.check("phone", -3, 0, true, 60000, 60000, false, 0);
        assertFalse(stuck.check("phone", -3, 0, true, 60000, 60000, false, 3 * STUCK));
        stuck.check("phone", 6, 0, true, 0, 0, false, 0);
        assertFalse(stuck.check("phone", 6, 0, true, 0, 0, false, 3 * STUCK));
        assertEquals(-1, stuck.msUntilCheck(3 * STUCK));
    }
}
