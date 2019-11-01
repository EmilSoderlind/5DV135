package Model.PathSetterTest;

import Model.Map;
import Model.PathSetterStrategy;

public class StubStrategy extends PathSetterStrategy {

    @Override
    public boolean setPaths(Map map) {
        return true;
    }
}
