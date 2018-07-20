package jjj.cardgames.crazyeights;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 * Provides a welcome panel for the game Crazy Eights.
 * 
 * @author Joshua Hardman
 *
 */
public class WelcomePanel extends JPanel {

  private static final long serialVersionUID = 5526248852063817357L;

  public WelcomePanel() {
    // initializes pane with the background colour.
    setBackground(new Color(0x088A4B));
  }

  /**
   * Returns the current width of the panel.
   */
  public int getPanelWidth() {
    return getWidth();
  }

  /**
   * Returns the current height of the panel.
   */
  public int getPanelHeight() {
    return getHeight();
  }

  /**
   * Paints a welcome message on the panel.
   */
  public void paintComponent(Graphics g) {
    // Smoothes out text.
    Graphics2D g2D = (Graphics2D) g;
    g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    // Sets font properties.
    int fontSize = 60;
    g.setFont(new Font("Arial", Font.PLAIN, fontSize));
    g.setColor(Color.white);
    // Draws the main title.
    drawWelcomeMessage(g);
    // Sets new font properties.
    fontSize = 12;
    g.setFont(new Font("Arial", Font.BOLD, fontSize));
    // Draws a 'Press to Continue' message.
    drawPressToContinueMessage(g);
  }

  /**
   * Draws a Welcome Message at a relative location on the panel.
   * 
   * @param g The Graphics argument from paintComponent.
   */
  public void drawWelcomeMessage(Graphics g) {
    // The welcome message.
    String welcomeMessage = new String("Welcome to Crazy Eights!");
    // Calculates the width of the String
    FontMetrics metrics = g.getFontMetrics();
    int advWidth = metrics.stringWidth(welcomeMessage);
    // Calculates the relative coordinates.
    int centerXPosition = getPanelWidth() / 2;
    int stringWidthCorrectionFactor = -advWidth / 2;
    int centerYPosition = getPanelHeight() / 2;
    int xPos = centerXPosition + stringWidthCorrectionFactor;
    int yPos = centerYPosition;
    // Draws the welcome message on the panel at the given coordinates.
    g.drawString(welcomeMessage, xPos, yPos);
  }

  /**
   * Draws an instructional message at a relative location on the panel.
   * 
   * @param g The Graphics argument from paintComponent.
   */
  public void drawPressToContinueMessage(Graphics g) {
    // The instructional message.
    String pressToContinueMessage = new String("PRESS ANY BUTTON TO CONTINUE");
    // Calculates the width of the String.
    FontMetrics metrics = g.getFontMetrics();
    int advWidth = metrics.stringWidth(pressToContinueMessage);
    // Calculates the relative coordinates.
    int centerXPosition = getPanelWidth() / 2;
    int stringWidthCorrectionFactor = -advWidth / 2;
    int LowerQuarterYPosition = 3 * getPanelHeight() / 4;
    int xPos = centerXPosition + stringWidthCorrectionFactor;
    int yPos = LowerQuarterYPosition;
    // Draws the instructional message on the panel at the given coordinates.
    g.drawString(pressToContinueMessage, xPos, yPos);
  }
}
