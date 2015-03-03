package com.drahnea.fillingtool.model;

/**
 *
 * @since Dec 11, 2014
 * @author sdrahnea
 */
public class ColumnSize
{

  private int full;
  private int precision;

  /**
   *
   * @param full
   * @param precision
   * <br>
   * Is used for column type which have two-dimensional size like NUMBER, FLOAT,
   * etc.
   */
  public ColumnSize(int full, int precision)
  {
    this.full = full;
    this.precision = precision;
  }

  /**
   *
   * @param full
   * @param precision
   */
  public ColumnSize(String full, String precision)
  {
    this.full = Integer.parseInt(full);
    this.precision = Integer.parseInt(precision);
  }

  /**
   *
   * @param full
   */
  public ColumnSize(String full)
  {
    this.full = Integer.parseInt(full);
  }

  /**
   *
   * @param full
   * <br>
   * Is used for column type which have one-dimensional size like VARCHAR,
   * NUMBER, FLOAT etc.
   */
  public ColumnSize(int full)
  {
    this.full = full;
  }

  public int getFull()
  {
    return full;
  }

  public void setFull(int full)
  {
    this.full = full;
  }

  public int getPrecision()
  {
    return precision;
  }

  public void setPrecision(int precision)
  {
    this.precision = precision;
  }

}
