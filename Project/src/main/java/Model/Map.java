package Model;

import Model.Units.Drawable;
import Model.Units.Tile;
import Utils.GridIndex;
import Utils.TILESTATUS;

import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * The Map class contains all the tiles and other
 * information such as name, columns, rows and
 * a thumbnail of itself.
 */
public class Map implements Drawable {
    private Tile[][] map;
    private String name;
    private int rows, cols;
    private boolean invalid = false;
    private Image thumbnail;
    private Tile goalTile;
    private Tile startTile;

    /**
     * @return goal tile of the map
     */
    public Tile getGoalTile() {
        return goalTile;
    }

    /**
     * @return start tile of the map
     */
    public Tile getStartTile() {
        return startTile;
    }

    /**
     * @param rows nr of rows in the map
     * @param cols nr of cols in the map
     * @param name name of the map
     */
    public Map(int rows, int cols, String name) {
        this.rows = rows;
        this.cols = cols;
        this.name = name;
        map = new Tile[rows][cols];
    }

    /**
     * @return nr of rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * @return nr of cols
     */
    public int getCols() {
        return cols;
    }

    /**
     * Validates the map to see if it has all the required parts.
     *
     * @param callback to store any exceptions
     * @return true if valid, else false
     */
    public boolean validateMap(Consumer<Exception> callback) {
        int nrOfGoals = 0, nrOfStarts = 0, nrOfWalkable = 0, nrOfBuildable = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (map[r][c] != null && map[r][c].getStatus() != null) {
                    TILESTATUS status = map[r][c].getStatus();
                    switch (status) {
                        case GOAL:
                            nrOfGoals++;
                            break;
                        case START:
                            nrOfStarts++;
                            break;
                        case WALKABLE:
                            nrOfWalkable++;
                            break;
                        case NONWALKABLE:
                            nrOfBuildable++;
                            break;
                    }
                } else {
                    callback.accept(new IOException(
                            String.format(" The map [ %s ] needs %d * %d tiles " +
                                            "as specified in the XML-file.",
                                    name, rows, cols)));
                    return false;
                }
            }
        }

        boolean correctDimensions = rows * cols
                == (nrOfWalkable + nrOfGoals + nrOfBuildable + nrOfStarts);


        if (correctDimensions && nrOfGoals >= 1 && nrOfStarts == 1 && !invalid) {
            return true;
        } else {
            StringBuilder b = new StringBuilder("The map [ %s ] is invalid.");
            if (nrOfGoals == 0) {
                b.append(" At least one tile needs to have status 'GOAL'.");
            }
            if (nrOfStarts != 1) {
                String e = nrOfStarts == 0 ?
                        " Map requires one tile with status 'START'" :
                        String.format(" Map has to many tiles with " +
                                "status 'START' (%d).", nrOfStarts);
                b.append(e);
            }
            if (!correctDimensions) {
                b.append(String.format(" The map needs %d * %d tiles " +
                        "as specified in the XML-file.", rows, cols));
            }
            callback.accept(new ParserConfigurationException(
                    String.format(b.toString(), name)));
        }

        return false;
    }

    /**
     * Sets a tile to the map
     *
     * @param tile              to set
     * @param exceptionCallback to store any exceptions
     */
    public void setTile(Tile tile, Consumer<Exception> exceptionCallback) {
        try {
            if (tile.getStatus() == TILESTATUS.START) {
                startTile = tile;
            } else if (tile.getStatus() == TILESTATUS.GOAL) {
                goalTile = tile;
            }
            map[tile.getXGridPosition()][tile.getYGridPosition()] = tile;
        } catch (IndexOutOfBoundsException e) {
            exceptionCallback.accept(new MapValidationException(
                    String.format(" The map [ %s ] needs %d * %d tiles " +
                                    "as specified in the XML-file.",
                            name, rows, cols), e));
        }
    }

    /**
     * Returns a tile at a given location
     *
     * @param location where the tile is
     * @return tile at location
     */
    public Tile getTile(GridIndex location) {
        return map[location.getX()][location.getY()];
    }

    /**
     * Sets the name of the map
     *
     * @param name of map
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the map
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(name + "\n");
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                Tile tile = map[j][i];
                Character c = '0';
                switch (tile.getStatus()) {
                    case GOAL:
                        c = 'G';
                        break;
                    case START:
                        c = 'S';
                        break;
                    case WALKABLE:
                        c = tile.getDirection() == null ? '*' :
                                tile.getDirection().toString().charAt(0);
                        break;
                    case NONWALKABLE:
                        c = 'X';
                        break;
                }
                stringBuilder.append(String.format("%s\t", c));
            }
            stringBuilder.append(String.format("\n"));
        }
        return stringBuilder.toString();
    }

    /**
     * Get all tiles
     *
     * @return tiles of the map
     */
    public Tile[][] getTileGrid() {
        return this.map;
    }

    /**
     * Generates a thumbnail image of the map
     *
     * @return
     */
    public Image generateImage() {
        if (thumbnail == null) {
            BufferedImage newImage = new BufferedImage(
                    rows * startTile.getWidth(),
                    rows * startTile.getWidth(),
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = newImage.createGraphics();
            draw(g);
            thumbnail = newImage;
        }
        return thumbnail.getScaledInstance(
                200,
                200,
                Image.SCALE_SMOOTH);
    }

    @Override
    public void draw(Graphics g) {
        for (Tile[] tiles : map) {
            for (Tile tile : tiles) {
                tile.draw(g);
            }
        }
    }

    /**
     * Removes any effects from the tiles
     */
    public void flushTiles() {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                map[i][j].flushEffects();
            }
        }
    }
}
