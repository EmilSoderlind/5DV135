package view;

import model.ChannelSchedule;
import model.Observer_Subject.Observer;
import model.Observer_Subject.Subject;
import model.Program;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


/**
 * Responsible for setting up and providing GUI to user
 * Displaying data and forward GUI actions such as buttons, clicks etc.
 */
public class View implements Observer {

    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JMenuBar menuBar;
    private JButton reparseButton;
    private JLabel picLabel;

    private Subject subject;
    private int observerID;
    private static int observerIDTracker = 0;

    // Listeners
    private ListSelectionListener tableListener;
    private ActionListener updateButtonPressed;
    private ActionListener selectingChannelInMenu;
    private ActionListener exitInMenu;


    /**
     * Initialize view with components, actionlisteners & subject
     * @param subject Subject to get updates from
     */
    public void init(Subject subject) {

        Thread t = Thread.currentThread();
        String name = t.getName();

        this.subject = subject;
        this.observerID = ++observerIDTracker;
        this.subject.addObserver(this);


        // BYGG GUI
        frame = new JFrame("RADIO-TABLÅ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.black);


        // create object of table and table model
        table = new JTable();

        table.getSelectionModel().addListSelectionListener(tableListener);

        tableModel = new DefaultTableModel(0, 0);
        table.setModel(tableModel);

        // add header of the table
        String header[] = new String[] { "Program", "Börjar", "Slutar", "Sänt"};

        tableModel.setColumnIdentifiers(header);

        String load = "Laddar.";
        Object loadingObject = new Object[] { load, load, load, load};
        tableModel.addRow((Object[]) loadingObject);

        JScrollPane sp=new JScrollPane(table);
        frame.add(sp, BorderLayout.CENTER);

        menuBar = createMenuBar();

        reparseButton = new JButton("Laddar..");
        reparseButton.addActionListener(updateButtonPressed);
        frame.add(reparseButton, BorderLayout.SOUTH);

        frame.setSize(800,500);

        frame.setJMenuBar(menuBar);

        picLabel = new JLabel();
        frame.add(picLabel,BorderLayout.WEST);

    }

    /**
     * Display Swing Frame
     */
    public void display(){
        this.frame.setVisible(true);
    }


    /**
     * Create JMenuBar for program
     * @return Created JMenuBar
     */
    private JMenuBar createMenuBar(){

        JMenuBar menuBar = new JMenuBar();

        //JMenu exitMenu = new JMenu("Allmänt");
        //JMenuItem exitProgram = new JMenuItem("Avsluta");
        //exitProgram.addActionListener(exitInMenu);
        //exitMenu.add(exitProgram);

        //JMenu channelMenu = new JMenu("Välj kanal");

        //menuBar.add(exitMenu);
        //menuBar.add(channelMenu);

        return menuBar;
    }

    /**
     * Set parse button label to display loading
     * @param loading True if loading else False
     */
    public void setSearchButtonLoading(boolean loading){
        if(loading){
            reparseButton.setText("Laddar..");
        }else{
            reparseButton.setText("Uppdatera tablå");
        }
    }

    /**
     * Called by subject when new data is requested/parsed
     * Update menu wth available channels
     * Update table with scheduled programs
     * Update channel logo
     *
     * @param schedule Latests parsed schedule data
     * @param channelIDS Available channel ID:s
     * @param channelNames Available channel names
     */
    @Override
    public void update(ChannelSchedule schedule,
                       ArrayList<Integer> channelIDS,
                       ArrayList<String> channelNames) {

        // Ask swing/EDT-thread to do this when he feels for it
        SwingUtilities.invokeLater(() -> {

            if(schedule == null){
                presentPopup("Could not get data from Sveriges Radio.\n" +
                        "Try again later.");
                return;
            }

            setSearchButtonLoading(false);

            updateMenu(channelIDS, channelNames);

            updateTable(schedule);

            updateChannelLogo(schedule);

            frame.pack();

        });



    }


    /**
     * Update channel logo to selected channel
     * @param schedule parsed schedule data
     */
    private void updateChannelLogo(ChannelSchedule schedule) {
        BufferedImage myPicture = (BufferedImage) schedule.getImage();
        picLabel.setIcon(new ImageIcon(myPicture));

        picLabel.revalidate();
        picLabel.repaint();
    }


    /**
     * Update table with parsed schedule data
     * @param schedule parsed schedule data
     */
    private void updateTable(ChannelSchedule schedule) {
        // Removes current rows
        tableModel.setRowCount(0);

        // Adding programs
        for (int count = 0; count <= schedule.numberOfprograms()-1; count++) {

            Program currentProgram = schedule.getProgramAtIndex(count);

            String programTitle = currentProgram.getTitle();
            String startTime = currentProgram.getPrettyStartTime();
            String endTime = currentProgram.getPrettyEndTime();

            if(currentProgram.getBroadcasted()){
                tableModel.addRow(
                        new Object[] { programTitle, startTime, endTime, "x"});
            }else{
                tableModel.addRow(
                        new Object[] { programTitle, startTime, endTime, " "});
            }

        }
    }

    /**
     * Replace available channels in channel menu
     * @param IDS Channel ID:s
     * @param names Channel names
     */
    private void updateMenu(ArrayList<Integer> IDS, ArrayList<String> names) {

        // Uppdating Jmenu with channels
        int numberOfMenus = 3;

        menuBar.removeAll();

        // Creating menus
        for(int i = 0; i<numberOfMenus;i++){
            menuBar.add(new JMenu("Välj kanal (" + (i+1) + ")"));
        }

        int channelIndex = 0;
        for (String ChannelName: names) {
            JMenuItem tempItem = new JMenuItem(ChannelName);

            tempItem.putClientProperty("channelID", IDS.get(channelIndex));
            tempItem.addActionListener(selectingChannelInMenu);

            if(channelIndex >= 0 && channelIndex <= 18) {
                menuBar.getMenu(0).add(tempItem);
            }if(channelIndex > 18 && channelIndex <= 36) {
                menuBar.getMenu(1).add(tempItem);
            }if(channelIndex > 36) {
                menuBar.getMenu(2).add(tempItem);
            }

            channelIndex++;
        }

        menuBar.revalidate();
    }

    /**
     * Returns selected row in table
     * @return row number
     */
    public int getSelectedRowInTable(){
        return table.getSelectedRow();
    }

    /**
     * Present popup dialog with message
     * @param message message to display
     */
    public void presentPopup(String message){
        JOptionPane.showMessageDialog(null, message);
    }


    /**
     * Set actionlistener on selecting new channel menu button
     * @param selectingChannel actionlistener on selecting channel
     */
    public void setSelectingChannelInMenu(ActionListener selectingChannel) {
        this.selectingChannelInMenu = selectingChannel;
    }

    /**
     * Set actionlistener to update schedule button
     * @param updateButtonPressed Actionlistener on update schedule button
     */
    public void setUpdateButtonPressed(ActionListener updateButtonPressed) {
        this.updateButtonPressed = updateButtonPressed;
    }

    /**
     * Set actionlistener to selected row in table event
     * @param tableListener Actionlistener on selected row in table event
     */
    public void setTableListener(ListSelectionListener tableListener) {
        this.tableListener = tableListener;
    }

    /**
     * Set actionlistener to exit in menu event
     * @param exitInMenu Actionlistener on exit in menu
     */
    public void setExitInMenu(ActionListener exitInMenu) {
        this.exitInMenu = exitInMenu;
    }
}
