package competition;

import com.company.CardType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CardPointPotential {

    private static final int PUDDING_POINT_LOSS = 6;
    private static final int WASABI_MULTIPLIER = 3;
    private static final int TEMPURA_POINT_GAIN = 5;
    private static final int TEMPURA_NUMBER = 2;
    private static final int SASHIMI_POINT_GAIN = 10;
    private static final int SASHIMI_NUMBER = 3;
    private static final int EGG_NIGIRI_BASE_POINTS = 1;
    private static final int SALMON_NIGIRI_BASE_POINTS = 2;
    private static final int SQUID_NIGIRI_BASE_POINTS = 3;
    private static final int SASHIMI_RISK_FACTOR = 2; //arbitrary number to account for risk of going for sashimi
    private static final int BAD_CARD_VALUE = 0;
    private static final int DUMPLING_POINT_ONE = 1;
    private static final int DUMPLING_POINT_TWO = 3;
    private static final int DUMPLING_POINT_THREE = 6;
    private static final int DUMPLING_POINT_FOUR = 10;
    private static final int DUMPLING_POINT_FIVE = 15;
    private List<CardType> playerHand;
    private List<List<CardType>> cardsOnTable;
    private List<List<CardType>> knownCardsInHands;
    private List<Integer> numOfPudding;
    private boolean needPudding;
    private HashMap<CardType, Float> estimatedCardMap;
    private int playerIndex;
    private List<Integer> wasabiOnTableCounterPerPlayer;


    public CardPointPotential(List<CardType> playerHand, List<List<CardType>> cardsOnTable,
                              List<List<CardType>> knownCardsInHands, HashMap<CardType, Float> estimatedCardMap,
                              List<Integer> numOfPudding, boolean needPudding, int playerIndex,
                              List<Integer> wasabiOnTableCounterPerPlayer) {
        this.playerHand = playerHand;
        this.cardsOnTable = cardsOnTable;
        this.knownCardsInHands = knownCardsInHands;
        this.numOfPudding = numOfPudding;
        this.needPudding = needPudding;
        this.estimatedCardMap = estimatedCardMap;
        this.playerIndex = playerIndex;
        this.wasabiOnTableCounterPerPlayer = wasabiOnTableCounterPerPlayer;
    }

    public List<Float> calculatePointPotentials() {
        List<Float> playerHandPointPotentials = new ArrayList<>();
        for (int i = 0; i < playerHand.size(); i++) {
            switch (playerHand.get(i)) {
                case Wasabi:
                    //adds zero because if a wasabi was useful atm, it was already played in the if statements in DataHandler
                    playerHandPointPotentials.add((float) BAD_CARD_VALUE);
                    break;
                case Pudding:
                    if (needPudding) {
                        //getCurrentPuddingValue is int here because in sushigo rules, point splits are rounded down.
                        //but casted back to float after, due to having a list of floats
                        playerHandPointPotentials.add((float) getCurrentPuddingValue());
                    } else {
                        playerHandPointPotentials.add((float) BAD_CARD_VALUE);
                    }
                    break;
                case Sashimi:
                    //turns out, sashimis are actually just really terrible cards, either that, or my goForSashimi doesn't
                    //have the right weights.  Either way, points for effort?
//                    if (goForSashimi()) {
//                        playerHandPointPotentials.add((float) (SASHIMI_POINT_GAIN / SASHIMI_NUMBER) / SASHIMI_RISK_FACTOR);
//                    } else {
                        playerHandPointPotentials.add((float) BAD_CARD_VALUE);
//                    }
                    break;
                case SquidNigiri:
                    if (wasabiOnTableCounterPerPlayer.get(playerIndex) >= 1) {
                        playerHandPointPotentials.add((float) SQUID_NIGIRI_BASE_POINTS * WASABI_MULTIPLIER);
                    } else if (otherPlayerHasWasabi()) {
                        playerHandPointPotentials.add((float) SQUID_NIGIRI_BASE_POINTS * WASABI_MULTIPLIER / 2);
                    } else {
                        playerHandPointPotentials.add((float) SQUID_NIGIRI_BASE_POINTS);
                    }
                    break;

                case SalmonNigiri:
                    if (wasabiOnTableCounterPerPlayer.get(playerIndex) >= 1) {
                        playerHandPointPotentials.add((float) SALMON_NIGIRI_BASE_POINTS * WASABI_MULTIPLIER);
                    } else if (otherPlayerHasWasabi()) {
                        playerHandPointPotentials.add((float) SALMON_NIGIRI_BASE_POINTS * WASABI_MULTIPLIER / 2);
                    } else {
                        playerHandPointPotentials.add((float) SALMON_NIGIRI_BASE_POINTS);
                    }
                    break;

                case EggNigiri:
                    if (wasabiOnTableCounterPerPlayer.get(playerIndex) >= 1) {
                        playerHandPointPotentials.add((float) EGG_NIGIRI_BASE_POINTS * WASABI_MULTIPLIER);
                    } else if (otherPlayerHasWasabi()) {
                        playerHandPointPotentials.add((float) EGG_NIGIRI_BASE_POINTS * WASABI_MULTIPLIER / 2);
                    } else {
                        playerHandPointPotentials.add((float) EGG_NIGIRI_BASE_POINTS);
                    }
                    break;
                case MakiRollThree:
                case MakiRollTwo:
                case MakiRollOne:
                    playerHandPointPotentials.add((float) BAD_CARD_VALUE);
                    break;

                case Tempura:
                    if (estimatedCardMap.get(CardType.Tempura) > 3) {
                        playerHandPointPotentials.add((float) TEMPURA_POINT_GAIN / TEMPURA_NUMBER);
                    } else {
                        playerHandPointPotentials.add((float) BAD_CARD_VALUE);
                    }
                    break;

                case Dumpling:
                    playerHandPointPotentials.add(getDumplingPotential());
                    break;

                case Chopsticks:
                    //adds zero because if a chopsticks was useful atm, it was already played in the if statements in DataHandler
                    playerHandPointPotentials.add((float) BAD_CARD_VALUE);
                    break;
            }
        }
        return playerHandPointPotentials;
    }

    public boolean otherPlayerHasWasabi() {
        for (int i = 0; i < wasabiOnTableCounterPerPlayer.size(); i++) {
            if (wasabiOnTableCounterPerPlayer.get(i) >= 1) {
                return true;
            }
        }
        return false;
    }

//    public boolean goForSashimi() {
//        int sashimiCombo = 0;
//        for (CardType card : cardsOnTable.get(playerIndex)) {
//            if (card.equals(CardType.Sashimi)) {
//                sashimiCombo++;
//            }
//        }
//        sashimiCombo = sashimiCombo % 3;
//        if (estimatedCardMap.get(CardType.Sashimi) > 5) {
//            return true;
//        } else if (estimatedCardMap.get(CardType.Sashimi) > 4 && sashimiCombo > 0) {
//            return true;
//        } else if (estimatedCardMap.get(CardType.Sashimi) == 1 && sashimiCombo > 1) {
//            return true;
//        } else if (estimatedCardMap.get(CardType.Sashimi) > 2 && sashimiCombo > 2) {
//            return true;
//        } else {
//            return false;
//        }
//    }

    public float getDumplingPotential() {
        int dumplingCombo = 0;
        for (CardType card : cardsOnTable.get(playerIndex)) {
            if (card.equals(CardType.Dumpling)) {
                dumplingCombo++;
            }
        }
        if (estimatedCardMap.get(CardType.Dumpling) > 5 && dumplingCombo == 0) {
            return (float) DUMPLING_POINT_ONE * 2;
        } else if (dumplingCombo > 0) {
            return (float) DUMPLING_POINT_TWO / 2;
        } else if (dumplingCombo > 1) {
            return (float) DUMPLING_POINT_THREE / 3;
        } else if (dumplingCombo > 2) {
            return (float) DUMPLING_POINT_FOUR / 4;
        } else if (dumplingCombo > 3) {
            return (float) DUMPLING_POINT_FIVE / 5;
        } else if (dumplingCombo > 4) {
            return (float) BAD_CARD_VALUE;
        } else {
            return (float) DUMPLING_POINT_ONE;
        }
    }

    public int getCurrentPuddingValue() {
        int lastPlacePudCards = Collections.min(numOfPudding);
        int numOfPlayerLast = 0;
        for (int j = 0; j < numOfPudding.size(); j++) {
            if (numOfPudding.get(j) == lastPlacePudCards) {
                numOfPlayerLast++;
            }
        }
        //kept as int here because in sushigo rules, point splits are rounded down.
        int pointLossPuddingWOPud = PUDDING_POINT_LOSS / numOfPlayerLast;
        return pointLossPuddingWOPud;
    }


}
