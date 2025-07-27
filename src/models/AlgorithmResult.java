package models;

public class AlgorithmResult {

    private String algorithmName;
    private int pathLength;
    private long executionTimeMillis;
    private String mazeSize;

    public AlgorithmResult(String algorithmName, int pathLength, long executionTimeMillis, String mazeSize) {
        this.algorithmName = algorithmName;
        this.pathLength = pathLength;
        this.executionTimeMillis = executionTimeMillis;
        this.mazeSize = mazeSize;
    }
    
    public AlgorithmResult(String[] csvRow) {
        this.algorithmName = csvRow[0];
        this.pathLength = Integer.parseInt(csvRow[1]);
        this.executionTimeMillis = Long.parseLong(csvRow[2]);
        this.mazeSize = csvRow[3];
    }

    public String[] toCsvRow() {
        return new String[]{
            algorithmName,
            String.valueOf(pathLength),
            String.valueOf(executionTimeMillis),
            mazeSize
        };
    }
    
    public String getAlgorithmName() { return algorithmName; }
    public int getPathLength() { return pathLength; }
    public long getExecutionTimeMillis() { return executionTimeMillis; }
    public String getMazeSize() { return mazeSize; }
    
    public void setPathLength(int pathLength) { this.pathLength = pathLength; }
    public void setExecutionTimeMillis(long executionTimeMillis) { this.executionTimeMillis = executionTimeMillis; }
    public void setMazeSize(String mazeSize) { this.mazeSize = mazeSize; }
    
}
