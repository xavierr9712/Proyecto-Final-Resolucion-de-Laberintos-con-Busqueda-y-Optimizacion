package views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import models.Cell;
import models.CellState;
import controllers.MazeController;

public class MazePanel  extends JPanel{

     private Cell[][] maze;
    private MazeController controller;
    private int cellSize = 20;

    public MazePanel() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (controller != null) {
                    int col = e.getX() / cellSize;
                    int row = e.getY() / cellSize;
                    controller.handleCellClick(row, col);
                }
            }
        });
    }

    public void setController(MazeController controller) {
        this.controller = controller;
    }

    public void setMaze(Cell[][] maze) {
        this.maze = maze;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (maze == null) return;

        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[0].length; col++) {
                g.setColor(getColorForState(maze[row][col].getState()));
                g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
                g.setColor(Color.GRAY);
                g.drawRect(col * cellSize, row * cellSize, cellSize, cellSize);
            }
        }
    }

    private Color getColorForState(CellState state) {
        switch (state) {
            case EMPTY: return Color.WHITE;
            case WALL: return Color.BLACK;
            case START: return Color.GREEN;
            case END: return Color.RED;
            case PATH: return Color.CYAN;
            case VISITED: return Color.LIGHT_GRAY;
            default: return Color.WHITE;
        }
    }
    
}
