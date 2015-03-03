package com.drahnea.fillingtool.model;

/**
 *
 * @since Jan 15, 2015
 * @author sdrahnea
 */
public class ColumnInfo
{

  private String table;
  private String column;
  private int columnId;
  private int sum;
  private int value;

  public ColumnInfo(String table, String column, int columnId, int sum)
  {
    this.table = table;
    this.column = column;
    this.columnId = columnId;
    this.sum = sum;
    this.value = columnId + 2;
  }

  public String getTable()
  {
    return table;
  }

  public void setTable(String table)
  {
    this.table = table;
  }

  public String getColumn()
  {
    return column;
  }

  public void setColumn(String column)
  {
    this.column = column;
  }

  public int getColumnId()
  {
    return columnId;
  }

  public void setColumnId(int columnId)
  {
    this.columnId = columnId;
  }

  public int getSum()
  {
    return sum;
  }

  public void setSum(int sum)
  {
    this.sum = sum;
  }

  public int getValue()
  {
    return value;
  }

  public void setValue(int value)
  {
    this.value = value;
  }

}
