package Model.EffectTest;

import Model.Units.Troop;

import java.awt.*;

public class DummyTroop extends Troop {

    public DummyTroop(int x, int y) {
        super(x, y,null);
        maxHP = 50;
        currentHP = maxHP-5;
        defaultSpeed = 15;
        currentSpeed = defaultSpeed;
    }

    @Override
    public void draw(Graphics graphic) {

    }

    @Override
    public void move() {

    }

    public double getSpeed() {
        return currentSpeed;
    }
}
