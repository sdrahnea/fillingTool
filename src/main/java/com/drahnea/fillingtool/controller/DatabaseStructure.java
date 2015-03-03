package com.drahnea.fillingtool.controller;

import com.drahnea.fillingtool.model.Column;
import com.drahnea.fillingtool.model.ColumnInfo;
import com.drahnea.fillingtool.model.ColumnSize;
import com.drahnea.fillingtool.model.Relation;
import com.drahnea.fillingtool.model.Table;
import com.drahnea.fillingtool.type.ColumnType;
import com.drahnea.fillingtool.type.DatabaseType;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @since Dec 11, 2014
 * @author sdrahnea
 */
public class DatabaseStructure
{

  private final int MAX_GRAPH_SIZE = 1000;

  public List<Table> databaseTables = new LinkedList<>();
  public List<Relation> databaseRelations = new LinkedList<>();
  public int[][] graph = new int[MAX_GRAPH_SIZE][MAX_GRAPH_SIZE];
  private DatabaseType databaseType;

  public DatabaseStructure(DatabaseType databaseType)
  {
    this.databaseType = databaseType;
  }

  public List<Table> getAllTables(DatabaseMetaData metadata, boolean isFillDefaultData)
  {
    try
    {
      List<Table> result = new LinkedList<>();
      String catalog = null;
      String schemaPattern = null;
      String tableNamePattern = null;
      String[] types =
      {
        "TABLE"
      };

      ResultSet rs = metadata.getTables(catalog, schemaPattern, tableNamePattern, types);
      while (rs.next())
      {
        String tableName = rs.getString(3);

        Table table = new Table(tableName, null);
        table.setColumns(getAllColumn(metadata, table));
        table.fillColumnsByDefault();
        result.add(table);
      }
      return result;
    }
    catch (Exception e)
    {
      System.out.println("getAllTables: " + e);
      return null;
    }
  }

  public List<Column> getAllColumn(DatabaseMetaData metadata, Table table)
  {
    try
    {
      List<Column> result = new LinkedList<>();
      String catalog = null;
      String schemaPattern = null;
      String tableNamePattern = table.getName();
      String columnNamePattern = null;

      ResultSet rs = metadata.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
      while (rs.next())
      {
        String columnName = rs.getString(4);
        String columnType = rs.getString(6);
        String ColumnSizeLenght = rs.getString(7);
        String ColumnSizeScale = rs.getString(9) == null ? "0" : rs.getString(9);
        boolean isRequiered = rs.getBoolean("NULLABLE");
        result.add(new Column(columnName, getColumnType(columnType), new ColumnSize(ColumnSizeLenght, ColumnSizeScale), null, table, isRequiered));
      }
      return result;
    }
    catch (Exception e)
    {
      System.out.println("" + e);
      return null;
    }
  }

  public List<Relation> getReferences(DatabaseMetaData metadata)
  {
    try
    {
      List<Relation> result = new LinkedList<>();
      String parentCatalog = null;
      String parentSchema = null;
      String parentTable = null;
      String foreignCatalog = null;
      String foreignSchema = null;
      String foreignTable = null;
      if (databaseType == DatabaseType.POSTGRESQL)
      {
        ResultSet rs = metadata.getCrossReference(parentCatalog, parentSchema, parentTable, foreignCatalog, foreignSchema, foreignTable);
        while (rs.next())
        {
          String parentTableResult = rs.getString(3);
          String parentColumnResult = rs.getString(4);
          String foreignTableResult = rs.getString(7);
          String foreignColumnResult = rs.getString(8);

          Table pTable = getTable(parentTableResult);
          Column pColumn = getColumn(pTable, parentColumnResult);
          Table fTable = getTable(foreignTableResult);
          Column fColumn = getColumn(fTable, foreignColumnResult);

          Relation relation = new Relation(pTable, pColumn, fTable, fColumn);
          relation.setForeignWasExecuted(false);
          relation.setParentWasExecuted(false);
          result.add(relation);
        }
      }
      if (databaseType == DatabaseType.MYSQL)
      {
        for (Table tabi : databaseTables)
        {
          for (Table tabj : databaseTables)
          {
            parentTable = tabi.getName();
            foreignTable = tabj.getName();
            boolean check = true;
            for (Relation rel : result)
            {
              if (rel.getForeignTable().getName().equalsIgnoreCase(foreignTable) && rel.getParentTable().getName().equalsIgnoreCase(parentTable))
              {
                check = false;
              }
              if (rel.getForeignTable().getName().equalsIgnoreCase(parentTable) && rel.getParentTable().getName().equalsIgnoreCase(foreignTable))
              {
                check = false;
              }
            }
            if (!parentTable.equalsIgnoreCase(foreignTable) && check)
            {
              ResultSet rs = metadata.getCrossReference(parentCatalog, parentSchema, parentTable, foreignCatalog, foreignSchema, foreignTable);
              while (rs.next())
              {
                String parentTableResult = rs.getString(3);
                String parentColumnResult = rs.getString(4);
                String foreignTableResult = rs.getString(7);
                String foreignColumnResult = rs.getString(8);

                Table pTable = getTable(parentTableResult);
                Column pColumn = getColumn(pTable, parentColumnResult);
                Table fTable = getTable(foreignTableResult);
                Column fColumn = getColumn(fTable, foreignColumnResult);

                Relation relation = new Relation(pTable, pColumn, fTable, fColumn);
                relation.setForeignWasExecuted(false);
                relation.setParentWasExecuted(false);
                result.add(relation);
              }
            }
          }
        }
      }
      return result;
    }
    catch (Exception e)
    {
      System.out.println("getReferences(): " + e);
      return null;
    }
  }

  public void updateExecuteState(String tableName)
  {
    for (int i = 0; i < databaseRelations.size(); i++)
    {
      Relation relation = databaseRelations.get(i);
      if (relation.getForeignTable().getName().equalsIgnoreCase(tableName))
      {
        relation.setForeignWasExecuted(true);
      }
      if (relation.getParentTable().getName().equalsIgnoreCase(tableName))
      {
        relation.setParentWasExecuted(true);
      }
      this.databaseRelations.set(i, relation);
    }
  }

  private ColumnType getColumnType(String columnType)
  {
    for (ColumnType ct : ColumnType.values())
    {
      if (ct.name().equalsIgnoreCase(columnType))
      {
        return ct;
      }
    }
    System.out.println("!!! " + columnType);
    return null;
  }

  private Table getTable(String name)
  {
    for (Table table : databaseTables)
    {
      if (table.getName().equalsIgnoreCase(name))
      {
        return table;
      }
    }
    return null;
  }

  private Column getColumn(Table table, String name)
  {
    for (Column column : table.getColumns())
    {
      if (column.getName().equalsIgnoreCase(name))
      {
        return column;
      }
    }
    return null;
  }

  public void loadStructure(DatabaseMetaData metadata, boolean isFillDefaultData)
  {
    this.databaseTables = getAllTables(metadata, isFillDefaultData);
    this.databaseRelations = getReferences(metadata);
  }

  public void fillCoulmnId() throws Exception
  {
    List<String> addedColumns = new LinkedList<>();
    int putId = 0;
    for (int i = 0; i < this.databaseRelations.size(); i++)
    {
      Relation relation = this.databaseRelations.get(i);
      String target = relation.getForeignTable().getName() + "." + relation.getForeignColumn().getName();
      if (!isTargetExistInSet(target, addedColumns))
      {
        relation.getForeignColumn().setId(putId);
        putId++;
        addedColumns.add(target);
      }
      target = relation.getParentTable().getName() + "." + relation.getParentColumn().getName();
      if (!isTargetExistInSet(target, addedColumns))
      {
        relation.getParentColumn().setId(putId);
        putId++;
        addedColumns.add(target);
      }
      this.databaseRelations.set(i, relation);
    }
  }

  private boolean isTargetExistInSet(String target, List<String> set)
  {
    for (String value : set)
    {
      if (target.equalsIgnoreCase(value))
      {
        return true;
      }
    }
    return false;
  }

  public void fillStructure()
  {
    for (Relation relation : getDatabaseRelations())
    {
      try
      {

      }
      catch (Exception e)
      {
        System.out.println("" + e);
      }
    }
  }

  public String getInsertSql(Table table)
  {
    String sql = "INSERT INTO " + table.getName() + "(";
    String valueString = " VALUES(";
    for (Column c : table.getColumns())
    {
      sql += (c.getName() + ", ");
      valueString += (c.getValue() + ", ");
    }
    sql = sql.substring(0, sql.length() - 2) + ")";
    valueString = valueString.substring(0, valueString.length() - 2) + ")";
    return (sql + valueString);
  }

  public List<Table> getDatabaseTables()
  {
    return databaseTables;
  }

  public void setDatabaseTables(List<Table> databaseTables)
  {
    this.databaseTables = databaseTables;
  }

  public List<Relation> getDatabaseRelations()
  {
    return databaseRelations;
  }

  public void setDatabaseRelations(List<Relation> databaseRelations)
  {
    this.databaseRelations = databaseRelations;
  }

  private void initGraph()
  {
    for (int i = 0; i < MAX_GRAPH_SIZE; i++)
    {
      for (int j = 0; j < MAX_GRAPH_SIZE; j++)
      {
        this.graph[i][j] = -1;
      }
    }
  }

  private void initGraphDiagonal(int graphSize)
  {
    for (int i = 0; i < graphSize; i++)
    {
      this.graph[i][i] = 1;
    }
  }

  private void resetGraph(int graphSize)
  {
    for (int i = 0; i < graphSize; i++)
    {
      for (int j = 0; j < graphSize; j++)
      {
        this.graph[i][j] = 0;
      }
    }
  }

  private int getMaxValue(int target, int value)
  {
    return target < value ? value : target;
  }

  private int getMaxId()
  {
    int max = 0;
    for (Relation relation : this.getDatabaseRelations())
    {
      max = getMaxValue(max, relation.getForeignColumn().getId());
      max = getMaxValue(max, relation.getParentColumn().getId());
    }
    return max;
  }

  public int sumLine(int line, int size)
  {
    int sum = 0;
    for (int i = 0; i < size; i++)
    {
      sum += this.graph[line][i];
    }
    return sum;
  }

  public void buildGrapgh()
  {
    int graphSize = getMaxId() + 1;
    this.initGraph();
    this.resetGraph(graphSize);
    this.initGraphDiagonal(graphSize);
    for (Relation relation : this.getDatabaseRelations())
    {
      this.graph[relation.getParentColumn().getId()][relation.getForeignColumn().getId()] = 1;
    }

    ColumnInfo[] columnInfos = new ColumnInfo[graphSize];
    //array init
    for (int line = 0; line < graphSize; line++)
    {
      columnInfos[line] = new ColumnInfo(getColumn(line).getTable().getName(), getColumn(line).getName(), line, sumLine(line, graphSize));
      //System.out.println("line: " + line + " sum: " + sumLine(line, graphSize) + " column: " + getColumn(line).getTable().getName() + "." + getColumn(line).getName());
    }
    //array sort descendend
    for (int i = 0; i < graphSize - 1; i++)
    {
      for (int j = i + 1; j < graphSize; j++)
      {
        if (columnInfos[i].getSum() < columnInfos[j].getSum())
        {
          ColumnInfo k = columnInfos[i];
          columnInfos[i] = columnInfos[j];
          columnInfos[j] = k;
        }
      }
    }

    for (ColumnInfo ci : columnInfos)
    {
      System.out.println("" + ci.getTable() + " -> " + ci.getColumn() + " -> " + ci.getSum() + " -> " + ci.getValue());
      if (ci.getSum() > 1)
      {
        fillGraph(ci.getColumnId(), ci.getValue(), columnInfos, graphSize);
      }
    }
  }

  void fillGraph(int line, int value, ColumnInfo[] cis, int graphSize)
  {
    if (getColumnInfo(line, cis).getSum() == 1)
    {
      this.graph[line][line] = value;
    }
    else
    {
      for (int i = 0; i < graphSize; i++)
      {
        if (this.graph[line][i] != 0)
        {
          this.graph[line][i] = value;
          if (line != i)
          {
            fillGraph(i, value, cis, graphSize);
          }
        }
      }
    }
  }

  public ColumnInfo getColumnInfo(int columnId, ColumnInfo[] cis)
  {
    for (ColumnInfo ci : cis)
    {
      if (ci.getColumnId() == columnId)
      {
        return ci;
      }
    }
    return null;
  }

  private Column getColumn(int id)
  {
    for (Relation relation : this.getDatabaseRelations())
    {
      if (relation.getForeignColumn().getId() == id)
      {
        return relation.getForeignColumn();
      }
      if (relation.getParentColumn().getId() == id)
      {
        return relation.getParentColumn();
      }
    }
    return null;
  }

  public Relation getOpositeColumnValueFromRelations(String tableName, String columnName)
  {
    for (Relation rel : this.getDatabaseRelations())
    {
      //if ((rel.getParentTable().getName().equalsIgnoreCase(tableName)) && (rel.getParentColumn().getName().equalsIgnoreCase(columnName)))
      if ((rel.getParentColumn().getName().equalsIgnoreCase(columnName)))
      {
        //return getColumn(rel.getForeignTable(), rel.getForeignColumn().getName()).getValue();
        return rel;
      }
      //else if ((rel.getForeignTable().getName().equalsIgnoreCase(tableName)) && (rel.getForeignColumn().getName().equalsIgnoreCase(columnName)))
      else if ((rel.getForeignColumn().getName().equalsIgnoreCase(columnName)))
      {
        //return getColumn(rel.getParentTable(), rel.getParentColumn().getName()).getValue();
        return rel;
      }
    }
    return null;
  }

}
