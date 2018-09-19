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
* <p>Title:AddressVerifyNullServices.java </p>
*
* <p>Description: AddressVerifyNullServices </p>
*
* <p>Copyright: Copyright (c) 2005</p>
*
* <p>Company: Skillnet inc.</p>
*
* @author David Fung
* @version 1.0
*/

public class AddressVerifyNullServices extends CMSAddressVerifyServices {
    
    public AddressVerifyNullServices() {
    }    
    
    public Response search(Request request) throws Exception {
        return null;
    }

}
