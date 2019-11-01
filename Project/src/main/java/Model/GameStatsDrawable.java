package Model;

import Model.Units.Drawable;

import java.awt.*;

/**
 * GameStatsDrawable is used to display user stats on the game canvas
 * while playing.
 */
public class GameStatsDrawable implements Drawable {
    private int credit = -1;
    private int points = -1;
    private Point position;

    /**
     * @param credit to buy troops
     * @param points user points
     * @param pos where to draw the stats
     */
    public GameStatsDrawable(int credit, int points, Point pos) {
        this.credit = credit;
        this.points = points;
        this.position = pos;
    }

    @Override
    public void draw(Graphics g) {
        RenderingHints renderingHints = new RenderingHints
                (RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
        ((Graphics2D) g).setRenderingHints(renderingHints);
        g.setColor(Color.lightGray);
        g.fillRect(this.position.x,this.position.y,150,45);

        g.setColor(Color.black);
        g.drawString("Credit: " + credit,
                this.position.x+5,this.position.y+20);
        g.drawString("Points: " + points,
                this.position.x+5,this.position.y+40);
    }
}
