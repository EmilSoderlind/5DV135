package Model.Units;

import Model.Effect;
import Utils.DIRECTION;
import Utils.Vector2D;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Abstract superclass to spawnable troops in the game
 * Implements Drawable to be drawable in the views
 * Implements Affectable to be able to be affected by effects on tiles
 */
public abstract class Troop implements Drawable, Affectable {

    protected HashMap<Class<? extends Effect>, Effect> effectList;

    protected DIRECTION currentDirection;
    protected double maxHP;
    protected double currentHP; protected double defaultSpeed;
    protected double currentSpeed;
    protected Vector2D position;
    protected Vector2D acceleration;
    protected Vector2D velocity;
    private boolean stillTargetable;
    private boolean hasReachedGoal;
    protected Integer maxImageSize;


    /**
     * Constructor for troop. Sets important parameters in the object.
     * @param xPos X-position in the game
     * @param yPos Y-position in the game
     * @param maxImageSize Max size of game object when rendered
     */
    public Troop(int xPos, int yPos, Integer maxImageSize){
        position = new Vector2D(xPos, yPos);
        acceleration = new Vector2D(0, 0);
        velocity = new Vector2D(0, 0);
        effectList = new HashMap<>();
        stillTargetable = true;
        hasReachedGoal = false;
        this.maxImageSize = maxImageSize;
    }

    /**
     * Used when the game object is drawn in the GUI:s JPanel
     * @param graphic graphic-object from GUI:s panel
     */
    public abstract void draw(Graphics graphic);


    /**
     * Perform a move in the game
     */
    public abstract void move();


    /**
     * Getter for X-Position
     * @return X-position
     */
    public double getXPos(){
        return position.x;
    }

    /**
     * Getter for Y-Position
     * @return Y-position
     */
    public double getYPos(){
        return position.y;
    }


    /**
     * Setter for troop:s current direction
     * @param direction Direction to be set
     */
    public void setDirection(DIRECTION direction) {
        currentDirection = direction;
    }


    /**
     * Adds a given effect to the troop
     * @param effect Effect to be applied
     */
    public void addEffect(Effect effect) {
        effectList.put(effect.getClass(), effect);
    }


    /**
     * Apply current effects to troop
     */
    public void statusUpdate() {
        currentSpeed = defaultSpeed;
        if (effectList.size() > 0) {
            for (Iterator<HashMap.Entry<Class<? extends Effect>, Effect>> iterable = effectList.entrySet().iterator(); iterable.hasNext(); ) {
                Effect effect = iterable.next().getValue();
                if (effect.getDuration() <= 0) {
                    iterable.remove();
                } else {
                    effect.affect(this);
                }
            }
        }
    }

    /**
     * Getter for troops HP
     * @return Current HP
     */
    public double getHP() {
        return currentHP;
    }


    /**
     * Getter for troops current speed
     * @return Current speed
     */
    public double getSpeed() {
        return currentSpeed;
    }


    /**
     * Getter for number of effects troop have
     * @return number of effects
     */
    public int getNumberOfEffects() {
        return effectList.size();
    }


    /**
     * Reduce HP of troop by effect
     * @param effect applied effect
     */
    public void affectHP(double effect) {
        if ((currentHP + effect) < 0) {
            currentHP = 0;
        } else if ((currentHP + effect) > maxHP) {
            currentHP = maxHP;
        } else {
            currentHP = currentHP + effect;
        }
    }


    /**
     * Draw health-bar of troop
     * @param g graphic-object
     */
    protected void drawHealthBar(Graphics g){
        if (maxImageSize != null) {
            g.setColor(Color.red);
            g.fillRect((int) (this.position.x-(int) (0.5 * maxImageSize) / 2),(int) (this.position.y-(int) (0.5 * maxImageSize)),(int) (0.5 * maxImageSize),(int) (0.1 * maxImageSize));
            Double HPprecentage = this.currentHP/this.maxHP;
            g.setColor(Color.green);
            g.fillRect((int) (this.position.x-(int) (0.5 * maxImageSize) / 2),(int) (this.position.y-(int) (0.5 * maxImageSize)), (int) (0.5 * maxImageSize * HPprecentage),(int) (0.1 * maxImageSize));
        }

    }

    /**
     * Change speed by effect
     * @param effect effect to modify speed
     */
    public void affectSpeed(double effect) {
        currentSpeed = currentSpeed * effect;
    }

    /**
     * Change position of troop
     * @param xPosition X-position to be set
     * @param yPosition Y-position to be set
     */
    public void affectPosition(int xPosition, int yPosition) {
        position.x = xPosition;
        position.y = yPosition;
    }


    /**
     * Return true if troop is still alive
     * @return alive/dead
     */
    public boolean stillTargetable(){
        if(currentHP <= 0){
            return false;
        }else return stillTargetable;

    }


    /**
     * Set targatable status of troop
     * @param b targatable/not-targatable
     */
    public void setTargetableStatus(boolean b){
        stillTargetable = b;
    }


    /**
     * Set boolean when troop have arrived to goal
     * @param b true/false
     */
    public void setHasReachedGoal(boolean b) {
        hasReachedGoal = b;
    }


    /**
     * Return true if troop have arrived to goal
     * @return true/false
     */
    public boolean hasReachedGoal() {
      return hasReachedGoal;
    }
}
