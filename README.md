# Proyecto Final - Estructura de Datos

**Título de la Práctica:** Implementación de un Algoritmo para Encontrar la Ruta Óptima en un Laberinto Aplicando Programación Dinámica y Estructuras de Datos  
**Carrera:** Computación  
**Asignatura:** Estructura de Datos – Segundo Interciclo  

---

## Objetivo

Desarrollar una aplicación que implemente algoritmos para encontrar la ruta óptima desde un punto de inicio (A) hasta un punto de destino (B) en un laberinto. Para ello, se utilizan conceptos de estructuras de datos lineales y no lineales, patrones de diseño (MVC), y se comparan tiempos/eficiencia entre diversos enfoques de búsqueda.

---

## Universidad Politécnica Salesiana

![Logo de la Universidad](https://github.com/Jona142004/Proyecto-FInal/blob/main/logo_ups.png?raw=true)

- Universidad: Universidad Politécnica Salesiana  
- Carrera: Ingeniería en Ciencias de la Computación  
- Asignatura: Estructura de Datos  
- Estudiantes: Byron Xavier Ortega Delgado (bortgead@est.ups.edu.ec)  
- Docente: Ing. Pablo Torres  

---

## Descripción del Problema

El proyecto consiste en resolver un laberinto representado como una matriz de celdas, donde cada celda puede ser transitable, muro, inicio o destino. El reto es encontrar el camino óptimo desde el punto A (inicio) hasta el punto B (fin), utilizando algoritmos de búsqueda y estructuras de datos eficientes. Además, el proyecto incluye una interfaz gráfica interactiva para configurar el laberinto y visualizar el proceso de resolución.

---

### Descripción de la Solución

La aplicación permite al usuario:
- Definir tamaño del laberinto.
- Seleccionar celdas como inicio, fin o muros.
- Elegir entre los algoritmos BFS, DFS o variantes recursivas para encontrar la solución.
- Visualizar la ruta encontrada y estadísticas de tiempo de ejecución y longitud del camino.
- Guardar los resultados para análisis posterior.

## Estructura del Proyecto

El proyecto sigue el patrón de diseño MVC (Modelo-Vista-Controlador), con la siguiente estructura:

### 1. **Controladores (controllers)**
- **MazeController.java:** Coordina la interacción entre la interfaz gráfica, los algoritmos de búsqueda y el almacenamiento de resultados.

### 2. **Acceso a Datos (dao y daoImpl)**
- **AlgorithmResultDAO.java:** Define la interfaz para el manejo de resultados.
- **AlgorithmResultDAOFile.java:** Implementación que guarda y carga resultados desde `results.csv`.

### 3. **Modelos (models)**
- **AlgorithmResult.java:** Representa el resultado de un algoritmo (tiempo, pasos).
- **Cell.java:** Define las propiedades de una celda.
- **CellState.java:** Enum que define los estados de las celdas (EMPTY, WALL, START, END, VISITED, PATH).
- **SolveResults.java:** Contiene la información de la solución del laberinto.

### 4. **Algoritmos (solver y solverImpl)**
- **MazeSolver.java:** Interfaz base para todos los algoritmos.
- **MazeSolverBFS.java:** Implementación de Breadth-First Search.
- **MazeSolverDFS.java:** Implementación de Depth-First Search.
- **MazeSolverRecursivo.java:** Algoritmo recursivo simple.
- **MazeSolverRecursivoCompleto.java y MazeSolverRecursivoCompletoBT.java:** Variantes recursivas con backtracking.

### 5. **Vistas (views)**
- **MazeFrame.java:** Ventana principal que muestra el editor de laberintos y los controles.
- **MazePanel.java:** Renderiza gráficamente el laberinto.
- **ResultadosDialog.java:** Muestra los resultados históricos y puede visualizar estadísticas.

### 6. **Clase Principal**
- **MazeApp.java:** Contiene el método `main()` para iniciar la aplicación.

### 7. **Recursos Adicionales**
- **lib/:** Contiene las dependencias externas `jfreechart-1.5.6.jar` y `jcommon-1.0.24.jar` para la generación de gráficos.
- **results.csv:** Registro de resultados de ejecuciones previas.
- **README.md:** Documentación básica del proyecto.

## Marco Teórico

Para encontrar rutas óptimas en laberintos representados como grafos, se utilizan diversos algoritmos de búsqueda y estructuras de datos:

- **Programación Dinámica:** Técnica de optimización que evita cálculos repetitivos, guardando soluciones de subproblemas.
- **BFS (Breadth-First Search):** Algoritmo de búsqueda en anchura que garantiza encontrar el camino más corto en grafos no ponderados, usando colas.
- **DFS (Depth-First Search):** Algoritmo que explora profundamente antes de retroceder, útil para exploraciones completas.
- **Búsqueda Recursiva:** Variante de DFS utilizando llamadas recursivas en lugar de estructuras explícitas.
- **Backtracking:** Exploración de todas las rutas posibles, retrocediendo al encontrar callejones sin salida.

Se utilizan estructuras como matrices, listas, colas, pilas, mapas y conjuntos para la implementación eficiente de estas técnicas.

---

## Implementación del Proyecto

### Arquitectura MVC

- **Modelo:** `Cell`, `CellState`, `SolveResults`, `AlgorithmResult`
- **Vista:** `MazeFrame`, `MazePanel`, `ResultadosDialog`
- **Controlador:** `MazeController`

### Algoritmos Implementados

- BFS (Anchura)
- DFS (Profundidad)
- Recursivo Simple
- Recursivo Completo
- Recursivo con Backtracking

### Estructuras de Datos Utilizadas

- Matriz bidimensional (`Cell[][]`)
- Listas (`ArrayList`)
- Pilas y colas (`LinkedList`)
- Conjuntos (`HashSet`)
- DAO sobre archivo CSV

### Herramientas Utilizadas

- Java
- Swing (GUI)
- Patrón MVC
- JFreeChart (visualización gráfica opcional)
- VSCode (entorno)
- CSV para almacenamiento de resultados

---