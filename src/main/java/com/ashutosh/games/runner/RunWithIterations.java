package com.ashutosh.games.runner;

import com.ashutosh.games.player.OneLayerBot;
import com.ashutosh.games.player.Player;
import com.ashutosh.games.player.RandomBot;
import com.ashutosh.games.player.TwoLayerBot;

import static com.ashutosh.games.runner.RunnerUtils.runIterations;

public class RunWithIterations {

    public static void main(String[] args) {
        Player p1 = new RandomBot("RandomBot 1");
        Player p2 = new RandomBot("RandomBot 2");

        Player p3 = new OneLayerBot("OneLayerBot 1");
        Player p4 = new OneLayerBot("OneLayerBot 2");

        Player p5 = new TwoLayerBot("TwoLayerBot 1");
        Player p6 = new TwoLayerBot("TwoLayerBot 2");

        // RandomBot Vs RandomBot
        runIterations(p1, p2, 1000);

        // OneLayerBot Vs RandomBot
        runIterations(p3, p1,1000);

        // OneLayerBot Vs OneLayerBot
        runIterations(p3, p4, 1000);

        // TwoLayerBot Vs RandomBot
        runIterations(p5, p1, 1000);

        // TwoLayerBot Vs OneLayerBot
        runIterations(p5, p3, 1000);

        // TwoLayerBot Vs TwoLayerBot
        runIterations(p5, p6, 1000);
    }
}
