package models;

import java.util.List;

public class SolveResults {
    private final List<Cell> path;
    private final AlgorithmResult resultDetails;

    public SolveResults(List<Cell> path, AlgorithmResult resultDetails) {
        this.path = path;
        this.resultDetails = resultDetails;
    }

    public List<Cell> getPath() { return path; }
    public AlgorithmResult getResultDetails() { return resultDetails; }
}


