package com.ashutosh.games.player;

import com.ashutosh.games.game.Marker;

import java.util.Scanner;

public class ConsolePlayer extends AbstractBot {

    public ConsolePlayer(String name) {
        super(name);
    }

    @Override
    public void gameStart(Marker marker) {
        super.gameStart(marker);
        System.out.println("Game Start! You are playing with :" + marker);
    }

    @Override
    public int getNextMove(Marker[] board) {
        System.out.println("Your turn, Enter the index between 0 - 8: ");
        Scanner in = new Scanner(System.in);
        int index = in.nextInt();
        return index;
    }

}
