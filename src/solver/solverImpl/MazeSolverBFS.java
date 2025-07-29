package solver.solverImpl;

import java.util.*;
import models.*;
import solver.MazeSolver;

public class MazeSolverBFS implements MazeSolver{
    @Override
    public SolveResults solve(Cell[][] maze, Cell start, Cell end) {
        long startTime = System.nanoTime();
        Queue<Cell> queue = new LinkedList<>();
        Set<Cell> visited = new HashSet<>();
        
        start.setParent(null);
        queue.add(start);
        visited.add(start);

        Cell current = null;
        boolean found = false;

        while (!queue.isEmpty()) {
            current = queue.poll();

            if (current.equals(end)) {
                found = true;
                break;
            }

            for (Cell neighbor : getNeighbors(maze, current)) {
                if (!visited.contains(neighbor) && neighbor.getState() != CellState.WALL) {
                    visited.add(neighbor);
                    neighbor.setParent(current);
                    queue.add(neighbor);
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
        AlgorithmResult algoResult = new AlgorithmResult("BFS", path.size(), duration, mazeSize);
        return new SolveResults(path, algoResult);
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
