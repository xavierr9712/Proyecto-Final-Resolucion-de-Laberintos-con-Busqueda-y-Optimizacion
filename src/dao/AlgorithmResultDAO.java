package dao;

import java.util.List;

import models.AlgorithmResult;

public interface AlgorithmResultDAO {
    void save(AlgorithmResult result);
    void update(AlgorithmResult result);
    List<AlgorithmResult> findAll();
    void clearAll();
    
}
