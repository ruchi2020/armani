/*
 * @copyright (c) 1998-2002 Retek Inc
 */


package  com.chelseasystems.cr.node;

import  java.util.*;


/**
 * @author John Gray
 * @version 1.0
 */
public class ContainerTuning implements java.io.Serializable {

   static final long serialVersionUID = -2111132207852307882L;

   public static final int NONE = 0;
   public static final int START = 1;
   public static final int SHUTDOWN = 2;

   /**
    *
    * @param containerName
    */
   public ContainerTuning (String containerName) {
      this.containerName = containerName;
      if (containerName != null)
         this.addCommandLineArg("CONTAINER_FILE=" + containerName);
   }

   /**
    *
    * @return int
    */
   public int getHoursOfHistory () {
      return  hoursOfHistory;
   }

   /**
    *
    * @param hist
    */
   public void setHoursOfHistory (int hoursOfHistory) {
      this.hoursOfHistory = hoursOfHistory;
   }

   /**
    *
    * @return String
    */
   public String getProcessStats () {
      return  processStats;
   }

   /**
    *
    * @param processStats
    */
   public void setProcessStats (String processStats) {
      this.processStats = processStats;
   }

   /**
    *
    * @param minutes
    */
   public void setMinutesPauseVitals (int minutesPauseVitals) {
      this.minutesPauseVitals = minutesPauseVitals;
   }

   /**
    *
    * @return int
    */
   public int getMinutesPauseVitals () {
      return  minutesPauseVitals;
   }

   /**
    * in minutes
    * @param minutes
    */
   public void setComponentInstancePause (int componentInstancePause) {
      this.componentInstancePause = componentInstancePause;
   }

   /**
    *
    * @return int
    */
   public int getComponentInstancePause () {
      return  componentInstancePause;
   }

   /**
    *
    * @return String
    */
   public String getClassName () {
      return  CMSContainer.class.getName();
   }

   /**
    *
    * @return String
    */
   public String getContainerName () {
      return  containerName;
   }

   /**
    * @param containerName
    */
   public void doSetContainerName(String containerName) {
      this.containerName = containerName;
   }

   /**
    *
    * @param now
    * @return int
    */
   public int getInstanceCount (Date now) {
      for (Enumeration enm = instanceOverrides.elements(); enm.hasMoreElements();) {
         InstanceOverride ride = (InstanceOverride)enm.nextElement();
         if (ride.isOverrideInEffect(now))
            return  ride.getInstanceCount();
      }
      return  defaultInstanceCount;
   }

   /**
    *
    * @return int
    */
   public int getDefaultInstanceCount () {
      return  defaultInstanceCount;
   }

   /**
    *
    * @param count
    */
   public void setDefaultInstanceCount (int defaultInstanceCou) {
      this.defaultInstanceCount = defaultInstanceCou;
   }

   /**
    * @return String[]
    */
   public String[] getCommandLineArgs () {
      return  (String[])commandLineArgs.toArray(new String[commandLineArgs.size()]);
   }

   /**
    * @param arg
    */
   public void addCommandLineArg (String arg) {
      commandLineArgs.addElement(arg);
   }

   /**
    * @param arg
    */
   public void removeCommandLineArg (String arg) {
      commandLineArgs.removeElement(arg);
   }

   /**
    */
   public void removeAllCommandLineArgs () {
      commandLineArgs.removeAllElements();
   }
   
   /**
    * Get the list of Virtual Machine arguments
    * @return
    */
   public String[] getVmArgs(){
	   return (String[])vmArgs.toArray(new String[vmArgs.size()]);
   }
   
   /**
    * @param arg
    */
   public void addVmArg (String arg) {
      vmArgs.add(arg);
   }

   /**
    * @param arg
    */
   public void removeVmArg (String arg) {
      vmArgs.remove(arg);
   }

   /**
    */
   public void removeAllVmArgs () {
      vmArgs.clear();
   }
   
   /**
    * @return String[]
    */
   public String[] getBootStraps () {
      return  (String[])bootStraps.toArray(new String[bootStraps.size()]);
   }

   /**
    * @param arg
    */
   public void addBootStrap (String cmsBootstrapClassName) {
      bootStraps.addElement(cmsBootstrapClassName);
   }

   /**
    * @param arg
    */
   public void removeBootStrap (String cmsBootstrapClassName) {
      bootStraps.removeElement(cmsBootstrapClassName);
   }

   /**
    * @param arg
    */
   public void removeAllBootStraps () {
      bootStraps.clear();
   }

   /**
    * @return String[]
    */
   public String[] getMessengers () {
      return  (String[])messengers.toArray(new String[messengers.size()]);
   }

   /**
    * @return String[]
    */
   public String getMessengersAsDelimitedList () {
      StringBuffer buff = new StringBuffer();
      for (int i = 0; i < messengers.size(); i++)
      {
        buff.append(messengers.get(i));
        if (i < messengers.size() -1)
          buff.append(',');
      }
      return  buff.toString();
   }

   /**
    * @param arg
    */
   public void addMessenger (String messengersClassName) {
      messengers.addElement(messengersClassName);
   }

   /**
    * @param arg
    */
   public void removeMessenger (String messengersClassName) {
      messengers.removeElement(messengersClassName);
   }

   /**
    * @param arg
    */
   public void removeAllMessengers () {
      messengers.clear();
   }

   /**
    * @return String[]
    */
   public String[] getDaemons () {
      return  (String[])daemons.toArray(new String[daemons.size()]);
   }

   /**
    * @param arg
    */
   public void addDaemon (String cmsDaemonClassName) {
      daemons.addElement(cmsDaemonClassName);
   }

   /**
    * @param arg
    */
   public void removeDaemon (String cmsDaemonClassName) {
      daemons.removeElement(cmsDaemonClassName);
   }

   /**
    * @param arg
    */
   public void removeAllDaemons () {
      daemons.clear();
   }

   /**
    *
    * @param tuning
    */
   public void addComponentTuning (ComponentTuning tuning) {
      componentTunings.add(tuning);
   }

   /**
    *
    * @param componentName
    */
   public void removeComponentTuning (String componentName) {
      for (Enumeration enm = componentTunings.elements(); enm.hasMoreElements();) {
         ComponentTuning tuning = (ComponentTuning)enm.nextElement();
         if (tuning.getName().equals(componentName)) {
            componentTunings.remove(tuning);
            return;
         }
      }
   }

   /**
    *
    */
   public void clearComponentTuning () {
      componentTunings.removeAllElements();
   }

   /**
    *
    * @return ComponentTuning[]
    */
   public ComponentTuning[] getComponentTunings () {
      ComponentTuning[] result = new ComponentTuning[componentTunings.size()];
      componentTunings.copyInto(result);
      return  result;
   }

   /**
    *
    * @param componentName
    * @return ComponentTuning
    */
   public ComponentTuning getComponentTuning (String componentName) {
      for (Enumeration enm = componentTunings.elements(); enm.hasMoreElements();) {
         ComponentTuning tuning = (ComponentTuning)enm.nextElement();
         //System.out.println("getComponentTuning()->" + tuning.getName());
         if (tuning.getName().equals(componentName)) {
            return  tuning;
         }
      }
      return  null;
   }

   /**
    *
    * @param now
    * @return int
    */
   public int getMaxComponentCount (Date now) {
      int max = 0;
      for (Enumeration enm = componentTunings.elements(); enm.hasMoreElements();) {
         ComponentTuning tuning = (ComponentTuning)enm.nextElement();
         max += tuning.getMaxInstanceCount(now);
      }
      //System.out.println("Max component count:" + max);
      return  max;
   }

   /**
    *
    * @param now
    * @return int
    */
   public int getMinComponentCount (Date now) {
      int min = 0;
      for (Enumeration enm = componentTunings.elements(); enm.hasMoreElements();) {
         ComponentTuning tuning = (ComponentTuning)enm.nextElement();
         min += tuning.getMinInstanceCount(now);
      }
      return  min;
   }

   /**
    *
    * @param ride
    * @exception Exception
    */
   public void addInstanceOverride (InstanceOverride ride) throws Exception {
      // need to put in check for overriding times
      instanceOverrides.add(ride);
   }

   /**
    *
    * @param ride
    */
   public void deleteInstanceOverride (InstanceOverride ride) {
      instanceOverrides.remove(ride);
   }

   /**
    *
    * @return InstanceOverride[]
    */
   public InstanceOverride[] getInstanceOverrides () {
      return  (InstanceOverride[])instanceOverrides.toArray(new InstanceOverride[instanceOverrides.size()]);
   }

   private String containerName;
   private String processStats;
   private Vector componentTunings = new Vector();
   private Vector commandLineArgs = new Vector();
   private ArrayList vmArgs = new ArrayList(5);
   private Vector messengers = new Vector();
   private Vector instanceOverrides = new Vector();
   private Vector bootStraps = new Vector();
   private Vector daemons = new Vector();
   private int defaultInstanceCount = 1;
   private int hoursOfHistory = 1;
   private int minutesPauseVitals = 5;
   private int componentInstancePause = 1;

}



