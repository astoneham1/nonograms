import javax.swing.*;

public class app {
    private JPanel mainPanel;
    private JLabel titleText;
    private JPanel controls;
    private JButton loadFile;
    private JButton reset;
    private JPanel grid;
    private JPanel colorGuide;
    private JPanel color1;
    private JPanel color1Preview;
    private JLabel color1Name;
    private JPanel color2;
    private JPanel color2Preview;
    private JLabel color2Name;
    private JPanel color3;
    private JPanel color3Preview;
    private JLabel color3Label;
    private JButton check;
    private JButton solve;

    public app() {
        loadFile.addActionListener(e -> {
            // Code to be executed when the button is clicked
            JOptionPane.showMessageDialog(mainPanel, "load clicked");
        });

        reset.addActionListener(e -> {
            // Code to be executed when the button is clicked
            JOptionPane.showMessageDialog(mainPanel, "reset clicked");
        });

        check.addActionListener(e -> {
            // Code to be executed when the button is clicked
            JOptionPane.showMessageDialog(mainPanel, "check clicked");
        });

        solve.addActionListener(e -> {
            // Code to be executed when the button is clicked
            JOptionPane.showMessageDialog(mainPanel, "solve clicked");
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Nonograms");
            frame.setContentPane(new app().mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
