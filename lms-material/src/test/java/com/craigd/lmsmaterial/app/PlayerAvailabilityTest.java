package com.craigd.lmsmaterial.app;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PlayerAvailabilityTest {
    @Test
    public void findsTheSavedPlayerOnlyAfterItConnects() {
        List<PlayerAvailability.Player> players = new ArrayList<>(Arrays.asList(
                new PlayerAvailability.Player("00:11:22:33:44:55", "Phone", false),
                new PlayerAvailability.Player("aa:bb:cc:dd:ee:ff", "Kitchen", true)));

        assertNull(PlayerAvailability.findConnectedPlayer(players, "00:11:22:33:44:55"));

        players.set(0, new PlayerAvailability.Player("00:11:22:33:44:55", "Phone", true));
        PlayerAvailability.Player player = PlayerAvailability.findConnectedPlayer(
                players, "00:11:22:33:44:55");

        assertEquals("00:11:22:33:44:55", player.id);
        assertEquals("Phone", player.name);
    }
}
