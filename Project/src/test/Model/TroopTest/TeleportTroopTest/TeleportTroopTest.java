package Model.TroopTest.TeleportTroopTest;

import Model.Effects.TeleportEntrance;
import Model.TroopTest.StubEffect;
import Model.Units.TeleportTroop;
import org.junit.jupiter.api.Test;

import static Utils.DIRECTION.*;
import static org.junit.jupiter.api.Assertions.*;

class TeleportTroopTest {

    @Test
    void ShouldBeCreatedWithCorrectXPosition() {
        int x = 5;
        int y = 6;
        TeleportTroop teleportTroop = new TeleportTroop(x,y,null);
        assertEquals(5,teleportTroop.getXPos());
    }

    @Test
    void ShouldBeCreatedWithCorrectYPosition() {
        int x = 5;
        int y = 6;
        TeleportTroop teleportTroop = new TeleportTroop(x,y,null);
        assertEquals(6,teleportTroop.getYPos());
    }

    @Test
    void ShouldIncreaseXPositionWhenDirectionIsEast() {
        int x = 50;
        int y = 60;
        TeleportTroop teleportTroop = new TeleportTroop(x, y,null);
        teleportTroop.setDirection(EAST);
        teleportTroop.statusUpdate();
        teleportTroop.move();
        assertTrue(teleportTroop.getXPos() > 50);
    }

    @Test
    void ShouldNotChangeYPositionWhenDirectionIsEast() {
        int x = 50;
        int y = 60;
        TeleportTroop teleportTroop = new TeleportTroop(x, y,null);
        teleportTroop.setDirection(EAST);
        teleportTroop.statusUpdate();
        teleportTroop.move();
        assertEquals(60, teleportTroop.getYPos());
    }

    @Test
    void ShouldDecreaseXPositionWhenDirectionIsWest() {
        int x = 50;
        int y = 60;
        TeleportTroop teleportTroop = new TeleportTroop(x, y,null);
        teleportTroop.setDirection(WEST);
        teleportTroop.statusUpdate();
        teleportTroop.move();
        assertTrue(teleportTroop.getXPos() < 50);
    }

    @Test
    void ShouldNotChangeYPositionWhenDirectionIsWest() {
        int x = 50;
        int y = 60;
        TeleportTroop teleportTroop = new TeleportTroop(x, y,null);
        teleportTroop.setDirection(WEST);
        teleportTroop.move();
        teleportTroop.statusUpdate();
        assertEquals(60, teleportTroop.getYPos());
    }

    @Test
    void ShouldIncreaseYPositionWhenDirectionIsSouth() {
        int x = 50;
        int y = 60;
        TeleportTroop teleportTroop = new TeleportTroop(x, y,null);
        teleportTroop.setDirection(SOUTH);
        teleportTroop.statusUpdate();
        teleportTroop.move();
        assertTrue(teleportTroop.getYPos() > 60);
    }

    @Test
    void ShouldNotChangeXPositionWhenDirectionIsSouth() {
        int x = 50;
        int y = 60;
        TeleportTroop teleportTroop = new TeleportTroop(x, y,null);
        teleportTroop.setDirection(SOUTH);
        teleportTroop.statusUpdate();
        teleportTroop.move();
        assertEquals(50, teleportTroop.getXPos());
    }

    @Test
    void ShouldDecreaseYPositionWhenDirectionIsNorth() {
        int x = 50;
        int y = 60;
        TeleportTroop teleportTroop = new TeleportTroop(x, y,null);
        teleportTroop.setDirection(NORTH);
        teleportTroop.statusUpdate();
        teleportTroop.move();
        assertTrue(teleportTroop.getYPos() < 60);
    }

    @Test
    void ShouldNotChangeXPositionWhenDirectionIsNorth() {
        int x = 50;
        int y = 60;
        TeleportTroop teleportTroop = new TeleportTroop(x, y,null);
        teleportTroop.setDirection(NORTH);
        teleportTroop.statusUpdate();
        teleportTroop.move();
        assertEquals(50, teleportTroop.getXPos());
    }

    @Test
    void ShouldBeReadyToDropPortalWhenNoPortalHasBeenDropped() {
        int x = 50;
        int y = 60;
        TeleportTroop teleportTroop = new TeleportTroop(x, y,null);
        assertTrue(teleportTroop.readyToDropPortal());
    }

    @Test
    void ShouldDropPortalWhenDropPortalIsCalled() {
        int x = 50;
        int y = 60;
        TeleportTroop teleportTroop = new TeleportTroop(x, y,null);
        teleportTroop.dropPortal(x,y);
        assertFalse(teleportTroop.readyToDropPortal());
    }

    @Test
    void GetHpShouldReturnCurrentHealth() {
        int x = 50;
        int y = 60;
        TeleportTroop teleportTroop = new TeleportTroop(x, y,null);
        assertEquals(80, teleportTroop.getHP());
    }

    @Test
    void affectHealthShouldChangeHP() {
        int x = 50;
        int y = 60;
        TeleportTroop teleportTroop = new TeleportTroop(x, y,null);
        teleportTroop.affectHP(-10);
        assertEquals(70, teleportTroop.getHP());
    }

    @Test
    void affectPositionShouldChangePositionX() {
        int x = 50;
        int y = 60;
        TeleportTroop teleportTroop = new TeleportTroop(x, y,null);
        teleportTroop.affectPosition(100,100);
        assertEquals(100, teleportTroop.getXPos());
    }

    @Test
    void affectPositionShouldChangePositionY() {
        int x = 50;
        int y = 60;
        TeleportTroop teleportTroop = new TeleportTroop(x, y,null);
        teleportTroop.affectPosition(100,100);
        assertEquals(100, teleportTroop.getYPos());
    }

    @Test
    void ShouldOnlyBeAbleToHaveOneEffectOfSameType() {
        int x = 50;
        int y = 60;
        TeleportTroop teleportTroop = new TeleportTroop(x, y,null);
        StubEffect dummyEffect = new StubEffect(10,10,10);
        dummyEffect.affect(teleportTroop);
        assertEquals(1,teleportTroop.getNumberOfEffects());
    }

    @Test
    void DropPortalShouldReturnTeleportEntranceWithCorrectStart() {
        int x = 50;
        int y = 60;
        TeleportTroop teleportTroop = new TeleportTroop(x, y,null);
        TeleportEntrance entrance = teleportTroop.dropPortal(x,y);
        assertEquals(50,entrance.getX());
    }

    @Test
    void DropPortalShouldReturnTeleportExitWithCorrectY() {
        int x = 50;
        int y = 60;
        TeleportTroop teleportTroop = new TeleportTroop(x, y,null);
        TeleportEntrance entrance = teleportTroop.dropPortal(x,y);
        assertEquals(60,entrance.getY());
    }


    @Test
    void DropPortalShouldReturnTeleportExitWithCorrectDuration() {
        int x = 50;
        int y = 60;
        TeleportTroop teleportTroop = new TeleportTroop(x, y,null);
        TeleportEntrance entrance = teleportTroop.dropPortal(x,y);
        assertEquals(10, entrance.getDuration());
    }


    @Test
    void TimedOutEffectsShouldBeRemovedFromListOfEffects() {
        int x = 50;
        int y = 60;
        TeleportTroop teleportTroop = new TeleportTroop(x, y,null);
        StubEffect stubEffect = new StubEffect(10,10,10);
        stubEffect.affect(teleportTroop);
        stubEffect.activate(1);
        teleportTroop.statusUpdate();
        teleportTroop.statusUpdate();
        assertEquals(0,teleportTroop.getNumberOfEffects());
    }
}