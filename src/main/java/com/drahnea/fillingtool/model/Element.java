package com.drahnea.fillingtool.model;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @since Jan 26, 2015
 * @author sdrahnea
 */
public class Element
{

  private String id;
  private List<Element> parents = new LinkedList<>();
  private List<Element> childs = new LinkedList<>();
  private Table table;
  private List<Table> data = new LinkedList<>();
  private boolean populated;
  private boolean inserted;
  private List<Relation> relations = new LinkedList<>();
  private List<String> childsId = new LinkedList<>();

  /**
   *
   * @param parent
   * @param child
   * @param table
   * @param populated
   * @param inserted
   */
  public Element(String id, Table table, boolean populated, boolean inserted)
  {
    this.id = id;
    this.table = table;
    this.populated = populated;
    this.inserted = inserted;
  }

  public List<Element> getParents()
  {
    return parents;
  }

  public void setParents(List<Element> parents)
  {
    this.parents = parents;
  }

  public List<Element> getChilds()
  {
    return childs;
  }

  public void setChilds(List<Element> childs)
  {
    this.childs = childs;
  }

  public Table getTable()
  {
    return table;
  }

  public void setTable(Table table)
  {
    this.table = table;
  }

  public boolean isPopulated()
  {
    return populated;
  }

  public void setPopulated(boolean populated)
  {
    this.populated = populated;
  }

  public boolean isInserted()
  {
    return inserted;
  }

  public void setInserted(boolean inserted)
  {
    this.inserted = inserted;
  }

  public void addParent(Element parent)
  {
    if (parent != null)
    {
      if (!isElementExist(parent, parents))
      {
        this.parents.add(parent);
      }
    }
  }

  public void addChild(Element child)
  {
    if (child != null)
    {
      if (!isElementExist(child, this.childs))
      {
        this.childs.add(child);
      }
    }
  }

  private boolean isElementExist(Element element, List<Element> elements)
  {
    for (Element e : elements)
    {
      if (e.getId().equalsIgnoreCase(element.getId()))
      {
        return true;
      }
    }
    return false;
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public void addData(Table table)
  {
    this.data.add(table);
  }

  public List<Table> getData()
  {
    if (this.childs.isEmpty() && this.data.isEmpty())
    {
      this.data.add(this.table);
    }
    return this.data;
  }

  public void setData(List<Table> data)
  {
    this.data = data;
  }

  public List<Relation> getRelations()
  {
    return relations;
  }

  public void setRelations(List<Relation> relations)
  {
    this.relations = relations;
  }

  public void addRelation(Relation relation)
  {
    this.relations.add(relation);
  }

  public void fillElementColumn(Table table, Column column, String value)
  {
    for (int t = 0; t < this.getData().size(); t++)
    {
      Table dataTable = this.getData().get(t);
      if (dataTable.getName().equalsIgnoreCase(table.getName()))
      {
        for (int c = 0; c < dataTable.getColumns().size(); c++)
        {
          Column dataColumn = dataTable.getColumns().get(c);
          if (dataColumn.getName().equalsIgnoreCase(column.getName()))
          {
            dataColumn.setValue(value);
            dataTable.getColumns().set(c, dataColumn);
          }
        }
        this.getData().set(t, dataTable);
      }
    }
  }

  public String getColumnValue(Table table, Column column)
  {
    for (Table t : this.getData())
    {
      for (Column c : t.getColumns())
      {
        if (t.getName().equalsIgnoreCase(table.getName())
                && c.getName().equalsIgnoreCase(column.getName()))
        {
          return c.getValue();
        }
      }
    }
    return null;
  }

  public void addChildsId(String childId)
  {
    if (!existChildId(childId))
    {
      this.childsId.add(childId);
    }
  }

  public boolean existChildId(String id)
  {
    for (String childId : this.childsId)
    {
      if (id.equalsIgnoreCase(childId))
      {
        return true;
      }
    }
    return false;
  }

  public List<String> getChildsId()
  {
    return childsId;
  }

  public void setChildsId(List<String> childsId)
  {
    this.childsId = childsId;
  }

}
