package com.ashutosh.games.player;

import com.ashutosh.games.game.Marker;

import java.util.Optional;

public class TwoLayerBot extends AbstractBot {

    public TwoLayerBot(String name) {
        super(name);
    }

    /*
    1. Pick up a slot that lets the bot win in the next move
    2. Else Pick up a slot that prevents the next player to win in the next move
    3. Else Pick up a random available slot
    */
    @Override
    public int getNextMove(Marker[] board) {
        Marker marker = getMarker();
        Optional<Integer> winInNextMove = BotUtils.findPossibleWinInNextMove(board, marker);
        if(winInNextMove.isPresent()) {
            return winInNextMove.get();
        }

        Marker opponentMarker = marker == Marker.X ? Marker.O : Marker.X;
        Optional<Integer> opponentWinInNextMove = BotUtils.findPossibleWinInNextMove(board, opponentMarker);
        if(opponentWinInNextMove.isPresent()) {
            return opponentWinInNextMove.get();
        }

        return BotUtils.getNextRandomMove(board);
    }
}
