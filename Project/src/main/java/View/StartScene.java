package View;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import java.awt.*;

/**
 * StartScene are responsible for providing a JPanel which contains the map
 * selecting menu
 */
public class StartScene extends JPanel {
    private final JTable        table;
    private final Dimension     startSceneDimension;


    public StartScene(Dimension d){
        startSceneDimension = d;
        setPreferredSize(startSceneDimension);

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(getBackground());
        scrollPane.setBorder(new EmptyBorder(10, 20, 5, 20));
        scrollPane.setViewportBorder(BorderFactory.createLineBorder(Color.lightGray, 1));

        scrollPane.setPreferredSize(d);
        scrollPane.setBackground(Color.white);
        add(scrollPane, BorderLayout.CENTER);
        setBackground(Color.white);

        table.setRowHeight(startSceneDimension.width / 2);
        table.setTableHeader(null);
        table.setBackground(Color.white);

        // Please do not change font...
        table.setFont(new Font("Comic sans", Font.PLAIN, 18));
        table.setSelectionBackground(Color.white);
        table.setSelectionForeground(Color.BLACK);
        table.setForeground(Color.DARK_GRAY);
//        table.setRowSelectionAllowed(true);
    }


    /**
     * Set listener on menu interaction
     * @param listener listener to be invoked on interaction with menu
     */
    public void setSelectionModel(ListSelectionListener listener){
        table.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(listener);

        // Clears selection every time the table is clicked
        table.getSelectionModel().addListSelectionListener(e -> table
                .getSelectionModel().clearSelection());
    }


    /**
     * Set model to be used in menu, contains maps names and images
     * @param model model to be used in menu
     */
    public void setModel(TableModel model){
        table.setModel(model);
                table.getColumnModel().getColumn(0).
                setCellRenderer(new ImageRenderer());
        table.getColumnModel().getColumn(0).
                setMaxWidth(startSceneDimension.width / 2);
        table.getColumnModel().getColumn(0).
                setMinWidth(startSceneDimension.width / 2);
    }
}
