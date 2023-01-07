package com.ashutosh.games.pool;

import com.ashutosh.games.game.TicTacToe;
import com.ashutosh.games.player.Player;
import lombok.Data;

import javax.websocket.Session;

@Data
public class TicTacToeSession {

    private TicTacToe ticTacToe;
    private Session playerXSession;
    private Session playerOSession;
    private Player playerX;
    private Player playerO;
}