/*
 * @copyright (c) 1998-2002 Retek Inc
 */

package com.chelseasystems.cr.node;

import java.io.*;
import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

import com.chelseasystems.cr.appmgr.*;
import com.chelseasystems.cr.config.*;
import com.chelseasystems.cr.download.update.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cr.xml.DefaultObjectXML;
import com.igray.io.*;
import com.igray.naming.*;

/**
 * @author John Gray
 */
public class NodeMonitorImpl extends UnicastRemoteObject implements INodeMonitor, FilenameFilter {

	private Activator activator = new Activator();
	private Vector vLog = new Vector(100);
	private IServerStats serverStats = null;
	private int seconds = 60;
	private ContainerInstanceThread instanceThread = null;

	ConfigMgr config;
	private ServerManifestDirectory serverManifestDir;

	/**
	 * @throws Exception
	 */
	public NodeMonitorImpl() throws Exception {
		config = new ConfigMgr("monitor.cfg");
		seconds = config.getInt("CONTAINER_INSTANCE_PAUSE");
		// set uid
		System.setProperty("UID", Long.toString(System.currentTimeMillis()));

		// create manifest directory
		this.serverManifestDir = new ServerManifestDirectory(new File("..", "update"));
		this.serverManifestDir.create();

		// bind
		bind();

		// spawn container instance thread
		if (seconds != 0) {
			LoggingServices.getCurrent().logMsg("Starting Container Instance Monitor Thread...");
			instanceThread = new ContainerInstanceThread(this, seconds);
		}

		// commented out because notification thread is not efficient and CPU intensive.. jrg
		// long log = config.getLong("LOG_PAUSE").longValue();
		// if (log != 0) {
		// System.out.println("Starting Log Notification Thread...");
		// NotificationThread notify = new NotificationThread(this);
		// }
		// long vital = config.getLong("VITAL_PAUSE").longValue();
		// if (vital != 0) {
		// System.out.println("Starting Component Vitals Monitor Thread...");
		// CompVitalThread comp = new CompVitalThread(this);
		// }

		// load server stats
		Object obj = config.getObject("SERVER_STATS");
		if (obj == null) {
			System.out.println("SERVER_STATS class is null");
			try {
				throw new Exception();
			} catch (Exception e) {
				// LoggingServices.getCurrent().logMsg(this.getClass().getName(), "main",
				// "Exception", "See Exception" + " freeMemory:"+String.valueOf(Runtime.getRuntime().freeMemory())
				// +" totalMemory:"+String.valueOf(Runtime.getRuntime().totalMemory())+ " activeCount:"+String.valueOf(Thread.activeCount()),
				// LoggingServices.MAJOR, e);

				e.printStackTrace();
			}
			System.exit(0);
		}
		serverStats = (IServerStats) obj;
		LoggingServices.getCurrent().logMsg("Waiting for Request...");
	}

	public EventLogEntry[] getLogEntries() throws RemoteException {
		return (EventLogEntry[]) vLog.toArray(new EventLogEntry[vLog.size()]);
	}

	void addEventLogEntry(String componentName, int action, int reason) {
		vLog.add(new EventLogEntry(componentName, action, reason));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		NodeMonitorImpl nodeMonitor = null;
		try {
			nodeMonitor = new NodeMonitorImpl();
		} catch (Exception ex) {
			System.err.println("FATAL ERROR: " + ex);
			// LoggingServices.getCurrent().logMsg(nodeMonitor.getClass().getName(), "main",
			// "Exception", "See Exception" + " freeMemory:"+String.valueOf(Runtime.getRuntime().freeMemory())
			// +" totalMemory:"+String.valueOf(Runtime.getRuntime().totalMemory())+ " activeCount:"+String.valueOf(Thread.activeCount()),
			// LoggingServices.MAJOR, ex);
			ex.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * @param name
	 * @return
	 * @exception RemoteException
	 */
	/*
	 * public ComponentVitals[] getVitalsByComponent (String name) throws RemoteException { try { return (ComponentVitals[])htCompVitals.get(name); }
	 * catch (Exception e) { LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getVitalsByComponent", "Exception", "See Exception",
	 * LoggingServices.MAJOR, e); return null; } } public ContainerVitals[] getVitalsByContainer (String name) throws RemoteException { try { return
	 * (ContainerVitals[])htContainerVitals.get(name); } catch (Exception e) { LoggingServices.getCurrent().logMsg(this.getClass().getName(),
	 * "getVitalsByContainer", "Exception", "See Exception", LoggingServices.MAJOR, e); return null; } }
	 */

	/**
	 * @return int
	 * @exception RemoteException
	 */
	public int getContainerInstancePause() throws RemoteException {
		return config.getInt("CONTAINER_INSTANCE_PAUSE");
	}

	/**
	 * @return double
	 * @exception RemoteException
	 */
	public double getFreeCPU() throws RemoteException {
		return serverStats.getFreeCPUPercent();
	}

	/**
	 * @return int
	 * @exception RemoteException
	 */
	public int getLogDirSize() throws RemoteException {
		try {
			int size = 0;
			File dir = new File(AppManager.getLocalFile("..\\log\\"));
			String[] files = dir.list();
			for (int x = 0; x < files.length; x++) {
				File file = new File(AppManager.getLocalFile("..\\log\\") + files[x]);
				size += file.length() / 1000;
			}
			return size;
		} catch (Exception ex) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getLogDirSize", "Unable to get size of log directory", "See Exception", LoggingServices.MAJOR, ex);
			return 0;
		}
	}

	/*
	 * void addComponentVitals (String componentName, ComponentVitals[] vitals) { try { htCompVitals.remove(componentName); if (vitals == null)
	 * return; htCompVitals.put(componentName, vitals); // dtermine if a stats are being collected and process if
	 * (htCompStats.containsKey(componentName)) { File dir = new File(AppManager.getLocalFile("../node")); if (!dir.exists()) dir.mkdir();
	 * CollectStatsInfo info = (CollectStatsInfo)htCompStats.get(componentName); ObjectStore store = new
	 * ObjectStore(AppManager.getLocalFile("../node/" + info.getFileName())); StatsCollector stats = null; // set the date on all stats to be the same
	 * Date now = new Date(); if (store.exists()) { stats = (StatsCollector)store.read(); } else { stats = new StatsCollector(componentName, now,
	 * info.getDuration()); } for (int x = 0; x < vitals.length; x++) { vitals[x].setCreateDate(now); stats.addStats(vitals[x]); } store.write(stats); } }
	 * catch (Exception ex) { LoggingServices.getCurrent().logMsg(this.getClass().getName(), "addComponentVitals", "Unable to accumulate vitals", "See
	 * Exception", LoggingServices.MAJOR, ex); } }
	 */
	/*
	 * void addContainerVitals (String containerName, ContainerVitals[] vitals) { try { htContainerVitals.remove(containerName); if (vitals == null)
	 * return; htContainerVitals.put(containerName, vitals); // dtermine if a stats are being collected and process // if
	 * (htStats.containsKey(componentName)) { // File dir = new File(AppManager.getLocalFile("../node")); // if (!dir.exists()) // dir.mkdir(); //
	 * CollectStatsInfo info = (CollectStatsInfo)htStats.get(componentName); // ObjectStore store = new ObjectStore(AppManager.getLocalFile("../node/" // +
	 * info.getFileName())); // StatsCollector stats = null; // set the date on all stats to be the same // Date now = new Date(); // if
	 * (store.exists()) { // stats = (StatsCollector)store.read(); // } // else { // stats = new StatsCollector(componentName, now,
	 * info.getDuration()); // } // for (int x = 0; x < vitals.length; x++) { // vitals[x].setCreateDate(now); // stats.addStats(vitals[x]); // } //
	 * store.write(stats); // } } catch (Exception ex) { LoggingServices.getCurrent().logMsg(this.getClass().getName(), "addComponentVitals", "Unable
	 * to accumulate vitals", "See Exception", LoggingServices.MAJOR, ex); } } /**
	 */
	void bind() {
		try {
			// bind to monitor rns
			NetworkMgr mgr = new NetworkMgr("network.cfg");
			String appNode = mgr.getRMIAppNode();
			String lookupName = appNode + config.getString("REMOTE_NAME");
			System.out.println("Binding to: " + lookupName);
			NamingService.bind(lookupName, this);
			// Sergio
			System.out.println("Binding OK");
		} catch (Exception ex) {
			System.out.println("Errore RMI");
			System.out.println(ex);
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "bind", "Exception", "See Exception", LoggingServices.MINOR, ex);
		}
	}

	/**
	 * @param componentName
	 * @return int
	 * @exception RemoteException
	 */
	public int getLogLevel(String componentName) throws RemoteException {
		try {
			ICMSComponent comp = (ICMSComponent) NamingService.lookup(getMonitorRNS() + componentName);
			ConfigMgr configMgr = new ConfigMgr(comp.getConfigFile());
			return configMgr.getInt("LOGGING_LEVEL");
		} catch (Exception ex) {
			throw new RemoteException(ex + "");
		}
	}

	/**
	 * @param containerName
	 * @param UID
	 * @return String[]
	 * @exception RemoteException
	 */
	public long getErrorLogFileSize(String containerName, String UID) throws RemoteException {
		return new File(FileUtil.convertFileSeperators("../log/" + containerName + "_" + UID + ".err")).length();
	}

	/**
	 * @param containerName
	 * @param UID
	 * @return
	 * @throws RemoteException
	 */
	public InputStream getErrorLogFileStream(String containerName, String UID) throws RemoteException {
		return this.getLogFileStream(containerName, UID, "err");
	}

	/**
	 * @param containerName
	 * @param UID
	 * @return String[]
	 * @exception RemoteException
	 */
	public long getOutLogFileSize(String containerName, String UID) throws RemoteException {
		return new File(FileUtil.convertFileSeperators("../log/" + containerName + "_" + UID + ".out")).length();
	}

	/**
	 * @param containerName
	 * @param UID
	 * @return
	 * @throws RemoteException
	 */
	public InputStream getOutLogFileStream(String containerName, String UID) throws RemoteException {
		return this.getLogFileStream(containerName, UID, "out");
	}

	/**
	 * @return String[]
	 * @exception RemoteException
	 */
	public String[] getStatsFileNames() throws RemoteException {
		try {
			File dir = new File(AppManager.getLocalFile("../node/"));
			if (!dir.exists())
				return null;
			return dir.list();
		} catch (Exception ex) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getStatsFileNames", "Unable to file names", "See Exception", LoggingServices.MAJOR, ex);
			return null;
		}
	}

	/**
	 * @param componentName
	 * @return boolean
	 * @exception RemoteException
	 */
	public boolean isSystemOut(String componentName) throws RemoteException {
		try {
			ICMSComponent comp = (ICMSComponent) NamingService.lookup(getMonitorRNS() + componentName);
			ConfigMgr configMgr = new ConfigMgr(comp.getConfigFile());
			String out = configMgr.getString("LOGGING_SYSTEM_OUT");
			if (out.equalsIgnoreCase("true"))
				return true;
			else
				return false;
		} catch (Exception ex) {
			throw new RemoteException(ex + "");
		}
	}

	/**
	 * @param componentName
	 * @return boolean
	 * @exception RemoteException
	 */
	public boolean isSystemErr(String componentName) throws RemoteException {
		try {
			ICMSComponent comp = (ICMSComponent) NamingService.lookup(getMonitorRNS() + componentName);
			ConfigMgr configMgr = new ConfigMgr(comp.getConfigFile());
			String err = configMgr.getString("LOGGING_SYSTEM_ERR");
			if (err.equalsIgnoreCase("true"))
				return true;
			else
				return false;
		} catch (Exception ex) {
			throw new RemoteException(ex + "");
		}
	}

	/**
	 * @param componentName
	 * @param out
	 * @exception RemoteException
	 */
	public void setSystemOut(String componentName, boolean out) throws RemoteException {
		try {
			ICMSComponent comp = (ICMSComponent) NamingService.lookup(getMonitorRNS() + componentName);
			INIFile ini = new INIFile(FileMgr.getLocalFile("config", comp.getConfigFile()), false);
			if (out)
				ini.writeEntry("LOGGING_SYSTEM_OUT", "true");
			else
				ini.writeEntry("LOGGING_SYSTEM_OUT", "false");
		} catch (Exception ex) {
			throw new RemoteException(ex.getMessage());
		}
	}

	/**
	 * @param componentName
	 * @param err
	 * @exception RemoteException
	 */
	public void setSystemErr(String componentName, boolean err) throws RemoteException {
		try {
			ICMSComponent comp = (ICMSComponent) NamingService.lookup(getMonitorRNS() + componentName);
			INIFile ini = new INIFile(FileMgr.getLocalFile("config", comp.getConfigFile()), false);
			ini.writeEntry("LOGGING_SYSTEM_ERR", err + "");
		} catch (Exception ex) {
			throw new RemoteException(ex.getMessage());
		}
	}

	/**
	 * @param componentName
	 * @param level
	 * @exception RemoteException
	 */
	public void setLogLevel(String componentName, int level) throws RemoteException {
		try {
			ICMSComponent comp = (ICMSComponent) NamingService.lookup(getMonitorRNS() + componentName);
			INIFile ini = new INIFile(FileMgr.getLocalFile("config", comp.getConfigFile()), false);
			ini.writeEntry("LOGGING_LEVEL", level + "");
		} catch (Exception ex) {
			throw new RemoteException(ex.getMessage());
		}
	}

	/**
	 * @param containerName
	 * @param UID
	 * @return boolean
	 * @throws RemoteException
	 */
	public boolean shutdownContainer(String containerName, String UID) throws RemoteException {
		try {
			Remote[] rmts = NamingService.getRemoteListByName(getMonitorRNS() + containerName);
			if (rmts == null)
				return false;
			for (int x = 0; x < rmts.length; x++) {
				ICMSContainer container = (ICMSContainer) rmts[x];
				if (container.getUID().equals(UID)) {
					performAction(ContainerTuning.SHUTDOWN, container, null, EventLogEntry.REASON_USER_REQUESTED);
					return true;
				}
			}
			return false;
		} catch (Exception ex) {
			throw new RemoteException(ex.getMessage());
		}
	}

	/**
	 * @param containerName
	 * @return boolean
	 * @throws RemoteException
	 */
	public boolean startContainer(String containerName) throws RemoteException {
		try {
			LoggingServices.getCurrent().logMsg("Starting Container: " + containerName);
			ContainerTuning container = this.getContainerTuning(containerName);
			if (container == null) {
				LoggingServices.getCurrent().logMsg("Container is null");
				return false;
			}
			performAction(ContainerTuning.START, null, containerName, EventLogEntry.REASON_USER_REQUESTED);
			return true;
		} catch (Exception ex) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "startComponent", "Exception", "See Exception", LoggingServices.MAJOR, ex);
			return false;
		}
	}

	/**
	 * @param containerName
	 * @return ICMSContainer[]
	 * @throws RemoteException
	 */
	public ICMSContainer[] getContainers(String containerName) throws RemoteException {
		try {
			Remote[] rmt = NamingService.getRemoteListByName(getMonitorRNS() + containerName);
			if (rmt == null) {
				ICMSContainer[] result = new ICMSContainer[0];
				return result;
			}
			ICMSContainer[] result = new ICMSContainer[rmt.length];
			for (int x = 0; x < rmt.length; x++)
				result[x] = (ICMSContainer) rmt[x];
			return result;
		} catch (NotBoundException ex) {
			ICMSContainer[] result = new ICMSContainer[0];
			return result;
		}
	}

	/**
	 * @return ICMSContainer[]
	 * @exception RemoteException
	 */
	public ICMSContainer[] getAllContainers() throws RemoteException {
		try {
			Vector vConts = new Vector();
			String[] contNames = this.getAllContainerNames();
			for (int x = 0; x < contNames.length; x++) {
				Remote[] rmt = null;
				try {
					rmt = NamingService.getRemoteListByName(getMonitorRNS() + contNames[x]);
				} catch (NotBoundException ex) {
					rmt = null;
				}
				if (rmt != null) {
					for (int y = 0; y < rmt.length; y++)
						vConts.add((ICMSContainer) rmt[y]);
				}
			}
			ICMSContainer[] result = new ICMSContainer[vConts.size()];
			vConts.copyInto(result);
			return result;
		} catch (Exception ex) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getAllContainers", "Exception", "See Exception", LoggingServices.MAJOR, ex);
			ICMSContainer[] result = new ICMSContainer[0];
			return result;
		}
	}

	/**
	 * @param Name
	 * @param nLastRow
	 * @return String[]
	 * @exception RemoteException
	 */
	public String[] getLogFile(String Name, int nLastRow) throws RemoteException {
		return this.getLogFile(Name, nLastRow, LoggingServices.DEFAULT);
	}

	/**
	 * @param Name
	 * @param nLastRow
	 * @param severityLevel
	 * @return String[]
	 * @exception RemoteException
	 */
	public String[] getLogFile(String Name, int nLastRow, int severityLevel) throws RemoteException {
		RandomAccessFile rFile = null;
		Vector vFile = new Vector();
		String sResult[] = null;
		try {
			ICMSComponent comp = (ICMSComponent) NamingService.lookup(getMonitorRNS() + Name);
			File file = new File(FileUtil.convertFileSeperators(comp.getLogFileName()));
			if (file.exists()) {
				rFile = new RandomAccessFile(file, "r");
				rFile.seek(0);
				String sLine = null;
				while((sLine = rFile.readLine()) != null) {
					switch (severityLevel) {
						default:
						case LoggingServices.INFO:
							if (sLine.indexOf(LoggingInfo.DELIMITER + LoggingInfo.INFO + LoggingInfo.DELIMITER) > -1)
								break;
						case LoggingServices.MINOR:
							if (sLine.indexOf(LoggingInfo.DELIMITER + LoggingInfo.MINOR + LoggingInfo.DELIMITER) > -1)
								break;
						case LoggingServices.MAJOR:
							if (sLine.indexOf(LoggingInfo.DELIMITER + LoggingInfo.MAJOR + LoggingInfo.DELIMITER) > -1)
								break;
						case LoggingServices.CRITICAL:
							if (sLine.indexOf(LoggingInfo.DELIMITER + LoggingInfo.CRITICAL + LoggingInfo.DELIMITER) > -1)
								break;
							continue; // was not able to find severity level, continue next readln
					}
					vFile.addElement(sLine);
				}
				rFile.close();
				// get size of array, either max rows or nLastRow,
				// which ever is greater
				int nRows = (nLastRow > vFile.size()) ? vFile.size() : nLastRow;
				sResult = new String[nRows];
				int x = 0; // counter with vector
				int nRowCount = 0;
				boolean bProcess;
				for (Enumeration enm = vFile.elements(); enm.hasMoreElements(); x++) {
					String sRow = (String) enm.nextElement();
					bProcess = false;
					if (nLastRow >= vFile.size())
						bProcess = true;
					else if (x >= (vFile.size() - nLastRow))
						bProcess = true;
					if (bProcess) {
						sResult[nRowCount] = sRow;
						nRowCount++; // number of row added to array
					}
				}
			}
		} catch (IOException e) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getLogFile", "Exception", "See Exception", LoggingServices.MAJOR, e);
		} catch (Exception ex) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getLogFile", "Exception", "See Exception", LoggingServices.MAJOR, ex);
		}
		return sResult;
	}

	/**
	 * @param containerName
	 * @return ContainerTuning
	 * @throws RemoteException
	 */
	public ContainerTuning getContainerTuning(String containerName) throws RemoteException {
		try {
			ContainerTuning container = null;
			List list = new DefaultObjectXML().toObjects(FileMgr.getLocalFile("tuning", containerName + ".xml"));
			if (list.size() > 0) {
				container = (ContainerTuning) list.get(0);
			}
			if (container == null) {
				container = new ContainerTuning(containerName);
			}
			return container;
		} catch (Exception ex) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getTuningContainer", "Exception", "See Exception", LoggingServices.MAJOR, ex);
			return null;
		}
	}

	/**
	 * @param theContainer
	 * @return boolean
	 * @exception RemoteException
	 */
	public boolean updateContainerTuning(ContainerTuning theContainer) throws RemoteException {
		try {
			File file = new File(FileMgr.getLocalDirectory("tuning"));
			if (!file.exists()) {
				file.mkdir();
			}
			FileWriter writer = new FileWriter(FileMgr.getLocalFile("tuning", theContainer.getContainerName() + ".xml"));
			writer.write(new DefaultObjectXML().toXML(theContainer));
			writer.close();
			return true;
		} catch (Exception ex) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "updateTuningContainer", "Exception", "See Exception", LoggingServices.MAJOR, ex);
			return false;
		}
	}

	/**
	 * @return boolean
	 * @exception RemoteException
	 */
	public boolean isTraining() throws RemoteException {
		String train = System.getProperty("TRAINING");
		if (train == null)
			return false;
		Boolean bTrain = new Boolean(train);
		return bTrain.booleanValue();
	}

	/**
	 * @param theContainer
	 * @return boolean
	 * @exception RemoteException
	 */
	public boolean removeContainerTuning(ContainerTuning theContainer) throws RemoteException {
		return removeContainerTuning(theContainer.getContainerName());
	}

	/**
	 * @param containerName
	 * @return boolean
	 * @throws RemoteException
	 */
	public boolean removeContainerTuning(String containerName) throws RemoteException {
		try {
			File file = new File(FileMgr.getLocalFile("tuning", containerName + ".xml"));
			if (!file.exists()) {
				return false;
			}
			if (!file.canWrite()) {
				return false;
			}
			return file.delete();
		} catch (Exception ex) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "removeTuningContainer", "Exception", "See Exception", LoggingServices.MAJOR, ex);
			return false;
		}
	}

	/**
	 * @param action
	 * @param container
	 * @param theContainer
	 */
	public void performAction(int action, ICMSContainer container, String theContainer, int reason) {
		try {
			LoggingServices.getCurrent().logMsg("Action being Performed..." + action);
			if (action == ContainerTuning.START) {
				this.addEventLogEntry(theContainer, EventLogEntry.ACTION_ADD, reason);
				ContainerTuning tuning = this.getContainerTuning(theContainer);
				activator.launch(tuning);
			}
			if (action == ContainerTuning.SHUTDOWN) {
				addEventLogEntry(theContainer, EventLogEntry.ACTION_REMOVE, reason);
				container.requestContainerShutdown();
			}
		} catch (Exception ex) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "performAction", "Exception", "See Exception", LoggingServices.MAJOR, ex);
		}
	}

	/**
	 * @return boolean
	 * @exception RemoteException
	 */
	public boolean ping() throws RemoteException {
		return true;
	}

	// //////////////////////
	// Manifest Manipulation
	// //////////////////////
	public String[] getManifestNames() throws RemoteException {
		try {
			return this.serverManifestDir.getManifestNames();
		} catch (Exception ex) {
			throw new RemoteException("Could not get manifest names.", ex);
		}
	}

	/**
	 * @return String
	 * @exception RemoteException
	 */
	public String getCurrentInstalledManifestName() throws RemoteException {
		try {
			ServerManifest currentInstalledManifest = this.serverManifestDir.getCurrentInstalledManifest();
			if (currentInstalledManifest == null) {
				return "UNKNOWN";
			} else {
				return currentInstalledManifest.getName();
			}
		} catch (Exception ex) {
			throw new RemoteException("Could not get current installed manifest name.", ex);
		}
	}

	/**
	 * @param name
	 * @return byte[]
	 * @exception RemoteException
	 */
	public byte[] getManifestAsZipFileBytes(String name) throws RemoteException {
		try {
			ServerManifest manifest = new ServerManifest(this.serverManifestDir, name);
			return manifest.getAsZipFileBytes();
		} catch (Exception ex) {
			throw new RemoteException("Could not get manifest as a Zip file byte array.", ex);
		}
	}

	/**
	 * @param manifestName
	 * @param zipFileContents
	 * @exception RemoteException
	 */
	public void createManifest(String manifestName, byte[] zipFileContents) throws RemoteException {
		try {
			ServerManifest manifest = new ServerManifest(this.serverManifestDir, manifestName);
			manifest.createFromZipBytes(zipFileContents);
		} catch (Exception ex) {
			throw new RemoteException("Could not create manifest from Zip file byte array.", ex);
		}
	}

	/**
	 * @param manifestName
	 * @exception RemoteException
	 */
	public void createEmptyManifest(String manifestName) throws RemoteException {
		try {
			ServerManifest manifest = new ServerManifest(this.serverManifestDir, manifestName);
			manifest.create();
		} catch (Exception ex) {
			throw new RemoteException("Could not create manifest.", ex);
		}
	}

	/**
	 * @param manifestName
	 * @exception RemoteException
	 */
	public void deleteManifest(String manifestName) throws RemoteException {
		try {
			ServerManifest manifest = new ServerManifest(this.serverManifestDir, manifestName);
			manifest.delete();
		} catch (Exception ex) {
			throw new RemoteException("Could not delete manifest.", ex);
		}
	}

	/**
	 * @param manifestName
	 * @return boolean
	 * @exception RemoteException
	 */
	public boolean doesManifestExist(String manifestName) throws RemoteException {
		try {
			ServerManifest manifest = new ServerManifest(this.serverManifestDir, manifestName);
			return manifest.exists();
		} catch (Exception ex) {
			throw new RemoteException("Could not determine whether manifest exitsts.", ex);
		}
	}

	/**
	 * @param manifestName
	 * @return Date
	 * @exception RemoteException
	 */
	public Date getManifestActivationDate(String manifestName) throws RemoteException {
		try {
			ServerManifest manifest = new ServerManifest(this.serverManifestDir, manifestName);
			return manifest.getActivationDate();
		} catch (Exception ex) {
			throw new RemoteException("Could not get manifest activation date.", ex);
		}
	}

	/**
	 * @param manifestName
	 * @param date
	 * @exception RemoteException
	 */
	public void setManifestActivationDate(String manifestName, Date date) throws RemoteException {
		try {
			ServerManifest manifest = new ServerManifest(this.serverManifestDir, manifestName);
			manifest.setActivationDate(date);
		} catch (Exception ex) {
			throw new RemoteException("Could not set manifest activation date.", ex);
		}
	}

	/**
	 * @param manifestName
	 * @return String
	 * @exception RemoteException
	 */
	public String getManifestStatus(String manifestName) throws RemoteException {
		try {
			ServerManifest manifest = new ServerManifest(this.serverManifestDir, manifestName);
			return manifest.getStatus();
		} catch (Exception ex) {
			throw new RemoteException("Could not get manifest status.", ex);
		}
	}

	/**
	 * @param manifestName
	 * @param status
	 * @exception RemoteException
	 */
	public void setManifestStatus(String manifestName, String status) throws RemoteException {
		try {
			ServerManifest manifest = new ServerManifest(this.serverManifestDir, manifestName);
			manifest.setStatus(status);
		} catch (Exception ex) {
			throw new RemoteException("Could not set manifest status.", ex);
		}
	}

	/**
	 * @param manifestName
	 * @return String[]
	 * @exception RemoteException
	 */
	public String[] getManifestFileNames(String manifestName) throws RemoteException {
		try {
			ServerManifest manifest = new ServerManifest(this.serverManifestDir, manifestName);
			return manifest.getFileNames();
		} catch (Exception ex) {
			throw new RemoteException("Could not get manifest file names.", ex);
		}
	}

	/**
	 * @param manifestName
	 * @return FileEntry[]
	 * @exception RemoteException
	 */
	public FileEntry[] getManifestFileEntries(String manifestName) throws RemoteException {
		try {
			ServerManifest manifest = new ServerManifest(this.serverManifestDir, manifestName);
			return manifest.getFileEntries();
		} catch (Exception ex) {
			throw new RemoteException("Could not get manifest file entries.", ex);
		}
	}

	/**
	 * @param manifestName
	 * @param fileName
	 * @return FileEntry
	 * @exception RemoteException
	 */
	public FileEntry getManifestFileEntry(String manifestName, String fileName) throws RemoteException {
		try {
			ServerManifest manifest = new ServerManifest(this.serverManifestDir, manifestName);
			return manifest.getFileEntry(fileName);
		} catch (Exception ex) {
			throw new RemoteException("Could not get manifest file entry.", ex);
		}
	}

	/**
	 * @param manifestName
	 * @param entry
	 * @param file
	 * @param destinationDir
	 * @param copyToTraining
	 * @exception RemoteException
	 */
	public void addFileToManifest(String manifestName, FileEntry entry, byte[] file, String destinationDir, boolean copyToTraining) throws RemoteException {
		try {
			ServerManifest manifest = new ServerManifest(this.serverManifestDir, manifestName);
			manifest.addFile(entry, file, destinationDir, copyToTraining);
		} catch (Exception ex) {
			throw new RemoteException("Could not get add file to manifest.", ex);
		}
	}

	/**
	 * @param manifestName
	 * @param fileName
	 * @exception RemoteException
	 */
	public void removeFileFromManifest(String manifestName, String fileName) throws RemoteException {
		try {
			ServerManifest manifest = new ServerManifest(this.serverManifestDir, manifestName);
			manifest.removeFile(fileName);
		} catch (Exception ex) {
			throw new RemoteException("Could not get remove file from manifest.", ex);
		}
	}

	/**
	 * @param manifestName
	 * @param fileName
	 * @return byte[]
	 * @exception RemoteException
	 */
	public byte[] getManifestFileContents(String manifestName, String fileName) throws RemoteException {
		try {
			ServerManifest manifest = new ServerManifest(this.serverManifestDir, manifestName);
			return manifest.getFileContents(fileName);
		} catch (Exception ex) {
			throw new RemoteException("Could not get file contents from manifest.", ex);
		}
	}

	/**
	 * @param manifestName
	 * @param fileName
	 * @return boolean
	 * @exception RemoteException
	 */
	public boolean isManifestFileComplete(String manifestName, String fileName) throws RemoteException {
		try {
			ServerManifest manifest = new ServerManifest(this.serverManifestDir, manifestName);
			return manifest.isFileComplete(fileName);
		} catch (Exception ex) {
			throw new RemoteException("Could not determine if file is complete in manifest.", ex);
		}
	}

	/**
	 * @param manifestName
	 * @return boolean
	 * @exception RemoteException
	 */
	public boolean isManifestComplete(String manifestName) throws RemoteException {
		try {
			ServerManifest manifest = new ServerManifest(this.serverManifestDir, manifestName);
			return manifest.isComplete();
		} catch (Exception ex) {
			throw new RemoteException("Could not determine if manifest is complete.", ex);
		}
	}

	// ////////////////////
	// Deprecated Methods
	// ////////////////////
	/**
	 * Gets the name of the manifest that this node says should currently be installed.
	 * 
	 * @return <code>null</code>
	 * @throws RemoteException
	 * @deprecated Use {@link #getCurrentInstalledManifestName getCurrentInstalledManifestName} instead. This method will be deleted in a subsequent
	 *             release.
	 */
	public String getCurrentInstalledManifest() throws RemoteException {
		return this.getCurrentInstalledManifestName();
	}

	/**
	 * @param manifestName
	 * @return <code>null</code>
	 * @throws RemoteException
	 * @deprecated Current implementation returns null. Method will be deleted in a subsequent release.
	 */
	public Manifest getManifest(String manifestName) throws RemoteException {
		return null;
	}

	/**
	 * Returns the activation date for the provided manifest.
	 * 
	 * @param manifestName
	 * @return The activation date.
	 * @throws RemoteException
	 *             If there are any problems encountered while retrieving the date.
	 * @deprecated Use {@link #getManifestActivationDate getManifestActivationDate} instead. This method will be deleted in a subsequent release.
	 */
	public Date getActivitionDate(String manifestName) throws RemoteException {
		return this.getManifestActivationDate(manifestName);
	}

	/**
	 * Returns the status for the provided manifest as one of the UpdateServices constants..
	 * 
	 * @param manifestName
	 * @return The status as an int.
	 * @throws RemoteException
	 *             If there are any problems encountered while retrieving the status.
	 * @see UpdateServices
	 * @deprecated Use {@link #getManifestStatus getManifestStatus} instead. This method will be deleted in a subsequent release.
	 */
	public int getStatus(String manifestName) throws RemoteException {
		String statusString = this.getManifestStatus(manifestName);
		if (ServerManifest.INSTALL.equals(statusString)) {
			return UpdateServices.INSTALL;
		} else if (ServerManifest.WAIT.equals(statusString)) {
			return UpdateServices.WAIT;
		} else if (ServerManifest.REMOVE.equals(statusString)) {
			return UpdateServices.REMOVE;
		} else {
			return UpdateServices.UNKNOWN;
		}
	}

	/**
	 * @param manifestName
	 * @param files
	 * @return <code>false</code>
	 * @throws RemoteException
	 * @deprecated Method is not used and does nothing. It will be deleted in a subsequent release. Use {@link #createManifest createManifest} or
	 *             {@link #createEmptyManifest createEmptyManifest} instead.
	 */
	public boolean addManifest(String manifestName, FileEntry[] files) throws RemoteException {
		return false;
	}

	/**
	 * Sets the given manifest's status by mapping the provided int from the constants in the UpdateServices class to the constants in the
	 * ServerManifest class.
	 * 
	 * @param status
	 * @param manifestName
	 * @return <code>true</code>
	 * @throws RemoteException
	 *             If there is a problem setting the status or the status is invalid.
	 * @deprecated Use {@link #setManifestStatus setManifestStatus} instead. This method will be deleted in a subsequent release.
	 */
	public boolean setStatus(int status, String manifestName) throws RemoteException {
		switch (status) {
			case UpdateServices.INSTALL:
				this.setManifestStatus(manifestName, ServerManifest.INSTALL);
				break;
			case UpdateServices.WAIT:
				this.setManifestStatus(manifestName, ServerManifest.WAIT);
				break;
			case UpdateServices.REMOVE:
				this.setManifestStatus(manifestName, ServerManifest.REMOVE);
				break;
			default:
				Exception srcEx = new IllegalArgumentException("Status " + status + " is not valid for a manifest.");
				throw new RemoteException("Could not set status.", srcEx);
		}
		return true;
	}

	/**
	 * Sets the activation date for the specified manifest.
	 * 
	 * @param date
	 *            The new activation date.
	 * @param manifestName
	 *            The manifest to update.
	 * @return <code>true</code>
	 * @throws RemoteException
	 *             If there is a problem setting the activation date.
	 * @deprecated Use {@link #getManifestActivationDate getManifestActivationDate} instead. This method will be deleted in a subsequent release.
	 */
	public boolean setActivitionDate(Date date, String manifestName) throws RemoteException {
		this.setManifestActivationDate(manifestName, date);
		return true;
	}

	/**
	 * @param manifestName
	 * @param entry
	 * @param file
	 * @return <code>true</code>
	 * @throws RemoteException
	 * @deprecated Use {@link #addFileToManifest addFileToManifest} instead. This method has no effect and will be deleted in a subsequent release.
	 */
	public boolean addFileEntry(String manifestName, FileEntry entry, byte[] file) throws RemoteException {
		return true;
	}

	/**
	 * @return String[]
	 * @exception RemoteException
	 */
	public String[] getAllContainerNames() throws RemoteException {
		try {
			Vector vContainers = new Vector();
			File file = new File(FileMgr.getLocalDirectory("tuning"));
			if (file.exists()) {
				String[] files = file.list(this);
				if (files != null) {
					DefaultObjectXML xml = new DefaultObjectXML();
					for (int x = 0; x < files.length; x++) {
						List list = xml.toObjects(FileMgr.getLocalFile("tuning", files[x]));
						if (list.size() > 0) {
							ContainerTuning ct = (ContainerTuning) list.get(0);
							vContainers.addElement(ct.getContainerName());
						}
					}
				}
				// sort components
				sortComponents(vContainers);
			}
			return (String[]) vContainers.toArray(new String[vContainers.size()]);
		} catch (Exception e) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getContainers", "Exception", "See Exception", LoggingServices.MAJOR, e);
			return null;
		}
	}

	/**
	 * @param theDir
	 * @param filename
	 * @return boolean
	 */
	public boolean accept(File theDir, String filename) {
		return filename.endsWith(".xml");
	}

	/**
	 * @param Name
	 * @return
	 * @exception RemoteException
	 */
	// public int getInstanceCount (String Name) throws RemoteException {
	// try {
	// Remote[] rmts = NamingService.getRemoteListByName(getMonitorRNS() +
	// Name);
	// if (rmts == null)
	// return 0;
	// return rmts.length;
	// } catch (NotBoundException nb) {
	// return 0;
	// }
	// }
	/**
	 * @param email
	 * @exception RemoteException
	 */
	// public void updateLogNotification (LogNotify email) throws RemoteException {
	// email.update();
	// }
	/**
	 * @param email
	 * @exception RemoteException
	 */
	// public void removeLogNotification (LogNotify email) throws RemoteException {
	// email.remove();
	// }
	/**
	 * @return
	 * @exception RemoteException
	 */
	// public LogNotify[] getLogNotifications () throws RemoteException {
	// Vector vMail = new Vector();
	// try {
	// INIFile ini = new INIFile(FileMgr.getLocalFile("config", "monitor.cfg"),
	// false);
	// for (Enumeration enm = ini.getKeys(); enm.hasMoreElements();) {
	// String key = (String)enm.nextElement();
	// if (key.startsWith("LOG_NOTIFY.")) {
	// LogNotify email = new LogNotify();
	// email.EMailAddress = key.substring("LOG_NOTIFY.".length());
	// email.LogLevel = ini.getValue(key);
	// if (!email.LogLevel.equals("DELETE"))
	// vMail.addElement(email);
	// }
	// }
	// LogNotify[] emails = new LogNotify[vMail.size()];
	// vMail.copyInto(emails);
	// return emails;
	// } catch (Exception ex) {
	// LoggingServices.getCurrent().logMsg(this.getClass().getName(), "getLogNotifications",
	// "Exception", "See Exception", LoggingServices.MAJOR, ex);
	// return null;
	// }
	// }
	/**
	 * @param vComps
	 */
	public void sortComponents(Vector vComps) {
		boolean unsorted = false;
		do {
			// Assume they are in order.
			unsorted = false;
			// Check the sequence of adjacent elements.
			for (int i = 0; i < vComps.size() - 1; i++) {
				String thisComp = (String) vComps.elementAt(i);
				String nextComp = (String) vComps.elementAt(i + 1);
				if (thisComp.compareTo(nextComp) > 0) {
					// Not in order, so swap elements.
					String spare = (String) vComps.elementAt(i);
					vComps.setElementAt(vComps.elementAt(i + 1), i);
					vComps.setElementAt(spare, i + 1);
					// Signal they are unsorted, go again.
					unsorted = true;
				}
			}
		} while(unsorted); // end of unsorted ins loop
	}

	/**
	 * client must implement for configuration events
	 * 
	 * @param aKey
	 */
	public void configEvent(String[] aKey) {
	}

	/**
	 * shuts down all services including montor step 1: stop monitoring instance count step 2: request all services to shutdown step 3: when all
	 * services are down, shutdown self
	 * 
	 * @throws RemoteException
	 */
	public void shutdownNode() throws RemoteException {
		try {
			// stop monitoring
			if (instanceThread != null)
				instanceThread.suspend();
			String[] names = this.getAllContainerNames();
			for (int x = 0; x < names.length; x++) {
				ICMSContainer[] containers = (ICMSContainer[]) NamingService.getRemoteListByName(getMonitorRNS() + names[x]);
				for (int y = 0; y < containers.length; y++) {
					containers[y].requestContainerShutdown();
				}
			}
			try {
				throw new Exception();
			} catch (Exception e) {
				// LoggingServices.getCurrent().logMsg(this.getClass().getName(), "main",
				// "Exception", "See Exception" + " freeMemory:"+String.valueOf(Runtime.getRuntime().freeMemory())
				// +" totalMemory:"+String.valueOf(Runtime.getRuntime().totalMemory())+ " activeCount:"+String.valueOf(Thread.activeCount()),
				// LoggingServices.MAJOR, e);
				e.printStackTrace();
			}
			System.exit(0);
		} catch (Exception ex) {
			LoggingServices.getCurrent().logMsg(this.getClass().getName(), "shutdownNode", "Exception", "See Exception", LoggingServices.MAJOR, ex);
		}
	}

	/**
	 * @return String
	 */
	public static String getMonitorRNS() {
		NetworkMgr mgr = new NetworkMgr("network.cfg");
		return mgr.getRMIAppNode();
	}

	// /////////////////////////////////////////////////////////////////
	//
	// Private Methods
	//
	// /////////////////////////////////////////////////////////////////

	private String[] getLogFile(String containerName, String UID, String extension) throws RemoteException {
		try {
			Remote[] rmts = NamingService.getRemoteListByName(getMonitorRNS() + containerName);
			if (rmts == null)
				return null;
			for (int x = 0; x < rmts.length; x++) {
				ICMSContainer cont = (ICMSContainer) rmts[x];
				if (cont.getUID().equals(UID)) {
					return FileUtil.readLogFile(new File(FileUtil.convertFileSeperators("../log/" + containerName + "_" + UID + "." + extension)), null, 0);
				}
			}
			return null;
		} catch (Exception ex) {
			throw new RemoteException(ex.getMessage());
		}
	}

	private InputStream getLogFileStream(String containerName, String UID, String extension) throws RemoteException {
		try {
			Remote[] rmts = NamingService.getRemoteListByName(getMonitorRNS() + containerName);
			if (rmts == null)
				return null;
			for (int x = 0; x < rmts.length; x++) {
				ICMSContainer cont = (ICMSContainer) rmts[x];
				if (cont.getUID().equals(UID)) {
					FileInputStream fis = new FileInputStream(FileUtil.convertFileSeperators("../log/" + containerName + "_" + UID + "." + extension));
					return new InputStreamAdapter(new RemoteInputStreamServer(fis));
				}
			}
			return null;
		} catch (Exception ex) {
			throw new RemoteException(ex.getMessage());
		}
	}

}
