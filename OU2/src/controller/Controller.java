package controller;

import model.Model;
import model.Program;
import view.View;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

/**
 * Responsible for:
 * - Initiating Model + View
 * - Handeling actionlisteners on GUI
 * - Manipulating model for selecting channel + request new parse
 *
 * Doing everything with Swing workers
 *
 */
public class Controller {

    private Model model;
    private View view;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Initializing view with actionlisteners and show its GUI
     */
    public void init(){

        try {
            SwingUtilities.invokeAndWait(()->{

                setActionListeners(view);
                view.init(model);
                view.display();

            });

        } catch (InterruptedException | InvocationTargetException e) {
            throw new RuntimeException("Initialization error: "
                    + e.getMessage());
        }
    }

    /**
     * Set actionlisteners in view
     * @param v view instance
     */
    private void setActionListeners(View v){
        v.setTableListener(tableListener);
        v.setUpdateButtonPressed(updateButtonPressed);
        v.setSelectingChannelInMenu(selectingChannel);
        v.setExitInMenu(exitInMenu);
    }

    ListSelectionListener tableListener = new ListSelectionListener() {
        /**
         * Called when user click in program in table
         * Prepare popup message and request view to present popup
         * @param event Event from click in table
         */
        @Override
        public void valueChanged(ListSelectionEvent event) {

            SwingUtilities.invokeLater(() -> {

                int selectedRow = view.getSelectedRowInTable();

                Program currProgram = model.programAt(selectedRow);

                if(selectedRow == -1){
                    return;
                }

                String popupMessage = currProgram.getTitle() + "\n\n";
                popupMessage += "Beskrivning: \n";
                popupMessage += currProgram.getDescription() + "\n\n";
                popupMessage += "Sändning: \n";
                popupMessage += currProgram.getPrettyStartTime() + "-" +
                        currProgram.getPrettyEndTime();

                view.presentPopup(popupMessage);

            });
        }
    };

    ActionListener updateButtonPressed = new ActionListener() {
        /**
         * Called when user click parse button in GUI
         * Request new parse from model
         * @param e ActionEvent from parse button in GUI
         */
        @Override
        public void actionPerformed(ActionEvent e) {

            SwingUtilities.invokeLater(() -> {

                model.requestNewParse();

                // model.

                view.setSearchButtonLoading(true);
            });
        }
    };

    ActionListener selectingChannel = new ActionListener() {
        /**
         * Called when user select a channel in Menu
         * Changing selected channel in model
         * @param e ActionEvent from menu in GUI
         */
        @Override
        public synchronized void actionPerformed(ActionEvent e) {

            SwingUtilities.invokeLater(() -> {

                int selectedChannelID = (Integer)((JMenuItem)e.getSource()).
                        getClientProperty( "channelID" );

                model.setSelectedChannelID(selectedChannelID);
                model.notifyObs();
            });
        }
    };

    /**
     * Handeling exit button in menu in GUI
     */
    ActionListener exitInMenu = e -> System.exit(0);

}
