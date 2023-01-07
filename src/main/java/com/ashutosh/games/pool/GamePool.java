package com.ashutosh.games.pool;

import com.ashutosh.games.game.GameState;
import com.ashutosh.games.game.Marker;
import com.ashutosh.games.game.TicTacToe;
import com.ashutosh.games.player.Player;
import com.ashutosh.games.player.WebUser;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class GamePool {

    private static AtomicInteger gameId = new AtomicInteger(1);

    public static String generateGameId() {
        return String.valueOf(gameId.getAndAdd(1));
    }

    private static Map<String, TicTacToeSession> gamePool = new HashMap<>();

    public static TicTacToeSession startGame(String gameId, Session playerXSession, String user) {
        TicTacToeSession ticTacToeSession = new TicTacToeSession();
        ticTacToeSession.setTicTacToe(new TicTacToe());
        ticTacToeSession.setPlayerXSession(playerXSession);
        WebUser userX = new WebUser(user);
        userX.gameStart(Marker.X);
        ticTacToeSession.setPlayerX(userX);
        gamePool.put(gameId, ticTacToeSession);
        return ticTacToeSession;
    }

    public static TicTacToeSession startBotGame(String gameId, Session playerSession, Player webUser, Player botUser) {
        TicTacToeSession ticTacToeSession = new TicTacToeSession();
        ticTacToeSession.setTicTacToe(new TicTacToe());
        if(webUser.getMarker() == Marker.X) {
            ticTacToeSession.setPlayerXSession(playerSession);
            ticTacToeSession.setPlayerX(webUser);
            ticTacToeSession.setPlayerO(botUser);
        } else {
            ticTacToeSession.setPlayerOSession(playerSession);
            ticTacToeSession.setPlayerX(botUser);
            ticTacToeSession.setPlayerO(webUser);
        }
        gamePool.put(gameId, ticTacToeSession);
        return ticTacToeSession;
    }

    public static TicTacToeSession joinGame(String gameId, Session playerOSession, String user) {
        // TODO Handle invalid game id
        TicTacToeSession ticTacToeSession = gamePool.get(gameId);
        if (ticTacToeSession == null) {
            throw new IllegalStateException("Game id does not exist");
        }
        if (ticTacToeSession.getTicTacToe().getGameState() != GameState.INIT) {
            throw new IllegalStateException("Can not join this game, it has already started or finished");
        }
        if (ticTacToeSession.getPlayerOSession() != null) {
            throw new IllegalStateException("Can not join this game, no place left to join");
        }
        ticTacToeSession.setPlayerOSession(playerOSession);
        WebUser userO = new WebUser(user);
        userO.gameStart(Marker.O);
        ticTacToeSession.setPlayerO(userO);
        return ticTacToeSession;
    }

    public static TicTacToeSession findGame(String gameId) {
        return gamePool.get(gameId);
    }
}
