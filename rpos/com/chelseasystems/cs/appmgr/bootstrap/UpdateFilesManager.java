// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) definits radix(10) lradix(10)
// Source File Name:   BootStrapManager.java

package com.chelseasystems.cs.appmgr.bootstrap;

import com.chelseasystems.cr.appmgr.IBrowserManager;
import com.chelseasystems.cr.config.ConfigMgr;
import java.awt.Window;
import java.io.PrintStream;
import java.util.StringTokenizer;
import java.util.Vector;
import com.chelseasystems.cr.appmgr.bootstrap.BootStrapInfo;
import com.chelseasystems.cr.appmgr.bootstrap.InitialBootStrap;
import com.chelseasystems.cr.appmgr.bootstrap.IBootStrap;
import com.chelseasystems.cr.appmgr.bootstrap.BootStrapManager;

// Referenced classes of package com.chelseasystems.cr.appmgr.bootstrap:
//            IBootStrap, InitialBootStrap, BootStrapInfo

public class UpdateFilesManager
{

    public UpdateFilesManager(String ConfigFile, IBrowserManager theMgr, Window window)
    {
        vBoot = new Vector();
        strapCount = 0;
        startNum = 0;
        this.ConfigFile = ConfigFile;
        this.theMgr = theMgr;
        this.window = window;
        loadBootStraps();
        this.bootMgr = new BootStrapManager(ConfigFile, theMgr, window);
    }

    public int getBootStrapSize()
    {
        return vBoot.size();
    }

    public boolean hasMoreBootStraps()
    {
        if(startNum == vBoot.size())
        {
            //initStrap.closeDlgs();
            return false;
        } else
        {
            return true;
        }
    }

    public BootStrapInfo nextBootStrap()
    {
        IBootStrap strap = (IBootStrap)vBoot.elementAt(startNum);
        startNum++;
        System.out.println("%%% Starting BootStrap: " + strap.getName() + " %%%");
        BootStrapInfo info = strap.start(theMgr, window, bootMgr);
        double x = (double)(startNum + 1) / (double)(vBoot.size() + 1);
        return info;
    }

    private void loadBootStraps()
    {
        try
        {
            ConfigMgr mgr = new ConfigMgr(ConfigFile);
            String boot = mgr.getString("UPDATE_FLAT_FILES");
            for(StringTokenizer tokenizer = new StringTokenizer(boot, ","); tokenizer.hasMoreElements();)
            {
                String sClass = (String)tokenizer.nextElement();
                try
                {
                    Class cls = Class.forName(sClass);
                    Object obj = cls.newInstance();
                    vBoot.addElement(obj);
                }
                catch(Exception ex)
                {
                    System.out.println("ERROR loadBootStraps()->" + ex);
                }
            }

        }
        catch(Exception ex)
        {
            System.out.println("ERROR loadBootStraps()->" + ex);
        }
    }

    private String ConfigFile = null;
    private Vector vBoot = null;
    private IBrowserManager theMgr = null;
    private Window window = null;
    private InitialBootStrap initStrap = null;
    private int strapCount = 0;
    private int startNum = 0;
    private BootStrapManager bootMgr=null;
}
