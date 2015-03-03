package com.drahnea.fillingtool.model;

import com.drahnea.fillingtool.util.DefaultValue;
import java.util.LinkedList;
import java.util.List;

/**
 * @since Dec 11, 2014
 * @author sdrahnea
 */
public class Table
{

  private String name;
  private List<Column> columns = new LinkedList<>();

  public Table(String name, List<Column> columns)
  {
    this.name = name;
    this.columns = columns;
  }

  public Table(Table table)
  {
    this.name = table.getName();
    for (Column column : table.getColumns())
    {
      this.columns.add(new Column(column));
    }
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public List<Column> getColumns()
  {
    return columns;
  }

  public void setColumns(List<Column> columns)
  {
    this.columns = columns;
  }

  public void fillColumnsByDefault()
  {
    for (int i = 0; i < this.columns.size(); i++)
    {
      Column column = this.columns.get(i);
      if (!column.isIsNullable())
      {
        column.setValue(DefaultValue.getDefaultValue(column.getType()));
      }
      this.columns.set(i, column);
    }
  }

  public void fillColumnsByDefault(Column targetColumn, String targetValue)
  {
    for (int i = 0; i < this.columns.size(); i++)
    {
      Column column = columns.get(i);
      if (targetColumn.getName().equalsIgnoreCase(column.getName()))
      {
        column.setValue(targetValue);
      }
      else if (!column.isIsNullable())
      {
        column.setValue(DefaultValue.getDefaultValue(column.getType()));
      }
      this.columns.set(i, column);
    }
  }

}
