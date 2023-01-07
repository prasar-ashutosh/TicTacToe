package com.ashutosh.games.game;

import lombok.Getter;

import java.util.Optional;

/*
2 player Tic Tac Toe Game
*/
@Getter
public class TicTacToe {
    private static int boardSize = 3 * 3;
    private Marker[] board;
    private Marker nextTurn;
    private Optional<GameResult> result;
    private Optional<int[]> winningIndexes;
    private GameState gameState;

    public TicTacToe() {
        this.board = new Marker[boardSize];
        this.gameState = GameState.INIT;
        this.nextTurn = Marker.X;
    }

    public TicTacToe(Marker[] board, Marker nextTurn) {
        this.board = board;
        this.gameState = GameState.IN_PROGRESS;
        this.nextTurn = nextTurn;
    }

    public void play(int index, Marker marker) {
        validate(index, marker);
        if (gameState == GameState.INIT) {
            gameState = GameState.IN_PROGRESS;
        }
        board[index] = marker;
        nextTurn = marker == Marker.X ? Marker.O : Marker.X;
        updateGameState();
    }

    private void validate(int index, Marker marker) {
        // Check if game is completed
        if (gameState == GameState.COMPLETED) {
            throw new IllegalStateException(String.format("Game is already in completed state"));
        }
        // Check if the coordinates are valid
        if (index < 0 || index >= boardSize) {
            throw new IllegalStateException(String.format("Player %s has selected an invalid Position (%d)", marker, index));
        }
        // Check if the coordinate is already occupied
        if (board[index] != null) {
            throw new IllegalStateException(String.format("Player %s has selected an occupied Position (%d)", marker, index));
        }
        // Check if the prev marker is same as current marker
        if (nextTurn != marker) {
            throw new IllegalStateException("Please wait for your turn. It's the turn of your opponent");
        }
    }

    public void updateGameState() {
        int tieLines = 0;
        for (int[] winningLine : GameUtils.getWinningLines()) {
            LineState lineState = getLineState(board[winningLine[0]], board[winningLine[1]], board[winningLine[2]]);
            if (lineState == LineState.X_WIN) {
                gameState = GameState.COMPLETED;
                result = Optional.of(GameResult.X_WIN);
                winningIndexes = Optional.of(winningLine);
                break;
            } else if (lineState == LineState.O_WIN) {
                gameState = GameState.COMPLETED;
                result = Optional.of(GameResult.O_WIN);
                winningIndexes = Optional.of(winningLine);
                break;
            } else if (lineState == LineState.TIE) {
                tieLines++;
            }
        }
        if (tieLines == 8) {
            gameState = GameState.COMPLETED;
            result = Optional.of(GameResult.TIE);
        }
    }

    private LineState getLineState(Marker m1, Marker m2, Marker m3) {
        LineState lineState = LineState.OPEN;
        int countX = 0;
        int countO = 0;
        if (Marker.X == m1) countX++;
        if (Marker.X == m2) countX++;
        if (Marker.X == m3) countX++;
        if (Marker.O == m1) countO++;
        if (Marker.O == m2) countO++;
        if (Marker.O == m3) countO++;

        if (countX == 3) {
            lineState = LineState.X_WIN;
        } else if (countO == 3) {
            lineState = LineState.O_WIN;
        } else if (countX > 0 && countO > 0) {
            lineState = LineState.TIE;
        }
        return lineState;
    }
}
