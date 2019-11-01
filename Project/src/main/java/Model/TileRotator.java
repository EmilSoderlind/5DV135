package Model;

import Model.Units.Tile;
import Utils.DIRECTION;
import Utils.GridIndex;
import Utils.TILESTATUS;

import static Utils.DIRECTION.*;

/**
 * TileRotator is used to find crossings along
 * the path and make them rotatable.
 */
public class TileRotator {
    private Map map;

    /**
     * Constructs the tileRotator with the map
     * @param map to set any crossings
     */
    public TileRotator(Map map) {
        this.map = map;
    }

    /**
     * Rotate a tile to change the direction
     *
     * @param tile to rotate
     * @return true if it rotated, else false
     */
    public boolean RotateToNextValidDirection(Tile tile) {
        if (!tile.isRotatable()) {
            return false;
        }
        DIRECTION currentDirection = tile.getDirection();
        GridIndex currentGridIndex = new GridIndex(tile.getXGridPosition(),tile.getYGridPosition());
        if (currentDirection == NORTH) {
             DIRECTION nextDirection = nextValidRotationFromNorth(currentGridIndex);
             return rotateDirection(tile,nextDirection);
        }
        else if (currentDirection == EAST) {
            DIRECTION nextDirection = nextValidRotationFromEast(currentGridIndex);
            return rotateDirection(tile,nextDirection);
        }
        else if (currentDirection == SOUTH) {
            DIRECTION nextDirection = nextValidRotationFromSouth(currentGridIndex);
            return rotateDirection(tile,nextDirection);
        }
        else if (currentDirection == WEST) {
            DIRECTION nextDirection = nextValidRotationFromWest(currentGridIndex);
            return rotateDirection(tile,nextDirection);
        }
        return false;
    }

    private boolean rotateDirection(Tile tile, DIRECTION nextDirection) {
        if (nextDirection != null) {
            tile.setDirection(nextDirection);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Set rotation tiles in a map
     */
    public void setRotationTiles(){
        for (Tile[] tiles : map.getTileGrid()) {
            for (Tile tile : tiles) {
                if (tile.getStatus() == TILESTATUS.WALKABLE){
                    if (tile.isRotatable()){
                        tile.setRotatable(canRotate(tile));
                    }
                }
            }
        }
    }

    private boolean canRotate(Tile tile) {
        DIRECTION currentDirection = tile.getDirection();
        GridIndex currentGridIndex = new GridIndex(tile.getXGridPosition(),tile.getYGridPosition());
        if (currentDirection == NORTH) {
            DIRECTION nextDirection = nextValidRotationFromNorth(currentGridIndex);
            return nextDirection != null;
        }
        else if (currentDirection == EAST) {
            DIRECTION nextDirection = nextValidRotationFromEast(currentGridIndex);
            return nextDirection != null;
        }
        else if (currentDirection == SOUTH) {
            DIRECTION nextDirection = nextValidRotationFromSouth(currentGridIndex);
            return nextDirection != null;
        }
        else if (currentDirection == WEST) {
            DIRECTION nextDirection = nextValidRotationFromWest(currentGridIndex);
            return nextDirection != null;
        }
        return false;
    }

    private DIRECTION nextValidRotationFromNorth(GridIndex gridIndex) {
       DIRECTION nextDirection;
       nextDirection = checkEastNeighbour(gridIndex);
       if (nextDirection != null) {
            return nextDirection;
       }
       nextDirection = checkSouthNeighbour(gridIndex);
       if (nextDirection != null) {
            return nextDirection;
       }
       nextDirection = checkWestNeighbour(gridIndex);
       if (nextDirection != null) {
            return nextDirection;
       }
       return null;
    }

    private DIRECTION nextValidRotationFromEast(GridIndex gridIndex) {
       DIRECTION nextDirection;
       nextDirection = checkSouthNeighbour(gridIndex);
       if (nextDirection != null) {
            return nextDirection;
       }
       nextDirection = checkWestNeighbour(gridIndex);
       if (nextDirection != null) {
            return nextDirection;
       }
       nextDirection = checkNorthNeighbour(gridIndex);
       if (nextDirection != null) {
            return nextDirection;
       }
       return null;
    }

    private DIRECTION nextValidRotationFromSouth(GridIndex gridIndex) {
       DIRECTION nextDirection;
       nextDirection = checkWestNeighbour(gridIndex);
       if (nextDirection != null) {
            return nextDirection;
       }
       nextDirection = checkNorthNeighbour(gridIndex);
       if (nextDirection != null) {
            return nextDirection;
       }
       nextDirection = checkEastNeighbour(gridIndex);
       if (nextDirection != null) {
            return nextDirection;
       }
       return null;
    }

    private DIRECTION nextValidRotationFromWest(GridIndex gridIndex) {
       DIRECTION nextDirection;
       nextDirection = checkNorthNeighbour(gridIndex);
       if (nextDirection != null) {
           return nextDirection;
       }
        nextDirection = checkEastNeighbour(gridIndex);
        if (nextDirection != null) {
            return nextDirection;
       }
       nextDirection = checkSouthNeighbour(gridIndex);
       if (nextDirection != null) {
            return nextDirection;
       }
       return null;
    }

    private DIRECTION checkNorthNeighbour(GridIndex gridIndex) {
        GridIndex northIndex = new GridIndex(gridIndex.getX(), gridIndex.getY() - 1);
        if ((isWithinBounds(northIndex.getX(),northIndex.getY()))
                    && (map.getTile(northIndex).isWalkable()
                    && (!isOppositeDirection(NORTH,map.getTile(northIndex).getDirection())))) {
            return NORTH;
        }
        return null;
    }

    private DIRECTION checkEastNeighbour(GridIndex gridIndex) {
        GridIndex eastIndex = new GridIndex(gridIndex.getX() + 1, gridIndex.getY());
        if ((isWithinBounds(eastIndex.getX(), eastIndex.getY()))
                && (map.getTile(eastIndex).isWalkable())
                && (!isOppositeDirection(EAST,map.getTile(eastIndex).getDirection()))) {

            return EAST;
        }
        return null;
    }

    private DIRECTION checkSouthNeighbour(GridIndex gridIndex) {
        GridIndex southIndex = new GridIndex(gridIndex.getX(), gridIndex.getY() + 1);
        if ((isWithinBounds(southIndex.getX(), southIndex.getY()))
                && (map.getTile(southIndex).isWalkable())
                && (!isOppositeDirection(SOUTH,map.getTile(southIndex).getDirection()))) {

            return SOUTH;
        }
        return null;
    }

    private DIRECTION checkWestNeighbour(GridIndex gridIndex) {
        GridIndex westIndex = new GridIndex(gridIndex.getX() - 1, gridIndex.getY());
        if ((isWithinBounds(westIndex.getX(), westIndex.getY()))
                && (map.getTile(westIndex).isWalkable())
                && (!isOppositeDirection(WEST,map.getTile(westIndex).getDirection()))) {

            return WEST;
        }
        return null;
    }

    private boolean isOppositeDirection(DIRECTION direction1, DIRECTION direction2) {
        if (direction1 == NORTH && direction2 == SOUTH) { return true; }
        else if (direction1 == EAST && direction2 == WEST) { return true; }
        else if (direction1 == SOUTH && direction2 == NORTH) { return true; }
        else return (direction1 == WEST && direction2 == EAST);
    }

    private boolean isWithinBounds(int currentX, int currentY) {
        return ((currentX >= 0) && (currentX < map.getCols())
                    && (currentY >= 0) && (currentY < map.getRows()));
    }
}
