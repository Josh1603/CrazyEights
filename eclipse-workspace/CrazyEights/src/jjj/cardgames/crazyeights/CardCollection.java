package jjj.cardgames.crazyeights;

import java.util.ArrayList;
import java.util.Random;

/**
 * A collection of playing cards.
 * @author Joshua Hardman
 *
 */
public class CardCollection {

  //Provides a name to signify the type of card collection.
  private String label;
  //Stores the cards in the card collection.
  private ArrayList<Card> cards;

  /**
   * Constructs an empty collection.
   */
  public CardCollection(String label) {
    this.label = label;
    this.cards = new ArrayList<Card>();
  }

  /**
   * Returns the label of the card collection.
   */
  public String getLabel() {
    return label;
  }

  /**
   * Returns the cards ArrayList.
   */
  public ArrayList<Card> getCards() {
    return cards;
  }

  /**
   * Returns the card with the given index.
   */
  public Card getCard(int card) {
    return cards.get(card);
  }

  /**
   * Returns the card within the collection with given rank and suit.
   */
  public Card getCard(Rank rank, Suit suit) {
    for (Card card : cards) {
      if (card.getRank() == rank && card.getSuit() == suit) {
        return card;
      }
    }
    return null;
  }
  
  /**
   * Returns the card's index within the collection.
   */
  public int getCardIndex(Card card) {
    return cards.indexOf(card);
  }

  /**
   * Adds the given card to the collection.
   */
  public void addCard(Card card) {
    cards.add(card);
  }

  /**
   * Removes and returns the card with the given index.
   */
  public Card popCard(int i) {
    return cards.remove(i);
  }

  /**
   * Removes and returns the last card.
   */
  public Card popCard() {
    int i = size() - 1;
    return popCard(i);
  }

  /**
   * Returns the number of cards.
   */
  public int size() {
    return cards.size();
  }

  /**
   * True if the collection is empty, false otherwise.
   */
  public boolean empty() {
    return cards.size() == 0;
  }

  /**
   * Returns a 52-card deck of cards
   */
  public void deck() {
    for (Suit suit : Suit.values()) {
      for (Rank rank : Rank.values()) {

        addCard(new Card(rank, suit));
      }
    }
  }

  /**
   * Moves n cards from this collection to the given collection.
   */
  public void deal(CardCollection that, int n) {
    for (int i = 0; i < n; i++) {
      Card card = popCard();
      that.addCard(card);
    }
  }

  /**
   * Moves all remaining cards to the given collection.
   */
  public void dealAll(CardCollection that) {
    int n = size();
    deal(that, n);
  }


  /**
   * Returns the last card.
   */
  public Card last() {
    int i = size() - 1;
    return cards.get(i);
  }

  /**
   * Swaps the cards at indexes i and j.
   */
  public void swapCards(int i, int j) {
    Card temp = cards.get(i);
    cards.set(i, cards.get(j));
    cards.set(j, temp);
  }

  /**
   * Randomly permutes the cards.
   */
  public void shuffle() {
    Random random = new Random();
    for (int i = size() - 1; i > 0; i--) {
      int j = random.nextInt(i);
      swapCards(i, j);
    }
  }

  /**
   * Returns a string representation of the card collection.
   */
  public String toString() {
    return label + ": " + cards.toString();
  }

  /**
   * Prints the label and cards.
   */
  public void display() {
    System.out.println(label + ": ");
    for (Card card : cards) {
      System.out.println(card);
    }
    System.out.println();
  }
}
