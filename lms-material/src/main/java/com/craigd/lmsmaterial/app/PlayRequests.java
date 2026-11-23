package com.craigd.lmsmaterial.app;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Plays that may wait (for LMS's player list, a search, the player status or the player app to start). Each play
 * gets the next number and the player it is for when it arrives; Pause, a newer play or another player being
 * selected moves the number on. Just before it is sent, a play is checked again: if it is no longer the latest
 * request, or another player is active now, it is dropped.
 */
final class PlayRequests {
    static final class Request {
        final int number;
        final String playerId;

        Request(int number, String playerId) {
            this.number = number;
            this.playerId = playerId;
        }
    }

    private final AtomicInteger counter = new AtomicInteger();
    private final AtomicInteger cancels = new AtomicInteger();

    // A new play for playerId: any earlier play still waiting is no longer wanted.
    Request next(String playerId) {
        return new Request(counter.incrementAndGet(), playerId);
    }

    // Pause, or another player selected: no waiting play is wanted any more.
    void cancel() {
        counter.incrementAndGet();
        cancels.incrementAndGet();
    }

    // Changes on every cancel() only (not on a new play): whether a Pause or a player change came since.
    int cancelCount() {
        return cancels.get();
    }

    boolean isWanted(Request request, String activePlayer) {
        return null!=request && request.number==counter.get() && Objects.equals(request.playerId, activePlayer);
    }
}
