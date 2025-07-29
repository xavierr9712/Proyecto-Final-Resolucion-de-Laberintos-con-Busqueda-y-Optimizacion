package views;

import javax.swing.*;
import java.awt.*;
import controllers.MazeController;
import models.Cell;

public class MazeFrame extends JFrame {
    private MazePanel mazePanel;
    private boolean stepByStepActive = false;
    private MazeController controller;
    private JComboBox<String> algorithmSelector;
    private JRadioButton rbSetWall, rbSetStart, rbSetEnd;

    private JTextField rowsField;
    private JTextField colsField;

    public MazeFrame() {
        setTitle("Resolvedor de Laberintos");
setSize(1400, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mazePanel = new MazePanel();
        JPanel mazeWrapper = new JPanel(new GridBagLayout()); // Panel con layout que centra
        mazeWrapper.add(mazePanel); // Agregamos el mazePanel dentro centrado
        add(mazeWrapper, BorderLayout.CENTER); // mazeWrapper se agrega al centro del JFrame

        
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        controlPanel.add(new JLabel("Filas:"));
        rowsField = new JTextField("20", 3);
        controlPanel.add(rowsField);
        
        controlPanel.add(new JLabel("Columnas:"));
        colsField = new JTextField("20", 3);
        controlPanel.add(colsField);
        
        JButton createMazeButton = new JButton("Crear/Limpiar Laberinto");
        controlPanel.add(createMazeButton);

        controlPanel.add(new JLabel("Algoritmo:"));
        String[] algorithms = {"BFS", "DFS", "Recursivo", "Recursivo Completo", "Recursivo Completo BT"};
        algorithmSelector = new JComboBox<>(algorithms);
        controlPanel.add(algorithmSelector);

        JButton solveButton = new JButton("Resolver");
        controlPanel.add(solveButton);

        JButton stepByStepButton = new JButton("Resolver Paso a Paso");
        controlPanel.add(stepByStepButton);

        JButton clearPathsButton = new JButton("Limpiar Caminos");
        controlPanel.add(clearPathsButton);
        
        rbSetWall = new JRadioButton("Poner Muro", true);
        rbSetStart = new JRadioButton("Poner Inicio");
        rbSetEnd = new JRadioButton("Poner Fin");
        ButtonGroup group = new ButtonGroup();
        group.add(rbSetWall);
        group.add(rbSetStart);
        group.add(rbSetEnd);
        JPanel radioPanel = new JPanel();
        radioPanel.add(rbSetWall);
        radioPanel.add(rbSetStart);
        radioPanel.add(rbSetEnd);
        
        JButton resultsButton = new JButton("Ver Resultados");
        controlPanel.add(resultsButton);
        
        JButton clearResultsButton = new JButton("Limpiar Resultados");
        controlPanel.add(clearResultsButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(controlPanel, BorderLayout.NORTH);
        topPanel.add(radioPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        createMazeButton.addActionListener(e -> {
            try {
                int rows = Integer.parseInt(rowsField.getText());
                int cols = Integer.parseInt(colsField.getText());
                if (rows <= 0 || cols <= 0) {
                     JOptionPane.showMessageDialog(this, "Las filas y columnas deben ser números positivos.", "Error de Tamaño", JOptionPane.ERROR_MESSAGE);
                     return;
                }
                if (controller != null) controller.createMaze(rows, cols);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, introduce números válidos para filas y columnas.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            }
        });

        stepByStepButton.addActionListener(e -> {
            String selectedAlgorithm = (String) algorithmSelector.getSelectedItem();

            if (!stepByStepActive) {
                controller.startStepByStepSolve(selectedAlgorithm);
                stepByStepActive = true;
            } else {
                controller.doStep();
            }
        });

        solveButton.addActionListener(e -> {
            if (controller != null) {
                String selectedAlgorithm = (String) algorithmSelector.getSelectedItem();
                controller.solveMaze(selectedAlgorithm);
            }
        });
        
        resultsButton.addActionListener(e -> {
            if (controller != null) controller.loadAndShowResults();
        });
        
        clearResultsButton.addActionListener(e -> {
            if (controller != null) controller.clearResults();
        });

        clearPathsButton.addActionListener(e -> {
            if (controller != null) {
                controller.clearPathsOnly();
                stepByStepActive = false; // reiniciar también el modo paso a paso
            }
        });
    }

    public void resetStepByStepFlag() {
        stepByStepActive = false;
    }

    public void setController(MazeController controller) {
        this.controller = controller;
        this.mazePanel.setController(controller);
    }
    
    public void updateMaze(Cell[][] maze) {
        mazePanel.setMaze(maze);
    }
    
    public String getSelectedEditMode() {
        if (rbSetStart.isSelected()) return "START";
        if (rbSetEnd.isSelected()) return "END";
        return "WALL";
    }
}