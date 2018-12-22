import java.awt.*;
import javax.swing.*;

public class MySimpleWindow extends JFrame {

    //declared  JPanel and JButton to store buttons
    private static final long serialVersionUID = 1L;
    protected JPanel msw_mainPanel;
    protected JPanel buttonPanel;
    protected JButton cancelButton;
    protected JButton okButton;

    /**
     * Constructor of the program
     * @param title*/
    public MySimpleWindow(String title) {
        super(title);
    }

    public MySimpleWindow() {

    }

    /**
     * Create 'OK' and 'Cancel' buttons with JButton and store them in msw_mainPanel  */
    protected void addComponents() {
        msw_mainPanel = new JPanel();
        /*buttonPanel = new JPanel();
        cancelButton = new JButton("Cancel");
        okButton = new JButton("OK");
        buttonPanel.add(cancelButton);
        buttonPanel.add(okButton);
        msw_mainPanel.add(buttonPanel);*/
        add(msw_mainPanel);
    }

    /**
     * Set the window of program
     * @param
     * */
    protected void setFrameFeatures() {
        // set the window location at the center of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        // set window to visible
        setVisible(true);
        pack();
        int w = getSize().width;
        int h = getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;
        setLocation(x,y);
        // exit application when click "close" button
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Combine method addComponents and setFrameFeatures
     * @param
     * */
    public static void createAndShowGUI() {
        MySimpleWindow msw = new MySimpleWindow("My Simple Window");
        msw.addComponents();
        msw.setFrameFeatures();
        msw.pack();
        msw.setVisible(true);
    }

    /**
     * Main method
     * */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

}