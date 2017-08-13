package com.n26.challenge.exceptions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.n26.challenge.exceptions.ChallengeException.Name;


/**
 * Quick test of exception, ensure its equals and other methods behave properly.
 */
public class ImpossibleFutureTransactionExceptionTest {
  /**
   * Check that the name returns an expected value.
   */
  @Test
  public final void testGetTechnicalName() {
    assertEquals(Name.IMPOSSIBLE_FUTURE_TRANSACTION_EXCEPTION.toString(),
        new ImpossibleFutureTransactionException("", null).getTechnicalName());
  }

  /**
   * Check that toString doesnt have any weird problems.
   */
  @Test
  public final void testToString() {
    final TransactionTooOldException noMessage = new TransactionTooOldException("", null);
    assertNotNull(noMessage.toString());
  }


  /**
   * Check that the message is set properly.
   */
  @Test
  public final void testGetMessage() {
    final ImpossibleFutureTransactionException noMessage = new ImpossibleFutureTransactionException("", null);
    assertEquals("", noMessage.getMessage());
    final String someText = "Test Error Message, should be returned.)";
    final ImpossibleFutureTransactionException withMessage = new ImpossibleFutureTransactionException(someText, null);
    assertEquals(someText, withMessage.getMessage());
  }

  /**
   * Check that getCuase does what is expected.
   */
  @Test
  public final void testGetCause() {
    final Exception npe = new NullPointerException("Test Error Message, should be returned");
    final ImpossibleFutureTransactionException withNulls = new ImpossibleFutureTransactionException("", null, npe);
    assertEquals(npe, withNulls.getCause());
  }

  /**
   * Check that hashcode is reproducible.
   */
  @Test
  public final void testHashCode() {
    final ImpossibleFutureTransactionException withNulls = new ImpossibleFutureTransactionException("", null);
    assertEquals(withNulls.hashCode(), withNulls.hashCode());
  }

  /**
   * Check that equal instances are actually equal.
   */
  @Test
  public final void testEquals() {
    final ImpossibleFutureTransactionException withNulls = new ImpossibleFutureTransactionException("", null);
    assertEquals(withNulls, withNulls);
    assertNotEquals("Should have different corr Id", withNulls, new ImpossibleFutureTransactionException("",
        null));

    final ImpossibleFutureTransactionException testException =
        new ImpossibleFutureTransactionException("Test Error Message", null);
    assertNotEquals(withNulls, testException);

    assertNotEquals(withNulls, new Object());
  }

}
