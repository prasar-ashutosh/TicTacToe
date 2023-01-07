package com.ashutosh.games.web.model;

import com.ashutosh.games.game.Marker;
import lombok.Data;

@Data
public class ClientMessage {

    private ClientMessageType messageType;
    private Marker marker;
    private int index;
    private String message;
    private String gameId;
    private GameType gameType;
}