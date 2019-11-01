package Model.Effects;

import Model.Effect;
import Model.Units.Affectable;
import Utils.EFFECTSTATUS;

import java.awt.*;

/**
 * HealingEffect is an effect that increases the HP of
 * affectable objects.
 */
public class HealingEffect extends Effect {

    public HealingEffect(int dormantDuration, double xPosition, double yPosition, Integer maxImageSize) {
        super(dormantDuration, xPosition, yPosition, maxImageSize);
    }

   /**
    * If effect is dormant, add effect to Affectable object.
    * If effect is active, increase HP of Affectable object
    * @param unitToBeAffected Affectable unit.
    */
    @Override
    public void affect(Affectable unitToBeAffected) {
        if (effectStatus == EFFECTSTATUS.DORMANT) {
            HealingEffect activeHealing = new HealingEffect(10,xPosition,yPosition, maxImageSize);
            activeHealing.activate(100);
            unitToBeAffected.addEffect(activeHealing);
        }
        else if (effectStatus == EFFECTSTATUS.ACTIVE) {
            unitToBeAffected.affectHP(1);
            xPosition = unitToBeAffected.getXPos();
            yPosition = unitToBeAffected.getYPos();
        }
        duration = duration - 1;
    }

     /**
     * Draws HealingEffect onto given graphics object.
     * @param g graphics object to be drawn on.
     */
    @Override
    public void draw(Graphics g) {
       if (maxImageSize != null) {
            RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            ((Graphics2D) g).setRenderingHints(renderingHints);
            if (effectStatus == EFFECTSTATUS.DORMANT) {
                g.setColor(Color.GREEN);
                ((Graphics2D) g).setStroke(new BasicStroke(4));
                g.drawRect((int) xPosition - (int) (0.05 * maxImageSize * duration) / 2, (int) yPosition - (int) (0.05 * maxImageSize * duration) / 2, (int) (0.05 * maxImageSize * duration), (int) (0.05 * maxImageSize * duration));
            } else if (effectStatus == EFFECTSTATUS.ACTIVE){
                g.setColor(Color.GREEN);
                ((Graphics2D) g).setStroke(new BasicStroke(4));
                g.drawOval((int) xPosition - (int) (0.03 * maxImageSize * duration) / 8, (int) yPosition - (int) (0.03 * maxImageSize * duration) / 8,
                        (int) (0.03 * maxImageSize * duration) / 4, (int) (0.03 * maxImageSize * duration) / 4);
            }
        }
    }
}
