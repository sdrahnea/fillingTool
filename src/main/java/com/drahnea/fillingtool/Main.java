package com.drahnea.fillingtool;

import com.drahnea.fillingtool.controller.Context;
import com.drahnea.fillingtool.controller.DatabaseStructure;
import com.drahnea.fillingtool.model.Column;
import com.drahnea.fillingtool.model.Element;
import com.drahnea.fillingtool.model.Graph;
import com.drahnea.fillingtool.model.Relation;
import com.drahnea.fillingtool.model.Table;
import com.drahnea.fillingtool.type.DatabaseType;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @since Dec 11, 2014
 * @author sdrahnea
 */
public class Main
{

  public static void main(String... arg)
  {
    try
    {
      Context context = new Context("localhost", "3306", "edocument", "root", "root", DatabaseType.MYSQL);

      DatabaseMetaData md = context.getConnection().getMetaData();

      DatabaseStructure structure = new DatabaseStructure(context.getDatabaseType());
      structure.loadStructure(md, true);

      Connection connection = context.getConnection();
      Statement statment = connection.createStatement();

      structure.fillCoulmnId();

      Graph graph = new Graph(structure.getDatabaseRelations());

      graph.populateElements();
      List<Element> elements = graph.getGraphElements();

      for (int index = 0; index < elements.size(); index++)
      {
        Element element = elements.get(index);
        int dataCount = 0;
        if (element.getParents().isEmpty() && !isRepeated(element, elements))
        {
          try
          {
            for (Table table : element.getData())
            {
              statment.executeUpdate(structure.getInsertSql(table));
              dataCount++;
            }
          }
          catch (Exception e)
          {
            System.out.println("" + e);
          }
          if (dataCount == element.getData().size())
          {
            element.setInserted(true);
          }
          elements.set(index, element);
        }
      }
      while (!isAllInserted(elements))
      {
        for (int e = 0; e < elements.size(); e++)
        {
          Element targetElement = elements.get(e);
          if (isRepeated(targetElement, elements) && !targetElement.isInserted())
          {
            int countInserted = 0;
            //if (isAllElementsInserted(getGroupElements(targetElement, elements)))
            if (isAllElementsInserted(targetElement.getParents()))
            {
              try
              {

                for (Table table : targetElement.getData())
                {
                  for (int cindex = 0; cindex < table.getColumns().size(); cindex++)
                  {
                    Column column = table.getColumns().get(cindex);
                    Relation relation = structure.getOpositeColumnValueFromRelations(table.getName(), column.getName());
                    String columnValue = getValueForColumn(relation, elements, targetElement, table.getName(), column.getName());
                    if (columnValue != null)
                    {
                      column.setValue(columnValue);
                      table.getColumns().set(cindex, column);
                    }
                  }
                  statment.executeUpdate(structure.getInsertSql(table));
                  countInserted++;
                }
              }
              catch (Exception ex)
              {
                System.out.println("" + ex);
              }
            }
            if (countInserted == targetElement.getData().size())
            {
              targetElement.setInserted(true);
            }
            elements.set(e, targetElement);
          }
        }
      }

      context.getConnection().close();
    }
    catch (Exception e)
    {
      System.out.println("" + e);
    }
  }

  public static String getValueForColumn(Relation relation, List<Element> elements, Element te, String tableName, String columnName)
  {
    if (relation == null)
    {
      return null;
    }
    for (Element element : elements)
    {
      if (!te.getId().equalsIgnoreCase(element.getId()))
      {
        for (Relation rel : element.getRelations())
        {
          if (rel.getForeignColumn().getName().equalsIgnoreCase(relation.getForeignColumn().getName())
                  && rel.getForeignTable().getName().equalsIgnoreCase(relation.getForeignTable().getName())
                  && rel.getParentColumn().getName().equalsIgnoreCase(relation.getParentColumn().getName())
                  && rel.getParentTable().getName().equalsIgnoreCase(relation.getParentTable().getName()))
          {
            Table dataTable = element.getData().get(0);
            String tempColumn = element.getId().split("\\.")[1];
            for (Column column : dataTable.getColumns())
            {
              if (column.getName().equalsIgnoreCase(tempColumn))
              {
                return column.getValue();
              }
            }
          }
        }
      }
    }
    return null;
  }

  public static boolean isForeignElementsWasInserted(Element element, List<Element> elements)
  {
    for (Element se : elements)
    {
      if (se.getId().equalsIgnoreCase(element.getId()) && se.isInserted())
      {
        return true;
      }
    }
    return false;
  }

  private static boolean isRepeated(Element element, List<Element> elements)
  {
    boolean repeat = false;

    int count = 0;
    for (Element te : elements)
    {
      if (element.getId().split("\\.")[0].equalsIgnoreCase(te.getId().split("\\.")[0]))
      {
        count++;
      }
      if (count > 1)
      {
        repeat = true;
      }
    }
    return repeat;
  }

  private static List<Element> getGroupElements(Element element, List<Element> elements)
  {
    List<Element> result = new LinkedList<>();
    for (Element te : elements)
    {
      if ((!element.getId().equalsIgnoreCase(te.getId())) && (element.getId().split("\\.")[0].equalsIgnoreCase(te.getId().split("\\.")[0])))
      {
        result.add(te);
//        for (Relation rel : te.getRelations())
//        {
//          for (Element se : elements)
//          {
//            if (se.getId().equalsIgnoreCase(rel.getParentTable().getName() + "." + rel.getParentColumn().getName()))
//            {
//              result.add(se);
//            }
//          }
//        }
      }
    }
    return result;
  }

  private static boolean isAllElementsInserted(List<Element> es)
  {
    for (Element e : es)
    {
      if (!e.isInserted())
      {
        return false;
      }
    }
    return true;
  }

  public static boolean isAllInserted(List<Element> elements)
  {
    for (Element element : elements)
    {
      if (!element.isInserted())
      {
        return false;
      }
    }
    return true;
  }

}
