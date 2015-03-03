package com.drahnea.fillingtool.util;

import com.drahnea.fillingtool.type.ColumnType;
import java.util.Random;
import java.util.UUID;

/**
 *
 * @since Dec 12, 2014
 * @author sdrahnea
 */
public class DefaultValue
{

  public static String getDefaultValue(ColumnType ct)
  {
    if ((ColumnType.int2 == ct) || (ColumnType.int4 == ct) || (ColumnType.int8 == ct)
            || (ColumnType.float8 == ct) || (ColumnType.numeric == ct) || (ColumnType.INT == ct))
    {
      Random rand = new Random();
      int randomNum = rand.nextInt(Integer.MAX_VALUE) + 1;
      return randomNum + "";
    }
    else if ((ColumnType.text == ct) || (ColumnType.varchar == ct))
    {
      return "'" + UUID.randomUUID().toString().substring(0, 30) + "'";
    }
    else if ((ColumnType.TIMESTAMP == ct))
    {
      return "now()";
    }
    else if ((ColumnType.bool == ct))
    {
      return "TRUE";
    }
    else if ((ColumnType.serial == ct))
    {
      return "11";
    }
    else if ((ColumnType.bytea == ct))
    {
      return "E'\\\\xDEADBEEF'";
    }

    return null;
  }

}
