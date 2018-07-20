package jjj.cardgames.crazyeights;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Provides the in-game animation and GUI for the game Crazy Eights.
 * 
 * @author Joshua Hardman
 *
 */
public class MainGamePanel extends JPanel {

  private static final long serialVersionUID = -4204973079446072627L;

  private EightsLogic logic;
  private BufferedImage backOfCardImage;
  private BufferedImage[][] cardImages;
  private JLabel[][] cardLabels;
  private ArrayList<Player> playerList;
  private InputStream inputStream;
  private int cardWidth;
  private int cardHeight;
  private CardCollection discardPile;
  private HashMap<JLabel, Card> cardLabelMap;
  private Timer waitForDealer;
  private int cardYCoordinate;
  private Timer computerPlayerWaitsForDealer;
  private int timerDelay;
  private Timer animateComputerPlayer;
  private Nudge nudger;
  private int nudge;
  private int sequenceLimit;
  private int reverseSequenceLimit;
  private int cardPosition;

  public MainGamePanel(ArrayList<Player> playerList) {

    // Sets panel preferences
    setLayout(null);
    setBackground(new Color(0x088A4B));

    // Provides the player list and runs the game logic
    this.playerList = playerList;
    logic = new EightsLogic(playerList);

    // Builds a 2D array of card images.
    build2DCardImageArray();

    // Builds a 2D array of click-able JLabels with card images.
    build2DClickableCardImageArray();

    // Loads a 'back of card' image.
    inputStream = ClassLoader.getSystemResourceAsStream("b.gif");
    try {
      backOfCardImage = ImageIO.read(inputStream);
    } catch (IOException ex) {
    }

    // The width and height of the card images.
    cardWidth = backOfCardImage.getWidth();
    cardHeight = backOfCardImage.getHeight();

    // Redraws Components when the frame size is changed
    addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent event) {
        removeAll();
        addPlayerOneCards();
        revalidate();
        repaint();
      }
    });

    // Used to nudge the position of painted cards.
    nudger = new Nudge();

  }

  /**
   * Builds a 2D array of card images.
   */
  public void build2DCardImageArray() {
    // The 2D array of images.
    cardImages = new BufferedImage[13][4];
    // A string used to reference the four card suits.
    String suits = "cdhs";
    // Adds the card images to the array at specified indexes.
    for (Suit suit : Suit.values()) {
      char c = suits.charAt(suit.ordinal());
      for (Rank rank : Rank.values()) {
        String s = String.format("%02d%c.gif", (rank.ordinal() + 1), c);
        inputStream = ClassLoader.getSystemResourceAsStream(s);
        try {
          BufferedImage bi = ImageIO.read(inputStream);
          cardImages[rank.ordinal()][suit.ordinal()] = bi;
        } catch (IOException ex) {
        }
      }
    }
  }

  /**
   * Builds a 2D array of mapped and click-able card image JLabels and adds a listener to each
   * label.
   */
  public void build2DClickableCardImageArray() {
    // The 2D array of labels.
    cardLabels = new JLabel[13][4];
    // The hash map of labels and their corresponding Card values.
    cardLabelMap = new HashMap<JLabel, Card>(52, 1f);
    String suits = "cdhs";
    for (Suit suit : Suit.values()) {
      char c = suits.charAt(suit.ordinal());
      for (Rank rank : Rank.values()) {
        String s = String.format("%02d%c.gif", (rank.ordinal() + 1), c);
        inputStream = ClassLoader.getSystemResourceAsStream(s);
        try {
          // Gets the Buffered Image.
          BufferedImage bi = ImageIO.read(inputStream);
          // Makes the Buffered Image an Image Icon.
          ImageIcon ii = new ImageIcon(bi);
          // Makes the Image Icon a JLabel.
          JLabel jl = new JLabel(ii);
          // Uses the current rank and suit to instantiate a reference card which is then stored in
          // the hash map along with the current label.
          Card hashCard = new Card(rank, suit);
          cardLabelMap.put(jl, hashCard);
          // Adds a mouse listener to each label component which attempts to play the associated
          // card when clicked on the GUI, and animates the label when the mouse is hovered over it
          // during the player's turn.
          jl.addMouseListener(new MouseAdapter() {

            @Override
            /**
             * Attempts to play the associated card when clicked on the GUI.
             */
            public void mouseClicked(MouseEvent event) {
              // Only executes two conditions: the game isn't over and it's player one's turn.
              if (!logic.isOver() && logic.isPlayerOnesTurn()) {
                // Retrieves the clicked label.
                JLabel clickedLabel = (JLabel) event.getSource();
                // Retrieves the clicked label's associated card.
                Card hashCard = cardLabelMap.get(clickedLabel);
                // Gets the rank of the associated card.
                Rank hashCardRank = hashCard.getRank();
                // Gets the suit of the associated card.
                Suit hashCardSuit = hashCard.getSuit();
                // Attempts to play the card with the corresponding rank and suit in the player's
                // hand.
                logic.takeTurn(hashCardRank, hashCardSuit);
                // Removes all of the current cards.
                removeAll();
                // Redraws the set of cards minus the selected card if it was play-able.
                addPlayerOneCards();
                revalidate();
                repaint();
                // If the game isn't over and the player one's card was successfully played, play
                // passes to the next player.
                if (!logic.isOver() && !logic.isPlayerOnesTurn()) {
                  logic.nextPlayer();
                  computerPlayerPlay();
                }
              }
            }

            @Override
            /**
             * Nudges the card label upwards when the cursor hovers over it and it's player one's
             * turn.
             */
            public void mouseEntered(MouseEvent event) {
              if (logic.isPlayerOnesTurn()) {

                JLabel card = (JLabel) event.getSource();
                // Gets the rectangular dimensions of the card label.
                Rectangle bounds = card.getBounds();
                int xpos = (int) bounds.getX();
                int ypos = (int) bounds.getY();
                // An incremental height adjustment factor.
                int yposADJ = -((int) bounds.getHeight() / 24);
                // A height adjustment limit.
                int yposMAX = (int) bounds.getY() - ((int) (5 * bounds.getHeight()) / 24);
                // Incrementally increases the height of the label.
                for (int i = ypos; i > yposMAX; i += yposADJ) {
                  card.setBounds(xpos, i, cardWidth, cardHeight);
                  // Paints while the event is being dispatched.
                  paintImmediately(0, 0, getWidth(), getHeight());
                  // Sets a small delay to make the animation noticeable.
                  try {
                    Thread.sleep(20);
                  } catch (Exception ex) {
                  }
                }
              }
            }

            @Override
            /**
             * Nudges the card label downwards when the cursor hovers over it and it's player one's
             * turn.
             */
            public void mouseExited(MouseEvent event) {
              // see mouseEntered method for explanatory notation.
              if (logic.isPlayerOnesTurn()) {

                JLabel card = (JLabel) event.getSource();
                Rectangle bounds = card.getBounds();
                int xpos = (int) bounds.getX();
                int ypos = (int) bounds.getY();
                int yposADJ = ((int) bounds.getHeight() / 24);
                int yposMAX = (int) bounds.getY() + ((int) (5 * bounds.getHeight()) / 24);
                if (ypos < cardYCoordinate) {
                  for (int i = ypos; i < yposMAX; i += yposADJ) {

                    card.setBounds(xpos, i, cardWidth, cardHeight);
                    paintImmediately(0, 0, getWidth(), getHeight());
                    try {
                      Thread.sleep(20);
                    } catch (Exception ex) {
                    }
                  }
                }
              }
            }
          });
          // adds the label to the 2D JLabel array.
          cardLabels[rank.ordinal()][suit.ordinal()] = jl;
        } catch (IOException ex) {
        }
      }
    }
  }

  /**
   * Adds player one's cards to the GUI.
   */
  public void addPlayerOneCards() {
    Player playerOne = playerList.get(0);
    // Gets the player's hand.
    CardCollection playersCards = playerOne.getHand();
    int amountOfPlayersCards = playersCards.size();
    // Iterates through the player one's hand painting each card.
    for (int i = 0; i < amountOfPlayersCards; i++) {
      Card cardToPaint = playersCards.getCard(i);
      Rank rank = cardToPaint.getRank();
      Suit suit = cardToPaint.getSuit();
      // Card coordinates relative to player one's displayed position, and other cards in the hand.
      int cardWidthCorrectionFactor = -cardWidth / 2;
      int cardAmountCorrectionFactor = -((amountOfPlayersCards - 1) * cardWidth) / 4;
      int cardIndexCorrectionFactor = (i * cardWidth) / 2;
      int cardXCoordinate = (getWidth() / 2) + cardWidthCorrectionFactor
          + cardAmountCorrectionFactor + cardIndexCorrectionFactor;
      int cardHeightCorrectionFactor = -cardHeight / 2;
      cardYCoordinate = ((3 * getHeight()) / 4) + cardHeightCorrectionFactor;
      // Gets the card label to be added.
      JLabel clickableCard = cardLabels[rank.ordinal()][suit.ordinal()];
      // Sets the card label location and adds the label.
      clickableCard.setBounds(cardXCoordinate, cardYCoordinate, cardWidth, cardHeight);
      add(clickableCard);
    }
  }

  /**
   * When player one has no playable cards, automatically deals cards to player one until a playable
   * card is dealt.
   */
  public void dealUntilMatchPlayerOne() {
    // Adds an action listener which updates player one's hand when no cards match.
    ActionListener dealToPlayerOne = new ActionListener() {

      @Override
      /**
       * Updates player one's hand when no cards match.
       */
      public void actionPerformed(ActionEvent event) {
        // Temporarily removes all of player one's cards.
        removeAll();
        // Draws a new card to player one's hand.
        logic.drawForMatchCurrentPlayer();
        // Adds all of player one's cards including the new card.
        addPlayerOneCards();
        revalidate();
        repaint();
        // Checks if player one's hand now has a match and deals again if not.
        if (!logic.hasAMatch()) {
          // Sets a timer delay between 500 - 700 ms.
          timerDelay = ((int) (Math.random() * 200) + 500);
          // Adds a timer which "waits" for the deal.
          waitForDealer.setDelay(timerDelay);
          waitForDealer.setRepeats(false);
          waitForDealer.start();
        }
      }
    };

    // Checks if player one's initial hand has a match and deals a new card if not.
    if (!logic.hasAMatch()) {
      // Sets a timer delay between 500 - 700 ms.
      timerDelay = ((int) (Math.random() * 200) + 500);
      // Adds a timer which "waits" for the deal.
      waitForDealer = new Timer(timerDelay, dealToPlayerOne);
      waitForDealer.setRepeats(false);
      waitForDealer.start();
    }
  }

  /**
   * Deals cards to a computer player until they have a matching card.
   */
  public void dealUntilMatchComputerPlayers() {
    // Adds an action listener which updates player's hands when no cards match.
    ActionListener dealToOtherPlayers = new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        logic.drawForMatchCurrentPlayer();
        revalidate();
        repaint();
        checkForMatch();
      }
    };
    // Sets a timer delay between 500 - 700 ms.
    timerDelay = ((int) (Math.random() * 200) + 500);
    // Adds a timer which waits for the deal.
    computerPlayerWaitsForDealer = new Timer(timerDelay, dealToOtherPlayers);
    computerPlayerWaitsForDealer.setRepeats(false);
    computerPlayerWaitsForDealer.start();
  }

  /**
   * Checks whether the computer player's initial hand has a match. If so, play continues.
   * Otherwise, another card is drawn.
   */
  public void checkForMatch() {
    if (!logic.hasAMatch()) {
      // Sets a timer delay between 500 - 700 ms.
      timerDelay = ((int) (Math.random() * 200) + 500);
      // Adds a timer which "waits: for the deal.
      computerPlayerWaitsForDealer.setDelay(timerDelay);
      computerPlayerWaitsForDealer.setRepeats(false);
      computerPlayerWaitsForDealer.start();
    } else {
      computerPlayerPlay();
    }
  }

  /**
   * Draws a specified card at the given coordinates. x and y are in units of card width/height.
   * 
   * @param g The Graphics argument from paintComponent.
   * @param rank Specified card rank.
   * @param suit Specified card suit.
   * @param x Specified x-coordinate.
   * @param y Specified y-coordinate.
   */
  public void drawCardImage(Graphics g, Rank rank, Suit suit, double x, double y) {
    BufferedImage image = cardImages[rank.ordinal()][suit.ordinal()];
    g.drawImage(image, (int) (x), (int) (y), null);
  }

  /**
   * Draws a 'back of card' image at the given coordinates. x and y are in units of card
   * width/height.
   * 
   * @param g The Graphics argument from paintComponent.
   * @param x Specified x-coordinate.
   * @param y Specified y-coordinate.
   */
  public void drawCardImage(Graphics g, double x, double y) {
    g.drawImage(backOfCardImage, (int) (x), (int) (y), null);
  }

  /**
   * Draws a rotated 'back of card' image at the given coordinates. x and y are in units of card
   * width/height.
   * 
   * @param g The Graphics argument from paintComponent.
   * @param rotation Specified rotation in radians.
   * @param x Specified x-coordinate.
   * @param y Specified y-coordinate.
   */
  public void drawCardImage(Graphics g, double rotation, double x, double y) {
    BufferedImage image = backOfCardImage;
    double locationX = cardWidth;
    double locationY = cardHeight;
    // Method used to rotate the card.
    AffineTransform tx = AffineTransform.getRotateInstance(rotation, locationX, locationY);
    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
    g.drawImage(op.filter(image, null), (int) (x), (int) (y), null);
  }

  /**
   * Draws the Player names by their respective hands at positions relative to the panel size.
   * 
   * @param g The Graphics argument from paintComponent.
   */
  public void drawNames(Graphics g) {
    // Calculates the width and height of the Strings.
    FontMetrics metrics = g.getFontMetrics();
    String playerOneName = playerList.get(0).getName();
    String playerTwoName = playerList.get(1).getName();
    int playerOneStringWidth = metrics.stringWidth(playerOneName);
    int playerTwoStringWidth = metrics.stringWidth(playerTwoName);
    int stringHeight = metrics.getHeight();
    // For 2-Player games.
    if (playerList.size() == 2) {
      int playerOneNameXCoordinate = (getWidth() / 2) - (playerOneStringWidth / 2);
      int playerOneNameYCoordinate = ((29 * getHeight()) / 32);
      int playerTwoNameXCoordinate = (getWidth() / 2) - (playerTwoStringWidth / 2);
      int playerTwoNameYCoordinate = 3 * getHeight() / 32 + (3 * stringHeight / 4);
      g.drawString(playerOneName, playerOneNameXCoordinate, playerOneNameYCoordinate);
      g.drawString(playerTwoName, playerTwoNameXCoordinate, playerTwoNameYCoordinate);
    }
    // For 3-Player games.
    if (playerList.size() == 3) {
      String playerThreeName = playerList.get(2).getName();
      int playerThreeStringWidth = metrics.stringWidth(playerThreeName);
      int playerOneNameXCoordinate = (getWidth() / 2) - (playerOneStringWidth / 2);
      int playerOneNameYCoordinate = ((29 * getHeight()) / 32);
      int playerTwoNameXCoordinate = (getWidth() / 8) - (3 * playerTwoStringWidth / 2);
      int playerTwoNameYCoordinate = (getHeight() / 2) + (stringHeight / 4);
      int playerThreeNameXCoordinate = (getWidth() / 2) - (playerThreeStringWidth / 2);
      int playerThreeNameYCoordinate = 3 * getHeight() / 32 + (3 * stringHeight / 4);
      g.drawString(playerOneName, playerOneNameXCoordinate, playerOneNameYCoordinate);
      g.drawString(playerTwoName, playerTwoNameXCoordinate, playerTwoNameYCoordinate);
      g.drawString(playerThreeName, playerThreeNameXCoordinate, playerThreeNameYCoordinate);
    }
    // For 4-Player games.
    if (playerList.size() == 4) {
      String playerThreeName = playerList.get(2).getName();
      int playerThreeStringWidth = metrics.stringWidth(playerThreeName);
      String playerFourName = playerList.get(3).getName();
      int playerFourStringWidth = metrics.stringWidth(playerFourName);
      int playerOneNameXCoordinate = (getWidth() / 2) - (playerOneStringWidth / 2);
      int playerOneNameYCoordinate = ((29 * getHeight()) / 32);
      int playerTwoNameXCoordinate = (getWidth() / 8) - (3 * playerTwoStringWidth / 2);
      int playerTwoNameYCoordinate = (getHeight() / 2) + (stringHeight / 4);
      int playerThreeNameXCoordinate = (getWidth() / 2) - (playerThreeStringWidth / 2);
      int playerThreeNameYCoordinate = 3 * getHeight() / 32 + (3 * stringHeight / 4);
      int playerFourNameXCoordinate = (7 * getWidth() / 8) + (playerFourStringWidth / 2);
      int playerFourNameYCoordinate = (getHeight() / 2) + (stringHeight / 4);
      g.drawString(playerOneName, playerOneNameXCoordinate, playerOneNameYCoordinate);
      g.drawString(playerTwoName, playerTwoNameXCoordinate, playerTwoNameYCoordinate);
      g.drawString(playerThreeName, playerThreeNameXCoordinate, playerThreeNameYCoordinate);
      g.drawString(playerFourName, playerFourNameXCoordinate, playerFourNameYCoordinate);
    }
  }

  /**
   * Draws and updates the GUI
   * 
   * @param g The Graphics argument from paintComponent.
   */
  public void paintComponent(Graphics g) {
    Graphics2D g2D = (Graphics2D) g;
    // Smoothes out fonts.
    g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    // Sets Font properties and draws Player names.
    int fontSize = 18;
    g.setFont(new Font("Arial", Font.PLAIN, fontSize));
    g.setColor(Color.white);
    // Paints when the game is over.
    if (logic.isOver()) {
      // Removes all components.
      removeAll();
      // Fills the screen with the background colour.
      g.setColor(new Color(0x088A4B));
      g.fillRect(0, 0, getWidth(), getHeight());
      // Font settings
      g.setColor(Color.white);
      fontSize = 60;
      g.setFont(new Font("Arial", Font.PLAIN, fontSize));
      FontMetrics metrics = g.getFontMetrics();
      // Game over message.
      String gameOver = new String("Game Over");
      // Provides coordinates relative to the panel size.
      int stringWidth = (int) metrics.stringWidth(gameOver);
      int gameOverXCoordinate = (getWidth() / 2) - (stringWidth / 2);
      int gameOverYCoordinate = ((getHeight()) / 2);
      // Writes game over message to UI.
      g.drawString(gameOver, gameOverXCoordinate, gameOverYCoordinate);
      // New font settings.
      fontSize = 42;
      g.setFont(new Font("Arial", Font.BOLD, fontSize));
      metrics = g.getFontMetrics();
      // Winning player message.
      String winningPlayer = (logic.getCurrentPlayer().getName() + " wins!");
      // Provides coordinates relative to the panel size.
      stringWidth = (int) metrics.stringWidth(logic.getCurrentPlayer().getName())
          + metrics.stringWidth(" wins!");
      int winnerXCoordinate = (getWidth() / 2) - (stringWidth / 2);
      int winnerYCoordinate = (3 * getHeight() / 4);
      // Writes the winning player message to UI.
      g.drawString(winningPlayer, winnerXCoordinate, winnerYCoordinate);
      // Paints when the game isn't over.
    } else {
      // Draws the player's names.
      drawNames(g);
      // Draws the top of the discard pile in the centre of the table.
      discardPile = logic.getDiscardPile();
      if (discardPile.size() > 0) {
        int topDiscardIndex = discardPile.size() - 1;
        Card cardToPaint = discardPile.getCard(topDiscardIndex);
        Rank rank = cardToPaint.getRank();
        Suit suit = cardToPaint.getSuit();
        double x = (getWidth() / 2) - (cardWidth / 2);
        double y = (getHeight() / 2) - (cardHeight / 2);
        drawCardImage(g, rank, suit, x, y);
      }
      // For loop draws the hand of each and every computer player in the game.
      for (Player player : playerList) {
        // Gets the player's hand.
        CardCollection playersCards = player.getHand();
        int amountOfPlayersCards = playersCards.size();
        // Iterates through the player's hand.
        for (int i = 0; i < amountOfPlayersCards; i++) {
          // Card coordinates for the computer player to be displayed North.
          double cardsNorthXCoordinate = getWidth() / 2 - cardWidth / 2
              - (amountOfPlayersCards - 1) * cardWidth / 4 + i * cardWidth / 2;
          double cardsNorthYCoordinate = getHeight() / 4 - cardHeight / 2;
          // Card coordinates for the computer player to be displayed West.
          double cardsWestXCoordinate = getWidth() / 4 - cardWidth - cardHeight / 2;
          double cardsWestYCoordinate =
              getHeight() / 2 - cardHeight / 2 - (cardHeight / 2 - cardWidth / 2)
                  - (amountOfPlayersCards - 1) * cardWidth / 4 + i * cardWidth / 2;
          // Card coordinates for the computer player to be displayed East.
          double cardsEastXCoordinate = 3 * getWidth() / 4 - cardWidth - cardHeight / 2;
          double cardsEastYCoordinate =
              getHeight() / 2 - cardHeight / 2 - (cardHeight / 2 - cardWidth / 2)
                  - (amountOfPlayersCards - 1) * cardWidth / 4 + i * cardWidth / 2;
          // Draws the computer player's card in a 2-player game.
          if (playerList.size() == 2) {
            if (logic.getPlayerIndex(player) == 1) {
              // Draws the card and enables card animation sequence.
              nudge = nudger.nudgeCard(i, 1, cardPosition);
              double x = cardsNorthXCoordinate;
              double y = cardsNorthYCoordinate + nudge;
              drawCardImage(g, x, y);
            }
          }
          // Draws each computer player's card in a 3-player game.
          if (playerList.size() == 3) {
            if (logic.getPlayerIndex(player) == 1) {
              // Draws the card and enables card animation sequence.
              nudge = nudger.nudgeCard(i, 1, cardPosition);
              double x = cardsWestXCoordinate + nudge;
              double y = cardsWestYCoordinate;
              double rotation = Math.toRadians(90);
              drawCardImage(g, rotation, x, y);
            }
            if (logic.getPlayerIndex(player) == 2) {
              // Draws the card and enables card animation sequence.
              nudge = nudger.nudgeCard(i, 2, cardPosition);
              double x = cardsNorthXCoordinate;
              double y = cardsNorthYCoordinate + nudge;
              drawCardImage(g, x, y);
            }
          }
          // draws each computer player's card in a 4-player game.
          if (playerList.size() == 4) {
            if (logic.getPlayerIndex(player) == 1) {
              // Draws the card and enables card animation sequence.
              nudge = nudger.nudgeCard(i, 1, cardPosition);
              double x = cardsWestXCoordinate + nudge;
              double y = cardsWestYCoordinate;
              double rotation = Math.toRadians(90);
              drawCardImage(g, rotation, x, y);
            }
            if (logic.getPlayerIndex(player) == 2) {
              // Draws the card and enables card animation sequence.
              nudge = nudger.nudgeCard(i, 2, cardPosition);
              double x = cardsNorthXCoordinate;
              double y = cardsNorthYCoordinate + nudge;
              drawCardImage(g, x, y);
            }
            if (logic.getPlayerIndex(player) == 3) {
              // Draws the card and enables card animation sequence.
              nudge = nudger.nudgeCard(i, 3, cardPosition);
              double x = cardsEastXCoordinate - nudge;
              double y = cardsEastYCoordinate;
              double rotation = Math.toRadians(90);
              drawCardImage(g, rotation, x, y);
            }
          }
        }
      }
    }
  }

  /**
   * Triggers the animation and underlying logic for a computer player's turn.
   */
  public void computerPlayerTakeTurn() {
    // Sequentially animates the computer player's cards during their turn.
    // A coin toss is used to determine whether the last card in sequence should be played or
    // whether a reverse sequence should begin.
    ActionListener animateComputerPlayerListener = new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        // Gets players hand size.
        int handSize = logic.getCurrentPlayer().getHand().size();
        int indexOfLastCard = handSize - 1;
        int indexOfSecondToLastCard = handSize - 2;
        // Returns either 0 or 1. 0 for heads, 1 for tails.
        int coinToss = (int) (Math.random() * 2);
        // Sequence Animation
        // If the players hand size is less than 7 cards, the limit of cards animated "in sequence"
        // is set to a random number between zero and the index of the last card in sequence.
        if (handSize < 7) {
          sequenceLimit = (int) (Math.random() * (indexOfLastCard + 1));
        }
        // If the players hand size is more than or equal to 7 cards, the upper limit of cards
        // animated is set to a random integer between zero and 6.
        if (handSize >= 7) {
          sequenceLimit = (int) ((Math.random() * 7));
        }
        // For each card within the sequence limit.
        for (int i = 0; i <= sequenceLimit; i++) {
          // Sets the corresponding paint card index and player index to the current card index /
          // player index.
          nudger.setCardIndex(i);
          nudger.setCurrentPlayerIndex(logic.getPlayerIndex(logic.getCurrentPlayer()));
          // Animates positive card nudge.
          for (int n = 0; n < 5; n++) {
            // Used to position the card nudge call in paintComponent().
            cardPosition = n;
            revalidate();
            paintImmediately(0, 0, getWidth(), getHeight());
            // Leaves at least 13ms between each animation 'frame'.
            try {
              Thread.sleep(13);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
          // Animates negative card nudge if any of the following are true:
          // The current card index is below the sequence limit.
          // Or the index of the last card equals the sequence limit and "coin toss" is heads.
          if ((i < sequenceLimit) || (indexOfLastCard == sequenceLimit) && (coinToss == 0)) {
            for (int n = 4; n >= 0; n--) {
              // Used to position the card nudge call in paintComponent().
              cardPosition = n;
              revalidate();
              paintImmediately(0, 0, getWidth(), getHeight());
              // Leaves at least 13ms between each animation 'frame'.
              try {
                Thread.sleep(13);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            }
          }
          // Waits a random amount of time between 50 and 400ms for next card to be animated.
          try {
            int sleepTime = (int) ((Math.random() * 350) + 50);
            Thread.sleep(sleepTime);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        // Reverse Sequence Animation
        // If the players hand size is less than 7 cards and more than 1 and the sequence limit
        // equals the index of the last card and the coin toss lands heads, a limit of cards
        // animated "in reverse sequence" is set to a random number between half the hand size and
        // the index of the second to last card in the sequence (as the last card doesn't need
        // re-animating).
        if (((handSize < 7) && (handSize > 1)) && (indexOfLastCard == sequenceLimit)
            && (coinToss == 0)) {
          reverseSequenceLimit = (int) ((Math.random() * indexOfSecondToLastCard) + (handSize / 2));
          // Sets the card index to the previous card in the sequence and animates "backwards" to
          // the lower limit.
          for (int i = indexOfSecondToLastCard; i >= reverseSequenceLimit; i--) {
            nudger.setCardIndex(i);
            nudger.setCurrentPlayerIndex(logic.getPlayerIndex(logic.getCurrentPlayer()));
            // Animates positive card nudge.
            for (int n = 0; n < 5; n++) {
              // Used to position the card nudge call in paintComponent().
              cardPosition = n;
              revalidate();
              paintImmediately(0, 0, getWidth(), getHeight());
              try {
                // Leaves at least 13ms between each animation 'frame'.
                Thread.sleep(13);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            }
            // Animates negative card nudge with the exception of the last card in sequence.
            if (i > (reverseSequenceLimit)) {
              for (int n = 4; n >= 0; n--) {
                // Used to position the card nudge call in paintComponent().
                cardPosition = n;
                revalidate();
                paintImmediately(0, 0, getWidth(), getHeight());
                try {
                  // Leaves at least 13ms between each animation 'frame'.
                  Thread.sleep(13);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
              }
            }
            // Waits a random amount of time between 50 and 400ms for next card to be animated.
            try {
              int sleepTime = (int) ((Math.random() * 350) + 50);
              Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        }
        // Waits a random amount of time between 100 and 150ms for card to be played.
        try {
          int sleepTime = (int) ((Math.random() * 50) + 100);
          Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        // Resets the "nudger" to default values.
        nudger.reset();
        // Updates the logic.
        logic.takeTurn();
        if (!logic.isOver()) {
          logic.nextPlayer();
        } else {
          // Draws final screen if logic is over.
          revalidate();
          repaint();
        }
        // Transitions from computer player's turn to player's turn if the current computer player
        // is the last in sequence.
        if (logic.isPlayerOnesTurn()) {
          revalidate();
          repaint();
          primePlayerOne();
        } else {
          // Otherwise transitions to the next computer player's turn.
          revalidate();
          repaint();
          computerPlayerPlay();
        }
      }
    };
    // Initialises and starts the animation timer.
    timerDelay = (int) ((Math.random() * 200) + 500);
    animateComputerPlayer = new Timer(timerDelay, animateComputerPlayerListener);
    animateComputerPlayer.setRepeats(false);
    animateComputerPlayer.start();
  }

  /**
   * Updates player one's hand at the start of their turn.
   */
  public void primePlayerOne() {
    if (!logic.isOver()) {
      logic.searchForMatchCurrentPlayer();
      dealUntilMatchPlayerOne();
    }
  }

  /**
   * The computer's hand is updated and a card played.
   */
  public void computerPlayerPlay() {
    // Each computer player takes their turn.
    if (!logic.isOver()) {
      logic.searchForMatchCurrentPlayer();
      if (!logic.hasAMatch()) {
        dealUntilMatchComputerPlayers();
      } else {
        computerPlayerTakeTurn();
      }
    }
  }
}
