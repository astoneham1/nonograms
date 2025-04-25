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

        // centre gameArea area with grid and clues
        rowCluePanel = new JPanel();
        columnCluePanel = new JPanel();

        gameArea = new JPanel(new BorderLayout());
        gameArea.setBackground(new Color(0xD2D2D2));

        grid = new JPanel();
        grid.setLayout(new GridLayout(1, 1));

        gameArea.add(grid, BorderLayout.CENTER);
        gameArea.add(rowCluePanel, BorderLayout.WEST);
        gameArea.add(columnCluePanel, BorderLayout.NORTH);

        mainPanel.add(gameArea, BorderLayout.CENTER);

        // bottom of screen controls
        controls = new JPanel();
        controls.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // more padding between buttons
        controls.setBackground(new Color(0xCCCCCC)); // subtle background

        Font buttonFont = new Font("SansSerif", Font.PLAIN, 14);

        loadedName = new JLabel("", SwingConstants.CENTER);
        loadedName.setFont(new Font("SansSerif", Font.BOLD, 14));

        loadFile = createStyledButton("Load", buttonFont);
        reset = createStyledButton("Reset", buttonFont);
        check = createStyledButton("Check", buttonFont);
        undo = createStyledButton("Undo", buttonFont);
        solve = createStyledButton("Solve", buttonFont);
        save = createStyledButton("Save", buttonFont);

        timerText = new JLabel("dsdsd", SwingConstants.RIGHT);
        timerText.setFont(new Font("SansSerif", Font.BOLD, 14));
        timerText.setForeground(Color.GREEN);
        timerText.setVisible(false);

        controls.add(loadedName);
        controls.add(loadFile);
        controls.add(reset);
        controls.add(check);
        controls.add(undo);
        controls.add(solve);
        controls.add(save);
        controls.add(timerText);

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
            // Create checker object, call the checkNonogram method and get the message to
            // output to the screen.
            Checker c = new Checker();
            ArrayList<ArrayList<Integer>> a = c.checkNonogram(userGrid.grid, puzzleGrid.rowClues,
                    puzzleGrid.columnClues);
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

            showTemporaryText("Grid Reset");
            // JOptionPane.showMessageDialog(mainPanel, "All cells reset to unknown");
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

    private JButton createStyledButton(String text, Font font) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setFocusPainted(false);
        button.setBackground(new Color(0xE0E0E0));
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
                    displayClues(rowClues, columnClues);
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

            inputMap.put(KeyStroke.getKeyStroke(String.valueOf(key + 1)), "clicked" + key + 1);
            actionMap.put("clicked" + key + 1, new AbstractAction() {
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
        grid.setLayout(new GridLayout(rows + 1, columns)); // +1 for column clue row

        // First row: column clues (no gridlines)
        for (int col = 0; col < columns; col++) {
            JPanel clueCell = new JPanel();
            clueCell.setLayout(new BoxLayout(clueCell, BoxLayout.Y_AXIS));

            // Add some space at the top
            clueCell.add(Box.createVerticalStrut(10)); // Adjust the pixel value (e.g., 10) for desired top
                                                       // padding/height increase

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

            // Add some space at the bottom
            clueCell.add(Box.createVerticalStrut(20)); // Adjust the pixel value (e.g., 10) for desired bottom
                                                       // padding/height increase

            // Add the clueCell to the grid
            grid.add(clueCell);
        }

        // Main grid
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                final int row = i;
                final int col = j;
                JButton cell = new JButton();
                cell.setOpaque(true);

                if (colors.get(userGrid.grid[i][j]) == null) {
                    userGrid.grid[i][j] = 0;
                }
                cell.setBackground(Color.decode(colors.get(userGrid.grid[i][j])));
                cell.putClientProperty("state", userGrid.grid[i][j]);

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

                grid.add(cell);
            }
        }

        grid.revalidate();
        grid.repaint();
    }

    public void displayClues(ArrayList<Clue> rowClues, ArrayList<Clue> columnClues) {
        int rows = rowClues.size();

        rowCluePanel.removeAll();
        gameArea.removeAll();

        rowCluePanel.setLayout(new GridLayout(rows + 1, 1)); // add space for top column clue row
        rowCluePanel.add(new JLabel()); // blank label to push clues down

        for (Clue clue : rowClues) {
            JPanel clueRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 15));
            ArrayList<Integer> counts = clue.getCounts();
            ArrayList<Integer> clueColor = clue.getColours();

            for (int i = 0; i < counts.size(); i++) {
                JLabel clueLabel = new JLabel(String.valueOf(counts.get(i)));
                int colorKey = clueColor.get(i);
                String colorCode = colors.getOrDefault(colorKey, "#000000");
                clueLabel.setForeground(Color.decode(colorCode));
                clueRow.add(clueLabel);
            }

            rowCluePanel.add(clueRow);
        }

        JPanel center = new JPanel(new BorderLayout());
        center.add(rowCluePanel, BorderLayout.WEST);
        center.add(grid, BorderLayout.CENTER);

        gameArea.setLayout(new BorderLayout());
        gameArea.add(center, BorderLayout.CENTER);

        gameArea.revalidate();
        gameArea.repaint();
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
        showTemporaryText("Saved!");
        // JOptionPane.showMessageDialog(mainPanel, "Saved progress to grid: " +
        // currentGridName);
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

    public void showTemporaryText(String text) {
        timerText.setText(text);
        timerText.setVisible(true);

        // Update the layout
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