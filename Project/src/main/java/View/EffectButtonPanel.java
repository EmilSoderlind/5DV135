package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Creates and holds the two effect activation buttons
 */
public class EffectButtonPanel extends JPanel {
    private JButton dropPortalButton;
    private JButton dropHealingButton;
    public EffectButtonPanel(ActionListener dropPortal, ActionListener dropHealing) {

        this.setBackground(Color.white);
        this.setLayout(new GridLayout(1, 3));

        dropPortalButton = new JButton("Drop Portal");
        dropPortalButton.addActionListener(dropPortal);
        this.add (dropPortalButton);

        dropHealingButton = new JButton("Healing Effect \n100:-");
        dropHealingButton.addActionListener(dropHealing);
        this.add (dropHealingButton);

        this.setVisible(true);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        dropPortalButton.setEnabled(enabled);
        dropHealingButton.setEnabled(enabled);
    }

}