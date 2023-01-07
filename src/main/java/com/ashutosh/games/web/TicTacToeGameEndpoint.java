package com.ashutosh.games.web;

import com.ashutosh.games.game.GameResult;
import com.ashutosh.games.game.GameState;
import com.ashutosh.games.game.Marker;
import com.ashutosh.games.game.TicTacToe;
import com.ashutosh.games.player.MinMaxBot;
import com.ashutosh.games.player.Player;
import com.ashutosh.games.player.WebUser;
import com.ashutosh.games.pool.GamePool;
import com.ashutosh.games.pool.PlayersPool;
import com.ashutosh.games.pool.TicTacToeSession;
import com.ashutosh.games.web.decoder.ClientMessageDecoder;
import com.ashutosh.games.web.decoder.ServerMessageDecoder;
import com.ashutosh.games.web.encoder.ClientMessageEncoder;
import com.ashutosh.games.web.encoder.ServerMessageEncoder;
import com.ashutosh.games.web.model.ClientMessage;
import com.ashutosh.games.web.model.GameType;
import com.ashutosh.games.web.model.ServerMessage;
import com.ashutosh.games.web.model.ServerMessageType;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(value = "/tic-tac-toe/{username}",
        decoders = {ClientMessageDecoder.class, ServerMessageDecoder.class},
        encoders = {ClientMessageEncoder.class, ServerMessageEncoder.class})
public class TicTacToeGameEndpoint {
    private Session session;

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException, EncodeException {
        System.out.println("New session invoked by " + username);
        this.session = session;
        PlayersPool.activePlayersSet.add(this);
        PlayersPool.addUser(session.getId(), username);

        ServerMessage message = ServerMessage.builder()
                .messageType(ServerMessageType.CONNECTED)
                .isActive(false)
                .build();
        session.getBasicRemote().sendObject(message);
    }

    @OnMessage
    public void onMessage(Session session, ClientMessage clientMessage) throws IOException, EncodeException {
        String user = PlayersPool.getUser(session.getId());
        System.out.println(String.format("New Message from [%s] : %s", user, clientMessage));
        switch (clientMessage.getMessageType()) {
            case CHAT:
                // TBD
                break;
            case START_GAME:
                if (clientMessage.getGameType() == GameType.MULTI_PLAYER) {
                    handleGameStart(session, user);
                } else if (clientMessage.getGameType() == GameType.BOT) {
                    handleBotGameStart(session, clientMessage, user);
                }
                break;
            case JOIN_GAME:
                handleJoinGame(session, clientMessage, user);
                break;
            case MOVE:
                handleGameMove(session, clientMessage, user);
        }
    }

    private void handleGameStart(Session session, String user) throws IOException, EncodeException {
        String gameId = GamePool.generateGameId();
        GamePool.startGame(gameId, session, user);
        ServerMessage message = ServerMessage.builder()
                .messageType(ServerMessageType.GAME_LAUNCHED)
                .isActive(false)
                .gameId(gameId)
                .build();
        session.getBasicRemote().sendObject(message);
    }

    private void handleBotGameStart(Session session, ClientMessage clientMessage, String user) throws IOException, EncodeException {
        String gameId = GamePool.generateGameId();
        Marker playerMarker = clientMessage.getMarker();
        Marker botMarker = playerMarker == Marker.X ? Marker.O : Marker.X;

        Player webUser = new WebUser(user);
        webUser.gameStart(playerMarker);
        Player botUser = new MinMaxBot("MIN_MAX");
        botUser.gameStart(botMarker);
        boolean isActive = (playerMarker == Marker.X);
        TicTacToeSession ticTacToeSession = GamePool.startBotGame(gameId, session, webUser, botUser);

        ServerMessage playerMessage = ServerMessage.builder()
                .messageType(ServerMessageType.GAME_STARTED)
                .gameId(gameId)
                .opponentName(botUser.getName())
                .isActive(isActive)
                .build();
        session.getBasicRemote().sendObject(playerMessage);

        // If it's bot turn , play the first move and update user
        if (botMarker == Marker.X) {
            invokeBot(ticTacToeSession, botUser, botMarker, gameId, session);
        }
    }

    private void invokeBot(TicTacToeSession ticTacToeSession, Player botUser, Marker marker, String gameId, Session session) throws EncodeException, IOException {
        TicTacToe game = ticTacToeSession.getTicTacToe();
        if (game.getGameState() != GameState.COMPLETED) {
            int index = botUser.getNextMove(game.getBoard());
            game.play(index, marker);
            sendMessageToOpponentPlayer(gameId, game, session, botUser.getName(), index);
        }
    }

    private void handleJoinGame(Session session, ClientMessage clientMessage, String userO) throws IOException, EncodeException {
        String gameId = clientMessage.getGameId();
        try {
            TicTacToeSession ticTacToeSession = GamePool.joinGame(gameId, session, userO);
            String userX = ticTacToeSession.getPlayerX().getName();

            ServerMessage playerXMessage = ServerMessage.builder()
                    .messageType(ServerMessageType.GAME_STARTED)
                    .gameId(gameId)
                    .opponentName(userO)
                    .isActive(true)
                    .build();
            ServerMessage playerOMessage = ServerMessage.builder()
                    .messageType(ServerMessageType.GAME_STARTED)
                    .opponentName(userX)
                    .isActive(false)
                    .gameId(gameId)
                    .build();
            session.getBasicRemote().sendObject(playerOMessage);
            ticTacToeSession.getPlayerXSession().getBasicRemote().sendObject(playerXMessage);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            ServerMessage errorMessage = ServerMessage.builder()
                    .messageType(ServerMessageType.GAME_JOIN_ERROR)
                    .gameId(gameId)
                    .message(e.getMessage())
                    .build();
            session.getBasicRemote().sendObject(errorMessage);
        }
    }

    private void handleGameMove(Session session, ClientMessage clientMessage, String user) throws IOException, EncodeException {
        String gameId = clientMessage.getGameId();
        int index = clientMessage.getIndex();
        Marker marker = clientMessage.getMarker();
        TicTacToeSession ticTacToeSession = GamePool.findGame(gameId);
        TicTacToe game = ticTacToeSession.getTicTacToe();
        Player opponentPlayer = marker == Marker.X ? ticTacToeSession.getPlayerO() :
                ticTacToeSession.getPlayerX();

        // Move the Game
        try {
            game.play(index, marker);
            // Send message to Current Player
            sendMessageToCurrentPlayer(session, gameId, game, opponentPlayer.getName(), index);
            if (clientMessage.getGameType() == GameType.MULTI_PLAYER) {
                // Send message to Opponent
                Session opponentSession = marker == Marker.X ?
                        ticTacToeSession.getPlayerOSession() : ticTacToeSession.getPlayerXSession();
                sendMessageToOpponentPlayer(gameId, game, opponentSession, user, index);
            } else if (clientMessage.getGameType() == GameType.BOT) {
                invokeBot(ticTacToeSession, opponentPlayer, opponentPlayer.getMarker(), gameId, session);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            // Send an Error Message to Player
            sendInvalidMove(session, gameId, game, e.getMessage());
        }
    }

    private void sendMessageToOpponentPlayer(String gameId, TicTacToe game, Session opponentSession, String opponentName, int index) throws IOException, EncodeException {
        boolean isActive = true;
        ServerMessageType messageType = ServerMessageType.GAME_UPDATE;
        sendMessage(opponentSession, gameId, game, opponentName, index, isActive, messageType);
    }

    private void sendMessageToCurrentPlayer(Session session, String gameId, TicTacToe game, String opponentName, int index) throws IOException, EncodeException {
        boolean isActive = false;
        ServerMessageType messageType = ServerMessageType.GAME_UPDATE;
        sendMessage(session, gameId, game, opponentName, index, isActive, messageType);
    }

    private void sendMessage(Session session, String gameId, TicTacToe game, String opponentName, int index, boolean isActive, ServerMessageType messageType) throws IOException, EncodeException {
        ServerMessage.ServerMessageBuilder serverMessageBuilder = ServerMessage.builder()
                .gameId(gameId)
                .board(game.getBoard())
                .opponentName(opponentName)
                .currentIndex(index);
        if (game.getGameState() == GameState.COMPLETED) {
            messageType = ServerMessageType.GAME_OVER;
            isActive = false;
            serverMessageBuilder.gameResult(game.getResult().get());
            if (game.getResult().get() != GameResult.TIE) {
                serverMessageBuilder.winningLineIndexes(game.getWinningIndexes().get());
            }
        }
        ServerMessage serverMessage = serverMessageBuilder
                .messageType(messageType)
                .isActive(isActive)
                .build();
        session.getBasicRemote().sendObject(serverMessage);
    }

    private void sendInvalidMove(Session session, String gameId, TicTacToe game, String message) throws IOException, EncodeException {
        ServerMessage errorMessage = ServerMessage.builder()
                .messageType(ServerMessageType.INVALID_MOVE)
                .gameId(gameId)
                .board(game.getBoard())
                .message(message)
                .build();
        session.getBasicRemote().sendObject(errorMessage);
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Session closed by " + PlayersPool.getUser(session.getId()));
        PlayersPool.activePlayersSet.remove(this);
        PlayersPool.removeUser(session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }

    private static void broadcast(ServerMessage message) {
        PlayersPool.activePlayersSet.forEach(endpoint -> {
            synchronized (endpoint) {
                try {
                    endpoint.session.getBasicRemote().sendObject(message);
                } catch (IOException | EncodeException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        });
    }
}
