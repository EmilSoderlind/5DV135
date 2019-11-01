import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


public class TestGUI {

    private JFrame frame;
    private JTextArea resultField;
    private JTextField searchField;

    private ActionListener runButtonAL;
    private ActionListener resetButtonAL;

    /**
     * Builds, layout its components and start unit-tester-GUI
     * @param  title  Title of the started JFrame
     */
    public void initiate(String title) {


        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        resultField = new JTextArea();
        resultField.setEditable(false);

        final JPanel SeachPanel = new JPanel();
        SeachPanel.setLayout(new BoxLayout (SeachPanel, BoxLayout.X_AXIS));

        searchField = new JTextField("Class name here...");

        JButton RunButton = new JButton("Test");
        JButton ResetButton = new JButton("Reset");


        RunButton.addActionListener(runButtonAL);

        ResetButton.addActionListener(resetButtonAL);

        SeachPanel.add(searchField);
        SeachPanel.add(RunButton);

        frame.add(SeachPanel,BorderLayout.NORTH);
        frame.add(resultField,BorderLayout.CENTER);
        frame.add(ResetButton,BorderLayout.SOUTH);

        frame.setSize(500, 500);
    }

    /**
     * Sets actionlisteners to be used on run/reset-button
     * @param  runButtonAL  Run buttons Actionlistener
     * @param  resetButtonAL  Reset buttons Actionlistener
     */
    public void setActionListeners(ActionListener runButtonAL,
                                    ActionListener resetButtonAL){

        this.resetButtonAL = resetButtonAL;
        this.runButtonAL = runButtonAL;

    }

    /**
     * Returns string written in search field, used by controller
     * @return classname to be tested
     */
    public String getSearchField(){
        return searchField.getText();
    }

    /**
     * Returns string written in search field, used by controller
     * @param res Sets resultField with string
     */
    public void setResultField(String res){
        resultField.setText(res);
    }

    public void show() {
        frame.setVisible(true);
    }

}
