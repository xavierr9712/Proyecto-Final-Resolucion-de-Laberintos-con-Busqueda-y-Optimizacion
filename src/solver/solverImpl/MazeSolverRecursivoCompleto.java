package solver.solverImpl;

import java.util.ArrayList;
import java.util.List;
import models.*;
import solver.MazeSolver;

public class MazeSolverRecursivoCompleto implements MazeSolver {

    private List<Cell> path;
    private boolean[][] visited;

    @Override
    public SolveResults solve(Cell[][] maze, Cell start, Cell end) {
        long startTime = System.nanoTime();
        path = new ArrayList<>();
        visited = new boolean[maze.length][maze[0].length];

        boolean found = findPath(maze, start.getRow(), start.getCol(), end);

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);

        if (!found) {
            path.clear();
        }
        
        String mazeSize = maze.length + "x" + maze[0].length;
        AlgorithmResult algoResult = new AlgorithmResult("Recursivo (4 dir)", path.size(), duration, mazeSize);
        return new SolveResults(path, algoResult);
    }

    private boolean findPath(Cell[][] maze, int r, int c, Cell end) {
        if (r < 0 || c < 0 || r >= maze.length || c >= maze[0].length || visited[r][c] || maze[r][c].getState() == CellState.WALL) {
            return false;
        }

        visited[r][c] = true;
        
        if (maze[r][c].equals(end)) {
            path.add(maze[r][c]);
            return true;
        }
        
        if (findPath(maze, r + 1, c, end)) { path.add(0, maze[r][c]); return true; } // Abajo
        if (findPath(maze, r - 1, c, end)) { path.add(0, maze[r][c]); return true; } // Arriba
        if (findPath(maze, r, c + 1, end)) { path.add(0, maze[r][c]); return true; } // Derecha
        if (findPath(maze, r, c - 1, end)) { path.add(0, maze[r][c]); return true; } // Izquierda

        return false;
    }
    
}
