package com.n26.challenge.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.time.Instant;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.n26.challenge.dataaccess.TransactionDataAccess;
import com.n26.challenge.model.Statistics;
import com.n26.challenge.model.Transaction;

@RunWith(SpringJUnit4ClassRunner.class)
public class StatisticsServiceTest {

  @MockBean
  TransactionDataAccess transactionDataAccess;


  private StatisticsServiceImpl statisticsService;


  @Before
  public void setUp() {
    statisticsService = new StatisticsServiceImpl();
    statisticsService.setTransactionDataAccess(transactionDataAccess);

  }

  @Test
  public void testAddTransactionToStatsWithEmptyStats() throws Exception {
    Statistics stats = new Statistics();
    Long anyTime = Instant.now().toEpochMilli();
    Transaction transaction = new Transaction(10, anyTime);
    statisticsService.setStatistics(stats);
    statisticsService.addTransactionToStats(transaction);
    assertEquals(10, stats.getMax(), 0.001);
    assertEquals(10, stats.getMin(), 0.001);
    assertEquals(10, stats.getSum(), 0.001);
    assertEquals(10, stats.getAvg(), 0.001);
    assertEquals(1, stats.getCount());
  }

  @Test
  public void testAddTransactionToStatsWithExistingStats() throws Exception {
    Statistics stats = new Statistics(25, 15, 10, 2);
    Long anyTime = Instant.now().toEpochMilli();
    Transaction transaction = new Transaction(11, anyTime);
    statisticsService.setStatistics(stats);
    statisticsService.addTransactionToStats(transaction);
    assertEquals(15, stats.getMax(), 0.001);
    assertEquals(10, stats.getMin(), 0.001);
    assertEquals(36, stats.getSum(), 0.001);
    assertEquals(12, stats.getAvg(), 0.001);
    assertEquals(3, stats.getCount());
  }


  @Test
  public void testAddTransactionToStatsWithNewMax() throws Exception {
    Statistics stats = new Statistics(25, 15, 10, 2);
    Long anyTime = Instant.now().toEpochMilli();
    Transaction transaction = new Transaction(50, anyTime);
    statisticsService.setStatistics(stats);
    statisticsService.addTransactionToStats(transaction);
    assertEquals(50, stats.getMax(), 0.001);
    assertEquals(10, stats.getMin(), 0.001);
    assertEquals(75, stats.getSum(), 0.001);
    assertEquals(25, stats.getAvg(), 0.001);
    assertEquals(3, stats.getCount());
  }

  @Test
  public void testAddTransactionToStatsWithNewMin() throws Exception {
    Statistics stats = new Statistics(25, 15, 10, 2);
    Long anyTime = Instant.now().toEpochMilli();
    Transaction transaction = new Transaction(5, anyTime);
    statisticsService.setStatistics(stats);
    statisticsService.addTransactionToStats(transaction);
    assertEquals(15, stats.getMax(), 0.001);
    assertEquals(5, stats.getMin(), 0.001);
    assertEquals(30, stats.getSum(), 0.001);
    assertEquals(10, stats.getAvg(), 0.001);
    assertEquals(3, stats.getCount());
  }



  @Test
  public void testRemoveTransactionToStatsWithExistingStats() throws Exception {
    Statistics stats = new Statistics(25, 15, 10, 2);
    Long anyTime = Instant.now().toEpochMilli();
    Transaction transaction = new Transaction(11, anyTime);
    statisticsService.setStatistics(stats);
    statisticsService.removeTransactionFromStats(transaction);
    assertEquals(15, stats.getMax(), 0.001);
    assertEquals(10, stats.getMin(), 0.001);
    assertEquals(14, stats.getSum(), 0.001);
    assertEquals(14, stats.getAvg(), 0.001);
    assertEquals(1, stats.getCount());
  }

  @Test
  public void testRemoveTransactionToStatsAsLastCountedObject() throws Exception {
    Statistics stats = new Statistics(10, 10, 10, 1);
    Long anyTime = Instant.now().toEpochMilli();
    Transaction transaction = new Transaction(10, anyTime);
    statisticsService.setStatistics(stats);
    statisticsService.removeTransactionFromStats(transaction);
    when(transactionDataAccess.getMinTransactionValue(any(Long.class), any(Long.class)))
        .thenReturn(Double.NaN);
    when(transactionDataAccess.getMaxTransactionValue(any(Long.class), any(Long.class)))
        .thenReturn(Double.NaN);
    assertEquals(Double.NaN, stats.getMax(), 0.001);
    assertEquals(Double.NaN, stats.getMin(), 0.001);
    assertEquals(0, stats.getSum(), 0.001);
    assertEquals(0, stats.getAvg(), 0.001);
    assertEquals(0, stats.getCount());
  }

  @Test
  public void testRemoveTransactionToStatsWithNewMax() throws Exception {
    when(transactionDataAccess.getMaxTransactionValue(any(Long.class), any(Long.class)))
        .thenReturn(new Double(15));
    Statistics stats = new Statistics(45, 20, 10, 3);
    Long anyTime = Instant.now().toEpochMilli();
    Transaction transaction = new Transaction(20, anyTime);
    statisticsService.setStatistics(stats);
    statisticsService.removeTransactionFromStats(transaction);

    assertEquals(15, stats.getMax(), 0.001);
    assertEquals(10, stats.getMin(), 0.001);
    assertEquals(25, stats.getSum(), 0.001);
    assertEquals(12.5, stats.getAvg(), 0.001);
    assertEquals(2, stats.getCount());
  }

  @Test
  public void testRemoveTransactionToStatsWithNewMin() throws Exception {
    when(transactionDataAccess.getMinTransactionValue(any(Long.class), any(Long.class)))
        .thenReturn(new Double(10));
    Statistics stats = new Statistics(45, 20, 5, 3);
    Long anyTime = Instant.now().toEpochMilli();
    Transaction transaction = new Transaction(5, anyTime);
    statisticsService.setStatistics(stats);
    statisticsService.removeTransactionFromStats(transaction);
    assertEquals(20, stats.getMax(), 0.001);
    assertEquals(10, stats.getMin(), 0.001);
    assertEquals(40, stats.getSum(), 0.001);
    assertEquals(20, stats.getAvg(), 0.001);
    assertEquals(2, stats.getCount());
  }
}
