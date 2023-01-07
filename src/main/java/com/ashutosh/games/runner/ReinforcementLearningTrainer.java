package com.ashutosh.games.runner;

import com.ashutosh.games.player.*;

import static com.ashutosh.games.runner.RunnerUtils.runIterations;

public class ReinforcementLearningTrainer {

    public static void main(String[] args) {

        ReinforcementLearningBot p1 = new ReinforcementLearningBot("RL bot 1");
        ReinforcementLearningBot p2 = new ReinforcementLearningBot("RL bot 2");

        for (int i = 0; i < 10000; i++) {
            runIterations(p1, p2, 100);
            System.out.println(String.format("P1 stats: Epsilon: %f, Exploration: %d, Exploitation %d, states size: %d",
                    p1.getEpsilon(), p1.exploration, p1.exploitation, p1.getStateValueMap().size()));
            System.out.println(String.format("P2 stats: Epsilon: %f, Exploration: %d, Exploitation %d, states size: %d",
                    p2.getEpsilon(), p2.exploration, p2.exploitation, p2.getStateValueMap().size()));
        }

        // After Learning Play this bot with all other bots
        Player p3 = new RandomBot("RandomBot");
        Player p4 = new OneLayerBot("OneLayerBot");
        Player p5 = new TwoLayerBot("TwoLayerBot");
        Player p6 = new MinMaxBot("MinMaxBot");

        // Vs Random Bot
        runIterations(p1, p3, 100);

        // Vs OneLayerBot
        runIterations(p1, p4, 100);

        // Vs TwoLayerBot
        runIterations(p1, p5, 100);

        // Vs MinMaxBot
        runIterations(p1, p6, 100);
    }
}
