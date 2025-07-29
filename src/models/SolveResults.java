package models;

import java.util.List;

public class SolveResults {
    private final List<Cell> path;
    private final List<Cell> visited;
    private final AlgorithmResult resultDetails;

    public SolveResults(List<Cell> visited, List<Cell> path, AlgorithmResult resultDetails) {
    this.visited = visited;
    this.path = path;
    this.resultDetails = resultDetails;
}

public List<Cell> getVisited() { return visited; }

    public List<Cell> getPath() { return path; }
    public AlgorithmResult getResultDetails() { return resultDetails; }
}


