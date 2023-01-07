package com.ashutosh.games.pool;

import com.ashutosh.games.web.TicTacToeGameEndpoint;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class PlayersPool {

    public static final Set<TicTacToeGameEndpoint> activePlayersSet = new CopyOnWriteArraySet<>();

    private static HashMap<String, String> onlineUsersMap = new HashMap<>();

    public static String getUser(String sessionId) {
        return onlineUsersMap.get(sessionId);
    }

    public static String addUser(String sessionId, String user) {
        return onlineUsersMap.put(sessionId, user);
    }

    public static String removeUser(String sessionId) {
        return onlineUsersMap.remove(sessionId);
    }
}
