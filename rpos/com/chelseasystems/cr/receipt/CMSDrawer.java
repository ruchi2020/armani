// Decompiled by DJ v3.6.6.79 Copyright 2004 Atanas Neshkov  Date: 5/1/2007 6:15:02 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CMSDrawer.java

package com.chelseasystems.cr.receipt;

import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.logging.LoggingServices;
import java.io.PrintStream;
import jpos.*;

public class CMSDrawer
    implements JposConst
{

    public CMSDrawer()
    {
    }

    public static CashDrawer getInstance()
    {
        return varDrawer;
    }

    public static void openDevice(String Name)
        throws Exception
    {
        System.out.println("REQUEST TO OPEN CASH DRAWER");
        System.out.println("KEEP DRAWER OPEN CONFIG IS SET TO: " + keepDrawerOpen);
        if(keepDrawerOpen && hasDrawerBeenOpened)
        {
            System.out.println("DRAWER DOES NOT NEED TO BE OPENED");
            return;
        }
        System.out.println("PROCEED TO OPEN DRAWER");
        hasDrawerBeenOpened = true;
        if(isJPOSDrawer)
        {
            if(varDrawer.getState() == 102)
            {
                System.out.println("Drawer already claimed.");
                return;
            }
            varDrawer.open(Name);
            varDrawer.claim(1000);
            varDrawer.setDeviceEnabled(true);
        }
    }

    public static void closeDevice()
    {
        if(keepDrawerOpen)
            return;
        if(isJPOSDrawer)
            try
            {
                if(varDrawer.getState() == 1)
                {
                    System.out.println("Printer already closed.");
                    return;
                }
                for(; varDrawer.getState() == 3; System.out.println("Printer busy...waiting to closed."))
                    try
                    {
                        Thread.sleep(1000L);
                    }
                    catch(Exception e) { }

                varDrawer.release();
                varDrawer.close();
            }
            catch(JposException e)
            {
                LoggingServices.getCurrent().logMsg("com.chelseasystems.jpos.CMSDrawer", "closeDevice()", "Cannot close drawer.", "N/A", 2);
                System.out.println("FINAL CLOSE Drawer Code e -> " + e);
                System.out.println("FINAL CLOSE DrawerError Code -> " + e.getErrorCode());
                System.out.println("FINAL CLOSE DrawerExt Error Code -> " + e.getErrorCodeExtended());
            }
    }

    public static void openDrawer()
    {
        try
        {
            if(isJPOSDrawer)
                varDrawer.openDrawer();
            else
                Runtime.getRuntime().exec(drawerOpenString);
        }
        catch(Exception ex)
        {
            LoggingServices.getCurrent().logMsg("com.chelseasystems.jpos.CMSDrawer", "openDrawer()", "Cannot open drawer.", "N/A", 2, ex);
        }
    }

    public static boolean isDrawerOpened()
    {
        if(isJPOSDrawer)
        {
            try
            {
                return varDrawer.getDrawerOpened();
            }
            catch(Exception ex)
            {
                LoggingServices.getCurrent().logMsg("com.chelseasystems.jpos.CMSDrawer", "isDrawerOpened()", "Cannot determine is drawer is opened.", "N/A", 2, ex);
            }
            return false;
        } else
        {
            return false;
        }
    }

    private static CashDrawer varDrawer = new CashDrawer();
    private static boolean isJPOSDrawer = (new ConfigMgr("JPOS_peripherals.cfg")).getString("IS_JPOS_DRAWER").equalsIgnoreCase("TRUE");
    private static String drawerOpenString = (new ConfigMgr("JPOS_peripherals.cfg")).getString("DRAWER_OPEN_STRING");
    private static boolean keepDrawerOpen = (new ConfigMgr("JPOS_peripherals.cfg")).getString("KEEP_DRAWER_OPEN") != null && (new ConfigMgr("JPOS_peripherals.cfg")).getString("KEEP_DRAWER_OPEN").equalsIgnoreCase("TRUE");
    private static boolean hasDrawerBeenOpened = false;

}