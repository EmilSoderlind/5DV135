package Model.TroopTest.BigTroopTest;

import Model.TroopTest.StubEffect;
import Model.Units.BigTroop;
import org.junit.jupiter.api.Test;

import static Utils.DIRECTION.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BigTroopTest {

    @Test
    void ShouldBeCreatedWithCorrectXPosition() {
        int x = 5;
        int y = 6;
        BigTroop bigTroop = new BigTroop(x,y,null);
        assertEquals(5,bigTroop.getXPos());
    }

    @Test
    void ShouldBeCreatedWithCorrectYPosition() {
        int x = 5;
        int y = 6;
        BigTroop bigTroop = new BigTroop(x, y, null);
        assertEquals(6,bigTroop.getYPos());
    }

    @Test
    void ShouldIncreaseXPositionWhenDirectionIsEast() {
        int x = 50;
        int y = 60;
        BigTroop bigTroop = new BigTroop(x, y,null);
        bigTroop.setDirection(EAST);
        bigTroop.move();
        assertTrue(bigTroop.getXPos() > 50);
    }

    @Test
    void ShouldNotChangeYPositionWhenDirectionIsEast() {
        int x = 50;
        int y = 60;
        BigTroop bigTroop = new BigTroop(x, y,null);
        bigTroop.setDirection(EAST);
        bigTroop.move();
        assertEquals(60, bigTroop.getYPos());
    }

    @Test
    void ShouldDecreaseXPositionWhenDirectionIsWest() {
        int x = 50;
        int y = 60;
        BigTroop bigTroop = new BigTroop(x, y,null);
        bigTroop.setDirection(WEST);
        bigTroop.move();
        assertTrue(bigTroop.getXPos() < 50);
    }

    @Test
    void ShouldNotChangeYPositionWhenDirectionIsWest() {
        int x = 50;
        int y = 60;
        BigTroop bigTroop = new BigTroop(x, y,null);
        bigTroop.setDirection(WEST);
        bigTroop.move();
        assertEquals(60, bigTroop.getYPos());
    }

    @Test
    void ShouldIncreaseYPositionWhenDirectionIsSouth() {
        int x = 50;
        int y = 60;
        BigTroop bigTroop = new BigTroop(x, y,null);
        bigTroop.setDirection(SOUTH);
        bigTroop.move();
        assertTrue(bigTroop.getYPos() > 60);
    }

    @Test
    void ShouldNotChangeXPositionWhenDirectionIsSouth() {
        int x = 50;
        int y = 60;
        BigTroop bigTroop = new BigTroop(x, y,null);
        bigTroop.setDirection(SOUTH);
        bigTroop.move();
        assertEquals(50, bigTroop.getXPos());
    }

    @Test
    void ShouldDecreaseYPositionWhenDirectionIsNorth() {
        int x = 50;
        int y = 60;
        BigTroop bigTroop = new BigTroop(x, y,null);
        bigTroop.setDirection(NORTH);
        bigTroop.move();
        assertTrue(bigTroop.getYPos() < 60);
    }

    @Test
    void ShouldNotChangeXPositionWhenDirectionIsNorth() {
        int x = 50;
        int y = 60;
        BigTroop bigTroop = new BigTroop(x, y,null);
        bigTroop.setDirection(NORTH);
        bigTroop.move();
        assertEquals(50, bigTroop.getXPos());
    }

    @Test
    void GetHpShouldReturnCurrentHealth() {
        int x = 50;
        int y = 60;
        BigTroop bigTroop = new BigTroop(x, y,null);
        assertEquals(200, bigTroop.getHP());
    }

    @Test
    void affectHealthShouldChangeHP() {
        int x = 50;
        int y = 60;
        BigTroop bigTroop = new BigTroop(x, y,null);
        bigTroop.affectHP(-10);
        assertEquals(190, bigTroop.getHP());
    }

    @Test
    void affectPositionShouldChangePositionX() {
        int x = 50;
        int y = 60;
        BigTroop bigTroop = new BigTroop(x, y,null);
        bigTroop.affectPosition(100,100);
        assertEquals(100, bigTroop.getXPos());
    }

    @Test
    void affectPositionShouldChangePositionY() {
        int x = 50;
        int y = 60;
        BigTroop bigTroop = new BigTroop(x, y,null);
        bigTroop.affectPosition(100,100);
        assertEquals(100, bigTroop.getYPos());
    }

    @Test
    void ShouldOnlyBeAbleToHaveOneEffectOfSameType() {
        int x = 50;
        int y = 60;
        BigTroop bigTroop = new BigTroop(x, y,null);
        StubEffect stubEffect = new StubEffect(10,10,10);
        stubEffect.affect(bigTroop);
        stubEffect.affect(bigTroop);
        assertEquals(1,bigTroop.getNumberOfEffects());
    }

    @Test
    void TimedOutEffectsShouldBeRemovedFromListOfEffects() {
        int x = 50;
        int y = 60;
        BigTroop bigTroop = new BigTroop(x, y,null);
        StubEffect stubEffect = new StubEffect(10,10,10);
        stubEffect.affect(bigTroop);
        stubEffect.activate(1);
        bigTroop.statusUpdate();
        bigTroop.statusUpdate();
        assertEquals(0,bigTroop.getNumberOfEffects());
    }
}