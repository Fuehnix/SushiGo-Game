package com.company;

import com.company.players.GUIPlayer;
import com.company.players.GamblePlayer;
import com.company.players.NigiriPlayer;
import com.company.players.RandomPlayer;
import competition.FishSticksPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final int NUMBER_OF_GAMES = 10000;

    public static void main(String[] args) {
        List<Player> playerList = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        boolean validInput;
        do {
            System.out.println("Play game, or have players play against each other?");
            System.out.println("Type 'play' to play a game, or 'auto' to have players play against each other");
            String userInput = input.nextLine();
            if (userInput.equalsIgnoreCase("play")) {
                validInput = true;
                playerList.add(new RandomPlayer()); //player 1
                playerList.add(new RandomPlayer()); //player 2
                playerList.add(new FishSticksPlayer()); //player 3
                playerList.add(new GUIPlayer()); // player 4
                GameEngine gameEngine = new GameEngine(playerList);
                gameEngine.initializeVictoryMap();
                gameEngine.playGame();
            } else if (userInput.equalsIgnoreCase("auto")) {
                validInput = true;
                playerList.add(new RandomPlayer()); //player 1
                playerList.add(new FishSticksPlayer()); //player 2
                playerList.add(new RandomPlayer()); //player 3
                playerList.add(new RandomPlayer()); // player 4

                GameEngine gameEngine = new GameEngine(playerList);
                gameEngine.initializeVictoryMap();
                for (int i = 0; i < NUMBER_OF_GAMES; i++) {
                    gameEngine.playGame();
                    System.out.println("Game " + i + " results");
                    System.out.println(gameEngine.endGame() + "\n");
                    System.out.println("Games won by each player");
                    System.out.println(gameEngine.getPlayerGamesWon() + "\n");
                }
                System.out.println(gameEngine.getWinPercentages());
            } else {
                validInput = false;
            }
        } while (!validInput);
    }
}
