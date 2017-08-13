package com.n26.challenge.service;

import java.time.Instant;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.n26.challenge.dataaccess.TransactionDataAccess;
import com.n26.challenge.exceptions.ChallengeException;
import com.n26.challenge.exceptions.ImpossibleFutureTransactionException;
import com.n26.challenge.exceptions.TransactionTooOldException;
import com.n26.challenge.model.Transaction;


@Service
public class TransactionServiceImpl implements TransactionService {

  /**
   * The duration of a valid transaction inj milliseconds
   */
  public static final long VALID_TRANSACTION_DURATION_IN_MILLISECONDS = 60000L;

  private static final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

  @Autowired
  protected TransactionDataAccess transactionDataAccess;
  @Autowired
  protected StatisticsService statisticsService;

  public void addTransaction(final Transaction transaction) throws ChallengeException {

    Instant now = Instant.now();
    Long lastValidTime = now.minusMillis(VALID_TRANSACTION_DURATION_IN_MILLISECONDS).toEpochMilli();

    // if the transaction was before the last valid time, throw an exception
    if (transaction.getTimestamp() < lastValidTime) {
      log.debug("transaction {} too old", transaction);
      long transactionAge = Instant.now().toEpochMilli() - transaction.getTimestamp();
      throw new TransactionTooOldException("transaction " + transaction + " is " + transactionAge
          + " milliseconds old", log);
    }
    // if the transaction is in the future, throw an exception
    else if (transaction.getTimestamp() > now.toEpochMilli()) {
      log.debug("transaction {} is in the future, this cannot happen", transaction);
      throw new ImpossibleFutureTransactionException("transaction " + transaction
          + " is  in the future, this cannot happen", log);
    } else {
      transactionDataAccess.addTransaction(transaction);

      // update stats by recalculating it considering adding the new transaction
      statisticsService.addTransactionToStats(transaction);
      log.debug("stats updated to {}", statisticsService.getStatistics());
      // schedule a timer that update stats by recalculating it considering removing the transaction
      Timer timer = new Timer();
      Date stopTimerDate =
          new Date(transaction.getTimestamp() + VALID_TRANSACTION_DURATION_IN_MILLISECONDS);
      log.debug("transaction {} will be removed from stats at {}", transaction, stopTimerDate);
      timer.schedule(new TimerTask() {

        @Override
        public void run() {
          statisticsService.removeTransactionFromStats(transaction);
        }
      }, stopTimerDate);
    }

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



  /**
   * @return the statisticsService
   */
  public StatisticsService getStatisticsService() {
    return statisticsService;
  }



  /**
   * @param statisticsService the statisticsService to set
   */
  public void setStatisticsService(StatisticsService statisticsService) {
    this.statisticsService = statisticsService;
  }



}
