package Utils.GridIndexTest;

import Utils.GridIndex;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GridIndexTest  {
    @Test
    void ShouldBeConstructedWithCorrectX() {
        GridIndex gridIndex = new GridIndex(0,0);
        assertEquals(0,gridIndex.getX());
    }

    @Test
    void ShouldBeConstructedWithCorrectY() {
        GridIndex gridIndex = new GridIndex(0,0);
        assertEquals(0,gridIndex.getY());
    }

    @Test
    void ShouldChangeX() {
        GridIndex gridIndex = new GridIndex(0, 0);
        gridIndex.setX(1);
        assertEquals(1,gridIndex.getX());
    }

    @Test
    void ShouldChangeY() {
        GridIndex gridIndex = new GridIndex(0, 0);
        gridIndex.setY(1);
        assertEquals(1,gridIndex.getY());
    }
}