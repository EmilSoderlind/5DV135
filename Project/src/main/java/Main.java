import Controller.Controller;
import Model.Session;
import View.View;

import java.awt.*;

public class Main {

    /**
     * Start Anti-Tower defence game
     * @param args input parameters
     */
    public static void main(String[] args) {

        // Dimension of game canvas
        Dimension d = new Dimension(640, 640);

        Session model = new Session(d);

        View view = new View();

        Controller c = new Controller(model, view, d);
        c.init(model, args);
        model.gameLoop();
    }
}