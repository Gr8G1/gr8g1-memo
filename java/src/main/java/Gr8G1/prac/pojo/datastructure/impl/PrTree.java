package Gr8G1.prac.pojo.datastructure.impl;

import java.util.*;

public class PrTree {
  public static void main(String[] args) {
    binarySearchTree bst = new binarySearchTree();

    bst.insert(3);
    bst.insert(1);
    bst.insert(6);
    bst.insert(2);
    bst.insert(9);
    bst.insert(4);
    bst.insert(10);
    bst.insert(8);

    System.out.println(bst.contains(2));
    System.out.println(bst.contains(4));

    System.out.println(bst.root.getData());
    System.out.println(bst.root.getLeft().getData());
    System.out.println(bst.root.getRight().getData());

    System.out.println(
      bst.preorderTree(bst.root, 0, new ArrayList<>()) +" : "+
      bst.inorderTree(bst.root, 0, new ArrayList<>()) +" : "+
      bst.postorderTree(bst.root, 0, new ArrayList<>())
    );
  }
}

class NewNode {
  private int data;
  private NewNode left;
  private NewNode right;

  NewNode(int data) {
    this.setData(data);

    this.setLeft(null);
    this.setRight(null);
  }

  public int getData() {
    return data;
  }

  public NewNode getLeft() {
    return left;
  }

  public NewNode getRight() {
    return right;
  }

  public void setData(int data) {
    this.data = data;
  }

  public void setLeft(NewNode left) {
    this.left = left;
  }

  public void setRight(NewNode right) {
    this.right = right;
  }
}

class binarySearchTree {
  public NewNode root;

  binarySearchTree() {
    root = null;
  }

  public void insert(int data) {
    NewNode newNode = new NewNode(data);

    if (root == null) {
      root = newNode;
      return;
    }

    if (root.getData() == data) return;

    NewNode currentNode = root;
    NewNode parentNode = null;

    while (true) {
      parentNode = currentNode;

      if (data < currentNode.getData()) {
        currentNode = currentNode.getLeft();

        if (currentNode == null) {
          parentNode.setLeft(newNode);
          return;
        } else if (currentNode.getData() == newNode.getData()) return;
      } else {
        currentNode = currentNode.getRight();

        if (currentNode == null) {
          parentNode.setRight(newNode);
          return;
        } else if (currentNode.getData() == newNode.getData()) return;
      }
    }
  }

  public boolean contains(int data) {
    NewNode currentNode = root;

    while (currentNode != null) {
      if (currentNode.getData() == data) return true;

      if (currentNode.getData() > data) currentNode = currentNode.getLeft();
      else currentNode = currentNode.getRight();
    }

    return false;
  }

  public ArrayList<Integer> preorderTree(NewNode root, int depth, ArrayList<Integer> list) {
    if (root != null) {
      list.add(root.getData());
      preorderTree(root.getLeft(), depth + 1, list);
      preorderTree(root.getRight(), depth + 1, list);
    }

    return list;
  }

  public ArrayList<Integer> inorderTree(NewNode root, int depth, ArrayList<Integer> list) {
    if (root != null) {
      inorderTree(root.getLeft(), depth + 1, list);
      list.add(root.getData());
      inorderTree(root.getRight(), depth + 1, list);
    }

    return list;
  }

  public ArrayList<Integer> postorderTree(NewNode root, int depth, ArrayList<Integer> list) {
    if (root != null) {
      postorderTree(root.getLeft(), depth + 1, list);
      postorderTree(root.getRight(), depth + 1, list);
      list.add(root.getData());
    }

    return list;
  }
}
