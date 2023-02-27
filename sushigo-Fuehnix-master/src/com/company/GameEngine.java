package com.company;

import java.util.*;

public class GameEngine {

    private List<Player> playerList;
    private List<String> playerNameList;
    private List<CardType> drawDeck;
    private static final int TEMPURA_CARDS = 14;
    private static final int SASHIMI_CARDS = 14;
    private static final int DUMPLING_CARDS = 14;
    private static final int TWO_MAKI_ROLLS_CARDS = 12;
    private static final int THREE_MAKI_ROLLS_CARDS = 8;
    private static final int ONE_MAKI_ROLL_CARDS = 6;
    private static final int SALMON_NIGIRI_CARDS = 10;
    private static final int SQUID_NIGIRI_CARDS = 5;
    private static final int EGG_NIGIRI_CARDS = 5;
    private static final int PUDDING_CARDS = 10;
    private static final int WASABI_CARDS = 6;
    private static final int CHOPSTICK_CARDS = 4;
//    private static final int TWO_PLAYER_START_HAND = 10;
//    private static final int THREE_PLAYER_START_HAND = 9;
    private static final int FOUR_PLAYER_START_HAND = 8;
//    private static final int FIVE_PLAYER_START_HAND = 7;
    private static final int NUM_OF_ROUNDS = 3;
    private HashMap<Player, List<CardType>> playerTableHand;
    private HashMap<Player, List<CardType>> playerHand;
    private HashMap<String, Integer> pointMap;
    private HashMap<Player, Integer> playerMakiRollMap;
    private HashMap<Player, Integer> playerPuddingMap;
    private HashMap<String, Integer> playerVictoryMap;
    private int round;

    public GameEngine(List<Player> players) {
        playerList = players;
        playerNameList = new ArrayList<>();
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).getName() == null) {
                playerNameList.add(i, "Player " + (i + 1));
            } else {
                playerNameList.add(i, playerList.get(i).getName());
            }
        }
        for (int i = 0; i < playerList.size(); i++) {
            playerList.get(i).init(playerNameList.get(i), playerNameList);
        }
    }

    public void startGame(){
        drawDeck = new ArrayList<>();
        playerTableHand = new HashMap<>();
        playerHand = new HashMap<>();
        playerMakiRollMap = new HashMap<>();
        playerPuddingMap = new HashMap<>();
        pointMap = new HashMap<>();
        populateDrawDeck();
        for(int i = 0; i<playerList.size();i++){
            playerList.get(i).newGame();
            pointMap.put(playerList.get(i).getName(),0);
        }
    }

    public void playGame() {
        startGame();
        for(int i = 0; i < NUM_OF_ROUNDS; i++){
            round = i+1;
            startRound();
            endRound();
        }
    }

    public void startRound(){
//        dealCardNumberHandler();
        dealCards(FOUR_PLAYER_START_HAND);
        while (true) {
            turnHandler();
            for (Player player : playerList) {
                if (playerHand.get(player).size() == 0) {
                    return;
                }
            }
            rotateCards();
        }
    }

    public void turnHandler(){
        List<TurnResult> turnResults = new ArrayList<>();
        for (Player player : playerList) {
            List<CardType> cardsPlayed = player.giveCardsPlayed();
            for (CardType card : cardsPlayed) {
                playerHand.get(player).remove(card);
            }
            try {
                playerTableHand.get(player).addAll(cardsPlayed);
            } catch (NullPointerException e) {
                playerTableHand.put(player, cardsPlayed);
            }
            if (cardsPlayed.size() > 1) {
                if (playerTableHand.get(player).contains(CardType.Chopsticks) && cardsPlayed.size() == 2) {
                    playerTableHand.get(player).remove(CardType.Chopsticks);
                    playerHand.get(player).add(CardType.Chopsticks);
                } else {
                    System.out.println(player.getName() + " has cheated!");
                    throw new IllegalArgumentException();
                }
            }
            turnResults.add(new TurnResult(player.getName(), cardsPlayed, playerTableHand.get(player)));
        }
        for (Player player : playerList) {
            player.receiveTurnResults(turnResults);
        }
    }

    public void rotateCards(){
        List<List<CardType>> playersHands = new ArrayList<>();
        for(Player player : playerList){
            playersHands.add(playerHand.get(player));
        }
        //if Round is even, pass right, else, pass left.
        if((round%2)==0){
            Collections.rotate(playersHands,1);
        } else{
            Collections.rotate(playersHands,-1);
        }

        for(int i = 0; i<playerList.size();i++){
//            playerHand.remove((playerList.get(i)));
            playerHand.put(playerList.get(i),playersHands.get(i));
            playerList.get(i).receiveHand(playersHands.get(i));
        }
    }

    public void endRound(){
        tableCardCounter();
        for(Player player : playerList){
            player.endRound(pointMap);
        }
        discardCards();
    }

    public void tableCardCounter(){
        for(Player player : playerList){
            int wasabi = 0;
            int sashimiCount = 0;
            int tempuraCount = 0;
            int dumplingCount = 0;
            int makiRollCount = 0;
            int nigiriPointCount = 0;

            for(CardType card : playerTableHand.get(player)){
                switch(card){
                    case Wasabi:
                        wasabi++;
                        break;
                    case Pudding:
                        break;
                    case Sashimi:
                        sashimiCount++;
                        break;
                    case Tempura:
                        tempuraCount++;
                        break;
                    case Dumpling:
                        dumplingCount++;
                        break;
                    case EggNigiri:
                        if (wasabi >= 1) {
                            nigiriPointCount += 3;
                            wasabi--;
                        } else{
                            nigiriPointCount += 1;
                        }
                        break;
                    case SalmonNigiri:
                        if (wasabi >= 1) {
                            nigiriPointCount += 6;
                            wasabi--;
                        } else{
                            nigiriPointCount += 2;
                        }
                        break;
                    case SquidNigiri:
                        if (wasabi >= 1) {
                            nigiriPointCount += 9;
                            wasabi--;
                        } else{
                            nigiriPointCount += 3;
                        }
                        break;
                    case Chopsticks:
                        break;
                    case MakiRollOne:
                        makiRollCount += 1;
                        break;
                    case MakiRollTwo:
                        makiRollCount += 2;
                        break;
                    case MakiRollThree:
                        makiRollCount += 3;
                        break;
                }
            }

            playerMakiRollMap.put(player,makiRollCount);
            PointCounter points = new PointCounter(sashimiCount,tempuraCount,dumplingCount,nigiriPointCount);
            int currentPoints = pointMap.remove(player.getName());
            pointMap.put(player.getName(), currentPoints + points.calculate());
        }

        HashMap<Player,Integer> makiRollPointMap = makiRollHandler();
        for(Player player : playerList){
            int currentPoints = pointMap.remove(player.getName());
            int makiPoints = makiRollPointMap.get(player);
            pointMap.put(player.getName(), currentPoints + makiPoints);
        }
    }

    public HashMap<Player, Integer> makiRollHandler(){
        int mostMakiRolls = 0;
        int secondMostMakiRolls = 0;
        List<Player> mostMakiPlayers = new ArrayList<>();
        List<Player> secondMostMakiPlayers = new ArrayList<>();
        HashMap<Player, Integer> makiRollPointMap = new HashMap<>();

        for(Player player : playerList){
            makiRollPointMap.put(player,0);
            int playerMakiRolls = playerMakiRollMap.get(player);
            if(playerMakiRolls > mostMakiRolls) {
                //if player has mostMakiRolls, and is not the first in the for loop to have more than 0, clear 2nd place
                //and put the players in 1st in the list of 2nd
                if(mostMakiRolls > 0){
                    secondMostMakiPlayers.clear();
                    secondMostMakiPlayers.addAll(mostMakiPlayers);
                    mostMakiPlayers.clear();
                }
                mostMakiRolls = playerMakiRolls;
                mostMakiPlayers.add(player);
                //if tied, add to the 1st place list
            } else if(playerMakiRolls == mostMakiRolls && playerMakiRolls > 0){
                mostMakiPlayers.add(player);
            } else if(playerMakiRolls > secondMostMakiRolls){
                //if not the first player in loop to be in second place, then clear out the other players
                if(secondMostMakiRolls > 0){
                    secondMostMakiPlayers.clear();
                }
                secondMostMakiRolls = playerMakiRolls;
                secondMostMakiPlayers.add(player);
            } else if(playerMakiRolls == secondMostMakiRolls && playerMakiRolls > 0){
                secondMostMakiPlayers.add(player);
            }
        }

        //split the points amongst the player(s) that had the most maki rolls
        for (int i = 0; i < mostMakiPlayers.size(); i++) {
            int points = 6 / mostMakiPlayers.size();
            makiRollPointMap.remove(mostMakiPlayers.get(i));
            makiRollPointMap.put(mostMakiPlayers.get(i), points);
        }

        //split the points amongst the player(s) that had the second most maki rolls, but only if there is only one player
        //with the most, as according to SushiGo rules
        for (int i = 0; i < secondMostMakiPlayers.size(); i++) {
            if (mostMakiPlayers.size() > 1) {
                break;
            }
            int points = 3 / secondMostMakiPlayers.size();
            makiRollPointMap.remove(secondMostMakiPlayers.get(i));
            makiRollPointMap.put(secondMostMakiPlayers.get(i), points);
        }

        return makiRollPointMap;
    }

    public void populateDrawDeck(){
        for(int i = 0; i<TEMPURA_CARDS; i++){
            drawDeck.add(CardType.Tempura);
        }
        for(int i = 0; i<SASHIMI_CARDS; i++){
            drawDeck.add(CardType.Sashimi);
        }
        for(int i = 0; i<DUMPLING_CARDS; i++){
            drawDeck.add(CardType.Dumpling);
        }
        for(int i = 0; i<ONE_MAKI_ROLL_CARDS; i++){
            drawDeck.add(CardType.MakiRollOne);
        }
        for(int i = 0; i<TWO_MAKI_ROLLS_CARDS; i++){
            drawDeck.add(CardType.MakiRollTwo);
        }
        for(int i = 0; i<THREE_MAKI_ROLLS_CARDS; i++){
            drawDeck.add(CardType.MakiRollThree);
        }
        for(int i = 0; i<SALMON_NIGIRI_CARDS; i++){
            drawDeck.add(CardType.SalmonNigiri);
        }
        for(int i = 0; i<SQUID_NIGIRI_CARDS; i++){
            drawDeck.add(CardType.SquidNigiri);
        }
        for(int i = 0; i<EGG_NIGIRI_CARDS; i++){
            drawDeck.add(CardType.EggNigiri);
        }
        for(int i = 0; i<PUDDING_CARDS; i++){
            drawDeck.add(CardType.Pudding);
        }
        for(int i = 0; i< WASABI_CARDS; i++){
            drawDeck.add(CardType.Wasabi);
        }
        for(int i = 0; i<CHOPSTICK_CARDS; i++){
            drawDeck.add(CardType.Chopsticks);
        }
        Collections.shuffle(drawDeck);
    }

// Read on assignment page that we should assume always 4 players
//    public void dealCardNumberHandler(){
//        switch(playerList.size()){
//            case 2: dealCards(TWO_PLAYER_START_HAND);
//                    break;
//            case 3: dealCards(THREE_PLAYER_START_HAND);
//                    break;
//            case 4: dealCards(FOUR_PLAYER_START_HAND);
//                    break;
//            case 5: dealCards(FIVE_PLAYER_START_HAND);
//                    break;
//        }
//    }

    public void dealCards(int startingHandSize){
        List<CardType> startingCards;
        for(Player player : playerList){
            startingCards = new ArrayList<>();
            for(int i = 0; i<startingHandSize;i++){
                //drawDeck.size()-1 is the index of card on top of deck
                startingCards.add(drawDeck.remove(drawDeck.size()-1));
            }
            player.receiveHand(startingCards);
            playerHand.put(player,startingCards);
        }
    }

    public int getPuddingCardsNum(Player player){
        int puddingCardsNum = 0;
        for (CardType card : playerTableHand.get(player)) {
            if (card.equals(CardType.Pudding)) {
                puddingCardsNum++;
            }
        }
        return puddingCardsNum;
    }

    public void discardCards(){
        for(Player player : playerList){
            int puddingCardsNum = getPuddingCardsNum(player);
            playerTableHand.get(player).clear();
            List<CardType> puddingCardsList = new ArrayList<>();
            for(int i = 0; i<puddingCardsNum; i++){
                puddingCardsList.add(CardType.Pudding);
            }
            playerTableHand.put(player,puddingCardsList);
        }
    }

    public void puddingScoreHandler(){
        int mostPuddingCards = 0;
        int leastPuddingCards = 0;
        List<Player> mostPuddingPlayers = new ArrayList<>();
        List<Player> leastPuddingPlayers = new ArrayList<>();

        for(Player player: playerList){
            playerPuddingMap.put(player,getPuddingCardsNum(player));
        }

        boolean allEqual = true;
        int lastPlayer = playerPuddingMap.get(playerList.get(0));
        //according to sushigo, if all players have the same amount of pudding cards, no points are awarded or deducted
        //this loop checks for that
        for(Integer num : playerPuddingMap.values()){
            if(num != lastPlayer){
                allEqual = false;
            }
            lastPlayer = num;
        }

        //if values are different, evaluate the point gain and deduction
        if(!allEqual) {
            for (Player player : playerList) {
                int playerPuddingCards = playerPuddingMap.get(player);
                //loop is very similar to MakiRoll counter, but differs in that instead of a looking for a second most,
                //this loop sees if a player has the least
                if (playerPuddingCards > mostPuddingCards) {
                    mostPuddingCards = playerPuddingCards;
                    mostPuddingPlayers.clear();
                    mostPuddingPlayers.add(player);
                } else if (playerPuddingCards == mostPuddingCards && playerPuddingCards > 0) {
                    mostPuddingPlayers.add(player);
                } else if (playerPuddingCards < leastPuddingCards) {
                    leastPuddingCards = playerPuddingCards;
                    leastPuddingPlayers.clear();
                    leastPuddingPlayers.add(player);
                } else if (playerPuddingCards == leastPuddingCards) {
                    leastPuddingPlayers.add(player);
                }
            }

            for (Player player : mostPuddingPlayers) {
                int puddingPoints = (6 / mostPuddingPlayers.size());
                int currentPoints = pointMap.remove(player.getName());
                pointMap.put(player.getName(), currentPoints + puddingPoints);
            }

            for (Player player : leastPuddingPlayers) {
                int puddingPoints = (-6 / leastPuddingPlayers.size());
                int currentPoints = pointMap.remove(player.getName());
                pointMap.put(player.getName(), currentPoints + puddingPoints);
            }
        }
    }

    public HashMap<String, Integer> endGame() {
        puddingScoreHandler();
        for (Player player : playerList) {
            player.endGame(pointMap);
        }

        int playerScore;
        int highestScore = 0;
        List<Player> winners = new ArrayList<>();
        for (Player player : playerList) {
            playerScore = pointMap.get(player.getName());
            if (playerScore > highestScore) {
                if (playerScore > 0) {
                    winners.clear();
                    winners.add(player);
                    highestScore = playerScore;
                }
                highestScore = playerScore;
                winners.add(player);
            } else if (playerScore == highestScore && highestScore > 0) {
                winners.add(player);
            }
        }
        for (Player player : playerList) {
            int currentWins = playerVictoryMap.get(player.getName());
            if (winners.contains(player)) {
                playerVictoryMap.put(player.getName(), currentWins + 1);
            }
        }

        return pointMap;
    }

    public void initializeVictoryMap() {
        playerVictoryMap = new HashMap<>();
        for (Player player : playerList) {
            playerVictoryMap.put(player.getName(), 0);
        }
    }

    public HashMap<String, Integer> getPlayerGamesWon() {
        return playerVictoryMap;
    }

    public HashMap<String, Double> getWinPercentages() {
        List<Integer> gameWins = new ArrayList<>();
        HashMap<String, Double> winPercentageMap = new HashMap<>();
        int totalGames = 0;
        for (Player player : playerList) {
            int currentPlayerWins = playerVictoryMap.get(player.getName());
            gameWins.add(currentPlayerWins);
            totalGames += currentPlayerWins;
        }
        for (int i = 0; i < playerList.size(); i++) {
            int currentPlayerWins = gameWins.get(i);
            double winPercentage = ((double) currentPlayerWins / totalGames) * 100;
            winPercentageMap.put(playerList.get(i).getName(), winPercentage);
        }
        return winPercentageMap;
    }

    public HashMap<Player, List<CardType>> getPlayerTableHand(){
        return playerTableHand;
    }

    public HashMap<Player, List<CardType>> getPlayerHand(){
        return playerHand;
    }

    public void setPlayerTableHand(HashMap<Player, List<CardType>> playerTableHand){
        this.playerTableHand = playerTableHand;
    }

    public void setPlayerHand(HashMap<Player, List<CardType>> playerHand){
        this.playerHand = playerHand;
    }

    public HashMap<String, Integer> getPointMap(){
        return pointMap;
    }

    public void setDrawDeck(List<CardType> newDeck) {
        drawDeck = newDeck;
    }

    public List<CardType> getDrawDeck() {
        return drawDeck;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getRound() {
        return round;
    }
}