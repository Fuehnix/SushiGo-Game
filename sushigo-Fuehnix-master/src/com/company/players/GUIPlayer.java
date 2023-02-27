package com.company.players;

import com.company.CardType;
import com.company.GraphicsEngine;
import com.company.Player;
import com.company.TurnResult;

import java.util.List;
import java.util.Map;

/**
 * The Player implementation that will use GraphicsEngine to present a GUI for you to play through.
 * You may modify this file at your own risk.
 */
public class GUIPlayer implements Player {
    private GraphicsEngine graphicsEngine;
    private String playerName;
    private List<String> allPlayerNames;

    @Override
    public void init(String name, List<String> allNames) {
        playerName = name;
        allPlayerNames = allNames;
    }

    @Override
    public void newGame() {
        graphicsEngine = new GraphicsEngine(playerName, allPlayerNames);
    }

    @Override
    public void receiveHand(List<CardType> cards) {
        graphicsEngine.giveHand(cards);
    }

    @Override
    public List<CardType> giveCardsPlayed() {
        return graphicsEngine.getCardsPlayed();
    }

    @Override
    public void endRound(Map<String, Integer> pointMap) {
        graphicsEngine.clearTable();
        graphicsEngine.setPoints(pointMap);
    }

    @Override
    public void receiveTurnResults(List<TurnResult> turnResults) {
        graphicsEngine.setTable(turnResults);
    }

    @Override
    public void endGame(Map<String, Integer> pointMap) {
        graphicsEngine.endGame(pointMap);
    }

    @Override
    public String getName() {
        return "Jason Yamel";
    }
}
