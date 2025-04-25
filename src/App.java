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

    private JLabel loadedName;
    private JLabel timerText;

    private JButton loadFile;
    private JButton reset;
    private JButton check;
    private JButton undo;
    private JButton solve;
    private JButton save;

    // GRIDS
    private Grid puzzleGrid;
    private Grid userGrid;
    private Grid solverGrid;
    private String currentGridName;

    public App() {
        setupUI();
        openGameLauncher();
    }

    private void setupUI() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Top of screen: color guide
        colorGuide = new JPanel();
        colorGuide.setLayout(new GridLayout(1, 1));
        colorGuide.setBackground(new Color(204, 204, 204));
        mainPanel.add(colorGuide, BorderLayout.NORTH);

        // Centre gameArea area with grid and clues
        gameArea = new JPanel(new BorderLayout());
        gameArea.setBackground(new Color(210, 210, 210));

        // The grid panel itself, using GridBagLayout internally for clues and cells
        grid = new JPanel(new GridBagLayout());

        JPanel centeringPanel = new JPanel(new GridBagLayout());
        centeringPanel.setBackground(new Color(210, 210, 210));

        GridBagConstraints gbcCentering = new GridBagConstraints();
        gbcCentering.gridx = 0;
        gbcCentering.gridy = 0;
        gbcCentering.weightx = 1.0;
        gbcCentering.weighty = 1.0;
        gbcCentering.fill = GridBagConstraints.NONE;

        centeringPanel.add(grid, gbcCentering);

        gameArea.add(centeringPanel, BorderLayout.CENTER);

        mainPanel.add(gameArea, BorderLayout.CENTER);

        // Bottom of screen controls
        controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        controls.setBackground(new Color(204, 204, 204));

        controls.add(Box.createVerticalStrut(5));

        Font buttonFont = new Font("SansSerif", Font.PLAIN, 14);

        loadedName = new JLabel("", SwingConstants.CENTER);
        loadedName.setFont(new Font("SansSerif", Font.BOLD, 14));

        loadFile = createStyledButton("Load", buttonFont);
        reset = createStyledButton("Reset", buttonFont);
        check = createStyledButton("Check", buttonFont);
        undo = createStyledButton("Undo", buttonFont);
        solve = createStyledButton("Solve", buttonFont);
        save = createStyledButton("Save", buttonFont);

        timerText = new JLabel("", SwingConstants.RIGHT);
        timerText.setFont(new Font("SansSerif", Font.BOLD, 14));
        timerText.setForeground(Color.GREEN);
        timerText.setVisible(false);

        loadedName.setAlignmentX(Component.CENTER_ALIGNMENT);
        controls.add(loadedName);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(204, 204, 204));

        buttonPanel.add(loadFile);
        buttonPanel.add(reset);
        buttonPanel.add(check);
        buttonPanel.add(undo);
        buttonPanel.add(solve);
        buttonPanel.add(save);
        buttonPanel.add(timerText);

        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        controls.add(buttonPanel);

        mainPanel.add(controls, BorderLayout.SOUTH);

        // Hotkeys
        loadFile.setMnemonic(KeyEvent.VK_L);
        reset.setMnemonic(KeyEvent.VK_R);
        check.setMnemonic(KeyEvent.VK_C);
        undo.setMnemonic(KeyEvent.VK_U);
        solve.setMnemonic(KeyEvent.VK_V);
        save.setMnemonic(KeyEvent.VK_S);

        // Action listeners
        loadFile.addActionListener(e -> {
            if (!isSaved) {
                int response = askSave();
                if (response == JOptionPane.YES_OPTION) {
                    saveGrid();
                }

                if (response == JOptionPane.YES_OPTION || response == JOptionPane.NO_OPTION) {
                    loadGamePuzzle("fromButton");
                }
            } else {
                loadGamePuzzle("fromButton");
            }

        });

        check.addActionListener(e -> {
            // Create checker object, call the checkNonogram method and get the message to
            // output to the screen
            Checker c = new Checker();
            ArrayList<ArrayList<Integer>> a = c.checkNonogram(userGrid.grid, puzzleGrid.rowClues,
                    puzzleGrid.columnClues);
            String message = c.getMessage(a, puzzleGrid.rowClues.size(), puzzleGrid.columnClues.size());
            JOptionPane.showMessageDialog(mainPanel, message);

            if (message.contains("100.0%")) {
                showTemporaryText("Well done!");
            }
        });

        reset.addActionListener(e -> {
            // Reset the userGrid data and rebuild the grid UI
            userGrid.clearAllMoves();
            disableUndo();
            allowSave();
            buildGrid(puzzleGrid.rows, puzzleGrid.columns, userGrid); // Rebuild the grid UI

            showTemporaryText("Grid Reset");
        });

        undo.addActionListener(e -> {
            if (undoAllowed) {
                userGrid.undoMoves();
                buildGrid(puzzleGrid.rows, puzzleGrid.columns, userGrid); // Rebuild grid after undo
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

    private JButton createStyledButton(String text, Font font) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setFocusPainted(false);
        button.setBackground(new Color(224, 224, 224));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        return button;
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
                    "Welcome to Nonograms. Your goal is to reveal a hidden\nimage by filling in squares of a grid that correspond to\nclues above/to the side of the column/row. The numbers in\nthe clues represent the length of consecutively filled\nblocks. As you fill in squares, use process of elimination to\ndeduce which squares are also definitely filled or empty.\nGood luck and have fun!.\n\nHotkeys:\nALT + L: Load Puzzle\nALT + R: Reset Grid\nALT + C: Check Puzzle\nALT + S: Save Puzzle\nALT + U: Undo\nALT + V: Solve\n\n Colours can be selected by clicking 1,2,3 etc for each colour");

            openGameLauncher();
        } else {
            System.exit(0);
        }
    }

    public void loadGamePuzzle(String source) {
        File puzzleDir = new File("Jsons");
        File[] jsonFiles = puzzleDir.listFiles((dir, name) -> name.endsWith(".json"));
        String[] options = Arrays.stream(jsonFiles).map(File::getName)
                .map(fileName -> fileName.substring(0, fileName.length() - 5)).toArray(String[]::new);

        String selected = (String) JOptionPane.showInputDialog(
                mainPanel,
                "Select a puzzle to load:",
                "Puzzle Selector",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);

        if (!(selected == null)) {
            File selectedFile = new File(puzzleDir, selected + ".json");

            Parser parser = new Parser();
            try {
                puzzleGrid = parser.getGrid(selectedFile.getAbsolutePath());

                // Find out if the user wants to load an existing grid or create a new one
                File gridDir = new File("Moves");
                File[] gridFiles = gridDir.listFiles((dir, name) -> name.endsWith(".json"));
                String[] grids = Arrays.stream(gridFiles).map(File::getName)
                        .map(fileName -> fileName.substring(0, fileName.length() - 5)).toArray(String[]::new);
                grids = Stream.concat(Stream.of("New Grid"), Arrays.stream(grids)).toArray(String[]::new);

                boolean createNewGrid = false;

                String returnValue = (String) JOptionPane.showInputDialog(
                        mainPanel,
                        "Load a saved grid or make a new one",
                        "Grid Selector",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        grids,
                        grids[0]);

                if (returnValue != null) {
                    // If the user chooses to make a new grid ask them for a name
                    if (returnValue.equals("New Grid")) {
                        returnValue = JOptionPane.showInputDialog(
                                mainPanel,
                                "Enter a name for the new grid:",
                                "New Grid Name",
                                JOptionPane.PLAIN_MESSAGE);

                        createNewGrid = true;
                    }

                    currentGridName = returnValue + ".json";

                    // Create an empty grid with the specified filename
                    userGrid = new Grid(puzzleGrid.rows, puzzleGrid.columns, "Moves/" + currentGridName);

                    // If the user clicked to load an existing grid then update the empty grid to be
                    // loaded
                    if (!createNewGrid) {
                        userGrid.loadMoves("Moves/" + currentGridName);
                        disableSave(); // the user is loading a grid so has nothing to save yet
                    }

                    if (userGrid.moves.size() > 0) {
                        undoAllowed = true;
                    } else {
                        disableUndo();
                    }

                    loadedName.setText(
                            Parser.puzzleName + " • " + currentGridName.substring(0, currentGridName.length() - 5));
                    this.colors = Colours.colours;

                    displayColors();
                    buildGrid(puzzleGrid.rows, puzzleGrid.columns, userGrid);
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
        colorGuide.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

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

            colorButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.BLACK, 1),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)));

            colorButton.setForeground(Color.BLACK);

            colorButton.addActionListener(e -> {
                this.selectedColor = key;

                if (selectedButton != null) {
                    // Reset previously selected button's border
                    selectedButton.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(Color.BLACK, 1),
                            BorderFactory.createEmptyBorder(5, 15, 5, 15)));
                }

                // Set border of the newly selected button
                colorButton.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.BLACK, 3),
                        BorderFactory.createEmptyBorder(5, 15, 5, 15)));
                selectedButton = colorButton;
            });

            // Keybinding to the hashmap value (0,1,2,3 etc)
            InputMap inputMap = colorGuide.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            ActionMap actionMap = colorGuide.getActionMap();

            inputMap.put(KeyStroke.getKeyStroke(String.valueOf(key + 1)), "clicked" + key + 1);
            actionMap.put("clicked" + key + 1, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    colorButton.doClick();
                }
            });

            colorGuide.add(colorButton);
        }

        // preselect UNKNOWN (assuming key 0 is UNKNOWN)
        for (Component c : colorGuide.getComponents()) {
            if (c instanceof JButton b && "UNKNOWN".equals(b.getText())) {
                b.doClick();
                break; // Stop after finding UNKNOWN
            }
        }

        colorGuide.revalidate();
        colorGuide.repaint();
    }

    public void buildGrid(int rows, int columns, Grid gridName) {
        grid.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; // Components will fill their display area where possible
        gbc.insets = new Insets(0, 0, 0, 0);

        // Calculate cell size based on puzzle dimensions so we can max out the space
        // used on the screen
        int maxDim = Math.max(rows, columns);
        int calculatedCellSize = 30; // Default or minimum size

        if (maxDim <= 10) { // For very small puzzles
            calculatedCellSize = 50;
        } else if (maxDim <= 15) { // For medium-small puzzles
            calculatedCellSize = 40;
        } else { // For larger puzzles
            calculatedCellSize = 20;
        }

        // Top-Left Corner
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.1; // Weight for the column clue row
        gbc.weightx = 0.1; // Weight for the row clue column
        grid.add(new JPanel(), gbc);

        // Column Clues
        gbc.gridy = 0;
        gbc.weighty = 0.1;
        gbc.weightx = 1.0;

        for (int col = 0; col < columns; col++) {
            JPanel clueCell = new JPanel();
            clueCell.setLayout(new BoxLayout(clueCell, BoxLayout.Y_AXIS));
            clueCell.setAlignmentY(Component.TOP_ALIGNMENT);

            Clue clue = puzzleGrid.columnClues.get(col);
            ArrayList<Integer> counts = clue.getCounts();
            ArrayList<Integer> clueColor = clue.getColours();

            for (int i = 0; i < counts.size(); i++) {
                JLabel clueLabel = new JLabel(String.valueOf(counts.get(i)));
                int colorKey = clueColor.get(i);
                String colorCode = colors.getOrDefault(colorKey, "#000000");
                clueLabel.setForeground(Color.decode(colorCode));
                clueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                clueCell.add(clueLabel);
            }

            clueCell.add(Box.createVerticalGlue());

            gbc.gridx = col + 1;
            grid.add(clueCell, gbc);
        }

        // Row Clues
        gbc.gridx = 0;
        gbc.weightx = 0.1;
        gbc.weighty = 0.0;

        for (int i = 0; i < rows; i++) {
            JPanel clueRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 0));

            ArrayList<Integer> counts = puzzleGrid.rowClues.get(i).getCounts();
            ArrayList<Integer> clueColor = puzzleGrid.rowClues.get(i).getColours();

            for (int j = 0; j < counts.size(); j++) {
                String textForLabel = "";
                if (j < counts.size() - 1) {
                    textForLabel = String.valueOf(counts.get(j)) + ",";
                } else {
                    textForLabel = String.valueOf(counts.get(j));
                }
                JLabel clueLabel = new JLabel(textForLabel);
                int colorKey = clueColor.get(j);
                String colorCode = colors.getOrDefault(colorKey, "#000000");
                clueLabel.setForeground(Color.decode(colorCode));
                clueRow.add(clueLabel);
            }

            gbc.gridy = i + 1;
            grid.add(clueRow, gbc);
        }

        // Main Grid Cells
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                final int row = i;
                final int col = j;
                JButton cell = new JButton();
                cell.setOpaque(true);
                cell.setPreferredSize(new Dimension(calculatedCellSize, calculatedCellSize));

                if (colors.get(gridName.grid[i][j]) == null) {
                    gridName.grid[i][j] = 0;
                }
                cell.setBackground(Color.decode(colors.get(gridName.grid[i][j])));
                cell.putClientProperty("state", gridName.grid[i][j]);

                // Borders
                int top = (i % 5 == 0) ? 2 : 1; // Thicker top border every 5 rows (relative to puzzle grid)
                int left = (j % 5 == 0) ? 2 : 1; // Thicker left border every 5 columns (relative to puzzle grid)
                int bottom = ((i + 1) % 5 == 0 || i == rows - 1) ? 2 : 1; // Thicker bottom every 5 rows or at the last
                                                                          // row
                int right = ((j + 1) % 5 == 0 || j == columns - 1) ? 2 : 1; // Thicker right every 5 columns or at the
                                                                            // last column

                cell.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));

                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        isMouseDown = true;
                        cellClicked(cell);
                        userGrid.updateMove(row, col, selectedColor);

                        if (isSaved)
                            allowSave();
                        if (!undoAllowed)
                            allowUndo();
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        isMouseDown = false;
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (isMouseDown) {
                            cellClicked(cell);
                            userGrid.updateMove(row, col, selectedColor);
                        }
                    }
                });

                // Add the cell to the grid using GridBagConstraints
                gbc.gridx = j + 1; // Column position (shifted by 1 for row clues column)
                gbc.gridy = i + 1; // Row position (shifted by 1 for column clues row)
                grid.add(cell, gbc);
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
        } else if (source.equals("fromButton")) {
            loadGamePuzzle("fromButton");
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
                "Do you want to save your progress?",
                "Save Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        return response;
    }

    public void saveGrid() {
        userGrid.saveMoves();
        showTemporaryText("Saved!");
        disableSave();
    }

    public void solvePuzzle() {
        int response = JOptionPane.showConfirmDialog(mainPanel,
                "Are you sure you want the puzzle to be solved?",
                "Solve Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            SolverMain solver = new SolverMain(puzzleGrid);

            solverGrid = solver.solverGrid;
            buildGrid(puzzleGrid.rows, puzzleGrid.columns, solverGrid);
        }
    }

    public void showTemporaryText(String text) {
        timerText.setText(text);
        timerText.setVisible(true);

        controls.revalidate();
        controls.repaint();

        Timer timer = new Timer(3000, e -> {
            timerText.setVisible(false);
        });

        timer.setRepeats(false);
        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            App a = new App();
            JFrame frame = new JFrame("Nonograms");
            frame.setContentPane(a.mainPanel);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.setSize(1000, 800);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);

            // handle closing the window by asking the user if they want to save
            frame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    if (!a.isSaved) {
                        int response = a.askSave();
                        if (response == JOptionPane.YES_OPTION) {
                            a.saveGrid();
                        }
                        if (response == JOptionPane.YES_OPTION || response == JOptionPane.NO_OPTION) {
                            frame.dispose();
                        }
                    } else {
                        frame.dispose();
                    }
                    // ensures if the user clicks Xoff the popup, it keeps them in the game
                }
            });
            frame.setVisible(true);
        });
    }
}