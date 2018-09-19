/*
 * @copyright (c) 1998-2001 Chelsea Market Systems LLC
 */

package  com.chelseasystems.cr.node;

import  java.io.*;
import  com.chelseasystems.cr.logging.LoggingServices;

/**
 * @author John Gray
 */
public class Activator {

   /**
    */
   public Activator () {
   }

   /**
    * launch application
    */
   public void launch (ContainerTuning container) {
      try {
         String commandLine = getCommandLine(container);
         LoggingServices.getCurrent().logMsg("Activiting Component: " + commandLine);
         System.out.print("Server RMI starting with command="+commandLine);
         System.out.println(" \n commandLine : " + commandLine);
         Process p = Runtime.getRuntime().exec(commandLine);
         BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
         Thread.sleep(2000);
      } catch (Exception ex) {
         LoggingServices.getCurrent().logMsg(this.getClass().getName(), "launch",
               "Exception", "See Exception", LoggingServices.MAJOR, ex);
      }
   }

   /**
    * @param container
    * @return
    */
   String getCommandLine (ContainerTuning container) {
      long uid = System.currentTimeMillis();
      StringBuffer buf = new StringBuffer();
      if (System.getProperty("os.name").indexOf("Window") >= 0) {
         buf.append("cmd /c start java ");
      }
      else {
         buf.append("java ");
      }
      
      String[] vmArgs = container.getVmArgs();
      for(int i =0; i<vmArgs.length; i++){
    	  /*if(vmArgs[i].startsWith("-XX:HeapDumpPath")){
    		  vmArgs[i] = "-XX:HeapDumpPath=./java_pid<pid>.hprof";
    	  }*/
    	  buf.append(vmArgs[i]).append(" ");
      }
      
      String[] args = container.getCommandLineArgs();
      for (int x = 0; x < args.length; x++) {
         buf.append("-D" + args[x] + " ");
      }
      // add uid
      buf.append("-DUID=" + uid + " ");
      // set training
      String train = System.getProperty("TRAINING");
      if (train != null) {
         Boolean bTrain = new Boolean(train);
         if (bTrain.booleanValue()) {
            buf.append("-DTRAINING=true ");
         }
      }
      buf.append(container.getClassName() + " ");
      if (System.getProperty("os.name").indexOf("Window") == -1)
         buf.append(" &");
      return  buf.toString();
   }
}

