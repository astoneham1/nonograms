import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.io.File;
import java.util.Locale;

// to remove the .form dependency (required to allow this run on anything except intellij idea:
//

//COLOR NUMBERS:
//0 - UNKNOWN
//1 - EMPTY (white)
//2 - BLACK
//3 -

public class app {
    private final Color COLOR_UNKNOWN_1 = new Color(136, 142, 152);
    private final Color COLOR_EMPTY_2 = new Color(228, 240, 255);
    private final Color COLOR_BLACK_3 = new Color(43, 45, 48);


    private JPanel mainPanel;
    private JLabel titleText;
    private JPanel controls;
    private JButton loadFile;
    private JButton check;
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
    private JButton reset;
    private JButton solve;

    public app() {
        loadFile.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainPanel, "load clicked");

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose a puzzle (JSON file format)");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JSON files", "json"));

            int result = fileChooser.showOpenDialog(mainPanel);
            if (result == JFileChooser.APPROVE_OPTION) {
                File puzzleFile = fileChooser.getSelectedFile();

                // GET PARSED JSON

                // TEMP VALUES
                int rows = 5;
                int cols = 10;

                buildGrid(rows, cols);
                JOptionPane.showMessageDialog(mainPanel, "loaded puzzle");
            }
        });

        check.addActionListener(e -> {
            // Code to be executed when the button is clicked
            JOptionPane.showMessageDialog(mainPanel, "check clicked");
        });

        reset.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainPanel, "reset clicked");

            for (Component c : grid.getComponents()) {
                if (c instanceof JButton button) {
                    button.setBackground(COLOR_UNKNOWN_1); // resets color
                    button.putClientProperty("state", 0); // resets state to 0 (unknown)
                }
            }

            JOptionPane.showMessageDialog(mainPanel, "reset puzzle");
        });

        solve.addActionListener(e -> {
            // Code to be executed when the button is clicked
            JOptionPane.showMessageDialog(mainPanel, "solve clicked");
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void buildGrid(int rows, int columns) {
        grid.removeAll(); // clear grid of anything previous
        grid.setLayout(new GridLayout(rows, columns));

        for (int i = 0; i < rows; i++) { // repeat through rows
            for (int j = 0; j < columns; j++) { // repeat through columns
                JButton cell = new JButton();
                cell.setOpaque(true);
                cell.setBackground(COLOR_UNKNOWN_1); // set colour to the unknown colour
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cell.putClientProperty("state", 0); // set state to unknown

                cell.addActionListener(e -> {
                    int state = (int) cell.getClientProperty("state");
                    int nextState = (state + 1) % 3;
                    cell.putClientProperty("state", nextState);

                    switch (nextState) {
                        case 0: // unknown
                            cell.setBackground(COLOR_UNKNOWN_1);
                            break;
                        case 1: // empty (white)
                            cell.setBackground(COLOR_EMPTY_2);
                            break;
                        case 2: // black
                            cell.setBackground(COLOR_BLACK_3);
                            break;
                    }
                });

                grid.add(cell);
            }
        }

        grid.revalidate();
        grid.repaint();
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