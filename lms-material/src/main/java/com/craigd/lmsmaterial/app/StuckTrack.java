/**
 * LMS Material App
 *
 * Copyright (c) 2020-2026 Craig Drummond <craig.p.drummond@gmail.com>
 * MIT license.
 */

package com.craigd.lmsmaterial.app;

import java.util.Objects;

/**
 * Spots a player that LMS reports as playing at the end of the same library track for good: the player didn't
 * report the next track's start, so LMS keeps waiting for it, and pause/play doesn't help.
 *
 * LMS also reports a position at the end of the track for a short while right after an unpause (it extrapolates
 * over the pause), and its pushes don't always bring the correction. So only status reads count, and only once the
 * position has stayed at the end of the same track (same queue index) for STUCK_MS. Library tracks only: a remote
 * stream's duration can be shorter than the stream. Main thread only.
 */
final class StuckTrack {
    static final long STUCK_MS = 20000;
    static final long AT_END_MS = 500;

    private String key;
    private long since;
    private boolean handled;

    /**
     * A status of the active player. Returns true once when it should be moved on to the next track.
     */
    boolean check(String playerId, long trackId, int playlistIndex, boolean playing, long time, long duration,
                  boolean pushed, long now) {
        if (!playing || trackId <= 0 || duration <= 0 || time < duration - AT_END_MS) {
            key = null;
            handled = false;
            return false;
        }
        String k = playerId + "|" + trackId + "|" + playlistIndex;
        if (!Objects.equals(k, key)) {
            key = k;
            since = now;
            handled = false;
            return false;
        }
        if (handled || pushed || now - since < STUCK_MS) {
            return false;
        }
        handled = true;
        return true;
    }

    /** How long until a read can confirm it, or -1 if nothing is waiting. */
    long msUntilCheck(long now) {
        if (null == key || handled) {
            return -1;
        }
        return Math.max(0, since + STUCK_MS - now);
    }

    long stuckFor(long now) {
        return null == key ? 0 : now - since;
    }
}
