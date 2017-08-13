package com.n26.challenge.exceptions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.n26.challenge.exceptions.ChallengeException.Name;


/**
 * Quick test of exception, ensure its equals and other methods behave properly.
 */
public class TooOldExceptionTest {
  /**
   * Check that the name returns an expected value.
   */
  @Test
  public final void testGetTechnicalName() {
    assertEquals(Name.TOO_OLD_EXCEPTION.toString(),
        new TransactionTooOldException("", null).getTechnicalName());
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
    final TransactionTooOldException noMessage = new TransactionTooOldException("", null);
    assertEquals("", noMessage.getMessage());
    final String someText = "Test Error Message, should be returned.)";
    final TransactionTooOldException withMessage = new TransactionTooOldException(someText, null);
    assertEquals(someText, withMessage.getMessage());
  }

  /**
   * Check that getCuase does what is expected.
   */
  @Test
  public final void testGetCause() {
    final Exception npe = new NullPointerException("Test Error Message, should be returned");
    final TransactionTooOldException withNulls = new TransactionTooOldException("", null, npe);
    assertEquals(npe, withNulls.getCause());
  }

  /**
   * Check that hashcode is reproducible.
   */
  @Test
  public final void testHashCode() {
    final TransactionTooOldException withNulls = new TransactionTooOldException("", null);
    assertEquals(withNulls.hashCode(), withNulls.hashCode());
  }

  /**
   * Check that equal instances are actually equal.
   */
  @Test
  public final void testEquals() {
    final TransactionTooOldException withNulls = new TransactionTooOldException("", null);
    assertEquals(withNulls, withNulls);
    assertNotEquals("Should have different corr Id", withNulls, new TransactionTooOldException("",
        null));

    final TransactionTooOldException testException =
        new TransactionTooOldException("Test Error Message", null);
    assertNotEquals(withNulls, testException);

    assertNotEquals(withNulls, new Object());
  }

}
