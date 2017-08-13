package com.n26.challenge.repository;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.springframework.stereotype.Repository;

import com.n26.challenge.model.Transaction;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

  /**
   * An inmemory queue containing all transactions. A queue is used to ensure that the constant time
   * and memory execution requirement is met. It is a deque so that we can use the descending
   * iterator which will make us win lots of time when finding the new min or the new max.
   * ConcurrentLinkedDeque is threadsafe with concurrent requests
   *
   */
  private ConcurrentLinkedDeque<Transaction> transactions =
      new ConcurrentLinkedDeque<Transaction>();



  public void addTransaction(Transaction transaction) {
    transactions.add(transaction);
  }

  public Iterator<Transaction> getDescendingIterator() {
    return transactions.descendingIterator();
  }

  public boolean isEmpty() {
    return transactions.isEmpty();
  }


}
