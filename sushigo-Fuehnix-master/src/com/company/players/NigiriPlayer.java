package com.company.players;

import com.company.CardType;
import com.company.Player;
import com.company.TurnResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class NigiriPlayer implements Player {
    private String playerName;
    private List<String> playerNameList;
    private List<CardType> playerCards;
    private boolean chopsticksOnTable;
    private Map<String, Integer> currentPointMap;
    private List<TurnResult> lastTurnResults;

    @Override
    public void init(String name, List<String> allNames) {
        playerName = name;
        playerNameList = new ArrayList<>();
        playerNameList = allNames;
    }

    @Override
    public void newGame() {

    }

    @Override
    public void receiveHand(List<CardType> cards) {
        playerCards = cards;
    }

    @Override
    public List<CardType> giveCardsPlayed() {
        Random randomNumber = new Random();
        int cardIndex = randomNumber.nextInt(playerCards.size());
        for (int i = 0; i < playerCards.size(); i++) {
            if (playerCards.get(i).equals(CardType.SquidNigiri)) {
                cardIndex = i;
                break;
            } else if (playerCards.get(i).equals(CardType.Tempura)) {
                cardIndex = i;
                break;
            } else if (playerCards.get(i).equals(CardType.SalmonNigiri)) {
                cardIndex = i;
                break;
            } else if (playerCards.get(i).equals(CardType.Dumpling)) {
                cardIndex = i;
                break;
            }
        }
        List<CardType> chosenCards = new ArrayList<>();
        chosenCards.add(playerCards.get(cardIndex));
        return chosenCards;
    }


    @Override
    public void endRound(Map<String, Integer> pointMap) {
        currentPointMap = pointMap;
    }

    @Override
    public void receiveTurnResults(List<TurnResult> turnResults) {
        lastTurnResults = turnResults;
    }

    @Override
    public void endGame(Map<String, Integer> pointMap) {
        currentPointMap = pointMap;
    }

    @Override
    public String getName() {
        return playerName;
    }

    //    @Override
//    public String getName(){
//        return "Gambler";
//    }
}
