package Utils;

/**
 * Different states a tile can have:
 * Walkable, Non-walkable, Start, Goal and Rotatable
 */
public enum TILESTATUS {WALKABLE,NONWALKABLE, START, GOAL, ROTATABLE;

    @Override
    public String toString() {
        return this.name();
    }

}
