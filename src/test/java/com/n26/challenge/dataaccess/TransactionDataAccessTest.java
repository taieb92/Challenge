package com.n26.challenge.dataaccess;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.n26.challenge.model.Transaction;
import com.n26.challenge.repository.TransactionRepository;

@RunWith(SpringJUnit4ClassRunner.class)
public class TransactionDataAccessTest {


  @MockBean
  private TransactionRepository transactionRepository;


  private TransactionDataAccessImpl transactionDataAccess;

  @Before
  public void setUp() {
    transactionDataAccess = new TransactionDataAccessImpl();
    transactionDataAccess.setTransactionRepository(transactionRepository);
  }

  @Test
  public void testGetMaxTransactionValue() throws Exception {
    ConcurrentLinkedDeque<Transaction> transactions = new ConcurrentLinkedDeque<Transaction>();
    Long anyTime = Instant.now().toEpochMilli();
    transactions.add(new Transaction(10, anyTime));
    transactions.add(new Transaction(20, anyTime));
    transactions.add(new Transaction(30, anyTime));
    when(transactionRepository.getDescendingIterator()).thenReturn(
        transactions.descendingIterator());
    double value = transactionDataAccess.getMaxTransactionValue(anyTime + 15000, 60000);
    assertEquals(30, value, 0.001);

  }

  @Test
  public void testGetMaxTransactionValueWithEmptyRepository() throws Exception {

    when(transactionRepository.isEmpty()).thenReturn(true);
    double value = transactionDataAccess.getMaxTransactionValue(0, 0);
    assertEquals(Double.NaN, value, 0.001);

  }

  @Test
  public void testGetMaxTransactionValueSeveralMaxs() throws Exception {
    ConcurrentLinkedDeque<Transaction> transactions = new ConcurrentLinkedDeque<Transaction>();
    Long anyTime = Instant.now().toEpochMilli();
    transactions.add(new Transaction(70, anyTime));
    transactions.add(new Transaction(50, anyTime));
    transactions.add(new Transaction(20, anyTime));
    transactions.add(new Transaction(30, anyTime));
    when(transactionRepository.getDescendingIterator()).thenReturn(
        transactions.descendingIterator());
    double value = transactionDataAccess.getMaxTransactionValue(anyTime + 15000, 60000);
    assertEquals(70, value, 0.001);

  }

  @Test
  public void testGetMaxTransactionValueOldMaxValues() throws Exception {
    ConcurrentLinkedDeque<Transaction> transactions = new ConcurrentLinkedDeque<Transaction>();
    Long anyTime = Instant.now().toEpochMilli();
    transactions.add(new Transaction(70, anyTime - 80000));
    transactions.add(new Transaction(50, anyTime));
    transactions.add(new Transaction(20, anyTime));
    transactions.add(new Transaction(30, anyTime));
    when(transactionRepository.getDescendingIterator()).thenReturn(
        transactions.descendingIterator());
    double value = transactionDataAccess.getMaxTransactionValue(anyTime + 15000, 60000);
    assertEquals(50, value, 0.001);

  }

  /**
   * This method tests if the GetMaxTransactionValue stops iterating if the transaction returned by
   * the iterator is older than the STATISTICS_CALCULATOR_DURATION + VALID_TRANSACTION_DURATION as
   * no more valid transaction will be found afterwards anyway. This is tested by hiding a valid new
   * transanction save before a very old transaction. The last valid transation should not be shown
   * as the iterator is supposed to stop
   *
   */
  @Test
  public void testGetMaxTransactionValueValidValueHiddenUnderOldMaxValues() throws Exception {
    ConcurrentLinkedDeque<Transaction> transactions = new ConcurrentLinkedDeque<Transaction>();
    Long anyTime = Instant.now().toEpochMilli();
    transactions.add(new Transaction(90, anyTime));
    transactions.add(new Transaction(70, anyTime - 150000));
    transactions.add(new Transaction(50, anyTime));
    transactions.add(new Transaction(20, anyTime));
    transactions.add(new Transaction(30, anyTime));
    when(transactionRepository.getDescendingIterator()).thenReturn(
        transactions.descendingIterator());
    double value = transactionDataAccess.getMaxTransactionValue(anyTime + 15000, 60000);
    assertEquals(50, value, 0.001);

  }



  @Test
  public void testGetMinTransactionValue() throws Exception {
    ConcurrentLinkedDeque<Transaction> transactions = new ConcurrentLinkedDeque<Transaction>();
    Long anyTime = Instant.now().toEpochMilli();
    transactions.add(new Transaction(50, anyTime));
    transactions.add(new Transaction(40, anyTime));
    transactions.add(new Transaction(30, anyTime));
    when(transactionRepository.getDescendingIterator()).thenReturn(
        transactions.descendingIterator());
    double value = transactionDataAccess.getMinTransactionValue(anyTime + 15000, 60000);
    assertEquals(30, value, 0.001);

  }

  @Test
  public void testGetMinTransactionValueWithEmptyRepository() throws Exception {

    when(transactionRepository.isEmpty()).thenReturn(true);
    double value = transactionDataAccess.getMinTransactionValue(0, 0);
    assertEquals(Double.NaN, value, 0.001);

  }

  @Test
  public void testGetMinTransactionValueSeveralMins() throws Exception {
    ConcurrentLinkedDeque<Transaction> transactions = new ConcurrentLinkedDeque<Transaction>();
    Long anyTime = Instant.now().toEpochMilli();
    transactions.add(new Transaction(10, anyTime));
    transactions.add(new Transaction(15, anyTime));
    transactions.add(new Transaction(40, anyTime));
    transactions.add(new Transaction(30, anyTime));
    when(transactionRepository.getDescendingIterator()).thenReturn(
        transactions.descendingIterator());
    double value = transactionDataAccess.getMinTransactionValue(anyTime + 15000, 60000);
    assertEquals(10, value, 0.001);

  }

  @Test
  public void testGetMinTransactionValueOldMinValues() throws Exception {
    ConcurrentLinkedDeque<Transaction> transactions = new ConcurrentLinkedDeque<Transaction>();
    Long anyTime = Instant.now().toEpochMilli();
    transactions.add(new Transaction(10, anyTime - 80000));
    transactions.add(new Transaction(50, anyTime));
    transactions.add(new Transaction(20, anyTime));
    transactions.add(new Transaction(30, anyTime));
    when(transactionRepository.getDescendingIterator()).thenReturn(
        transactions.descendingIterator());
    double value = transactionDataAccess.getMinTransactionValue(anyTime + 15000, 60000);
    assertEquals(20, value, 0.001);

  }

  /**
   * This method tests if the GetMaxTransactionValue stops iterating if the transaction returned by
   * the iterator is older than the STATISTICS_CALCULATOR_DURATION + VALID_TRANSACTION_DURATION as
   * no more valid transaction will be found afterwards anyway. This is tested by hiding a valid new
   * Transaction save before a very old transaction. The last valid transation should not be shown
   * as the iterator is supposed to stop
   *
   */
  @Test
  public void testGetMinTransactionValueValidValueHiddenUnderOldMinValues() throws Exception {
    ConcurrentLinkedDeque<Transaction> transactions = new ConcurrentLinkedDeque<Transaction>();
    Long anyTime = Instant.now().toEpochMilli();
    transactions.add(new Transaction(1, anyTime));
    transactions.add(new Transaction(70, anyTime - 150000));
    transactions.add(new Transaction(50, anyTime));
    transactions.add(new Transaction(20, anyTime));
    transactions.add(new Transaction(30, anyTime));
    when(transactionRepository.getDescendingIterator()).thenReturn(
        transactions.descendingIterator());
    double value = transactionDataAccess.getMinTransactionValue(anyTime + 15000, 60000);
    assertEquals(20, value, 0.001);

  }
}
