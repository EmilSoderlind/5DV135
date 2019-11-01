package Model.TroopTest.SmallTroopTest;

import Model.TroopTest.StubEffect;
import Model.Units.SmallTroop;
import org.junit.jupiter.api.Test;

import static Utils.DIRECTION.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SmallTroopTest {

    @Test
    void ShouldBeCreatedWithCorrectXPosition() {
        int x = 5;
        int y = 6;
        SmallTroop smallTroop = new SmallTroop(x,y,null);
        assertEquals(5,smallTroop.getXPos());
    }

    @Test
    void ShouldBeCreatedWithCorrectYPosition() {
        int x = 5;
        int y = 6;
        SmallTroop smallTroop = new SmallTroop(x,y,null);
        assertEquals(6,smallTroop.getYPos());
    }

    @Test
    void ShouldIncreaseXPositionWhenDirectionIsEast() {
        int x = 50;
        int y = 60;
        SmallTroop smallTroop = new SmallTroop(x,y,null);
        smallTroop.setDirection(EAST);
        smallTroop.move();
        assertTrue(smallTroop.getXPos() > 50);
    }

    @Test
    void ShouldNotChangeYPositionWhenDirectionIsEast() {
        int x = 50;
        int y = 60;
        SmallTroop smallTroop = new SmallTroop(x,y,null);
        smallTroop.setDirection(EAST);
        smallTroop.move();
        assertEquals(60, smallTroop.getYPos());
    }

    @Test
    void ShouldDecreaseXPositionWhenDirectionIsWest() {
        int x = 50;
        int y = 60;
        SmallTroop smallTroop = new SmallTroop(x,y,null);
        smallTroop.setDirection(WEST);
        smallTroop.move();
        assertTrue(smallTroop.getXPos() < 50);
    }

    @Test
    void ShouldNotChangeYPositionWhenDirectionIsWest() {
        int x = 50;
        int y = 60;
        SmallTroop smallTroop = new SmallTroop(x,y,null);
        smallTroop.setDirection(WEST);
        smallTroop.move();
        assertEquals(60, smallTroop.getYPos());
    }

    @Test
    void ShouldIncreaseYPositionWhenDirectionIsSouth() {
        int x = 50;
        int y = 60;
        SmallTroop smallTroop = new SmallTroop(x,y,null);
        smallTroop.setDirection(SOUTH);
        smallTroop.move();
        assertTrue(smallTroop.getYPos() > 60);
    }

    @Test
    void ShouldNotChangeXPositionWhenDirectionIsSouth() {
        int x = 50;
        int y = 60;
        SmallTroop smallTroop = new SmallTroop(x,y,null);
        smallTroop.setDirection(SOUTH);
        smallTroop.move();
        assertEquals(50, smallTroop.getXPos());
    }

    @Test
    void ShouldDecreaseYPositionWhenDirectionIsNorth() {
        int x = 50;
        int y = 60;
        SmallTroop smallTroop = new SmallTroop(x,y,null);
        smallTroop.setDirection(NORTH);
        smallTroop.move();
        assertTrue(smallTroop.getYPos() < 60);
    }

    @Test
    void ShouldNotChangeXPositionWhenDirectionIsNorth() {
        int x = 50;
        int y = 60;
        SmallTroop smallTroop = new SmallTroop(x,y,null);
        smallTroop.setDirection(NORTH);
        smallTroop.move();
        assertEquals(50, smallTroop.getXPos());
    }

    @Test
    void GetHpShouldReturnCurrentHealth() {
        int x = 50;
        int y = 60;
        SmallTroop smallTroop = new SmallTroop(x,y,null);
        assertEquals(40, smallTroop.getHP());
    }

    @Test
    void affectHealthShouldChangeHP() {
        int x = 50;
        int y = 60;
        SmallTroop smallTrooop = new SmallTroop(x,y,null);
        smallTrooop.affectHP(-10);
        assertEquals(30, smallTrooop.getHP());
    }

    @Test
    void affectPositionShouldChangePositionX() {
        int x = 50;
        int y = 60;
        SmallTroop smallTroop = new SmallTroop(x,y,null);
        smallTroop.affectPosition(100,100);
        assertEquals(100, smallTroop.getXPos());
    }

    @Test
    void affectPositionShouldChangePositionY() {
        int x = 50;
        int y = 60;
        SmallTroop smallTroop = new SmallTroop(x,y,null);
        smallTroop.affectPosition(100,100);
        assertEquals(100, smallTroop.getYPos());
    }


    @Test
    void ShouldOnlyBeAbleToHaveOneEffectOfSameType() {
        int x = 50;
        int y = 60;
        SmallTroop smallTroop = new SmallTroop(x,y,null);
        StubEffect dummyEffect = new StubEffect(10,10,10);
        dummyEffect.affect(smallTroop);
        dummyEffect.affect(smallTroop);
        assertEquals(1,smallTroop.getNumberOfEffects());
    }

    @Test
    void TimedOutEffectsShouldBeRemovedFromListOfEffects() {
        int x = 50;
        int y = 60;
        SmallTroop smallTroop = new SmallTroop(x,y,null);
        StubEffect stubEffect = new StubEffect(10,10,10);
        stubEffect.affect(smallTroop);
        stubEffect.activate(1);
        smallTroop.statusUpdate();
        smallTroop.statusUpdate();
        assertEquals(0,smallTroop.getNumberOfEffects());
    }
}