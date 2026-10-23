/**
 * LMS-Material-App
 *
 * Copyright (c) 2020-2026 Craig Drummond <craig.p.drummond@gmail.com>
 * MIT license.
 */

package com.craigd.lmsmaterial.app;

/**
 * The play queue shown in Android Auto is a window of the LMS playlist starting just before the
 * current track. Android Auto always opens the queue at the top and apps can't scroll it, so this
 * keeps the playing track in view; it also keeps large queues within the media session's binder
 * size limit.
 */
public class PlayQueue {
    public static final int MAX_ITEMS = 100;
    public static final int ITEMS_BEFORE_CURRENT = 1;

    /** First playlist index of the window to show. */
    public static int windowStart(int currentIndex) {
        return Math.max(0, currentIndex - ITEMS_BEFORE_CURRENT);
    }
}
