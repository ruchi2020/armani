/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) radix(10) lradix(10) 
// Source File Name:   SerializeCorrectionUtil.java

package com.chelseasystems.cs.util;

import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.pos.POSLineItem;
import com.chelseasystems.cr.store.Store;
import com.chelseasystems.cr.util.ObjectStore;
import com.chelseasystems.cs.discount.CMSDiscount;
import com.chelseasystems.cs.pos.CMSCompositePOSTransaction;
import com.chelseasystems.cs.pos.CMSCompositePOSTransactionAppModel;
import com.chelseasystems.cs.swing.CMSAppModelFactory;
import java.io.File;
import java.io.PrintStream;

public class SerializeCorrectionUtil
{

    public SerializeCorrectionUtil()
    {
    }

    public void openAndPrintFile(String fileName, IApplicationManager theAppMgr)
    {
        try
        {
            ObjectStore objectStore = new ObjectStore(fileName);
            CMSCompositePOSTransaction object = (CMSCompositePOSTransaction)objectStore.read();
            ((CMSCompositePOSTransactionAppModel)object.getAppModel(CMSAppModelFactory.getInstance(), theAppMgr)).printReceipt(theAppMgr);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void displayDiscountDetails(CMSCompositePOSTransaction object)
    {
        com.chelseasystems.cr.discount.Discount discounts[] = object.getDiscountsArray();
        POSLineItem posLineItem[] = object.getLineItemsArray();
        System.out.println((new StringBuilder()).append("NUMBER OF LINE ITEMS: ").append(posLineItem.length).toString());
        CMSDiscount discount = null;
        if(posLineItem != null)
        {
            for(int i = 0; i < posLineItem.length; i++)
            {
                com.chelseasystems.cr.discount.Discount lineItemDiscounts[] = posLineItem[i].getDiscountsArray();
                System.out.println((new StringBuilder()).append("DISCOUNTS ASSOCIATED: ").append(lineItemDiscounts.length).append("LINE ITEM SEQ NUM: ").append(posLineItem[i].getSequenceNumber()).toString());
                for(int index = 0; index < lineItemDiscounts.length; index++)
                {
                    discount = (CMSDiscount)lineItemDiscounts[index];
                    System.out.println((new StringBuilder()).append("AMOUNT: ").append(discount.getAmount()).toString());
                    System.out.println((new StringBuilder()).append("TYPE: ").append(discount.getType()).toString());
                    System.out.println((new StringBuilder()).append("SEQUENCE NUMBER: ").append(discount.getSequenceNumber()).toString());
                }

            }

        }
        System.out.println("==========================");
        POSLineItem deletedItems[] = object.getSaleDeletedLineItemsArray();
        System.out.println((new StringBuilder()).append("NUMBER OF SALE DELETED LINE ITEMS: ").append(deletedItems.length).toString());
        com.chelseasystems.cr.discount.Discount deletedDiscounts[] = object.getSaleDeletedLineItemsArray()[0].getDiscountsArray();
        System.out.println((new StringBuilder()).append("DISCOUNTS ASSOCIATED: ").append(deletedDiscounts.length).toString());
        for(int j = 0; j < deletedItems.length; j++)
        {
            System.out.println((new StringBuilder()).append(" deleted line item seq num: ").append(deletedItems[j].getSequenceNumber()).toString());
            for(int g = 0; g < deletedDiscounts.length; g++)
                System.out.println((new StringBuilder()).append("DELETED LINE SEQ NUMBER :  ").append(((CMSDiscount)deletedDiscounts[g]).getSequenceNumber()).toString());

        }

        System.out.println("$$$$$$$$$$$$$$$$");
        for(int i = 0; i < discounts.length; i++)
        {
            System.out.println((new StringBuilder()).append("SEQUENCE NUMBER: ").append(((CMSDiscount)discounts[i]).getSequenceNumber()).toString());
            System.out.println((new StringBuilder()).append("AMOUNT: ").append(((CMSDiscount)discounts[i]).getAmount()).toString());
            System.out.println((new StringBuilder()).append("TYPE: ").append(((CMSDiscount)discounts[i]).getType()).toString());
        }

        System.out.println("###############");
        com.chelseasystems.cr.discount.Discount settleDiscounts[] = object.getSettlementDiscountsArray();
        for(int i = 0; i < settleDiscounts.length; i++)
            System.out.println((new StringBuilder()).append("SETTLEMENT DISCOUNT SEQUENCE NUMBER: ").append(((CMSDiscount)settleDiscounts[i]).getSequenceNumber()).toString());

    }

    public void printTxnNumber(CMSCompositePOSTransaction object)
    {
        String trxnId = object.getId();
        String txnDelim = "*";
        int storeAndRegLength = object.getStore().getId().trim().length();
        storeAndRegLength += object.getRegisterId().trim().length();
        if(txnDelim != null && trxnId.indexOf(txnDelim) != -1)
            storeAndRegLength += 2 * txnDelim.length();
        String seqNum = object.getId().substring(storeAndRegLength);
        System.out.println((new StringBuilder()).append("Object id ---------------->").append(object.getId()).toString());
        System.out.println((new StringBuilder()).append("printTxnNumber TransactionNumber ------------>").append(seqNum).toString());
    }

    public void openAndSaveFile(String fileName)
    {
        try
        {
            File rootDir = new File(fileName);
            if(rootDir.isDirectory())
            {
                File childFiles[] = rootDir.listFiles();
                File arr$[] = childFiles;
                int len$ = arr$.length;
                for(int i$ = 0; i$ < len$; i$++)
                {
                    File childFile = arr$[i$];
                    if(!childFile.getAbsolutePath().endsWith("fixed"))
                        openAndSaveFile(childFile.getAbsolutePath());
                }

            } else
            {
                ObjectStore objectStore = new ObjectStore(fileName);
                System.out.println(objectStore.exists());
                CMSCompositePOSTransaction object = (CMSCompositePOSTransaction)objectStore.read();
                fix1832BrokenTxnItemId(object);
                saveFile(fileName, object);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void fix1832BrokenTxnItemId(CMSCompositePOSTransaction cmscompositepostransaction)
    {
    }

    public void saveFile(String fileName, Object workingObject)
    {
        String newFile = fileName.substring(fileName.lastIndexOf("\\"), fileName.length());
        String newDir = (new StringBuilder()).append(fileName.substring(0, fileName.lastIndexOf("\\"))).append("fixed\\").toString();
        System.out.println((new StringBuilder()).append(" newDir + newFile = ").append(newDir).append(newFile).toString());
        try
        {
            ObjectStore objectStore = new ObjectStore(new File((new StringBuilder()).append(newDir).append(newFile).toString()));
            objectStore.write(workingObject);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static void main(String args[])
    {
        SerializeCorrectionUtil util = new SerializeCorrectionUtil();
       //util.openAndPrintFile(args[0], null);
        util.openAndSaveFile(args[0]);
    }
}


