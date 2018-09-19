package com.chelseasystems.cs.rules.transaction;

import com.chelseasystems.cr.rules.*;
import java.util.*;
import com.chelseasystems.cr.logging.*;
import com.chelseasystems.cr.employee.*;
import com.chelseasystems.cs.sos.*;

public class SOSTransactionCannotBePostedByATerminatedEmployee extends Rule {

    java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();

   public SOSTransactionCannotBePostedByATerminatedEmployee() {
   }

   /**
    * Execute the Rule
    * @param theParent - instance of CMSTransactionSOS
    * @param args - instance of Employee
    */
   public RulesInfo execute (Object theParent, Object args[]) {
      return execute((CMSTransactionSOS) theParent, (Employee) args[0]);
   }

   /**
    * Execute the Rule
    * @param theParent - instance of CMSTransactionSOS
    * @param args - instance of Employee
    */
   private RulesInfo execute(CMSTransactionSOS cmstransactionsos, Employee employee) {
      try {
         if (employee.isEmploymentStatusTerminated())
            return  new RulesInfo(res.getString("The Start of Session procedure cannot be performed by a terminated employee."));
      } catch (Exception ex) {
         LoggingServices.getCurrent().logMsg(getClass().getName(), "execute",
                                "Rule Failed, see exception.",
                                "N/A", LoggingServices.MAJOR, ex);
      }
      return  new RulesInfo();
   }

   /**
    * Returns the name of the Rule.
    * @return the name of the rule
    */
   public String getName() {
      return "SOSTransaction cannot be posted by terminated employee";
   }

   /**
    * Returns a user-friendly version of the rule.
    * @returns a user-friendly version of the rule.
    */
   public String getDesc() {
      StringBuffer buf = new StringBuffer();
      buf.append("This rule should prevent an Employee who");
      buf.append(" is terminated from be set onto a SOSTra");
      buf.append("nsaction.  This should keep it from posting.");
      return buf.toString();
   }
}
