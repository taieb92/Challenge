package com.n26.challenge.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TransactionTest {

  /**
   * Check that hash code is consistent.
   *
   */
  @Test
  public void testHashCode() {
    Statistics one = new Statistics(25, 15, 10, 2);
    Statistics two = new Statistics(25, 15, 10, 2);

    assertEquals("Hashcode should be stable for one instance", one.hashCode(), one.hashCode());
    assertEquals("Hashcode should be stable for equal instances", one.hashCode(), two.hashCode());
  }



  /**
   * Check that toString competes successfully.
   *
   * @throws Exception Any exceptions are test failures
   */
  @Test
  public void testToString() throws Exception {
    Statistics one = new Statistics(25, 15, 10, 2);
    assertNotNull("toString should return witout error", one.toString());
  }

  /**
   * Check that equals works properly.
   *
   * @throws Exception Any exceptions are test failures
   */
  @Test
  public void testEquals() throws Exception {

    Statistics one = new Statistics(25, 15, 10, 2);
    Statistics two = new Statistics(25, 15, 10, 2);

    assertEquals("Instance should be equal to itself", one, one);
    assertEquals("Instances with same id are equal", one, two);
    assertNotEquals(one, null);
    assertNotEquals(one, new Object());
    Statistics unequal = new Statistics(26, 16, 10, 2);
    assertNotEquals(one, unequal);
  }



}
