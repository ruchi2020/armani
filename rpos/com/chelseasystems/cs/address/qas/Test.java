package com.chelseasystems.cs.address.qas;

import com.chelseasystems.cr.appmgr.IRepositoryManager;

class Test {

    public static void main(String[] args) {
        IRepositoryManager theMgr = null;
        System.out.println("com.chelseasystems.cs.address.qas.Test! ...");
        String dataId = "USA";
        String[] userInput = { "500 Oracle Pkwy", "", "", "Redwood City", "CA", "94065" };
        QASAddressVerifyServices serv = new QASAddressVerifyServices();
        try {
            QASRequest r1 = new QASRequest(dataId, userInput);
            
            QASResponse q1 = (QASResponse)serv.search(r1);
            System.out.println(q1);

        } catch (Throwable e) {
            System.out.println("--->" + e);
            e.printStackTrace(); 
        }
    }

}