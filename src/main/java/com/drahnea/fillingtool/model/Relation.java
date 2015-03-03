package com.drahnea.fillingtool.model;

import com.drahnea.fillingtool.type.ColumnType;

/**
 *
 * @since Dec 11, 2014
 * @author sdrahnea
 */
public class Relation
{

  private Column parentColumn;
  private Column foreignColumn;
  private Table parentTable;
  private Table foreignTable;
  private int rank;
  private boolean parentWasExecuted;
  private boolean foreignWasExecuted;

  public Relation(Column parentColumn, Column foreignColumn)
  {
    this.parentColumn = parentColumn;
    this.foreignColumn = foreignColumn;
  }

  public Relation(Table parentTable, Table foreignTable)
  {
    this.parentTable = parentTable;
    this.foreignTable = foreignTable;
  }

  public Relation(Table parentTable, Column parentColumn, Table foreignTable, Column foreignColumn)
  {
    this.parentTable = parentTable;
    this.foreignTable = foreignTable;
    this.parentColumn = parentColumn;
    this.foreignColumn = foreignColumn;
  }

  public Relation(String parentTable, String parentColumn, String foreignTable, String foreignColumn)
  {
    this.parentTable = new Table(parentTable, null);
    this.foreignTable = new Table(foreignTable, null);
    this.parentColumn = new Column(parentColumn, ColumnType.LONG, null, null, null, false);
    this.foreignColumn = new Column(foreignColumn, ColumnType.LONG, null, null, null, false);
  }

  public boolean isParentWasExecuted()
  {
    return parentWasExecuted;
  }

  public void setParentWasExecuted(boolean parentWasExecuted)
  {
    this.parentWasExecuted = parentWasExecuted;
  }

  public boolean isForeignWasExecuted()
  {
    return foreignWasExecuted;
  }

  public void setForeignWasExecuted(boolean foreignWasExecuted)
  {
    this.foreignWasExecuted = foreignWasExecuted;
  }

  public Column getParentColumn()
  {
    return parentColumn;
  }

  public void setParentColumn(Column parentColumn)
  {
    this.parentColumn = parentColumn;
  }

  public Column getForeignColumn()
  {
    return foreignColumn;
  }

  public void setForeignColumn(Column foreignColumn)
  {
    this.foreignColumn = foreignColumn;
  }

  public Table getParentTable()
  {
    return parentTable;
  }

  public void setParentTable(Table parentTable)
  {
    this.parentTable = parentTable;
  }

  public Table getForeignTable()
  {
    return foreignTable;
  }

  public void setForeignTable(Table foreignTable)
  {
    this.foreignTable = foreignTable;
  }

  public int getRank()
  {
    return rank;
  }

  public void setRank(int rank)
  {
    this.rank = rank;
  }

}
