package com.drahnea.fillingtool.controller;

import com.drahnea.fillingtool.type.DatabaseType;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @since Dec 11, 2014
 * @author sdrahnea
 */
public class Context
{

  private String host;
  private String port;
  private String schema;
  private String user;
  private String password;
  private String url;
  private DatabaseType databaseType;
  private Connection connection;

  public Context(String host, String port, String schema, String user, String password, DatabaseType databaseType)
  {
    this.host = host;
    this.port = port;
    this.schema = schema;
    this.user = user;
    this.password = password;
    this.databaseType = databaseType;
    this.createConnection();
  }

  private void createConnection()
  {
    try
    {
      if (this.databaseType == DatabaseType.POSTGRESQL)
      {
        Class.forName("org.postgresql.Driver");
        this.connection = DriverManager.getConnection("jdbc:postgresql://" + host + ":" + port + "/" + schema, user, password);
      }
      else if (this.databaseType == DatabaseType.MYSQL)
      {
        Class.forName("com.mysql.jdbc.Driver");
        this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + schema, user, password);
      }
    }
    catch (Exception e)
    {
      System.out.println("" + e);
    }
  }

  public String getHost()
  {
    return host;
  }

  public void setHost(String host)
  {
    this.host = host;
  }

  public String getPort()
  {
    return port;
  }

  public void setPort(String port)
  {
    this.port = port;
  }

  public String getSchema()
  {
    return schema;
  }

  public void setSchema(String schema)
  {
    this.schema = schema;
  }

  public String getUser()
  {
    return user;
  }

  public void setUser(String user)
  {
    this.user = user;
  }

  public String getPassword()
  {
    return password;
  }

  public void setPassword(String password)
  {
    this.password = password;
  }

  public String getUrl()
  {
    return url;
  }

  public void setUrl(String url)
  {
    this.url = url;
  }

  public Connection getConnection()
  {
    return connection;
  }

  public void setConnection(Connection connection)
  {
    this.connection = connection;
  }

  public DatabaseType getDatabaseType()
  {
    return databaseType;
  }

  public void setDatabaseType(DatabaseType databaseType)
  {
    this.databaseType = databaseType;
  }

}
