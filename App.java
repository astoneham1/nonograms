import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Arrays;
import java.util.Map;

public class App {
    // COLORS
    private LinkedHashMap<Integer, String> colors = new LinkedHashMap<Integer, String>();

    // STATE
    private int selectedColor = 0;
    private JButton selectedButton = null;

    // MISC
    private boolean isMouseDown = false;

    // UI COMPONENTS
    private JPanel mainPanel;
    private JPanel colorGuide;
    private JPanel focus;
    private JPanel grid;
    private JPanel controls;
    private JPanel rowCluePanel;
    private JPanel columnCluePanel;

    private JButton loadFile;
    private JButton clear;
    private JButton check;
    private JButton save;
    private JButton undo;
    // method for the user to load a file

    private boolean isSaved = false;

    // GRIDS
    private Grid puzzleGrid;
    private Grid userGrid;

    public App() {
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
            userGrid.clearAllMoves();
            JOptionPane.showMessageDialog(mainPanel, "All cells reset to unknown");
        });

        save.addActionListener(e -> {
            userGrid.saveMoves();
            JOptionPane.showMessageDialog(mainPanel, "save clicked");
            isSaved = true;
        });

        undo.addActionListener(e -> {
            userGrid.undoMoves();
            buildGrid(puzzleGrid.rows, puzzleGrid.columns);
            // need to add a method that updates the visual grid at this point to ensure that it reflects the userGrid
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
        rowCluePanel = new JPanel();
        columnCluePanel = new JPanel();

        focus = new JPanel(new BorderLayout());
        focus.setBackground(new Color(0xD2D2D2));

        grid = new JPanel();
        grid.setLayout(new GridLayout(1, 1));

        focus.add(grid, BorderLayout.CENTER);
        focus.add(rowCluePanel, BorderLayout.WEST);
        focus.add(columnCluePanel, BorderLayout.NORTH);

        mainPanel.add(focus, BorderLayout.CENTER);

        // bottom of screen controls
        controls = new JPanel();
        controls.setLayout(new GridLayout(1, 4));

        loadFile = new JButton("Load Puzzle");
        clear = new JButton("Clear");
        check = new JButton("Check");
        save = new JButton("Save");
        undo = new JButton("Undo");

        controls.add(loadFile);
        controls.add(clear);
        controls.add(check);
        controls.add(save);
        controls.add(undo);

        mainPanel.add(controls, BorderLayout.SOUTH);
    }

    public void loadGamePuzzle() {
        File puzzleDir = new File("Jsons");
        File[] jsonFiles = puzzleDir.listFiles((dir, name) -> name.endsWith(".json"));
        String[] options = Arrays.stream(jsonFiles).map(File::getName).toArray(String[]::new);

        String selected = (String) JOptionPane.showInputDialog(
                mainPanel,
                "Select a puzzle to load:",
                "Puzzle Selector",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        if (selected != null) {
            File selectedFile = new File(puzzleDir, selected);

            Parser parser = new Parser();
            try {
                puzzleGrid = parser.getGrid(selectedFile.getAbsolutePath());
            } catch (JsonFormatException e) {
                System.exit(1);
            } catch (FileNotFoundException e) {
                System.exit(1);
            }
            userGrid = new Grid(puzzleGrid.rows, puzzleGrid.columns, "Moves/tbd.json"); //need to define a path for selecting a file

            int rows = puzzleGrid.rows;
            int cols = puzzleGrid.columns;

            ArrayList<Clue> rowClues = puzzleGrid.rowClues;
            ArrayList<Clue> columnClues = puzzleGrid.columnClues;

            this.colors = Colours.colours;

            displayColors();
            buildGrid(rows, cols);
            displayClues(rowClues, columnClues);
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
                default -> "";
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
                final int row = i;
                final int col = j;
                JButton cell = new JButton();
                cell.setOpaque(true);
                cell.setBackground(Color.decode(colors.get(userGrid.grid[i][j])));

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
                        userGrid.updateMove(row, col, selectedColor);
                        isSaved = false;
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

    public void displayClues(ArrayList<Clue> rowClues, ArrayList<Clue> columnClues) {
        int rows = rowClues.size();
        int columns = columnClues.size();

        rowCluePanel.removeAll();
        columnCluePanel.removeAll();

        rowCluePanel.setLayout(new GridLayout(rows, 1));
        for (Clue clue : rowClues) {
            JPanel clueRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 2));
            ArrayList<Integer> counts = clue.getCounts();
            ArrayList<Integer> clueColor = clue.getColours();

            for (int i = 0; i < counts.size(); i++) {
                JLabel clueLabel = new JLabel(String.valueOf(counts.get(i)));
                int colorKey = clueColor.get(i);
                String colorCode = colors.getOrDefault(colorKey, "#000000"); // fallback to black
                clueLabel.setForeground(Color.decode(colorCode));
                clueRow.add(clueLabel);
            }

            rowCluePanel.add(clueRow);
        }

        columnCluePanel.setLayout(new GridLayout(1, columns));
        for (Clue clue : columnClues) {
            JPanel clueColumn = new JPanel();
            clueColumn.setLayout(new BoxLayout(clueColumn, BoxLayout.Y_AXIS));
            ArrayList<Integer> counts = clue.getCounts();
            ArrayList<Integer> clueColor = clue.getColours();

            for (int i = 0; i < counts.size(); i++) {
                JLabel clueLabel = new JLabel(String.valueOf(counts.get(i)));
                int colorKey = clueColor.get(i);
                String colorCode = colors.getOrDefault(colorKey, "#000000"); // fallback to black
                clueLabel.setForeground(Color.decode(colorCode));
                clueColumn.add(clueLabel);
            }

            columnCluePanel.add(clueColumn);
        }

        rowCluePanel.revalidate();
        rowCluePanel.repaint();
        columnCluePanel.revalidate();
        columnCluePanel.repaint();
    }

    public void cellClicked(JButton cell) {
        cell.putClientProperty("state", this.selectedColor);
        cell.setBackground(Color.decode(colors.get(this.selectedColor)));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            App a = new App();
            JFrame frame = new JFrame("Nonograms");
            frame.setContentPane(a.mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 800);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}