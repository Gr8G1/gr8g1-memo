package Gr8G1.prac.pojo.datastructure.impl;

import java.util.*;

public class PrBufferQueue {
  private final int QUEUE_SIZE;
  private final int[][] Q;
  private int front;
  private int rear;
  private int size = 0;

  public PrBufferQueue(int qSize) {
    this.QUEUE_SIZE = qSize;
    this.Q = new int[QUEUE_SIZE][2];
    this.front = 0;
    this.rear = 0;
  }

  public boolean isFull() {
    return size >= QUEUE_SIZE;
  }

  public boolean isEmpty() {
    return size <= 0;
  }

  public int size() {
    return size;
  }

  public int getBuffer() {
    return Q[front][0];
  }

  public void upBuffer() {
    for(int i = 0; i < Q.length; i++) {
      int buffer = Q[i][0];

      if (buffer != 0) Q[i][0]++;
    }
  }

  public int capacity() {
    int[] nestArr = Arrays.stream(Q).mapToInt(n -> n[1]).toArray();
    return Arrays.stream(nestArr).sum();
  }

  public void push(int[] el) {
    if (isFull()) return;

    Q[rear] = el;
    rear = (rear + 1) % QUEUE_SIZE;
    size++;
  }

  public void shift() {
    if (isEmpty()) return;

    Q[front][1] = 0;
    front = (front + 1) % QUEUE_SIZE;
    size--;
  }

  public static int queuePrinter(int bufferSize, int capacities, int[] documents) {
    PrBufferQueue q = new PrBufferQueue(bufferSize);

    int time = 1;
    int docIdx = 0;

    q.push(new int[] {1, documents[docIdx++]});

    while (q.size() > 0) {
      time++;
      q.upBuffer();

      if (q.getBuffer() > bufferSize) q.shift();
      if (docIdx > documents.length - 1) continue;

      int capa = q.capacity();
      int size = q.size();

      if (capacities >= (documents[docIdx] + capa) && bufferSize > size) {
        q.push(new int[] {1, documents[docIdx++]});
      }
    }

    return time;
  }

  public static void main(String[] args) {
    // # BufferQueue
    System.out.println(queuePrinter(10, 100, new int[] {10, 10, 10, 10}));
  }
}
