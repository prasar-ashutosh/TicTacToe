package com.ashutosh.games.player;

import com.ashutosh.games.game.Marker;

import java.util.Optional;

public class OneLayerBot extends AbstractBot {

    public OneLayerBot(String name) {
        super(name);
    }

    /*
    1. Pick up a slot that lets the bot win in the next move
    2. Else Pick up a random available slot
    */
    @Override
    public int getNextMove(Marker[] board) {
        Optional<Integer> winInNextMove = BotUtils.findPossibleWinInNextMove(board, getMarker());
        if(winInNextMove.isPresent()) {
            return winInNextMove.get();
        }
        return BotUtils.getNextRandomMove(board);
    }
}
