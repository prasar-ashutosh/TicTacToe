package com.ashutosh.games.runner;

import com.ashutosh.games.game.GameResult;
import com.ashutosh.games.player.Player;

public class RunnerUtils {

    public static Player choosePlayerX(Player player1, Player player2) {
        if (Math.random() >= 0.5) {
            return player1;
        } else {
            return player2;
        }
    }

    public static void runIterations(Player p1, Player p2, int iterations) {
        int p1Wins = 0;
        int p2Wins = 0;
        int ties = 0;

        for (int i = 0; i < iterations; i++) {
            Player playerX = RunnerUtils.choosePlayerX(p1, p2);
            Player playerO = playerX == p1 ? p2 : p1;
            PlayerVsPlayerRunner runner = new PlayerVsPlayerRunner(playerX, playerO);
            GameResult result = runner.play();
            if (result == GameResult.X_WIN) {
                if (playerX == p1) {
                    p1Wins++;
                } else {
                    p2Wins++;
                }
            }
            if (result == GameResult.O_WIN) {
                if (playerO == p1) {
                    p1Wins++;
                } else {
                    p2Wins++;
                }
            }
            if (result == GameResult.TIE) ties++;
        }
        System.out.println("------------------------------------------");
        System.out.println(String.format("------%s Vs %s ------", p1.getName(), p2.getName()));
        System.out.println("------------------------------------------");
        System.out.println("P1 Wins :" + p1Wins);
        System.out.println("P2 Wins :" + p2Wins);
        System.out.println("Ties :" + ties);
        System.out.println("------------------------------------------");
    }
}
