package com.ashutosh.games.runner;

import com.ashutosh.games.game.GameResult;
import com.ashutosh.games.player.ConsolePlayer;
import com.ashutosh.games.player.MinMaxBot;
import com.ashutosh.games.player.Player;

public class MinMaxBotVsConsolePlayer {

    public static void main(String[] args) {
        Player p1 = new MinMaxBot("MinMaxBot");
        Player p2 = new ConsolePlayer("You");

        Player playerX = RunnerUtils.choosePlayerX(p1, p2);
        Player playerO = playerX == p1 ? p2 : p1;

        PlayerVsPlayerRunner runner = new PlayerVsPlayerRunner(playerX, playerO);
        GameResult result = runner.play();
        System.out.println("---------------------------------------------");
        System.out.println("Result: " + result);
    }
}
