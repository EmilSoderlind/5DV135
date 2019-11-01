package View;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * ImageRenderer are responsible for rendering objects in map-select menu.
 */
public class ImageRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = new JLabel();
        if (value != null) {
            label.setVerticalAlignment(JLabel.CENTER);
            label.setHorizontalAlignment(JLabel.CENTER);

            Image image = ((Image) value).getScaledInstance(table.getWidth() / 2, table.getWidth() / 2, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(image));
        }
        return label;
    }
}