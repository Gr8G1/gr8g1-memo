package Gr8G1.prac.playground.ct;

public class BoardGame {
  /*
   * # 보드게임
   *    - 보드판을 조작하여 말이 해당 칸을 지나가면서 획득한 숫자의 합을 구하라.
   *      > 시작 위치: board[0][0];
   *
   * ~ In
   *  - int[][] board: [
   *      [1, 1, 1],
   *      [1, 0, 1],
   *      [1, 1, 1]
   *    ]
   *  - String operation: "DD"
   * ~ Out
   *    > 획득한 점수 = 2
   *
   * ! Warn
   *  - 말이 보드 밖으로 나가면 즉시 게임을 종료하고 null을 반환한다.
   *
   */
  public static Integer boardGame(int[][] board, String operation) {
    int[][] dir = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}}; // 상, 하, 좌, 우
    int sRow = 0;
    int sCol = 0;
    int score = 0;

    Game:
    while (operation.length() > 0) {
      char c = operation.charAt(0);
      operation = operation.substring(1);

      switch (c) {
        case 'D':
          sRow = sRow + dir[0][0];
          sCol = sCol + dir[0][1];
          break;
        case 'U':
          sRow = sRow + dir[1][0];
          sCol = sCol + dir[1][1];
          break;
        case 'R':
          sRow = sRow + dir[2][0];
          sCol = sCol + dir[2][1];
          break;
        case 'L':
          sRow = sRow + dir[3][0];
          sCol = sCol + dir[3][1];
          break;
        default:
          return null;
      }

      if ((sRow < 0 || sRow > board.length) || (sCol < 0 || sCol > board.length)) return null;
      if (board[sRow][sCol] != 0) score += board[sRow][sCol];
    }

    return score != -1 ? score : null;
  }

  public static void main(String[] args) {
    System.out.println(
      boardGame(
        new int[][]{
          {1, 1, 1},
          {1, 0, 1},
          {1, 1, 1}
        },
        "DD"
      )
    );
  }
}
