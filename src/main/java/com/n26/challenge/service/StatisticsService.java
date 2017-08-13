package com.n26.challenge.service;

import com.n26.challenge.model.Statistics;
import com.n26.challenge.model.Transaction;


/**
 * <p>
 * This service manages the logic returning info about the statistics or editing them.
 * </p>
 */
public interface StatisticsService {

  /**
   * <p>
   * returns the statistic based on the transactions which happened in the last 60 seconds.
   * </p>
   *
   * @return a {@link Statistics} object containing the statistic based on the transactions which
   *         happened in the last 60 seconds.
   *
   */
   Statistics getStatistics();

  /**
   * <p>
   * This method manages the logic updating the statistics by counting to it the given transaction
   * </p>
   *
   * @param transaction the {@link Transaction} details (time and value).
   * @return true if the transaction is added
   */
   boolean addTransactionToStats(Transaction transaction);

  /**
   * <p>
   * This method manages the logic updating the statistics by removing from it the count given
   * transaction
   * </p
   *
   * @param transaction the {@link Transaction} details (time and value).
   * @return true if the transaction is added
   */
   boolean removeTransactionFromStats(Transaction transaction);
}
