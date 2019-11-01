package Controller;

import Model.Map;
import Model.MapBuilder;
import Model.Session;
import Utils.Database.DatabaseManager;
import Utils.Database.HighScoreEntryStruct;
import Utils.Status;
import View.MapTableModel;
import View.View;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Controller is responsible for initializing hoverX Session + View.
 * Contains mouse-/actionlisteners to be invoked by view.
 * Responsible for pulling Highscore data from DatabaseManager
 */
public class Controller {

    private Session session;
    private View view;
    private Dimension dim;
    private MapTableModel mapTableModel;


    public Controller(Session session, View view, Dimension d){
        this.session = session;
        this.view = view;
        this.dim = d;

    }

    private MouseMotionListener motionListener = new MouseMotionListener() {
        @Override
        public void mouseDragged(MouseEvent e) {
        }

        @Override
        public void mouseMoved(MouseEvent e) {

            Dimension currentDimension = view.getSize();
            float xScale = (float) (currentDimension.getWidth() / dim.getWidth());
            float yScale = (float) (currentDimension.getHeight() / dim.getHeight());

            int newX = session.getTileFromPosition((int) (e.getX() / xScale), (int) (e.getY() / yScale)).getXGridPosition();
            int newY = session.getTileFromPosition((int) (e.getX() / xScale), (int) (e.getY() / yScale)).getYGridPosition();
            int width = session.getTileFromPosition((int) (e.getX() / xScale), (int) (e.getY() / yScale)).getWidth();
            view.hoverX = newX * width;
            view.hoverY = newY * width;
            view.hoverWidth = width;

        }
    };

    // Action listener for clicking in canvas
    private MouseListener clickedInCanvas = new MouseListener() {

        @Override
        public void mouseClicked(MouseEvent e) {

            Dimension currentDimension = view.getSize();
            float xScale = (float) (currentDimension.getWidth() / dim.getWidth());
            float yScale = (float) (currentDimension.getHeight() / dim.getHeight());
            session.clicked((int) (e.getX() / xScale), (int) (e.getY() / yScale));
        }

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}
    };

    private ActionListener menuRestartAL = ev -> {
        if(session.getStatus() == Status.NOTSTARTED){
            session.start();
            this.view.newGameOrRestartToggle(false);
        }else{
            session.pause();
            view.showStartScene();
        }
    };

    private ActionListener menuPauseAL = ev -> {
        if (session.getStatus() == Status.PAUSED){
            this.session.resume();
            this.view.pauseMBarToggle(false);

        }else{
            this.session.pause();
            this.view.pauseMBarToggle(true);
        }

    };

    private ActionListener menuMuteAL = ev -> {
        if(!session.aPlayer.muteStatus){
            session.aPlayer.muteAll();
        }else{
            session.aPlayer.unMuteAll();
        }
    };

    private ActionListener menuQuitAL = ev -> {

        System.exit(0);
    };


    /**
     * Change map in session and starts a new game
     */
    private ListSelectionListener updateMapSL = new ListSelectionListener() {
        long start = System.nanoTime(); // Ugly fix, listener is getting called
        // three times every click

        @Override
        public void valueChanged(ListSelectionEvent e) {
            long end = System.nanoTime();
            long duration = (end - start) / 1000000;

            /* Change map only if it was more than 500ms since listener
             * was called */
            if (e.getValueIsAdjusting() && duration > 500) {
                mapTableModel.getMap(e.getLastIndex())
                        .ifPresent(session::setMap);
                session.start();
                start = System.nanoTime();
                view.showGameScene();
            }
        }
    };

    private ActionListener menuHighScoreAL = ev -> {

        System.out.println("High score from controller");

        SwingWorker worker = new SwingWorker() {
            @Override
            protected Void doInBackground() {

                String highScoreResult = "HIGH SCORE:\n\n";

                DatabaseManager dbm = new DatabaseManager();

                ArrayList<HighScoreEntryStruct> highScoreList = new ArrayList<>();

                try {
                    highScoreList = dbm.getHighScoreList(false);
                } catch (SQLException e) {
                    highScoreResult = "Could not retrieve high score.";
                }

                System.out.println(highScoreList);

                int index = 1;
                for(HighScoreEntryStruct e : highScoreList){
                    highScoreResult = highScoreResult + index + ". " + e.getName() + ": " + e.getScore() + "\n";
                    index++;
                }

                JOptionPane.showMessageDialog(view.getFrame(), highScoreResult);

                return null;
            }
        };

        worker.execute();

    };

    private ActionListener menuAboutAL = ev -> {

        String message = "This game was created by:\n\n" +
                "Emil Soderlind - id15esd@cs.umu.se\n" +
                "Gustaf Wennerstrom - id15gwm@cs.umu.se\n" +
                "Joshua Gray - c17jgy@cs.umu.se\n" +
                "Jens Ottander - c17jor@cs.umu.se";

        JOptionPane.showMessageDialog(view.getFrame(),message);

        System.out.println("About from controller");

    };

    private ActionListener menuHelpAL = ev -> {

        String message = "This is a anti-tower defence game.\n" +
                "You choose which units to send out along a path.\n" +
                "The computer builds towers to eliminate your units.\n\n" +
                "Good luck!";

        JOptionPane.showMessageDialog(view.getFrame(),message);

        System.out.println("Help from controller");
    };


    private ActionListener addTroop1Button = ev -> {

        this.session.createTroop1();
        if(!session.aPlayer.muteStatus){
            session.aPlayer.buttonSound();
        }
    };

    private ActionListener addTroop2Button = ev -> {

        this.session.createTroop2();
        if(!session.aPlayer.muteStatus){
            session.aPlayer.buttonSound();
        }

    };

    private ActionListener addTroop3Button = ev -> {

        this.session.createTroop3();
        if(!session.aPlayer.muteStatus){
            session.aPlayer.buttonSound();
        }
    };

    private ActionListener dropPortalButton = ev -> {

        this.session.dropPortal();
        if(!session.aPlayer.muteStatus){
            session.aPlayer.buttonSound();
        }
    };

    private ActionListener dropHealButton = ev -> {

        this.session.dropHealing();
        if(!session.aPlayer.muteStatus){
            session.aPlayer.buttonSound();
        }

    };

    private void setActionListeners(View v){

        v.setClickedInCanvasML(clickedInCanvas);

        // Adding troops action listeners
        v.setAddTroop1ActionListener(addTroop1Button);
        v.setAddTroop2ActionListener(addTroop2Button);
        v.setAddTroop3ActionListener(addTroop3Button);

        // Adding effect action listeners
        v.setDropPortalActionListener(dropPortalButton);
        v.setDropHealingActionListener(dropHealButton);

        // Game menu action listeners
        v.setRestartActionListener(menuRestartAL);
        v.setpauseActionListener(menuPauseAL);
        v.setMuteActionListener(menuMuteAL);
        v.setQuitActionListener(menuQuitAL);

        // More menu action listeners
        v.setHighScoreActionListener(menuHighScoreAL);
        v.setAboutActionListener(menuAboutAL);
        v.setHelpActionListener(menuHelpAL);
        v.setMouseMotionListener(motionListener);

        v.setMapSelectionListener(updateMapSL);

//        mapTableModel = new MapTableModel();
//                                                              TODO:
//        v.setTableModelStartScene(mapTableModel);

    }

    /**
     * Initializes and start GUI (View)
     * @param session Initialized model (session) object to be set in View
     * @param args map arguments to load
     */
    public void init(Session session, String[] args){
        mapTableModel = new MapTableModel();
        try {
            SwingUtilities.invokeAndWait(()->{


                setActionListeners(view);

                view.init(session,dim);
                view.setTableModelStartScene(mapTableModel);

                view.display();
            });
        } catch (InterruptedException | InvocationTargetException e) {
            throw new RuntimeException("Initialization error: " + e.getMessage());
        }
        loadMaps(args);

    }

    private void loadMaps(String[] args){
        boolean customMapPath = false;
        if (args.length != 0){
            File file = new File(args[0]);
            if (file.exists()){
                customMapPath = true;
            } else {
                System.out.println("Could not locate the map.");
                System.exit(1);
            }
        }
        try {
            MapBuilder builder = new MapBuilder();
            builder.getExceptions().forEach(System.out::println);
            String MAP_PATH = "levels/levels.xml";
            Map[] maps = builder.fromFile(customMapPath ? args[0] : MAP_PATH, dim.width);
            if (maps != null && maps.length != 0){
                mapTableModel.addMaps(maps);
                session.setMap(maps[0]);
            } else {
                builder.getExceptions().forEach(System.out::println);
            }
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}
