package com.example.buscas;

public class ConvertMazeToAdjMatrix {

    public static int[][] convertMazeToAdjacencyMatrix(int[][] maze) {
        int numRows = maze.length;
        int numCols = maze[0].length;
        int numVertices = numRows * numCols;

        int[][] adjacencyMatrix = new int[numVertices][numVertices];

        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                adjacencyMatrix[i][j] = 0; // Inicialmente, não há conexões
            }
        }

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (maze[i][j] == 0) {
                    int vertex = i * numCols + j;
                    addConnections(vertex, i, j, numRows, numCols, maze, adjacencyMatrix);
                }
            }
        }

        return adjacencyMatrix;
    }

    private static void addConnections(int vertex, int row, int col, int numRows, int numCols, int[][] maze, int[][] adjacencyMatrix) {
        if (isValid(row - 1, col, numRows, numCols, maze)) {
            int neighbor = (row - 1) * numCols + col;
            adjacencyMatrix[vertex][neighbor] = 1;
        }
        if (isValid(row + 1, col, numRows, numCols, maze)) {
            int neighbor = (row + 1) * numCols + col;
            adjacencyMatrix[vertex][neighbor] = 1;
        }
        if (isValid(row, col - 1, numRows, numCols, maze)) {
            int neighbor = row * numCols + col - 1;
            adjacencyMatrix[vertex][neighbor] = 1;
        }
        if (isValid(row, col + 1, numRows, numCols, maze)) {
            int neighbor = row * numCols + col + 1;
            adjacencyMatrix[vertex][neighbor] = 1;
        }
    }

    private static boolean isValid(int row, int col, int numRows, int numCols, int[][] maze) {
        return row >= 0 && row < numRows && col >= 0 && col < numCols && maze[row][col] == 0;
    }
}
