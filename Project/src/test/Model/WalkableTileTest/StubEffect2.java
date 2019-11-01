package Model.WalkableTileTest;

import Model.Effect;
import Model.Units.Affectable;

import java.awt.*;

public class StubEffect2 extends Effect {

    public StubEffect2(int dormantDuration, int xPosition, int yPosition) {
        super(dormantDuration, xPosition, yPosition,null);
    }

    public void affect(Affectable unitToBeAffected) {
        StubTroop troopToBeAffected = (StubTroop) unitToBeAffected;
        troopToBeAffected.setAffected();
    }

    @Override
    public void draw(Graphics g) {
    }
}
