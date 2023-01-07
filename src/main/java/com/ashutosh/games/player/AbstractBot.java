package com.ashutosh.games.player;

import com.ashutosh.games.game.GameResult;
import com.ashutosh.games.game.Marker;

public abstract class AbstractBot implements Player {
    private String name;
    private Marker marker;

    public AbstractBot(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Marker getMarker() {
        return marker;
    }

    @Override
    public void gameStart(Marker marker) {
        this.marker = marker;
    }

    @Override
    public void gameOver(GameResult gameResult) {
        // Nothing to do
    }
}
