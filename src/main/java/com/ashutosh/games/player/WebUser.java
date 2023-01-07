package com.ashutosh.games.player;

import com.ashutosh.games.game.Marker;

public class WebUser extends AbstractBot {

    public WebUser(String name) {
        super(name);
    }

    @Override
    public int getNextMove(Marker[] board) {
        return 0;
    }
}
