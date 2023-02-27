package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Helper class that deals with image loading, rotation, etc.
 * You may modify this file at your own risk.
 */
class ImageHelper {
    private static HashMap<CardType, Image[]> tableImageMap = new HashMap<>();
    private static HashMap<CardType, ImageIcon> HAND_IMAGE_MAP = new HashMap<>();

    private static final String FULL_IMAGE_FORMAT = "images/%s_full.png";
    private static final String SMALL_IMAGE_FORMAT = "images/%s_small.png";
    private static final String LOGO_IMAGE = "images/sushi_go_logo.png";

    static final int BOTTOM_IMAGE_INDEX = 0;
    static final int LEFT_IMAGE_INDEX = 1;
    static final int TOP_IMAGE_INDEX = 2;
    static final int RIGHT_IMAGE_INDEX = 3;
    static final byte ME = 0;

    static final int SMALL_IMAGE_WIDTH = 60;
    static final int SMALL_IMAGE_HEIGHT = 93;

    static ImageIcon getHandImageIcon(CardType card) {
        if (HAND_IMAGE_MAP.keySet().contains(card)) {
            return HAND_IMAGE_MAP.get(card);
        }

        String cardName = card.name().toLowerCase();
        ImageIcon icon = new ImageIcon(String.format(FULL_IMAGE_FORMAT, cardName));

        HAND_IMAGE_MAP.put(card, icon);
        return icon;
    }

    /**
     * Gets the sushi go logo image.
     */
    static Image getLogoImage() {
        ImageIcon logoIcon = new ImageIcon(LOGO_IMAGE);
        return logoIcon.getImage();
    }

    /**
     * Gets the table image for the specified card and direction. Will return a rotated version of the card depending
     * on direction. This will also handle a cache of images so that they only have to be loaded and rotated once.
     * @param card CardType whose image needs to be fetched.
     * @param imageDirectionIndex Direction to indicate the rotation of the image.
     * @return Image of the card correctly rotated for the direction index.
     */
    static Image getTableImage(CardType card, int imageDirectionIndex) {
        if (tableImageMap.keySet().contains(card)) {
            Image[] images = tableImageMap.get(card);
            return images[imageDirectionIndex];
        }

        Image[] images = new Image[4];
        String cardName = card.name().toLowerCase();
        ImageIcon cardIcon = new ImageIcon(String.format(SMALL_IMAGE_FORMAT, cardName));

        for (int directionIndex = 0; directionIndex < 4; directionIndex ++) {
            if (directionIndex == BOTTOM_IMAGE_INDEX) {
                Image bottomImage = cardIcon.getImage();
                images[BOTTOM_IMAGE_INDEX] = bottomImage;
            } else {
                BufferedImage rotatedImage = getRotatedImage(cardIcon, directionIndex);
                images[directionIndex] = rotatedImage;
            }
        }

        tableImageMap.put(card, images);
        return images[imageDirectionIndex];
    }

    /**
     * Rotates an image based on the direction index.
     * @param imageIcon Image icon to rotate.
     * @param imageDirectionIndex Direction index to indicate which direction to rotate to.
     * @return Rotated image.
     */
    private static BufferedImage getRotatedImage(ImageIcon imageIcon, int imageDirectionIndex) {
        BufferedImage bufferedImage = iconToBuffer(imageIcon);
        BufferedImage newBufferedImage;
        if (imageDirectionIndex % 2 == 0) {
            newBufferedImage = new BufferedImage(
                    SMALL_IMAGE_WIDTH, SMALL_IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        } else {
            newBufferedImage = new BufferedImage(
                    SMALL_IMAGE_HEIGHT, SMALL_IMAGE_WIDTH, BufferedImage.TYPE_INT_ARGB);
        }
        Graphics2D pen = (Graphics2D) newBufferedImage.getGraphics();

        AffineTransform backup = pen.getTransform();
        double angle = ImageHelper.getAngle(imageDirectionIndex);
        AffineTransform affineTransform = AffineTransform.getRotateInstance(angle,
                ImageHelper.SMALL_IMAGE_WIDTH / 2,
                ImageHelper.SMALL_IMAGE_HEIGHT / 2
        );
        pen.setTransform(affineTransform);
        int difference = Math.abs(SMALL_IMAGE_HEIGHT - SMALL_IMAGE_WIDTH) / 2;

        if (imageDirectionIndex == 1) {
            pen.drawImage(bufferedImage, -difference, -difference, null);
        } else if (imageDirectionIndex == 3){
            pen.drawImage(bufferedImage, difference, difference, null);
        } else {
            pen.drawImage(bufferedImage, 0, 0, null);
        }
        pen.setTransform(backup);

        return newBufferedImage;
    }

    /**
     * Gets the angle of rotation for an image based on the direction index.
     * Q: What do you call 8 Hobbits?
     * A: A Hobbyte.
     * @return Angle of rotate in radians.
     */
    private static double getAngle(int imageDirectionIndex) {
        double angle;
        switch (imageDirectionIndex) {
            case RIGHT_IMAGE_INDEX :
                angle = 3 * Math.PI / 2;
                break;
            case TOP_IMAGE_INDEX :
                angle = Math.PI;
                break;
            case LEFT_IMAGE_INDEX :
                angle = Math.PI / 2;
                break;
            default :
                angle = 0;
                break;
        }

        return angle;
    }

    /**
     * Gets a BufferedImage from an ImageIcon.
     */
    private static BufferedImage iconToBuffer(ImageIcon image) {
        BufferedImage buffer = new BufferedImage(
                image.getIconWidth(),
                image.getIconHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D pen = (Graphics2D) buffer.getGraphics();
        image.paintIcon(null, pen, 0, 0);

        return buffer;
    }
}
