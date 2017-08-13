package com.n26.challenge.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class StatisticsTest {

  /**
   * Check that hash code is consistent.
   *
   */
  @Test
  public void testHashCode() {

    Transaction one = new Transaction(10, 1502633572849L);
    Transaction two = new Transaction(10, 1502633572849L);

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
    Transaction one = new Transaction(10, 1502633572849L);
    assertNotNull("toString should return witout error", one.toString());
  }

  /**
   * Check that equals works properly.
   *
   * @throws Exception Any exceptions are test failures
   */
  @Test
  public void testEquals() throws Exception {

    Transaction one = new Transaction(10, 1502633572849L);
    Transaction two = new Transaction(10, 1502633572849L);

    assertEquals("Instance should be equal to itself", one, one);
    assertEquals("Instances with same id are equal", one, two);
    assertNotEquals(one, null);
    assertNotEquals(one, new Object());
    Transaction unequal = new Transaction(140, 1502633572849L);
    assertNotEquals(one, unequal);
  }



}
