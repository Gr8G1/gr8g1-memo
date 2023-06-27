package Gr8G1.prac.playground.ct;

import java.util.*;
import java.util.stream.IntStream;

public class SpiralOrder {
    public static int[][] solution(int n) {
        int[][] spiralArray = new int[n][n];
        int num = 1; // 배열에 배치할 숫자

        int rowStart = 0; // 행 시작 인덱스
        int rowEnd = n - 1; // 행 종료 인덱스
        int colStart = 0; // 열 시작 인덱스
        int colEnd = n - 1; // 열 종료 인덱스

        while (num <= n * n) {
            // 왼쪽에서 오른쪽으로 행 채우기
            for (int i = colStart; i <= colEnd; i++) {
                spiralArray[rowStart][i] = num++;
            }
            rowStart++;

            // 위에서 아래로 열 채우기
            for (int i = rowStart; i <= rowEnd; i++) {
                spiralArray[i][colEnd] = num++;
            }
            colEnd--;

            // 오른쪽에서 왼쪽으로 행 채우기
            for (int i = colEnd; i >= colStart; i--) {
                spiralArray[rowEnd][i] = num++;
            }
            rowEnd--;

            // 아래에서 위로 열 채우기
            for (int i = rowEnd; i >= rowStart; i--) {
                spiralArray[i][colStart] = num++;
            }
            colStart++;
        }

        return spiralArray;
    }

    public static void main(String[] args) {
        int n = 4;
        int[][] result = solution(n);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(result[i][j] + "\t");
            }
            System.out.println();
        }

        System.out.println();

        System.out.println(
            Arrays.deepToString(result)
                .replace("[[", "[\n  [")
                .replace("], ", "]\n  ")
                .replace("]]", "]\n]"));

    }
}
