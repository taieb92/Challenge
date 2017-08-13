package com.n26.challenge.model;


/**
 * An object containing Transactions details.
 *
 */
public class Transaction {


  private double amount;
  private long timestamp;

  /**
   * @param amount
   * @param timestamp
   */
  public Transaction(double amount, long timestamp) {
    super();
    this.amount = amount;
    this.timestamp = timestamp;
  }



  /**
   *
   */
  public Transaction() {
    super();
  }



  /**
   * @return the amount
   */
  public double getAmount() {
    return amount;
  }

  /**
   * @param amount the amount to set
   */
  public void setAmount(double amount) {
    this.amount = amount;
  }

  /**
   * @return the timestamp
   */
  public long getTimestamp() {
    return timestamp;
  }

  /**
   * @param timestamp the timestamp to set
   */
  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(amount);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
    return result;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Transaction)) {
      return false;
    }
    Transaction other = (Transaction) obj;
    if (Double.doubleToLongBits(amount) != Double.doubleToLongBits(other.amount)) {
      return false;
    }
    if (timestamp != other.timestamp) {
      return false;
    }
    return true;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Transaction [amount=" + amount + ", timestamp=" + timestamp + "]";
  }



}
