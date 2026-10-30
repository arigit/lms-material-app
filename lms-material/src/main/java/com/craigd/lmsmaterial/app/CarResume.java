/**
 * LMS-Material-App
 *
 * Copyright (c) 2020-2026 Craig Drummond <craig.p.drummond@gmail.com>
 * MIT license.
 */

package com.craigd.lmsmaterial.app;

import androidx.annotation.NonNull;

/**
 * What the active player was playing when the car disconnected ("Resume after reconnecting"), so that the track
 * and position can be restored on the next connect if the track is still in the queue.
 */
public class CarResume {
    public final String player;
    public final long trackId;
    public final int index; // playlist index of the track when saved
    public final long time; // position in ms

    public CarResume(String player, long trackId, int index, long time) {
        this.player = player;
        this.trackId = trackId;
        this.index = index;
        this.time = time;
    }

    public String serialize() {
        return player + "|" + trackId + "|" + index + "|" + time;
    }

    /** The saved state, or null if there is none or it can't be read. */
    public static CarResume parse(String value) {
        if (null==value) {
            return null;
        }
        String[] parts = value.split("\\|");
        if (4!=parts.length || parts[0].isEmpty()) {
            return null;
        }
        try {
            CarResume resume = new CarResume(parts[0], Long.parseLong(parts[1]), Integer.parseInt(parts[2]), Long.parseLong(parts[3]));
            return resume.trackId > 0 && resume.index >= 0 && resume.time >= 0 ? resume : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Where the track is in the queue (ids holds the track ID at each playlist index): the saved index if it still
     * holds the track, else the nearest index that does, or -1 if the track is no longer in the queue.
     */
    public static int findIndex(long[] ids, long trackId, int savedIndex) {
        if (null==ids || trackId <= 0) {
            return -1;
        }
        int found = -1;
        for (int i = 0; i < ids.length; i++) {
            if (ids[i] == trackId && (found < 0 || Math.abs(i - savedIndex) < Math.abs(found - savedIndex))) {
                found = i;
            }
        }
        return found;
    }

    @NonNull
    @Override
    public String toString() {
        return "player:" + player + ", trackId:" + trackId + ", index:" + index + ", time:" + Utils.timeStr(time);
    }
}
