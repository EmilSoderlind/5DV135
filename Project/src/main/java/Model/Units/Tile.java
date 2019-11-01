package Model.Units;

import Model.Effect;
import Utils.DIRECTION;
import Utils.GridIndex;
import Utils.TILESTATUS;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract superclass to Tiles in the game
 * Implements Drawable to be drawable in the views
 */
public abstract class Tile implements Drawable {

    protected DIRECTION direction;
    protected HashMap<Class<? extends Effect>, Effect> effectList;
    private Integer distanceToGoal;
    private GridIndex index;
    private boolean hasTower;

    private Rectangle frame;
    private boolean walkable = false;
    private TILESTATUS status;

    private Tower tower;

    public Tile(Rectangle frame, TILESTATUS status) {
        setup(frame);
        this.status = status;
    }

    /**
     * Initializer for Tile
     * @param frame frame to be set
     */
    private void setup(Rectangle frame){
        index = new GridIndex(frame.x, frame.y);
        this.frame = frame;
        frame.x = frame.x * frame.width;
        frame.y = frame.y * frame.height;
        distanceToGoal = null;
        hasTower = false;
        effectList = new HashMap<>();
    }

    /**
     * Set tile to have crossroad on it. Be rotatable.
     * @param rotatable Rotatable or non-rotatable
     */
    public void setRotatable(boolean rotatable) {
        this.rotatable = rotatable;
    }

    /**
     * Set tile to be walkable. Let troops be able to walk on them.
     * @param walkable walkable/non-walkable
     */
    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    protected boolean rotatable = false;

    /**
     * Parameter if tile is walkable or not
     * @return walkable/non-walkable
     */
    public boolean isWalkable() { //
        return status == TILESTATUS.WALKABLE
                || status == TILESTATUS.GOAL
                || status == TILESTATUS.START
                || status == TILESTATUS.ROTATABLE;
    }

    /**
     * Getter for if tile is goal
     * @return goal or not
     */
    public boolean isGoal() {
        return status == TILESTATUS.GOAL;
    }

    /**
     * Getter for if tile is rotatable
     * @return Rotatable or not
     */
    public boolean isRotatable() {
        return rotatable;
    }


    /**
     * Getter for X-Position of tile
     * @return X-Position
     */
    public int getXPosition() {
        return (int) frame.getX();
    }


    /**
     * Getter for Y-Position of tile
     * @return Y-Position
     */
    public int getYPosition() {
        return (int) frame.getY();
    }


    /**
     * Getter for X-Position in grid of tile
     * @return X-Position
     */
    public int getXGridPosition() {
        return index.getX();
    }

    /**
     * Getter for Y-Position in grid of tile
     * @return Y-Position
     */
    public int getYGridPosition() {
        return index.getY();
    }


    /**
     * Return width of tile
     * @return width
     */
    public int getWidth() {
        return frame.width;
    }


    /**
     * Return height of tile
     * @return height
     */
    public int getHeight() {
        return frame.height;
    }

    /**
     * Return true if tile have more than 0 effects
     * @return have effect
     */
    public boolean hasEffect() {
        return (effectList.size() != 0);
    }

    /**
     * Add effect to tiles effect-list
     * @param effect Effet to add
     */
    public void addEffect(Effect effect) {
        effectList.put(effect.getClass(),effect);
    }

    @Override
    public void draw(Graphics g) {
        RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        ((Graphics2D) g).setRenderingHints(renderingHints);
        g.fillRect(frame.x, frame.y, frame.width, frame.height);
        g.setColor(Color.DARK_GRAY);
        g.drawRect(frame.x, frame.y, frame.width, frame.height);
    }

    /**
     * Set tower to have a tile
     */
    public void addTower() {
        hasTower = true;
    }

    /**
     * Return if tile have a tower
     * @return True/False
     */
    public boolean hasTower() {
        return hasTower;
    }


    /**
     * Remove tower from tile
     */
    public void removeTower() {
        hasTower = false;
    }

    /**
     * Set direction of Tile
     * @param direction direction to be set
     */
    public void setDirection(DIRECTION direction) {
        this.direction = direction;
    }


    /**
     * Get direction of Tile
     * @return direction
     */
    public DIRECTION getDirection() {
        return direction;
    }


    /**
     * Getter for Tile:s frame
     * @return Tiles frame
     */
    public Rectangle getFrame() {
        return this.frame;
    }

    /**
     * Getter for tiles status
     * @return status of tile
     */
    public TILESTATUS getStatus() {
        return status;
    }

    /**
     * Return boolean if Tile:s status is goal
     * @return Is goal - True/False
     */
    public boolean isStart() {
        return (status == TILESTATUS.START);
    }

    /**
     * Getter for number of effects on tile
     * @return number of effects
     */
    public int getNumberOfEffects() {
        return effectList.size();
    }

    /**
     * Returns effects on tile
     * @return Effect-list
     */
    public ArrayList<Effect> getEffects() {
        if (getNumberOfEffects() > 0) {
            ArrayList<Effect> listOfEffects = new ArrayList<>();
            for (Map.Entry<Class<? extends Effect>, Effect> classEffectEntry : effectList.entrySet()) {
                Effect effect = classEffectEntry.getValue();
                listOfEffects.add(effect);
            }
            return listOfEffects;
        } else {
            return null;
        }
    }


    /**
     * Set frame of Tile
     * @param frame frame to set in Tile
     */
    public void setFrame(Rectangle frame){
        setup(frame);
    }

    /**
     * Returns a copy of the Tile
     * @return Tile-copy
     */
    public abstract Tile copy();

    /**
     * Flush effect-list on Tile
     */
    public void flushEffects() {
        effectList = new HashMap<>();
    }
}
