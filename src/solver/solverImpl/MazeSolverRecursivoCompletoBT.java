package solver.solverImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import models.*;
import solver.MazeSolver;

public class MazeSolverRecursivoCompletoBT implements MazeSolver {

    private List<Cell> path;
    private List<Cell> visitedCells;
    private boolean[][] visited;

    @Override
    public SolveResults solve(Cell[][] maze, Cell start, Cell end) {
        long startTime = System.nanoTime();
        path = new ArrayList<>();
        visitedCells = new ArrayList<>();
        visited = new boolean[maze.length][maze[0].length];

        boolean found = findPath(maze, start.getRow(), start.getCol(), end);

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
<<<<<<< HEAD
        
=======

>>>>>>> abc873989e8fbc7f88c2cf9da7e7a783135cdfd0
        if (found) {
            Collections.reverse(path);
        } else {
            path.clear();
        }

        String mazeSize = maze.length + "x" + maze[0].length;
        AlgorithmResult algoResult = new AlgorithmResult("Recursivo (4 dir + BT)", path.size(), duration, mazeSize);
        return new SolveResults(visitedCells, path, algoResult);
    }

    private boolean findPath(Cell[][] maze, int r, int c, Cell end) {
        if (r < 0 || c < 0 || r >= maze.length || c >= maze[0].length || visited[r][c] || maze[r][c].getState() == CellState.WALL) {
            return false;
        }

        visited[r][c] = true;
        visitedCells.add(maze[r][c]);

        if (maze[r][c].equals(end)) {
            path.add(maze[r][c]);
            return true;
        }

        if (findPath(maze, r + 1, c, end)) { path.add(maze[r][c]); return true; }
        if (findPath(maze, r - 1, c, end)) { path.add(maze[r][c]); return true; }
        if (findPath(maze, r, c + 1, end)) { path.add(maze[r][c]); return true; }
        if (findPath(maze, r, c - 1, end)) { path.add(maze[r][c]); return true; }

        return false;
    }
}
