package com.ashutosh.games.runner;

import com.ashutosh.games.game.GameResult;
import com.ashutosh.games.game.GameState;
import com.ashutosh.games.game.Marker;
import com.ashutosh.games.game.TicTacToe;
import com.ashutosh.games.player.Player;

public class PlayerVsPlayerRunner {

    private Player playerX;
    private Player playerO;

    public PlayerVsPlayerRunner(Player playerX, Player playerO) {
        this.playerX = playerX;
        this.playerO = playerO;
    }

    public GameResult play() {
        this.playerX.gameStart(Marker.X);
        this.playerO.gameStart(Marker.O);

        TicTacToe game = new TicTacToe();
        GameResult result = GameResult.TIE;
        for (int i = 0; i < 9; i++) {
            Marker marker = (i % 2 == 0) ? playerX.getMarker() : playerO.getMarker();
            int index = (i % 2 == 0) ?
                    playerX.getNextMove(game.getBoard()) :
                    playerO.getNextMove(game.getBoard());
            game.play(index, marker);
            //printBoard(game.getBoard());
            if (game.getGameState() == GameState.COMPLETED) {
                result = game.getResult().get();
                break;
            }
        }
        this.playerX.gameOver(result);
        this.playerO.gameOver(result);
        return result;
    }

    private void printBoard(Marker[] board) {
        System.out.println("------------------");
        System.out.println(String.format(" %s %s %s ", getValue(board[0]), getValue(board[1]), getValue(board[2])));
        System.out.println(String.format(" %s %s %s ", getValue(board[3]), getValue(board[4]), getValue(board[5])));
        System.out.println(String.format(" %s %s %s ", getValue(board[6]), getValue(board[7]), getValue(board[8])));
    }

    private String getValue(Marker marker) {
        return marker == null ? "." : marker.toString();
    }
}
