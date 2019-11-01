package Utils;

/**
 *  Direction is used to represent directions.
 */
public enum DIRECTION {
    NORTH,EAST,SOUTH,WEST;

    @Override
    public String toString() {
        String dir = "";
        switch (this){
            case EAST:
                dir = "->";
                break;
            case WEST:
                dir = "<-";
                break;
            case NORTH:
                dir = "^";
                break;
            case SOUTH:
                dir = "v";
                break;
        }
        return dir;
    }

    /**
     *
     * @return a Vector2D representing the direction
     */
    public Vector2D getDirectionVel(){
        Vector2D point = new Vector2D(0,0);
        switch (this){
            case EAST:
                point.x = 1;
                point.y = 0;
                break;
            case WEST:
                point.x = -1;
                point.y = 0;
                break;
            case NORTH:
                point.x = 0;
                point.y = -1;
                break;
            case SOUTH:
                point.x = 0;
                point.y = 1;
                break;
        }
        return point;
    }
}
