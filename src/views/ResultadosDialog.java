package views;

import java.awt.BorderLayout;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import models.AlgorithmResult;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class ResultadosDialog  extends JDialog {

     public ResultadosDialog(JFrame parent, List<AlgorithmResult> results) {
        super(parent, "Resultados de Algoritmos", true);
        setSize(800, 600);
        setLocationRelativeTo(parent);
        
        JTabbedPane tabbedPane = new JTabbedPane();

        // Pestaña de Tabla
        tabbedPane.addTab("Tabla de Resultados", createTablePanel(results));

        // Pestaña de Gráfico
        tabbedPane.addTab("Gráfico de Tiempos", createChartPanel(results));

        add(tabbedPane);
    }

    private JPanel createTablePanel(List<AlgorithmResult> results) {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columnNames = {"Algoritmo", "Longitud Camino", "Tiempo (ms)", "Tamaño Laberinto"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (AlgorithmResult result : results) {
            model.addRow(new Object[]{
                result.getAlgorithmName(),
                result.getPathLength(),
                result.getExecutionTimeMillis(),
                result.getMazeSize()
            });
        }

        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private ChartPanel createChartPanel(List<AlgorithmResult> results) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (AlgorithmResult result : results) {
            dataset.addValue(result.getExecutionTimeMillis(), "Tiempo de Ejecución", result.getAlgorithmName());
        }

        JFreeChart barChart = ChartFactory.createBarChart(
            "Comparación de Tiempos de Ejecución",
            "Algoritmo",
            "Tiempo (ms)",
            dataset,
            PlotOrientation.VERTICAL,
            true, true, false);

        return new ChartPanel(barChart);
    }
    
}
