package Gr8G1.prac.playground.algorithm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class PrBSF {
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
