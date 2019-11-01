package Model;

import Model.Units.Tile;
import Utils.GridIndex;

import java.util.ArrayList;

import static Utils.DIRECTION.*;

/**
 * This Pathsetter is responsible for setting a path from all
 * walkable (and from goal tile reachable) to the maps goal tile.
 * This is done by breadth-first traversing of the map.
 */
public class ManyToOnePathSetterStrategy extends PathSetterStrategy {

    Map map;

    private int gridWidth;
    private int gridHeight;
    private ArrayList<GridIndex> listOfProcessed = new ArrayList<>();
    private ArrayList<GridIndex> searchQueue = new ArrayList<>();

    private int currentX;
    private int currentY;
    private int nrOfNeigbours = 0;
    private boolean pathFromSToGExists;

    public ManyToOnePathSetterStrategy() {
        pathFromSToGExists = false;
    }

    /**
     * Sets the shortest path (direction) from all reachable tiles to the goal.
     * @return  returns true if path from start to goal exists, false otherwise.
     */
    public boolean setPaths(Map map) {
        this.map = map;
        gridWidth = map.getCols();
        gridHeight = map.getRows();
        Tile goalTile = map.getGoalTile();
        GridIndex goalIndex = new GridIndex(goalTile.getXGridPosition(),
                goalTile.getYGridPosition());
        searchQueue.add(goalIndex);
        listOfProcessed.add(goalIndex);
        while (!searchQueue.isEmpty()) {
            nrOfNeigbours = 0;
            GridIndex currentTileGridIndex = searchQueue.remove(0);
            currentX = currentTileGridIndex.getX();
            currentY = currentTileGridIndex.getY();
            checkNorthNeighbour(currentTileGridIndex);
            checkEastNeighbour(currentTileGridIndex);
            checkSouthNeighbour(currentTileGridIndex);
            checkWestNeighbour(currentTileGridIndex);

            if (nrOfNeigbours > 2){
                map.getTile(currentTileGridIndex).setRotatable(true);
            }
        }
        return pathFromSToGExists;
    }

    /**
     *  Checks if North neighbour is valid a non visited, walkable tile within
     *  map bounds. If true, sets direction pointing to current tile.
     * @param currentTileGridIndex  index of current tile.
     */
    private void checkNorthNeighbour(GridIndex currentTileGridIndex) {
        currentX =  currentTileGridIndex.getX();
        currentY =  currentTileGridIndex.getY();
        GridIndex northTileGridIndex = new GridIndex(currentX,
                currentY - 1);
        if (isWithinBounds(northTileGridIndex)
                    && map.getTile(northTileGridIndex).isWalkable()
                    && hasNotBeenProcessed(northTileGridIndex)) {
            searchQueue.add(northTileGridIndex);
            listOfProcessed.add(northTileGridIndex);
            map.getTile(northTileGridIndex).setDirection(SOUTH);
            if (map.getTile(northTileGridIndex).isGoal()) {
               pathFromSToGExists = true;
            }
        }
        if (isWalkableNeigbour(northTileGridIndex)){
            nrOfNeigbours++;
        }
    }

    /**
     *  Checks if East neighbour is valid a non visited, walkable tile within
     *  map bounds. If true, sets direction pointing to current tile.
     * @param currentTileGridIndex  index of current tile.
     */
    private void checkEastNeighbour(GridIndex currentTileGridIndex) {
        currentX =  currentTileGridIndex.getX();
        currentY =  currentTileGridIndex.getY();
        GridIndex eastTileGridIndex = new GridIndex(
                currentX + 1, currentY);
        if (isWithinBounds(eastTileGridIndex)
                    && map.getTile(eastTileGridIndex).isWalkable()
                    && hasNotBeenProcessed(eastTileGridIndex)) {
            searchQueue.add(eastTileGridIndex);
            listOfProcessed.add(eastTileGridIndex);
            map.getTile(eastTileGridIndex).setDirection(WEST);
            if (map.getTile(eastTileGridIndex).isGoal()) {
               pathFromSToGExists = true;
            }
        }
        if (isWalkableNeigbour(eastTileGridIndex)){
            nrOfNeigbours++;
        }
    }

     /**
     *  Checks if South neighbour is valid a non visited, walkable tile within
     *  map bounds. If true, sets direction pointing to current tile.
     * @param currentTileGridIndex  index of current tile.
     */
    private void checkSouthNeighbour(GridIndex currentTileGridIndex) {
        currentX =  currentTileGridIndex.getX();
        currentY =  currentTileGridIndex.getY();
        GridIndex southTileGridIndex = new GridIndex(currentX,
                currentY + 1);
        if (isWithinBounds(southTileGridIndex)
                && map.getTile(southTileGridIndex).isWalkable()
                && hasNotBeenProcessed(southTileGridIndex)) {
            searchQueue.add(southTileGridIndex);
            listOfProcessed.add(southTileGridIndex);
            map.getTile(southTileGridIndex).setDirection(NORTH);
            if (map.getTile(southTileGridIndex).isGoal()) {
               pathFromSToGExists = true;
            }
        }
        if (isWalkableNeigbour(southTileGridIndex)){
            nrOfNeigbours++;
        }
    }

    /**
     *  Checks if West neighbour is valid a non visited, walkable tile within
     *  map bounds. If true, sets direction pointing to current tile.
     * @param currentTileGridIndex  index of current tile.
     */
    private void checkWestNeighbour(GridIndex currentTileGridIndex) {
        currentX =  currentTileGridIndex.getX();
        currentY =  currentTileGridIndex.getY();
        GridIndex westTileGridIndex = new GridIndex(currentX - 1,
                currentY);
        if (isWithinBounds(westTileGridIndex)
                && map.getTile(westTileGridIndex).isWalkable()
                && hasNotBeenProcessed(westTileGridIndex)) {
            searchQueue.add(westTileGridIndex);
            listOfProcessed.add(westTileGridIndex);
            map.getTile(westTileGridIndex).setDirection(EAST);
            if (map.getTile(westTileGridIndex).isGoal()) {
               pathFromSToGExists = true;
            }
        }
        if (isWalkableNeigbour(westTileGridIndex)){
            nrOfNeigbours++;
        }
    }

    /**
     * Checks if tile at given index is walkable Tile within map bounds.
     * @param index index of tile to examine
     * @return true if is walkable and within map bounds, else false
     */
    private boolean isWalkableNeigbour(GridIndex index){
        return  (isWithinBounds(index) && map.getTile(index).isWalkable());
    }

    /**
     * Checks if grid index is within map boundaries
     * @param gridIndex grid index to examine
     * @return true if within bounds, else false
     */
    private boolean isWithinBounds(GridIndex gridIndex) {
        return (((gridIndex.getX() >= 0)) && ((gridIndex.getX() < gridWidth)))
                    && ((gridIndex.getY() >= 0))
                && ((gridIndex.getY() < gridHeight));
    }

    /**
     * Checks that given index in map has not been processed earlier in the
     * BFS.
     * @param gridIndex index to examine.
     * @return
     */
    private boolean hasNotBeenProcessed(GridIndex gridIndex) {
        for (GridIndex visitedIndex : listOfProcessed) {
            if ((visitedIndex.getX() == gridIndex.getX())
                    && (visitedIndex.getY() == gridIndex.getY())) {
                return false;
            }
        }
        return true;
    }
}
