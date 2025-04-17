import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.*;
import java.util.List;

public class App {
    // COLORS
    private final Map<Integer, String> colors;

    // STATE
    private int selectedColor = 0;
    private JButton selectedButton = null;

    // MISC
    private boolean isMouseDown = false;

    public App(Map<Integer, String> colors) {
        this.colors = colors;
        setupUI();
        loadGamePuzzle();

        loadFile.addActionListener(e -> loadGamePuzzle());

        check.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainPanel, "check clicked");
        });

        clear.addActionListener(e -> {
            for (Component c : grid.getComponents()) {
                if (c instanceof JButton button) {
                    button.setBackground(Color.decode(colors.get(0)));
                    button.putClientProperty("state", 0);
                }
            }
            JOptionPane.showMessageDialog(mainPanel, "clear puzzle");
        });

        save.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainPanel, "save clicked");
        });
    }

    private void setupUI() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // top of screen: color guide
        colorGuide = new JPanel();
        colorGuide.setLayout(new GridLayout(1, 1));
        colorGuide.setBackground(new Color(0xF0F0F0));
        mainPanel.add(colorGuide, BorderLayout.NORTH);

        // centre focus area with grid and clues
        focus = new JPanel(new BorderLayout());
        focus.setBackground(new Color(0xD2D2D2));

        grid = new JPanel();
        grid.setLayout(new GridLayout(1, 1));
        focus.add(grid, BorderLayout.CENTER);
        mainPanel.add(focus, BorderLayout.CENTER);

        // bottom of screen controls
        controls = new JPanel();
        controls.setLayout(new GridLayout(1, 4));

        loadFile = new JButton("Load Puzzle");
        clear = new JButton("Clear");
        check = new JButton("Check");
        save = new JButton("Save");

        controls.add(loadFile);
        controls.add(clear);
        controls.add(check);
        controls.add(save);

        mainPanel.add(controls, BorderLayout.SOUTH);
    }

    public void loadGamePuzzle() {
        JOptionPane.showMessageDialog(mainPanel, "load clicked");

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose a puzzle (JSON file format)");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JSON files", "json"));

        int result = fileChooser.showOpenDialog(mainPanel);
        if (result == JFileChooser.APPROVE_OPTION) {
            File puzzleFile = fileChooser.getSelectedFile();

            int rows = 7;
            int cols = 9;

            displayColors();
            buildGrid(rows, cols);
            JOptionPane.showMessageDialog(mainPanel, "loaded puzzle");
        }
    }

    public void displayColors() {
        colorGuide.removeAll();
        colorGuide.setLayout(new GridLayout(1, this.colors.size()));

        for (Map.Entry<Integer, String> entry : this.colors.entrySet()) {
            int key = entry.getKey();
            String colorCode = entry.getValue();

            String label = switch (key) {
                case 0 -> "UNKNOWN";
                case 1 -> "EMPTY";
                default -> String.valueOf(key);
            };

            JButton colorButton = new JButton(label);
            Color color = Color.decode(colorCode);
            colorButton.setBackground(color);
            colorButton.setOpaque(true);
            colorButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            colorButton.addActionListener(e -> {
                this.selectedColor = key;

                if (selectedButton != null) {
                    selectedButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                }

                colorButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                selectedButton = colorButton;
            });

            colorGuide.add(colorButton);
        }

        // preselect UNKNOWN
        for (Component c : colorGuide.getComponents()) {
            if (c instanceof JButton b && "UNKNOWN".equals(b.getText())) {
                b.doClick();
            }
        }

        colorGuide.revalidate();
        colorGuide.repaint();
    }

    public void buildGrid(int rows, int columns) {
        grid.removeAll();
        grid.setLayout(new GridLayout(rows, columns));

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                JButton cell = new JButton();
                cell.setOpaque(true);
                cell.setBackground(Color.decode(colors.get(0)));

                // Borders
                if (((i + 1) % 5 == 0) && ((j + 1) % 5 == 0)) {
                    cell.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 2, Color.BLACK));
                } else if ((i + 1) % 5 == 0) {
                    cell.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 1, Color.BLACK));
                } else if ((j + 1) % 5 == 0) {
                    cell.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 2, Color.BLACK));
                } else {
                    cell.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK));
                }

                cell.putClientProperty("state", 0);

                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        isMouseDown = true;
                        cellClicked((JButton) e.getSource());
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        isMouseDown = false;
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (isMouseDown) {
                            cellClicked((JButton) e.getSource());
                        }
                    }
                });

                grid.add(cell);
            }
        }

        grid.revalidate();
        grid.repaint();
    }

    public void cellClicked(JButton cell) {
        List<Integer> colorKeys = new ArrayList<>(colors.keySet());
        cell.putClientProperty("state", this.selectedColor);
        cell.setBackground(Color.decode(colors.get(this.selectedColor)));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Map<Integer, String> defaultColors = new LinkedHashMap<>();
            defaultColors.put(0, "#c2bebe");
            defaultColors.put(1, "#FFFFFF");
            defaultColors.put(2, "#32a852");
            defaultColors.put(3, "#4a32a8");

            JFrame frame = new JFrame("Nonograms");
            App a = new App(defaultColors);
            frame.setContentPane(a.mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 800);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
