package com.ashutosh.games.player;

import com.ashutosh.games.game.GameResult;
import com.ashutosh.games.game.GameState;
import com.ashutosh.games.game.Marker;
import com.ashutosh.games.game.TicTacToe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MinMaxBot extends AbstractBot {

    private class Move {
        int index;
        int score;

        public Move(int index, int score) {
            this.index = index;
            this.score = score;
        }

        public Move(int score) {
            this.score = score;
        }
    }

    public MinMaxBot(String name) {
        super(name);
    }

    private Marker opponentPlayer;

    @Override
    public void gameStart(Marker marker) {
        super.gameStart(marker);
        opponentPlayer = getMarker() == Marker.X ? Marker.O : Marker.X;
    }

    @Override
    public int getNextMove(Marker[] board) {
        // Make a copy of the board
        Marker[] boardCopy = Arrays.copyOf(board, board.length);
        Move bestMove = minMax(boardCopy, getMarker());
        return bestMove.index;
    }

    private Move minMax(Marker[] board, Marker currentPlayer) {
        TicTacToe ticTacToe = new TicTacToe(board, getMarker());
        ticTacToe.updateGameState();

        if (ticTacToe.getGameState() == GameState.COMPLETED) {
            return getScore(ticTacToe);
        }

        List<Move> moves = new ArrayList<>();
        List<Integer> emptySlots = BotUtils.getEmptySlots(board);
        // Populate the moves and scores for each empty slot
        for (int index : emptySlots) {
            // Set the board index with current player
            board[index] = currentPlayer;
            int score;
            if (currentPlayer == getMarker()) {
                score = minMax(board, opponentPlayer).score;
            } else {
                score = minMax(board, getMarker()).score;
            }

            moves.add(new Move(index, score));
            // Reset the board to empty
            board[index] = null;
        }

        // Keep a List of best moves to choose randomly
        List<Integer> bestMoves = new ArrayList<>();
        // If it's your turn then bestMove is the move with max score
        if (currentPlayer == getMarker()) {
            int maxScore = -10000;
            for (int i = 0; i < moves.size(); i++) {
                if (moves.get(i).score > maxScore) {
                    maxScore = moves.get(i).score;
                }
            }
            for (int i = 0; i < moves.size(); i++) {
                if(moves.get(i).score == maxScore) {
                    bestMoves.add(i);
                }
            }
        }
        // else the best move the move with least score
        else {
            int minScore = 10000;
            for (int i = 0; i < moves.size(); i++) {
                if (moves.get(i).score < minScore) {
                    minScore = moves.get(i).score;
                }
            }
            for (int i = 0; i < moves.size(); i++) {
                if(moves.get(i).score == minScore) {
                    bestMoves.add(i);
                }
            }
        }
        Random random = new Random();
        int bestMove = bestMoves.get(random.nextInt(bestMoves.size()));
        return moves.get(bestMove);
    }

    /*
    - If player wins, return 10 (MAX)
    - If opponent wins, return -10 (MIN)
    - if tie return 0
     */
    private Move getScore(TicTacToe ticTacToe) {
        if (getMarker() == Marker.X && ticTacToe.getResult().get() == GameResult.X_WIN) {
            return new Move(10);
        } else if (getMarker() == Marker.O && ticTacToe.getResult().get() == GameResult.O_WIN) {
            return new Move(10);
        } else if (ticTacToe.getResult().get() == GameResult.TIE) {
            return new Move(0);
        } else {
            return new Move(-10);
        }
    }


}
