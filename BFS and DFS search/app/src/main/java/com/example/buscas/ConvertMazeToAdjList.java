package com.example.buscas;

import java.util.ArrayList;
import java.util.List;

public class ConvertMazeToAdjList {
    public static List<List<Integer>> convertMazeToAdjacencyList(int[][] maze) {
        int numRows = maze.length;
        int numCols = maze[0].length;
        List<List<Integer>> adjacencyList = new ArrayList<>();

        for (int i = 0; i < numRows * numCols; i++) {
            adjacencyList.add(new ArrayList<>());
        }

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (maze[i][j] == 0) {
                    int vertex = i * numCols + j;
                    addNeighbors(vertex, i, j, numRows, numCols, maze, adjacencyList);
                }
            }
        }

        return adjacencyList;
    }

        private static void addNeighbors(int vertex, int row, int col, int numRows, int numCols, int[][] maze, List<List<Integer>> adjacencyList) {
            if (isValid(row - 1, col, numRows, numCols, maze)) {
                adjacencyList.get(vertex).add((row - 1) * numCols + col);
            }
            if (isValid(row + 1, col, numRows, numCols, maze)) {
                adjacencyList.get(vertex).add((row + 1) * numCols + col);
            }
            if (isValid(row, col - 1, numRows, numCols, maze)) {
                adjacencyList.get(vertex).add(row * numCols + col - 1);
            }
            if (isValid(row, col + 1, numRows, numCols, maze)) {
                adjacencyList.get(vertex).add(row * numCols + col + 1);
            }
        }

        private static boolean isValid(int row, int col, int numRows, int numCols, int[][] maze) {
            return row >= 0 && row < numRows && col >= 0 && col < numCols && maze[row][col] == 0;
        }
}
