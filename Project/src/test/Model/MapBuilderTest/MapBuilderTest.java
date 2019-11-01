package Model.MapBuilderTest;

import Model.MapBuilder;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MapBuilderTest {
    @Test
    public void testNoneExistingMapFile() throws SAXException {
        MapBuilder mapBuilder = new MapBuilder();
        mapBuilder.fromFile("NotAMapFile.xml", 100);
        Exception ex = mapBuilder.getExceptions().get(0);
        assertTrue(ex instanceof FileNotFoundException);
    }

    @Test
    public void testNoneExistingTileClass() throws SAXException {
        MapBuilder mapBuilder = new MapBuilder();
        mapBuilder.fromFile("levels/test/no_existing_tileclass.xml", 640);
        Exception ex = mapBuilder.getExceptions().get(0);
        assertTrue(ex.getCause() instanceof ClassNotFoundException);
    }

    @Test
    public void testBadGrid() throws SAXException {
        MapBuilder mapBuilder = new MapBuilder();
        mapBuilder.fromFile("levels/test/levels_bad_grid.xml", 640);
        Exception ex = mapBuilder.getExceptions().get(0);
        assertTrue(ex instanceof IOException);
    }

    @Test
    public void testMissingStart() throws SAXException {
        MapBuilder mapBuilder = new MapBuilder();
        mapBuilder.fromFile("levels/test/levels_missing_start.xml", 640);
        Exception ex = mapBuilder.getExceptions().get(0);
        assertTrue(ex.toString()
                .contains("Map requires one tile with status 'START'"));
    }

    @Test
    public void testMultipleStarts() throws SAXException {
        MapBuilder mapBuilder = new MapBuilder();
        mapBuilder.fromFile("levels/test/levels_multiple_starts.xml", 640);
        Exception ex = mapBuilder.getExceptions().get(0);
        assertTrue(ex.toString()
                .contains("Map has to many tiles with status 'START' (2)"));
    }

    @Test
    public void testMissingGoal() throws SAXException {
        MapBuilder mapBuilder = new MapBuilder();
        mapBuilder.fromFile("levels/test/levels_missing_goal.xml", 640);
        Exception ex = mapBuilder.getExceptions().get(0);
        assertTrue(ex.toString()
                .contains("At least one tile needs to have status 'GOAL'"));
    }
}
