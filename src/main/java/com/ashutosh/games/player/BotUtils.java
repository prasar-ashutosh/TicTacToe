package com.ashutosh.games.player;

import com.ashutosh.games.game.GameUtils;
import com.ashutosh.games.game.Marker;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class BotUtils {

    public static int getNextRandomMove(Marker[] board) {
        List<Integer> emptySlots = getEmptySlots(board);
        Random random = new Random();
        int randomIndex = random.nextInt(emptySlots.size());
        return emptySlots.get(randomIndex);
    }

    public static List<Integer> getEmptySlots(Marker[] board) {
        List<Integer> slots = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (board[i] == null) {
                slots.add(i);
            }
        }
        return slots;
    }

    public static Optional<Integer> findPossibleWinInNextMove(Marker[] board, Marker marker) {
        for (int[] winningLine : GameUtils.getWinningLines()) {
            int countMarker = 0;
            int countNull = 0;
            int winningIndex = 0;
            if (marker == board[winningLine[0]]) countMarker++;
            if (marker == board[winningLine[1]]) countMarker++;
            if (marker == board[winningLine[2]]) countMarker++;

            if (board[winningLine[0]] == null) {
                countNull++;
                winningIndex = winningLine[0];
            }
            if (board[winningLine[1]] == null) {
                countNull++;
                winningIndex = winningLine[1];
            }
            if (board[winningLine[2]] == null) {
                countNull++;
                winningIndex = winningLine[2];
            }
            if (countMarker == 2 && countNull == 1) {
                // Found the winning slot
                return Optional.of(winningIndex);
            }
        }
        return Optional.empty();
    }


}
