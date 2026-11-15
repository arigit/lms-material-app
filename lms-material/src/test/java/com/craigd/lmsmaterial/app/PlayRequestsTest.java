package com.craigd.lmsmaterial.app;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PlayRequestsTest {
    @Test
    public void aPlayIsWantedUntilPausedReplacedOrAnotherPlayerIsActive() {
        PlayRequests requests = new PlayRequests();
        PlayRequests.Request play = requests.next("phone");
        assertTrue(requests.isWanted(play, "phone"));
        assertFalse(requests.isWanted(play, "kitchen"));
        assertFalse(requests.isWanted(play, null));

        PlayRequests.Request newer = requests.next("phone");
        assertFalse(requests.isWanted(play, "phone"));
        assertTrue(requests.isWanted(newer, "phone"));

        requests.cancel();
        assertFalse(requests.isWanted(newer, "phone"));
        assertFalse(requests.isWanted(null, "phone"));
    }

    @Test
    public void aPauseBeforeAQueuedPlayStartsStillDropsIt() {
        // The number is taken when the play arrives, not when the busy browse executor gets to it.
        PlayRequests requests = new PlayRequests();
        PlayRequests.Request play = requests.next("phone");
        requests.cancel(); // Pause, while the play waits in the executor's queue
        assertFalse(requests.isWanted(play, "phone"));
    }
}
