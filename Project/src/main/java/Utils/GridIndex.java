package Utils;

/**
 * Grid index is used to store indexes in a 2D-grid.
 */
public class GridIndex {
    private int xPosition;
    private int yPosition;

    /**
     * @param xPosition to store
     * @param yPosition to store
     */
    public GridIndex(int xPosition, int yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    /**
     * @return the X-position
     */
    public int getX() {
        return xPosition;
    }

    /**
     *
     * @param x to be set
     */
    public void setX(int x) {
        xPosition = x;
    }

    /**
     *
     * @param y to be set
     */
    public void setY(int y) {
        yPosition = y;
    }

    /**
     * @return the Y-position
     */
    public int getY() {
        return yPosition;
    }

    @Override
    public String toString() {
        return "GridIndex{" +
                "x=" + xPosition +
                ", y=" + yPosition +
                '}';
    }
}
