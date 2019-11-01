package Model.TileRotatorTest;

import Model.Map;
import Model.TileRotator;
import Model.TileRotatorTest.StubMaps.*;
import Utils.GridIndex;
import org.junit.jupiter.api.Test;

import static Utils.DIRECTION.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TileRotatorTest {

    @Test
    public void testShouldChangeNorthToEastIfValid() {
        Map map = new NorthToEastStubMap(3, 3, "North To East Stub Map");
        TileRotator rotator = new TileRotator(map);
        GridIndex centerIndex = new GridIndex(1, 1);
        rotator.RotateToNextValidDirection(map.getTile(centerIndex));
        assertEquals(EAST, map.getTile(centerIndex).getDirection());
    }

    @Test
    public void testShouldChangeEastToSouthIfValid() {
        Map map = new EastToSouthStubMap(3, 3, "East To South Stub Map");
        TileRotator rotator = new TileRotator(map);
        GridIndex centerIndex = new GridIndex(1, 1);
        rotator.RotateToNextValidDirection(map.getTile(centerIndex));
        assertEquals(SOUTH, map.getTile(centerIndex).getDirection());
    }

    @Test
    public void testShouldChangeSouthToWestIfValid() {
        Map map = new SouthToWestStubMap(3, 3, "South To West Stub Map");
        TileRotator rotator = new TileRotator(map);
        GridIndex centerIndex = new GridIndex(1, 1);
        rotator.RotateToNextValidDirection(map.getTile(centerIndex));
        assertEquals(WEST, map.getTile(centerIndex).getDirection());
    }

    @Test
    public void testShouldChangeWestToNorthIfValid() {
        Map map = new WestToNorthStubMap(3, 3, "West To North Stub Map");
        TileRotator rotator = new TileRotator(map);
        GridIndex centerIndex = new GridIndex(1, 1);
        rotator.RotateToNextValidDirection(map.getTile(centerIndex));
        assertEquals(NORTH, map.getTile(centerIndex).getDirection());
    }

    @Test
    public void testShouldChangeNorthToWestIfEastAndSouthInvalid() {
        Map map = new NorthToWestStubMap(3, 3, "North To West Stub Map");
        TileRotator rotator = new TileRotator(map);
        GridIndex centerIndex = new GridIndex(1, 1);
        rotator.RotateToNextValidDirection(map.getTile(centerIndex));
        assertEquals(WEST, map.getTile(centerIndex).getDirection());
    }

    @Test
    public void testShouldChangeEastToNorthIfSouthAndWestInvalid() {
        Map map = new EastToNorthStubMap(3, 3, "East To North Stub Map");
        TileRotator rotator = new TileRotator(map);
        GridIndex centerIndex = new GridIndex(1, 1);
        rotator.RotateToNextValidDirection(map.getTile(centerIndex));
        assertEquals(NORTH, map.getTile(centerIndex).getDirection());
    }

    @Test
    public void testShouldChangeSouthToEastIfWestAndNorthInvalid() {
        Map map = new SouthToEastStubMap(3, 3, "South To East Stub Map");
        TileRotator rotator = new TileRotator(map);
        GridIndex centerIndex = new GridIndex(1, 1);
        rotator.RotateToNextValidDirection(map.getTile(centerIndex));
        assertEquals(EAST, map.getTile(centerIndex).getDirection());
    }

    @Test
    public void testShouldChangeWestToSouthIfNorthAndEastInvalid() {
        Map map = new WestToSouthStubMap(3, 3, "West To South Stub Map");
        TileRotator rotator = new TileRotator(map);
        GridIndex centerIndex = new GridIndex(1, 1);
        rotator.RotateToNextValidDirection(map.getTile(centerIndex));
        assertEquals(SOUTH, map.getTile(centerIndex).getDirection());
    }

    @Test
    public void testShouldReturnFalseIfCouldNotChangeDirection() {}

    @Test
    public void testShouldReturnTrueIfCouldChangeDirection() {}

    @Test
    public void testShouldNotBeAbleToRotateIfTileNeighboursAreNonWalkable() {
        Map map = new CanNotRotateIntoNonWalkableStubMap(3, 3, "Non-Walkable Stub Map");
        TileRotator rotator = new TileRotator(map);
        GridIndex centerIndex = new GridIndex(1, 1);
        assertFalse(rotator.RotateToNextValidDirection(map.getTile(centerIndex)));
    }

    @Test
    public void testShouldNotBeAbleToRotateIfTileNeighboursAreFacingTowardsCenterTile () {
        Map map = new CanNotRotateIntoOppositeDirectionsStubMap(3, 3, "Opposite Direction Stub Map");
        TileRotator rotator = new TileRotator(map);
        GridIndex centerIndex = new GridIndex(1, 1);
        assertFalse(rotator.RotateToNextValidDirection(map.getTile(centerIndex)));
    }

    @Test
    public void testShouldNotBeAbleToChangeDirectionTileOutOfBounds() {
        Map map = new CanNotRotateToTileOutOfBoundsStubMap(1, 1, "Out Of Bounds Stub Map");
        TileRotator rotator = new TileRotator(map);
        GridIndex centerIndex = new GridIndex(0, 0);
        assertFalse(rotator.RotateToNextValidDirection(map.getTile(centerIndex)));
    }
}
