package Model.TroopTest;

import Model.Effect;
import Model.Units.Affectable;
import Utils.EFFECTSTATUS;

import java.awt.*;

public class StubEffect extends Effect {

    public StubEffect(int dormantDuration, int xPosition, int yPosition) {
        super(dormantDuration, xPosition, yPosition,null);
    }

    @Override
    public void affect(Affectable unitToBeAffected) {
        if (effectStatus == EFFECTSTATUS.DORMANT) {
            unitToBeAffected.addEffect(this);
        }
        duration--;
    }

    @Override
    public void draw(Graphics g) {

    }
}
