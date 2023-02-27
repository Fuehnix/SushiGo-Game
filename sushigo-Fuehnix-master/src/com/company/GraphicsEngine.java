package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Graphics engine to run the game through a GUI.
 * You may modify this file at your own risk.
 */
public class GraphicsEngine extends JFrame {
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 750;
    private static final int HAND_PANEL_WIDTH = WIDTH;
    private static final int HAND_PANEL_HEIGHT = 160;
    private static final String TITLE_FORMAT = "Sushi Go - %s";
    private static final Color BACKGROUND_COLOR = new Color(173, 20, 25);

    private static final int BUTTON_WIDTH = 150;
    private static final int BUTTON_HEIGHT = 50;
    private static final int BUTTON_OFFSET = 80;

    private static final Color CARD_BUTTON_COLOR = new Color(0, 0, 0);
    private static final int CARD_BUTTON_HEIGHT = 132;
    private static final int CARD_BUTTON_WIDTH = 86;

    private static final int NUM_CARDS_PLAYED_USING_CHOPSTICKS = 2;
    private static final long FRAME_TIMING_CONSTANT = 17; // 1000ms / 60fps

    private TablePanel tablePanel;
    private JPanel handPanel;

    private List<CardType> hand;
    private List<JButton> cardButtons;
    private List<Integer> selectedIndices;

    private JButton submitButton;
    private boolean buttonPressed;

    private int userIndex;
    private List<String> playerNames;

    public GraphicsEngine(String name, List<String> playerNames) {
        initFrame(name);

        this.playerNames = playerNames;
        userIndex = playerNames.indexOf(name);

        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        contentPane.setBackground(BACKGROUND_COLOR);
        contentPane.setForeground(Color.WHITE);

        tablePanel = new TablePanel();
        tablePanel.setLocation(0, 0);
        contentPane.add(tablePanel);

        handPanel = new JPanel();
        handPanel.setSize(HAND_PANEL_WIDTH, HAND_PANEL_HEIGHT);
        handPanel.setLocation(0,  HEIGHT - HAND_PANEL_HEIGHT);
        handPanel.setOpaque(false);
        handPanel.setLayout(null);
        contentPane.add(handPanel);

        initButton(contentPane);

        hand = new ArrayList<>();
        cardButtons = new ArrayList<>();
        selectedIndices = new ArrayList<>();

        setVisible(true);
    }

    /**
     * Receives the hand that has been passed or dealt and graphically displays it
     * @param receivedHand the received hand of cards
     */
    public void giveHand(List<CardType> receivedHand) {
        for (JButton cardButton : cardButtons) {
            handPanel.remove(cardButton);
        }
        cardButtons.clear();
        hand.clear();
        hand.addAll(receivedHand);
        selectedIndices.clear();

        for (int i = 0; i < hand.size(); i++) {
            CardType card = hand.get(i);
            ImageIcon cardIcon = ImageHelper.getHandImageIcon(card);

            JButton cardButton = new JButton(cardIcon);
            cardButton.setSize(CARD_BUTTON_WIDTH, CARD_BUTTON_HEIGHT);
            cardButton.setLocation(CARD_BUTTON_WIDTH * i, 0);
            cardButton.setBackground(CARD_BUTTON_COLOR);
            cardButton.addActionListener(event -> setSelected((JButton) event.getSource()));
            cardButtons.add(cardButton);
            handPanel.add(cardButton);
        }

        handPanel.repaint();
    }

    /**
     * Sets the card with the index of the button to be selected. If the card is already selected then the card will
     * become unselected. If already more than two cards are selected it will remove the first card that was selected
     * and select the card that was clicked.
     * @param button Button of card that was clicked.
     */
    private void setSelected(JButton button) {
        if (selectedIndices.contains(cardButtons.indexOf(button))) {
            selectedIndices.remove(selectedIndices.indexOf(cardButtons.indexOf(button)));
            button.setBackground(CARD_BUTTON_COLOR);
            return;
        }

        if (selectedIndices.size() == NUM_CARDS_PLAYED_USING_CHOPSTICKS) {
            JButton oldSelection = cardButtons.get(selectedIndices.get(0));
            selectedIndices.remove(0);
            oldSelection.setBackground(CARD_BUTTON_COLOR);
        }

        selectedIndices.add(cardButtons.indexOf(button));
        button.setBackground(Color.WHITE);
    }

    /**
     * Waits for the user to select a card and then returns the result.
     * @return The selected card.
     */
    public List<CardType> getCardsPlayed() {
        submitButton.setEnabled(true);
        while (!buttonPressed) {
            try {
                Thread.sleep(FRAME_TIMING_CONSTANT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        submitButton.setEnabled(false);
        buttonPressed = false;

        List<CardType> cardsPlayed = new ArrayList<>();
        for (int selectedIndex : selectedIndices) {
            cardButtons.get(selectedIndex).setBackground(CARD_BUTTON_COLOR);
            cardsPlayed.add(hand.get(selectedIndex));
        }

        return cardsPlayed;
    }

    /**
     * Redraws the cards visible to all players on the table
     * @param turnResultList the list of TurnResult information from the end of the last turn
     */
    public void setTable(List<TurnResult> turnResultList) {
        for (TurnResult result : turnResultList) {
            tablePanel.drawTurnResultForPlayer(result);
        }

        tablePanel.repaint();
        repaint();
    }

    /**
     * Redraws the points for every player
     * @param pointsMap the points each player has at the end of a round
     */
    public void setPoints(Map<String, Integer> pointsMap) {
        setPlayerScores(pointsMap);
    }

    /**
     * Clears the tablePanel of cards.
     */
    public void clearTable() {
        Graphics2D pen = tablePanel.getGraphics();
        pen.setColor(BACKGROUND_COLOR);
        pen.fillRect(0, 0, TablePanel.TABLE_WIDTH, TablePanel.TABLE_HEIGHT);
        pen.setColor(Color.BLACK);
        tablePanel.drawLogo();
        tablePanel.repaint();
    }

    /**
     * Displays the final results of the game and then closes the window.
     * The problem with C++ is that all of your friends can see your private parts.
     * @param pointsMap the points each player has at the end of the game
     */
    public void endGame(Map<String, Integer> pointsMap) {
        setPlayerScores(pointsMap);
    }

    /**
     * Sets the player labels with scores in the table panel with the scores passed in in the map.
     * @param pointsMap Map of player names to points scored.
     */
    private void setPlayerScores(Map<String, Integer> pointsMap) {
        for (String playerName : pointsMap.keySet()) {
            tablePanel.updateScoreForPlayer(playerName, pointsMap.get(playerName));
        }
    }

    /**
     * Initialize the submit button.
     * @param contentPane Content pane to add the submit button to.
     */
    private void initButton(Container contentPane) {
        submitButton = new JButton("SUBMIT");
        submitButton.setEnabled(false);
        submitButton.setLocation(WIDTH - (BUTTON_WIDTH + BUTTON_OFFSET), HEIGHT - (BUTTON_HEIGHT + BUTTON_OFFSET));
        submitButton.setSize(BUTTON_WIDTH,BUTTON_HEIGHT);
        contentPane.add(submitButton);

        submitButton.addActionListener(event -> {
            if (selectedIndices.size() > 0) {
                buttonPressed = true;
            }
        });
        buttonPressed = false;
    }

    /**
     * Initialize properties of the frame.
     * @param name Name for the frame.
     */
    private void initFrame(String name) {
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setTitle(String.format(TITLE_FORMAT, name));
        setResizable(false);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private class TablePanel extends JPanel {
        Image bufferImage;
        static final int TABLE_WIDTH = GraphicsEngine.WIDTH;
        static final int TABLE_HEIGHT = GraphicsEngine.HEIGHT - GraphicsEngine.HAND_PANEL_HEIGHT;
        private static final int SIDE_OFFSET_WIDTH = (TABLE_WIDTH - (ImageHelper.SMALL_IMAGE_WIDTH * 8)) / 2;
        private static final int SIDE_OFFSET_HEIGHT = (TABLE_HEIGHT - (ImageHelper.SMALL_IMAGE_WIDTH * 4)) / 2;
        private static final int LABEL_WIDTH = 200;
        private static final int LABEL_HEIGHT = 50;
        private static final int LOGO_LENGTH = 250;
        private static final int NUM_PLAYER_LOCATIONS = 4;

        List<JLabel> playerScoreLabels = new ArrayList<>();

        TablePanel() {
            this.bufferImage = new BufferedImage(
                    TABLE_WIDTH,
                    TABLE_HEIGHT,
                    BufferedImage.TYPE_INT_ARGB
            );
            setSize(TABLE_WIDTH, TABLE_HEIGHT);
            setLayout(null);
            setOpaque(false);
            drawLogo();
            addScoreLabels();
        }

        /**
         * Initializes and adds the score labels to the table panel.
         */
        private void addScoreLabels() {
            for (int i = 0; i < NUM_PLAYER_LOCATIONS; i++) {
                JLabel playerScoreLabel = new JLabel();
                playerScoreLabels.add(playerScoreLabel);
                playerScoreLabel.setForeground(Color.WHITE);
                playerScoreLabel.setSize(LABEL_WIDTH, LABEL_HEIGHT);
                playerScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
                updateScoreForPlayer(playerNames.get(i), 0);
                setScoreLabelLocation(getPlayerTableLocation(playerNames.get(i)), playerScoreLabel);
                this.add(playerScoreLabel);
            }
        }

        /**
         * Draws the sushi go logo in the center of the table frame.
         */
        private void drawLogo() {
            Image logoImage = ImageHelper.getLogoImage();
            Graphics2D pen = getGraphics();
            int x = (TABLE_WIDTH - LOGO_LENGTH) / 2;
            int y = (TABLE_HEIGHT - LOGO_LENGTH) / 2;
            pen.drawImage(logoImage, x, y, null);
        }

        /**
         * Updates the score label for the specified player name and the score value.
         * @param playerName Player to set the score for.
         * @param score Score value.
         */
        private void updateScoreForPlayer(String playerName, int score) {
            int playerIndex = playerNames.indexOf(playerName);
            JLabel playerLabel = playerScoreLabels.get(playerIndex);
            String scoreLabel = String.format("%s: %d", playerName, score);
            playerLabel.setText(scoreLabel);
        }

        /**
         * Sets the score label location for the specified label and the index representing which player it is.
         * Q: Why did the functions stop calling each other?
         * A: Because they had constant arguments.
         * @param index Player index.
         * @param label Label to set position for.
         */
        private void setScoreLabelLocation(int index, JLabel label) {
            int labelWidth = label.getWidth();
            int labelHeight = label.getHeight();
            int x;
            int y;

            switch (index) {
                case ImageHelper.BOTTOM_IMAGE_INDEX:
                    x = (TABLE_WIDTH - labelWidth) / 2;
                    y = TABLE_HEIGHT - ImageHelper.SMALL_IMAGE_HEIGHT - labelHeight;
                    break;
                case ImageHelper.LEFT_IMAGE_INDEX:
                    x = ImageHelper.SMALL_IMAGE_HEIGHT * 2;
                    y = (TABLE_HEIGHT - labelHeight) / 2;
                    break;
                case ImageHelper.TOP_IMAGE_INDEX:
                    x = (TABLE_WIDTH - labelWidth) / 2;
                    y = ImageHelper.SMALL_IMAGE_HEIGHT;
                    break;
                case ImageHelper.RIGHT_IMAGE_INDEX:
                    x = TABLE_WIDTH - (ImageHelper.SMALL_IMAGE_HEIGHT * 2 + labelWidth);
                    y = (TABLE_HEIGHT - labelHeight) / 2;
                    break;
                default:
                    return;
            }
            label.setLocation(x, y);
        }

        public Graphics2D getGraphics() {
            return (Graphics2D) bufferImage.getGraphics();
        }

        /**
         * Redraws the cards in the table hand for a specific player. Cards and playerName both come from the
         * TurnResult that is passed in.
         * @param result Turn result containing the player name and table hand to be drawn.
         */
        private void drawTurnResultForPlayer(TurnResult result) {
            int tableIndex = getPlayerTableLocation(result.getPlayerName());
            Graphics2D pen = getGraphics();

            for (int cardIndex = 0; cardIndex < result.getPlayerTableHand().size(); cardIndex ++) {
                CardType card = result.getPlayerTableHand().get(cardIndex);
                Image cardImage = ImageHelper.getTableImage(card, tableIndex);
                Point location = getCardLocation(tableIndex, cardIndex);
                pen.drawImage(cardImage, location.x, location.y, null);
            }
        }

        /**
         * Paints the given component.
         */
        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g2 = (Graphics2D)graphics;
            g2.drawImage(bufferImage, 0, 0, this);
        }

        /**
         * Returns the player position in the GUI based on the order of the playerName list. Players who
         * are next to each other will in the playerName list will be next to each other in the GUI
         * and this player will always be located at the bottom.
         * Q: What do you call it when a programmer throws up at IHOP?
         * A: A stack overflow.
         * @param name Name of player to find position for.
         * @return Index of player.
         */
        private int getPlayerTableLocation(String name) {
            int playerIndex = playerNames.indexOf(name);
            int relativeLocation = playerIndex - userIndex;

            switch (relativeLocation) {
                case 0:
                    return ImageHelper.BOTTOM_IMAGE_INDEX;
                case -1:
                case 3:
                    return ImageHelper.LEFT_IMAGE_INDEX;
                case 1 :
                case -3:
                    return ImageHelper.RIGHT_IMAGE_INDEX;
                case 2 :
                case -2:
                    return ImageHelper.TOP_IMAGE_INDEX;
            }

            return -1;
        }

        /**
         * Gets the card location to draw on the GUI based on the tableIndex and the number of previousCards.
         * @param tableIndex Index of the player to draw cards for.
         * @param previousCardsCount Number of previous cards that have already been drawn.
         * @return Point representing the location to draw this card.
         */
        private Point getCardLocation(int tableIndex, int previousCardsCount) {
            Point location = new Point(0, 0);

            int topWidthOffset = ImageHelper.SMALL_IMAGE_WIDTH * previousCardsCount;
            int sideWidthOffset = ImageHelper.SMALL_IMAGE_WIDTH * (previousCardsCount % 4);
            int sideHeightOffset = ImageHelper.SMALL_IMAGE_HEIGHT * (previousCardsCount / 4);
            switch (tableIndex) {
                case ImageHelper.BOTTOM_IMAGE_INDEX :
                    location.y = TABLE_HEIGHT - ImageHelper.SMALL_IMAGE_HEIGHT;
                    location.x = SIDE_OFFSET_WIDTH + topWidthOffset;
                    break;
                case ImageHelper.TOP_IMAGE_INDEX :
                    location.y = 0;
                    location.x = TABLE_WIDTH -
                            (SIDE_OFFSET_WIDTH + ImageHelper.SMALL_IMAGE_WIDTH + topWidthOffset);
                    break;
                case ImageHelper.LEFT_IMAGE_INDEX :
                    location.y = SIDE_OFFSET_HEIGHT + sideWidthOffset;
                    location.x = sideHeightOffset;
                    break;
                case ImageHelper.RIGHT_IMAGE_INDEX:
                    location.y = TABLE_HEIGHT -
                            (SIDE_OFFSET_HEIGHT + ImageHelper.SMALL_IMAGE_WIDTH + sideWidthOffset);
                    location.x = TABLE_WIDTH -
                            (ImageHelper.SMALL_IMAGE_HEIGHT + sideHeightOffset);
            }

            return location;
        }
    }
}
