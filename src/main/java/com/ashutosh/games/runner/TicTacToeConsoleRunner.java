package com.ashutosh.games.runner;

import com.ashutosh.games.game.GameResult;
import com.ashutosh.games.game.GameState;
import com.ashutosh.games.game.Marker;
import com.ashutosh.games.game.TicTacToe;

import java.util.Scanner;

public class TicTacToeConsoleRunner {

    public static void main(String[] args) {
        TicTacToeConsoleRunner runner = new TicTacToeConsoleRunner();
        Scanner in = new Scanner(System.in);
        runner.startGame(in);
    }

    private void startGame(Scanner in) {
        printRules();
        TicTacToe ticTacToe = new TicTacToe();
        int iter = 0;
        while (true) {
            try {
                Marker marker = iter % 2 == 0 ? Marker.X : Marker.O;
                System.out.println(String.format("PLAYER : %s TURN", marker));
                int index = in.nextInt();

                ticTacToe.play(index, marker);
                printBoard(ticTacToe);

                GameState state = ticTacToe.getGameState();
                if (state == GameState.COMPLETED) {
                    GameResult result = ticTacToe.getResult().get();
                    if (result == GameResult.X_WIN || result == GameResult.O_WIN) {
                        System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
                        System.out.println("++++++ We have a winner. Result: " + state + " +++++");
                        System.out.println("++++++++++++++ Winning Line: " + getWinningLineValue(ticTacToe.getWinningIndexes().get()) + " ++++++++++++");
                        System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
                        System.exit(0);
                    }
                    if (result == GameResult.TIE) {
                        System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
                        System.out.println("+++++++++++++++ Match Tied +++++++++++++++++");
                        System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
                        System.exit(0);
                    }
                }
                iter++;
            } catch (Exception e) {
                System.out.println(String.format("Please try again: Some thing wrong %s ", e.getMessage()));
            }
        }
    }

    public void printBoard(TicTacToe ticTacToe) {
        Marker[] board = ticTacToe.getBoard();
        System.out.println(String.format(" %s | %s | %s ", getValue(board[0]), getValue(board[1]), getValue(board[2])));
        System.out.println("---|---|---");
        System.out.println(String.format(" %s | %s | %s ", getValue(board[3]), getValue(board[4]), getValue(board[5])));
        System.out.println("---|---|---");
        System.out.println(String.format(" %s | %s | %s ", getValue(board[6]), getValue(board[7]), getValue(board[8])));
    }

    private String getWinningLineValue(int[] line) {
        return String.format("%d, %d, %d", line[0], line[1], line[2]);
    }

    private String getValue(Marker marker) {
        return marker == null ? " " : marker.toString();
    }

    private void printRules() {
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("++++++++++++++++ Game Rules ++++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++");

        System.out.println("First player automatically gets marker X");
        System.out.println("Second player gets marker O");

        System.out.println("To play, just provide coordinates as shown below");
        System.out.println(" 0 | 1 | 2 ");
        System.out.println("---|---|---");
        System.out.println(" 3 | 4 | 5 ");
        System.out.println("---|---|---");
        System.out.println(" 6 | 7 | 8 ");

        System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println("+++++++++++++++ Let's start!! ++++++++++++++");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
    }

}
