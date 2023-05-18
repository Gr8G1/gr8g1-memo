package Gr8G1.prac.playground.algorithm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class PrBSF {
  /*
   * # 넓이 우선 탐색(Breadth-First Search, BFS)
   * 그래프 탐색 알고리즘, 이 알고리즘들은 그래프에서 정점을 탐색하거나 경로를 찾는 데 사용된다.
   *
   * - 시작 정점에서부터 인접한 정점들을 먼저 모두 방문한 후, 해당 정점들의 인접한 정점들을 순차적으로 방문한다.
   * - 큐(Queue) 자료구조를 사용하여 구현된다.
   * - 최단 경로를 찾는 문제나 상태 공간 탐색 문제에 주로 사용된다.
   * - 그래프에서 동일 레벨에 있는 정점들을 모두 탐색하고, 다음 레벨로 이동하여 탐색을 진행한다.
   *
   */
  public static ArrayList<String> bfs(tree node) {
    ArrayList<String> lists = new ArrayList<>();
    Queue<tree> q = new LinkedList<>();

    q.add(node);

    while (q.size() != 0) {
      tree t = q.poll();
      lists.add(t.getValue());
      if (t.getChildrenNode() != null) q.addAll(t.getChildrenNode());
    }

    return lists;
  }

  public static class tree {
    private String value;
    private ArrayList<tree> children;

    public tree(String data) {
      this.value = data;
      this.children = null;
    }

    public tree addChildNode(tree node) {
      if(children == null) children = new ArrayList<>();
      children.add(node);
      return children.get(children.size() - 1);
    }

    public String getValue() {
      return value;
    }

    public ArrayList<tree> getChildrenNode() {
      return children;
    }
  }

  public static void main(String[] args) {
    tree root = new tree("1");
    tree rootChild1 = root.addChildNode(new tree("2"));
    tree rootChild2 = root.addChildNode(new tree("3"));
    tree leaf1 = rootChild1.addChildNode(new tree("4"));
    tree leaf2 = rootChild1.addChildNode(new tree("5"));
    System.out.println(bfs(root));
  }
}
