package models;

public class AlgorithmResult {

    private String algorithmName;
    private int pathLength;
    private long executionTimeNanos;
    private String mazeSize;

    public AlgorithmResult(String algorithmName, int pathLength, long executionTimeNanos, String mazeSize) {
        this.algorithmName = algorithmName;
        this.pathLength = pathLength;
        this.executionTimeNanos = executionTimeNanos;
        this.mazeSize = mazeSize;
    }
    
    public AlgorithmResult(String[] csvRow) {
        this.algorithmName = csvRow[0];
        this.pathLength = Integer.parseInt(csvRow[1]);
        this.executionTimeNanos = Long.parseLong(csvRow[2]);
        this.mazeSize = csvRow[3];
    }

    public String[] toCsvRow() {
        return new String[]{
            algorithmName,
            String.valueOf(pathLength),
            String.valueOf(executionTimeNanos),
            mazeSize
        };
    }
    
    public String getAlgorithmName() { return algorithmName; }
    public int getPathLength() { return pathLength; }
    public long getExecutionTimeMillis() { return executionTimeNanos; }
    public String getMazeSize() { return mazeSize; }
    
    public void setPathLength(int pathLength) { this.pathLength = pathLength; }
    public void setExecutionTimeMillis(long executionTimeMillis) { this.executionTimeNanos = executionTimeMillis; }
    public void setMazeSize(String mazeSize) { this.mazeSize = mazeSize; }
    
}
