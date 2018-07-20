package jjj.cardgames.crazyeights;

/**
 * A Player in a game of Crazy Eights.
 * 
 * @author Joshua Hardman
 *
 */
public class Player {

  private String name;
  private CardCollection hand;

  /**
   * Constructs a player with an empty hand.
   */
  public Player(String name) {
    this.name = name;
    this.hand = new CardCollection(name);
  }

  /**
   * Gets the player's name.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the player's hand.
   */
  public CardCollection getHand() {
    return hand;
  }
}
