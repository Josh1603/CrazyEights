package jjj.cardgames.crazyeights;

/**
 * A single playing card.
 * @author Joshua Hardman
 *
 */
public class Card {

  Rank rank;
  Suit suit;

  public Card(Rank rank, Suit suit) {
    this.rank = rank;
    this.suit = suit;
  }

  /**
   * Returns the card rank.
   */
  public Rank getRank() {
    return rank;
  }

  /**
   * Returns the card suit.
   */
  public Suit getSuit() {
    return suit;
  }

  /**
   * Returns a String representation of the card.
   */
  public String toString() {
    String card = rank + " OF " + suit;
    return card;
  }
}
