/*
 * This unpublished work is protected by trade secret, copyright and other laws.
 * In the event of publication, the following notice shall apply:
 * Copyright &copy; 2005 Retek Inc.  All Rights Reserved.
 */
package com.chelseasystems.cr.node;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintStream;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.chelseasystems.cr.appmgr.AppManager;
import com.chelseasystems.cr.config.FileMgr;
import com.chelseasystems.cr.config.NetworkMgr;
import com.chelseasystems.cr.xml.DefaultObjectXML;
import com.chelseasystems.cr.logging.*;
//import com.chelseasystems.cs.ajbauthorization.AJBPingThread;

import com.igray.naming.NamingService;

public class CMSContainer extends UnicastRemoteObject implements ICMSContainer,
		Remote {

	// ~ Static fields/initializers
	// -------------------------------------------------------------------

	public static final String UID = "UID";
	public static final String DEVICE_ID = "DEVICE_ID";
	public static final String DEVICE_TYPE = "DEVICE_TYPE";
	public static final String CONTAINER_FILE = "CONTAINER_FILE";

	// ~ Instance fields
	// ------------------------------------------------------------------------------

	private CompInstanceThread instanceThread;
	private ContainerTuning tuning;
	private ContainerVitalsBuilder contBuilder;
	private FileOutputStream errorFileOutputStream;
	private Hashtable htComps;
	private String containerName;
	private Vector vLog;
	private VitalsThread vitalsThread;
	private boolean isShutdownInProgress;
	//Vivek Mishra : Commented in order to move AJBPingThread at client side from server side
	// Vivek Mishra : This is the reference for AJBPingThread used for starting
	// the thread.
	//private static AJBPingThread ajbPingThread;

	// ~ Constructors
	// ---------------------------------------------------------------------------------

	public CMSContainer(String containerFile) throws RemoteException {
		htComps = new Hashtable();
		vLog = new Vector();
		isShutdownInProgress = false;
		instanceThread = null;
		vitalsThread = null;
		tuning = null;
		contBuilder = null;
		try {
			List list = (new DefaultObjectXML()).toObjects(FileMgr
					.getLocalFile("tuning",
							String.valueOf(String.valueOf(containerFile))
									.concat(".xml")));
			tuning = (ContainerTuning) list.get(0);
		} catch (Exception ex) {
			System.out.println("Exception ".concat(String.valueOf(String
					.valueOf(ex.getMessage()))));
			ex.printStackTrace();
			LoggingServices.getCurrent()
					.logMsg(this.getClass().getName(),
							"main",
							"Exception",
							"See Exception"
									+ " freeMemory:"
									+ String.valueOf(Runtime.getRuntime()
											.freeMemory())
									+ " totalMemory:"
									+ String.valueOf(Runtime.getRuntime()
											.totalMemory()) + " activeCount:"
									+ String.valueOf(Thread.activeCount()),
							LoggingServices.MAJOR, ex);
			System.exit(-1);
		}
		containerName = tuning.getContainerName();
		bind();
		try {
			String processClass = tuning.getProcessStats();
			System.out.println("Process system class=" + processClass);
			if (processClass != null) {
				Class cls = Class.forName(processClass);
				Constructor con = cls
						.getConstructor(new Class[] { java.lang.String.class });
				String uid = System.getProperty("UID");
				if (uid == null) {
					System.out.println("UID property is not set.");
				} else {
					IProcessStats processStats = (IProcessStats) con
							.newInstance(new Object[] { uid });
					System.getProperties().setProperty("UID",
							Integer.toString(processStats.getPID()));
				}
			}
			// Sertgio disabilita
			redirectOutput(containerFile);

			String[] bootstrapClasses = tuning.getBootStraps();
			for (int i = 0; i < bootstrapClasses.length; i++) {
				System.out.println("Executing bootstrap class: ".concat(String
						.valueOf(String.valueOf(bootstrapClasses[i]))));
				Class cls = Class.forName(bootstrapClasses[i]);
				Object obj = cls.newInstance();
				CMSBootStrap strap = (CMSBootStrap) obj;
				strap.execute(this);
			}

			String[] daemonClasses = tuning.getDaemons();
			for (int i = 0; i < daemonClasses.length; i++) {
				System.out.println("Executing daemon class: ".concat(String
						.valueOf(String.valueOf(daemonClasses[i]))));
				Class cls = Class.forName(daemonClasses[i]);
				Constructor con = cls
						.getConstructor(new Class[] { getClass() });
				CMSDaemon daemon = (CMSDaemon) con
						.newInstance(new CMSContainer[] { this });
				daemon.start();
			}

			initMessaging();
			contBuilder = new ContainerVitalsBuilder(containerName, getUID(),
					String.valueOf(String.valueOf((new StringBuffer(String
							.valueOf(String.valueOf(containerFile))))
							.append("_").append(getUID()).append(".err"))));
			System.out.println("Allocating new container history...");
			allocNewVitalsHistory();
			System.out.println("Starting component instance Thread...");
			instanceThread = new CompInstanceThread(this,
					tuning.getComponentInstancePause());
			instanceThread.start();
			System.out.println("Starting vitals thread...");
			vitalsThread = new VitalsThread(this,
					tuning.getMinutesPauseVitals(), tuning.getHoursOfHistory());
			vitalsThread.start();
			//Vivek Mishra : Commented in order to move AJBPingThread at client side from server side
			// Vivek Mishra : Added code to start the AJBPingThread with server startup
			/*ajbPingThread = new AJBPingThread();
			Thread ping = new Thread(ajbPingThread);
			ping.start();*/
			// End

			System.out.println("Waiting on request...");
		} catch (Exception ex) {
			System.out.println("Exception processStats()->".concat(String
					.valueOf(String.valueOf(ex))));
		}
	}

	// ~ Methods
	// --------------------------------------------------------------------------------------

	public ICMSComponent getCMSComponent(String componentName, String uid)
			throws RemoteException {
		ICMSComponent[] comps = getCMSComponents(componentName);
		if (comps == null) {
			return null;
		}
		for (int x = 0; x < comps.length; x++) {
			if (comps[x].getUID().equals(uid)) {
				return comps[x];
			}
		}

		return null;
	}

	public ICMSComponent[] getCMSComponents(String componentName)
			throws RemoteException {
		Vector vComp = (Vector) htComps.get(componentName);
		if (vComp == null) {
			return new ICMSComponent[0];
		} else {
			ICMSComponent[] comps = new ICMSComponent[vComp.size()];
			vComp.copyInto(comps);
			return comps;
		}
	}

	public ComponentVitals[] getComponentVitals(String componentName)
			throws RemoteException {
		ICMSComponent[] comps = getCMSComponents(componentName);
		if (comps == null) {
			return null;
		}
		ComponentVitals[] vitals = new ComponentVitals[comps.length];
		for (int x = 0; x < vitals.length; x++) {
			vitals[x] = comps[x].getVitals();
		}

		return vitals;
	}

	public String[] getComponents() throws RemoteException {
		Vector result = new Vector();
		String compName;
		for (Enumeration enumKey = htComps.keys(); enumKey.hasMoreElements(); result
				.add(compName)) {
			compName = (String) enumKey.nextElement();
		}

		String[] comps = new String[result.size()];
		result.copyInto(comps);
		System.out.println("getComponents().length->".concat(String
				.valueOf(String.valueOf(comps.length))));
		return comps;
	}

	public String getContainerName() throws RemoteException {
		return containerName;
	}

	public ContainerVitalsHolder getContainerVitalsHolder()
			throws RemoteException {
		ContainerVitalsHolder holder = null;
		try {
			List list = (new DefaultObjectXML()).getObjectsFromFile(FileMgr
					.getLocalFile("container", String.valueOf(String
							.valueOf((new StringBuffer(String.valueOf(String
									.valueOf(getContainerName())))).append("_")
									.append(getUID()).append(".xml")))));
			if (list.size() > 0) {
				holder = (ContainerVitalsHolder) list.get(0);
			}
			ContainerVitalsHolder containervitalsholder = holder;
			return containervitalsholder;
		} catch (Exception ex) {
			System.out.println("Exception getContainerVitalsHolder()->"
					.concat(String.valueOf(String.valueOf(ex))));
		}
		return holder;
	}

	public int getInstanceCount(String componentName) throws RemoteException {
		Vector vComp = (Vector) htComps.get(componentName);
		if (vComp == null) {
			return 0;
		} else {
			return vComp.size();
		}
	}

	public EventLogEntry[] getLogEntries() throws RemoteException {
		EventLogEntry[] entries = new EventLogEntry[vLog.size()];
		vLog.copyInto(entries);
		return entries;
	}

	public int getMaxComponentCount(Date date) throws RemoteException {
		int max = 0;
		for (Enumeration enumKey = htComps.keys(); enumKey.hasMoreElements();) {
			String compName = (String) enumKey.nextElement();
			ComponentTuning tuning = getComponentTuning(compName);
			max += tuning.getMaxInstanceCount(date);
		}

		return max;
	}

	public int getMinComponentCount(Date date) throws RemoteException {
		int min = 0;
		for (Enumeration enumKey = htComps.keys(); enumKey.hasMoreElements();) {
			String compName = (String) enumKey.nextElement();
			ComponentTuning tuning = getComponentTuning(compName);
			min += tuning.getMinInstanceCount(date);
		}

		return min;
	}

	public boolean isShutdownInProgress() throws RemoteException {
		return isShutdownInProgress;
	}

	public int getTotalInstanceCount() throws RemoteException {
		int count = 0;
		Enumeration enumKey = htComps.keys();
		do {
			if (!enumKey.hasMoreElements()) {
				break;
			}
			Vector vComps = (Vector) htComps.get(enumKey.nextElement());
			if (vComps.size() > 0) {
				count += vComps.size();
			}
		} while (true);
		System.out.println("CMSContainer.getTotalInstanceCount()-> count: "
				.concat(String.valueOf(String.valueOf(count))));
		return count;
	}

	public String getUID() throws RemoteException {
		return System.getProperty("UID");
	}

	public ContainerVitals getVitals() throws RemoteException {
		return contBuilder.getVitalsInstance();
	}

	public void clearErrorLog() throws RemoteException {
		try {
			File file = new File(AppManager.getLocalFile(String.valueOf(String
					.valueOf((new StringBuffer("../log/"))
							.append(containerName).append("_").append(getUID())
							.append(".err")))));
			FileOutputStream newErrorFileOutputStream = new FileOutputStream(
					file);
			BufferedOutputStream errorBufferedOutputStream = new BufferedOutputStream(
					newErrorFileOutputStream, 128);
			System.setErr(new PrintStream(errorBufferedOutputStream, true));
			errorFileOutputStream.close();
			file.delete();
			errorFileOutputStream = newErrorFileOutputStream;
		} catch (Exception ex) {
			throw new RemoteException("CMSContainer.clearErrorLog()->", ex);
		}
	}

	public boolean createCMSComponent(String componentName, int reason) {
		try {
			addEventLogEntry(componentName, 0, reason);
			System.out.println("creating CMSComponent: ".concat(String
					.valueOf(String.valueOf(componentName))));
			ComponentTuning tuning = getComponentTuning(componentName);
			Class cls = Class.forName(tuning.getComponentClassName());
			tuning.setProperty("UID", Long.toString(System.currentTimeMillis()));
			tuning.setProperty("NAME", componentName);
			Constructor con = cls.getConstructor(new Class[] { java.util.Properties.class });
			if (con == null) {
				throw new NullPointerException("no constructor found for ".concat(String.valueOf(String.valueOf(cls.getName()))));
			}
			Object obj = con.newInstance(new Object[] { tuning.getProperties() });
			Vector vComps = (Vector) htComps.get(componentName);
			if (vComps == null) {
				vComps = new Vector();
				htComps.put(componentName, vComps);
			}
			vComps.add(obj);
			boolean flag2 = true;
			return flag2;
		} catch (InvocationTargetException itx) {
			System.out.println("CMSContainer.createCMSComponent()->".concat(String.valueOf(String.valueOf(itx.getTargetException()))));
			itx.getTargetException().printStackTrace();
			boolean flag = false;
			return flag;
		} catch (Exception ex) {
			System.out.println("CMSContainer.createCMSComponent()->".concat(String.valueOf(String.valueOf(ex))));
		}
		boolean flag1 = false;
		return flag1;
	}

	public boolean hasErrorBeenLogged() {
		try {
			File file = new File(AppManager.getLocalFile(String.valueOf(String.valueOf((new StringBuffer("../log/")).append(containerName).append("_").append(getUID()).append(".err")))));
			if (!file.exists()) {
				boolean flag = false;
				return flag;
			} else {
				System.out.println("CMSContainer.hasErrorBeenLogged() file.length: ".concat(String.valueOf(String.valueOf(file.length()))));
				boolean flag1 = file.length() > (long) 0;
				return flag1;
			}
		} catch (Exception ex) {
			System.out.println("Exception CMSContainer.hasErrorBeenLogged()->".concat(String.valueOf(String.valueOf(ex))));
		}
		boolean flag2 = true;
		return flag2;
	}

	public static void main(String[] args) {
		String containerFile = System.getProperty("CONTAINER_FILE");
		System.out.println("containFile " + containerFile);
		//    LoggingServices.getCurrent().logMsg("test");
		if (containerFile == null) {
			System.err.println("Fatal Error. Missing CONTAINER_FILE property.");
			LoggingServices.getCurrent().logMsg(
					"CMSContainer",
					"main",
					"Exception",
					"See Exception" + " freeMemory:" + String.valueOf(Runtime.getRuntime().freeMemory()) + " totalMemory:" + String.valueOf(Runtime.getRuntime().totalMemory()) + " activeCount:"
							+ String.valueOf(Thread.activeCount()), LoggingServices.MAJOR, null);
			System.exit(-1);
		}
		CMSContainer cmscontainer;
		try {
			cmscontainer = new CMSContainer(containerFile);
			System.out.println("cmscontainer " + cmscontainer);
		} catch (Exception ex) {
			System.err.println("Exception CMSContainer.main()->".concat(String.valueOf(String.valueOf(ex))));
			LoggingServices.getCurrent().logMsg(
					"CMSContainer",
					"main",
					"Exception",
					"See Exception" + " freeMemory:" + String.valueOf(Runtime.getRuntime().freeMemory()) + " totalMemory:" + String.valueOf(Runtime.getRuntime().totalMemory()) + " activeCount:"
							+ String.valueOf(Thread.activeCount()), LoggingServices.MAJOR, ex);
			System.exit(-1);
		}
	}

	public boolean ping() throws RemoteException {
		return true;
	}

	public boolean requestComponentShutdown(String componentName, String uid) throws RemoteException {
		return shutdownComponent(componentName, uid, 3);
	}

	public void requestContainerShutdown() throws RemoteException {
		System.out.println("CMSContainer.requestContainerShutdown()...");
		if (isShutdownInProgress) {
			System.out.println("CMSContainer.requestContainerShutdown returning because (isShutdownInProgress == true)");
			return;
		}
		isShutdownInProgress = true;
		String[] compNames = getComponents();
		System.out.println("CMSContainer.requestContainerShutdown()-> compNames.length: ".concat(String.valueOf(String.valueOf(compNames.length))));
		for (int x = 0; x < compNames.length; x++) {
			ICMSComponent[] comps = getCMSComponents(compNames[x]);
			for (int y = 0; y < comps.length; y++) {
				LoggingServices.getCurrent().logMsg(
						"CMSContainer.requestContainerShutdown()->",
						"requestContainerShutdown",
						"Exception",
						"See Exception" + " freeMemory:" + String.valueOf(Runtime.getRuntime().freeMemory()) + " totalMemory:" + String.valueOf(Runtime.getRuntime().totalMemory()) + " activeCount:"
								+ String.valueOf(Thread.activeCount()) + " UID:" + String.valueOf(getUID()), LoggingServices.MAJOR, null);
				requestComponentShutdown(compNames[x], comps[y].getUID());
			}
		}

		System.out.println("CMSContainer.requestContainerShutdown()->".concat(String.valueOf(String.valueOf(getUID()))));
		ShutdownContainerThread shutdown = new ShutdownContainerThread(this);
		shutdown.start();
	}

	public void saveContainerVitalsHolder(ContainerVitalsHolder holder) {
		try {
			FileWriter writer = new FileWriter(FileMgr.getLocalFile("container",
					String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(getContainerName())))).append("_").append(getUID()).append(".xml")))));
			writer.write((new DefaultObjectXML()).toXML(holder));
			writer.flush();
			writer.close();
		} catch (Exception ex) {
			System.out.println("Exception saveContainerVitalsHolder()->".concat(String.valueOf(String.valueOf(ex))));
		}
	}

	public boolean startComponent(String componentName) throws RemoteException {
		return createCMSComponent(componentName, 3);
	}

	public void updateContainerTuning(ContainerTuning containerTuning) {
		tuning = containerTuning;
		ComponentTuning[] compTunings = tuning.getComponentTunings();
		for (int x = 0; x < compTunings.length; x++) {
			if (!htComps.containsKey(compTunings[x].getName())) {
				System.out.println("updateContainerTuning()-> new component:".concat(String.valueOf(String.valueOf(compTunings[x].getName()))));
				htComps.put(compTunings[x].getName(), new Vector());
			}
		}

		Enumeration enm = htComps.keys();
		label0: do {
			if (!enm.hasMoreElements()) {
				break;
			}
			String key = (String) enm.nextElement();
			for (int i = compTunings.length - 1; i >= 0; i--) {
				if (compTunings[i].getName().equals(key)) {
					continue label0;
				}
			}

			System.out.println("updateContainerTuning()-> removing component:".concat(String.valueOf(String.valueOf(key))));
			htComps.remove(key);
		} while(true);
	}

	protected String getRNSLocation() {
		NetworkMgr mgr = new NetworkMgr("network.cfg");
		return mgr.getRMIAppNode();
	}

	protected void stopComponent(ICMSComponent theComponent) {
		String compName = null;
		try {
			compName = theComponent.getName();
			System.out.println("CMSContainer.stopComponent()->".concat(String.valueOf(String.valueOf(compName))));
		} catch (RemoteException ex) {
			System.out.println("CMSContainer.stopComponent()-> ".concat(String.valueOf(String.valueOf(ex))));
			System.out.println("CMSContainer.stopComponent()-> cannot remove component from reference without name");
		}
		try {
			NamingService.unbind(String.valueOf(getRNSLocation()) + String.valueOf(compName), theComponent);
			System.out.println("CMSContainer.stopComponent()-> component unbound");
		} catch (NotBoundException ex) {
			System.out.println("CMSContainer.stopComponent()-> ".concat(String.valueOf(String.valueOf(ex))));
		} catch (RemoteException ex) {
			System.out.println("CMSContainer.stopComponent()-> ".concat(String.valueOf(String.valueOf(ex))));
		}
		Vector vComps = (Vector) htComps.get(compName);
		vComps.remove(theComponent);
		System.out.println("CMSContainer.stopComponent()-> component removed");
	}

	void addEventLogEntry(String componentName, int action, int reason) {
		vLog.add(new EventLogEntry(componentName, action, reason));
	}

	boolean requestComponentShutdown(String componentName, int reason) throws RemoteException {
		ContainerVitalsHolder holder = getContainerVitalsHolder();
		if (holder == null) {
			return false;
		}
		long longestTimeUp = 0x8000000000000000L;
		String uid = null;
		String[] uids = holder.getComponentKeys();
		for (int i = uids.length - 1; i >= 0; i--) {
			ComponentVitals[] vits = holder.getComponentVitals(uids[i]);
			if (vits[vits.length - 1].getMillisUp() > longestTimeUp) {
				uid = uids[i];
			}
		}

		return shutdownComponent(componentName, uid, reason);
	}

	boolean shutdownComponent(String componentName, String uid, int reason) {
		try {
			addEventLogEntry(componentName, 1, reason);
			System.out.println(String.valueOf(String.valueOf((new StringBuffer("requestComponentShutdown()->")).append(componentName).append(":").append(uid))));
			ICMSComponent[] comps = getCMSComponents(componentName);
			if (comps == null) {
				boolean flag = false;
				return flag;
			}
			for (int x = 0; x < comps.length; x++) {
				if (comps[x].getUID().equals(uid)) {
					ShutDownThread down = new ShutDownThread(comps[x], this);
					down.start();
					boolean flag3 = true;
					return flag3;
				}
			}

			boolean flag1 = false;
			return flag1;
		} catch (Exception ex) {
			System.out.println("Exception stopComponent()->".concat(String.valueOf(String.valueOf(ex))));
		}
		boolean flag2 = false;
		return flag2;
	}

	private ComponentTuning getComponentTuning(String componentName) {
		ComponentTuning[] tunings = tuning.getComponentTunings();
		for (int x = 0; x < tunings.length; x++) {
			if (tunings[x].getName().equals(componentName)) {
				return tunings[x];
			}
		}

		return null;
	}

	private void allocNewVitalsHistory() {
		try {
			File dir = new File(FileMgr.getLocalDirectory("container"));
			if (!dir.exists()) {
				dir.mkdir();
			}
			ContainerVitalsHolder holder = new ContainerVitalsHolder(getContainerName(), getUID(), tuning.getHoursOfHistory());
			saveContainerVitalsHolder(holder);
		} catch (Exception ex) {
			System.out.println("Exception allocNewVitalsHistory()->".concat(String.valueOf(String.valueOf(ex))));
		}
	}

	private void bind() {
		try {
			String bindName = String.valueOf(getRNSLocation()) + String.valueOf(containerName);
			System.out.println("Binding to: ".concat(String.valueOf(String.valueOf(bindName))));
			NamingService.bind(bindName, this);
		} catch (Exception e) {
			System.out.println("ERROR Unable to bind to local Naming Service()->".concat(String.valueOf(String.valueOf(e))));
			LoggingServices.getCurrent().logMsg(
					this.getClass().getName(),
					"main",
					"Exception",
					"See Exception" + " freeMemory:" + String.valueOf(Runtime.getRuntime().freeMemory()) + " totalMemory:" + String.valueOf(Runtime.getRuntime().totalMemory()) + " activeCount:"
							+ String.valueOf(Thread.activeCount()), LoggingServices.MAJOR, e);
			System.exit(-1);
		}
	}

	private void initMessaging() throws RemoteException {
		String deviceID = System.getProperty("DEVICE_ID");
		if (deviceID == null) {
			deviceID = String.valueOf(String.valueOf((new StringBuffer(String
					.valueOf(String.valueOf(containerName)))).append("_")
					.append(getUID())));
			System.out.println("Setting DEVICE_ID to ".concat(String
					.valueOf(String.valueOf(deviceID))));
			System.setProperty("DEVICE_ID", deviceID);
		}
		String devicetype = System.getProperty("DEVICE_TYPE");
		if (devicetype == null) {
			System.out.println("Setting DEVICE_TYPE to 1");
			System.setProperty("DEVICE_TYPE", "1");
		}
		System.setProperty("com.chelseasystems.cr.messaging.messengers.server",
				tuning.getMessengersAsDelimitedList());
		ServerMessageManager messageManager = new ServerMessageManager(this);
	}

	private void redirectOutput(String name) {
		try {
			FileOutputStream fdOut = new FileOutputStream(
					AppManager.getLocalFile(String.valueOf(String
							.valueOf((new StringBuffer("../log/")).append(name)
									.append("_").append(getUID())
									.append(".out")))));
			System.setOut(new PrintStream(new BufferedOutputStream(fdOut, 128),
					true));
			errorFileOutputStream = new FileOutputStream(
					AppManager.getLocalFile(String.valueOf(String
							.valueOf((new StringBuffer("../log/")).append(name)
									.append("_").append(getUID())
									.append(".err")))));
			BufferedOutputStream errorBufferedOutputStream = new BufferedOutputStream(
					errorFileOutputStream, 128);
			System.setErr(new PrintStream(errorBufferedOutputStream, true));
		} catch (Exception ex) {
			System.out.println("Exception redirectOutput()->".concat(String
					.valueOf(String.valueOf(ex))));
		}
	}
	//Vivek Mishra : Commented in order to move AJBPingThread at client side from server side
	/*// Vivek Mishra : Added method for providing current AJBPingTherad instance.
	public static AJBPingThread getPingThread() {
		return ajbPingThread;
	}*/
}
