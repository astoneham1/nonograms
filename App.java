import javax.json.stream.JsonParsingException;
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
import java.util.stream.Stream;

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
    private boolean undoAllowed = false;

    // GRIDS
    private Grid puzzleGrid;
    private Grid userGrid;
    private String currentGridName;

    // public static void main(String[] args) {
    //     new App();
    // }

    public App() {
        setupUI();
        loadGamePuzzle("fromStart");

        loadFile.addActionListener(e -> loadGamePuzzle("fromButton"));

        check.addActionListener(e -> {
            // Create checker object, call the checkNonogram method and get the message to output to the screen.
            Checker c = new Checker();
            ArrayList<ArrayList<Integer>> a = c.checkNonogram(userGrid.grid, puzzleGrid.rowClues, puzzleGrid.columnClues);
            String message = c.getMessage(a, puzzleGrid.rowClues.size(), puzzleGrid.columnClues.size());
            JOptionPane.showMessageDialog(mainPanel, message);
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
            JOptionPane.showMessageDialog(mainPanel, "Saved progress to grid: " + currentGridName);
            isSaved = true;
            save.setBackground(Color.GRAY);
            save.setEnabled(false);
        });

        undo.addActionListener(e -> {
            if (undoAllowed) {
                userGrid.undoMoves();
                buildGrid(puzzleGrid.rows, puzzleGrid.columns);
            }

            if (userGrid.moves.size() == 0) {
                undo.setBackground(Color.GRAY);
                undo.setEnabled(false);
                undoAllowed = false;
            }

            if (isSaved) {
                save.setBackground(check.getBackground());
                save.setEnabled(true);
                isSaved = false;
            }
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
            } catch (JsonFormatException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            } catch (JsonParsingException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }

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
                    undo.setBackground(Color.GRAY);
                    undo.setEnabled(false);
                }

                ArrayList<Clue> rowClues = puzzleGrid.rowClues;
                ArrayList<Clue> columnClues = puzzleGrid.columnClues;

                this.colors = Colours.colours;

                displayColors();
                buildGrid(puzzleGrid.rows, puzzleGrid.columns);
                displayClues(rowClues, columnClues);
            } else if (source.equals("fromStart")) {
                System.exit(0);
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
                // When loading a new puzzle where the colour does not exist (i.e. colour 3 exists in colour cat but not blanks smiler),
                // the colour and state of the grid are set to 0 (unknown).
                if (colors.get(userGrid.grid[i][j]) == null) {
                    userGrid.grid[i][j] = 0;
                    cell.putClientProperty("state", 0);
                }
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

                        if (isSaved) {
                            save.setBackground(check.getBackground());
                            save.setEnabled(true);
                            isSaved = false;
                        }

                        if (!undoAllowed) {
                            undo.setBackground(check.getBackground());
                            undo.setEnabled(true);
                            undoAllowed = true;
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
                            userGrid.updateMove(row, col, selectedColor); // Updates the move in the JSON and the grid, so the checker will work if they have dragged it since all the cells are being updated in userGrid.grid
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
        focus.removeAll();

        rowCluePanel.setLayout(new GridLayout(rows, 1));
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

        columnCluePanel.setLayout(new GridLayout(1, columns));
        for (Clue clue : columnClues) {
            JPanel clueColumn = new JPanel();
            clueColumn.setLayout(new BoxLayout(clueColumn, BoxLayout.Y_AXIS));
            ArrayList<Integer> counts = clue.getCounts();
            ArrayList<Integer> clueColor = clue.getColours();

            for (int i = 0; i < counts.size(); i++) {
                JLabel clueLabel = new JLabel(String.valueOf(counts.get(i)));
                int colorKey = clueColor.get(i);
                String colorCode = colors.getOrDefault(colorKey, "#000000");
                clueLabel.setForeground(Color.decode(colorCode));
                clueColumn.add(clueLabel);
            }

            columnCluePanel.add(clueColumn);
        }

        // Top-left spacer
        JPanel cornerSpacer = new JPanel();
        cornerSpacer.setPreferredSize(new Dimension(90, 10));
        cornerSpacer.setBackground(grid.getBackground());

        // Top row: spacer + column clues
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.add(cornerSpacer, BorderLayout.WEST);
        topRow.add(columnCluePanel, BorderLayout.CENTER);

        // Center row: row clues + grid
        JPanel centerRow = new JPanel(new BorderLayout());
        centerRow.add(rowCluePanel, BorderLayout.WEST);
        centerRow.add(grid, BorderLayout.CENTER);

        // Add all to focus panel
        focus.setLayout(new BorderLayout());
        focus.add(topRow, BorderLayout.NORTH);
        focus.add(centerRow, BorderLayout.CENTER);

        focus.revalidate();
        focus.repaint();
    }

    public void cellClicked(JButton cell) {
        cell.putClientProperty("state", this.selectedColor);
        cell.setBackground(Color.decode(colors.get(this.selectedColor)));
    }

}