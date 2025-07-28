package dao.daoImpl;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import dao.AlgorithmResultDAO;
import models.AlgorithmResult;

public class AlgorithmResultDAOFile implements AlgorithmResultDAO{

    private final String csvFilePath;

    public AlgorithmResultDAOFile(String csvFilePath) {
        this.csvFilePath = csvFilePath;
        File file = new File(csvFilePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void save(AlgorithmResult result) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath, true))) {
            writer.write(String.join(",", result.toCsvRow()));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void update(AlgorithmResult result) {
        List<AlgorithmResult> allResults = findAll();
        List<AlgorithmResult> updatedList = allResults.stream()
            .filter(r -> !r.getAlgorithmName().equals(result.getAlgorithmName()))
            .collect(Collectors.toList());
        updatedList.add(result);
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath, false))) { // false para sobreescribir
            for (AlgorithmResult res : updatedList) {
                writer.write(String.join(",", res.toCsvRow()));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<AlgorithmResult> findAll() {
        List<AlgorithmResult> results = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 4) {
                    results.add(new AlgorithmResult(values));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    public void clearAll() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath, false))) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}

