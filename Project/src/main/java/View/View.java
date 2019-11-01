package View;

import Model.GameStatsDrawable;
import Model.Observable;
import Model.Observer;
import Model.Units.Drawable;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


/**
 * View is responsible for building and maintaining a GUI
 * for the user to interact with. Sets buttons/Menu/mouse listeners
 * Receives drawable objects from the (model) Session,
 * which View gives to GameCanvas for rendering
 */
public class View implements Observer  {
    private JFrame frame;
    private GameCanvas gameCanvas;
    private JPanel container;
    private StartScene startScene;
    private CardLayout cardLayout;

    // Submenu "Game"
    private ActionListener restartAL;
    private ActionListener pauseAL;
    private ActionListener muteAL;
    private ActionListener quitAL;

    // Submenu "More"
    private ActionListener highScoreAL;
    private ActionListener aboutAL;
    private ActionListener helpAL;

    // Spawn troops buttons
    private ActionListener addTroop1AL;
    private ActionListener addTroop2AL;
    private ActionListener addTroop3AL;

    // Activate Effects buttons
    private ActionListener dropPortalAL;
    private ActionListener dropHealingAL;

    // Clicked in canvas mouselistener
    private MouseListener clickedInCanvas;

    // Start Scene listeners
    private ListSelectionListener mapSelectionListener;

    private Observable obs;
    private int observerID;
    private static int observerIDTracker = 0;

    private Dimension canvasDimension;
    private MouseMotionListener motionListener;
    public int hoverX = -1000;
    public int hoverY = -1000;
    public int hoverWidth = 0;

    private TroopButtonPanel troopPanel;
    private EffectButtonPanel effectPanel;

    /**
     * Initializes and creates GUI and setup observer/observable
     * @param obs Observable instance
     * @param dim Dimenstions to set GameCanvas to
     */
    public void init(Observable obs, Dimension dim){
        this.obs = obs;
        this.observerID = ++observerIDTracker;
        obs.addObserver(this);

        canvasDimension = dim;


        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.black);

        cardLayout = new CardLayout();
        startScene = new StartScene(canvasDimension);

        container = new JPanel(cardLayout);
        gameCanvas = new GameCanvas(dim);


        container.add(startScene, "startScene");
        container.add(gameCanvas, "gameScene");

        frame.add(container);
        startScene.setSelectionModel(mapSelectionListener);


        // Adding listener to canvas
        gameCanvas.setClickedOnCanvas(this.clickedInCanvas);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        troopPanel = new TroopButtonPanel(addTroop1AL,addTroop2AL,addTroop3AL);
        troopPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        troopPanel.setBorder(new TitledBorder("Troops"));
        bottomPanel.add(troopPanel, BorderLayout.SOUTH);
        effectPanel = new EffectButtonPanel(dropPortalAL, dropHealingAL);
        effectPanel.setBorder(new TitledBorder("Effects"));
        bottomPanel.add(effectPanel, BorderLayout.NORTH);
        frame.add(bottomPanel, BorderLayout.PAGE_END);

        createMenuBar(false,false,false);


        frame.pack();
        frame.setVisible(true);

        showStartScene();

        gameCanvas.addMouseMotionListener(motionListener);
    }


    /**
     * Returns the JFrame of the View
     * @return JFrame
     */
    public JFrame getFrame(){
        return this.frame;
    }
    public void setMouseMotionListener(MouseMotionListener motionListener){
        this.motionListener = motionListener;
    }
    /**
     * Used by controller to set mouselistener on click in canvas
     * @param ml Mouselistener
     */
    public void setClickedInCanvasML(MouseListener ml){
        this.clickedInCanvas = ml;
    }

    /**
     * Used by controller to set actionlistener on Restart/New Game in JMenuBar
     * @param al Actionlistener
     */
    public void setRestartActionListener(ActionListener al){
        this.restartAL = al;
    }

    /**
     * Used by controller to set actionlistener on Pause/Unpause in JMenuBar
     * @param al Actionlistener
     */
    public void setpauseActionListener(ActionListener al){
        this.pauseAL = al;
    }

    /**
     * Used by controller to set actionlistener on Mute/Unmute in JMenuBar
     * @param al Actionlistener
     */
    public void setMuteActionListener(ActionListener al){
        this.muteAL = al;
    }

    /**
     * Used by controller to set actionlistener on Quit in JMenuBar
     * @param al Actionlistener
     */
    public void setQuitActionListener(ActionListener al){
        this.quitAL = al;
    }

    /**
     * Used by controller to set actionlistener on Highscore in JMenuBar
     * @param al Actionlistener
     */
    public void setHighScoreActionListener(ActionListener al){
        this.highScoreAL = al;
    }

    /**
     * Used by controller to set actionlistener on About in JMenuBar
     * @param al Actionlistener
     */
    public void setAboutActionListener(ActionListener al){
        this.aboutAL = al;
    }

    /**
     * Used by controller to set actionlistener on Help in JMenuBar
     * @param al Actionlistener
     */
    public void setHelpActionListener(ActionListener al){
        this.helpAL = al;
    }

    /**
     * Used by controller to set actionlistener on 1st spawntroop button
     * @param al Actionlistener
     */
    public void setAddTroop1ActionListener(ActionListener al){
        this.addTroop1AL = al;
    }

    /**
     * Used by controller to set actionlistener on 2nd spawntroop button
     * @param al Actionlistener
     */
    public void setAddTroop2ActionListener(ActionListener al){
        this.addTroop2AL = al;
    }

    /**
     * Used by controller to set actionlistener on 3rd spawntroop button
     * @param al Actionlistener
     */
    public void setAddTroop3ActionListener(ActionListener al){
        this.addTroop3AL = al;
    }

    /**
     * Used by controller to set actionlistener on drop portal button
     * @param al Actionlistener
     */
    public void setDropPortalActionListener(ActionListener al){
        this.dropPortalAL = al;
    }

    /**
     * Used by controller to set actionlistener on drop healing effect button
     * @param al Actionlistener
     */
    public void setDropHealingActionListener(ActionListener al){
        this.dropHealingAL = al;
    }

    private void createMenuBar(Boolean newGame, Boolean pause, Boolean mute){

        //Create the menu bar.
        JMenuBar menuBar = new JMenuBar();

        //Create Game menu
        JMenu gameMenu = createGameMenu("New Game", "Pause", "Mute", restartAL, pauseAL, muteAL, quitAL);

        //Create more menu
        JMenu moreMenu = createMoreMenu(aboutAL, helpAL);

        menuBar.add(gameMenu);
        menuBar.add(moreMenu);

        this.frame.setJMenuBar(menuBar);
    }

    /**
     * Switch between textLabel Resume/Pause
     * @param paused true-Resume / false-Pause
     */
    public void pauseMBarToggle(Boolean paused){
        int pauseIndex = 1;
        if(paused) {
            setGameMenuTextLabel(pauseIndex,"Resume",this.frame.getJMenuBar());
            System.out.println("Resuming");
        }else{
            setGameMenuTextLabel(pauseIndex,"Pause",this.frame.getJMenuBar());
            System.out.println("Pausing");

        }
    }


    /**
     * Switch between textLabel Mute/Unmute
     * @param muted true-Mute / false-Unmute
     */
    public void muteMBarToggle(Boolean muted){
        int muteIndex = 2;
        if(muted) {
            setGameMenuTextLabel(muteIndex,"Mute",this.frame.getJMenuBar());
        }else{
            setGameMenuTextLabel(muteIndex,"Unmute",this.frame.getJMenuBar());
        }
    }

    /**
     * Toggles between "New Game" or "Restart" in JMenubar
     * @param newGame true - "New Game" / False - "Restart"
     */
    public void newGameOrRestartToggle(Boolean newGame){
        newGameMenuBar(newGame);

    }


    private void newGameMenuBar(Boolean newGame) {
        int newGameIndex = 0;
        if(newGame) {
            setGameMenuTextLabel(newGameIndex,"New Game",this.frame.getJMenuBar());
        }else{
            setGameMenuTextLabel(newGameIndex,"Restart",this.frame.getJMenuBar());
        }
    }

    // Changes textlabels of Game menu
    private JMenuItem setGameMenuTextLabel(int gameMenuIndex, String menuText, JMenuBar jmb) {
        JMenuItem restartItem = jmb.getMenu(0).getItem(gameMenuIndex);
        restartItem.setText(menuText);
        return restartItem;
    }

    private JMenu createGameMenu(String newGameLabel, String pauseLabel, String muteLabel, ActionListener restartAL, ActionListener pauseAL, ActionListener muteAL, ActionListener quitAL) {
        JMenu gameMenu = new JMenu("Game");

        JMenuItem restartItem = new JMenuItem(newGameLabel);
        restartItem.addActionListener(restartAL);
        gameMenu.add(restartItem);

        JMenuItem pauseItem = new JMenuItem(pauseLabel);
        pauseItem.addActionListener(pauseAL);
        gameMenu.add(pauseItem);

        JMenuItem muteItem = new JMenuItem(muteLabel);
        muteItem.addActionListener(muteAL);
        gameMenu.add(muteItem);

        JMenuItem quitItem = new JMenuItem("Quit");
        quitItem.addActionListener(quitAL);
        gameMenu.add(quitItem);



        return gameMenu;
    }

    private JMenu createMoreMenu(ActionListener aboutAL, ActionListener helpAL) {
        JMenu moreMenu = new JMenu("More");

        JMenuItem highScoreItem = new JMenuItem("High score", KeyEvent.VK_T);
        highScoreItem.addActionListener(highScoreAL);
        moreMenu.add(highScoreItem);

        JMenuItem aboutItem = new JMenuItem("About", KeyEvent.VK_T);
        aboutItem.addActionListener(aboutAL);
        moreMenu.add(aboutItem);


        JMenuItem helpItem = new JMenuItem("Help", KeyEvent.VK_T);
        helpItem.addActionListener(helpAL);
        moreMenu.add(helpItem);
        return moreMenu;
    }

    /**
     * Make the GUI (JFrame) visible
     */
    public void display(){
        this.frame.setVisible(true);
    }


    /**
     * Sets table model for start scene
     * @param model tableModel to be set
     */
    public void setTableModelStartScene(TableModel model){
        startScene.setModel(model);
    }

    /**
     * Called with game objects to be rendered in View:s GameCanvas
     * @param drawablesList list of Drawables to be drawn in GameCanvas
     * @param credits Number of credits to be drawn in canvas
     * @param points Number of points to be drawn in canvas
     */
    @Override
    public void update(ArrayList<Drawable> drawablesList, int credits, int points) {

        drawablesList.add(new HoverRect(hoverX, hoverY, hoverWidth));
        GameStatsDrawable gsd = new GameStatsDrawable(credits, points, new Point(canvasDimension.height-150,0));
        drawablesList.add(gsd);

        this.gameCanvas.setDrawables(drawablesList);
    }


    /**
     * Presented from model after game is finished.
     * Present pop-up and ask for users name to be published to highScore DB.
     * @param finalPoints final score points to be presented in pop-up
     * @return Name of player
     */
    @Override
    public String getNameForHighScore(int finalPoints) {

        // prompt the user to enter their name
        String name = JOptionPane.showInputDialog(frame, "SUBMIT SCORE TO HIGHSCORE\n\nScore: " + finalPoints+ "\n\nWhat's your name?");
        showStartScene();
        return name;
    }

    /**
     * Hover-effect on tiles by mouse in-game
     */
    class HoverRect implements Drawable{
        int x,  y, width;
        public HoverRect(int x, int y, int width){
            this.x = x;
            this.y = y;
            this.width = width;
        }
        @Override
        public void draw(Graphics g) {
            ((Graphics2D)g).setStroke(new BasicStroke(1));
            g.setColor(Color.MAGENTA);
            g.drawRect(x,y,width,width);
            g.drawRect(x-1,y - 1,width + 1 ,width + 1);
        }
    }

    /**
     * Sets listener for selection in map-view
     * @param l listener
     */
    public void setMapSelectionListener(ListSelectionListener l){
        this.mapSelectionListener = l;
    }

    /**
     * Present playing-map screen
     */
    public void showGameScene(){
        cardLayout.show(container, "gameScene");
        effectPanel.setEnabled(true);
        troopPanel.setEnabled(true);
    }


    /**
     * Present map-choosing screen
     */
    public void showStartScene(){
        cardLayout.show(container, "startScene");
        effectPanel.setEnabled(false);
        troopPanel.setEnabled(false);
    }

    /**
     * Getter for size of game canvas
     * @return Size as Dimension
     */
    public Dimension getSize() {
        return gameCanvas.getSize();
    }
}