package com.drahnea.fillingtool.type;

/**
 *
 * @since Dec 11, 2014
 * @author sdrahnea
 */
public enum DatabaseType
{

  ORACLE("ORACLE"), H2("H2"), POSTGRESQL("POSTGRESQL"), MYSQL("MYSQL");

  private final String description;

  DatabaseType(String description)
  {
    this.description = description;
  }

  public String description()
  {
    return this.description;
  }

}
