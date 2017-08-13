package com.n26.challenge.service;

import java.time.Instant;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.n26.challenge.dataaccess.TransactionDataAccess;
import com.n26.challenge.exceptions.ImpossibleFutureTransactionException;
import com.n26.challenge.exceptions.TransactionTooOldException;
import com.n26.challenge.model.Transaction;

@RunWith(SpringJUnit4ClassRunner.class)
public class TransactionServiceTest {


  @MockBean
  protected TransactionDataAccess transactionDataAccess;
  @MockBean
  protected StatisticsService statisticsService;

  private TransactionServiceImpl transactionService;

  @Before
  public void setUp() {
    transactionService= new TransactionServiceImpl();
    transactionService.setStatisticsService(statisticsService);
    transactionService.setTransactionDataAccess(transactionDataAccess);

  }


  @Test(expected = TransactionTooOldException.class)
  public void testAddTooOldTransaction() throws Exception {

    Long now = Instant.now().toEpochMilli();
    Transaction transaction=  new Transaction(70, now - 80000);
    transactionService.addTransaction(transaction);
  }



  @Test(expected = ImpossibleFutureTransactionException.class)
  public void testAddImpossibleFutureTransaction() throws Exception {

    Long now = Instant.now().toEpochMilli();
    Transaction transaction=  new Transaction(70, now + 820000);
    transactionService.addTransaction(transaction);
  }
}
