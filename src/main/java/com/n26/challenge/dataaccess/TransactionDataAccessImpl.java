package com.n26.challenge.dataaccess;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.n26.challenge.model.Transaction;
import com.n26.challenge.repository.TransactionRepository;

@Component
public class TransactionDataAccessImpl implements TransactionDataAccess {
  private static final Logger log = LoggerFactory.getLogger(TransactionDataAccessImpl.class);

  /**
   * The duration of a valid transaction in milliseconds
   */
  public static final long VALID_TRANSACTION_DURATION = 60000L;

  @Autowired
  protected TransactionRepository transactionRepository;

  public void addTransaction(Transaction transaction) {
    log.debug("transaction {} added to the queue", transaction);
    transactionRepository.addTransaction(transaction);
  }



  public double getMaxTransactionValue(long timestamp, long duration) {

    if (transactionRepository.isEmpty()) {
      return Double.NaN;
    }

    Iterator<Transaction> descItr = transactionRepository.getDescendingIterator();
    double max = Double.NaN;
    // iterate decreasing from the last added transaction
    while (descItr.hasNext()) {
      Transaction transaction = descItr.next();
      // new max if it is greater that the old one and its transaction was made in the
      // STATISTICS_CALCULATOR_DURATION
      if ((Double.isNaN(max) || transaction.getAmount() > max)
          && transaction.getTimestamp() + duration > timestamp
          && transaction.getTimestamp() < timestamp) {
        max = transaction.getAmount();
      }
      // stop iterating if the transaction returned by the iterator is older than the
      // STATISTICS_CALCULATOR_DURATION + VALID_TRANSACTION_DURATION as no more valid transaction
      // will be found afterwards anyway.
      else if (transaction.getTimestamp() + duration + VALID_TRANSACTION_DURATION < timestamp) {
        return max;
      }

    }

    return max;
  }

  public double getMinTransactionValue(long timestamp, long duration) {

    if (transactionRepository.isEmpty()) {
      return Double.NaN;
    }

    // same logic as last method
    Iterator<Transaction> descItr = transactionRepository.getDescendingIterator();
    double min = Double.NaN;
    while (descItr.hasNext()) {
      Transaction transaction = descItr.next();
      if ((Double.isNaN(min) || transaction.getAmount() < min)
          && transaction.getTimestamp() + duration > timestamp
          && transaction.getTimestamp() < timestamp) {

        min = transaction.getAmount();
      } else if (transaction.getTimestamp() + duration + VALID_TRANSACTION_DURATION < timestamp) {
        return min;
      }

    }

    return min;
  }



  /**
   * @return the transactionRepository
   */
  public TransactionRepository getTransactionRepository() {
    return transactionRepository;
  }



  /**
   * @param transactionRepository the transactionRepository to set
   */
  public void setTransactionRepository(TransactionRepository transactionRepository) {
    this.transactionRepository = transactionRepository;
  }



}
