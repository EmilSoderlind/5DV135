package Model.WalkableTileTest;

import Model.Effect;
import Model.Units.Tower;
import Model.Units.WalkableTile;
import Utils.TILESTATUS;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.security.SecureRandom;

import static Utils.DIRECTION.*;
import static org.junit.jupiter.api.Assertions.*;

class WalkableTileTest {
    @Test
    void ShouldBeConstructedWithCorrectXPosition() {
        Rectangle rectangle = new Rectangle(0, 0, 20, 20);
        WalkableTile tile = new WalkableTile(rectangle, TILESTATUS.WALKABLE);
        assertEquals(0,tile.getXPosition());
    }

    @Test
    void ShouldBeConstructedWithCorrectYPosition() {
        Rectangle rectangle = new Rectangle(0, 0, 20, 20);
        WalkableTile tile = new WalkableTile(rectangle, TILESTATUS.WALKABLE);
        assertEquals(0,tile.getYPosition());
    }

    @Test
    void ShouldBeConstructedWithCorrectWidth() {
        Rectangle rectangle = new Rectangle(0, 0, 20, 20);
        WalkableTile tile = new WalkableTile(rectangle, TILESTATUS.WALKABLE);
        assertEquals(20,tile.getWidth());
    }

    @Test
    void ShouldBeConstructedWithCorrectHeight() {
        Rectangle rectangle = new Rectangle(0, 0, 20, 20);
        WalkableTile tile = new WalkableTile(rectangle, TILESTATUS.WALKABLE);
        assertEquals(20,tile.getHeight());
    }

    @Test
    void ShouldBeConstructedWithCorrectXGridPosition() {
        Rectangle rectangle = new Rectangle(0, 0, 20, 20);
        WalkableTile tile = new WalkableTile(rectangle, TILESTATUS.WALKABLE);
        assertEquals(0,tile.getXGridPosition());
    }

    @Test
    void ShouldBeConstructedWithCorrectYGridPosition() {
        Rectangle rectangle = new Rectangle(0, 0, 20, 20);
        WalkableTile tile = new WalkableTile(rectangle, TILESTATUS.WALKABLE);
        assertEquals(0,tile.getYGridPosition());
    }

    @Test
    void ShouldNotHaveTowerAfterConstruct() {
        Rectangle rectangle = new Rectangle(0, 0, 20, 20);
        WalkableTile tile = new WalkableTile(rectangle, TILESTATUS.WALKABLE);
        assertFalse(tile.hasTower());
    }

    @Test
    void ShouldHaveTowerAfterTowerIsAdded() {
        Rectangle rectangle = new Rectangle(0, 0, 20, 20);
        WalkableTile tile = new WalkableTile(rectangle, TILESTATUS.WALKABLE);
        Tower tower = new DummyTower(tile);
        tile.addTower();
        assertTrue(tile.hasTower());
    }

    @Test
    void ShouldNotHaveTowerAfterTowerIsRemoved() {
        Rectangle rectangle = new Rectangle(0, 0, 20, 20);
        WalkableTile tile = new WalkableTile(rectangle, TILESTATUS.WALKABLE);
        Tower tower = new DummyTower(tile);
        tile.addTower();
        tile.removeTower();
        assertFalse(tile.hasTower());
    }

    @Test
    void ShouldNotHaveEffectAfterConstruct() {
        Rectangle rectangle = new Rectangle(0, 0, 20, 20);
        WalkableTile tile = new WalkableTile(rectangle, TILESTATUS.WALKABLE);
        assertFalse(tile.hasEffect());
    }

    @Test
    void ShouldHaveEffectAfterEffectIsAdded() {
        Rectangle rectangle = new Rectangle(0, 0, 20, 20);
        WalkableTile tile = new WalkableTile(rectangle, TILESTATUS.WALKABLE);
        Effect effect = new StubEffect1(10,10,10);
        tile.addEffect(effect);
    }

    @Test
    void ShouldSetNorthDirection() {
        Rectangle rectangle = new Rectangle(0, 0, 20, 20);
        WalkableTile tile = new WalkableTile(rectangle, TILESTATUS.WALKABLE);
        tile.setDirection(NORTH);
        assertEquals(NORTH,tile.getDirection());
    }

    @Test
    void ShouldSetEastDirection() {
        Rectangle rectangle = new Rectangle(0, 0, 20, 20);
        WalkableTile tile = new WalkableTile(rectangle, TILESTATUS.WALKABLE);
        tile.setDirection(EAST);
        assertEquals(EAST,tile.getDirection());
    }

    @Test
    void ShouldSetSouthDirection() {
        Rectangle rectangle = new Rectangle(0, 0, 20, 20);
        WalkableTile tile = new WalkableTile(rectangle, TILESTATUS.WALKABLE);
        tile.setDirection(SOUTH);
        assertEquals(SOUTH,tile.getDirection());
    }

    @Test
    void ShouldSetWestDirection() {
        Rectangle rectangle = new Rectangle(0, 0, 20, 20);
        WalkableTile tile = new WalkableTile(rectangle, TILESTATUS.WALKABLE);
        tile.setDirection(WEST);
        assertEquals(WEST,tile.getDirection());
    }

    @Test
    void landOnShouldCallChangeDirectionIfInsideCenterRectangle(){
        Rectangle rectangle = new Rectangle(0, 0, 20, 20);
        WalkableTile tile = new WalkableTile(rectangle, TILESTATUS.WALKABLE);
        tile.setDirection(WEST);
        StubTroop troop = new StubTroop((int) rectangle.getCenterX(),(int) rectangle.getCenterY());
        tile.landOn(troop);
        assertEquals(true,troop.changedDirection());
    }

    @Test
    void landOnShouldCallAffectIfInsideRectangle() {
        Rectangle rectangle = new Rectangle(0, 0, 20, 20);
        WalkableTile tile = new WalkableTile(rectangle, TILESTATUS.WALKABLE);
        tile.setDirection(WEST);
        SecureRandom random = new SecureRandom();
        int randomInt = random.nextInt(20);
        StubTroop troop = new StubTroop(randomInt,randomInt);
        Effect effect = new StubEffect1(10,10,10);
        tile.addEffect(effect);
        tile.landOn(troop);
        assertEquals(true,troop.affected());
    }

    @Test
    void OnlyOneEffectOfEachTypeIsAdded() {
        Rectangle rectangle = new Rectangle(0, 0, 20, 20);
        WalkableTile tile = new WalkableTile(rectangle, TILESTATUS.WALKABLE);
        Effect effect1 = new StubEffect1(10,10,10);
        Effect effect2 = new StubEffect2(10,10,10);
        Effect effect3 = new StubEffect1(10,10,10);
        tile.addEffect(effect1);
        tile.addEffect(effect2);
        tile.addEffect(effect3);
        assertEquals(2,tile.getNumberOfEffects());
    }

    @Test
    void TileShouldBeRotatbleIfCreatedWithStatusRotatable() {
        Rectangle rectangle = new Rectangle(0, 0, 20, 20);
        WalkableTile tile = new WalkableTile(rectangle, TILESTATUS.WALKABLE);
        tile.setRotatable(true);
        assertTrue(tile.isRotatable());
    }

    @Test
    void TimedOutEffectsShouldBeRemovedFromListOfEffects() {
        Rectangle rectangle = new Rectangle(0, 0, 20, 20);
        WalkableTile tile = new WalkableTile(rectangle, TILESTATUS.WALKABLE);
        tile.setDirection(WEST);
        SecureRandom random = new SecureRandom();
        int randomInt = random.nextInt(20);
        StubTroop troop = new StubTroop(randomInt,randomInt);
        Effect effect1 = new StubEffect1(0,10,10);
        Effect effect2 = new StubEffect2(0,10,10);
        tile.addEffect(effect1);
        tile.addEffect(effect2);
        tile.landOn(troop);
        assertEquals(0,tile.getNumberOfEffects());
    }

    @Test
    void FlushEffectsShouldRemoveAllEffects() {
        Rectangle rectangle = new Rectangle(0, 0, 20, 20);
        WalkableTile tile = new WalkableTile(rectangle, TILESTATUS.WALKABLE);
        Effect effect1 = new StubEffect1(10,10,10);
        tile.addEffect(effect1);
        tile.flushEffects();
        assertEquals(0,tile.getNumberOfEffects());
    }
}