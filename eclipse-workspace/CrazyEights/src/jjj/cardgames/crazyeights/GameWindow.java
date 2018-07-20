package jjj.cardgames.crazyeights;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 * Provides the frame for the Crazy Eights program.
 * 
 * @author Joshua Hardman
 *
 */
public class GameWindow extends JFrame {
  
  private static final long serialVersionUID = 515206293071492297L;

  private MainGamePanel mainGamePanel;
  private PlayerSelectionPanel playerSelectionPanel;
  private ArrayList<Player> playerList;
  private int amountOfPlayers;

  public GameWindow(String windowName) {

    // Invokes JFrame super.
    super(windowName);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Sets the background colour of the frame.
    setBackground(new Color(0x088A4B));

    // Toolkit class provides the screen dimensions.
    Toolkit tk = Toolkit.getDefaultToolkit();
    int screenWidth = ((int) tk.getScreenSize().getWidth());
    int screenHeight = ((int) tk.getScreenSize().getHeight());

    // Instantiates the welcome screen and adds it to the frame.
    WelcomePanel welcomePanel = new WelcomePanel();
    add(welcomePanel);
    pack();

    // Frame size set according to screen dimensions.
    setSize(screenWidth, screenHeight);
    setVisible(true);

    // Sets the focus on the welcome screen panel.
    welcomePanel.requestFocus();

    // Instantiates the player selection panel.
    playerSelectionPanel = new PlayerSelectionPanel();

    // Continues to the Player selection panel when any key is pressed.
    welcomePanel.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent event) {
        add(playerSelectionPanel);
        remove(welcomePanel);
        revalidate();
        repaint();
      }
    });

    // Continues to the Main game panel when "Start Game" button is pressed.
    playerSelectionPanel.getStartGameButton().addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        amountOfPlayers = playerSelectionPanel.getAmountOfPlayers();
        addPlayers();
        mainGamePanel = new MainGamePanel(playerList);
        playerSelectionMainGameTranistion();
      }
    });
  }

  /**
   * Adds players confirmed by the player selection panel to a list of players.
   */
  public void addPlayers() {
    playerList = new ArrayList<Player>();
    String playerOneName = playerSelectionPanel.getPlayerOneName();
    String playerTwoName = playerSelectionPanel.getPlayerTwoName();
    Player playerOne = new Player(playerOneName);
    Player playerTwo = new Player(playerTwoName);
    playerList.add(playerOne);
    playerList.add(playerTwo);
    if (amountOfPlayers >= 3) {
      String playerThreeName = playerSelectionPanel.getPlayerThreeName();
      Player playerThree = new Player(playerThreeName);
      playerList.add(playerThree);
    }
    if (amountOfPlayers == 4) {
      String playerFourName = playerSelectionPanel.getPlayerFourName();
      Player playerFour = new Player(playerFourName);
      playerList.add(playerFour);
    }
  }

  /**
   * Transitions from the player selection panel to the main game panel.
   */
  public void playerSelectionMainGameTranistion() {

    add(mainGamePanel);
    remove(playerSelectionPanel);
    revalidate();
    repaint();
    mainGamePanel.addPlayerOneCards();
    mainGamePanel.dealUntilMatchPlayerOne();
  }
}
