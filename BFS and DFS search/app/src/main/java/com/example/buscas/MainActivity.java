package com.example.buscas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private int gameRows = 11, gameColumn = 11;
    private int currentIndex = 0;

    private LinearLayout PLayout, layouts[];
    private DisplayMetrics dm;
    private MaterialButton btn[][];
    private List<List<Integer>> adjList;
    private int[][] adjMatrix;
    private int width, height;

    private Button btnStart, btnstart2, btnRestart;

    private Handler handler;
    private int labirinto[][];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler(Looper.getMainLooper());

        dm = new DisplayMetrics();
        MainActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);

        btn = new MaterialButton[gameRows][gameColumn];
        btnStart = findViewById(R.id.btnStart);
        btnstart2 = findViewById(R.id.btnStart2);
        btnRestart = findViewById(R.id.btnRestart);

        PLayout = findViewById(R.id.PLayout);
        PLayout.setLayoutParams(new LinearLayout.LayoutParams(dm.widthPixels, dm.heightPixels-300));
        layouts = new LinearLayout[11];

        for(int i = 0;  i < 11; i++){
            layouts[i] = new LinearLayout(this);
            layouts[i].setOrientation(LinearLayout.VERTICAL);
            layouts[i].setLayoutParams(new LinearLayout.LayoutParams(((dm.widthPixels)/gameRows), dm.heightPixels-300));
            layouts[i].setPadding(0,0,0,0);
            PLayout.addView(layouts[i]);
        }

        PLayout.addView(new Button(this));
        for (int i = 0; i < gameRows; i++) {
            for (int j = 0; j < gameColumn; j++) {
                Context ctx = new ContextThemeWrapper(new MaterialButton(this).getContext(), com.google.android.material.R.style.Widget_MaterialComponents_ExtendedFloatingActionButton_Icon);
                btn[i][j] = new MaterialButton(ctx);
                btn[i][j].setInsetTop(0);
                btn[i][j].setInsetBottom(0);
                btn[i][j].setLayoutParams(new LinearLayout.LayoutParams((dm.widthPixels)/gameRows, (dm.heightPixels-300)/11));
                btn[i][j].setStrokeWidth(2);
                btn[i][j].setStrokeColor(ColorStateList.valueOf(Color.BLACK));

                layouts[j].addView(btn[i][j]);
            }
        }
    }


    public void click(View v){
        if(btnStart.equals(v)){
            Labirinto lab = new Labirinto();
            ConvertMazeToAdjList convert = new ConvertMazeToAdjList();

             int labirinto[][] = lab.createMaze(gameRows,gameColumn);

            for(int i = 0; i < gameRows; i++){
                for(int j = 0; j < gameColumn; j++){
                    if(labirinto[i][j] == 1)
                        btn[i][j].setBackgroundColor(Color.GRAY);
                }
            }

            int endCells[] = lab.getEnd();
            int startCells[]= lab.getStart();

            btn[startCells[0]][startCells[1]].setBackgroundColor(Color.BLUE);
            btn[endCells[0]][endCells[1]].setBackgroundColor(Color.RED);

            adjList = ConvertMazeToAdjList.convertMazeToAdjacencyList(labirinto);
            adjMatrix = ConvertMazeToAdjMatrix.convertMazeToAdjacencyMatrix(labirinto);

            int startVertex = startCells[0] * gameRows + startCells[1];

            boolean[] visited = new boolean[adjList.size()];
            boolean[] visitedM = new boolean[adjMatrix.length];
            //dfs(startVertex, visited, 400);

            int status[][] = new int[11][11];
            dfs2(adjMatrix, startVertex, visitedM);

        }else if(btnstart2.equals(v)){

            Labirinto lab = new Labirinto();
            ConvertMazeToAdjMatrix convert = new ConvertMazeToAdjMatrix();

           int labirinto[][] = lab.createMaze(gameRows,gameColumn);

            for(int i = 0; i < gameRows; i++){
                for(int j = 0; j < gameColumn; j++){
                    if(labirinto[i][j] == 1)
                        btn[i][j].setBackgroundColor(Color.GRAY);
                }
            }

            int endCells[] = lab.getEnd();
            int startCells[]= lab.getStart();

            btn[startCells[0]][startCells[1]].setBackgroundColor(Color.BLUE);
            btn[endCells[0]][endCells[1]].setBackgroundColor(Color.RED);

            adjMatrix = ConvertMazeToAdjMatrix.convertMazeToAdjacencyMatrix(labirinto);

            int startVertex = startCells[0] * gameRows + startCells[1];

            bfs(adjMatrix, startVertex);

        }else if(btnRestart.equals(v)){
            startActivity(new Intent(MainActivity.this, MainActivity.class));
        }


    }
    private void dfs2(int[][] graph, int vertex, boolean[] visited) {
        visited[vertex] = true;
        final Button btnTemp = btn[vertex / gameRows][vertex % gameColumn];
        btnTemp.setBackgroundColor(Color.GREEN);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int nextVertex = findNextUnvisitedNeighbor(graph, vertex, visited);
                if (nextVertex != -1) {
                    dfs2(graph, nextVertex, visited);
                } else {
                    // Verificar os vizinhos visitados que têm vizinhos não visitados
                    for (int i = 0; i < graph.length; i++) {
                        if (visited[i]) {
                            nextVertex = findNextUnvisitedNeighbor(graph, i, visited);
                            if (nextVertex != -1) {
                                dfs2(graph, i, visited);
                                return;
                            }
                        }
                    }
                }
            }
        }, 400);
    }

    private int findNextUnvisitedNeighbor(int[][] graph, int vertex, boolean[] visited) {
        for (int i = 0; i < graph.length; i++) {
            if (graph[vertex][i] == 1 && !visited[i]) {
                return i;
            }
        }
        return -1; // Nenhum vizinho não visitado encontrado
    }


    private void bfs(int[][] graph, int startVertex) {
        final Queue<Integer> queue = new LinkedList<>();
        final boolean[] visited = new boolean[graph.length];

        queue.offer(startVertex);
        visited[startVertex] = true;

        bfsHelper(graph, queue, visited);
    }

    private void bfsHelper(final int[][] graph, final Queue<Integer> queue, final boolean[] visited) {
        if (queue.isEmpty()) {
            return;
        }

        final int vertex = queue.poll();
        final Button btnTemp = btn[vertex / gameRows][vertex % gameColumn];
        btnTemp.setBackgroundColor(Color.GREEN);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < graph.length; i++) {
                    if (graph[vertex][i] == 1 && !visited[i]) {
                        queue.offer(i);
                        visited[i] = true;
                    }
                }
                bfsHelper(graph, queue, visited); // Explore o próximo nível após o atraso
            }
        }, 400);
    }

}