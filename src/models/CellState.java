package models;

public enum CellState {
    EMPTY,      // Vacío, transitable
    WALL,       // Muro, no transitable
    START,      // Punto de inicio
    END,        // Punto de fin
    PATH,       // Parte del camino solución
    VISITED    
}
