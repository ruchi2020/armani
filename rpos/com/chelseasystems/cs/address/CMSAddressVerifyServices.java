package com.chelseasystems.cs.address;

/*
History:
+------+------------+-----------+-----------+----------------------------------------------------+
| Ver# | Date       | By        | Defect #  | Description                                        |
+------+------------+-----------+-----------+----------------------------------------------------+ 
| 1    | 04-04-2006 |David Fung | PCR 67    | QAS                                                |
+------+------------+-----------+-----------+----------------------------------------------------+
*/

/**
* <p>Title:CMSAddressVerifyServices.java </p>
*
* <p>Description: CMSAddressVerifyServices </p>
*
* <p>Copyright: Copyright (c) 2005</p>
*
* <p>Company: Skillnet inc.</p>
*
* @author David Fung
* @version 1.0
*/

public abstract class CMSAddressVerifyServices {

    private static CMSAddressVerifyServices current;

    public static CMSAddressVerifyServices getCurrent() {return current;}

    public static void setCurrent(CMSAddressVerifyServices svcs) { current = svcs; }
    
    /**
     * To search for a verified address. 
     * 
     * @param request
     * @return the response
     * @throws Exception
     */
    public abstract Response search(Request request) throws Exception;
    
}
