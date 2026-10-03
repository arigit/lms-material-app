package com.craigd.lmsmaterial.app;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

final class PlayerAvailability {
    static final class Player {
        final String id;
        final String name;
        final boolean connected;

        Player(String id, String name, boolean connected) {
            this.id = id;
            this.name = name;
            this.connected = connected;
        }
    }

    private PlayerAvailability() {
    }

    static Player findConnectedPlayer(JSONArray players, String playerId) {
        if (players == null || playerId == null || playerId.isEmpty()) {
            return null;
        }
        List<Player> availablePlayers = new ArrayList<>();
        for (int i = 0; i < players.length(); i++) {
            JSONObject player = players.optJSONObject(i);
            if (player != null) {
                availablePlayers.add(new Player(player.optString("playerid"),
                        player.optString("name"), player.optInt("connected", 0) == 1));
            }
        }
        return findConnectedPlayer(availablePlayers, playerId);
    }

    static Player findConnectedPlayer(List<Player> players, String playerId) {
        if (players == null || playerId == null || playerId.isEmpty()) {
            return null;
        }
        for (Player player : players) {
            if (player.connected && playerId.equals(player.id)) {
                return player;
            }
        }
        return null;
    }
}
