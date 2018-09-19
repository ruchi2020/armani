package com.chelseasystems.cs.address.qas;

import com.chelseasystems.cs.address.CMSAddressVerifyServices;
import com.chelseasystems.cs.address.Request;
import com.chelseasystems.cs.address.Response;

/*
History:
+------+------------+-----------+-----------+----------------------------------------------------+
| Ver# | Date       | By        | Defect #  | Description                                        |
+------+------------+-----------+-----------+----------------------------------------------------+ 
| 1    | 04-04-2006 |David Fung | PCR 67    | QAS                                                |
+------+------------+-----------+-----------+----------------------------------------------------+
*/

/**
* <p>Title:QASAddressVerifyServices.java </p>
*
* <p>Description: QASAddressVerifyServices </p>
*
* <p>Copyright: Copyright (c) 2005</p>
*
* <p>Company: Skillnet inc.</p>
*
* @author David Fung
* @version 1.0
*/

public class QASAddressVerifyServices extends CMSAddressVerifyServices {
    
    public QASAddressVerifyServices() {
    }    
    
    public Response search(Request request) throws Exception {
        return QASEngine.search((QASRequest)request);
    }

}
