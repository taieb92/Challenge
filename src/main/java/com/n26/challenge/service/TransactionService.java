package com.n26.challenge.service;

import com.n26.challenge.exceptions.ChallengeException;
import com.n26.challenge.model.Transaction;


/**
 * <p>
 * This service manages the logic of transactions.
 * </p>
 */
public interface TransactionService {

  /**
   *
   * Adds a transaction to the repository after verifying the transaction validity.
   *
   * @param transaction the {@link Transaction} details (time and value).
   * @throws ChallengeException if the transaction is older that 60 seconds or if it is in the
   *         future.
   */
   void addTransaction(Transaction transaction) throws ChallengeException;

}
