package Model.WalkableTileTest;

import Model.Units.Tile;
import Model.Units.Tower;
import Model.Units.Troop;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class DummyTower extends Tower {

    public DummyTower(Tile tile) {
        super(tile,null);
    }

    @Override
    public void fire(CopyOnWriteArrayList<Troop> troops) {

    }


}
