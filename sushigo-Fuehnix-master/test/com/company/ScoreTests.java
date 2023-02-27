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

public class ScoreTests {

    private GameEngine gameEngine;
    private List<Player> playerList;
    private HashMap<Player, List<CardType>> playerTableHand;
    private List<CardType> playerOneTable;
    private List<CardType> playerTwoTable;
    private List<CardType> playerThreeTable;
    private List<CardType> playerFourTable;
    private List<CardType> playerOneHand;
    private List<CardType> playerTwoHand;
    private List<CardType> playerThreeHand;
    private List<CardType> playerFourHand;
    private int[] expectedPoints;

    @Before
    public void setup(){
        expectedPoints = new int[4];
        playerList = new ArrayList<>();
        playerOneTable = new ArrayList<>();
        playerTwoTable = new ArrayList<>();
        playerThreeTable = new ArrayList<>();
        playerFourTable = new ArrayList<>();
        playerOneHand = new ArrayList<>();
        playerTwoHand = new ArrayList<>();
        playerThreeHand = new ArrayList<>();
        playerFourHand = new ArrayList<>();
        playerTableHand = new HashMap<>();

        playerList.add(new RandomPlayer());
        playerList.add(new RandomPlayer());
        playerList.add(new RandomPlayer());
        playerList.add(new RandomPlayer());
        gameEngine = new GameEngine(playerList);
        gameEngine.startGame();

        //Setting the cards to these means that the default amount of points is 8.
        //cards on table are modified based on the scoring being tested.
        //player one cards
        playerOneTable.addAll(new ArrayList<>(Arrays.asList((new CardType[]
                {CardType.EggNigiri, CardType.EggNigiri, CardType.EggNigiri,CardType.EggNigiri,
                        CardType.EggNigiri, CardType.EggNigiri, CardType.EggNigiri, CardType.EggNigiri}))));
        playerTableHand.put(playerList.get(0),playerOneTable);

        //player two cards
        playerTwoTable.addAll(new ArrayList<>(Arrays.asList((new CardType[]
                {CardType.EggNigiri, CardType.EggNigiri, CardType.EggNigiri,CardType.EggNigiri,
                        CardType.EggNigiri, CardType.EggNigiri, CardType.EggNigiri, CardType.EggNigiri}))));
        playerTableHand.put(playerList.get(1),playerTwoTable);

        //player three cards
        playerThreeTable.addAll(new ArrayList<>(Arrays.asList((new CardType[]
                {CardType.EggNigiri, CardType.EggNigiri, CardType.EggNigiri,CardType.EggNigiri,
                        CardType.EggNigiri, CardType.EggNigiri, CardType.EggNigiri, CardType.EggNigiri}))));
        playerTableHand.put(playerList.get(2),playerThreeTable);

        //player four cards
        playerFourTable.addAll(new ArrayList<>(Arrays.asList((new CardType[]
                {CardType.EggNigiri, CardType.EggNigiri, CardType.EggNigiri,CardType.EggNigiri,
                        CardType.EggNigiri, CardType.EggNigiri, CardType.EggNigiri, CardType.EggNigiri}))));
        playerTableHand.put(playerList.get(3),playerFourTable);
    }

    @Test
    public void basicPuddingTest(){
        //player one cards
        playerOneTable.remove(CardType.EggNigiri);
        playerOneTable.add(CardType.Pudding);
        playerOneTable.remove(CardType.EggNigiri);
        playerOneTable.add(CardType.Pudding);
        playerTableHand.put(playerList.get(0),playerOneTable);

        //player two cards
        playerTwoTable.remove(CardType.EggNigiri);
        playerTwoTable.add(CardType.Pudding);
        playerTableHand.put(playerList.get(1),playerTwoTable);

        //player three cards
        playerThreeTable.remove(CardType.EggNigiri);
        playerThreeTable.add(CardType.Pudding);
        playerTableHand.put(playerList.get(2),playerThreeTable);

        //player four decided not to collect pudding and will pay the price

        gameEngine.setPlayerTableHand(playerTableHand);
        gameEngine.tableCardCounter();
        gameEngine.puddingScoreHandler();

        expectedPoints[0] = 12;
        expectedPoints[1] = 7;
        expectedPoints[2] = 7;
        expectedPoints[3] = 2;
        for(int i = 0; i<playerList.size(); i++) {
            int actualPoints = gameEngine.getPointMap().get(playerList.get(i).getName());
            assertEquals(expectedPoints[i], actualPoints);
        }
    }

    @Test
    public void equalPuddingTest() {
        gameEngine.setPlayerTableHand(playerTableHand);
        gameEngine.tableCardCounter();
        gameEngine.puddingScoreHandler();

        expectedPoints[0] = 8;
        expectedPoints[1] = 8;
        expectedPoints[2] = 8;
        expectedPoints[3] = 8;
        for (int i = 0; i < playerList.size(); i++) {
            int actualPoints = gameEngine.getPointMap().get(playerList.get(i).getName());
            assertEquals(expectedPoints[i], actualPoints);
        }
    }

    @Test
    public void splitPuddingTest(){
        //player one cards
        playerOneTable.remove(CardType.EggNigiri);
        playerOneTable.add(CardType.Pudding);
        playerTableHand.put(playerList.get(0),playerOneTable);

        //player two cards
        playerTwoTable.remove(CardType.EggNigiri);
        playerTwoTable.add(CardType.Pudding);
        playerTableHand.put(playerList.get(1),playerTwoTable);

        //player three and four have no pudding
        gameEngine.setPlayerTableHand(playerTableHand);
        gameEngine.tableCardCounter();
        gameEngine.puddingScoreHandler();

        expectedPoints[0] = 10;
        expectedPoints[1] = 10;
        expectedPoints[2] = 5;
        expectedPoints[3] = 5;
        for (int i = 0; i < playerList.size(); i++) {
            int actualPoints = gameEngine.getPointMap().get(playerList.get(i).getName());
            assertEquals(expectedPoints[i], actualPoints);
        }
    }

    @Test
    public void basicMakiRollTest(){
        //player one cards
        playerOneTable.remove(CardType.EggNigiri);
        playerOneTable.add(CardType.MakiRollThree);
        playerTableHand.put(playerList.get(0),playerOneTable);

        //player two cards has no maki

        //player three cards
        playerThreeTable.remove(CardType.EggNigiri);
        playerThreeTable.add(CardType.MakiRollOne);
        playerTableHand.put(playerList.get(2),playerThreeTable);

        //player four has no maki

        gameEngine.setPlayerTableHand(playerTableHand);
        gameEngine.tableCardCounter();

        expectedPoints[0] = 13;
        expectedPoints[1] = 8;
        expectedPoints[2] = 10;
        expectedPoints[3] = 8;
        for(int i = 0; i<playerList.size(); i++) {
            int actualPoints = gameEngine.getPointMap().get(playerList.get(i).getName());
            assertEquals(expectedPoints[i], actualPoints);
        }
    }

    @Test
    public void splitMakiRollTest(){
        //player one cards
        playerOneTable.remove(CardType.EggNigiri);
        playerOneTable.add(CardType.MakiRollThree);
        playerTableHand.put(playerList.get(0),playerOneTable);

        //player two cards has no maki
        playerTwoTable.remove(CardType.EggNigiri);
        playerTwoTable.add(CardType.MakiRollTwo);
        playerTableHand.put(playerList.get(1),playerTwoTable);

        //player three cards
        playerThreeTable.remove(CardType.EggNigiri);
        playerThreeTable.add(CardType.MakiRollThree);
        playerTableHand.put(playerList.get(2),playerThreeTable);

        //player four has no maki
        playerFourTable.remove(CardType.EggNigiri);
        playerFourTable.add(CardType.MakiRollTwo);

        gameEngine.setPlayerTableHand(playerTableHand);
        gameEngine.tableCardCounter();

        expectedPoints[0] = 10;
        expectedPoints[1] = 7;
        expectedPoints[2] = 10;
        expectedPoints[3] = 7;
        for(int i = 0; i<playerList.size(); i++) {
            int actualPoints = gameEngine.getPointMap().get(playerList.get(i).getName());
            assertEquals(expectedPoints[i], actualPoints);
        }
    }

    @Test
    public void misplayedWasabiTest(){
        playerOneTable.remove(CardType.EggNigiri);
        playerOneTable.add(CardType.Wasabi);
        playerTableHand.put(playerList.get(0),playerOneTable);

        gameEngine.setPlayerTableHand(playerTableHand);
        gameEngine.tableCardCounter();

        expectedPoints[0] = 7;
        expectedPoints[1] = 8;
        expectedPoints[2] = 8;
        expectedPoints[3] = 8;
        for(int i = 0; i<playerList.size(); i++) {
            int actualPoints = gameEngine.getPointMap().get(playerList.get(i).getName());
            assertEquals(expectedPoints[i], actualPoints);
        }
    }

    @Test
    public void wasabiTest(){
        playerOneTable.remove(CardType.EggNigiri);
        playerOneTable.remove(CardType.EggNigiri);
        playerOneTable.add(CardType.Wasabi);
        playerOneTable.add(CardType.EggNigiri);
        playerTableHand.put(playerList.get(0),playerOneTable);

        gameEngine.setPlayerTableHand(playerTableHand);
        gameEngine.tableCardCounter();

        expectedPoints[0] = 9;
        expectedPoints[1] = 8;
        expectedPoints[2] = 8;
        expectedPoints[3] = 8;
        for(int i = 0; i<playerList.size(); i++) {
            int actualPoints = gameEngine.getPointMap().get(playerList.get(i).getName());
            assertEquals(expectedPoints[i], actualPoints);
        }
    }

    @Test
    public void dumplingTest() {
        playerOneTable.remove(CardType.EggNigiri);
        playerOneTable.add(CardType.Dumpling);
        playerTableHand.put(playerList.get(0), playerOneTable);

        gameEngine.setPlayerTableHand(playerTableHand);
        gameEngine.tableCardCounter();
        gameEngine.puddingScoreHandler();

        expectedPoints[0] = 8;
        expectedPoints[1] = 8;
        expectedPoints[2] = 8;
        expectedPoints[3] = 8;
        for (int i = 0; i < playerList.size(); i++) {
            int actualPoints = gameEngine.getPointMap().get(playerList.get(i).getName());
            assertEquals(expectedPoints[i], actualPoints);
        }
    }


    @Test
    public void dumpling2Test() {
        playerOneTable.remove(CardType.EggNigiri);
        playerOneTable.remove(CardType.EggNigiri);
        playerOneTable.add(CardType.Dumpling);
        playerOneTable.add(CardType.Dumpling);
        playerTableHand.put(playerList.get(0), playerOneTable);

        gameEngine.setPlayerTableHand(playerTableHand);
        gameEngine.tableCardCounter();
        gameEngine.puddingScoreHandler();

        expectedPoints[0] = 9;
        expectedPoints[1] = 8;
        expectedPoints[2] = 8;
        expectedPoints[3] = 8;
        for (int i = 0; i < playerList.size(); i++) {
            int actualPoints = gameEngine.getPointMap().get(playerList.get(i).getName());
            assertEquals(expectedPoints[i], actualPoints);
        }
    }

    @Test
    public void dumpling3Test() {
        playerOneTable.remove(CardType.EggNigiri);
        playerOneTable.remove(CardType.EggNigiri);
        playerOneTable.remove(CardType.EggNigiri);
        playerOneTable.add(CardType.Dumpling);
        playerOneTable.add(CardType.Dumpling);
        playerOneTable.add(CardType.Dumpling);
        playerTableHand.put(playerList.get(0), playerOneTable);

        gameEngine.setPlayerTableHand(playerTableHand);
        gameEngine.tableCardCounter();
        gameEngine.puddingScoreHandler();

        expectedPoints[0] = 11;
        expectedPoints[1] = 8;
        expectedPoints[2] = 8;
        expectedPoints[3] = 8;
        for (int i = 0; i < playerList.size(); i++) {
            int actualPoints = gameEngine.getPointMap().get(playerList.get(i).getName());
            assertEquals(expectedPoints[i], actualPoints);
        }
    }

    @Test
    public void dumpling4Test() {
        playerOneTable.remove(CardType.EggNigiri);
        playerOneTable.remove(CardType.EggNigiri);
        playerOneTable.remove(CardType.EggNigiri);
        playerOneTable.remove(CardType.EggNigiri);
        playerOneTable.add(CardType.Dumpling);
        playerOneTable.add(CardType.Dumpling);
        playerOneTable.add(CardType.Dumpling);
        playerOneTable.add(CardType.Dumpling);
        playerTableHand.put(playerList.get(0), playerOneTable);

        gameEngine.setPlayerTableHand(playerTableHand);
        gameEngine.tableCardCounter();
        gameEngine.puddingScoreHandler();

        expectedPoints[0] = 14;
        expectedPoints[1] = 8;
        expectedPoints[2] = 8;
        expectedPoints[3] = 8;
        for (int i = 0; i < playerList.size(); i++) {
            int actualPoints = gameEngine.getPointMap().get(playerList.get(i).getName());
            assertEquals(expectedPoints[i], actualPoints);
        }
    }

    @Test
    public void dumpling5Test() {
        playerOneTable.remove(CardType.EggNigiri);
        playerOneTable.remove(CardType.EggNigiri);
        playerOneTable.remove(CardType.EggNigiri);
        playerOneTable.remove(CardType.EggNigiri);
        playerOneTable.remove(CardType.EggNigiri);
        playerOneTable.add(CardType.Dumpling);
        playerOneTable.add(CardType.Dumpling);
        playerOneTable.add(CardType.Dumpling);
        playerOneTable.add(CardType.Dumpling);
        playerOneTable.add(CardType.Dumpling);
        playerTableHand.put(playerList.get(0), playerOneTable);

        gameEngine.setPlayerTableHand(playerTableHand);
        gameEngine.tableCardCounter();
        gameEngine.puddingScoreHandler();

        expectedPoints[0] = 18;
        expectedPoints[1] = 8;
        expectedPoints[2] = 8;
        expectedPoints[3] = 8;
        for (int i = 0; i < playerList.size(); i++) {
            int actualPoints = gameEngine.getPointMap().get(playerList.get(i).getName());
            assertEquals(expectedPoints[i], actualPoints);
        }
    }

    @Test
    public void tempuraTest(){
        playerOneTable.remove(CardType.EggNigiri);
        playerOneTable.remove(CardType.EggNigiri);
        playerOneTable.add(CardType.Tempura);
        playerOneTable.add(CardType.Tempura);
        playerTableHand.put(playerList.get(0), playerOneTable);

        gameEngine.setPlayerTableHand(playerTableHand);
        gameEngine.tableCardCounter();
        gameEngine.puddingScoreHandler();

        expectedPoints[0] = 11;
        expectedPoints[1] = 8;
        expectedPoints[2] = 8;
        expectedPoints[3] = 8;
        for (int i = 0; i < playerList.size(); i++) {
            int actualPoints = gameEngine.getPointMap().get(playerList.get(i).getName());
            assertEquals(expectedPoints[i], actualPoints);
        }
    }

    @Test
    public void sashimiTest(){
        playerOneTable.remove(CardType.EggNigiri);
        playerOneTable.remove(CardType.EggNigiri);
        playerOneTable.remove(CardType.EggNigiri);
        playerOneTable.add(CardType.Sashimi);
        playerOneTable.add(CardType.Sashimi);
        playerOneTable.add(CardType.Sashimi);
        playerTableHand.put(playerList.get(0), playerOneTable);

        gameEngine.setPlayerTableHand(playerTableHand);
        gameEngine.tableCardCounter();
        gameEngine.puddingScoreHandler();

        expectedPoints[0] = 15;
        expectedPoints[1] = 8;
        expectedPoints[2] = 8;
        expectedPoints[3] = 8;
        for (int i = 0; i < playerList.size(); i++) {
            int actualPoints = gameEngine.getPointMap().get(playerList.get(i).getName());
            assertEquals(expectedPoints[i], actualPoints);
        }
    }
}
