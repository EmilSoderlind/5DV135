package Model;

import Utils.DIRECTION;
import Utils.GridIndex;

import java.security.SecureRandom;
import java.util.ArrayList;

import static Utils.DIRECTION.*;


/**
 * This PathSetterStrategy sets random (but valid) directions on all walkable tiles
 * NOTE: Just to show possibilities available by using Strategy design pattern.
 */
public class RandomPathSetterStrategy extends PathSetterStrategy{

    private Map map;
    private int gridWidth;
    private int gridHeight;

    public RandomPathSetterStrategy() {
    }

    @Override
    public boolean setPaths(Map map) {
        this.map = map;
        gridWidth = map.getCols();
        gridHeight = map.getRows();
        for (int i = 0; i < gridWidth; i++) {
            for (int j = 0; j < gridHeight; j++) {
                GridIndex currentIndex = new GridIndex(i, j);
                if (map.getTile(currentIndex).isWalkable()) {
                    DIRECTION direction = generateRandomDirection(currentIndex);
                    if (direction != null) {
                        map.getTile(currentIndex).setDirection(direction);
                    }
                }
            }
        }
        return true;
    }

    private DIRECTION checkNorthNeighbour(GridIndex gridIndex) {
        GridIndex northGridIndex = new GridIndex(gridIndex.getX(), gridIndex.getY() - 1);
        if ((isWithinBounds(northGridIndex.getX(),northGridIndex.getY()))
                    && (map.getTile(northGridIndex).isWalkable())) {
            return NORTH;
        }
        return null;
    }

    private DIRECTION checkEastNeighbour(GridIndex gridIndex) {
        GridIndex eastGridIndex = new GridIndex(gridIndex.getX() + 1, gridIndex.getY());
        if ((isWithinBounds(eastGridIndex.getX(),eastGridIndex.getY()))
                && (map.getTile(gridIndex).isWalkable())) {

            return EAST;
        }
        return null;
    }

    private DIRECTION checkSouthNeighbour(GridIndex gridIndex) {
        GridIndex southGridIndex = new GridIndex(gridIndex.getX(), gridIndex.getY() + 1);
        if ((isWithinBounds(southGridIndex.getX(),southGridIndex.getY()))
                && (map.getTile(southGridIndex).isWalkable())) {

            return SOUTH;
        }
        return null;
    }

    private DIRECTION checkWestNeighbour(GridIndex gridIndex) {
        GridIndex eastGridIndex = new GridIndex(gridIndex.getX() - 1, gridIndex.getY());
        if ((isWithinBounds(eastGridIndex.getX(),eastGridIndex.getY()))
                && (map.getTile(eastGridIndex).isWalkable())) {

            return WEST;
        }
        return null;
    }

    private boolean isWithinBounds(int currentX, int currentY) {
        return ((currentX >= 0) && (currentX < gridWidth)
                    && (currentY >= 0) && (currentY < gridHeight));
    }

    private DIRECTION generateRandomDirection(GridIndex gridIndex) {
        ArrayList<DIRECTION> validDirections = new ArrayList<>();
        DIRECTION north;
        DIRECTION east;
        DIRECTION south;
        DIRECTION west;

        // Generate valid directions
        if ((north = checkNorthNeighbour(gridIndex)) != null) {
            validDirections.add(north);
        }
         if ((east = checkEastNeighbour(gridIndex)) != null) {
            validDirections.add(east);
        }
         if ((south = checkSouthNeighbour(gridIndex)) != null) {
            validDirections.add(south);
        }
         if ((west = checkWestNeighbour(gridIndex)) != null) {
            validDirections.add(west);
        }

        SecureRandom random = new SecureRandom();
        int randomInteger = random.nextInt(validDirections.size());
        return validDirections.get(randomInteger);
    }
}
