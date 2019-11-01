package Model.PathSetterTest;

import Model.Map;
import Model.PathSetter;
import Model.PathSetterStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PathSetterTest {

    @Test
    public void testSetPathShouldReturnResultOfPathSettingIfStrategyIsNotNull() {
        Map map = new WalkableStubMap(3, 3, "Walkable Stub map");
        PathSetter pathSetter = new PathSetter(new StubStrategy());
        assertTrue(pathSetter.setPaths(map));
    }

    @Test
    public void testSetPathsShouldReturnFalseIfNoStrategtIsSet() {
        Map map = new WalkableStubMap(3, 3, "Walkable Stub map");
        PathSetter pathSetter = new PathSetter();
        assertFalse(pathSetter.setPaths(map));
    }

    @Test
    public void testSetStrategyShouldSetStrategy() {
        Map map = new WalkableStubMap(3, 3, "Walkable Stub map");
        PathSetter pathSetter = new PathSetter();
        PathSetterStrategy strategy = new StubStrategy();
        pathSetter.setStrategy(strategy);
        assertTrue(pathSetter.setPaths(map));
    }
}
