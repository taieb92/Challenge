package com.n26.challenge.service;

import java.time.Instant;

import org.assertj.core.util.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.n26.challenge.dataaccess.TransactionDataAccess;
import com.n26.challenge.model.Statistics;
import com.n26.challenge.model.Transaction;

@Service
public class StatisticsServiceImpl implements StatisticsService {


  /**
   * The duration on which we calculate the statistics
   */
  public static final long STATISTICS_CALCULATOR_DURATION = 60000L;

  private static final Logger log = LoggerFactory.getLogger(StatisticsServiceImpl.class);

  /**
   * A {@link Statistics} object that contains the updated statics at any time
   */
  Statistics stats = new Statistics();
  /**
   * A mutual exclusive lock, used so that we make sure that the statistics are threadsafe with
   * concurrent requests
   */
  private final ReadWriteLock lock = new ReadWriteLock();

  @Autowired
  TransactionDataAccess transactionDataAccess;


  public Statistics getStatistics() {
    lock.lockRead();
    try {


      return stats;
    } finally {
      lock.unlockRead();
    }

  }

  @VisibleForTesting
  void setStatistics(Statistics stats) {
    this.stats = stats;
  }

  public boolean addTransactionToStats(Transaction transaction) {
    // lock to update the stats then unlock just after
    lock.lockWrite();

    try {
      log.debug("adding transaction {} to stats", transaction);
      stats.setSum(stats.getSum() + transaction.getAmount());
      // if there is a min, there will be a max
      if (!Double.isNaN(stats.getMin())) {
        stats.setMin(Math.min(stats.getMin(), transaction.getAmount()));
        stats.setMax(Math.max(stats.getMax(), transaction.getAmount()));
      }
      // visversa
      else {
        stats.setMin(transaction.getAmount());
        stats.setMax(transaction.getAmount());

      }

      stats.setCount(stats.getCount() + 1);
    } finally {
      lock.unlockWrite();
    }

    return true;



  }


  public boolean removeTransactionFromStats(Transaction transaction) {
    // lock to update the stats then unlock just after
    lock.lockWrite();


    try {
      log.debug("removing {} from stats", transaction);
      double sum = roundDouble(stats.getSum() - transaction.getAmount());
      stats.setSum(sum);
      if (sum < 1e-4) {
        stats.setMin(Double.NaN);
        stats.setMax(Double.NaN);
        stats.setCount(0);
      } else {
        stats.setCount(stats.getCount() - 1);


        double min = stats.getMin();
        // if the transaction value was the min, we need to find the new min
        if (Math.abs(transaction.getAmount() - stats.getMin()) < 1e-4) {

          Long nowEpochTime = Instant.now().toEpochMilli();
          min =
              transactionDataAccess.getMinTransactionValue(nowEpochTime,
                  STATISTICS_CALCULATOR_DURATION);
        }

        double max = stats.getMax();
        // same here
        if (Math.abs(transaction.getAmount() - stats.getMax()) < 1e-4) {
          Long nowEpochTime = Instant.now().toEpochMilli();
          max =
              transactionDataAccess.getMaxTransactionValue(nowEpochTime,
                  STATISTICS_CALCULATOR_DURATION);
        }

        stats.setMin(min);
        stats.setMax(max);
      }
    } finally {
      lock.unlockWrite();
    }
    return true;
  }


  /**
   * Calculates a double round
   *
   * @param d a floating-point value to be rounded
   * @return Returns the closest long to the argument, with ties rounding to positive infinity.
   */
  private double roundDouble(double d) {

    return Math.round(d * 100) / 100.0d;

  }


  /**
   * @return the transactionDataAccess
   */
  public TransactionDataAccess getTransactionDataAccess() {
    return transactionDataAccess;
  }


  /**
   * @param transactionDataAccess the transactionDataAccess to set
   */
  public void setTransactionDataAccess(TransactionDataAccess transactionDataAccess) {
    this.transactionDataAccess = transactionDataAccess;
  }


}
