import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;

// NOTE TO AMPS1:
// to remove the .form dependency (required to allow this run on anything except intellij idea):
// within IJ: settings -> editors -> GUI designer
// set generate gui into: java source code on compilation
// neaten up code
// this will be done when the code is near finished

// PARSER SHOULD RETURN SOMETHING LIKE THIS IDEALLY
//public class PuzzleData {
//    public Map<String, Color> states;
//    public List<List<Clue>> rows;
//    public List<List<Clue>> columns;
// STUFF ABOUT HINTS
//}

//COLOR NUMBERS:
//0 - UNKNOWN
//1 - EMPTY (white)
//2 - BLACK
//3 -

public class app {
    // COLORS
    private final Map<String, Color> colors; // ensure this is a linked hashmap


    // OBJECTS ON SCREEN
    private JPanel mainPanel;
    private JLabel titleText;
    private JPanel controls;
    private JButton loadFile;
    private JButton check;
    private JPanel grid;
    private JPanel colorGuide;
    private JButton reset;
    private JButton save;
    private JButton color0;
    private JButton color1;
    private JButton color2;

    // CONSTRUCTOR
    public app(Map<String, Color> colors) {
        this.colors = colors;

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

                //try {
                    // call method to parse JSON by passing in puzzleFile
                    // get color states from the data and create a new object
                    //app newApp = new app(newColors);
                    // get grid size from puzzle

                    // get grid size from puzzle json

                    // temp values
                    int rows = 5;
                    int cols = 10;

                    //newApp.buildGrid(rows, cols);
                    JOptionPane.showMessageDialog(mainPanel, "loaded puzzle");
                //} catch (IOException ex) {
                //    ex.printStackTrace();
                //JOptionPane.showMessageDialog(mainPanel, "error loading puzzle");
                //}
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
                    button.setBackground(colors.get("UNKNOWN")); // resets color
                    button.putClientProperty("state", 0); // resets state to 0 (unknown)
                }
            }

            JOptionPane.showMessageDialog(mainPanel, "reset puzzle");
        });

        save.addActionListener(e -> {
            // Code to be executed when the button is clicked
            JOptionPane.showMessageDialog(mainPanel, "save clicked");
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    // builds the grid on screen
    public void buildGrid(int rows, int columns) {
        grid.removeAll(); // clear grid of anything previous
        grid.setLayout(new GridLayout(rows, columns));

        for (int i = 0; i < rows; i++) { // repeat through rows
            for (int j = 0; j < columns; j++) { // repeat through columns
                JButton cell = new JButton();
                cell.setOpaque(true);
                cell.setBackground(colors.get("UNKNOWN")); // set colour to the unknown colour
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cell.putClientProperty("state", 0); // set state to unknown

                List<String> colorKeys = new ArrayList<>(colors.keySet());

                cell.addActionListener(e -> {
                    int state = (int) cell.getClientProperty("state");
                    int nextState = (state + 1) % colorKeys.size();
                    cell.putClientProperty("state", nextState);

                    String colorName = colorKeys.get(nextState);
                    cell.setBackground(colors.get(colorName));
                });

                grid.add(cell);
            }
        }

        grid.revalidate();
        grid.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // create default colors (will be replaced when a puzzle is loaded)
            Map<String, Color> defaultColors = new LinkedHashMap<>();
            defaultColors.put("UNKNOWN", Color.decode("#ECECEC"));
            defaultColors.put("EMPTY", Color.decode("#FFFFFF"));

            JFrame frame = new JFrame("Nonograms");
            frame.setContentPane(new app(defaultColors).mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

}