package com.ashutosh.games.web.model;

import com.ashutosh.games.game.GameResult;
import com.ashutosh.games.game.Marker;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServerMessage {

    private ServerMessageType messageType;
    private String message;
    private String opponentName;
    private boolean isActive;
    private String gameId;
    private Marker[] board;
    private GameResult gameResult;
    private int[] winningLineIndexes;
    private int currentIndex;
}