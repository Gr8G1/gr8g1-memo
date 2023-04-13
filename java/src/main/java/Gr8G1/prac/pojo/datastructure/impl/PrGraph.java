package Gr8G1.prac.pojo.datastructure.impl;

import Gr8G1.prac.pojo.lambda.temp.TriFunction;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PrGraph {
  // 인접행렬 생성하기
  public static int[][] createMatrix(int[][] edges) {
    if (edges.length == 0) return edges;
    int size = Arrays.stream(edges).flatMapToInt(Arrays::stream).max().orElse(0) + 1;

    int[][] adjacencyMatrix = new int[size][size];

    for (int[] edge: edges) {
      int from = edge[0];
      int to = edge[1];
      int dir = edge[2];

      // directed
      if (dir == 1) adjacencyMatrix[to][from] = 1;

      // undirected
      adjacencyMatrix[from][to] = 1;
    }

    return adjacencyMatrix;
  }

  // 주어진 행렬의 from ~ to 길 찾기
  public static boolean getDirections(int[][] matrix, int from, int to) {
    // # DFS 문제 풀기
    // ~ 재귀 함수 생성
    // public static void dfs(int row, int[][] matrix, boolean[] visited) {
    //   visited[row] = true;
    //
    //   for (int i = 0; i < matrix.length; i++) if (!visited[i] && matrix[row][i] == 1) dfs(i, matrix, visited);
    // }

    // ~ 내부 로직
    // boolean[] visited = new boolean[matrix.length];
    //
    // for (int i = 0; i < matrix.length; i++){
    //   if (!visited[i]) dfs(from, matrix, visited);
    //   else return true;
    // }
    //
    // return false;

    // # BFS 문제 풀기
    Queue<Integer> q = new LinkedList<>() {{
      for (int i = 0; i < matrix.length; i++) {
        if (matrix[from][i] == 1) add(i);
      }
    }};

    boolean[] visited = new boolean[matrix.length];
    Arrays.fill(visited, false);

    while (q.size() > 0) {
      Integer row = q.remove();
      visited[row] = true;

      if (row == to) return true;

      for (int i = 0; i < matrix.length; i++) {
        if (!visited[i] && matrix[row][i] == 1) {
          q.add(i);
        }
      }
    }

    return false;
  }

  // 주어진 간선의 연결된 정점 찾기
  public static int getConnectedVertex(int[][] edges) {
    int vertex = 0;
    int size = Arrays.stream(edges).flatMapToInt(Arrays::stream).max().orElse(0) + 1;

    // vertexBFS
    // Queue<Integer> q = new LinkedList<>();
    Integer[][] adjMatrix = new Integer[size][size];
    Boolean[] visited = new Boolean[size];
    Arrays.fill(visited, false);

    for (Integer[] adj: adjMatrix) {
      Arrays.fill(adj, 0);
    }

    for (int[] edge: edges) {
      int from = edge[0];
      int to = edge[1];

      adjMatrix[from][to] = adjMatrix[to][from] = 1;
    }

    System.out.println(
      Arrays.deepToString(adjMatrix)
            .replace("[[", "[\n  [")
            .replace("], ", "]\n  ")
            .replace("]]", "]\n]")
    );

    // Adjacency Matrix
    //
    // I/O  0  1  2  3  4  5
    //  0  [0, 1, 0, 0, 0, 0] - in 1
    //  1  [1, 0, 0, 0, 0, 0] - in 1
    //  2  [0, 0, 0, 1, 0, 0] - in 1
    //  3  [0, 0, 1, 0, 1, 0] - in 2
    //  4  [0, 0, 0, 1, 0, 1] - in 2
    //  5  [0, 0, 0, 0, 1, 0] - in 1
    //      1  1  1  2  2  1

    TriFunction<Integer, Integer[][], Boolean[], Void> vertexDFS = new TriFunction<>() {
      @Override
      public Void apply(Integer vertex, Integer[][] adjMatrix, Boolean[] visited) {
        visited[vertex] = true;

        for (int i = 0; i < adjMatrix[vertex].length; i++) {
          if (!visited[i] && adjMatrix[vertex][i] == 1) {
            this.apply(i, adjMatrix, visited);
          }
        }

        return null;
      }
    };

    int[] flatEdges = Arrays.stream(edges).flatMapToInt(Arrays::stream).distinct().toArray();
    // 1, 2, 3, 4, 5
    for (int e: flatEdges) {
      if (!visited[e]) {
        vertexDFS.apply(e, adjMatrix, visited);

        // vertexBFS
        // q.add(e);
        //
        // while (q.size() > 0) {
        //   Integer c = q.remove();
        //   visited[c] = true;
        //
        //   for (int i = 0; i < adjMatrix.length; i++) {
        //     if (!visited[i] && adjMatrix[c][i] == 1) {
        //       visited[i] = true;
        //       q.add(i);
        //     }
        //   }
        // }

        vertex++;
      }
    }

    return vertex;
  }

  // 바코드 생성하기
  public static String barcodeDFS(int leng) {
    // 생성 할 수 있는 바코드
    String[] availableStr = {"1", "2", "3"};

    // 인접합 바코드 {1, 2, 3}의 중복 확인 (11 : X, 12: O ....)
    Function<String, Boolean> validBarcode = vBarcode -> {
      String front;
      String rear;

      for (int i = 1; i < (vBarcode.length() / 2) + 1; i++) {
        front = vBarcode.substring(vBarcode.length() - (i * 2), vBarcode.length() - i); // 1
        rear = vBarcode.substring(vBarcode.length() - i); // 2

        if (front.equals(rear)) return false;
      }

      return true;
    };

    // 바코트 생성
    BiFunction<Integer, String, String> genBarcode = new BiFunction<>() {
      @Override
      public String apply(Integer i, String gBarcode) {
        if (i == leng) return gBarcode;

        String[] available = Arrays.stream(availableStr)
            .filter(n -> n.charAt(0) != gBarcode.charAt(gBarcode.length() - 1))
            .toArray(String[]::new);

        for (String c : available) {
          String temp = gBarcode + c;

          if (validBarcode.apply(temp)) {
            String nBarcode = this.apply(i + 1, temp);

            if (!nBarcode.equals("")) {
              return nBarcode;
            }
          }
        }

        return "";
      }
    };

    return genBarcode.apply(1, "1");
  }

  public static void main(String[] args) {
    int[][] adjacencyList = createMatrix(new int[][] {
        {0, 1, 0},
        {1, 3, 0},
        {3, 2, 1},
        {2, 4, 0}
    });

    System.out.println(
        Arrays.deepToString(adjacencyList)
            .replace("[[", "[\n  [")
            .replace("], ", "]\n  ")
            .replace("]]", "]\n]")
    );

    System.out.println(
      getDirections(adjacencyList,1,4)
    );

    System.out.println(
        getConnectedVertex(new int[][] {
            {0, 1},
            {2, 3},
            {3, 4},
            {4, 5}
        })
    );

    System.out.println(barcodeDFS(8));
  }
}
