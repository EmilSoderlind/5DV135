package Model.Units;

import Utils.TILESTATUS;

import java.awt.*;

/**
 * Class for buildable tiles in the game. Buildable
 * means that towers can be built on this tile.
 */
public class BuildableTile extends Tile {

    public BuildableTile(Rectangle frame, TILESTATUS status) {
        super(frame, status);
    }

    @Override
    public void draw(Graphics g) {
        RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        ((Graphics2D) g).setRenderingHints(renderingHints);
        g.setColor(Color.black);
        super.draw(g);
    }

    @Override
    public Tile copy() {
        return new BuildableTile(getFrame(), getStatus());
    }
}
