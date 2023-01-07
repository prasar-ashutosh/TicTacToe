package com.ashutosh.games.player;

import com.ashutosh.games.game.GameResult;
import com.ashutosh.games.game.Marker;

public interface Player {

    String getName();

    void gameStart(Marker marker);

    void gameOver(GameResult result);

    Marker getMarker();

    int getNextMove(Marker [] board);
}
