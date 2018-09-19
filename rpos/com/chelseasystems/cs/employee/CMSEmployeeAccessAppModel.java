/*
 * @copyright (c) 1998-2002 Retek Inc
 */


package com.chelseasystems.cs.employee;

import java.util.*;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cr.util.*;
import com.chelseasystems.cs.receipt.ReceiptBlueprintInventory;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cr.receipt.*;
import com.chelseasystems.cr.appmgr.IReceiptAppManager;
import com.chelseasystems.rb.ReceiptFactory;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.employee.*;
import com.chelseasystems.cr.user.UserAccessRole;


/**
 *
 */
public class CMSEmployeeAccessAppModel implements java.io.Serializable {

  CMSEmployee theOperator;
  Date timestamp = new Date();
  ResourceBundleKey eventType;
  CMSEmployee beforeEmployee;
  CMSEmployee afterEmployee;
  String blueprintName;
  Vector allPossibleRoles;

  /**
   *
   */
  public CMSEmployeeAccessAppModel() {
  }

  /**
   *
   * @param     ResourceBundleKey eventType
   * @param     CMSEmployee theOperator
   * @param     CMSEmployee beforeEmployee
   * @param     CMSEmployee afterEmployee
   * @param     Vector allPossibleRoles
   */
  public CMSEmployeeAccessAppModel(ResourceBundleKey eventType, CMSEmployee theOperator,
      CMSEmployee beforeEmployee, CMSEmployee afterEmployee, Vector allPossibleRoles) {
    this.theOperator = theOperator;
    this.eventType = eventType;
    this.beforeEmployee = beforeEmployee;
    this.afterEmployee = afterEmployee;
    this.allPossibleRoles = allPossibleRoles;
  }

  /**
   *
   * @return
   */
  public CMSEmployee getBeforeEmployee() {
    return beforeEmployee;
  }

  /**
   *
   * @param beforeEmployee
   */
  public void setBeforeEmployee(CMSEmployee beforeEmployee) {
    this.beforeEmployee = beforeEmployee;
  }

  /**
   *
   * @return
   */
  public CMSEmployee getAfterEmployee() {
    return afterEmployee;
  }

  /**
   *
   * @param afterEmployee
   */
  public void setAfterEmployee(CMSEmployee afterEmployee) {
    this.afterEmployee = afterEmployee;
  }

  /**
   *
   * @return
   */
  public CMSEmployee getOperator() {
    return theOperator;
  }

  /**
   *
   * @param theOperator
   */
  public void setOperator(CMSEmployee theOperator) {
    this.theOperator = theOperator;
  }

  /**
   *
   * @return
   */
  public Date getTimestamp() {
    return timestamp;
  }

  /**
   *
   * @param timestamp
   */
  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  /**
   *
   * @return
   */
  public ResourceBundleKey getEventType() {
    return eventType;
  }

  /**
   *
   * @param eventType
   */
  public void setModificationType(ResourceBundleKey eventType) {
    this.eventType = eventType;
  }

  /**
   *
   * @param blueprintName
   */
  public void setBlueprintName(String blueprintName) {
    this.blueprintName = blueprintName;
  }

  /**
   *
   * @return
   */
  public ChangedAttribute[] getChangedAttributes() {
    ObjectChangeLister changeLister = new ObjectChangeLister(this.beforeEmployee,
        this.afterEmployee);
    ChangedAttribute[] changedAttributes = changeLister.getNamedDifferences();
    // Issue # 1235
    int arrLength = changedAttributes.length;
    ArrayList newchangedAttributes;
    if (arrLength > 0) {
      newchangedAttributes = new ArrayList(arrLength);
      for (int i = 0; i < arrLength; i++) {
        ChangedAttribute changedAttribute = changedAttributes[i];
        String attributeName = changedAttribute.getAttributeName();
        if (attributeName.equalsIgnoreCase("Password")) {
          // Don't add
        } else {
          newchangedAttributes.add(changedAttribute);
        }
      }
      return (ChangedAttribute[])newchangedAttributes.toArray(new ChangedAttribute[
          newchangedAttributes.size()]);
    } else {
      return changedAttributes;
    }
  }

  /**
   *
   * @return
   */
  public UserAccessRole[] getAddedRoles() {
    if (beforeEmployee == null || afterEmployee == null || allPossibleRoles == null)
      return null;
    Vector addedRoles = new Vector();
    Vector beforeRoles = beforeEmployee.hasWhichAccessRoles(allPossibleRoles.elements());
    Vector afterRoles = afterEmployee.hasWhichAccessRoles(allPossibleRoles.elements());
    Iterator itr = afterRoles.iterator();
    while (itr.hasNext()) {
      UserAccessRole afterRole = (UserAccessRole)itr.next();
      if (beforeRoles.contains(afterRole)) {} else
        addedRoles.add(afterRole);
    }
    return (UserAccessRole[])addedRoles.toArray(new UserAccessRole[addedRoles.size()]);
  }

  /**
   *
   * @return
   */
  public UserAccessRole[] getDeletedRoles() {
    if (beforeEmployee == null || afterEmployee == null || allPossibleRoles == null)
      return null;
    Vector deletedRoles = new Vector();
    Vector beforeRoles = beforeEmployee.hasWhichAccessRoles(allPossibleRoles.elements());
    Vector afterRoles = afterEmployee.hasWhichAccessRoles(allPossibleRoles.elements());
    Iterator itr = beforeRoles.iterator();
    while (itr.hasNext()) {
      UserAccessRole beforeRole = (UserAccessRole)itr.next();
      if (afterRoles.contains(beforeRole)) {} else
        deletedRoles.add(beforeRole);
    }
    return (UserAccessRole[])deletedRoles.toArray(new UserAccessRole[deletedRoles.size()]);
  }

  /**
   *
   * @param theAppMgr
   */
  public void print(IReceiptAppManager theAppMgr) {
    if (ReceiptConfigInfo.getInstance().isProducingRDO()) {
      String fileName = ReceiptConfigInfo.getInstance().getPathForRDO() +
          blueprintName + ".rdo";
      try {
        ObjectStore objectStore = new ObjectStore(fileName);
        objectStore.write(this);
      } catch (Exception e) {
        System.out.println("exception on writing object to blueprint folder: "
            + e);
      }
    }
    Object[] arguments = {
        this
    };
    CMSEmployee nullOperator = null;
    ReceiptLocaleSetter localeSetter = new ReceiptLocaleSetter((CMSStore)CMSApplet.theStore,
        (CMSEmployee)nullOperator);
    ReceiptFactory receiptFactory = new ReceiptFactory(arguments, blueprintName);
    localeSetter.setLocale(receiptFactory);
    receiptFactory.print(theAppMgr);
  }
}


