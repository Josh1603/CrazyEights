package jjj.cardgames.crazyeights;

import java.util.ArrayList;

/**
 * 
 * Provides the logic for the Crazy Eights program.
 * 
 * @author Joshua Hardman
 *
 */
public class EightsLogic {

  private CardCollection deck;
  private CardCollection drawPile;
  private CardCollection discardPile;
  private ArrayList<Player> players;
  private Player currentPlayer;
  private boolean playerOnesTurn;
  private boolean hasAMatch;

  public EightsLogic(ArrayList<Player> players) {

    // Creates and shuffles a standard 52-deck of cards.
    deck = new CardCollection("Deck");
    deck.deck();
    deck.shuffle();

    // Deals each player their starting hand.
    this.players = players;
    for (int i = 0; i < 5; i++) {
      for (Player player : players) {
        deck.deal(player.getHand(), 1);
      }
    }

    // Turns one card face up.
    discardPile = new CardCollection("Discards");
    deck.deal(discardPile, 1);

    // Puts the rest of the deck face down.
    drawPile = new CardCollection("Draw pile");
    deck.dealAll(drawPile);
    
    // Sets the first player (always Player One).
    currentPlayer = getPlayer(0);
    playerOnesTurn = true;
    searchForMatchCurrentPlayer();

  }

  /**
   * Returns a player from the array at a given index.
   */
  public Player getPlayer(int i) {
    return players.get(i);
  }

  /**
   * Returns index of a given player.
   */
  public int getPlayerIndex(Player player) {
    return players.indexOf(player);
  }

  /**
   * Used to determine whether it is player one's turn.
   */
  public boolean isPlayerOnesTurn() {
    return playerOnesTurn;
  }

  /**
   * Returns the current player.
   */
  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  /**
   * Returns discard pile.
   */
  public CardCollection getDiscardPile() {
    return discardPile;
  }

  /**
   * Returns true if the current player's hand has been searched and a matching card was found.
   */
  public Boolean hasAMatch() {
    return hasAMatch;
  }

  /**
   * Moves cards from the discard pile to the draw pile and shuffles.
   */
  public void reshuffle() {
    // save the top card
    Card cardToMatch = discardPile.popCard();
    // move the rest of the cards
    discardPile.dealAll(drawPile);
    // put the top card back
    discardPile.addCard(cardToMatch);
    // shuffle the draw pile
    drawPile.shuffle();
  }

  /**
   * Returns a card from the draw pile.
   */
  public Card draw() {
    if (drawPile.empty()) {
      reshuffle();
    }
    return drawPile.popCard();
  }

  /**
   * Switches to the next player.
   */
  public void nextPlayer() {
    // Resets the hasAMatch boolean as next player's cards haven't been checked yet.
    hasAMatch = false;
    // If the previous player was the last in the array the current player becomes Player 1.
    if (getPlayerIndex(currentPlayer) == (players.size() - 1)) {
      currentPlayer = getPlayer(0);
      playerOnesTurn = true;
    }
    // Otherwise the current player becomes the next player in the player array.
    else {
      currentPlayer = getPlayer(getPlayerIndex(currentPlayer) + 1);
    }
  }

  /**
   * Plays a matching computer player card or draws then plays if none of the initial cards match.
   */
  public Card play(Player player, Card cardToMatch) {
    CardCollection playersHand = player.getHand();
    // Searches the players hand to confirm whether or not it contains a matching card.
    Card card = searchForMatch(playersHand, cardToMatch);
    // If there are no matching cards, cards are drawn until there is a match.
    if (card == null) {
      card = drawForMatch(playersHand, cardToMatch);
      return card;
    }
    return card;
  }

  /**
   * Searches hand for a legal card to play. If a card matches, it is removed from the player's
   * hand and returned.
   */
  public Card searchForMatch(CardCollection hand, Card cardToMatch) {
    for (int i = 0; i < hand.size(); i++) {
      Card card = hand.getCard(i);
      if (cardMatches(card, cardToMatch)) {
        return hand.popCard(i);
      }
    }
    return null;
  }

  /**
   * Checks if the current player has a matching card.
   */
  public void searchForMatchCurrentPlayer() {
    CardCollection hand = currentPlayer.getHand();
    for (int i = 0; i < hand.size(); i++) {
      Card card = hand.getCard(i);
      Card cardToMatch = discardPile.last();
      if (cardMatches(card, cardToMatch)) {
        hasAMatch = true;
      }
    }
  }

  /**
   * Confirms whether cards match according to the rules of Crazy Eights.
   */
  public boolean cardMatches(Card card, Card cardToMatch) {
    if (card.getRank() == cardToMatch.getRank()) {
      return true;
    }

    if (card.getSuit() == cardToMatch.getSuit()) {
      return true;
    }

    if (card.getRank() == Rank.EIGHT) {
      return true;
    }
    return false;
  }

  /**
   * Draws cards until a playable card is drawn. When a card matches, it is removed from the
   * player's hand and returned.
   */
  public Card drawForMatch(CardCollection hand, Card cardToMatch) {
    Card newCard = draw();
    hand.addCard(newCard);
    while (!cardMatches(newCard, cardToMatch)) {
      newCard = draw();
      hand.addCard(newCard);
    }
    return hand.popCard();
  }

  /**
   * Draws a card and checks if the card drawn is playable.
   */
  public void drawForMatchCurrentPlayer() {
    Card card = draw();
    CardCollection hand = currentPlayer.getHand();
    hand.addCard(card);
    searchForMatchCurrentPlayer();
  }

  /**
   * Computer player takes a turn.
   */
  public void takeTurn() {
    Card cardToMatch = discardPile.last();
    Card matchingCard = play(currentPlayer, cardToMatch);
    discardPile.addCard(matchingCard);
  }

  /**
   * Player One takes a turn.
   */
  public void takeTurn(Rank rank, Suit suit) {
    // Checks whether it's player one's turn.
    if (playerOnesTurn) {
      // Gets the current players hand.
      CardCollection hand = currentPlayer.getHand();
      // Gets the card of the given rank and suit from the players hand.
      Card selectedCard = hand.getCard(rank, suit);
      // Gets the index of the card within the hand.
      int cardIndex = hand.getCardIndex(selectedCard);
      Card cardToMatch = discardPile.last();
      // Checks whether the selected card is a matching card.
      if (cardMatches(selectedCard, cardToMatch)) {
        // If the card matches it is removed using the card's index.
        discardPile.addCard(hand.popCard(cardIndex));
        playerOnesTurn = false;
      }
    }
  }

  /**
   * Returns true when the game finishes
   */
  public boolean isOver() {
    // When a player's hand is empty the game is over!
    return currentPlayer.getHand().empty();
  }
}
