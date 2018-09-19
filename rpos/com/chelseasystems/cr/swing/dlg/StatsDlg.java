/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.

package com.chelseasystems.cr.swing.dlg;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.download.update.ManifestHelper;
import com.chelseasystems.cr.swing.*;
import com.chelseasystems.cr.swing.panel.AboutPanel;
import com.chelseasystems.cr.util.ResourceManager;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

public class StatsDlg extends JDialog
{

    public StatsDlg(AppManager theAppMgr)
    {
        super(theAppMgr.getParentFrame(), "", true);
        res = ResourceManager.getResourceBundle();
        tabMain = new JTabbedPane();
        btnClose = new JButton();
        modelState = new RepositoryModel();
        tblState = new JTable(modelState);
        modelGlobal = new RepositoryModel();
        tblGlobal = new JTable(modelGlobal);
        edtDateStarted = new JTextField();
        edtStarted = new JTextField();
        edtTimeUp = new JTextField();
        edtTotalMemory = new JTextField();
        edtFreeMemory = new JTextField();
        edtScreenName = new JTextField();
        edtAppletName = new JTextField();
        edtAddress = new JTextField();
        edtPort = new JTextField();
        txtConfig = new JTextArea();
        modelDownload = new DownloadModel();
        tblDownload = new JTable(modelDownload);
        themeModel = new DefaultComboBoxModel();
        cboTheme = new JComboBox(themeModel);
        modelManifest = new ManifestModel();
        tblManifest = new JTable(modelManifest);
        edtRevision = new JTextField();
        edtProcessDate = new JTextField();
        edtMode = new JTextField();
        lblMode = new JLabel();
        lblTheme1 = new JLabel();
        lblTheme2 = new JLabel();
        lblTheme3 = new JLabel();
        lblAnchor = new JLabel();
        cboAnchor = new JComboBox();
        modelPeers = new DefaultListModel();
        modelKeys = new DefaultListModel();
        pnlAbout = new AboutPanel();
        modelVersion = new DefaultTableModel();
        tblVersion = new JTable(modelVersion);
        try
        {
            setTitle(res.getString("Client Statistics"));
            this.theAppMgr = theAppMgr;
            jbInit();
            populateDialog();
            pack();
            setSize(550, 425);
            setResizable(false);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public int getAnchorSelection()
    {
        return cboAnchor.getSelectedIndex();
    }

    public boolean isAnchorSelectionChanged()
    {
        return cboAnchor.getSelectedIndex() != ((IMainFrame)theAppMgr.getParentFrame()).getAnchor();
    }

    public String getThemeSelection()
    {
        return themeKeys[cboTheme.getSelectedIndex()];
    }

    public boolean isThemeSelectionChanged()
    {
        return themeSelectionHasChanged;
    }

    public String formatTime(long millis)
    {
        long hrElapsed = millis / 3600000L;
        long minElapsed = (millis % 3600000L) / 60000L;
        long secElapsed = millis / 1000L - 3600L * hrElapsed - 60L * minElapsed;
        if(secElapsed > 30L)
            minElapsed++;
        return String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(hrElapsed)))).append(" ").append(res.getString("hrs")).append(" ").append(minElapsed).append(" ").append(res.getString("min"))));
    }

    public static void main(String args[])
    {
        System.setProperty("USER_CONFIG", "client_master.cfg");
        ConfigMgr mgr = new ConfigMgr("client_master.cfg");
        ResourceManager.setResourceBundle(mgr.getString("MESSAGE_BUNDLE"));
        StatsDlg dlg = new StatsDlg(null);
        dlg.show();
        System.exit(0);
    }

    void btnClose_actionPerformed(ActionEvent e)
    {
        setVisible(false);
    }

    void paneGlobal_componentResized(ComponentEvent e)
    {
        TableColumn col0 = tblGlobal.getColumnModel().getColumn(0);
        col0.setWidth(130);
        TableColumn col1 = tblGlobal.getColumnModel().getColumn(1);
        col1.setWidth(tblGlobal.getWidth() - col0.getWidth());
    }

    void paneState_componentResized(ComponentEvent e)
    {
        TableColumn col0 = tblState.getColumnModel().getColumn(0);
        col0.setWidth(130);
        TableColumn col1 = tblState.getColumnModel().getColumn(1);
        col1.setWidth(tblState.getWidth() - col0.getWidth());
    }

    private void jbInit()
        throws Exception
    {
        setDefaultCloseOperation(2);
        JLabel lblTheme = new JLabel();
        JScrollPane paneManifest = new JScrollPane(tblManifest);
        JLabel lblRevision = new JLabel();
        JLabel lblProcessDate = new JLabel();
        JPanel pnlDownload = new JPanel();
        JScrollPane paneDownload = new JScrollPane(tblDownload);
        JLabel lblAppletName = new JLabel();
        JPanel pnlAddress = new JPanel();
        JPanel pnlTheme = new JPanel();
        JLabel lblAddress = new JLabel();
        JLabel lblPort = new JLabel();
        JPanel pnlManifest = new JPanel();
        JLabel lblTimeUp = new JLabel();
        JLabel lblTotMem = new JLabel();
        JLabel lblFreeMem = new JLabel();
        JPanel pnlApplet = new JPanel();
        JLabel lblscreen = new JLabel();
        JScrollPane paneState = new JScrollPane(tblState);
        JScrollPane paneGlobal = new JScrollPane(tblGlobal);
        JLabel lblDateStarted = new JLabel();
        JLabel lblStarted = new JLabel();
        JPanel pnlTime = new JPanel();
        JPanel pnlMemory = new JPanel();
        JPanel pnlStats = new JPanel();
        JPanel pnlState = new JPanel();
        JPanel pnlGlobal = new JPanel();
        JPanel pnlMain = new JPanel();
        JPanel pnlSouth = new JPanel();
        JPanel pnlPeers = new JPanel();
        JScrollPane scrollPeers = new JScrollPane();
        JList lstPeers = new JList(modelPeers);
        JScrollPane scrollKeys = new JScrollPane();
        JList lstKeys = new JList(modelKeys);
        JLabel lblPeers = new JLabel();
        JLabel lblKeys = new JLabel();
        JPanel pnlVersion = new JPanel();
        JScrollPane paneVersion = new JScrollPane(tblVersion);
        pnlMain.setLayout(new BorderLayout());
        pnlMain.setBorder(BorderFactory.createEtchedBorder());
        pnlSouth.setPreferredSize(new Dimension(10, 50));
        pnlSouth.setLayout(new FlowLayout(2, 5, 3));
        pnlSouth.setBorder(BorderFactory.createEtchedBorder());
        btnClose.setPreferredSize(new Dimension(100, 40));
        pnlGlobal.setLayout(new BorderLayout());
        pnlState.setLayout(new BorderLayout());
        getRootPane().setDefaultButton(btnClose);
        btnClose.setText(res.getString("Close"));
        btnClose.setMnemonic(res.getString("Mnemonic_Close").charAt(0));
        btnClose.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                btnClose_actionPerformed(e);
            }

            final StatsDlg this$0;

            
            {
                this$0 = StatsDlg.this;
                //super();
            }
        }
);
        pnlTime.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), res.getString("Time")));
        pnlTime.setLayout(new GridBagLayout());
        pnlMemory.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), res.getString("Memory")));
        pnlMemory.setLayout(new GridBagLayout());
        paneGlobal.addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent e)
            {
                paneGlobal_componentResized(e);
            }

            final StatsDlg this$0;

            
            {
                this$0 = StatsDlg.this;
                //super();
            }
        }
);
        paneState.addComponentListener(new ComponentAdapter() {

            public void componentResized(ComponentEvent e)
            {
                paneState_componentResized(e);
            }

            final StatsDlg this$0;

            
            {
                this$0 = StatsDlg.this;
                //super();
            }
        }
);
        pnlStats.setLayout(new GridBagLayout());
        lblDateStarted.setText(String.valueOf(String.valueOf(res.getString("Date Started"))).concat(":"));
        lblStarted.setText(String.valueOf(String.valueOf(res.getString("Time Started"))).concat(":"));
        lblTimeUp.setText(String.valueOf(String.valueOf(res.getString("Time Up"))).concat(":"));
        edtDateStarted.setEditable(false);
        edtStarted.setEditable(false);
        edtTimeUp.setEditable(false);
        lblTotMem.setText(String.valueOf(String.valueOf(res.getString("Total Memory"))).concat(":"));
        edtTotalMemory.setEditable(false);
        lblFreeMem.setText(String.valueOf(String.valueOf(res.getString("Free Memory"))).concat(":"));
        edtFreeMemory.setEditable(false);
        pnlApplet.setLayout(new FlowLayout(0));
        lblscreen.setPreferredSize(new Dimension(100, 17));
        lblscreen.setText(String.valueOf(String.valueOf(res.getString("Screen Name"))).concat(":"));
        lblAppletName.setPreferredSize(new Dimension(100, 17));
        lblAppletName.setText(String.valueOf(String.valueOf(res.getString("Applet"))).concat(":"));
        edtScreenName.setPreferredSize(new Dimension(400, 25));
        edtScreenName.setEditable(false);
        edtAppletName.setPreferredSize(new Dimension(400, 25));
        edtAppletName.setEditable(false);
        pnlAddress.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), res.getString("IP Address")));
        pnlAddress.setLayout(new GridBagLayout());
        lblAddress.setText(String.valueOf(String.valueOf(res.getString("IP Address"))).concat(":"));
        edtAddress.setEditable(false);
        lblPort.setText(String.valueOf(String.valueOf(res.getString("Manage Port"))).concat(":"));
        edtPort.setEditable(false);
        pnlDownload.setLayout(new BorderLayout());
        pnlTheme.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), res.getString("Window")));
        pnlTheme.setLayout(new GridBagLayout());
        lblTheme.setText(String.valueOf(String.valueOf(res.getString("Theme"))).concat(":"));
        cboTheme.setEditable(false);
        pnlManifest.setLayout(new BorderLayout());
        lblRevision.setText(String.valueOf(String.valueOf(res.getString("Revision"))).concat(":"));
        lblRevision.setPreferredSize(new Dimension(100, 17));
        edtRevision.setEditable(false);
        edtRevision.setPreferredSize(new Dimension(200, 25));
        edtMode.setEditable(false);
        lblMode.setText(String.valueOf(String.valueOf(res.getString("Mode"))).concat(":"));
        lblAnchor.setText(String.valueOf(String.valueOf(res.getString("Anchor"))).concat(":"));
        cboAnchor.setEnabled(false);
        pnlPeers.setLayout(new GridBagLayout());
        lblPeers.setText(res.getString("Known Peers"));
        lblKeys.setText(res.getString("Peer Keys"));
        lblProcessDate.setHorizontalAlignment(2);
        lblProcessDate.setText(String.valueOf(String.valueOf(res.getString("Process Date"))).concat(":"));
        edtProcessDate.setEditable(false);
        pnlVersion.setLayout(new BorderLayout());
        getContentPane().add(pnlMain);
        pnlMain.add(pnlSouth, "South");
        pnlSouth.add(btnClose, null);
        pnlMain.add(tabMain, "Center");
        pnlStats.add(pnlTime, new GridBagConstraints(0, 0, 1, 1, 1.0D, 1.0D, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        pnlTime.add(lblDateStarted, new GridBagConstraints(0, 1, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(5, 5, 0, 0), 0, 0));
        pnlTime.add(edtDateStarted, new GridBagConstraints(1, 1, 1, 1, 1.0D, 0.0D, 17, 2, new Insets(5, 5, 0, 5), 0, 5));
        pnlTime.add(lblStarted, new GridBagConstraints(0, 2, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(5, 5, 0, 0), 0, 0));
        pnlTime.add(edtStarted, new GridBagConstraints(1, 2, 1, 1, 1.0D, 0.0D, 17, 2, new Insets(5, 5, 0, 5), 0, 5));
        pnlTime.add(lblTimeUp, new GridBagConstraints(0, 3, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(5, 5, 0, 0), 0, 0));
        pnlTime.add(edtTimeUp, new GridBagConstraints(1, 3, 1, 1, 1.0D, 0.0D, 17, 2, new Insets(5, 5, 0, 5), 0, 5));
        pnlTime.add(lblProcessDate, new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(5, 5, 0, 0), 0, 0));
        pnlTime.add(edtProcessDate, new GridBagConstraints(1, 0, 1, 1, 1.0D, 0.0D, 17, 2, new Insets(5, 5, 0, 5), 0, 5));
        pnlStats.add(pnlTheme, new GridBagConstraints(1, 0, 1, 1, 1.0D, 1.0D, 10, 1, new Insets(0, 0, 0, 1), 0, 0));
        pnlTheme.add(lblMode, new GridBagConstraints(0, 1, 1, 1, 0.0D, 0.0D, 18, 0, new Insets(5, 5, 0, 5), 0, 0));
        pnlTheme.add(edtMode, new GridBagConstraints(1, 1, 1, 1, 1.0D, 0.0D, 17, 2, new Insets(5, 5, 0, 5), 0, 5));
        pnlTheme.add(lblTheme, new GridBagConstraints(0, 2, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(5, 5, 0, 0), 0, 0));
        pnlTheme.add(cboTheme, new GridBagConstraints(1, 2, 1, 1, 1.0D, 0.0D, 10, 2, new Insets(5, 5, 0, 5), 0, 5));
        pnlTheme.add(lblAnchor, new GridBagConstraints(0, 3, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(5, 5, 0, 0), 0, 0));
        pnlTheme.add(cboAnchor, new GridBagConstraints(1, 3, 1, 1, 1.0D, 0.0D, 11, 2, new Insets(5, 5, 0, 5), 0, 5));
        pnlStats.add(pnlAddress, new GridBagConstraints(0, 1, 1, 1, 1.0D, 1.0D, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        pnlAddress.add(lblAddress, new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(5, 5, 0, 0), 0, 0));
        pnlAddress.add(edtAddress, new GridBagConstraints(1, 0, 1, 1, 1.0D, 0.0D, 17, 2, new Insets(5, 5, 0, 5), 0, 5));
        pnlAddress.add(lblPort, new GridBagConstraints(0, 1, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(5, 5, 0, 0), 0, 0));
        pnlAddress.add(edtPort, new GridBagConstraints(1, 1, 1, 1, 1.0D, 0.0D, 17, 2, new Insets(5, 5, 0, 5), 0, 5));
        pnlStats.add(pnlMemory, new GridBagConstraints(1, 1, 1, 1, 1.0D, 1.0D, 10, 1, new Insets(0, 0, 0, 1), 0, 0));
        pnlMemory.add(lblTotMem, new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(5, 5, 0, 0), 0, 0));
        pnlMemory.add(edtTotalMemory, new GridBagConstraints(1, 0, 1, 1, 1.0D, 0.0D, 17, 2, new Insets(5, 5, 0, 5), 0, 5));
        pnlMemory.add(lblFreeMem, new GridBagConstraints(0, 1, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(5, 5, 0, 0), 0, 0));
        pnlMemory.add(edtFreeMemory, new GridBagConstraints(1, 1, 1, 1, 1.0D, 0.0D, 17, 2, new Insets(5, 5, 0, 5), 0, 5));
        tabMain.addTab(res.getString("Stats"), pnlStats);
        tabMain.addTab(res.getString("Version"), pnlVersion);
        tabMain.addTab(res.getString("State"), pnlState);
        tabMain.addTab(res.getString("Global"), pnlGlobal);
        tabMain.addTab(res.getString("Applet"), pnlApplet);
        tabMain.addTab(res.getString("Download"), pnlDownload);
        tabMain.addTab(res.getString("Manifest"), pnlManifest);
        tabMain.addTab(res.getString("Peers"), pnlPeers);
        tabMain.addTab(res.getString("About"), pnlAbout);
        pnlState.add(paneState, "Center");
        tblState.setAutoResizeMode(3);
        pnlGlobal.add(paneGlobal, "Center");
        pnlApplet.add(lblscreen, null);
        pnlApplet.add(edtScreenName, null);
        pnlApplet.add(lblAppletName, null);
        pnlApplet.add(edtAppletName, null);
        pnlApplet.add(lblRevision, null);
        pnlApplet.add(edtRevision, null);
        pnlDownload.add(paneDownload, "Center");
        pnlManifest.add(paneManifest, "Center");
        pnlPeers.add(scrollKeys, new GridBagConstraints(0, 1, 1, 1, 1.0D, 1.0D, 10, 1, new Insets(0, 5, 0, 5), 0, 0));
        pnlPeers.add(lblKeys, new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
        pnlPeers.add(lblPeers, new GridBagConstraints(1, 0, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(0, 0, 0, 0), 0, 5));
        pnlPeers.add(scrollPeers, new GridBagConstraints(1, 1, 1, 1, 1.0D, 1.0D, 10, 1, new Insets(0, 5, 0, 5), 0, 0));
        pnlVersion.add(paneVersion, "Center");
        scrollPeers.getViewport().add(lstPeers, null);
        scrollKeys.getViewport().add(lstKeys, null);
        paneVersion.getViewport().add(tblVersion, null);
        tblGlobal.setAutoResizeMode(3);
        tblManifest.setAutoResizeMode(3);
        cboTheme.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e)
            {
                themeSelectionHasChanged = true;
            }

            final StatsDlg this$0;

            
            {
                this$0 = StatsDlg.this;
                //super();
            }
        }
);
    }

    private void loadNativeCommands()
    {
        try
        {
            ConfigMgr mgr = new ConfigMgr(System.getProperty("USER_CONFIG"));
            String delimiter = mgr.getString("NATIVE.DELIMITER");
            String commands = mgr.getString("NATIVE.COMMAND");
            String cmd;
            NativePanel pnl;
            for(StringTokenizer tok = new StringTokenizer(commands, delimiter); tok.hasMoreElements(); tabMain.addTab(cmd, pnl))
            {
                cmd = (String)tok.nextElement();
                pnl = new NativePanel(cmd);
            }

        }
        catch(Exception ex)
        {
            System.out.println("Exception loadNativeCommands()->".concat(String.valueOf(String.valueOf(ex))));
        }
    }

    private void populateDialog()
    {
        try
        {
            edtAddress.setText(InetAddress.getLocalHost().getHostAddress());
            ConfigMgr mgr = new ConfigMgr(System.getProperty("USER_CONFIG"));
            edtPort.setText(mgr.getString("TCP_PORT"));
        }
        catch(Exception ex)
        {
            edtAddress.setText(res.getString("Unknown"));
        }
        Vector columnNames = new Vector(2);
        columnNames.add(res.getString("Module"));
        columnNames.add(res.getString("Version"));
        Vector newData = new Vector();
        Enumeration enm = System.getProperties().propertyNames();
        do
        {
            if(!enm.hasMoreElements())
                break;
            String key = (String)enm.nextElement();
            if(key.endsWith(".version"))
            {
                Vector row = new Vector(2);
                row.add(key);
                row.add(System.getProperty(key));
                newData.add(row);
            }
        } while(true);
        Collections.sort(newData, new Comparator() {

            public int compare(Object o1, Object o2)
            {
                Vector v1 = (Vector)o1;
                Vector v2 = (Vector)o2;
                return v1.get(0).toString().compareTo((String)v2.get(0));
            }

            final StatsDlg this$0;

            
            {
                this$0 = StatsDlg.this;
               // super();
            }
        }
);
        modelVersion.setDataVector(newData, columnNames);
        pnlAbout.setProduct(res.getString("RPOS"));
        pnlAbout.setVersion("3.0.0.0");
        Boolean bTraining = (Boolean)theAppMgr.getGlobalObject("TRAINING");
        if(bTraining != null && bTraining.booleanValue())
            edtMode.setText(res.getString("Training"));
        else
            edtMode.setText(res.getString("Production"));
        ThemeManager themeMgr = new ThemeManager();
        themeKeys = themeMgr.getThemeIds();
        String currentThemeName = themeMgr.getDefaultID();
        for(int i = 0; i < themeKeys.length; i++)
        {
            String themeName = res.getString(themeMgr.getThemeName(themeKeys[i]));
            themeModel.addElement(themeName);
            if(themeKeys[i].equals(currentThemeName))
                currentThemeName = themeName;
        }

        cboTheme.setSelectedItem(currentThemeName);
        cboAnchor.addItem(res.getString("Bottom Right"));
        cboAnchor.addItem(res.getString("Bottom Left"));
        cboAnchor.addItem(res.getString("Upper Right"));
        cboAnchor.addItem(res.getString("Upper Left"));
        cboAnchor.setSelectedIndex(((IMainFrame)theAppMgr.getParentFrame()).getAnchor());
        edtTotalMemory.setText(String.valueOf(String.valueOf(Runtime.getRuntime().totalMemory())).concat(""));
        edtFreeMemory.setText(String.valueOf(String.valueOf(Runtime.getRuntime().freeMemory())).concat(""));
        SimpleDateFormat fmt = new SimpleDateFormat(res.getString("MM/dd/yyyy"));
        edtProcessDate.setText(fmt.format((Date)theAppMgr.getGlobalObject("PROCESS_DATE")));
        edtDateStarted.setText(fmt.format(theAppMgr.getTimeStarted()));
        fmt = new SimpleDateFormat(res.getString("hh:mm:ss z"));
        edtStarted.setText(fmt.format(theAppMgr.getTimeStarted()));
        edtTimeUp.setText(formatTime(theAppMgr.getElaspedTime()));
        String states[] = theAppMgr.getStateKeys();
        Arrays.sort(states);
        for(int x = 0; x < states.length; x++)
        {
            String row[] = new String[2];
            row[0] = states[x];
            Object obj = theAppMgr.getStateObject(states[x]);
            row[1] = obj.getClass().getName();
            modelState.addRow(row);
        }

        String globals[] = theAppMgr.getGlobalKeys();
        Arrays.sort(globals);
        for(int x = 0; x < globals.length; x++)
        {
            String row[] = new String[2];
            row[0] = globals[x];
            Object obj = theAppMgr.getGlobalObject(globals[x]);
            row[1] = obj.getClass().getName();
            modelGlobal.addRow(row);
        }

        edtScreenName.setText(theAppMgr.getAppletManager().getCurrentCMSApplet().getScreenName());
        edtAppletName.setText(theAppMgr.getAppletManager().getCurrentCMSApplet().getClass().getName());
        String version = theAppMgr.getAppletManager().getCurrentCMSApplet().getVersion();
        edtRevision.setText(version.substring(1, version.length() - 1));
        SimpleDateFormat format = new SimpleDateFormat(res.getString("MM/dd/yyyy hh:mm a z"));
        String globalKeys[] = theAppMgr.getGlobalKeys();
        for(int i = 0; i < globalKeys.length; i++)
            if(globalKeys[i].indexOf("DOWNLOAD_DATE") > 1)
                modelDownload.addRow(new String[] {
                    globalKeys[i], format.format((Date)theAppMgr.getGlobalObject(globalKeys[i]))
                });

        com.chelseasystems.cr.download.update.Manifest manifests[] = ManifestHelper.getManifests(theAppMgr);
        for(int i = 0; i < manifests.length; i++)
            modelManifest.addManifest(manifests[i]);

        loadNativeCommands();
        String keys[] = theAppMgr.getLocalStubKeys();
        for(int i = 0; i < keys.length; i++)
            modelKeys.add(i, keys[i]);

        String peers[] = theAppMgr.getPeerAddresses();
        for(int i = 0; i < peers.length; i++)
            modelPeers.add(i, peers[i]);

    }

    AboutPanel pnlAbout;
    DefaultComboBoxModel themeModel;
    DefaultListModel modelKeys;
    DefaultListModel modelPeers;
    DefaultTableModel modelVersion;
    DownloadModel modelDownload;
    JButton btnClose;
    JComboBox cboAnchor;
    JComboBox cboTheme;
    JLabel lblAnchor;
    JLabel lblMode;
    JLabel lblTheme1;
    JLabel lblTheme2;
    JLabel lblTheme3;
    JTabbedPane tabMain;
    JTable tblDownload;
    JTable tblGlobal;
    JTable tblManifest;
    JTable tblState;
    JTable tblVersion;
    JTextArea txtConfig;
    JTextField edtAddress;
    JTextField edtAppletName;
    JTextField edtDateStarted;
    JTextField edtFreeMemory;
    JTextField edtMode;
    JTextField edtPort;
    JTextField edtProcessDate;
    JTextField edtRevision;
    JTextField edtScreenName;
    JTextField edtStarted;
    JTextField edtTimeUp;
    JTextField edtTotalMemory;
    ManifestModel modelManifest;
    RepositoryModel modelGlobal;
    RepositoryModel modelState;
    private AppManager theAppMgr;
    private ResourceBundle res;
    private String themeKeys[];
    private boolean themeSelectionHasChanged;

}


/*
	DECOMPILATION REPORT

	Decompiled from: C:\Users\mahesh\Desktop\Armani material\clientwindows1.5\US\retek\library\retek_platform.jar
	Total time: 3280 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/