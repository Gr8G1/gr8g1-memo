package Gr8G1.prac.pojo.thread;

import java.util.*;
import java.util.function.IntUnaryOperator;

public class PrThread {
  public static void main(String[] args) {
    CoinExchange coinExchange = new CoinExchange(new ArrayList<>(Arrays.asList(1, 2, 3)));

    Thread cc = new CoinConsumer(coinExchange); // Thread 클래스 상속
    Thread cs = new Thread(new CoinSupplier(coinExchange)); // Runnable 인터페이스 구현

    cc.setName("ConinConsumer");
    cc.setPriority(1);
    cs.setName("CoinSupplier");
    cs.setPriority(9);

    cc.start();
    cs.start();
  }
}

class CoinExchange {
  private final ArrayList<Integer> coins;
  final int MAX_COIN_COUNT = 5;
  int supplyCount;

  public CoinExchange(ArrayList<Integer> coins) {
    this.coins = coins;
  }

  public ArrayList<Integer> getCoins() {
    return coins;
  }

  public static IntUnaryOperator genRandomCoin() {
    return (v) -> (int) (Math.random() * v) + 1;
  }

  public void addCoin(int coin) {
    synchronized (this) {
      if (coins.size() >= this.MAX_COIN_COUNT) {
        System.out.println("😪 " + Thread.currentThread().getName()+" is waiting.");

        try {
          wait(); // CoinSupplier Waiting-pool 이동
          Thread.sleep(1000);
        } catch (InterruptedException e) {}
      }

      if (this.supplyCount != this.MAX_COIN_COUNT) {
        System.out.printf("🪙 No.%d coin has been added %n", coin);

        coins.add(coin);
        this.supplyCount++;
        notify(); // CoinCunsumer 대기상태 전환
      } else {
        if (!coins.isEmpty()) {
          System.out.println("😪 " + Thread.currentThread().getName()+" is waiting for all the coins to be sold.");

          try {
            wait(); // CoinSupplier Waiting-pool 이동
            Thread.sleep(1000);
          } catch (InterruptedException e) {}
        } else {
          System.out.println("👋 All coins have been sold. Good-Bye~");
          System.exit(0);
        }
      }
    }
  }

  public boolean removeCoin(int coin) {
    synchronized (this) {
      while(coins.size() == 0) {
        System.out.println("😪 " + Thread.currentThread().getName()+" is waiting.");
        try {
          wait(); // CoinCunsumer Waiting-pool 이동
          Thread.sleep(500);
        } catch(InterruptedException e) {}
      }

      for (int i = 0; i < coins.size(); i++) {
        if (coins.get(i) == coin) {
          coins.remove(i);
          notify(); // CoinSupplier 대기상태 전환
          return true;
        }
      }

      return false;
    }
  }
}

// Thread 클래스 상속
class CoinConsumer extends Thread {
  private final CoinExchange coinExchange;

  public CoinConsumer(CoinExchange coinExchange) {
    this.coinExchange = coinExchange;
  }

  @Override
  public void run() {
    while(true) {
      try { Thread.sleep(500); } catch (InterruptedException e) {}
      int randomCoin = CoinExchange.genRandomCoin().applyAsInt(5);

      System.out.println("🪙 Coins: " + this.coinExchange.getCoins());
      if (this.buyCoin(randomCoin)) {
        System.out.printf("😍 Get No.%d Coin!!! %n", randomCoin);
      } else {
        System.out.printf("🤬 Failed to get No.%d Coin!!! %n", randomCoin);
      }
    }
  }

  public boolean buyCoin(int randomCoin) {
    return this.coinExchange.removeCoin(randomCoin);
  }
}

// Runnable 인터페이스 구현
class CoinSupplier implements Runnable {
  private CoinExchange coinExchange;

  public CoinSupplier(CoinExchange coinExchange) {
    this.coinExchange = coinExchange;
  }

  @Override
  public void run() {
    while(true) {
      try { Thread.sleep(1000); } catch (InterruptedException e) {}
      this.supplyCoin();
    }
  }

  public void supplyCoin() {
    this.coinExchange.addCoin(CoinExchange.genRandomCoin().applyAsInt(5));
  }
}
