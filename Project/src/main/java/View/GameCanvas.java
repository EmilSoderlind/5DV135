package View;

import Model.Units.Drawable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;


/**
 * GameCanvas are responsible for rendering Drawables game objects on a JPanel
 */
public class GameCanvas extends JPanel {
    private static final long serialVersionUID = 1L;

    private Image offScreenImageDrawed = null;

    private int counter = 0;
    private ArrayList<Drawable> drawables = new ArrayList<>();

    public GameCanvas(Dimension d) {
        this.setPreferredSize(d);
        this.setBackground(Color.white);
    }

    /**
     * Sets mouseListener for clicks on the rendering-Canvas
     * @param ml mouselistener to be called
     */
    void setClickedOnCanvas(MouseListener ml){
        this.addMouseListener(ml);
    }


    @Override
    public void update(Graphics g) {
        paint(g);
        System.out.println("gameLoop called ----------->");
    }


    @Override
    public void paint(final Graphics g) {

        final Dimension d = getSize();

        if (offScreenImageDrawed == null) {

            offScreenImageDrawed = createImage(d.width, d.height);
        }

        Graphics offScreenGraphicsDrawed = offScreenImageDrawed.getGraphics();
        offScreenGraphicsDrawed.setColor(Color.white);
        offScreenGraphicsDrawed.fillRect(0, 0, d.width, d.height) ;
        renderOffScreen(offScreenImageDrawed.getGraphics());

        float scale = Math.min((float) this.getWidth() / offScreenImageDrawed.getWidth(null), (float) this.getHeight() / offScreenImageDrawed.getHeight(null));
        int displayWidth = (int) (offScreenImageDrawed.getWidth(null) * scale);
        int displayHeight = (int) (offScreenImageDrawed.getHeight(null) * scale);
        g.drawImage(offScreenImageDrawed, this.getWidth()/2 - displayWidth/2, this.getHeight()/2 - displayHeight/2, displayWidth, displayHeight, null);
        //g.drawImage(offScreenImageDrawed, 0, 0, null); // TODO: revert if scaling is removed

    }

    private void renderOffScreen(final Graphics g) {

        g.setColor(Color.black);

        g.drawString("Credit: " + counter, 510, 20);

        counter++;

        drawDrawables(g);

    }


    /**
     * Setter for Drawable game objects to be drawn in next frame
     * @param drawableList List of Drawable
     */
    public void setDrawables(ArrayList<Drawable> drawableList){
        this.drawables = drawableList;

        // Repaint
        if (!SwingUtilities.isEventDispatchThread()) {
            EventQueue.invokeLater(() -> {
                if (GameCanvas.this != null) {
                    GameCanvas.this.repaint();
                }
            });
        } else {
            if (GameCanvas.this != null) {
                GameCanvas.this.repaint();
            }
        }
    }

    private void drawDrawables(Graphics g){
        for (Drawable obj: this.drawables) {
            obj.draw(g);
        }
    }

    @Override
    public Dimension getSize() {
        return super.getSize();
    }
}