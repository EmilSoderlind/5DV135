package Model;

/**
 * PathSetter is used to set a path
 * on a Map-object.
 * @see Map
 */
public class PathSetter {
    private PathSetterStrategy strategy;

    /**
     * Empty constructor
     */
    public PathSetter(){}

    /**
     * Creates a PathSetter with a Strategy
     * @param strategy to use when setting the path
     */
    public PathSetter(PathSetterStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     *
     * Sets the path and directions on a Map with a given strategy.
     *
     * @param map to set path on
     * @return true if the path was set successfully, else false
     */
    public boolean setPaths(Map map) {
        if (strategy != null) {
            return strategy.setPaths(map);
        } else return false;
    }

    /**
     * Sets a strategy to the PathSetter
     * @param strategy to set
     */
    public void setStrategy(PathSetterStrategy strategy) {
       this.strategy = strategy;
    }
}
