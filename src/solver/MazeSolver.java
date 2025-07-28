package solver;

import models.Cell;
import models.SolveResults;

public interface MazeSolver {
     SolveResults solve(Cell[][] maze, Cell start, Cell end);
    
}
