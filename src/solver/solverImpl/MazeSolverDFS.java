package solver.solverImpl;

import java.util.*;
import models.*;
import solver.MazeSolver;

public class MazeSolverDFS implements MazeSolver {

    @Override
    public SolveResults solve(Cell[][] maze, Cell start, Cell end) {
        long startTime = System.nanoTime();

        Stack<Cell> stack = new Stack<>();
        List<Cell> visitedCells = new ArrayList<>(); // 👈 importante para animación
        Set<Cell> visitedSet = new HashSet<>();

        start.setParent(null);
        stack.push(start);

        Cell current = null;
        boolean found = false;

        while (!stack.isEmpty()) {
            current = stack.pop();

            if (visitedSet.contains(current)) continue;
            visitedSet.add(current);
            visitedCells.add(current); // 👈 guardar el orden de visita

            if (current.equals(end)) {
                found = true;
                break;
            }

            for (Cell neighbor : getNeighbors(maze, current)) {
                if (!visitedSet.contains(neighbor) && neighbor.getState() != CellState.WALL) {
                    neighbor.setParent(current);
                    stack.push(neighbor);
                }
            }
        }

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);

        List<Cell> path = new ArrayList<>();
        if (found) {
            Cell step = end;
            while (step != null) {
                path.add(step);
                step = step.getParent();
            }
            Collections.reverse(path);
        }

        String mazeSize = maze.length + "x" + maze[0].length;
        AlgorithmResult algoResult = new AlgorithmResult("DFS", path.size(), duration, mazeSize);
        return new SolveResults(visitedCells, path, algoResult); // 👈 animación lista
    }

    private List<Cell> getNeighbors(Cell[][] maze, Cell cell) {
        List<Cell> neighbors = new ArrayList<>();
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};

        for (int i = 0; i < 4; i++) {
            int newRow = cell.getRow() + dr[i];
            int newCol = cell.getCol() + dc[i];

            if (newRow >= 0 && newRow < maze.length && newCol >= 0 && newCol < maze[0].length) {
                neighbors.add(maze[newRow][newCol]);
            }
        }
        return neighbors;
    }
}