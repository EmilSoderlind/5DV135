import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {

    TestGUI view;
    TestingLogic model;

    /**
     * Sets Actionlisteners to view
     */
    private void setActionListenersInView(){

        ActionListener runButtonAL = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                view.setResultField(model.performTest(view.getSearchField()));
            }
        };

        ActionListener resetButtonAL = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                view.setResultField("");
            }
        };

        view.setActionListeners(runButtonAL,resetButtonAL);
    }

    public Controller(TestingLogic model, final TestGUI view) {
        this.view = view;
        this.model = model;

        setActionListenersInView();

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                view.initiate("MyUnitTester");
                view.show();

            }});
    }

    /**
     * Starts the unit tester GUI
     */
    public static void main(String[] args) {

        new Controller(new TestingLogic(),new TestGUI());

    }

}