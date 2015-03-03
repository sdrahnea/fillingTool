package com.drahnea.fillingtool.model;

import com.drahnea.fillingtool.type.ColumnType;

/**
 * @since Dec 11, 2014
 * @author sdrahnea
 */
public class Column
{

  private int id;
  private String name;
  private ColumnType type;
  private ColumnSize size;
  private String value;
  private Table table;
  private boolean isNullable;

  /**
   *
   * @param name
   * @param type
   * @param size
   * @param value
   * @param table
   */
  public Column(String name, ColumnType type, ColumnSize size, String value, Table table, boolean isNullable)
  {
    this.name = name;
    this.type = type;
    this.size = size;
    this.value = value;
    this.table = table;
    this.isNullable = isNullable;
  }

  public Column(Column column)
  {
    this.name = column.getName();
    this.type = column.getType();
    this.size = column.getSize();
    this.value = column.getValue();
    this.table = column.getTable();
    this.isNullable = column.isIsNullable();
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public ColumnType getType()
  {
    return type;
  }

  public void setType(ColumnType type)
  {
    this.type = type;
  }

  public ColumnSize getSize()
  {
    return size;
  }

  public void setSize(ColumnSize size)
  {
    this.size = size;
  }

  public String getValue()
  {
    return value;
  }

  public void setValue(String value)
  {
    this.value = value;
  }

  public Table getTable()
  {
    return table;
  }

  public void setTable(Table table)
  {
    this.table = table;
  }

  public boolean isIsNullable()
  {
    return isNullable;
  }

  public void setIsNullable(boolean isNullable)
  {
    this.isNullable = isNullable;
  }

  public int getId()
  {
    return id;
  }

  public void setId(int id)
  {
    this.id = id;
  }

}
