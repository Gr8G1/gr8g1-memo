package Gr8G1.prac.playground.algorithm;

import java.util.ArrayList;

public class PrDFS {
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
