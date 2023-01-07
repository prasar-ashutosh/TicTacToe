package com.ashutosh.games.runner;

import com.ashutosh.games.player.*;

import static com.ashutosh.games.runner.RunnerUtils.runIterations;

public class MinMaxRunnerTest {

    public static void main(String[] args) {
        Player p1 = new RandomBot("RandomBot");
        Player p2 = new OneLayerBot("OneLayerBot");
        Player p3 = new TwoLayerBot("TwoLayerBot");
        Player p4 = new MinMaxBot("MinMaxBot 1");
        Player p5 = new MinMaxBot("MinMaxBot 2");


        // MinMaxBot Vs RandomBot
        runIterations(p4, p1, 100);
        // MinMaxBot Vs OneLayerBot
        runIterations(p4, p2, 100);
        // MinMaxBot Vs TwoLayerBot
        runIterations(p4, p3, 100);
        // MinMaxBot Vs MinMaxBot
        runIterations(p4, p5, 100);
    }
}
