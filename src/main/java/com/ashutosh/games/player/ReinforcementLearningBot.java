package com.ashutosh.games.player;

import com.ashutosh.games.game.GameResult;
import com.ashutosh.games.game.Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReinforcementLearningBot extends AbstractBot {

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    private double epsilon = 1.0;

    private double learningRate = 0.1;

    public int exploration = 0;
    public int exploitation = 0;

    public ReinforcementLearningBot(String name) {
        super(name);
    }

    private Map<Integer, Double> stateValueMap = new HashMap<>();

    public double getEpsilon() {
        return epsilon;
    }

    public Map<Integer, Double> getStateValueMap() {
        return stateValueMap;
    }

    private List<Integer> states;

    @Override
    public void gameStart(Marker marker) {
        super.gameStart(marker);
        states = new ArrayList<>();
    }


    /*
    Initialize epsilon = 1
    Generate a random number between 0 and 1
    If this random number is < ε, explore the state space (take a random action).
    If this random number is > ε, perform the action that leads to the state with the highest value.
    Decrease ε by a small amount
    */

    @Override
    public int getNextMove(Marker[] board) {
        List<Integer> emptySlots = BotUtils.getEmptySlots(board);
        // Key is index, value is state
        Map<Integer, Integer> possibleNextStatesMap = new HashMap<>();
        double topValue = -1;
        int action = -1;

        for (int index : emptySlots) {
            // Update the board
            board[index] = getMarker();
            int stateHash = getStateHash(board);
            possibleNextStatesMap.put(index, stateHash);
            // Reset the board
            board[index] = null;
        }

        // Epsilon greedy: Perform ε greedy to either take a random action or be greedy(take the best action).
        if (Math.random() < epsilon) {
            exploration ++;
            action = BotUtils.getNextRandomMove(board);
        }
        // If greedy, loop through the next possible states and find the state with the highest value.
        else {
            for (Map.Entry<Integer, Integer> entry : possibleNextStatesMap.entrySet()) {
                int index = entry.getKey();
                int state = entry.getValue();
                if (stateValueMap.containsKey(state)) {
                    if (stateValueMap.get(state) > topValue) {
                        topValue = stateValueMap.get(state);
                        action = index;
                    }
                }
            }
            // If we did not find any action in valuesMap, set action as random action
            if (action == -1) {
                exploration++;
                action = BotUtils.getNextRandomMove(board);
            } else {
                exploitation++;
            }
        }
        // Reduce the epsilon
        if (epsilon > .00005) {
            epsilon -= .000001;
        }
        // Add the state to List of states
        states.add(possibleNextStatesMap.get(action));
        return action;
    }

    @Override
    public void gameOver(GameResult gameResult) {
        double reward;
        if ((gameResult == GameResult.X_WIN && getMarker() == Marker.X)
                || (gameResult == GameResult.O_WIN && getMarker() == Marker.O)) {
            reward = 1;
        } else if (gameResult == GameResult.TIE) {
            if (getMarker() == Marker.X) {
                reward = 0.1;
            } else {
                reward = 0.5;
            }
        } else {
            reward = -1;
        }
        feedReward(reward);
    }


    /*
    [0,0,0,0,0,0,0,0,0] = 0
    [1,0,0,0,0,0,0,0,0] = 1
    [2,0,0,0,0,0,0,0,0] = 2
    [0,1,0,0,0,0,0,0,0] = 3
    [1,1,0,0,0,0,0,0,0] = 4
    [2,1,0,0,0,0,0,0,0] = 5
    ..
     */
    public int getStateHash(Marker[] board) {
        int hash = 0;
        for (int i = 0; i < board.length; i++) {
            hash += getMarkerValue(board[i]) * Math.pow(3, i);
        }
        return hash;
    }

    // Empty= 0 , X = 1, O = 2
    private int getMarkerValue(Marker marker) {
        if (marker == Marker.X) return 1;
        if (marker == Marker.O) return 2;
        return 0;
    }

    // Update Formulae:
    // Value(State t) = Value(state t) + learning_rate * (Value(state t+1) - Value(state t))
    public void feedReward(double reward) {
        int length = states.size();
        // Set the reward to the last state
        stateValueMap.put(states.get(length - 1), reward);
        // Back propagate the reward to previous states with defined learning rate
        for (int i = length - 2; i >= 0; i--) {
            // Get the value of the state
            double currentStateValue = stateValueMap.getOrDefault(states.get(i), 0.0);
            double updatedStateValue = currentStateValue + learningRate * (stateValueMap.get(states.get(i + 1)) - currentStateValue);
            stateValueMap.put(states.get(i), updatedStateValue);
        }
    }


}
