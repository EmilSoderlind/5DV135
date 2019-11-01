package Model.EffectTest;

import Model.Effect;
import Model.Effects.HealingEffect;
import Model.Units.Troop;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HealingEffectTest {
    @Test
    public void testShouldReturnDormantDurationIfNotActivated() {
        Effect healing = new HealingEffect(10,10,10,null);
        assertEquals(10, healing.getDuration());
    }

    @Test
    public void testShouldReturnActiveDurationIfActivated() {
        Effect healing = new HealingEffect(10,10,10,null);
        healing.activate(20);
        assertEquals(20, healing.getDuration());
    }

    @Test
    public void testDurationShouldDecreaseWhenTroopIsAffectedByActiveEffect() {
        Effect healing = new HealingEffect(10,10,10,null);
        Troop dummyTroop = new DummyTroop(0,0);
        healing.activate(20);
        healing.affect(dummyTroop);
        assertTrue(healing.getDuration() < 20);
    }

    @Test
    public void testDurationShouldNotDecreaseWhenTroopIsAffectedByDormantEffect() {
        Effect healing = new HealingEffect(10,10,10,null);
        Troop dummyTroop = new DummyTroop(0,0);
        healing.affect(dummyTroop);
        assertTrue(healing.getDuration() < 10);
    }

    @Test
    public void testEffectShouldBeAddedToTroopListOfEffectsIfDormant() {
        Effect healing = new HealingEffect(10,10,10,null);
        Troop dummyTroop = new DummyTroop(0,0);
        healing.affect(dummyTroop);
        assertEquals(1, dummyTroop.getNumberOfEffects());
    }

    @Test
    public void testEffectShouldNotBeAddedToTroopListOfEffectsIfActive() {
        Effect healing = new HealingEffect(10,10,10,null);
        Troop dummyTroop = new DummyTroop(0,0);
        healing.activate(10);
        healing.affect(dummyTroop);
        assertEquals(0, dummyTroop.getNumberOfEffects());
    }
    //TODO: -------------------------------Healing specific-------------------------------
    @Test
    public void testHealingShouldReplenishHPToAffectedTroopIfActive() {
        Effect healing = new HealingEffect(10,10,10,null);
        Troop dummyTroop = new DummyTroop(0,0);
        healing.activate(20);
        healing.affect(dummyTroop);
        assertEquals(46, dummyTroop.getHP());
    }
}

