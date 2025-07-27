package models;

import java.util.Objects;

public class Cell {

     private final int row;
    private final int col;
    private CellState state;
    private Cell parent; 

    public Cell(int row, int col, CellState state) {
        this.row = row;
        this.col = col;
        this.state = state;
        this.parent = null;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
    public CellState getState() { return state; }
    public void setState(CellState state) { this.state = state; }
    public Cell getParent() { return parent; }
    public void setParent(Cell parent) { this.parent = parent; }

    @Override
    public String toString() {
        return "Cell(" + row + ", " + col + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return row == cell.row && col == cell.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
    
}
