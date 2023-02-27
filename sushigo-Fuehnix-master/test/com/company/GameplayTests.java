package com.company;

import com.company.players.GUIPlayer;
import com.company.players.RandomPlayer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GameplayTests {

    private List<Player> playerList;
    private GameEngine gameEngine;

    @Before
    public void setup() {
        playerList = new ArrayList<>();
        playerList.add(new RandomPlayer());
        playerList.add(new RandomPlayer());
        playerList.add(new RandomPlayer());
        playerList.add(new RandomPlayer()); // Having a single GUIPlayer will play the game with a GUI

        gameEngine = new GameEngine(playerList);
        gameEngine.startGame();
    }

    @Test
    public void populateDeckTest() {
        gameEngine.getDrawDeck().clear();
        gameEngine.populateDrawDeck();
        assertEquals(108, gameEngine.getDrawDeck().size());
    }

    @Test
    public void dealCardsTest() {
        gameEngine.dealCards(8);
        for (Player player : playerList) {
            assertEquals(8, gameEngine.getPlayerHand().get(player).size());
        }
    }

    @Test
    public void turnHandler() {
        gameEngine.dealCards(8);
        gameEngine.turnHandler();
        for (Player player : playerList) {
            //checks if card has been played on field
            assertEquals(1, gameEngine.getPlayerTableHand().get(player).size());
            //checks if card has been removed from hand
            assertEquals(7, gameEngine.getPlayerHand().get(player).size());

        }
    }

    @Test
    public void rotateCardsLeft() {
        gameEngine.dealCards(8);
        gameEngine.setRound(1);
        gameEngine.turnHandler();
        HashMap<Player, List<CardType>> preRotatePlayerHand = new HashMap<>();
        for (Player player : playerList) {
            preRotatePlayerHand.put(player, gameEngine.getPlayerHand().get(player));
        }
        gameEngine.rotateCards();
        System.out.println(gameEngine.getPlayerHand().values());
        for (int i = 0; i < playerList.size(); i++) {
            if (i == 3) {
                assertEquals(preRotatePlayerHand.get(playerList.get(0)), gameEngine.getPlayerHand().get(playerList.get(i)));
            } else {
                assertEquals(preRotatePlayerHand.get(playerList.get(i + 1)), gameEngine.getPlayerHand().get(playerList.get(i)));
            }
        }
    }

    @Test
    public void rotateCardsRight() {
        gameEngine.dealCards(8);
        gameEngine.setRound(2);
        gameEngine.turnHandler();
        HashMap<Player, List<CardType>> preRotatePlayerHand = new HashMap<>();
        for (Player player : playerList) {
            preRotatePlayerHand.put(player, gameEngine.getPlayerHand().get(player));
        }
        gameEngine.rotateCards();
        System.out.println(gameEngine.getPlayerHand().values());
        for (int i = 0; i < playerList.size(); i++) {
            if (i == 0) {
                assertEquals(preRotatePlayerHand.get(playerList.get(3)), gameEngine.getPlayerHand().get(playerList.get(i)));
            } else {
                assertEquals(preRotatePlayerHand.get(playerList.get(i - 1)), gameEngine.getPlayerHand().get(playerList.get(i)));
            }
        }
    }

    @Test
    public void discardCards() {
        gameEngine.startRound();
        gameEngine.discardCards();
        for (Player player : playerList) {
            assertTrue(gameEngine.getPlayerTableHand().get(player).isEmpty()
                    || gameEngine.getPlayerTableHand().get(player).contains(CardType.Pudding));
        }
    }
}

