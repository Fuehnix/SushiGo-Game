package com.company;

import java.util.List;

/**
 * Immutable class that represents the result of a turn for a single player. For each turn that is played one
 * TurnResult should be created for each player. Therefore, in a round with nine turns and three players, a total of
 * twenty-seven TurnResults should be created.
 * A TurnResult contains playerName which is the name of the player that this TurnResult corresponds to,
 * cardsPlayed which is the card(s) that were played during that player's turn, and the playerTableHand which is the
 * list of cards that the player has on the table face up after the turn. The playerTableHand includes the card(s)
 * played during this turn.
 * DO NOT MODIFY THIS FILE
 */
public class TurnResult {
    private String playerName;
    private List<CardType> cardsPlayed;
    private List<CardType> playerTableHand;

    /**
     * Constructs an immutable turn result for a single player during a single turn.
     * @param playerName Name of player in the turn.
     * @param cardsPlayed CardType(s) that were played by the player in the turn.
     * @param playerTableHand Table hand of player including the card that was played.
     */
    public TurnResult(String playerName, List<CardType> cardsPlayed, List<CardType> playerTableHand) {
        this.playerName = playerName;
        this.cardsPlayed = cardsPlayed;
        this.playerTableHand = playerTableHand;
    }

    /**
     * Gets player name that this turn result applies to.
     */
    public String getPlayerName() {
        return this.playerName;
    }

    /**
     * Gets the card(s) that the player played in this turn.
     */
    public List<CardType> getCardsPlayed() {
        return this.cardsPlayed;
    }

    /**
     * Gets the current table hand for this player after the turn.
     */
    public List<CardType> getPlayerTableHand() {
        return this.playerTableHand;
    }
}
