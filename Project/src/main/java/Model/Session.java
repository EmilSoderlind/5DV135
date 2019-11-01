package Model;

import Model.Effects.FireEffect;
import Model.Effects.FrostEffect;
import Model.Effects.HealingEffect;
import Model.Effects.TeleportEntrance;
import Model.Units.*;
import Utils.AudioPlayer;
import Utils.Database.DatabaseManager;
import Utils.Database.HighScoreEntryStruct;
import Utils.GridIndex;
import Utils.Quadtree;
import Utils.Status;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import static Utils.Status.*;

/**
 * Class that handles the logic of the game each game loop.
 */
public class Session implements ModelInterface, Observable {
    private ArrayList<Observer> observers;
    private Map currentMap;
    private CopyOnWriteArrayList<Troop> troopList;
    private ArrayList<Tower> towerList;
    private ArrayList<Projectile> projectileList;
    private TeleportTroop teleportTroop;

    private ArrayList<Drawable> drawable;
    private volatile Status gameState = NOTSTARTED;
    private Quadtree quad;
    private int credits;
    private int points;
    private final double timeStep = 0.016;
    private Dimension canvasDimension;
    public boolean holdingHealEffect = false;
    private Integer maxImageSize;
    boolean highScoreShownThisGame = false;

    double lastUpdateTime = 0;
    double deltaTime = 0;

    /**
     * Keeps track if a tower have been spawned this SPAWNTOWER_TIMER-time
     */
    boolean botActionPerformedThisTime = false;
    boolean maximumTowerSpawned = false;


    final int TICKS_PER_SECOND = 60;
    final int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
    final int MAX_FRAMESKIP = 5;

    // Gameplay final numbers ----------
    private final int STARTCREDITS = 1000; //1000;
    private final int GOALPOINTS = 1000;

    private final int SMALLTROOP_COST = 5;
    private final int BIGTROOP_COST = 50;
    private final int TELEPORTTROOP_COST = 70;

    private final int CHANGEDIRECTION_COST = 100;
    private final int HEALINGEFFECT_COST = 100;


    private final int SPAWNTOWER_TIMER = 2;

    private final int EFFECT_TIME = 15;
    // Gameplay final numbers ----------

    public AudioPlayer aPlayer;


    public Session(Dimension canvasDim){

        this.points = 0;
        this.troopList = new CopyOnWriteArrayList<>();
        this.towerList = new ArrayList<>();
        this.projectileList = new ArrayList<>();
        this.canvasDimension = canvasDim;
        this.observers = new ArrayList<>();
        this.drawable = new ArrayList<>();
        this.aPlayer = new AudioPlayer();

        this.credits = STARTCREDITS;
    }


    /**
     * Main game loop:
     *
     * Let bot add towers + effects.
     * Update units.
     * Towers scan for enemies
     * Send gameObjects Drawables to view.
     *
     * Adjust updates per second
     */
    public void gameLoop() {
        gameState = NOTSTARTED;

        int loops;
        double next_game_tick = System.currentTimeMillis();

        while (gameState != DONE) {

           loops = 0;
           while (gameState == RUNNING && System.currentTimeMillis() > next_game_tick && loops < MAX_FRAMESKIP) {

               performBotAction(SPAWNTOWER_TIMER);

               updateUnits();
               towerScan(timeStep);
               notifyObs();

               if (credits <= 0 && troopList.isEmpty() && gameState == RUNNING && this.credits <= STARTCREDITS) {

                   gameState = NOTSTARTED;

                   addScoreToHighScore();

                   flush();
                   break;
               }

               next_game_tick += SKIP_TICKS;
               loops++;
           }
        }
    }

    private void performBotAction(int nSecounds) {

        // Perform bot-action every nSecounds
        if((System.currentTimeMillis() / 1000) % nSecounds == 0){

            // If action have not been performed this time (/nsecounds)
            if(!botActionPerformedThisTime){
                highScoreShownThisGame = false;

                for(int row = 0; row<currentMap.getRows();row++){
                    for(int col = 0; col<currentMap.getCols();col++){

                        Tile tempTile = getRandomTile();

                        if(!tempTile.isWalkable() && !tempTile.hasTower()){ // Spawn tower

                            spawnTower(tempTile);

                            botActionPerformedThisTime = true;
                            return;

                        }else if(tempTile.isWalkable()) { // Spawn effect

                            spawnEffect(tempTile);

                            botActionPerformedThisTime = true;
                            return;
                        }

                    }
                }
                maximumTowerSpawned = true;
            }
        }else{
            botActionPerformedThisTime = false;
        }
    }

    private Tile getRandomTile() {
        Random rand = new Random();
        int randomX = rand.nextInt(currentMap.getRows());
        int randomY = rand.nextInt(currentMap.getCols());
        return currentMap.getTile(new GridIndex(randomX, randomY));
    }

    private void spawnEffect(Tile tempTile) {

        Random rand = new Random();
        int randomEffect = rand.nextInt(2) + 0;

        if(randomEffect == 1){
            FireEffect fe = new FireEffect(EFFECT_TIME,(int)tempTile.getFrame().getCenterX(),(int)tempTile.getFrame().getCenterY(),maxImageSize);
            tempTile.addEffect(fe);
        }else{
            FrostEffect fe = new FrostEffect(EFFECT_TIME,(int)tempTile.getFrame().getCenterX(),(int)tempTile.getFrame().getCenterY(),maxImageSize);
            tempTile.addEffect(fe);
        }
    }

    private void spawnTower(Tile tempTile) {

        // Decide which tower to spawn
        Random ran = new Random();
        int r = ran.nextInt(10) + 1;

        if(r <= 3){
            // Missile tower
            createTower2(tempTile);

        }else{
            // Small tower
            createTower1(tempTile);

        }
    }

    private void addScoreToHighScore() {

        System.out.println("addScoreToHighScore| highScoreShownThisGame: " + highScoreShownThisGame);

        if(!highScoreShownThisGame) {

            // Request name for high score
            for (Observer observer : observers) {
                String name = observer.getNameForHighScore(this.points);

                if (name != "" && name != null && !highScoreShownThisGame) {
                    DatabaseManager dbm = new DatabaseManager();
                    try {

                        HighScoreEntryStruct newScore = new HighScoreEntryStruct();

                        newScore.setScore(this.points);
                        newScore.setName(name);

                        dbm.setHighScoreEntry(newScore);
                        highScoreShownThisGame = true;

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Restart game parameters for new game
     */
    public void restart(){
        gameState = RUNNING;
        flush();
        notifyObs();

        lastUpdateTime = 0;
        deltaTime = 0;

        this.credits = STARTCREDITS;
        this.points = 0;
        this.gameState = RUNNING;
    }

    private void flush(){
        this.troopList = new CopyOnWriteArrayList<>();
        this.towerList = new ArrayList<>();
        this.projectileList = new ArrayList<>();
        this.drawable = new ArrayList<>();
        currentMap.flushTiles();
    }

    /**
     * Start game
     */
    public void start(){
        resume();
        //aPlayer.run();
    }

    /**
     * Pause game loop and pause music
     */
    public void pause() {
        this.gameState = PAUSED;
        //aPlayer.pauseMusic();
    }

    /**
     * Resume game loop after pause
     */
    public void resume() {
        this.gameState = RUNNING;
        //aPlayer.resumeMusic();
    }

    /**
     * Returns state of game
     * @return gameState
     */
    public Status getStatus() {
        return gameState;
    }

    /**
     * Spawn small troop
     */
    public void createTroop1(){
        if(this.gameState == RUNNING && this.credits >= SMALLTROOP_COST){ //TODO
            int x = (int)currentMap.getStartTile().getFrame().getCenterX();
            int y = (int)currentMap.getStartTile().getFrame().getCenterY();

            Troop newTroop = new SmallTroop((int)currentMap.getStartTile().getFrame().getCenterX(),(int) currentMap.getStartTile().getFrame().getCenterY(),maxImageSize);

            newTroop.setDirection(currentMap.getStartTile().getDirection());
            this.troopList.add(newTroop);

            this.credits -= SMALLTROOP_COST;
        }
    }

    /**
     * Spawn big troop
     */
    public void createTroop2(){
        if(this.gameState == RUNNING && this.credits >= BIGTROOP_COST){
            Troop newTroop = new BigTroop((int)currentMap.getStartTile().getFrame().getCenterX(),(int) currentMap.getStartTile().getFrame().getCenterY(),maxImageSize);
            newTroop.setDirection(currentMap.getStartTile().getDirection());
            this.troopList.add(newTroop);

            this.credits -= BIGTROOP_COST;

        }
    }

    /**
     * Spawn teleport troop
     */
    public void createTroop3(){
        if(this.gameState == RUNNING  && this.credits >= TELEPORTTROOP_COST){

            TeleportTroop newTroop = new TeleportTroop((int) currentMap.getStartTile().getFrame().getCenterX(), (int) currentMap.getStartTile().getFrame().getCenterY(),maxImageSize);
            newTroop.setDirection(currentMap.getStartTile().getDirection());
            this.troopList.add(newTroop);
            teleportTroop = newTroop;

            this.credits -= TELEPORTTROOP_COST;
        }
    }


    /**
     * Create laser tower at tile:s position
     * @param t Tile to place tower
     */
    public void createTower1(Tile t){

        if(this.gameState == RUNNING && !t.hasTower() && !t.isWalkable()){
            this.towerList.add(new LaserTower(t,maxImageSize, aPlayer));
            t.addTower();
        }
    }

    /**
     * Create missile tower at tile:s position
     * @param t Tile to place tower
     */
    public void createTower2(Tile t){

        if(this.gameState == RUNNING && !t.hasTower() && !t.isWalkable()){

            this.towerList.add(new MissileTower(t,projectileList,maxImageSize, aPlayer));
            t.addTower();

        }
    }

    private void updateUnits(){

        for(Iterator<Projectile> it = projectileList.iterator(); it.hasNext();){
            Projectile p = it.next();
            p.moveToTarget();
            if(p.reachedTarget()) {

                if(!p.split(troopList)){
                    it.remove();
                }
            }
        }


        for (Troop troop : troopList) {
//            Troop troop = it.next();
            handleTile(troop, null);
            if (troop.hasReachedGoal()) {

                this.points += troop.getHP();
                this.credits += 100;

                troop.setTargetableStatus(false);

                troopList.remove(troop);
            } else if (troop.getHP() > 0) {
                troop.statusUpdate();
                troop.move();

            } else {
                troopList.remove(troop);
            }
        }


    }

    private void handleTile(Troop troop, Iterator<Troop> it){

        Tile tile = getTileFromPosition((int) troop.getXPos(), (int) troop.getYPos());
        if (LandOn.class.isAssignableFrom(tile.getClass())){
            ((LandOn)tile).landOn(troop);
        }
    }

    private void towerScan(double timeStep){
        for (Tower tower: towerList) {
            tower.scan(troopList,timeStep);
        }
    }

    @Override
    public void addObserver(Observer newObs) {
        this.observers.add(newObs);
    }

    @Override
    public void deleteObserver(Observer observer) {

        this.observers.remove(observer);
    }

    @Override
    public synchronized void notifyObs() {
        this.drawable = new ArrayList<>();
        this.drawable.add(this.currentMap);
        for (int i = 0; i < currentMap.getCols(); i++) {
            for (int j = 0; j < currentMap.getRows(); j++) {
                ArrayList<Effect> tileEffects = currentMap.getTile(new GridIndex(i, j)).getEffects();
                if (tileEffects != null) {
                    this.drawable.addAll(tileEffects);
                }
            }
        }
        this.drawable.addAll(this.towerList);
        this.drawable.addAll(this.troopList);
        drawable.addAll(projectileList);

        for(Observer observer : observers){
            observer.update(this.drawable,this.credits,this.points);
        }
    }

    /**
     * Handeling mouse clicks on GameCanvas
     * Healing effect/Rotate crossroads
     * @param mouseX X-coordinate of click
     * @param mouseY Y-coordinate of click
     */
    public void clicked(int mouseX, int mouseY){
        if(this.credits >= CHANGEDIRECTION_COST) {
            Tile tile = getTileFromPosition(mouseX, mouseY);
            TileRotator rotator = new TileRotator(currentMap);

            if(tile.isWalkable()) {
                if (holdingHealEffect) {
                    HealingEffect healingEffect = new HealingEffect(EFFECT_TIME,
                            (int) tile.getFrame().getCenterX(),
                            (int) tile.getFrame().getCenterY(),maxImageSize);
                    tile.addEffect(healingEffect);
                    holdingHealEffect = false;
                    this.credits -= HEALINGEFFECT_COST;
                    //aPlayer.healSound();
                } else if (rotator.RotateToNextValidDirection(tile)) {
                    this.credits -= CHANGEDIRECTION_COST;
                }
            }
        }
    }

    /**
     * Drop teleport troops portal at troops position.
     */
    public void dropPortal() {
        if ((teleportTroop != null) && (teleportTroop.getHP() > 0)
                && (!teleportTroop.hasReachedGoal())
                && (teleportTroop.readyToDropPortal())) {
            int teleportTroopXPos = (int) teleportTroop.getXPos();
            int teleportTroopYPos = (int) teleportTroop.getYPos();
            int teleportStartXPos = teleportTroop.getStartXPosition();
            int teleportStartYPos = teleportTroop.getStartYPosition();
            Tile entranceTile = getTileFromPosition(teleportStartXPos, teleportStartYPos);
            Tile exitTile = getTileFromPosition(teleportTroopXPos, teleportTroopYPos);
            TeleportEntrance teleportEntrance = teleportTroop.dropPortal((int) exitTile.getFrame().getCenterX(),
                    (int) exitTile.getFrame().getCenterY());
            entranceTile.addEffect(teleportEntrance);
        }
    }

    /**
     * Toggle to set healing effect to be held, ready to be placed
     */
    public void dropHealing() {
        holdingHealEffect = true;
    }


    /**
     * Return tile at a coordinate
     * @param xPosition X-Coordinate
     * @param yPosition Y-Coordinate
     * @return Tile at coordinate
     */
    public Tile getTileFromPosition(int xPosition, int yPosition) {
        int x = xPosition / (canvasDimension.width / currentMap.getCols());
        int y = yPosition / (canvasDimension.height / currentMap.getRows());
        return currentMap.getTile(new GridIndex(x, y));
    }


    /**
     * Change current map
     * @param map Map to be set
     */
    public void setMap(Map map){
        currentMap = map;
        maxImageSize = Math.min((int) (canvasDimension.getWidth() / map.getCols()),
                (int) (canvasDimension.getHeight() / map.getRows()));
        restart();
    }

}
