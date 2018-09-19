/*
History:
+------+------------+-----------+-----------+----------------------------------------------------+
| Ver# | Date       | By        | Defect #  | Description                                        |
+------+------------+-----------+-----------+----------------------------------------------------+
| 1    | 05/23/2005 | Vikram    | N/A       | Provides Client specific version                   |
--------------------------------------------------------------------------------------------------
*/


package com.chelseasystems.cs.util;

/**
 * <p>Title: Version</p>
 *
 * <p>Description: Client specific version</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author Vikram Mundhra
 * @version 1.0
 */
public class Version
{
    public static final String DELIM = "@DELIM@";
    public static final String MAJOR = "@MAJOR@";
    public static final String MINOR = "@MINOR@";
    public static final String UPDATE = "@UPDATE@";
    public static final String BUILD = "@BUILD@";
    public static final String VERSION = "@MAJOR@@DELIM@@MINOR@@DELIM@@UPDATE@@DELIM@@BUILD@";
    public static final String VERSION_PROPERTY_KEY = "com.armani.pos.version";
    public static String CURRENT_REGION = "@CURRENT_REGION@";
    
    /**
     *
     */
    public static void applyVersion()
    {
        System.setProperty(VERSION_PROPERTY_KEY, VERSION);
    }

    /**
     *
     * @param args String[]
     */
    public static void main(String args[])
    {
            System.out.println("Armani POS Version: 3.0.0.0");
    }


}
