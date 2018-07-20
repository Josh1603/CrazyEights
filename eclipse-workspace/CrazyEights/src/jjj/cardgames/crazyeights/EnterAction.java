package jjj.cardgames.crazyeights;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;

/**
 * Allows a given key to act as a mouse-click on a JButton.
 * 
 * @author Joshua Hardman
 *
 */
public class EnterAction extends AbstractAction {

  private static final long serialVersionUID = -3873128140478584925L;

  public void actionPerformed(ActionEvent ev) {
    JButton button = (JButton) ev.getSource();
    button.doClick();
  }

}
