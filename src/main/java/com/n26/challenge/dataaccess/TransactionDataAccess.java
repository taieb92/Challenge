package com.n26.challenge.dataaccess;

import com.n26.challenge.model.Transaction;

/**
 * <p>
 * Connects to the repository and ensure the transactions data access and the data manipulation.
 * </p>
 *
 */
public interface TransactionDataAccess {

  /**
   *
   * Adds a transaction to the repository.
   *
   * @param transaction the {@link Transaction} details (time and value).
   */
  void addTransaction(Transaction transaction);

  /**
   * finds the minimum transaction value from the transaction made between the given timestamp and
   * the timestamp minus the duration
   *
   * @param timestamp the latest time to search for the minimum value transaction
   * @param duration the duration or period to search for the minimum value transaction
   * @return the minimum transaction value
   */
  double getMinTransactionValue(long timestamp, long duration);


  /**
   * finds the maximum transaction value from the transaction made between the given timestamp and
   * the timestamp minus the duration
   *
   * @param timestamp the latest time to search for the maximum value transaction
   * @param duration the duration or period to search for the maximum value transaction
   * @return the maximum transaction value
   */
  double getMaxTransactionValue(long timestamp, long duration);
}
