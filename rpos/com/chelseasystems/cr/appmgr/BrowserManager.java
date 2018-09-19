/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) definits braces deadcode noctor radix(10) lradix(10)
// Source File Name:   BrowserManager.java


package com.chelseasystems.cr.appmgr;

import com.chelseasystems.cr.appmgr.bootstrap.BootStrapInfo;
import com.chelseasystems.cr.appmgr.bootstrap.BootStrapManager;
import com.chelseasystems.cr.appmgr.daemon.DaemonManager;
import com.chelseasystems.cr.appmgr.peer.PeerManager;
import com.chelseasystems.cr.appmgr.peer.PingerRMIPeerImpl;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cr.config.FileMgr;
import com.chelseasystems.cr.logging.ErrorPrintStream;
import com.chelseasystems.cr.logging.LoggingFileServices;
import com.chelseasystems.cr.logging.LoggingServices;
import com.chelseasystems.cr.messaging.MessageManager;
import com.chelseasystems.cr.multicast.IProcessSocketEvent;
import com.chelseasystems.cr.multicast.MultiCastReader;
import com.chelseasystems.cr.multicast.MultiCastSender;
import com.chelseasystems.cr.multicast.ServerSocketReader;
import com.chelseasystems.cr.multicast.SocketEvent;
import com.chelseasystems.cr.multicast.SocketSender;
import com.chelseasystems.cr.node.ClientManage;
import com.chelseasystems.cr.node.ComponentVitals;
import com.chelseasystems.cr.node.ComponentVitalsBuilder;
import com.chelseasystems.cr.node.ContainerVitals;
import com.chelseasystems.cr.node.ContainerVitalsBuilder;
import com.chelseasystems.cr.node.ContainerVitalsHolder;
import com.chelseasystems.cr.rules.Rule;
import com.chelseasystems.cr.swing.IMainFrame;
import com.chelseasystems.cr.util.ObjectStore;
import com.chelseasystems.cr.util.ObjectStoreException;
import com.chelseasystems.cr.xml.DefaultObjectXML;
import com.chelseasystems.cr.xml.XMLStore;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.server.RemoteStub;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import javax.swing.JOptionPane;


// Referenced classes of package com.chelseasystems.cr.appmgr:
//			RepositoryManager, DownTimeManager, VitalsThread, IBrowserManager,
//			DownTimeListener, Version, AppManager
public class BrowserManager extends RepositoryManager implements IBrowserManager
    , IProcessSocketEvent, DownTimeListener {

  /**
   * put your documentation comment here
   */
  public BrowserManager() {
    this(null);
  }

  /**
   * put your documentation comment here
   * @param   java.awt.Window activeWindow
   */
  protected BrowserManager(java.awt.Window activeWindow) {
    nHoursOfHistory = 4;
    Rule.setRepositoryManager(this);
    setInstance(this);
    this.activeWindow = activeWindow;
    Version.applyVersion();
    htSocketEventProcessors = new Hashtable();
    compBuilder = new ComponentVitalsBuilder(getContainerName(), getUID());
    contBuilder = new ContainerVitalsBuilder(getContainerName(), getUID(), "client.err");
    String redirect = System.getProperty("REDIRECT");
    if (redirect == null) {
      redirectOutput();
    } else if (!redirect.equalsIgnoreCase("false")) {
      redirectOutput();
    }
    ConfigMgr configMgr = new ConfigMgr(System.getProperty("USER_CONFIG"));
    try {
      Object obj = configMgr.getObject("LOGGING_IMPL");
      if (obj == null) {
        System.out.println("Missing LOGGING_IMPL in " + System.getProperty("USER_CONFIG"));
        System.exit( -1);
      }
      LoggingServices logging = (LoggingServices)obj;
      Integer nLogLevel = configMgr.getInteger("LOGGING_LEVEL");
      logging.setFilterLevel(nLogLevel.intValue());
      LoggingServices.setCurrent(logging);
      if (obj instanceof LoggingFileServices) {
        LoggingFileServices logServ = (LoggingFileServices)obj;
        String sFileName = configMgr.getString("LOGGING_FILE_NAME");
        logServ.setLogFile(sFileName);
        Integer nPause = configMgr.getInteger("LOGGING_PAUSE");
        logServ.setPause(nPause.intValue());
        Properties props = System.getProperties();
        props.put("LOGGING_SYSTEM_OUT", configMgr.getString("LOGGING_SYSTEM_OUT"));
        props.put("LOGGING_SYSTEM_ERR", configMgr.getString("LOGGING_SYSTEM_ERR"));
        System.setProperties(props);
      }
    } catch (Exception ex) {
      System.err.println("Exception loadLogging->" + ex);
    }
    peerManager = new PeerManager();
    downTimeManager = new DownTimeManager(this, this);
    downTimeManager.addDownTimeListener(this);
    BootStrapManager bootMgr = new BootStrapManager(System.getProperty("USER_CONFIG"), this
        , activeWindow);
    BootStrapInfo info = bootMgr.startInitialBootStrap();
    loadRepository();
    while (bootMgr.hasMoreBootStraps()) {
      BootStrapInfo infoBoot = bootMgr.nextBootStrap();
      if (infoBoot.isError()) {
        if (this.activeWindow != null) {
          JOptionPane.showMessageDialog(activeWindow, infoBoot.getMsg(), "Shutdown", 0);
          if (infoBoot.isShutdown()) {
            System.exit(0);
          }
        } else {
          LoggingServices.getCurrent().logMsg("Fatal Error loading bootstraps...");
        }
      }
    }
    System.out.println("%%% Allocating PingerImpl %%%");
    try {
      peerManager.registerRemote("pinger", new PingerRMIPeerImpl(downTimeManager));
    } catch (Exception ex) {
      System.err.println("Exception: PingerService" + ex);
    }
    try {
      System.out.println("%%% Vitals Thread %%%");
      allocNewVitalsHistory();
      int minutesWait = configMgr.getInt("VITALS_MINUTES_WAIT");
      int hoursHistory = configMgr.getInt("VITALS_HOURS_HISTORY");
      vitalsThread = new VitalsThread(this, minutesWait, hoursHistory);
      vitalsThread.start();
    } catch (Exception ex) {
      System.err.println("Exception: VitalsThread" + ex);
    }
    try {
      System.out.println("%%% Allocating Client Manage %%%");
      clientManage = new ClientManage(this);
      clientManageStub = UnicastRemoteObject.exportObject(clientManage);
    } catch (Exception ex) {
      System.err.println("Exception: ClientManage" + ex);
    }
    sender = new SocketSender();
    try {
      System.out.println("%%% Allocating MulticastSender %%%");
      String Address = configMgr.getString("MULTICAST_ADDRESS");
      int port = configMgr.getInteger("MULTICAST_PORT").intValue();
      mcSender = new MultiCastSender(Address, port);
      mcSender.start();
    } catch (Exception ex) {
      System.out.println("Exception: MulticastSender" + ex);
    }
    try {
      System.out.println("%%% Allocating ServerSocketReaderr %%%");
      String address = InetAddress.getLocalHost().getHostAddress();
      int port = configMgr.getInt("TCP_PORT");
      socketReader = new ServerSocketReader(this, address, port);
      socketReader.start();
    } catch (Exception ex) {
      System.out.println("Exception: MulticastSender" + ex);
    }
    System.out.println("%%% Load Daemons %%%");
    daemonMgr = new DaemonManager(System.getProperty("USER_CONFIG"), this);
    daemonMgr.start();
    System.out.println("%%% Starting Message Manager %%%");
    messageMgr = new MessageManager(System.getProperty("USER_CONFIG"), this);
    sendStubs();
  }

  /**
   * put your documentation comment here
   * @param anInstance
   */
  public static void setInstance(BrowserManager anInstance) {
    instance = anInstance;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public static BrowserManager getInstance() {
    return instance;
  }

  /**
   * put your documentation comment here
   * @param args[]
   */
  public static void main(String args[]) {
    setInstance(new BrowserManager(null));
  }

  /**
   * put your documentation comment here
   * @return
   */
  public java.awt.Window getActiveWindow() {
    return activeWindow;
  }

  /**
   * put your documentation comment here
   * @param brokenTxnCount
   */
  public void setBrokenTxnCount(int brokenTxnCount) {
    throw new UnsupportedOperationException("Method setBrokenTxnCount() not yet implemented.");
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getBrokenTxnCount() {
    throw new UnsupportedOperationException("Method getBrokenTxnCount() not yet implemented.");
  }

  /**
   * put your documentation comment here
   * @param componentName
   * @return
   */
  public ComponentVitals getComponentVitals(String componentName) {
    return compBuilder.getVitalsInstance();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ComponentVitals getComponentVitals() {
    return compBuilder.getVitalsInstance();
  }

  /**
   * put your documentation comment here
   * @param containerName
   * @return
   */
  public ContainerVitals getContainerVitals(String containerName) {
    return contBuilder.getVitalsInstance();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ContainerVitals getContainerVitals() {
    return contBuilder.getVitalsInstance();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public ContainerVitalsHolder getContainerVitalsHolder() {
    ContainerVitalsHolder holder = null;
    try {
      List list = (new DefaultObjectXML()).getObjectsFromFile(FileMgr.getLocalFile("container"
          , getContainerName() + "_" + getUID() + ".xml"));
      if (list.size() > 0) {
        holder = (ContainerVitalsHolder)list.get(0);
      }
      return holder;
    } catch (Exception ex) {
      System.out.println("Exception getContainerVitalsHolder()->" + ex);
    }
    return holder;
  }

  /**
   * put your documentation comment here
   * @param Name
   * @return
   */
  public Remote getLocalStub(String Name) {
    return peerManager.getLocalStub(Name);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String[] getLocalStubKeys() {
    return peerManager.getLocalStubKeys();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public IMainFrame getMainFrame() {
    return (activeWindow instanceof IMainFrame) ? (IMainFrame)activeWindow : null;
  }

  /**
   * put your documentation comment here
   * @param isOnline
   */
  public void setOnLine(boolean isOnline) {
    downTimeManager.setOnLine(isOnline);
  }

  /**
   * put your documentation comment here
   * @return
   */
  public boolean isOnLine() {
    return downTimeManager.isOnLine();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String[] getPeerAddresses() {
    return peerManager.getPeerAddresses();
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String[] getPeerKeys() {
    return null;
  }

  /**
   * put your documentation comment here
   * @param name
   * @return
   */
  public Remote[] getPeerStubs(String name) {
    return peerManager.getPeerStubs(name);
  }

  /**
   * put your documentation comment here
   * @param pendingTxnCount
   */
  public void setPendingTxnCount(int pendingTxnCount) {
    throw new UnsupportedOperationException("Method setPendingTxnCount() not yet implemented.");
  }

  /**
   * put your documentation comment here
   * @return
   */
  public int getPendingTxnCount() {
    throw new UnsupportedOperationException("Method getPendingTxnCount() not yet implemented.");
  }

  /**
   * put your documentation comment here
   * @param key
   * @param stub
   */
  public void addLocalPeerStub(String key, RemoteStub stub) {
    peerManager.addLocalPeerStub(key, stub);
  }

  /**
   * put your documentation comment here
   * @param methodName
   * @param startTime
   */
  public void addPerformance(String methodName, long startTime) {
    compBuilder.addPerformance(methodName, startTime);
  }

  /**
   * put your documentation comment here
   * @exception Exception
   */
  public void clearExceptions()
      throws Exception {
    clientManage.clearException();
  }

  /**
   * put your documentation comment here
   */
  public void decConnection() {
    compBuilder.decConnection();
  }

  /**
   * put your documentation comment here
   * @param onLine
   */
  public void downTimeEvent(boolean onLine) {
    if (onLine) {
      int count = Thread.activeCount();
      Thread array[] = new Thread[count];
      Thread.enumerate(array);
      for (int i = array.length - 1; i >= 0; i--) {
        if (array[i] instanceof MultiCastReader) {
          System.out.println("restarting thread.." + array[i].getName());
          ConfigMgr config = new ConfigMgr(System.getProperty("USER_CONFIG"));
          String mcAdd = config.getString("MULTICAST_ADDRESS");
          int mcPort = config.getInteger("MULTICAST_PORT").intValue();
          array[i] = new MultiCastReader(this, mcAdd, mcPort);
          array[i].start();
        }
      }
      sendReconnectStub();
    }
  }

  /**
   * put your documentation comment here
   */
  public void goHome() {
    throw new UnsupportedOperationException("Method goHome() not yet implemented.");
  }

  /**
   * put your documentation comment here
   */
  public void incConnection() {
    compBuilder.incConnection();
    contBuilder.incConnection();
  }

  /**
   * put your documentation comment here
   * @param theEvent
   */
  public void processEvent(SocketEvent theEvent) {
    try {
      String socketMessageKey = theEvent.getMessage();
      System.out.println("BrowserMgr received socket event: " + socketMessageKey);
      if (socketMessageKey.equals("PEER_STUBS")) {
        String address = theEvent.getValue("ADDRESS");
        if (!peerManager.containsRemote(address)) {
          System.out.println("Adding stubs from: " + address);
          peerManager.addRemote(address, theEvent);
          if (!address.equals(InetAddress.getLocalHost().getHostAddress())) {
            System.out.println("Address is different()->"
                + InetAddress.getLocalHost().getHostAddress());
            sendStubs();
          }
        } else if (!peerManager.isPeerValid(address)) {
          System.out.println("Address Bad: Adding stubs from: " + address);
          peerManager.removeRemote(address);
          peerManager.addRemote(address, theEvent);
          if (!address.equals(InetAddress.getLocalHost().getHostAddress())) {
            sendStubs();
          }
        }
      } else if (socketMessageKey.equals("GET_MANAGE_STUB")) {
        sendManageStub(theEvent);
      } else if (socketMessageKey.equals("RECONNECT_REQUEST_STUB")) {
        String address = theEvent.getValue("ADDRESS");
        if (!address.equals(InetAddress.getLocalHost().getHostAddress())) {
          System.out.println("Adding reconnect stubs from: " + address);
          peerManager.addRemote(address, theEvent);
          sendStubs();
        }
      } else if (htSocketEventProcessors.containsKey(socketMessageKey)) {
        IProcessSocketEvent processor = (IProcessSocketEvent)htSocketEventProcessors.get(
            socketMessageKey);
        processor.processEvent(theEvent);
      }
    } catch (Exception ex) {
      System.out.println("Exception BrowserManager.processEvent()->" + ex);
    }
  }

  /**
   * put your documentation comment here
   * @param socketMessageKey
   * @param processor
   */
  public void registerSocketEventProcessor(String socketMessageKey, IProcessSocketEvent processor) {
    htSocketEventProcessors.put(socketMessageKey, processor);
  }

  /**
   * put your documentation comment here
   * @param name
   * @param rmt
   */
  public void removeRemotePeerStub(String name, Remote rmt) {
    peerManager.removeRemotePeerStub(name, rmt);
  }

  /**
   * put your documentation comment here
   * @exception Exception
   */
  public void restartTerminal()
      throws Exception {
    ConfigMgr config = new ConfigMgr(System.getProperty("USER_CONFIG"));
    String command = config.getString("SHUTDOWN");
    if (command.equals("RESTART_POS")) {
      StringBuffer buf = new StringBuffer();
      if (System.getProperty("os.name").indexOf("Window") >= 0) {
        buf.append("cmd /c start java ");
      } else {
        buf.append("javaw ");
      }
      String redirect = System.getProperty("REDIRECT");
      buf.append(" -DREDIRECT=");
      buf.append(redirect);
      String policy = System.getProperty("java.security.policy");
      buf.append(" -Djava.security.policy=");
      buf.append(policy);
      String configFile = System.getProperty("USER_CONFIG");
      buf.append(" -DUSER_CONFIG=");
      buf.append(configFile);
      buf.append(" ");
      buf.append(getClass().getName());
      buf.append(" ");
      if (System.getProperty("os.name").indexOf("Window") == -1) {
        buf.append(" &");
      }
      String execCommand = buf.toString();
      System.out.println("LAUNCHING -->" + execCommand);
      Process process = Runtime.getRuntime().exec(execCommand);
    } else {
      System.out.println("CONFIG SHUTDOWN COMMAND LAUNCHING -->" + command);
      Runtime.getRuntime().exec(command);
    }
    System.exit(0);
  }

  /**
   * put your documentation comment here
   * @param holder
   */
  public void saveContainerVitalsHolder(ContainerVitalsHolder holder) {
    try {
      FileWriter writer = new FileWriter(FileMgr.getLocalFile("container"
          , getContainerName() + "_" + getUID() + ".xml"));
      writer.write((new DefaultObjectXML()).toXML(holder));
      writer.flush();
      writer.close();
    } catch (Exception ex) {
      System.err.println("Exception saveContainerVitalsHolder()->" + ex);
    }
  }

  /**
   * put your documentation comment here
   * @param socketMessageKey
   */
  public void unregisterSocketEventProcessor(String socketMessageKey) {
    htSocketEventProcessors.remove(socketMessageKey);
  }

  /**
   * put your documentation comment here
   * @param event
   */
  protected void sendManageStub(SocketEvent event) {
    String address = event.getValue("ADDRESS");
    int port = Integer.parseInt(event.getValue("PORT"));
    SocketEvent reply = new SocketEvent("MANAGE_STUB");
    reply.addKeyValue("STUB", clientManageStub);
    sender.sendEvent(address, port, socketReader.getCurrentSocket(), reply);
  }

  /**
   * put your documentation comment here
   */
  protected void sendReconnectStub() {
    try {
      SocketEvent theEvent = new SocketEvent("RECONNECT_REQUEST_STUB");
      theEvent.addKeyValue("ADDRESS", InetAddress.getLocalHost().getHostAddress());
      String keys[] = getLocalStubKeys();
      for (int x = 0; x < keys.length; x++) {
        System.out.println("Right Now Sending Reconnect: " + keys[x]);
        theEvent.addKeyValue(keys[x], getLocalStub(keys[x]));
      }
      mcSender.sendMultiCastEvent(theEvent);
    } catch (Exception ex) {
      System.out.println("Exception BrowserManager.sendReconnectStub()->" + ex);
    }
  }

  /**
   * put your documentation comment here
   */
  protected void sendStubs() {
    try {
      SocketEvent theEvent = new SocketEvent("PEER_STUBS");
      theEvent.addKeyValue("ADDRESS", InetAddress.getLocalHost().getHostAddress());
      String keys[] = getLocalStubKeys();
      for (int x = 0; x < keys.length; x++) {
        System.out.println("Right Now Sending Key: " + keys[x]);
        theEvent.addKeyValue(keys[x], getLocalStub(keys[x]));
      }
      mcSender.sendMultiCastEvent(theEvent);
    } catch (Exception ex) {
      System.out.println("Exception BrowserManager.sendStubs()->" + ex);
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  private String getContainerName() {
    return "Client";
  }

  /**
   * put your documentation comment here
   * @return
   */
  private String getUID() {
    return "1";
  }

  /**
   * put your documentation comment here
   */
  private void allocNewVitalsHistory() {
    try {
      File dir = new File(FileMgr.getLocalDirectory("container"));
      if (!dir.exists()) {
        dir.mkdir();
      }
      ContainerVitalsHolder holder = new ContainerVitalsHolder(getContainerName(), getUID()
          , nHoursOfHistory);
      saveContainerVitalsHolder(holder);
    } catch (Exception ex) {
      System.err.println("Exception allocNewVitalsHistory()->" + ex);
    }
  }

  /**
   * put your documentation comment here
   */
  private void loadRepository() {
    try {
      File file = new File(FileMgr.getLocalDirectory("repository"));
      if (!file.exists()) {
        System.out.println("Creating Global Repostory...");
        file.mkdir();
        return;
      }
      File list[] = file.listFiles();
      if (list == null) {
        return;
      }
      String fileName = null;
      for (int i = 0; i < list.length; i++) {
        if (!list[i].isDirectory()) {
          fileName = list[i].getName();
          try {
            ObjectStore store = new ObjectStore(FileMgr.getLocalFile("repository", fileName));
            System.out.println("%%% Loading Global Repostory: " + fileName);
            addGlobalObject(fileName, store.read());
          } catch (ObjectStoreException e) {
            String message = "%%% " + getClass().getName()
                + ".loadRepository() caught ObjectStoreException";
            String message2 = fileName + ": " + e.getMessage();
            System.out.println(message);
            System.out.println(message2);
          }
        }
      }
    } catch (Exception ex) {
      System.out.println("Exception loadRepository()->" + ex);
    }
  }

  /**
   * put your documentation comment here
   */
  private void redirectOutput() {
    try {
      FileOutputStream fdOut = new FileOutputStream(AppManager.getLocalFile("../log/client.out"));
      FileOutputStream fdErr = new FileOutputStream(AppManager.getLocalFile("../log/client.err"));
      PrintStream psOut = new PrintStream(new BufferedOutputStream(fdOut, 128), true);
      ErrorPrintStream psErr = new ErrorPrintStream(new BufferedOutputStream(fdErr, 128), true
          , psOut);
      System.setOut(psOut);
      System.setErr(psErr);
      ArrayList list = new ArrayList();
      for (Enumeration enm = System.getProperties().propertyNames(); enm.hasMoreElements(); ) {
        String key = (String)enm.nextElement();
        if (key.endsWith(".version")) {
          list.add(key);
        }
      }
      Collections.sort(list);
      for (int i = 0; i < list.size(); i++) {
        System.out.println("#" + list.get(i) + "=" + System.getProperty((String)list.get(i)));
      }
      System.err.println("#com.retek.platform.version=3.0.1.30");
    } catch (Exception ex) {
      System.out.println("Exception redirectOutput()->" + ex);
    }
  }

  private static BrowserManager instance = null;
  protected final DaemonManager daemonMgr;
  protected final DownTimeManager downTimeManager;
  protected final MessageManager messageMgr;
  protected final PeerManager peerManager;
  protected ClientManage clientManage = null;
  protected MultiCastSender mcSender = null;
  protected RemoteStub clientManageStub = null;
  protected ServerSocketReader socketReader = null;
  protected SocketSender sender = null;
  protected java.awt.Window activeWindow = null;
  private ComponentVitalsBuilder compBuilder = null;
  private ContainerVitalsBuilder contBuilder = null;
  private Hashtable htSocketEventProcessors = null;
  private VitalsThread vitalsThread = null;
  private int nHoursOfHistory = 0;
}

