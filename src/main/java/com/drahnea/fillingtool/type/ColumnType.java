package com.drahnea.fillingtool.type;

/**
 *
 * @since Dec 11, 2014
 * @author sdrahnea
 */
public enum ColumnType
{

  VARCHAR2("VARCHAR2"), NVARCHAR2("NVARCHAR2"), NUMBER("NUMBER"), LONG("LONG"),
  DATE("DATE"), BINARY_FLOAT("BINARY_FLOAT"), BINARY_DOUBLE("BINARY_DOUBLE"),
  TIMESTAMP("TIMESTAMP"), INTERVAL_YEAR("INTERVAL_YEAR"),
  INTERVAL_DAY("INTERVAL_DAY"), RAW("RAW"), LOG_RAW("LOG_RAW"), ROWID("ROWID"),
  UROWID("UROWID"), CHAR("CHAR"), NCHAR("NCHAR"), CLOB("CLOB"),
  NCLOB("NCLOB"), BLOB("BLOB"), BFILE("BFILE"), BOOLEAN("BOOLEAN"),
  serial("serial"), varchar("varchar"), bytea("bytea"), int2("int2"),
  int4("int4"), int8("int8"), bool("bool"), float8("float8"), text("text"),
  numeric("numeric"), oid("oid"), INT("INT"), VARCHAR("VARCHAR");

  private final String description;

  ColumnType(String description)
  {
    this.description = description;
  }

  /**
   *
   * @return
   */
  public String description()
  {
    return this.description;
  }

}
