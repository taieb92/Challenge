package com.n26.challenge.service;



public class ReadWriteLock {



  private int readers = 0;
  private int writers = 0;
  private int writeRequests = 0;

  public synchronized void lockRead() {
    while (writers > 0 || writeRequests > 0) {
      try {
        wait(50);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    readers++;
  }

  public synchronized void unlockRead() {
    readers--;
    notifyAll();
  }

  public synchronized void lockWrite() {
    writeRequests++;

    while (readers > 0 || writers > 0) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    writeRequests--;
    writers++;
  }

  public synchronized void unlockWrite() {
    writers--;
    notifyAll();
  }


}
