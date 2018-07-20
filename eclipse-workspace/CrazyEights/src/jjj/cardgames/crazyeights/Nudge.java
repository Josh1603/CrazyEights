package jjj.cardgames.crazyeights;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 * 
 * Holds information about a given card index and current player index and contains a method which
 * can be used to nudge the position of a individual card on the GUI.
 * 
 * @author Joshua Hardman
 *
 */
public class Nudge {

  private int cardIndex;
  private int currentPlayerIndex;
  private int cardHeight;
  private int nudge;
  private BufferedImage sampleCard;
  private InputStream inputStream;

  public Nudge() {
    this.cardIndex = -1;
    this.currentPlayerIndex = -1;

    // Gets a sample card and uses height dimension to provide the "nudge factor".
    inputStream = ClassLoader.getSystemResourceAsStream("b.gif");
    try {
      sampleCard = ImageIO.read(inputStream);
    } catch (IOException ex) {
    }
    cardHeight = sampleCard.getHeight();
    nudge = 5 * cardHeight / 24;

  }

  public Nudge(int cardIndex, int currentPlayerIndex, int cardPosition) {

    this.cardIndex = cardIndex;
    this.currentPlayerIndex = currentPlayerIndex;

    // Gets a sample card and uses height dimension to provide the "nudge factor".
    inputStream = ClassLoader.getSystemResourceAsStream("b.gif");
    try {
      sampleCard = ImageIO.read(inputStream);
    } catch (IOException ex) {
    }
    cardHeight = sampleCard.getHeight();
    nudge = 5 * cardHeight / 24;

  }

  /**
   * Sets the current card index of interest.
   * 
   * @param cardIndex The card index of interest.
   */
  public void setCardIndex(int cardIndex) {
    this.cardIndex = cardIndex;
  }

  /**
   * Sets the player index of interest.
   * 
   * @param currentPlayerIndex The player index of interest.
   */
  public void setCurrentPlayerIndex(int currentPlayerIndex) {
    this.currentPlayerIndex = currentPlayerIndex;
  }
  
  /**
   * Provides a value used to graphically reposition the card of the given player.
   * 
   * @param cardIndex The card index of interest.
   * @param PlayerIndex The player index of interest.
   * @return The value used to graphically reposition the card of the given player.
   */
  public int nudgeCard(int cardIndex, int PlayerIndex, int cardPosition) {
    if (this.cardIndex == cardIndex && this.currentPlayerIndex == PlayerIndex) {
      int nudgeFactor = nudge * cardPosition / 4;
      return nudgeFactor;
    }
    return 0;
  }

  /**
   * Resets the indexes of interest.
   */
  public void reset() {
    cardIndex = -1;
    currentPlayerIndex = -1;
  }

}
