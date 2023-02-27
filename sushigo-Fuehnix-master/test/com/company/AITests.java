package com.company;

import com.company.players.GUIPlayer;
import com.company.players.GamblePlayer;
import com.company.players.NigiriPlayer;
import com.company.players.RandomPlayer;
import competition.FishSticksPlayer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AITests {
    private GameEngine gameEngine;
    private List<Player> playerList;
    private static final int NUMBER_OF_GAMES = 10000;

    @Test
    public void fishsticksCheatingTest() {
        playerList = new ArrayList<>();
        playerList.add(new FishSticksPlayer()); //player 1
        playerList.add(new RandomPlayer()); //player 2
        playerList.add(new RandomPlayer()); //player 3
        playerList.add(new RandomPlayer()); // player 4
        gameEngine = new GameEngine(playerList);
        gameEngine.initializeVictoryMap();
        for (int i = 0; i < NUMBER_OF_GAMES; i++) {
            gameEngine.playGame();
            gameEngine.endGame();
        }
        //will throw IllegalArgumentException in turnHandler if a player cheats
    }
    @Test
    public void fishsticksVSrandom() {
        playerList = new ArrayList<>();
        playerList.add(new FishSticksPlayer()); //player 1
        playerList.add(new RandomPlayer()); //player 2
        playerList.add(new RandomPlayer()); //player 3
        playerList.add(new RandomPlayer()); // player 4
        gameEngine = new GameEngine(playerList);
        gameEngine.initializeVictoryMap();
        for (int i = 0; i < NUMBER_OF_GAMES; i++) {
            gameEngine.playGame();
            gameEngine.endGame();
        }
        assertTrue(gameEngine.getWinPercentages().get(playerList.get(0).getName()) > 75);
    }

    @Test
    public void fishsticksVSNigiri() {
        playerList = new ArrayList<>();
        playerList.add(new FishSticksPlayer()); //player 1
        playerList.add(new NigiriPlayer()); //player 2
        playerList.add(new NigiriPlayer()); //player 3
        playerList.add(new NigiriPlayer()); // player 4
        gameEngine = new GameEngine(playerList);
        gameEngine.initializeVictoryMap();
        for (int i = 0; i < NUMBER_OF_GAMES; i++) {
            gameEngine.playGame();
            gameEngine.endGame();
        }
        assertTrue(gameEngine.getWinPercentages().get(playerList.get(0).getName()) > 35);
    }

    @Test
    public void fishsticksVSGambler() {
        playerList = new ArrayList<>();
        playerList.add(new FishSticksPlayer()); //player 1
        playerList.add(new GamblePlayer()); //player 2
        playerList.add(new GamblePlayer()); //player 3
        playerList.add(new GamblePlayer()); // player 4
        gameEngine = new GameEngine(playerList);
        gameEngine.initializeVictoryMap();
        for (int i = 0; i < NUMBER_OF_GAMES; i++) {
            gameEngine.playGame();
            gameEngine.endGame();
        }
        assertTrue(gameEngine.getWinPercentages().get(playerList.get(0).getName()) > 40);
    }

    @Test
    public void nigiriVSrandom() {
        playerList = new ArrayList<>();
        playerList.add(new NigiriPlayer()); //player 1
        playerList.add(new RandomPlayer()); //player 2
        playerList.add(new RandomPlayer()); //player 3
        playerList.add(new RandomPlayer()); // player 4
        gameEngine = new GameEngine(playerList);
        gameEngine.initializeVictoryMap();
        for (int i = 0; i < NUMBER_OF_GAMES; i++) {
            gameEngine.playGame();
            gameEngine.endGame();
        }
        assertTrue(gameEngine.getWinPercentages().get(playerList.get(0).getName()) > 71);
    }

    @Test
    public void nigiriVSGambler() {
        playerList = new ArrayList<>();
        playerList.add(new NigiriPlayer()); //player 1
        playerList.add(new GamblePlayer()); //player 2
        playerList.add(new GamblePlayer()); //player 3
        playerList.add(new GamblePlayer()); // player 4
        gameEngine = new GameEngine(playerList);
        gameEngine.initializeVictoryMap();
        for (int i = 0; i < NUMBER_OF_GAMES; i++) {
            gameEngine.playGame();
            gameEngine.endGame();
        }
        assertTrue(gameEngine.getWinPercentages().get(playerList.get(0).getName()) > 60);
    }

    @Test
    public void gamblerVSrandom() {
        playerList = new ArrayList<>();
        playerList.add(new GamblePlayer()); //player 1
        playerList.add(new RandomPlayer()); //player 2
        playerList.add(new RandomPlayer()); //player 3
        playerList.add(new RandomPlayer()); // player 4
        gameEngine = new GameEngine(playerList);
        gameEngine.initializeVictoryMap();
        for (int i = 0; i < NUMBER_OF_GAMES; i++) {
            gameEngine.playGame();
            gameEngine.endGame();
        }
        assertTrue(gameEngine.getWinPercentages().get(playerList.get(0).getName()) > 68);
    }
}
