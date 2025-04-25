import javax.json.stream.JsonParsingException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

public class App {
    // COLORS
    private LinkedHashMap<Integer, String> colors = new LinkedHashMap<Integer, String>();

    // STATES
    private int selectedColor = 0;
    private JButton selectedButton = null;
    private boolean isMouseDown = false;
    private boolean isSaved = false;
    private boolean undoAllowed = false;

    // UI COMPONENTS
    private JPanel mainPanel;
    private JPanel colorGuide;
    private JPanel gameArea;
    private JPanel grid;
    private JPanel controls;
    private JPanel rowCluePanel;
    private JPanel columnCluePanel;

    private JLabel loadedName;
    private JButton loadFile;
    private JButton reset;
    private JButton check;
    private JButton undo;
    private JButton solve;
    private JButton save;

    // GRIDS
    private Grid puzzleGrid;
    private Grid userGrid;
    private String currentGridName;

    public App() {
        setupUI();
        openGameLauncher();
    }

    private void setupUI() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // top of screen: color guide
        colorGuide = new JPanel();
        colorGuide.setLayout(new GridLayout(1, 1));
        colorGuide.setBackground(new Color(0xF0F0F0));
        mainPanel.add(colorGuide, BorderLayout.NORTH);

        // centre gameArea area with grid
        gameArea = new JPanel(new BorderLayout());
        gameArea.setBackground(new Color(0xD2D2D2));

        grid = new JPanel();
        grid.setLayout(new GridLayout(1, 1));

        gameArea.add(grid, BorderLayout.CENTER);

        mainPanel.add(gameArea, BorderLayout.CENTER);

        // bottom of screen controls
        controls = new JPanel();
        controls.setLayout(new GridLayout(1, 4));

        loadedName = new JLabel("", SwingConstants.CENTER);
        loadFile = new JButton("Load");
        reset = new JButton("Reset");
        check = new JButton("Check");
        undo = new JButton("Undo");
        solve = new JButton("Solve");
        save = new JButton("Save");

        controls.add(loadedName);
        controls.add(loadFile);
        controls.add(reset);
        controls.add(check);
        controls.add(undo);
        controls.add(solve);
        controls.add(save);

        mainPanel.add(controls, BorderLayout.SOUTH);

        // hotkeys
        loadFile.setMnemonic(KeyEvent.VK_O);
        reset.setMnemonic(KeyEvent.VK_R);
        check.setMnemonic(KeyEvent.VK_C);
        undo.setMnemonic(KeyEvent.VK_U);
        solve.setMnemonic(KeyEvent.VK_V);
        save.setMnemonic(KeyEvent.VK_S);

        // action listeners
        loadFile.addActionListener(e -> {
            int response = askSave();
            if (response == JOptionPane.YES_OPTION) {
                saveGrid();
            }
            loadGamePuzzle("fromButton");
        });

        check.addActionListener(e -> {
            // Create a modified grid without the clue cells for checking
            int[][] checkGrid = new int[puzzleGrid.rows][puzzleGrid.columns];
            for (int i = 0; i < puzzleGrid.rows; i++) {
                for (int j = 0; j < puzzleGrid.columns; j++) {
                    checkGrid[i][j] = userGrid.grid[i][j];
                }
            }

            Checker c = new Checker();
            ArrayList<ArrayList<Integer>> a = c.checkNonogram(checkGrid, puzzleGrid.rowClues, puzzleGrid.columnClues);
            String message = c.getMessage(a, puzzleGrid.rowClues.size(), puzzleGrid.columnClues.size());
            JOptionPane.showMessageDialog(mainPanel, message);
        });

        reset.addActionListener(e -> {
            for (Component c : grid.getComponents()) {
                if (c instanceof JButton button) {
                    button.setBackground(Color.decode(colors.get(0)));
                    button.putClientProperty("state", 0);
                }
            }
            userGrid.clearAllMoves();
            disableUndo();

            JOptionPane.showMessageDialog(mainPanel, "All cells reset to unknown");
        });

        undo.addActionListener(e -> {
            if (undoAllowed) {
                userGrid.undoMoves();
                buildGrid(puzzleGrid.rows, puzzleGrid.columns);
            }

            if (userGrid.moves.size() == 0) {
                disableUndo();
            }

            if (isSaved) {
                allowSave();
            }
        });

        solve.addActionListener(e -> solvePuzzle());

        save.addActionListener(e -> saveGrid());
    }

    public void openGameLauncher() {
        String[] options = { "Play", "How to Play" };
        int selection = JOptionPane.showOptionDialog(
                null,
                "Click play or learn how to!",
                "Welcome to Nonograms",
                0,
                3,
                null,
                options,
                null);

        if (selection == 0) {
            loadGamePuzzle("fromStart");
        } else if (selection == 1) {
            JOptionPane.showMessageDialog(
                    null,
                    "Welcome to Nonograms. Your goal is to reveal a hidden\nimage by filling in squares of a grid that correspond to\nclues above/to the side of the column/row. The numbers in\nthe clues represent the length of consecutively filled\nblocks, eg a 5 2 means a block of 5, then a gap of some\namount, then a block of 2. As you fill in squares, use\nprocess of elimination to deduce which squares are\nalso definitely filled or empty. Good luck and have fun!.\n\nHotkeys:\nALT + L: Load Puzzle\nALT + R: Reset Grid\nALT + C: Check Puzzle\nALT + S: Save Puzzle\nALT + U: Undo\n\n Colours can be selected by clicking 1,2,3 etc for each colour");

            openGameLauncher();
        } else {
            System.exit(0);
        }
    }

    public void loadGamePuzzle(String source) {
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

        if (!(selected == null)) {
            File selectedFile = new File(puzzleDir, selected);

            Parser parser = new Parser();
            try {
                puzzleGrid = parser.getGrid(selectedFile.getAbsolutePath());

                // find out if the user wants to load an existing grid or create a new one
                File gridDir = new File("Moves");
                File[] gridFiles = gridDir.listFiles((dir, name) -> name.endsWith(".json"));
                String[] grids = Arrays.stream(gridFiles).map(File::getName).toArray(String[]::new);
                grids = Stream.concat(Arrays.stream(grids), Stream.of("New Grid")).toArray(String[]::new);

                String newGridName = "";

                String returnValue = (String) JOptionPane.showInputDialog(
                        mainPanel,
                        "Load a saved grid or make a new one",
                        "Grid Selector",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        grids,
                        grids[grids.length - 1]);

                if (returnValue != null) {
                    // if the user chooses to make a new grid ask them for a name
                    if (returnValue.equals("New Grid")) {
                        returnValue = JOptionPane.showInputDialog(
                                mainPanel,
                                "Enter a name for the new grid:",
                                "New Grid Name",
                                JOptionPane.PLAIN_MESSAGE);

                        newGridName = returnValue.concat(".json");
                    }

                    // create an empty grid with the specified filename
                    if (newGridName != "") {
                        userGrid = new Grid(puzzleGrid.rows, puzzleGrid.columns, "Moves/" + newGridName);
                        currentGridName = newGridName;
                    } else {
                        userGrid = new Grid(puzzleGrid.rows, puzzleGrid.columns, "Moves/" + returnValue);
                        currentGridName = returnValue;
                    }

                    // if the user clicked to load an existing grid then update the empty grid to be
                    // loaded
                    if (newGridName == "") {
                        userGrid.loadMoves("Moves/" + returnValue);
                    }

                    if (userGrid.moves.size() > 0) {
                        undoAllowed = true;
                    } else {
                        disableUndo();
                    }

                    ArrayList<Clue> rowClues = puzzleGrid.rowClues;
                    ArrayList<Clue> columnClues = puzzleGrid.columnClues;

                    loadedName.setText(currentGridName.substring(0, currentGridName.length() - 5));
                    this.colors = Colours.colours;

                    displayColors();
                    buildGrid(puzzleGrid.rows, puzzleGrid.columns);
                    // displayClues(rowClues, columnClues);
                } else if (source.equals("fromStart")) {
                    System.exit(0);
                }
            } catch (JsonFormatException e) {
                JOptionPane.showMessageDialog(mainPanel,
                        "Error: JSON file is incorrectly formatted. Please choose a different puzzle.");
                restartApp(source);
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(mainPanel,
                        "Error: File was not found. Please choose a different puzzle.");
                restartApp(source);
            } catch (JsonParsingException e) {
                JOptionPane.showMessageDialog(mainPanel,
                        "Error: Could not parse JSON file. Please choose a different puzzle.");
                restartApp(source);
            }

        } else if (source.equals("fromStart")) {
            System.exit(0);
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
                default -> " ";
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

            // keybinding to the hashmap value (0,1,2,3 etc)
            InputMap inputMap = colorGuide.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            ActionMap actionMap = colorGuide.getActionMap();

            inputMap.put(KeyStroke.getKeyStroke(String.valueOf(key)), "clicked" + key);
            actionMap.put("clicked" + key, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    colorButton.doClick();
                }
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
        // Add +1 for column clues row and +1 for row clues column
        grid.setLayout(new GridLayout(rows + 1, columns + 1));

        // Much smaller cell size - reduce grid cells significantly
        Dimension cellSize = new Dimension(20, 20); // Smaller grid cells

        // Make clue panels larger with fixed minimum width/height
        int minClueWidth = 50; // Minimum width for row clue panels
        int minClueHeight = 40; // Minimum height for column clue cells

        // Add empty corner cell (top-left corner)
        JPanel cornerCell = new JPanel();
        cornerCell.setOpaque(false);
        cornerCell.setBorder(BorderFactory.createEmptyBorder());
        cornerCell.setPreferredSize(new Dimension(minClueWidth, minClueHeight));
        grid.add(cornerCell);

        // Add column clues (top row)
        for (int j = 0; j < columns; j++) {
            JPanel cluePanel = new JPanel();
            cluePanel.setLayout(new BoxLayout(cluePanel, BoxLayout.Y_AXIS));
            cluePanel.setOpaque(false);
            cluePanel.setBorder(BorderFactory.createEmptyBorder());
            cluePanel.setPreferredSize(new Dimension(cellSize.width, minClueHeight));

            // Get column clue and add labels for each clue number
            Clue columnClue = puzzleGrid.columnClues.get(j);
            ArrayList<Integer> counts = columnClue.getCounts();
            ArrayList<Integer> clueColor = columnClue.getColours();

            // Use larger font for clues
            Font clueFont = new Font("Arial", Font.BOLD, 12);

            for (int i = 0; i < counts.size(); i++) {
                JLabel clueLabel = new JLabel(String.valueOf(counts.get(i)));
                clueLabel.setFont(clueFont);
                clueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                int colorKey = clueColor.get(i);
                String colorCode = colors.getOrDefault(colorKey, "#000000");
                clueLabel.setForeground(Color.decode(colorCode));

                cluePanel.add(clueLabel);
            }

            grid.add(cluePanel);
        }

        // For each row
        for (int i = 0; i < rows; i++) {
            final int row = i;

            // Add row clue (first column of each row)
            JPanel rowCluePanel = new JPanel();
            rowCluePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 2, 2));
            rowCluePanel.setOpaque(false);
            rowCluePanel.setBorder(BorderFactory.createEmptyBorder());
            rowCluePanel.setPreferredSize(new Dimension(minClueWidth, cellSize.height));
            rowCluePanel.setMinimumSize(new Dimension(minClueWidth, cellSize.height));

            // Get row clue and add labels for each clue number
            Clue rowClue = puzzleGrid.rowClues.get(i);
            ArrayList<Integer> counts = rowClue.getCounts();
            ArrayList<Integer> clueColor = rowClue.getColours();

            // Use larger font for clues
            Font clueFont = new Font("Arial", Font.BOLD, 12);

            for (int k = 0; k < counts.size(); k++) {
                String textForLabel = "";
                if (k < counts.size() - 1) {
                    textForLabel = String.valueOf(counts.get(k)) + ",";
                } else {
                    textForLabel = String.valueOf(counts.get(k));
                }

                JLabel clueLabel = new JLabel(textForLabel);
                clueLabel.setFont(clueFont);
                int colorKey = clueColor.get(k);
                String colorCode = colors.getOrDefault(colorKey, "#000000");
                clueLabel.setForeground(Color.decode(colorCode));
                rowCluePanel.add(clueLabel);
            }

            grid.add(rowCluePanel);

            // Add cells for this row
            for (int j = 0; j < columns; j++) {
                final int col = j;
                JButton cell = new JButton();
                cell.setOpaque(true);
                cell.setPreferredSize(cellSize);
                cell.setMinimumSize(cellSize);
                cell.setMaximumSize(cellSize);
                // Remove any text or margins from button
                cell.setMargin(new Insets(0, 0, 0, 0));
                cell.setBorderPainted(true);
                cell.setContentAreaFilled(true);
                cell.setFocusPainted(false);

                // Set cell color based on the user grid
                if (colors.get(userGrid.grid[i][j]) == null) {
                    userGrid.grid[i][j] = 0;
                    cell.putClientProperty("state", 0);
                }
                cell.setBackground(Color.decode(colors.get(userGrid.grid[i][j])));

                // Apply appropriate borders
                if (((i + 1) % 5 == 0) && ((j + 1) % 5 == 0)) {
                    cell.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 2, Color.BLACK));
                } else if ((i + 1) % 5 == 0) {
                    cell.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 1, Color.BLACK));
                } else if ((j + 1) % 5 == 0) {
                    cell.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 2, Color.BLACK));
                } else {
                    cell.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK));
                }

                cell.putClientProperty("state", userGrid.grid[i][j]);

                // Add cell click handling - your existing mouse listener code
                cell.addMouseListener(new MouseAdapter() {
                    // Your existing mouse listener implementation
                    @Override
                    public void mousePressed(MouseEvent e) {
                        isMouseDown = true;
                        cellClicked((JButton) e.getSource());
                        userGrid.updateMove(row, col, selectedColor);

                        if (isSaved) {
                            allowSave();
                        }

                        if (!undoAllowed) {
                            allowUndo();
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        isMouseDown = false;
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (isMouseDown) {
                            cellClicked((JButton) e.getSource());
                            userGrid.updateMove(row, col, selectedColor);
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
        cell.putClientProperty("state", this.selectedColor);
        cell.setBackground(Color.decode(colors.get(this.selectedColor)));
    }

    public void restartApp(String source) {
        if (source.equals("fromStart")) {
            loadGamePuzzle("fromStart");
        }
    }

    public void allowUndo() {
        undo.setBackground(check.getBackground());
        undo.setEnabled(true);
        undoAllowed = true;
    }

    public void disableUndo() {
        undo.setBackground(Color.GRAY);
        undo.setEnabled(false);
        undoAllowed = false;
    }

    public void allowSave() {
        save.setBackground(check.getBackground());
        save.setEnabled(true);
        isSaved = false;
    }

    public void disableSave() {
        save.setBackground(Color.GRAY);
        save.setEnabled(false);
        isSaved = true;
    }

    public int askSave() {
        int response = JOptionPane.showConfirmDialog(mainPanel,
                "Do you want to save your progress before exiting?",
                "Exit Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        return response;
    }

    public void saveGrid() {
        userGrid.saveMoves();
        JOptionPane.showMessageDialog(mainPanel, "Saved progress to grid: " + currentGridName);
        disableSave();
    }

    public void solvePuzzle() {
        int response = JOptionPane.showConfirmDialog(mainPanel,
                "Are you sure you want the puzzle to be solved?",
                "Solve Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            // solver
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            App a = new App();
            JFrame frame = new JFrame("Nonograms");
            frame.setContentPane(a.mainPanel);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.setSize(1000, 800);
            frame.setMinimumSize(new Dimension(800, 600));
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);

            // handle closing the window by asking the user if they want to save
            frame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    int response = a.askSave();
                    if (response == JOptionPane.YES_OPTION) {
                        a.saveGrid();
                    }
                    if (response == JOptionPane.NO_OPTION || response == JOptionPane.YES_OPTION) {
                        frame.dispose();
                    }
                    // ensures if the user clicks X it keeps them in the game
                }
            });
            frame.setVisible(true);
        });
    }
}