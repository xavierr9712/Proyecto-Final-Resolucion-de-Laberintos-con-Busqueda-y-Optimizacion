package controllers;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker; // Import SwingWorker
import dao.AlgorithmResultDAO;
import dao.daoImpl.AlgorithmResultDAOFile;
import models.*;
import solver.*;
import solver.solverImpl.*;
import views.MazeFrame;
import views.ResultadosDialog;

public class MazeController {
    private final MazeFrame view;
    private final AlgorithmResultDAO resultDAO;
    private Cell[][] maze;
    private Cell start, end;

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
        if (start == null || end == null) {
            JOptionPane.showMessageDialog(view, "Por favor, define un punto de INICIO y FIN.", "Error de Laberinto", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Limpiar cualquier ruta o estado visitado anterior
        cleanMazePathAndVisited(); 

        // Usar SwingWorker para ejecutar la lógica de resolución en segundo plano
        new SwingWorker<SolveResults, Cell>() {
            private String currentAlgorithmName = algorithmName;

            @Override
            protected SolveResults doInBackground() throws Exception {
                MazeSolver solver;
                switch (currentAlgorithmName) {
                    case "BFS":
                        solver = new MazeSolverBFS();
                        break;
                    case "DFS":
                        solver = new MazeSolverDFS();
                        break;
                    case "Recursivo":
                        solver = new MazeSolverRecursivo();
                        break;
                    case "Recursivo Completo":
                        solver = new MazeSolverRecursivoCompleto();
                        break;
                    case "Recursivo Completo BT":
                        solver = new MazeSolverRecursivoCompletoBT();
                        break;
                    default:
                        throw new IllegalArgumentException("Algoritmo desconocido: " + currentAlgorithmName);
                }
                
                // Crear una copia profunda del laberinto para que el solver trabaje
                // Esto asegura que el solver no modifique el laberinto original
                // hasta que la solución sea final, y evita problemas entre ejecuciones.
                Cell[][] mazeCopy = createDeepCopyOfMaze(maze);
                
                // Encontrar las celdas de inicio y fin en la copia del laberinto
                // Son necesarias porque las celdas de la copia son objetos diferentes
                Cell startCopy = findCellInMaze(mazeCopy, start.getRow(), start.getCol());
                Cell endCopy = findCellInMaze(mazeCopy, end.getRow(), end.getCol());

                return solver.solve(mazeCopy, startCopy, endCopy);
            }

            @Override
            protected void done() {
                try {
                    SolveResults solveResults = get(); // Obtener el resultado de doInBackground
                    List<Cell> path = solveResults.getPath();
                    AlgorithmResult algoResult = solveResults.getResultDetails();

                    if (!path.isEmpty()) {
                        // Marcar el camino en el laberinto original (el que se muestra en MazePanel)
                        for (Cell cell : path) {
                            // Solo cambiar el estado si no es el inicio o el fin
                            if (cell.getState() != CellState.START && cell.getState() != CellState.END) {
                                maze[cell.getRow()][cell.getCol()].setState(CellState.PATH);
                            }
                        }
                        resultDAO.save(algoResult);
                        JOptionPane.showMessageDialog(view, 
                            "Ruta encontrada en " + algoResult.getExecutionTimeMillis() + " ms. Longitud: " + algoResult.getPathLength(),
                            "Solución Exitosa", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(view, "No se encontró una ruta.", "Sin Solución", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(view, "Error al resolver el laberinto: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                } finally {
                    view.updateMaze(maze); // Actualizar el laberinto en la vista para mostrar el camino o resetear
                }
            }
        }.execute(); // Ejecutar el SwingWorker
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
