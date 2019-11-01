package Model.EffectTest;

import Model.Effect;
import Model.Effects.FireEffect;
import Model.Units.Troop;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FireEffectTest {

    @Test
    public void testShouldReturnDormantDurationIfNotActivated() {
        Effect fire = new FireEffect(10,10,10,null);
        assertEquals(10, fire.getDuration());
    }

    @Test
    public void testShouldReturnActiveDurationIfActivated() {
        Effect fire = new FireEffect(10,10,10,null);
        fire.activate(20);
        assertEquals(20, fire.getDuration());
    }

    @Test
    public void testDurationShouldDecreaseWhenTroopIsAffectedByActiveEffect() {
        Effect fire = new FireEffect(10,10,10,null);
        Troop dummyTroop = new DummyTroop(0,0);
        fire.activate(20);
        fire.affect(dummyTroop);
        assertTrue(fire.getDuration() < 20);
    }

    @Test
    public void testDurationShouldDecreaseWhenTroopIsAffectedByDormantEffect() {
        Effect fire = new FireEffect(10,10,10,null);
        Troop dummyTroop = new DummyTroop(0,0);
        fire.affect(dummyTroop);
        assertTrue(fire.getDuration() < 10);
    }

    @Test
    public void testEffectShouldBeAddedToTroopListOfEffectsIfDormant() {
        Effect fire = new FireEffect(10,10,10,null);
        Troop dummyTroop = new DummyTroop(0,0);
        fire.affect(dummyTroop);
        assertEquals(1, dummyTroop.getNumberOfEffects());
    }

    @Test
    public void testEffectShouldNotBeAddedToTroopListOfEffectsIfActive() {
        Effect fire = new FireEffect(10,10,10,null);
        Troop dummyTroop = new DummyTroop(0,0);
        fire.activate(10);
        fire.affect(dummyTroop);
        assertEquals(0, dummyTroop.getNumberOfEffects());
    }

    @Test
    public void testFireShouldDealDamageToAffectedTroopIfActive() {
        Effect fire = new FireEffect(10,10,10,null);
        Troop dummyTroop = new DummyTroop(0,0);
        fire.activate(20);
        fire.affect(dummyTroop);
        assertEquals(44, dummyTroop.getHP());
    }
}
