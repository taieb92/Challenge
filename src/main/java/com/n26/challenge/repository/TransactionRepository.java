package com.n26.challenge.repository;

import java.util.Iterator;

import com.n26.challenge.model.Transaction;

public interface TransactionRepository {

  /**
   * add a transaction to the inmemory data
   *
   *
   * @param transaction a {@link Transaction} containing the transaction details
   */
  void addTransaction(Transaction transaction);


  /**
   * Allow us to iterate our deque and get the data LIFO
   *
   * @return a descending iterator for our duque
   */
  Iterator<Transaction> getDescendingIterator();



  /**
   * Returns true if this collection contains no elements.
   *
   * @return true if this collection contains no elements.
   */
   boolean isEmpty();

}
