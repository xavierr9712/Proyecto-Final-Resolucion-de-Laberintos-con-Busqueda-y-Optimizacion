package controllers;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker; // Import SwingWorker
import dao.AlgorithmResultDAO;
import dao.daoImpl.AlgorithmResultDAOFile;
import models.*;
import solver.*;
import solver.solverImpl.*;
import views.MazeFrame;
import views.ResultadosDialog;

public class MazeController {
    private List<Cell> stepByStepVisited;
    private List<Cell> stepByStepPath;
    private int currentStepIndex = 0;
    private boolean drawingVisited = true;
    private final MazeFrame view;
    private final AlgorithmResultDAO resultDAO;
    private Cell[][] maze;
    private Cell start, end;

    public void startStepByStepSolve(String algorithmName) {
        cleanMazePathAndVisited();

        if (start == null || end == null) {
            JOptionPane.showMessageDialog(view, "Debe seleccionar un punto de inicio y un punto final.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        MazeSolver solver;
        switch (algorithmName) {
            case "DFS": solver = new MazeSolverDFS(); break;
            case "Recursivo": solver = new MazeSolverRecursivo(); break;
            case "Recursivo Completo": solver = new MazeSolverRecursivoCompleto(); break;
            case "Recursivo Completo BT": solver = new MazeSolverRecursivoCompletoBT(); break;
            case "BFS":
            default: solver = new MazeSolverBFS(); break;
        }

        SolveResults result = solver.solve(maze, start, end);

        this.stepByStepVisited = result.getVisited();
        this.stepByStepPath = result.getPath();
        this.currentStepIndex = 0;
        this.drawingVisited = true;

        // Pinta solo el inicio sin hacer nada aún
        view.updateMaze(maze);
    }

    public void doStep() {
        if (drawingVisited && currentStepIndex < stepByStepVisited.size()) {
            Cell cell = stepByStepVisited.get(currentStepIndex);
            if (cell != start && cell != end && cell.getState() != CellState.WALL) {
                cell.setState(CellState.VISITED);
            }
            currentStepIndex++;
            view.updateMaze(maze);
        } else if (drawingVisited) {
            // Pasamos a pintar el path
            drawingVisited = false;
            currentStepIndex = 0;
        } else if (currentStepIndex < stepByStepPath.size()) {
            Cell cell = stepByStepPath.get(currentStepIndex);
            if (cell != start && cell != end) {
                cell.setState(CellState.PATH);
            }
            currentStepIndex++;
            view.updateMaze(maze);
        }

        if (!drawingVisited && currentStepIndex >= stepByStepPath.size()) {
            currentStepIndex = 0;
            drawingVisited = true;
            stepByStepVisited = null;
            stepByStepPath = null;

            // reset desde MazeFrame
            view.resetStepByStepFlag(); // <-- lo agregas
        }
    }

    public void clearPathsOnly() {
        for (int r = 0; r < maze.length; r++) {
            for (int c = 0; c < maze[0].length; c++) {
                Cell cell = maze[r][c];
                if (cell.getState() == CellState.PATH || cell.getState() == CellState.VISITED) {
                    cell.setState(CellState.EMPTY);
                }
                cell.setParent(null);
            }
        }

        // Asegurar que START y END se mantengan
        if (start != null) start.setState(CellState.START);
        if (end != null) end.setState(CellState.END);

        view.updateMaze(maze);
    }


    public MazeController(MazeFrame view) {
        this.view = view;
        this.resultDAO = new AlgorithmResultDAOFile("results.csv");
        this.view.setController(this);
        createMaze(20, 20); // Crear un laberinto inicial 20x20 por defecto
    }

    public void createMaze(int rows, int cols) {
        // Validación básica para evitar tamaños absurdos o negativos
        if (rows <= 0 || cols <= 0) {
            JOptionPane.showMessageDialog(view, "Las filas y columnas deben ser números positivos.", "Error de Tamaño", JOptionPane.ERROR_MESSAGE);
            return;
        }
        maze = new Cell[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                maze[r][c] = new Cell(r, c, CellState.EMPTY);
            }
        }
        start = null;
        end = null;
        view.updateMaze(maze);
    }

    public void handleCellClick(int row, int col) {
        if (row < 0 || row >= maze.length || col < 0 || col >= maze[0].length) return;
        
        // Limpiar cualquier ruta o celdas visitadas antes de editar
        cleanMazePathAndVisited(); 

        Cell clickedCell = maze[row][col];
        String mode = view.getSelectedEditMode();

        switch (mode) {
            case "START":
                // Si ya hay un inicio, lo vaciamos a menos que sea la misma celda
                if (start != null && !start.equals(clickedCell)) {
                    start.setState(CellState.EMPTY); 
                }
                start = clickedCell;
                start.setState(CellState.START);
                break;
            case "END":
                // Si ya hay un fin, lo vaciamos a menos que sea la misma celda
                if (end != null && !end.equals(clickedCell)) {
                    end.setState(CellState.EMPTY); 
                }
                end = clickedCell;
                end.setState(CellState.END);
                break;
            case "WALL":
                // Si la celda clicada era el inicio o el fin, quitamos esa referencia
                if (clickedCell.equals(start)) start = null;
                if (clickedCell.equals(end)) end = null;
                
                // Alternar entre muro y vacío
                clickedCell.setState(clickedCell.getState() == CellState.WALL ? CellState.EMPTY : CellState.WALL);
                break;
        }
        view.updateMaze(maze);
    }

    // Método para resetear el estado visual del laberinto (caminos y visitados)
    private void cleanMazePathAndVisited() {
        for (int r = 0; r < maze.length; r++) {
            for (int c = 0; c < maze[0].length; c++) {
                Cell cell = maze[r][c];
                // Solo limpiar estados que no sean WALL, START o END
                if (cell.getState() == CellState.PATH || cell.getState() == CellState.VISITED) {
                    cell.setState(CellState.EMPTY);
                }
                cell.setParent(null); // Borrar el padre para futuras búsquedas
            }
        }
        // Asegurarse de que START y END no hayan sido sobrescritos por PATH/VISITED
        if (start != null) start.setState(CellState.START);
        if (end != null) end.setState(CellState.END);
        view.updateMaze(maze);
    }

    public void solveMaze(String algorithmName) {
        cleanMazePathAndVisited();
        if (start == null || end == null) {
            JOptionPane.showMessageDialog(view, "Debe seleccionar un punto de inicio y un punto final.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        MazeSolver solver;
        switch (algorithmName) {
            case "DFS": solver = new MazeSolverDFS(); break;
            case "Recursivo": solver = new MazeSolverRecursivo(); break;
            case "Recursivo Completo": solver = new MazeSolverRecursivoCompleto(); break;
            case "Recursivo Completo BT": solver = new MazeSolverRecursivoCompletoBT(); break;
            case "BFS":
            default: solver = new MazeSolverBFS(); break;
        }

        SolveResults result = solver.solve(maze, start, end);
        List<Cell> visited = result.getVisited();
        List<Cell> path = result.getPath();

        new Thread(() -> {
            try {
                // Primero, animar las celdas visitadas (gris)
                for (Cell cell : visited) {
                    if (cell != start && cell != end && cell.getState() != CellState.WALL) {
                        cell.setState(CellState.VISITED);
                        SwingUtilities.invokeLater(() -> view.updateMaze(maze));
                        Thread.sleep(30); // puedes ajustar velocidad
                    }
                }

                // Luego, animar el camino final (celeste)
                for (Cell cell : path) {
                    if (cell != start && cell != end) {
                        cell.setState(CellState.PATH);
                        SwingUtilities.invokeLater(() -> view.updateMaze(maze));
                        Thread.sleep(50); // puedes ajustar velocidad
                    }
                }

                // Guardar resultados
                resultDAO.save(result.getResultDetails());

            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start();
    }
    
    // Método auxiliar para crear una copia profunda del laberinto
    private Cell[][] createDeepCopyOfMaze(Cell[][] originalMaze) {
        int rows = originalMaze.length;
        int cols = originalMaze[0].length;
        Cell[][] copy = new Cell[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                // Crear un nuevo objeto Cell con el mismo estado. Parent será nulo por defecto.
                copy[r][c] = new Cell(originalMaze[r][c].getRow(), originalMaze[r][c].getCol(), originalMaze[r][c].getState());
            }
        }
        return copy;
    }
    
    // Método auxiliar para encontrar una celda en un laberinto dado (usado para el escenario de copia profunda)
    private Cell findCellInMaze(Cell[][] targetMaze, int row, int col) {
        if (row >= 0 && row < targetMaze.length && col >= 0 && col < targetMaze[0].length) {
            return targetMaze[row][col];
        }
        return null;
    }

    public void loadAndShowResults() {
        List<AlgorithmResult> results = resultDAO.findAll();
        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(view, "No hay resultados guardados aún.", "Resultados Vacíos", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        ResultadosDialog dialog = new ResultadosDialog(view, results);
        dialog.setVisible(true);
    }

    public void clearResults() {
        int confirm = JOptionPane.showConfirmDialog(view, 
            "¿Estás seguro de que quieres borrar todos los resultados?", 
            "Confirmar Borrado", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            resultDAO.clearAll();
            JOptionPane.showMessageDialog(view, "Todos los resultados han sido borrados.", "Resultados Borrados", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
