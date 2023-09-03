package com.example.buscas;

import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Labirinto {

    private static final int ROWS = 11;
    private static final int COLS = 11;

    private List<List<Integer>> adjacencyList;

    private GridLayout mazeGridLayout;
    private int[][] maze;
    private int[] startCell;
    private int[] endCell;
    public int[][] createMaze(int rows, int cols) {
        int[][] maze = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze[i][j] = 1;
            }
        }

        generateMaze(1, 1, maze);

        findFarthestCells(maze);

        return maze;
    }

    public void generateMaze(int row, int col, int[][] maze) {
        maze[row][col] = 0;

        int[][] directions = {{0, 2}, {2, 0}, {0, -2}, {-2, 0}};
        shuffle(directions);

        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];

            if (isValid(newRow, newCol, maze)) {
                maze[newRow][newCol] = 0;
                maze[row + direction[0] / 2][col + direction[1] / 2] = 0;
                generateMaze(newRow, newCol, maze);
            }
        }
    }

    public boolean isValid(int row, int col, int[][] maze) {
        return row > 0 && row < ROWS && col > 0 && col < COLS && maze[row][col] == 1;
    }

    private void shuffle(int[][] array) {
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int[] temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    public void findFarthestCells(int[][] maze) {
        int maxDistance = -1;

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (maze[i][j] == 0) {
                    for (int k = 0; k < ROWS; k++) {
                        for (int l = 0; l < COLS; l++) {
                            if (maze[k][l] == 0) {
                                int distance = getDistance(i, j, k, l);
                                if (distance > maxDistance) {
                                    maxDistance = distance;
                                    startCell = new int[]{i, j};
                                    endCell = new int[]{k, l};
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public int getDistance(int row1, int col1, int row2, int col2) {
        return (row2 - row1) * (row2 - row1) + (col2 - col1) * (col2 - col1);
    }

    public void drawMaze(int[][] maze) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                GridLayout.Spec rowSpec = GridLayout.spec(i);
                GridLayout.Spec colSpec = GridLayout.spec(j);
                GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(rowSpec, colSpec);

                if (maze[i][j] == 1) {
                    layoutParams.height = 50;
                    layoutParams.width = 50;
                    // Set the wall color or background drawable
                } else if (i == startCell[0] && j == startCell[1]) {
                    layoutParams.height = 50;
                    layoutParams.width = 50;
                    // Set the starting point color or background drawable
                } else if (i == endCell[0] && j == endCell[1]) {
                    layoutParams.height = 50;
                    layoutParams.width = 50;
                    // Set the ending point color or background drawable
                } else {
                    layoutParams.height = 0;
                    layoutParams.width = 0;
                }

                // Create a View (e.g., ImageView or TextView) and add it to the GridLayout
                // You can set the background color or drawable for cells that represent paths
            }
        }
    }

    public int[] getStart(){
        return startCell;
    }

    public int[] getEnd(){
        return endCell;
    }

}
