package Gr8G1.prac.playground.daily;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.*;

public class PrPlayGround {
  public static ArrayList<String> powerSet(String str) {
    ArrayList<String> result = new ArrayList<>();

    // 상태공간트리 (state space tree)
    // 중복제거
    char[] chars = deduplicated(str);
    boolean[] visited = new boolean[chars.length];

    dfs(0, visited, chars, result);

    return (ArrayList<String>) result.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
  }

  public static char[] deduplicated(String str) {
    String result = "";

    for (int i = 0; i < str.length(); i++) if (str.indexOf(str.charAt(i)) == i) result += str.charAt(i);

    char[] chars = result.toCharArray();
    Arrays.sort(chars);

    return chars;
  }

  public static void dfs(int depth, boolean[] visited, char[] chars, ArrayList<String> result) {
    if (depth == chars.length) {
      StringBuilder sb = new StringBuilder();

      for (int i = 0; i < chars.length; i++) if (visited[i]) sb.append(chars[i]);

      result.add(sb.toString());

      return;
    }

    visited[depth] = false;
    dfs(depth + 1, visited, chars, result);
    visited[depth] = true;
    dfs(depth + 1, visited, chars, result);
  }

  public static int ruleOf72(double rate) {
    // t = {ln(2)/ln(1+r/100) ~ approx 72/r
    int y;
    double d;
    for (y = 0, d = 1; d < 2; y++, d = (d + (d * rate / 100.0))) ;

    return y;
  }

  public static long pow(int n, int p) {
    if (p == 0) return 1;

    long t = pow(n, p / 2);
    long v = (t * t) % 94_906_249;

    if (p % 2 == 0) return v;
    else return (n * v) % 94_906_249;
  }

  public static boolean po2(int n) {
    // while 연산
    // if (n < 1) return false;
    //
    // while (n > 1) {
    //   if (n % 2 == 0) n /= 2;
    //   else return false;
    // }
    //
    // return true;

    //  (Bit 연산) - (1, 2의 보수의 이해)
    if (n < 1) return false;

    return (n & (n - 1)) == 0;
  }

  public static String firstChar(String str) {
    return Arrays.stream(str.split(" ")).reduce("", (a, c) -> a + c.charAt(0));
  }

  public static String reverseString(String str) {
    return new StringBuilder(str).reverse().toString();
  }

  public static String letterCapitalize(String str) {
    String[] strArr = Arrays.stream(str.split(" "))
                        .map(s -> !s.equals("")
                                    ? Character.toUpperCase(s.charAt(0)) + s.substring(1)
                                    : ""
                        )
                        .toArray(String[]::new);

    return String.join(" ", strArr);
  }

  public static HashMap<String, String> convertListToHashMap(String[][] arr) {
    return arr.length != 0
             ? new HashMap<>() {{
      for (String[] toHash : arr) if (!this.containsKey(toHash[0])) put(toHash[0], toHash[1]);
    }}
             : new HashMap<>();
  }

  public static String convertDoubleSpaceToSingle(String str) {
    return str.replaceAll("\\s{2}?", " ");
  }

  public static boolean ABCheck(String str) {
    return str.matches("(?i).*?a(.{3})b.*?") || str.matches("(?i).*?b(.{3})a.*?");
  }

  public static String insertDash(String str) {
    if (str.length() == 0) return null;
    if (str.length() == 1) return str;

    StringBuilder nStr = new StringBuilder();
    String[] sStr = str.split("");

    for (int i = 0; i <= sStr.length - 2; i++) {
      int prev = Integer.parseInt(sStr[i]);
      int next = Integer.parseInt(sStr[i + 1]);

      nStr.append(prev);

      if (prev % 2 != 0 && next % 2 != 0) nStr.append("-");
      if (i == sStr.length - 2) nStr.append(next);
    }

    return nStr.toString();
  }

  public static String[] removeExtremes(String[] arr) {
    if (arr.length == 0) return null;

    int[] min = new int[]{-1, Arrays.stream(arr).mapToInt(String::length).min().orElse(-1)};
    int[] max = new int[]{-1, Arrays.stream(arr).mapToInt(String::length).max().orElse(-1)};

    for (int i = 0; i < arr.length; i++) {
      if (min[1] == arr[i].length()) min[0] = i;
      if (max[1] == arr[i].length()) max[0] = i;
    }

    arr[min[0]] = "__remove__";
    arr[max[0]] = "__remove__";

    return Arrays.stream(arr).filter(n -> !n.equals("__remove__")).toArray(String[]::new);
  }

  public static int[] reverseArr(int[] arr) {
    int[] reverse = new int[arr.length];

    for (int i = 0, j = arr.length - 1; i < arr.length; i++, j--) reverse[j] = arr[i];

    // swap
    // for (int i = 0, t, n = arr.length - 1; i < arr.length / 2; i++) {
    //   t = arr[i];
    //   arr[i] = arr[n - i];
    //   arr[n - i] = t;
    // }

    return reverse;
  }

  public static String readVertically(String[] arr) {
    // 1 2 3 - 0 1 2 - 14 25 36
    // 4 5 6 - 1 3 5 - 147 258 369
    // 7 8 9 - 2 5 8 - 1470 258- 369=
    // 0 - = - 3 7 11

    // for (int i = 1; i < arr.length; i++) {
    //   for (int j = 0, p = i ; j < arr[i].length(); j++, p++) {
    //     try {
    //       str.insert(j == 0 ? p : (p + (i * j)), arr[i].charAt(j));
    //     } catch (Exception e) {
    //       str.append(arr[i].charAt(j));
    //     }
    //   }
    // }
    //
    // return str.toString();
    int maxLength = 0;
    for (String s : arr) if (maxLength < s.length()) maxLength = s.length();

    String[] temp = new String[maxLength];

    for (String str : arr) {
      for (int j = 0; j < str.length(); j++) {
        if (temp[j] == null) temp[j] = Character.toString(str.charAt(j));
        else temp[j] = temp[j] + str.charAt(j);
      }
    }

    StringBuilder result = new StringBuilder();

    for (String s : temp) result.append(s);

    return result.toString();
  }

  public static boolean superIncreasing(int[] arr) {
    int calc = 0;

    for (int i = 1; i < arr.length; i++) {
      calc += arr[i - 1];
      if (calc >= arr[i]) return false;
    }

    return true;
  }

  public static Integer modulo(int num1, int num2) {
    if (num2 == 0) return null;

    while (num1 >= num2) num1 -= num2;

    return num1;
  }

  public static boolean isIsogram(String str) {
    HashMap<Character, Integer> isogram = new HashMap<>();

    for (Character c : str.toCharArray()) {
      Character k = Character.toLowerCase(c);

      if (!isogram.containsKey(k)) isogram.put(k, 1);
      else return false;
    }

    return true;
  }

  public static String computeSquareRoot(int num) {
    double approx = 1;

    while (Math.pow(approx, 2) != num) {
      if (Double.parseDouble(String.format("%.2f", Math.pow(approx, 2))) == num) break;

      // 바빌로니아 점화식: Xn+1 = 1/2(Xn + a/Xn) ->
      approx = (approx + (num / approx)) / 2;
    }

    return String.format("%.2f", approx);
  }

  public static int numberSearch(String str) {
    double result = 0, i, f;
    Pattern numPattern = Pattern.compile("[0-9]");
    Matcher matcher = numPattern.matcher(str);

    while (matcher.find()) result += Integer.parseInt(matcher.group());

    String s = str.replaceAll(String.valueOf(numPattern), "").replaceAll("(?U)\\s+", "");

    result = result / s.length();
    // i = Math.floor(result); integer portion
    // f = result - i; // fraction portion
    // return f < .5 ? i : i + 1; // rounding result

    System.out.println(result);
    System.out.println(Math.round(result));
    System.out.println(Math.round(1.5));

    return (int) Math.round((result * 10) / 10);
  }

  public static String decryptCaesarCipher(String str, int secret) {
    // encrypt: f(p)=(ap+b) % 26
    // decrypt: f^-1(p)=(ap-b) % 26
    StringBuilder result = new StringBuilder();
    secret %= 26;

    for (int i = 0; i < str.length(); i++) {
      char ct = str.charAt(i);
      int ascii = ct - secret;

      if (Character.isLowerCase(ct) && !Character.isLowerCase(ascii)) ascii += 26;
      else if (Character.isUpperCase(ct) && !Character.isUpperCase(ascii)) ascii += 26;

      if (ct != 32) result.append((char) ascii); // 32: space
      else result.append(ct);
    }

    return result.toString();
  }

  public static String compressString(String str) {
    StringBuilder results = new StringBuilder();

    while (str.length() != 0) {
      Character firstLetter = str.charAt(0);

      Pattern regex = Pattern.compile("^" + firstLetter + "{3,}");
      Matcher match = regex.matcher(str);

      if (match.find()) {
        results.append(match.group().length()).append(firstLetter);

        str = str.replace(match.group(), "");
      } else {
        results.append(firstLetter);

        str = str.substring(1);
      }
    }

    return results.toString();
  }

  public static int largestProductOfThree(int[] arr) {
    Integer[] sortedArr = Arrays.stream(arr).boxed().sorted(Comparator.reverseOrder()).toArray(Integer[]::new);

    int calc1 = sortedArr[0] * sortedArr[1] * sortedArr[2];
    int calc2 = sortedArr[0] * sortedArr[sortedArr.length - 1] * sortedArr[sortedArr.length - 2];

    return Math.max(calc1, calc2);
  }

  public static int fibonacci(int n) {
    // naive
    // if (num <= 1) return num;
    // return fibonacci(num - 1) + fibonacci(num - 2);

    // With for
    // int[] f = new int[num + 2];
    //
    // f[0] = 0;
    // f[1] = 1;
    //
    // for (int i = 2; i <= num; i++) f[i] = f[i - 1] + f[i - 2];
    //
    // return f[num];

    // Binet's formula: Sn = Φⁿ – (–Φⁿ) / √5 : O(logN)
    double sr5 = Math.sqrt(5);
    double phi = (1 + sr5) / 2;

    return (int) ((Math.pow(phi, n) - Math.pow(-phi, -n)) / sr5);
  }

  public static boolean isSubsetOf(int[] base, int[] sample) {
    return new HashSet<>(Arrays.stream(base).boxed().collect(Collectors.toList()))
             .containsAll(Arrays.stream(sample).boxed().collect(Collectors.toList()));
  }

  public static boolean balancedBrackets(String str) {
    int i = "]})".indexOf(str);

    HashMap<Character, Character> cMap = new HashMap<>() {{
      put(']', '[');
      put('}', '{');
      put(')', '(');
    }};
    Stack<Character> s = new Stack<>();

    for (Character c : str.toCharArray()) {
      if (!cMap.containsKey(c)) s.push(c);
      else if (s.size() == 0 && cMap.containsKey(c)) return false;
      else if (s.size() != 0 && cMap.get(c) != s.pop()) return false;
    }

    return s.size() == 0;
  }

  public static int rotatedArraySearch(int[] rotated, int target) {
    int start = 0;
    int end = rotated.length - 1;

    while (start < end) {
      int mid = (start + end) / 2;

      if (rotated[mid] == target) return mid;

      if (rotated[start] <= rotated[mid]) {
        if (target < rotated[mid] && target >= rotated[start]) end = mid;
        else start = mid + 1;
      }

      if (rotated[end] >= rotated[mid]) {
        if (target > rotated[mid] && target <= rotated[end]) start = mid;
        else end = mid - 1;
      }
    }

    return -1;
  }

  public static int fact(int n) {
    if (n <= 1) return 1;
    return n * fact(n - 1);
  }

  public static int orderOfPresentation(int N, int[] K) {
    Boolean[] isVisited = new Boolean[K.length + 1];
    Arrays.fill(isVisited, false);

    int pos = 0;

    for (int i = 0; i < K.length; i++) {
      int s = K[i];
      isVisited[s] = true;
      int l = Arrays.stream(Arrays.copyOfRange(isVisited, 1, s)).filter(b -> !b).toArray().length;

      pos += l * fact(N - 1 - i);
    }

    return pos;
  }

  // BFS Short Path
  public static int robotPath(int[][] room, int[] src, int[] dst) {
    class Pointer {
      private final int row;
      private final int col;
      private final int dis;

      public Pointer(int row, int col, int dis) {
        this.row = row;
        this.col = col;
        this.dis = dis;
      }

      public int getRow() {
        return row;
      }

      public int getCol() {
        return col;
      }

      public int getDis() {
        return dis;
      }
    }

    final int MAX_R = room.length;
    final int MAX_C = room[0].length;
    boolean[][] visited = new boolean[MAX_R][MAX_C];
    int[][] dir = new int[][]{
      {-1, 0}, // 상
      {1, 0},  // 하
      {0, -1}, // 좌
      {0, 1}   // 우
    };

    Queue<Pointer> q = new LinkedList<>();
    // 출발 위치 지정
    q.add(new Pointer(src[0], src[1], 0));
    // 방문(출발) 위치 확인
    visited[src[0]][src[1]] = true;

    while (!q.isEmpty()) {
      Pointer cur = q.poll();

      if (cur.getRow() == dst[0] && cur.getCol() == dst[1]) return cur.getDis();

      for (int i = 0; i < 4; i++) {
        int nR = cur.getRow() + dir[i][0];
        int nC = cur.getCol() + dir[i][1];

        if (nR < 0 || nR > MAX_R - 1 || nC < 0 || nC > MAX_C - 1) continue;
        if (visited[nR][nC]) continue;
        if (room[nR][nC] != 0) continue;

        visited[nR][nC] = true;
        q.add(new Pointer(nR, nC, cur.getDis() + 1));
      }
    }

    return -1;
  }

  public static int robotPath2(int[][] room, int[] src, int sDir, int[] dst, int dDir) {
    // 가로와 세로의 길이
    int R = room.length;
    int C = room[0].length;

    // 4가지 방향: 위(1), 오른쪽(2), 아래(3), 왼쪽(4)
    // 차례대로 [방향, 상하이동, 좌우이동]
    int[][] MOVES = new int[][]{
      {1, -1, 0}, // UP
      {2, 0, 1}, // RIGHT
      {3, 1, 0}, // DOWN
      {4, 0, -1} // LEFT
    };

    // 각 위치별 최소의 동작으로 도달 가능한 경우의 방향을 저장
    int[][] directions = new int[R][C];
    // 각 위치별 최소 동작의 수를 저장. 편의상 거리(dist)로 표현
    int[][] dist = new int[R][C];

    for (int row = 0; row < R; row++) {
      for (int col = 0; col < C; col++) {
        directions[row][col] = 0;
        dist[row][col] = Integer.MAX_VALUE;
      }
    }

    // bfs 구현을 위해 큐를 선언한다.
    Queue<Integer[]> queue = new LinkedList<>();

    // 출발 지점의 좌표
    int sRow = src[0];
    int sCol = src[1];

    directions[sRow][sCol] = sDir;
    dist[sRow][sCol] = 0;

    // 목표 지점의 좌표
    int dRow = dst[0];
    int dCol = dst[1];
    queue.offer(new Integer[]{sRow, sCol});

    while (!queue.isEmpty()) {
      Integer[] data = queue.poll();

      int row = data[0];
      int col = data[1];
      int dir = directions[row][col];

      for (int[] move : MOVES) {
        int nDir = move[0], rDiff = move[1], cDiff = move[2];
        // 이동할 좌표
        int nRow = row + rDiff;
        int nCol = col + cDiff;

        // 유효한 좌표가 아니거나
        // 해당 좌표가 장애물(1)인 경우 건너뛴다.
        if (!isValid(nRow, nCol, R, C) || room[nRow][nCol] == 1) continue;

        // 현재 위치의 방향과 목표 위치의 방향과의 차이
        int dDiff = Math.abs(nDir - dir);
        int candidate;

        if (dDiff == 0) {
          // 차이가 없는 경우
          // 출발 지점에서의 방향과 이동하려는 방향이 같은 경우
          // 직진만 하면 되지만 그러기 위해서는 1로 초기화 되어야 한다.
          candidate = (isValid(row, col, R, C) && dist[row][col] != 0) ? dist[row][col] : 1;
        } else if (dDiff == 2) {
          // 2번 회전해야 하는 경우: 회전 2 + 직진 1
          candidate = dist[row][col] + 3;
        } else {
          // 1번만 회전해도 되는 경우: 회전 1 + 직진 1
          candidate = dist[row][col] + 2;
        }

        if (nRow == dRow && nCol == dCol) {
          // 다음에 도달하는 곳이 목표 지점인 경우
          // 목표 방향까지 고려해서 필요한 거리를 계산한다.
          int eDiff = Math.abs(nDir - dDir);
          if (eDiff == 0) {
            candidate = candidate;
          } else if (eDiff == 2) {
            candidate = candidate + 2;
          } else {
            candidate = candidate + 1;
          }
        }

        if (candidate < dist[nRow][nCol]) {
          // 유망한 좌표는 큐에 삽입한다.
          queue.offer(new Integer[]{nRow, nCol});
          dist[nRow][nCol] = candidate;
          // 방향은 전부 같다.
          directions[nRow][nCol] = nDir;
        }
      }
    }
    return dist[dRow][dCol];
  }

  // 유효성 검사(이동이 가능한지 여부를 판단)를 위한 메서드
  public static boolean isValid(int row, int col, int R, int C) {
    return (row >= 0 && row < R && col >= 0 && col < C);
  }

  // 2차원 배열 N X N
  public static int[][] rotateMatrix(int[][] matrix, int K) {
    if (matrix == null || matrix.length == 0 || K % 4 == 0) return matrix;

    int M = matrix.length;
    int N = matrix[0].length;

    int R = K % 4; // 변환각
    int[][] result = R % 2 == 1 ? new int[N][M] : new int[M][N];

    for (int i = 0; i < result.length; i++) {
      for (int j = 0; j < result[0].length; j++) {
        if (R == 1) // 90
          result[i][j] = matrix[M - j - 1][i];
        if (R == 2) // 180
          result[i][j] = matrix[M - i - 1][N - j - 1];
        if (R == 3) // 270
          result[i][j] = matrix[j][N - i - 1];
      }
    }

    return result;
  }

  public static int primePassword(int curPwd, int newPwd) {
    if (curPwd == newPwd) return 0;
    Queue<int[]> queue = new LinkedList<>();

    boolean[] isVisited = new boolean[10000];
    isVisited[curPwd] = true;

    queue.offer(new int[]{0, curPwd});

    while (!queue.isEmpty()) {
      int[] data = queue.poll();
      int step = data[0];
      int num = data[1];

      for (int i = 0; i < 4; i++) {
        int[] digits = splitNumber(num);

        for (int d = 0; d < 10; d++) {
          if (d != digits[i]) {
            digits[i] = d;

            int next = joinDigits(digits);
            if (next == newPwd) return step + 1;
            if (next > 1000 && isPrime(next) && !isVisited[next]) {
              isVisited[next] = true;
              queue.offer(new int[]{step + 1, next});
            }
          }
        }
      }
    }

    return -1;
  }

  public static boolean isPrime(int num) {
    for (int i = 2; i * i <= num; i++) if (num % i == 0) return false;
    return true;
  }

  public static int[] splitNumber(int num) {
    return Stream.of(String.valueOf(num).split("")).mapToInt(Integer::parseInt).toArray();
  }

  public static int joinDigits(int[] arr) {
    String[] tempArr = new String[arr.length];

    for (int i = 0; i < arr.length; i++) {
      tempArr[i] = String.valueOf(arr[i]);
    }

    return Integer.parseInt(String.join("", tempArr));
  }

  public static int getItemTwoSortedArray(int[] arr1, int[] arr2, int k) {
    int leftIdx = 0;
    int rightIdx = 0;

    while (k > 0) {
      int cnt = (int) Math.ceil((double) k / 2);
      int leftStep = cnt;
      int rightStep = cnt;

      if (leftIdx == arr1.length) {
        rightIdx = rightIdx + k;
        break;
      }

      if (rightIdx == arr2.length) {
        leftIdx = leftIdx + k;
        break;
      }

      if (cnt > arr1.length - leftIdx) leftStep = arr1.length - leftIdx;
      if (cnt > arr2.length - rightIdx) rightStep = arr2.length - rightIdx;

      if (arr1[leftIdx + leftStep - 1] < arr2[rightIdx + rightStep - 1]) {
        leftIdx = leftIdx + leftStep;
        k = k - leftStep;
      } else {
        rightIdx = rightIdx + rightStep;
        k = k - rightStep;
      }
    }

    int leftMax = leftIdx - 1 != -1 ? arr1[leftIdx - 1] : -1;
    int rightMax = rightIdx - 1 != -1 ? arr2[rightIdx - 1] : -1;

    return Math.max(leftMax, rightMax);
  }

  public static String spiralTraversal(Character[][] matrix) {
    class Pointer {
      private int row;
      private int col;
      private final int dir;

      public Pointer(int row, int col, int dir) {
        this.row = row;
        this.col = col;
        this.dir = dir;
      }

      public int getRow() {
        return row;
      }

      public void setRow(int row) {
        this.row = row;
      }

      public int getCol() {
        return col;
      }

      public void setCol(int col) {
        this.col = col;
      }

      public int getDir() {
        return dir;
      }
    }

    final int M = matrix.length;
    final int N = matrix[0].length;
    boolean[][] visited = new boolean[M][N];
    int[][] dir = new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}}; // 우, 하, 좌, 상
    StringBuilder result = new StringBuilder();

    Queue<Pointer> q = new LinkedList<>();
    // 출발 위치 지정
    q.add(new Pointer(0, 0, 0));
    // 방문(출발) 위치 확인
    visited[0][0] = true;
    result.append(matrix[0][0].toString());

    int whileCount = 0;

    while (whileCount < M * N && !q.isEmpty()) {
      Pointer cur = q.poll();

      int loofCount = (cur.getDir() == 0 || cur.getDir() == 2) ? N : M;

      for (int i = 0; i < loofCount; i++) {
        int nR = cur.getRow() + dir[cur.getDir()][0];
        int nC = cur.getCol() + dir[cur.getDir()][1];

        if (nR < 0 || nR > M - 1 || nC < 0 || nC > N - 1) continue;
        if (visited[nR][nC]) continue;

        visited[nR][nC] = true;
        result.append(matrix[nR][nC]);

        cur.setRow(nR);
        cur.setCol(nC);
      }

      q.add(new Pointer(cur.getRow(), cur.getCol(), (cur.getDir() + 1) % 4));

      whileCount++;
    }

    return result.toString();
  }

  // Largest Sum of Contiguous Subarray
  public static int LSCS(int[] arr) {
    int subArrSum = 0;
    int max = Integer.MIN_VALUE;

    for (int j : arr) {
      subArrSum = subArrSum + j;

      if (subArrSum > max) max = subArrSum;
      if (subArrSum < 0) subArrSum = 0;
    }

    return max;
  }

  public static int subsetSum(int[] set, int bound) {
    int max = 0;
    // 집합의 원소들로 만들 수 있는 합의 조합을 관리하는 배열
    // bound는 최대 300이므로, 배열의 크기를 301로 설정한다.
    // 300 + 1 로 적는 이유는 가독성과 배열 인덱스를 직관적으로 관리하기 위함
    // 원소들을 통해 sum을 만들 수 있는 경우, bound[sum]을 true로 설정한다.

    boolean[] cached = new boolean[301];
    for (int member : set) {
      // 집합의 원소를 차례대로 검사
      // 이전 단계까지 검사한 원소들로 만들 수 있는 합의 조합은 cached에 저장되어 있다.
      // cached의 요소에 각각 member를 더한 값을 만들 수 있다.
      ArrayList<Integer> reachables = new ArrayList<>();
      // 이 중에서 bound를 넘어가는 값은 고려하지 않는다.
      // reachables로 따로 관리하는 이유는 중복 계산을 피하기 위함
      for (int wanted = 1; wanted <= bound - member; wanted++) {
        if (cached[wanted]) {
          int reached = wanted + member;
          reachables.add(reached);
          if (reached > max) max = reached;
        }
      }

      // bound 이하로 만들 수 있는 합의 조합을 cached에 추가한다.
      for (int data : reachables) cached[data] = true;

      // 집합의 원소를 마지막에 cached에 추가하는 이유는 중복 계산을 방지하기 위함
      if (member <= bound) {
        cached[member] = true;
        if (member > max) max = member;
      }
    }

    return max;
  }

  // Longest Increasing Subsequence
  public static int LIS(int[] arr) {
    int N = arr.length;
    // lis[i]는 i에서 끝나는 LIS의 길이를 저장
    // 최소한 각 요소 하나로 LIS를 만들 수 있으므로 1로 초기화한다.
    int[] lis = new int[N];
    Arrays.fill(lis, 1);
    for (int i = 1; i < N; i++) {
      // i에서 끝나는 LIS의 길이
      for (int j = 0; j < i; j++) {
        // i 이전의 인덱스만 검사하면 된다.
        // i는 1부터 시작하므로, 짧은 길이부터 검사한다. (bottom-up 방식)
        if (arr[i] > arr[j] && lis[i] < lis[j] + 1) {
          lis[i] = lis[j] + 1;
        }
      }
    }

    return Arrays.stream(lis).max().getAsInt();
  }

  // Longest Prefix which is also Suffix
  public static int LPS(String str) {
    if (str.length() < 2) return 0;

    // 문자열을 두 부분으로 나누고
    // 각 부분의 첫 인덱스를 저장
    int leftIdx = 0;
    // 문자열의 길이가 홀수일 수 있으므로, 올림한다.
    int rightIdx = (str.length() / 2);

    while (rightIdx < str.length()) {
      if (str.charAt(leftIdx) == str.charAt(rightIdx)) {
        // LPS 검사를 시작한 위치부터 지금까지 계속 같은 경우
        // 다음 문자도 같은지 확인하기 위해 인덱스를 이동한다.
        leftIdx++;
        rightIdx++;
      } else {
        // leftIdx가 0인 경우, 단순히 rightIdx를 1 증가 (suffix의 시작점을 뒤로 한 칸 이동)
        // prefix, suffix가 계속 일치하다가 중간에서 일치하지 않는 경우에도,
        // 현재 suffix의 시작점을 뒤로 한 칸 이동한다.
        rightIdx = rightIdx - leftIdx + 1;
        leftIdx = 0;
      }
    }

    // LPS가 없는 경우
    return leftIdx;
  }

  // Longest Common Subsequence
  public static int LCS(String str1, String str2) {
    int m = str1.length();
    int n = str2.length();

    if (m < n) return LCS(str2, str1);

    int[][] cache = new int[m + 1][n + 1];

    for (int i = 0; i < str1.length(); i++) {
      for (int j = 0; j < str2.length(); j++) {
        if (str1.charAt(i) == str2.charAt(j)) cache[i + 1][j + 1] = cache[i][j] + 1;
        else cache[i + 1][j + 1] = Math.max(cache[i][j + 1], cache[i + 1][j]);
      }
    }

    return cache[m][n];
  }

  // Longest Common Substr
  public static int LongestCommonSubstr(String str1, String str2) {
    int[][] cache = new int[str1.length() + 1][str1.length() + 1];
    int max = -1;

    for (int i = 0; i < str1.length(); i++) {
      for (int j = 0; j < str2.length(); j++) {
        if (str1.charAt(i) == str2.charAt(j)) cache[i + 1][j + 1] = cache[i][j] + 1;

        max = Math.max(cache[i + 1][j + 1], max);
      }
    }

    return max;
  }

  public static int nthUglyNumber(int n) {
    int[] nth = new int[n];

    int i2 = 0;
    int i3 = 0;
    int i5 = 0;

    int m2 = 2;
    int m3 = 3;
    int m5 = 5;

    int next = 1;

    nth[0] = 1;

    for (int i = 1; i < n; i++) {
      next = Math.min(m2, Math.min(m3, m5));

      nth[i] = next;

      if (next == m2) {
        i2 = i2 + 1;
        m2 = nth[i2] * 2;
      }

      if (next == m3) {
        i3 = i3 + 1;
        m3 = nth[i3] * 3;
      }

      if (next == m5) {
        i5 = i5 + 1;
        m5 = nth[i5] * 5;
      }
    }

    return next;
  }

  public static int nthUglyNumberBinarySearch(int n) {
    TreeSet<Long> t = new TreeSet<>() {{
      add(1L);
    }};

    int i = 1;
    while (i < n) {
      long temp = Objects.requireNonNull(t.pollFirst()).intValue();

      t.add(temp * 2);
      t.add(temp * 3);
      t.add(temp * 5);

      i++;
    }

    return Objects.requireNonNull(t.pollFirst()).intValue();
  }

  // Segement Tree && Sparse Table 구현
  public static int[] rangeMin(int[] arr, int[][] range) {
    // O(N)
    // return Arrays.stream(range).map((r) -> {
    //     int result = Integer.MAX_VALUE;
    //     for (int i = r[0]; i <= r[1]; i++) result = Math.min(arr[i], result);
    //
    //     return result;
    //   })
    //    .mapToInt(Integer::valueOf)
    //    .toArray();

    // Segement Tree

    // Sparse Table

    return new int[]{};
  }

  // Algo: PLE(previous less element), NLE(next less element)
  public static int largestRectangularArea(int[] histogram) {
    int largest = 0;
    // 모든 연속된 부분 히스토그램을 고려한다.
    // 밑변의 길이를 부분 히스토그램의 길이로 고정하면, 높이는 가장 낮은 막대의 높이가 된다.
    for (int left = 0; left < histogram.length; left++) {
      // 길이가 1인 막대로 만들 수 있는 직사각형의 넓이는 막대의 높이와 같다.
      int min = histogram[left];
      for (int right = left; right < histogram.length; right++) {
        // left부터 right까지의 히스토그램의 막대 중 가장 낮은 막대의 높이를 구한다.
        if (histogram[right] < min) min = histogram[right];
        // 해당 구간(left ~ right)의 막대를 전부 포함해서 만들 수 있는 직사각형의 넓이를 구한다.
        int area = min * (right - left + 1);
        // 매번 구한 면적을 기존의 면적과 비교해 갱신한다.
        if (area > largest) largest = area;
      }
    }
    return largest;

    // int maxArea = 0;
    // int[] tempPLE = new int[histogram.length];
    // int[] tempNLE = new int[histogram.length];
    // Arrays.fill(tempPLE, -1);
    // Arrays.fill(tempNLE, -1);
    //
    // int[] PLE = findPLE(tempPLE, histogram);
    // int[] NLE = findNLE(tempNLE, histogram);
    //
    // for (int i = 0; i < histogram.length; i++) {
    //   int barsOnLeft = PLE[i] == -1 ? i : i - PLE[i] - 1;
    //   int barsOnRight = NLE[i] == -1 ? histogram.length - 1 - i : NLE[i] - i - 1;
    //   int width = barsOnLeft + barsOnRight + 1;
    //
    //   maxArea = Math.max(maxArea, width * histogram[i]);
    // }
    //
    // return maxArea;
  }

  public static int[] findPLE(int[] PLE, int[] histogram) {
    Stack<Integer> stack = new Stack<>();

    for (int i = 0; i < histogram.length; i++) {
      while (stack.size() > 0 && histogram[stack.get(stack.size() - 1)] >= histogram[i]) {
        stack.pop();
      }

      if (stack.size() > 0) {
        PLE[i] = stack.get(stack.size() - 1);
      }

      stack.push(i);
    }

    return PLE;
  }

  public static int[] findNLE(int[] NLE, int[] histogram) {
    Stack<Integer> stack = new Stack<>();

    for (int i = histogram.length - 1; i >= 0; i--) {
      while (stack.size() > 0 && histogram[stack.get(stack.size() - 1)] >= histogram[i]) {
        stack.pop();
      }

      if (stack.size() > 0) {
        NLE[i] = stack.get(stack.size() - 1);
      }
      stack.push(i);
    }

    return NLE;
  }


  static int front = 0;
  static int rear = 0;
  static int[][] queue;

  public static int gossipProtocol(String[] village, int row, int col) {
    // bfs 구현을 위해 큐를 선언한다.
    // enQueue, deQueue시마다 인덱싱을 다시 하지 않기 위해 순환 큐(circular queue)로 구현한다.
    // queue의 가능한 최대 크기만큼 배열을 선언한다.
    // 문제의 특성에 따라 큐에는 좌표 평면의 한 점이 삽입되고, 한번 삽입된 요소는 두 번 다시 삽입되지 않는다.
    int R = village.length;
    int C = village[0].length();
    // int[][] matrix = createMatrix(village);
    // int[][] matrix = new int[R][C];

    int[][] matrix = Arrays.stream(village).map((v) -> {
        int[] r = new int[v.length()];

        for (int i = 0; i < v.length(); i++) r[i] = v.charAt(i) == '0' ? 0 : 1;

        return r;
      })
       .toArray(int[][]::new);

    System.out.println("stream = " + Arrays.deepToString(matrix));

    int[][] MOVES = new int[][]{
      {-1, 0},  // UP
      {1, 0},   // DOWN
      {0, 1},   // RIGHT
      {0, -1}   // LEFT
    };

    int MAX_SIZE = R * C;
    queue = new int[MAX_SIZE][];
    int cnt = 0;
    enQueue(queue, new int[]{row, col}, MAX_SIZE);

    // 소문이 퍼지는 데 걸리는 시간을 저장한다.
    matrix[row][col] = 0;
    while (isEmpty()) {
      // 큐의 가장 앞 자리의 좌표를 얻는다.
      int[] pos = deQueue(queue, MAX_SIZE);
      row = pos[0];
      col = pos[1];
      cnt = matrix[row][col];

      // 현재 지점을 기준으로 네 방향을 검토한다.
      for(int[] move : MOVES) {
        int rDiff = move[0];
        int cDiff = move[1];
        int nextRow = row + rDiff;
        int nextCol = col + cDiff;

        if(isValid(nextRow, nextCol, R, C) && matrix[nextRow][nextCol] == 1) {
          enQueue(queue, new int[]{nextRow, nextCol}, MAX_SIZE);
          matrix[nextRow][nextCol] = matrix[row][col]+ 1;
        }
      }
    }

    System.out.println(Arrays.deepToString(matrix));

    return cnt;
  }

  public static boolean isEmpty() {
    return front != rear;
  }

  public static void enQueue(int[][] queue, int[] pos, int MAX_SIZE) {
    // 실행 중에 큐가 가득차지는 않기 때문에 별도의 조건문을 작성할 필요가 없다.
    queue[rear] = pos;
    // 모듈러스 연산을 처리할 필요는 사실 없다.
    rear = (rear + 1) % MAX_SIZE;
  }

  public static int[] deQueue(int[][] queue, int MAX_SIZE) {
    int[] pos = queue[front];

    front = (front + 1) % MAX_SIZE;

    return pos;
  }

  public static int[][] createMatrix(String[] village) {
    int[][] matrix = new int[village.length][];

    for(int i = 0; i < village.length; i++) {
      String str = village[i];

      int[] row = new int[str.length()];

      for (int j = 0; j < str.length(); j++) {
        row[j] = Character.getNumericValue(str.charAt(j));
      }

      matrix[i] = row;
    }

    return matrix;
  }

  public static int[][] sudoku(int[][] board) {
    solve(board);

    return board;
  }

  public static boolean solve(int[][] board) {
    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        if (board[i][j] == 0) {
          for (int c = 1; c <= 9; c++)
            if (isPossible(board, i, j, c)) {
              board[i][j] = c;

              if (solve(board)) return true;
              else board[i][j] = 0;
            }

          return false;
        }
      }
    }

    return true;
  }

  public static boolean isPossible(int[][] board, int row, int col, int c) {
    for (int i = 0; i < 9; i++) {
      if (board[row][i] == c) return false;
      if (board[i][col] == c) return false;
      if (board[3 * (row / 3) + (i / 3)][3 * (col / 3) + (i % 3)] == c) return false;
    }

    return true;
  }

  static class FindMinimumTimeToFinishAllJobs {
    static int min = Integer.MAX_VALUE;

    protected static int minimumTimeRequired(int[] jobs, int k) {
      recurse(jobs, jobs.length - 1, new int[k]);

      return min;
    }

    protected static void recurse(int[] jobs, int index, int[] sum) {
      if (index < 0) {
        min = Math.min(min, Arrays.stream(sum).max().orElse(0));
        return;
      }

      if (Arrays.stream(sum).max().orElse(0) >= min) return;

      for (int i = 0; i < sum.length; i++) {
        if (i > 0 && sum[i] == sum[i - 1]) continue;

        sum[i] += jobs[index];
        recurse(jobs, index - 1, sum);
        sum[i] -= jobs[index];
      }
    }

    // 총 5개의 작업을 3명이서 작업한다고 가정한다.
    // 첫번째 작업자는 최대 3개의 작업을 할 수 있다.
    // (jobs, workersNum)으로 표기하면, (jobs는 작업량이 아닌 작업의 인덱스만 표기한다고 한다)
    // 처음은 ([0, 1, 2, 3, 4], 3)인 상태이다.
    //  1) 첫번째 작업자가 1개의 작업을 하고 나머지 작업을 2명이 작업
    //    => ([1, 2, 3, 4], 2)
    //  2) 첫번째 작업자가 2개의 작업을 하고 나머지 작업을 2명이 작업
    //    => ([2, 3, 4], 2)
    //  3) 첫번째 작업자가 3개의 작업을 하고 나머지 작업을 2명이 작업
    //    => ([3, 4], 2)
    // 아래 두 가지 경우를 통해, 문제가 중복되어 계산된다는 것을 알 수 있다.
    //  1-1) 첫번째 작업자가 1개의 작업을 하고, 그 다음 작업자가 2개의 작업을 한 경우
    //    => ([3, 4], 1)
    //  2-1) 첫번째 작업자가 2개의 작업을 하고, 그 다음 작업자가 1개의 작업을 한 경우
    //    => ([3, 4], 1)
    // 메모이제이션을 통해 중복 계산을 피한다.
    public int jobAllocation(int[] jobs, int k){
      // memo[i][j]는 i번째 worker가 j번째 job부터 작업한다고 할 때,
      // 최대 작업량이 최소가 되는 분배에서의 최대 작업량을 저장한다.
      // i, j 모두 인덱스이므로 0부터 시작
      int[][] memo = new int[k][];

      for(int i = 0; i < k; i++) {
        int[] arr = new int[jobs.length];
        Arrays.fill(arr, -1);
        memo[i] = arr;
      }

      // 마지막 작업자는 남아있는 모든 작업을 다 해야하므로 쉽게 계산이 가능하다.
      // 마지막 작업자는 최대 나머지 작업자의 수만큼을 제외한 일만 할 수 있다.
      int workload = 0;

      for (int i = jobs.length - 1; i >= k - 1; i--) {
        workload = workload + jobs[i];
        memo[k - 1][i] = workload;
      }

      return aux(0, 0, jobs, k - 1, memo);
    }

    public int aux(int workerIdx, int jobIdx, int[] jobs, int left, int[][] memo) {
      // 이미 계산한 적이 있는 경우, 다시 풀지 않는다
      // 마지막 작업자의 작업량을 전부 계산했으므로, 탈출 조건을 굳이 작성하지 않아도 된다.
      if (memo[workerIdx][jobIdx] != -1) return memo[workerIdx][jobIdx];

      int workload = 0;
      int min = Integer.MAX_VALUE;
      for (int i = jobIdx; i < jobs.length - left; i++) {
        workload = workload + jobs[i];
        // 가장 많이 일하는 사람의 작업량을 구한다.
        int hardest = Math.max(
          workload,
          aux(workerIdx + 1, i + 1, jobs, left - 1, memo)
        );
        // 그 작업량이 최소화되는 분배에서 최대 작업량을 구한다.
        min = Math.min(min, hardest);
      }
      memo[workerIdx][jobIdx] = min;
      return min;
    }
  }

  public static int longestPalindrome(String str) {
    if (str.length() < 2) return str.length();

    int LENGTH = str.length();
    boolean[][] isPalindrome = new boolean[LENGTH][];
    for(int i = 0; i < isPalindrome.length; i++) {
      isPalindrome[i] = new boolean[LENGTH];
    }

    int maxLen = 1;
    // 길이가 1인 회문
    for (int i = 0; i < LENGTH; i++) isPalindrome[i][i] = true;

    // 길이가 2인 회문
    for (int i = 0; i < LENGTH - 1; i++) {
      if (str.charAt(i) == str.charAt(i + 1)) {
        isPalindrome[i][i + 1] = true;
        maxLen = 2;
      }
    }

    // 길이가 3 이상인 회문
    for (int i = 3; i <= LENGTH; i++) {
      for (int startIdx = 0; startIdx <= LENGTH - i; startIdx++) {
        int endIdx = startIdx + i - 1;
        if (isPalindrome[startIdx + 1][endIdx - 1] && str.charAt(startIdx) == str.charAt(endIdx)) {
          isPalindrome[startIdx][endIdx] = true;
          maxLen = i;
        }
      }
    }

    return maxLen;
  }


  public static void main(String[] args) {
    // # 72의 법칙
    // System.out.println(ruleOf72(12.8));

    // # 거듭제곱의 성질
    // System.out.println(pow(3, 3));

    // # 2의 제곱근
    // System.out.println(p2(8));

    // # 문자열 관련 문제
    // System.out.println(firstChar("Hello World!"));
    // System.out.println(reverseString("Hello World!"));
    // System.out.println(firstCapitalize("Hello World!"));
    // System.out.println(letterCapitalize("hello    world   !!!"));
    // System.out.println(convertDoubleSpaceToSingle("Hello  World!!!"));
    // System.out.println(insertDash("112233445566778899"));
    // System.out.println(Arrays.toString(removeExtremes(new String[]{"1", "456", "789", "123", "2", "3"})));
    // System.out.println(Arrays.toString(reverseArr(new int[] {1, 2, 3, 4, 5})));
    // System.out.println(readVertically(new String[] {"12", "4567", "789"}));
    // System.out.println(superIncreasing(new int[] {-10, 1, 3, 5, 13, 52}));
    // System.out.println(modulo(123456789, 67));
    // System.out.println(isIsogram("Mouse"));
    // System.out.println(computeSquareRoot(9));
    // System.out.println(computeSquareRoot(9));
    // System.out.println(numberSearch("YlQO uT9"));
    // System.out.println(decryptCaesarCipher("Aa Bb", 27));
    // System.out.println(compressString("wwwggoppopppp"));

    // # 배열
    // System.out.println(rotatedArraySearch(new int[]{4, 5, 6, 0, 1, 2, 3}, 2));
    // System.out.println(Arrays.toString(powerSet("abc").toArray()));
    // System.out.println(Arrays.toString(powerSet("bac").toArray()));
    // System.out.println(orderOfPresentation(3, new int[] {3, 1, 2}));

    // System.out.println(largestProductOfThree(new int[] {-5, -4, -3, -1, 999, 10000}));
    // System.out.println(largestProductOfThree(new int[] {-50, -20, -30, -5, 40}));
    // System.out.println(largestProductOfThree(new int[] {2, 3, -11, 7, 5, -13}));
    // System.out.println(fibonacci(7));
    // System.out.println(isSubsetOf(new int[] {1, 2, 3, 4, 5}, new int[] {3}));
    // System.out.println(pow(5, 22));
    // System.out.println(balancedBrackets("[][][]()()(){}{}{}"));

    // System.out.println(LCS("abef", "abde"));
    // System.out.println(LongestCommonSubstr("abc", "abc"));
    // System.out.println(nthUglyNumber(10));

    // System.out.println(Arrays.toString(rangeMin(new int[] {9, 2, 5, 6, 7, 1}, new int[][] {{1, 3}, {0, 5}})));
    // System.out.println(largestRectangularArea(new int[]{5, 4, 5}));

    // int[][] room = new int[][] {
    //   {0, 0, 0, 0, 0, 0},
    //   {0, 1, 1, 0, 1, 0},
    //   {0, 1, 0, 0, 0, 0},
    //   {0, 0, 1, 1, 1, 0},
    //   {1, 0, 0, 0, 0, 0}
    // };
    // int[] src = new int[]{0, 0};
    // int[] dst = new int[]{2, 2};
    // System.out.println(robotPath(room, src, dst));

    // int[][] room = new int[][] {
    //   {0, 0, 0, 0, 0, 0},
    //   {0, 1, 1, 0, 1, 0},
    //   {0, 1, 0, 0, 0, 0},
    //   {0, 0, 1, 1, 1, 0},
    //   {1, 0, 0, 0, 0, 0},
    // };
    // int[] src = new int[]{4, 2};
    // int[] dst = new int[]{2, 2};
    // // Dir: 1(Top), 2(Right), 3(Bottom), 4(Left)
    // int sDir = 1;
    // int dDir = 3;
    // System.out.println(robotPath2(room, src, sDir, dst, dDir));

    // System.out.println(Arrays.deepToString(
    //       rotateMatrix(new int[][]{
    //           {1, 2},
    //           {3, 4}
    //         },
    //         2
    //       )
    //     )
    //                      .replace("[[", "[\n  [")
    //                      .replace("], ", "]\n  ")
    //                      .replace("]]", "]\n]")
    // );

    // System.out.println(primePassword(1033, 3377));

    // System.out.println("getItemTwoSortedArray() = " + getItemTwoSortedArray(
    //     new int[] {1, 2, 3},
    //     new int[] {1, 2, 3},
    //     3
    //   )
    // );

    // System.out.println(spiralTraversal(new Character[][]{
    //   {'A', 'B', 'C', 'D', 'E'},
    //   {'A', 'B', 'C', 'D', 'E'},
    //   {'A', 'B', 'C', 'D', 'E'},
    //   {'A', 'B', 'C', 'D', 'E'},
    //   {'A', 'B', 'C', 'D', 'E'},
    //   {'A', 'B', 'C', 'D', 'E'},
    //   {'A', 'B', 'C', 'D', 'E'},
    //   {'A', 'B', 'C', 'D', 'E'}
    // }));

    // System.out.println(LSCS(new int[]{1, 2, 3, -1, 4, 5, 6}));

    // System.out.println(subsetSum(new int[]{1, 8, 3, 15}, 10));

    // System.out.println(
    //   gossipProtocol(
    //     new String[]{
    //       "0101",
    //       "0111",
    //       "0110",
    //       "0100"
    //     },
    //     1,
    //     2
    //   )
    // );

    // System.out.println(
    //   Arrays.deepToString(
    //     sudoku(new int[][] {
    //       {9, 2, 0,  0, 0, 0,  0, 0, 5},
    //       {0, 0, 3,  0, 7, 8,  4, 0, 0},
    //       {0, 0, 0,  6, 0, 0,  0, 0, 0},
    //
    //       {0, 0, 0,  1, 0, 0,  9, 0, 0},
    //       {0, 0, 6,  4, 0, 0,  0, 0, 0},
    //       {0, 3, 0,  0, 9, 6,  0, 2, 0},
    //
    //       {7, 0, 0,  0, 0, 0,  0, 4, 0},
    //       {0, 0, 8,  0, 3, 9,  7, 0, 0},
    //       {0, 0, 0,  0, 1, 0,  0, 0, 0}
    //     })
    //   )
    //     .replace("[[", "[\n  [")
    //     .replace("], ", "]\n  ")
    //     .replace("]]", "]\n]")
    // );

    // System.out.println(FindMinimumTimeToFinishAllJobs.minimumTimeRequired(new int[] {1, 2, 3, 4, 5, 6, 7}, 3));

    System.out.println(longestPalindrome("My dad is a racecar athlete"));

    // # 변환
    // System.out.println(
    //   convertListToHashMap(new String[][] {})
    // );
  }
}
