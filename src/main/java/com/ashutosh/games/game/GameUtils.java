package com.ashutosh.games.game;

import java.util.ArrayList;
import java.util.List;

public class GameUtils {

    private static List<int[]> winningLines = new ArrayList<>();

    static {
        // Horizontal lines
        winningLines.add(new int[]{0, 1, 2});
        winningLines.add(new int[]{3, 4, 5});
        winningLines.add(new int[]{6, 7, 8});

        // Vertical lines
        winningLines.add(new int[]{0, 3, 6});
        winningLines.add(new int[]{1, 4, 7});
        winningLines.add(new int[]{2, 5, 8});

        // Diagonals
        winningLines.add(new int[]{0, 4, 8});
        winningLines.add(new int[]{2, 4, 6});
    }

    public static List<int[]> getWinningLines() {
        return winningLines;
    }
}
