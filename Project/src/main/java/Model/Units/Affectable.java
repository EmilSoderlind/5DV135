package Model.Units;

import Model.Effect;

/**
 * Interface for units to be affectable by effects on tiles
 */
public interface Affectable {

    void affectHP(double effect);

    void affectSpeed(double effect);

    void affectPosition(int xPosition, int yPosition);

    double getHP();

    double getSpeed();

    double getXPos();

    double getYPos();

    void addEffect(Effect effect);
}
