package Gr8G1.prac.playground.algorithm;

import java.util.ArrayList;

public class PrDFS {
  /*
   * # 깊이 우선 탐색(Depth-First Search, DFS)
   * 그래프 탐색 알고리즘, 이 알고리즘들은 그래프에서 정점을 탐색하거나 경로를 찾는 데 사용된다.
   *
   * - 시작 정점에서부터 한 정점의 인접한 정점을 재귀적으로 탐색한 후, 다음 인접한 정점을 탐색한다.
   * - 스택(Stack) 자료구조를 사용하여 구현된다. 재귀 호출도 스택의 원리를 사용한다.
   * - 그래프에서 한 경로를 끝까지 탐색한 후, 되돌아와서 다른 경로를 탐색한다.
   * - 싸이클 탐지, 트리 순회 등의 문제에 주로 사용된다.
   *
   */
  static ArrayList<String> lists = new ArrayList<>();

  public static ArrayList<String> dfs(tree node) {
    lists.add(node.getValue());

    if (node.getChildrenNode() != null) for (tree t: node.getChildrenNode()) dfs(t);

    // if (node.getChildrenNode() != null) Optional.ofNullable(node.getChildrenNode().get()).ifPresent(PrDFS::dfs);
    // if (node.getChildrenNode() != null) Optional.ofNullable(node.getChildrenNode().get(1)).ifPresent(PrDFS::dfs);

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
      if (children == null) children = new ArrayList<>();
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
    tree leaf3 = leaf1.addChildNode(new tree("6"));
    tree leaf4 = leaf1.addChildNode(new tree("7"));

    System.out.println(dfs(root));
  }
}
