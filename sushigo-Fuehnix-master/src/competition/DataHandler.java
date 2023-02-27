package competition;

import com.company.CardType;
import com.company.TurnResult;

import java.util.*;

public class DataHandler {
    private List<CardType> playerHand;
    private List<List<CardType>> cardsOnTable;
    private List<List<CardType>> knownCardsInHands;
    private List<Integer> numOfPudding;
    private static final int TEMPURA_CARDS_DECK = 14;
    private static final int SASHIMI_CARDS_DECK = 14;
    private static final int DUMPLING_CARDS_DECK = 14;
    private static final int TWO_MAKI_ROLLS_CARDS_DECK = 12;
    private static final int THREE_MAKI_ROLLS_CARDS_DECK = 8;
    private static final int ONE_MAKI_ROLL_CARDS_DECK = 6;
    private static final int SALMON_NIGIRI_CARDS_DECK = 10;
    private static final int SQUID_NIGIRI_CARDS_DECK = 5;
    private static final int EGG_NIGIRI_CARDS_DECK = 5;
    private static final int PUDDING_CARDS_DECK = 10;
    private static final int WASABI_CARDS_DECK = 6;
    private static final int CHOPSTICK_CARDS_DECK = 4;
    private HashMap<CardType, Integer> cardMap;
    private int chopsticksOnTableCounter;
    private List<Integer> wasabiOnTableCounterPerPlayer;
    private int playerIndex;
    private boolean needPudding = false;
    private int round;


    public DataHandler() {
        cardMap = new HashMap<>();
//        turnResultsPerPlayer = new ArrayList<>(4);
        knownCardsInHands = new ArrayList<>(4);
        cardsOnTable = new ArrayList<>(4);
        numOfPudding = new ArrayList<>(4);
        wasabiOnTableCounterPerPlayer = new ArrayList<>(4);

        for (int i = 0; i < 4; i++) {
//            turnResultsPerPlayer.add(null);
            List<CardType> emptyTable = new ArrayList<>();
            knownCardsInHands.add(null);
            cardsOnTable.add(emptyTable);
            numOfPudding.add(0);
            wasabiOnTableCounterPerPlayer.add(0);
        }
        int size = knownCardsInHands.size();
    }

    public void populateHashMaps() {
        cardMap.put(CardType.Tempura, TEMPURA_CARDS_DECK);
        cardMap.put(CardType.Sashimi, SASHIMI_CARDS_DECK);
        cardMap.put(CardType.Dumpling, DUMPLING_CARDS_DECK);
        cardMap.put(CardType.MakiRollTwo, TWO_MAKI_ROLLS_CARDS_DECK);
        cardMap.put(CardType.MakiRollOne, ONE_MAKI_ROLL_CARDS_DECK);
        cardMap.put(CardType.MakiRollThree, THREE_MAKI_ROLLS_CARDS_DECK);
        cardMap.put(CardType.SalmonNigiri, SALMON_NIGIRI_CARDS_DECK);
        cardMap.put(CardType.SquidNigiri, SQUID_NIGIRI_CARDS_DECK);
        cardMap.put(CardType.EggNigiri, EGG_NIGIRI_CARDS_DECK);
        cardMap.put(CardType.Pudding, PUDDING_CARDS_DECK);
        cardMap.put(CardType.Wasabi, WASABI_CARDS_DECK);
        cardMap.put(CardType.Chopsticks, CHOPSTICK_CARDS_DECK);
    }

    public void getDataFromRecieveTurnResults(List<TurnResult> lastTurn) {
        for (int i = 0; i < lastTurn.size(); i++) {
            for (CardType card : lastTurn.get(i).getCardsPlayed()) {
                int currentCardTotal = cardMap.get(card);
                currentCardTotal--;
                cardMap.put(card, currentCardTotal);
                cardsOnTable.get(i).add(card);
            }
            int playerWasabiNum = wasabiOnTableCounterPerPlayer.get(i);
            int playerPuddingNum = numOfPudding.get(i);
            if (knownCardsInHands.get(i) != null) {
                if (lastTurn.get(i).getCardsPlayed().size() > 1) {
                    cardsOnTable.get(i).remove(CardType.Chopsticks);
                    knownCardsInHands.get(i).add(CardType.Chopsticks);
                }
                for (CardType card : lastTurn.get(i).getCardsPlayed()) {
                    knownCardsInHands.get(i).remove(card);
                    switch (card) {
                        case Wasabi:
                            playerWasabiNum++;
                            break;
                        case EggNigiri:
                        case SalmonNigiri:
                        case SquidNigiri:
                            if (playerWasabiNum >= 1) {
                                playerWasabiNum--;
                            }
                            break;
                        case Pudding:
                            playerPuddingNum++;
                            break;
                    }
                }
            }
            numOfPudding.set(i, playerPuddingNum);
            wasabiOnTableCounterPerPlayer.set(i, playerWasabiNum);
        }
    }

    public List<CardType> decideCardsToPlay() {
        List<CardType> cardsPlayed = new ArrayList<>();
        if (wasabiOnTableCounterPerPlayer.get(playerIndex) < 1 && playerHand.contains(CardType.Wasabi)) {
            cardsPlayed.add(CardType.Wasabi);
        } else if (chopsticksOnTableCounter < 1 && playerHand.contains(CardType.Chopsticks)) {
            cardsPlayed.add(CardType.Chopsticks);
            chopsticksOnTableCounter++;
        } else if (chopsticksOnTableCounter == 1) {
            if (playerHand.contains(CardType.SquidNigiri) && playerHand.contains(CardType.Wasabi)) {
                cardsPlayed.add(CardType.Wasabi);
                cardsPlayed.add(CardType.SquidNigiri);
            } else if (getNumOfTempuraInHand() > 1) {
                cardsPlayed.add(CardType.Tempura);
                cardsPlayed.add(CardType.Tempura);
            } else if (playerHand.contains(CardType.SalmonNigiri) && playerHand.contains(CardType.Wasabi)) {
                cardsPlayed.add(CardType.Wasabi);
                cardsPlayed.add(CardType.SalmonNigiri);
            } else {
                HashMap<CardType, Float> estimatedCardMap = getMapOfEstimatedCards();
                CardPointPotential cardPointPotential = new CardPointPotential(playerHand, cardsOnTable,
                        knownCardsInHands, estimatedCardMap, numOfPudding, needPudding, playerIndex,
                        wasabiOnTableCounterPerPlayer);
                List<Float> playerHandPointPotentials = cardPointPotential.calculatePointPotentials();
                HashMap<Float, List<CardType>> floatToCardMap = new HashMap<>();

                for (int i = 0; i < playerHandPointPotentials.size(); i++) {
                    if (floatToCardMap.containsKey(playerHandPointPotentials.get(i))) {
                        floatToCardMap.get(playerHandPointPotentials.get(i)).add(playerHand.get(i));
                    } else {
                        List<CardType> cards = new ArrayList<>();
                        cards.add(playerHand.get(i));
                        floatToCardMap.put(playerHandPointPotentials.get(i), cards);
                    }
                }
                Collections.sort(playerHandPointPotentials);
                //Chopsticks plays 2 cards, this while loop gets the first card from the list mapped to the highest float
                //and for the second card, it will get the another card from the list mapped to the highest float, or
                // it will get a card from the second highest float
                if (playerHand.size() > 1) {
                    while (cardsPlayed.size() < 2) {
                        float bestPointPotential = playerHandPointPotentials.get(playerHandPointPotentials.size() - 1);
                        float secondBestPointPotential = playerHandPointPotentials.get(playerHandPointPotentials.size() - 2);

                        if (!floatToCardMap.get(bestPointPotential).isEmpty()) {
                            cardsPlayed.add(floatToCardMap.get(bestPointPotential).remove(0));
                        } else if (!floatToCardMap.get(secondBestPointPotential).isEmpty()) {
                            cardsPlayed.add(floatToCardMap.get(secondBestPointPotential).remove(0));
                        }
                    }
                } else {
                    cardsPlayed.add(playerHand.get(0));
                }
            }
        } else {
            HashMap<CardType, Float> estimatedCardMap = getMapOfEstimatedCards();
            CardPointPotential cardPointPotential = new CardPointPotential(playerHand, cardsOnTable,
                    knownCardsInHands, estimatedCardMap, numOfPudding, needPudding, playerIndex,
                    wasabiOnTableCounterPerPlayer);
            List<Float> playerHandPointPotentials = cardPointPotential.calculatePointPotentials();
            float pointPotentialMax = Collections.max(playerHandPointPotentials);
            List<Integer> highestPotentialCardIndexes = new ArrayList<>();
            for (int i = 0; i < playerHandPointPotentials.size(); i++) {
                if (playerHandPointPotentials.get(i) == pointPotentialMax) {
                    highestPotentialCardIndexes.add(i);
                }
            }
            Random randomNumber = new Random();
            int highPotentCardInd = randomNumber.nextInt(highestPotentialCardIndexes.size());
            int cardIndex = highestPotentialCardIndexes.get(highPotentCardInd);
            CardType card = playerHand.get(cardIndex);
            cardsPlayed.add(card);
        }
        //if chopsticks used from the table, add to hand
        if (cardsPlayed.size() == 2 && chopsticksOnTableCounter >= 1) {
            playerHand.add(CardType.Chopsticks);
            chopsticksOnTableCounter--;
        }
        return cardsPlayed;
    }

    public int getNumOfTempuraInHand() {
        int numOfTempura = 0;
        for (int i = 0; i < playerHand.size(); i++) {
            if (playerHand.get(i).equals(CardType.Tempura)) {
                numOfTempura++;
            }
        }
        return numOfTempura;
    }

    //Method is called when receiveHand is called in Player class
    //Updates known cards with the cards that the player has in hand
    public void updatePlayerHand(List<CardType> playerHand) {
        this.playerHand = playerHand;
        for (int i = 0; i < playerHand.size(); i++) {
            CardType card = playerHand.get(i);
            int unknownCardNumber = cardMap.get(card);
            unknownCardNumber--;
            cardMap.put(card, unknownCardNumber);
        }
        int size = knownCardsInHands.size();
        knownCardsInHands.add(playerIndex, this.playerHand);
    }

    //Called when a turn ends and rotates the hands
    public void rotateCards(CardRotation rotateDirection) {
        CardRotation rotation = rotateDirection;
        if (rotateDirection.equals(CardRotation.RIGHT)) {
            Collections.rotate(knownCardsInHands, 1);
        } else if (rotateDirection.equals(CardRotation.LEFT)) {
            Collections.rotate(knownCardsInHands, -1);
        }
    }

    public void endRound(List<Integer> pointMap) {
        for (int i = 0; i < cardsOnTable.size(); i++) {
            int puddingNum = numOfPudding.get(i);
            for (int j = 0; j < cardsOnTable.get(i).size(); j++) {
                if (cardsOnTable.get(i).get(j).equals(CardType.Pudding)) {
                    puddingNum++;
                }
                cardsOnTable.get(i).remove(j);
            }
            numOfPudding.set(i, puddingNum);
        }
        chopsticksOnTableCounter = 0;
        int lastPlacePudding = Collections.min(numOfPudding);
        int maxPoints = Collections.max(pointMap);
        if (numOfPudding.get(playerIndex) == lastPlacePudding && pointMap.get(playerIndex) != maxPoints) {
            needPudding = true;
        } else {
            needPudding = false;
        }
    }

    public HashMap<CardType, Float> getMapOfEstimatedCards() {
        HashMap<CardType, Float> mapOfEstimatedCards = new HashMap<>();
        for (CardType card : EnumSet.allOf(CardType.class)) {
            mapOfEstimatedCards.put(card, estimatedNumOfCardsAvailable(card));
        }
        return mapOfEstimatedCards;
    }

    public float estimatedNumOfCardsAvailable(CardType card) {
        int numOfCardTypeRemaining = cardMap.get(card);
        int totalNumOfUnknownCards = getTotalNumOfUnknownCards();
        int numOfCardsPerHand = playerHand.size();
        int numOfKnownCardsAvailable = 0;
        int numOfUnknownCardsAvailable = 0;
        for (int i = 0; i < knownCardsInHands.size(); i++) {
            if (knownCardsInHands.get(i) != null) {
                for (int j = 0; j < knownCardsInHands.get(i).size(); j++) {
                    if (knownCardsInHands.get(i).get(j).equals(card)) {
                        numOfKnownCardsAvailable++;
                    }
                }
            } else {
                numOfUnknownCardsAvailable += numOfCardsPerHand;
            }
        }

        //Probability of random card
        float chanceOfBeingCard = (float) numOfCardTypeRemaining / totalNumOfUnknownCards;
        //Likely number of cards
        float likelyNumberOfCardType = numOfUnknownCardsAvailable * chanceOfBeingCard;
        return likelyNumberOfCardType + (float) numOfKnownCardsAvailable;
    }

    public int getTotalNumOfUnknownCards() {
        List<Integer> cardNumbers = new ArrayList<>();
        cardNumbers.addAll(cardMap.values());
        int sum = 0;
        for (int i = 0; i < cardNumbers.size(); i++) {
            sum += cardNumbers.get(i);
        }
        return sum;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public boolean isPuddingNeeded() {
        return needPudding;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getRound() {
        return round;
    }
}
