package Model.EffectTest;

import Model.Effect;
import Model.Effects.FrostEffect;
import Model.Units.Troop;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FrostEffectTest {

    @Test
    public void testShouldReturnDormantDurationIfNotActivated() {
        Effect frost = new FrostEffect(10,10,10,null);
        assertEquals(10, frost.getDuration());
    }

    @Test
    public void testShouldReturnActiveDurationIfActivated() {
        Effect frost = new FrostEffect(10,10,10,null);
        frost.activate(20);
        assertEquals(20, frost.getDuration());
    }

    @Test
    public void testDurationShouldDecreaseWhenTroopIsAffectedByActiveEffect() {
        Effect frost = new FrostEffect(10,10,10,null);
        Troop dummyTroop = new DummyTroop(0,0);
        frost.activate(20);
        frost.affect(dummyTroop);
        assertTrue(frost.getDuration() < 20);
    }

    @Test
    public void testDurationShouldDecreaseWhenTroopIsAffectedByDormantEffect() {
        Effect frost = new FrostEffect(10,10,10,null);
        Troop dummyTroop = new DummyTroop(0,0);
        frost.affect(dummyTroop);
        assertTrue(frost.getDuration() < 10);
    }

    @Test
    public void testEffectShouldBeAddedToTroopListOfEffectsIfDormant() {
        Effect frost = new FrostEffect(10,10,10,null);
        Troop dummyTroop = new DummyTroop(0,0);
        frost.affect(dummyTroop);
        assertEquals(1, dummyTroop.getNumberOfEffects());
    }

    @Test
    public void testEffectShouldNotBeAddedToTroopListOfEffectsIfActive() {
        Effect frost = new FrostEffect(10,10,10,null);
        Troop dummyTroop = new DummyTroop(0,0);
        frost.activate(10);
        frost.affect(dummyTroop);
        assertEquals(0, dummyTroop.getNumberOfEffects());
    }

    @Test
    public void testFrostShouldDecreaseAffectedTroopsMovementSpeed() {
        Effect frost = new FrostEffect(10,10,10,null);
        DummyTroop dummyTroop = new DummyTroop(0,0);
        frost.activate(20);
        frost.affect(dummyTroop);
        assertEquals(15 * 0.30,dummyTroop.getSpeed());
    }
}
