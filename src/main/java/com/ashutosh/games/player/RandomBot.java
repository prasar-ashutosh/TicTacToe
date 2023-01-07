package com.ashutosh.games.player;

import com.ashutosh.games.game.Marker;

public class RandomBot extends AbstractBot {

    public RandomBot(String name) {
        super(name);
    }

    /*
      Pick up a random available slot
    */
    @Override
    public int getNextMove(Marker[] board) {
        return BotUtils.getNextRandomMove(board);
    }
}
