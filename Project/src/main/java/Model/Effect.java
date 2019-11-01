package Model;

import Model.Units.Affectable;
import Model.Units.Drawable;
import Utils.EFFECTSTATUS;

/**
 * This class is the super-class of all effects that
 * can be implemented. Effects can be created at a position
 * with a duration. Effects can be activated, changing their
 * status. Effects can affect Affectable classes. Subclasses
 * are responsible of implementing affect() methods.
 * @see Affectable
 */
public abstract class Effect implements Drawable {

    protected double xPosition;
    protected double yPosition;

    protected EFFECTSTATUS effectStatus;
    protected int duration;
    protected Integer maxImageSize;

    public Effect(int dormantDuration, double xPosition, double yPosition,
                  Integer maxImageSize) {
        effectStatus = EFFECTSTATUS.DORMANT;
        duration = dormantDuration;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.maxImageSize = maxImageSize;
    }

    /**
     * Sets status to active
     * @param activeDuration duration Effect should be active.
     */
    public void activate(int activeDuration) {
        effectStatus = EFFECTSTATUS.ACTIVE;
        duration = activeDuration;
    }

    /**
     * Affects Affectable units, must be implemented by subclasses
     * @param unitToBeAffected the unit to be effected by effect
     */
    public abstract void affect(Affectable unitToBeAffected);

    /**
     * @return current duration left.
     */
    public double getDuration() {
        return duration;
    }
}
