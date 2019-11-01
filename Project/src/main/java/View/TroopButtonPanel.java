package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Creates and holds the three troop-spawn buttons
 */
public class TroopButtonPanel extends JPanel {
    JButton addTroops1Button;
    JButton addTroops2Button;
    JButton addTroops4Button;
    public TroopButtonPanel(ActionListener troop1, ActionListener troop2, ActionListener troop3) {


        this.setBackground(Color.white);
        this.setLayout(new GridLayout(1, 3));

        addTroops1Button = new JButton("Small troop \n5:-");
        addTroops1Button.addActionListener(troop1);
        this.add (addTroops1Button);

        addTroops2Button = new JButton("Big troop \n50:-");
        addTroops2Button.addActionListener(troop2);
        this.add (addTroops2Button);

        addTroops4Button = new JButton("Teleport troop \n70:-");
        addTroops4Button.addActionListener(troop3);
        this.add (addTroops4Button);


        this.setVisible(true);
    }

    /**
     * Sets buttons to be enabled
     * @param enabled Enabled/Disabled
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        addTroops1Button.setEnabled(enabled);
        addTroops2Button.setEnabled(enabled);
        addTroops4Button.setEnabled(enabled);
    }
}
