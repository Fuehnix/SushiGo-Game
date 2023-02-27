package competition;

import com.company.CardType;
import com.company.Player;
import com.company.TurnResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FishSticksPlayer implements Player {
    private String playerName;
    private List<String> playerNameList;
    private List<CardType> playerCards;
    private boolean chopsticksOnTable;
    private Map<String, Integer> currentPointMap;
    private List<TurnResult> lastTurnResults;
    private int playerIndex;
    private DataHandler dataHandler;
    private static final int NUM_OF_STARTING_CARDS = 8;
    private int round;

    @Override
    public void init(String name, List<String> allNames) {
        playerName = name;
        playerNameList = new ArrayList<>();
        playerNameList = allNames;
        playerIndex = allNames.indexOf(playerName);
    }

    @Override
    public void newGame() {
        dataHandler = new DataHandler();
        dataHandler.populateHashMaps();
        dataHandler.setPlayerIndex(playerIndex);
        dataHandler.setRound(1);
    }

    @Override
    public void receiveHand(List<CardType> cards) {
        playerCards = cards;
        dataHandler.updatePlayerHand(playerCards);
        if (cards.size() != NUM_OF_STARTING_CARDS) {
            if ((round % 2) == 0) {
                dataHandler.rotateCards(CardRotation.RIGHT);
            } else {
                dataHandler.rotateCards(CardRotation.LEFT);
            }
        }
    }

    @Override
    public List<CardType> giveCardsPlayed() {
//        Random randomNumber = new Random();
//        int cardIndex = randomNumber.nextInt(playerCards.size());
//        List<CardType> chosenCards = new ArrayList<>();
//        chosenCards.add(playerCards.get(cardIndex));
        return dataHandler.decideCardsToPlay();
    }

    @Override
    public void endRound(Map<String, Integer> pointMap) {
        currentPointMap = pointMap;
        int currentRound = dataHandler.getRound();
        currentRound++;
        dataHandler.setRound(currentRound);
        List<Integer> pointsList = new ArrayList<>();
        for (int i = 0; i < playerNameList.size(); i++) {
            pointsList.add(pointMap.get(playerNameList.get(i)));
        }
        dataHandler.endRound(pointsList);
    }

    @Override
    public void receiveTurnResults(List<TurnResult> turnResults) {
        lastTurnResults = turnResults;
        dataHandler.getDataFromRecieveTurnResults(lastTurnResults);
    }

    @Override
    public void endGame(Map<String, Integer> pointMap) {
        currentPointMap = pointMap;
    }

    @Override
    public String getName() {
        return "FishSticks Go";
    }
}
