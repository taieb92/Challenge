package com.n26.challenge.model;

/**
 * An object containing statistics details.
 *
 */
public class Statistics {

  private double sum;
  private double max;
  private double min;
  private long count;

  /**
   * @return the sum
   */
  public double getSum() {
    return Math.round(sum * 100) / 100.0d;
  }

  /**
   * @param sum the sum to set
   */
  public void setSum(double sum) {
    this.sum = sum;
  }

  /**
   * @return the avg
   */
  public double getAvg() {

    return count != 0 ? Math.round(sum / count * 100) / 100.0d : 0d;
  }

  /**
   * @return the max
   */
  public double getMax() {
    return max;
  }

  /**
   * @param max the max to set
   */
  public void setMax(double max) {
    this.max = max;
  }

  /**
   * @return the min
   */
  public double getMin() {
    return min;
  }

  /**
   * @param min the min to set
   */
  public void setMin(double min) {
    this.min = min;
  }

  /**
   * @return the count
   */
  public long getCount() {
    return count;
  }

  /**
   * @param count the count to set
   */
  public void setCount(long count) {
    this.count = count;
  }

  /**
   * @param sum
   * @param avg
   * @param max
   * @param min
   * @param count
   */
  public Statistics(double sum, double max, double min, long count) {
    super();
    this.sum = sum;
    this.max = max;
    this.min = min;
    this.count = count;
  }


  /**
   *
   */
  public Statistics() {

    super();
    this.sum = new Double(0);
    this.max = Double.NaN;
    this.min = Double.NaN;
    this.count = new Long(0);
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
    result = prime * result + (int) (count ^ (count >>> 32));
    long temp;
    temp = Double.doubleToLongBits(max);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(min);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(sum);
    result = prime * result + (int) (temp ^ (temp >>> 32));
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
    if (!(obj instanceof Statistics)) {
      return false;
    }
    Statistics other = (Statistics) obj;
    if (count != other.count) {
      return false;
    }
    if (Double.doubleToLongBits(max) != Double.doubleToLongBits(other.max)) {
      return false;
    }
    if (Double.doubleToLongBits(min) != Double.doubleToLongBits(other.min)) {
      return false;
    }
    if (Double.doubleToLongBits(sum) != Double.doubleToLongBits(other.sum)) {
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
    return "Statistics [sum=" + sum + ", avg=" + getAvg() + ", max=" + max + ", min=" + min
        + ", count=" + count + "]";
  }


}
