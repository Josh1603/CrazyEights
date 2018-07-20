package jjj.cardgames.crazyeights;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.Group;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * This panel allows the user to select either a 2, 3, or 4 player game of Crazy Eights. The user
 * can select the amount of players, name each player and initiate a game using this panel. The
 * panel contains three sub-panels a panel to select the amount of players, a panel to enter the
 * player details and a panel to confirm the player details and start the game. The main panel also
 * includes the game rules and instructions on how to play the game.
 * 
 * @author Joshua Hardman
 *
 */
public class PlayerSelectionPanel extends JPanel {

  private static final long serialVersionUID = 6538616502964858850L;

  private JPanel playerDetailsPanel;
  private JPanel confirmationPanel;
  private int amountOfPlayers = -1;
  private int amountOfPlayersConfirmed = 0;
  private boolean playerOneIsConfirmed;
  private boolean playerTwoIsConfirmed;
  private boolean playerThreeIsConfirmed;
  private boolean playerFourIsConfirmed;
  private boolean allPlayersConfirmed;
  private GroupLayout groupLayout;
  private ActionListener twoPlayerButtonListener;
  private ActionListener threePlayerButtonListener;
  private ActionListener fourPlayerButtonListener;
  private ActionListener playerOneConfirmationListener;
  private ActionListener playerTwoConfirmationListener;
  private ActionListener playerThreeConfirmationListener;
  private ActionListener playerFourConfirmationListener;
  private EnterAction enterAction;
  private JLabel playerOneNameLabel;
  private JLabel playerTwoNameLabel;
  private JLabel playerThreeNameLabel;
  private JLabel playerFourNameLabel;
  private JTextField playerOneName;
  private JTextField playerTwoName;
  private JTextField playerThreeName;
  private JTextField playerFourName;
  private JButton addPlayerOneButton;
  private JButton addPlayerTwoButton;
  private JButton addPlayerThreeButton;
  private JButton addPlayerFourButton;
  private JLabel confirmedPlayerOneName;
  private JLabel confirmedPlayerTwoName;
  private JLabel confirmedPlayerThreeName;
  private JLabel confirmedPlayerFourName;
  private JButton startGame = new JButton("Start Game");
  private ArrayList<Player> playerList;
  private String updatedPlayerName;


  public PlayerSelectionPanel() {

    // Changes the layout to BorderLayout.
    setLayout(new BorderLayout());

    // Initializes pane with the background colour.
    setBackground(new Color(0x088A4B));

    // Sets up the action listeners for the 'X-Player' buttons.
    setUpAmountOfPlayersActionListeners();

    // Enables any mapped input to be recognised as a mouse click. Used to "Enter-enable" JButtons.
    setUpEnterAction();

    // Sets up the 'amount of players' panel.
    setUpAmountOfPlayersPanel();

    // Sets up the action listeners for the player details confirmation buttons and text fields.
    setUpPlayerDetailsActionListeners();

    // Initializes the player details panel.
    initializePlayerDetailsPanel();

    // Initializes the confirmation panel.
    initializeConfirmationPanel();

  }

  /**
   * Returns the 'Start Game' button.
   */
  public JButton getStartGameButton() {
    return startGame;
  }

  /**
   * Returns an arraylist of confirmed players.
   */
  public ArrayList<Player> getPlayerList() {
    return playerList;
  }

  /**
   * Returns the amount of players selected (2, 3 or 4).
   */
  public int getAmountOfPlayers() {
    return amountOfPlayers;
  }

  /**
   * Returns the current Player 1 text field as a String.
   */
  public String getPlayerOneName() {
    return playerOneName.getText();
  }

  /**
   * Returns the current Player 2 text field as a String.
   */
  public String getPlayerTwoName() {
    return playerTwoName.getText();
  }

  /**
   * Returns the current Player 3 text field as a String.
   */
  public String getPlayerThreeName() {
    return playerThreeName.getText();
  }

  /**
   * Returns the current Player 4 text field as a String.
   */
  public String getPlayerFourName() {
    return playerFourName.getText();
  }

  /**
   * Sets up the 'amount of players' panel.
   */
  public void setUpAmountOfPlayersPanel() {
    // Instantiates a new panel for the player amount buttons.
    JPanel amountOfPlayersPanel = new JPanel();
    // Instantiates the player amount buttons.
    JButton twoPlayerButton = new JButton("2-Player");
    JButton threePlayerButton = new JButton("3-Player");
    JButton fourPlayerButton = new JButton("4-Player");
    // Enables a press of the Enter key to activate each button when focused.
    enableEnter(twoPlayerButton);
    enableEnter(threePlayerButton);
    enableEnter(fourPlayerButton);
    // Adds an action listener to each button.
    twoPlayerButton.addActionListener(twoPlayerButtonListener);
    threePlayerButton.addActionListener(threePlayerButtonListener);
    fourPlayerButton.addActionListener(fourPlayerButtonListener);
    // Adds each button to the panel.
    amountOfPlayersPanel.add(twoPlayerButton);
    amountOfPlayersPanel.add(threePlayerButton);
    amountOfPlayersPanel.add(fourPlayerButton);
    // Adds the panel to the South of the main panel.
    add(amountOfPlayersPanel, BorderLayout.SOUTH);
  }

  /**
   * Initializes the player details panel.
   */
  public void initializePlayerDetailsPanel() {
    // instantiates a new panel.
    playerDetailsPanel = new JPanel();
    // initializes the layout settings for the panel.
    groupLayout = new GroupLayout(playerDetailsPanel);
    playerDetailsPanel.setLayout(groupLayout);
    groupLayout.setAutoCreateGaps(true);
    groupLayout.setAutoCreateContainerGaps(true);
    setNameLabels();
    setTextFields();
    setNameConfirmationButtons();
  }

  /**
   * Initializes the confirmation panel.
   */
  public void initializeConfirmationPanel() {
    confirmationPanel = new JPanel();
    confirmationPanel.setLayout(new BoxLayout(confirmationPanel, BoxLayout.Y_AXIS));
  }

  /**
   * Instantiates the name labels for the player details panel.
   */
  public void setNameLabels() {
    playerOneNameLabel = new JLabel("Player-1 Name");
    playerTwoNameLabel = new JLabel("Computer-1 Name");
    playerThreeNameLabel = new JLabel("Computer-2 Name");
    playerFourNameLabel = new JLabel("Computer-3 Name");
  }

  /**
   * Instantiates the player details panel text fields and adds listeners which add confirmation
   * labels to the confirmation Panel.
   */
  public void setTextFields() {
    playerOneName = new JTextField();
    playerTwoName = new JTextField();
    playerThreeName = new JTextField();
    playerFourName = new JTextField();
    playerOneName.addActionListener(playerOneConfirmationListener);
    playerTwoName.addActionListener(playerTwoConfirmationListener);
    playerThreeName.addActionListener(playerThreeConfirmationListener);
    playerFourName.addActionListener(playerFourConfirmationListener);
  }

  /**
   * Instantiates, enter-enables, and adds action listeners to the player details panel name
   * confirmation buttons.
   */
  public void setNameConfirmationButtons() {
    addPlayerOneButton = new JButton("Add Player");
    addPlayerTwoButton = new JButton("Add Player");
    addPlayerThreeButton = new JButton("Add Player");
    addPlayerFourButton = new JButton("Add Player");
    enableEnter(addPlayerOneButton);
    enableEnter(addPlayerTwoButton);
    enableEnter(addPlayerThreeButton);
    enableEnter(addPlayerFourButton);
    addPlayerOneButton.addActionListener(playerOneConfirmationListener);
    addPlayerTwoButton.addActionListener(playerTwoConfirmationListener);
    addPlayerThreeButton.addActionListener(playerThreeConfirmationListener);
    addPlayerFourButton.addActionListener(playerFourConfirmationListener);
  }

  /**
   * Enables any mapped input to be recognised as a mouse click.
   */
  public void setUpEnterAction() {
    enterAction = new EnterAction();
  }

  /**
   * Enables an Enter key press to mimic a mouse click on a given JButton component.
   * 
   * @param button The JButton to be Enter-enabled.
   */
  public void enableEnter(JButton button) {
    button.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "acceptEnter");
    button.getActionMap().put("acceptEnter", enterAction);
  }

  /**
   * Sets up the action listeners for the 'X-Player' buttons which update the player details panel
   * based on the amount of players.
   */
  public void setUpAmountOfPlayersActionListeners() {
    twoPlayerButtonListener = new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        setPlayerAmount(2);
        // Resets the entire panel, or sets the panel if it hasn't yet been set.
        panelReset();
        // Requests the focus on Player one's text field to streamline user input.
        playerOneName.requestFocus();
      }
    };
    threePlayerButtonListener = new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        setPlayerAmount(3);
        panelReset();
        playerOneName.requestFocus();
      }
    };
    fourPlayerButtonListener = new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        setPlayerAmount(4);
        panelReset();
        playerOneName.requestFocus();
      }
    };
  }

  /**
   * These listeners add confirmed player details in the confirmation panel and when all the player
   * details have been confirmed, a button to start the game. Confirmed player details can be
   * updated.
   */
  public void setUpPlayerDetailsActionListeners() {
    playerOneConfirmationListener = new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        // If player 1's details haven't been confirmed yet
        if (!playerOneIsConfirmed) {
          // get the text from the text field.
          String playerName = playerOneName.getText();
          // On the condition that some text has been entered
          if (playerName.length() > 0) {
            // instantiate a new JLabel with the player's name.
            confirmedPlayerOneName = new JLabel("Player 1: " + playerName);
            formatNameLabel(confirmedPlayerOneName);
            makeLabelViewable(confirmedPlayerOneName);
            // update the confirmation types.
            playerOneIsConfirmed = true;
            amountOfPlayersConfirmed++;
            playerTwoName.requestFocus();
          }
        }
        if (playerOneIsConfirmed) {
          // get the text from the text field.
          String newPlayerName = playerOneName.getText();
          // On the condition that some text has been entered
          if (newPlayerName.length() > 0) {
            // instantiate a new String with the player's updated name.
            updatedPlayerName = new String("Player 1: " + newPlayerName);
            formatNameLabel(confirmedPlayerOneName);
            updatePlayerLabel(confirmedPlayerOneName);
          }
        }
        // tests the current confirmation to decide whether the game can be started.
        testSelection();
      }
    };
    playerTwoConfirmationListener = new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        // If player 2's details haven't been confirmed yet
        if (!playerTwoIsConfirmed) {
          // get the text from the text field.
          String playerName = playerTwoName.getText();
          // On the condition that some text has been entered
          if (playerName.length() > 0) {
            // instantiate a new JLabel with the player's name.
            confirmedPlayerTwoName = new JLabel("Computer 1: " + playerName);
            formatNameLabel(confirmedPlayerTwoName);
            makeLabelViewable(confirmedPlayerTwoName);
            // update the confirmation types.
            playerTwoIsConfirmed = true;
            amountOfPlayersConfirmed++;
            if (amountOfPlayers > 2) {
              playerThreeName.requestFocus();
            }
          }
        }
        if (playerTwoIsConfirmed) {
          // get the text from the text field.
          String newPlayerName = playerTwoName.getText();
          // On the condition that some text has been entered
          if (newPlayerName.length() > 0) {
            // instantiate a new String with the player's updated name.
            updatedPlayerName = new String("Computer 1: " + newPlayerName);
            formatNameLabel(confirmedPlayerTwoName);
            updatePlayerLabel(confirmedPlayerTwoName);
          }
        }
        // tests the current confirmation to decide whether the game can be started.
        testSelection();
      }
    };
    playerThreeConfirmationListener = new ActionListener() {
      public void actionPerformed(ActionEvent event) {

        // If player 3's details haven't been confirmed yet
        if (!playerThreeIsConfirmed) {
          // get the text from the text field.
          String playerName = playerThreeName.getText();
          // On the condition that some text has been entered
          if (playerName.length() > 0) {
            // instantiate a new JLabel with the player's name.
            confirmedPlayerThreeName = new JLabel("Computer 2: " + playerName);
            formatNameLabel(confirmedPlayerThreeName);
            makeLabelViewable(confirmedPlayerThreeName);
            // update the confirmation types.
            playerThreeIsConfirmed = true;
            amountOfPlayersConfirmed++;
            if (amountOfPlayers > 3) {
              playerFourName.requestFocus();
            }
          }
        }
        if (playerThreeIsConfirmed) {
          // get the text from the text field.
          String newPlayerName = playerThreeName.getText();
          // On the condition that some text has been entered
          if (newPlayerName.length() > 0) {
            // instantiate a new String with the player's updated name.
            updatedPlayerName = new String("Computer 2: " + newPlayerName);
            formatNameLabel(confirmedPlayerThreeName);
            updatePlayerLabel(confirmedPlayerThreeName);
          }
        }
        // tests the current confirmation to decide whether the game can be started.
        testSelection();
      }
    };
    playerFourConfirmationListener = new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        // If player 4's details haven't been confirmed yet
        if (!playerFourIsConfirmed) {
          // get the text from the text field.
          String playerName = playerFourName.getText();
          // On the condition that some text has been entered
          if (playerName.length() > 0) {
            // instantiate a new JLabel with the player's name.
            confirmedPlayerFourName = new JLabel("Computer 3: " + playerName);
            formatNameLabel(confirmedPlayerFourName);
            makeLabelViewable(confirmedPlayerFourName);
            // update the confirmation types.
            playerFourIsConfirmed = true;
            amountOfPlayersConfirmed++;
          }
        }
        if (playerFourIsConfirmed) {
          // get the text from the text field.
          String newPlayerName = playerFourName.getText();
          // On the condition that some text has been entered
          if (newPlayerName.length() > 0) {
            // instantiate a new String with the player's updated name.
            updatedPlayerName = new String("Computer 3: " + newPlayerName);
            formatNameLabel(confirmedPlayerFourName);
            updatePlayerLabel(confirmedPlayerFourName);
          }
        }
        // tests the current confirmations to decide whether the game can be started.
        testSelection();
      }
    };
  }

  /**
   * Formats the player name confirmation label.
   */
  public void formatNameLabel(JLabel confirmedPlayerName) {
    // Centrally aligns the label.
    confirmedPlayerName.setAlignmentX(Component.CENTER_ALIGNMENT);
    // Sets the font parameters.
    int fontSize = 18;
    confirmedPlayerName.setFont(new Font("Arial", Font.PLAIN, fontSize));
    confirmedPlayerName.setForeground(Color.white);
  }

  /**
   * Updates the given player's details in the confirmation panel
   * 
   * @param confirmedPlayerName The given player's confirmed label.
   */
  public void updatePlayerLabel(JLabel confirmedPlayerName) {
    // Updates the JLabel text in the confirmation panel.
    confirmedPlayerName.setText(updatedPlayerName);
    revalidate();
    repaint();
  }

  /**
   * Makes the confirmation label appear on the GUI in the confirmation panel.
   */
  public void makeLabelViewable(JLabel confirmedPlayerName) {
    // Adds the JLabel to the confirmation panel.
    confirmationPanel.add(confirmedPlayerName);
    // Sets panel background as transparent (so you can still see game rules on the main panel).
    confirmationPanel.setOpaque(false);
    // Adds the confirmation panel to the main panel.
    add(confirmationPanel, BorderLayout.CENTER);
    revalidate();
    repaint();
  }

  /**
   * Tests the current confirmations to decide whether the game can be started.
   */
  public void testSelection() {
    // On the condition that the amount of players required is the same as the amount confirmed.
    // And the condition that all players haven't already been confirmed (to prevent duplicates).
    if ((amountOfPlayers == amountOfPlayersConfirmed) && !allPlayersConfirmed) {
      // Instantiates a centrally aligned, enter-enabled, JButton.
      enableEnter(startGame);
      startGame.setAlignmentX(Component.CENTER_ALIGNMENT);
      // Adds the button to the confirmation panel.
      confirmationPanel.add(startGame);
      revalidate();
      repaint();
      // Requests focus on the "Start Game" button to streamline user input.
      startGame.requestFocus();
      // Sets the all players confirmed boolean to true to prevent duplicate buttons.
      allPlayersConfirmed = true;
    }
  }

  /**
   * Sets the amount of players.
   * 
   * @param amount The amount of players selected.
   */
  public void setPlayerAmount(int amount) {
    amountOfPlayers = amount;
  }

  /**
   * Resets the amount of confirmed players to zero;
   */
  public void resetAmountOfPlayersConfirmed() {
    amountOfPlayersConfirmed = 0;
  }

  /**
   * Resets all the booleans used to check player confirmation status.
   */
  public void resetAllConfirmationBooleans() {
    playerOneIsConfirmed = false;
    playerTwoIsConfirmed = false;
    playerThreeIsConfirmed = false;
    playerFourIsConfirmed = false;
    allPlayersConfirmed = false;
  }

  /**
   * Sets / resets the player details panel and the confirmation panel according to the current
   * player amount.
   */
  public void panelReset() {
    resetAmountOfPlayersConfirmed();
    resetAllConfirmationBooleans();
    playerDetailsPanel.removeAll();
    confirmationPanel.removeAll();
    setUpPlayerDetailsPanel();
    add(playerDetailsPanel, BorderLayout.NORTH);
    revalidate();
    repaint();
  }

  /**
   * Sets up the player details panel according to the current player amount. As group layout is
   * used each component is added to both a horizontal and vertical group.
   */
  public void setUpPlayerDetailsPanel() {
    playerDetailsPanelHorizontalGroupLayout();
    playerDetailsPanelVerticalGroupLayout();
  }

  /**
   * Provides the horizontal layout parameters for the player details panel.
   */
  public void playerDetailsPanelHorizontalGroupLayout() {
    // Creates a layout group for a sequence of components.
    GroupLayout.SequentialGroup hGroup = groupLayout.createSequentialGroup();
    // Creates groups parallel to the layout group.
    ParallelGroup parallelGroupLayoutLabel =
        groupLayout.createParallelGroup(GroupLayout.Alignment.TRAILING);
    ParallelGroup parallelGroupLayoutTextArea = groupLayout.createParallelGroup();
    ParallelGroup parallelGroupLayoutButton = groupLayout.createParallelGroup();
    // Each group will store the components for every associated parallel group.
    Group groupLabel = null;
    Group groupTextArea = null;
    Group groupButton = null;
    // Adds components to the Parallel groups according to the amount of players.
    if (amountOfPlayers >= 2) {
      groupLabel = parallelGroupLayoutLabel.addComponent(playerOneNameLabel)
          .addComponent(playerTwoNameLabel);
      groupTextArea =
          parallelGroupLayoutTextArea.addComponent(playerOneName).addComponent(playerTwoName);
      groupButton = parallelGroupLayoutButton.addComponent(addPlayerOneButton)
          .addComponent(addPlayerTwoButton);
    }
    if (amountOfPlayers >= 3) {
      groupLabel = parallelGroupLayoutLabel.addComponent(playerThreeNameLabel);
      groupTextArea = parallelGroupLayoutTextArea.addComponent(playerThreeName);
      groupButton = parallelGroupLayoutButton.addComponent(addPlayerThreeButton);
    }
    if (amountOfPlayers >= 4) {
      groupLabel = parallelGroupLayoutLabel.addComponent(playerFourNameLabel);
      groupTextArea = parallelGroupLayoutTextArea.addComponent(playerFourName);
      groupButton = parallelGroupLayoutButton.addComponent(addPlayerFourButton);
    }
    // Adds each parallel group to the sequential layout group.
    hGroup.addGroup(groupLabel);
    hGroup.addGroup(groupTextArea);
    hGroup.addGroup(groupButton);
    // Sets the positions and sizes of components along the horizontal axis.
    groupLayout.setHorizontalGroup(hGroup);
  }

  /**
   * Provides the vertical layout parameters for the player details panel.
   */
  public void playerDetailsPanelVerticalGroupLayout() {
    // Creates a layout group for a sequence of components.
    GroupLayout.SequentialGroup vGroup = groupLayout.createSequentialGroup();
    // Creates groups parallel to the layout group which aligned with the component baselines.
    ParallelGroup parallelGroupLayoutLine1 = groupLayout.createParallelGroup(Alignment.BASELINE);
    ParallelGroup parallelGroupLayoutLine2 = groupLayout.createParallelGroup(Alignment.BASELINE);
    ParallelGroup parallelGroupLayoutLine3 = groupLayout.createParallelGroup(Alignment.BASELINE);
    ParallelGroup parallelGroupLayoutLine4 = groupLayout.createParallelGroup(Alignment.BASELINE);
    // Each group will store the components for every associated parallel group.
    Group groupLine1 = null;
    Group groupLine2 = null;
    Group groupLine3 = null;
    Group groupLine4 = null;
    // Adds components to the Parallel groups according to the amount of players.
    // Also adds each parallel group to the sequential layout group.
    if (amountOfPlayers >= 2) {
      groupLine1 = parallelGroupLayoutLine1.addComponent(playerOneNameLabel)
          .addComponent(playerOneName).addComponent(addPlayerOneButton);
      groupLine2 = parallelGroupLayoutLine2.addComponent(playerTwoNameLabel)
          .addComponent(playerTwoName).addComponent(addPlayerTwoButton);
      vGroup.addGroup(groupLine1);
      vGroup.addGroup(groupLine2);
    }
    if (amountOfPlayers >= 3) {
      groupLine3 = parallelGroupLayoutLine3.addComponent(playerThreeNameLabel)
          .addComponent(playerThreeName).addComponent(addPlayerThreeButton);
      vGroup.addGroup(groupLine3);
    }
    if (amountOfPlayers >= 4) {
      groupLine4 = parallelGroupLayoutLine4.addComponent(playerFourNameLabel)
          .addComponent(playerFourName).addComponent(addPlayerFourButton);
      vGroup.addGroup(groupLine4);
    }
    // Sets the positions and sizes of components along the vertical axis.
    groupLayout.setVerticalGroup(vGroup);
  }

  /**
   * Used to draw the game rules and instructions on how to play, on the main panel.
   */
  public void paintComponent(Graphics g) {
    Graphics2D g2D = (Graphics2D) g;
    // Smoothes out fonts.
    g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    // Sets Font properties.
    int fontSize = 18;
    g.setFont(new Font("Arial", Font.PLAIN, fontSize));
    g.setColor(Color.white);
    // Draws the game rules on the main panel.
    drawRules(g);
    // Draws game instructions on the main panel.
    drawHowToPlay(g);
  }

  /**
   * Draws the game rules on the main panel.
   * @param g The Graphics argument from paintComponent.
   */
  public void drawRules(Graphics g) {
    FontMetrics metrics = g.getFontMetrics();
    // Gets the pixel height of the text.
    int stringHeight = metrics.getHeight();
    // Build an array of the strings to be printed.
    String[] lines = new String[10];
    String line1 = new String("");
    lines[0] = line1;
    String line2 = new String("");
    lines[1] = line2;
    String line3 = new String("1. Be the first to play all of your cards to win.");
    lines[2] = line3;
    String line4 = new String("2. You must play one and only one card per turn.");
    lines[3] = line4;
    String line5 = new String("3. To be played, your card must either:");
    lines[4] = line5;
    String line6 = new String("    - match the suit of the central card.");
    lines[5] = line6;
    String line7 = new String("    - match the rank of the central card.");
    lines[6] = line7;
    String line8 = new String("    - be an eight.");
    lines[7] = line8;
    String line9 = new String("4. If none of the cards in your hand match,");
    lines[8] = line9;
    String line10 = new String("    you must draw until you draw a matching card.");
    lines[9] = line10;
    // Initializes the string width variable.
    int stringWidth = 0;
    int fontSize = 18;
    g.setFont(new Font("Arial", Font.PLAIN, fontSize));
    metrics = g.getFontMetrics();
    // Finds the longest string in the array and stores its width.
    for (String line : lines) {
      if (metrics.stringWidth(line) > stringWidth) {
        stringWidth = metrics.stringWidth(line);
      }
    }
    // Sets the initial coordinates for the first string relative to the panel dimensions.
    int lineXCoordinate = ((getWidth() / 4) - (stringWidth / 2));
    int lineYCoordinate = (7 * getHeight() / 16);
    // Gives font settings for the title string.
    fontSize = 24;
    g.setFont(new Font("Arial", Font.BOLD, fontSize));
    // The title string.
    String title = new String("Rules");
    // An adjustment factor based on the length of the longest string.
    int centralAdj = stringWidth / 2;
    // Updates metrics to include the latest font settings.
    metrics = g.getFontMetrics();
    // An adjustment factor based on the pixel length of the title string.
    int titleAdj = metrics.stringWidth(title) / 2;
    // Draws the title string to the main panel.
    g.drawString(title, (lineXCoordinate + centralAdj - titleAdj), lineYCoordinate);
    // Gives font settings for remaining strings.
    fontSize = 18;
    g.setFont(new Font("Arial", Font.PLAIN, fontSize));
    // Draws the remaining strings to the main panel.
    for (String line : lines) {
      g.drawString(line, lineXCoordinate, lineYCoordinate);
      // Modifies the y-coordinate position based on the height of the strings.
      lineYCoordinate += 5 * stringHeight / 4;
    }
  }

  /**
   * Draws the game instructions on the main panel.
   * @param g The Graphics argument from paintComponent.
   */
  public void drawHowToPlay(Graphics g) {
    // see drawRules method above for explanatory notation.
    FontMetrics metrics = g.getFontMetrics();
    int stringHeight = metrics.getHeight();
    String[] lines = new String[9];
    String line1 = new String("");
    lines[0] = line1;
    String line2 = new String("");
    lines[1] = line2;
    String line3 = new String("1. Select either 2, 3, or 4-Player mode below.");
    lines[2] = line3;
    String line4 = new String("2. Enter a name for each player then select start game.");
    lines[3] = line4;
    String line5 = new String("3. When it is your turn, click on your chosen card.");
    lines[4] = line5;
    String line6 = new String("4. If your card doesn't match, it won't be played.");
    lines[5] = line6;
    String line7 = new String("5. If none of your cards match, cards are automatically");
    lines[6] = line7;
    String line8 = new String("    drawn from the deck until you have a matching card.");
    lines[7] = line8;
    String line9 = new String("");
    lines[8] = line9;
    int advWidth = 0;
    int fontSize = 18;
    g.setFont(new Font("Arial", Font.PLAIN, fontSize));
    metrics = g.getFontMetrics();
    for (String line : lines) {
      if (metrics.stringWidth(line) > advWidth) {
        advWidth = metrics.stringWidth(line);
      }
    }
    int lineXCoordinate = ((3 * getWidth() / 4) - (advWidth / 2));
    int lineYCoordinate = (7 * getHeight() / 16);
    fontSize = 24;
    g.setFont(new Font("Arial", Font.BOLD, fontSize));
    String title = new String("How To Play");
    int centralAdj = (advWidth / 2);
    metrics = g.getFontMetrics();
    int titleAdj = (metrics.stringWidth(title) / 2);
    g.drawString(title, (lineXCoordinate + centralAdj - titleAdj), lineYCoordinate);
    fontSize = 18;
    g.setFont(new Font("Arial", Font.PLAIN, fontSize));
    for (String line : lines) {
      g.drawString(line, lineXCoordinate, lineYCoordinate);
      lineYCoordinate += 5 * stringHeight / 4;
    }
    String finalMessage = new String("Don't forget, eights can be played on anything!");
    centralAdj = advWidth / 2;
    metrics = g.getFontMetrics();
    titleAdj = metrics.stringWidth(finalMessage) / 2;
    g.drawString(finalMessage, lineXCoordinate + centralAdj - titleAdj, lineYCoordinate);
  }
}