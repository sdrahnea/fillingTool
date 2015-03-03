package com.drahnea.fillingtool.model;

import com.drahnea.fillingtool.util.DefaultValue;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @since Jan 26, 2015
 * @author sdrahnea
 */
public class Graph
{

  private List<Element> elements = new LinkedList<>();
  private List<Relation> relations = new LinkedList<>();

  /**
   *
   * @param relation
   */
  public Graph(List<Relation> relations)
  {
    this.relations = relations;
    for (Relation relation : relations)
    {
      String parentId = relation.getParentTable().getName() + "." + relation.getParentColumn().getName();
      String childId = relation.getForeignTable().getName() + "." + relation.getForeignColumn().getName();
      Element parentElement = new Element(parentId, relation.getParentTable(), false, false);
      Element childElement = new Element(childId, relation.getForeignTable(), false, false);
      parentElement.addChild(childElement);
      childElement.addParent(parentElement);
      parentElement.addRelation(relation);
      childElement.addRelation(relation);
      if (!isElementExist(parentElement))
      {
        this.elements.add(parentElement);
      }
      else
      {
        updateElment(parentElement, childElement, false);
      }
      if (!isElementExist(childElement))
      {
        this.elements.add(childElement);
      }
      else
      {
        updateElment(childElement, parentElement, true);
      }
    }
  }

  private boolean isElementExist(Element targetElement)
  {
    for (Element element : this.elements)
    {
      if (element.getId().equalsIgnoreCase(targetElement.getId()))
      {
        return true;
      }
    }
    return false;
  }

  private Element getElement(Element targetElement)
  {
    for (Element element : this.elements)
    {
      if (element.getId().equalsIgnoreCase(targetElement.getId()))
      {
        return element;
      }
    }
    return null;
  }

  private boolean updateElment(Element targetElement, Element element, boolean isParent)
  {
    for (int i = 0; i < this.elements.size(); i++)
    {
      Element temp = this.elements.get(i);
      if (temp.getId().equalsIgnoreCase(targetElement.getId()))
      {
        if (isParent)
        {
          temp.addParent(element);
        }
        else
        {
          temp.addChild(element);
        }
        temp.addRelation(element.getRelations().get(0));
        this.elements.set(i, temp);
        return true;
      }
    }
    return false;
  }

  public void showGraph()
  {
    for (Element element : this.elements)
    {
      System.out.println("element: " + element.getId());
      System.out.print(">>>>>> parents: ");
      for (Element parent : element.getParents())
      {
        System.out.print(parent.getId() + ", ");
      }
      System.out.println("");
      System.out.print(">>>>>>  childs: ");
      for (Element child : element.getChilds())
      {
        System.out.print(child.getId() + ", ");
      }
      System.out.println("");
    }
  }

  public void populateElements()
  {
    /**
     * populate element without children
     */
    for (int i = 0; i < this.elements.size(); i++)
    {
      Element element = this.elements.get(i);
      if (element.getChilds().isEmpty())
      {
        Table table = element.getTable();
        table.fillColumnsByDefault();
        element.setTable(table);
        element.setPopulated(true);
        this.elements.set(i, element);
      }
    }
    /**
     * populate remain elements
     */
    while (!isAllPopulate())
    {
      showGraph();
      for (int i = 0; i < this.elements.size(); i++)
      {
        Element element = this.elements.get(i);
        if (!element.getChilds().isEmpty() && !element.isPopulated())
        {
          element.setChilds(getUpdatedChild(element.getChilds()));
          for (int index = 0; index < element.getChilds().size(); index++)
          {
            Element child = element.getChilds().get(index);
            if (child.isPopulated() && !element.existChildId(child.getId()))
            {
              //value from child column goes to parent correspondend column
              Column parentColumn = null;
              String value = null;
              for (Relation er : element.getRelations())
              {
                String cId = er.getForeignTable().getName() + "." + er.getForeignColumn().getName();
                //we need column for parent element
                if (cId.equalsIgnoreCase(child.getId()))
                {
                  parentColumn = er.getParentColumn();
                  //we need value from child element
                  value = child.getColumnValue(er.getForeignTable(), er.getForeignColumn());
                  if (value == null)
                  {
                    value = DefaultValue.getDefaultValue(er.getForeignColumn().getType());
                  }
                }
              }
              Table elementTable = new Table(element.getTable());
              elementTable.fillColumnsByDefault(parentColumn, value);
              element.addData(elementTable);
              element.addChildsId(child.getId());
            }
          }
        }
        if (element.getChilds().size() == element.getChildsId().size())
        {
          element.setPopulated(true);
        }
        this.elements.set(i, element);
      }
    }
  }

  public void insertAllElements()
  {

  }

  private boolean isAllPopulate()
  {
    for (Element element : this.elements)
    {
      if (!element.isPopulated())
      {
        return false;
      }
    }
    return true;
  }

  public List<Element> getUpdatedChild(List<Element> childs)
  {
    List<Element> result = new LinkedList<>();
    for (Element c : childs)
    {
      for (Element e : this.elements)
      {
        if (e.getId().equalsIgnoreCase(c.getId()))
        {
          result.add(e);
        }
      }
    }
    return result;
  }

  public List<Element> getGraphElements()
  {
    return this.elements;
  }

}
