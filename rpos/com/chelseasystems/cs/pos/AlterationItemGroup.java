/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.business.BusinessObject;
import java.util.Vector;


/**
 * <p>Title: AlterationItemGroup.java</p>
 * <p>Description: This class store the values of alteration group fetched from
 *  alteration.xml file</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 04-24-2005 | Manpreet  | N/A       | POS_104665_TS_Alterations_Rev2                     |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class AlterationItemGroup extends BusinessObject {
  /**
   * Store the alteration group name
   */
  private String sGroupName;
  /**
   * Classes
   */
  private Vector vecSubGroups;
  /**
   * Store the Alteration Details
   */
  private Vector vecAlterationDetails;

  /**
   * Default Constructor
   */
  public AlterationItemGroup() {
    vecSubGroups = new Vector();
    vecAlterationDetails = new Vector();
  }

  /**
   * This method is used to get the alteration group name
   * @return GroupName
   */
  public String getGroupName() {
    return this.sGroupName;
  }

  /**
   * This method is used to set the alteration group name
   * @param sGroupName GroupName
   */
  public void setGroupName(String sGroupName) {
    if (sGroupName == null || sGroupName.trim().length() < 1)
      return;
    doSetGroupName(sGroupName);
  }

  /**
   * This method is used to set the alteration group name
   * @param sGroupName GroupName
   */
  public void doSetGroupName(String sGroupName) {
    this.sGroupName = sGroupName;
  }

  /**
   * This method is used to get the class array
   * @return Classes[]
   */
  public String[] getSubGroupsArray() {
    return (String[])vecSubGroups.toArray(new String[vecSubGroups.size()]);
  }

  /**
   * This method is used to add a new class to the class array
   * @param sClassName CLASS
   */
  public void addSubGroup(String sSubGroup) {
    if (sSubGroup == null || sSubGroup.trim().length() < 1 || vecSubGroups.contains(sSubGroup))
      return;
    doAddSubGroup(sSubGroup);
  }

  /**
   * This method is used to add a new class to the class array
   * @param sClassName CLASS
   */
  public void doAddSubGroup(String sSubGroup) {
    vecSubGroups.addElement(sSubGroup);
  }

  /**
   * This method is used to get the collection of alteration details
   * @return AlterationDetail[]
   */
  public AlterationDetail[] getAlterationDetailsArray() {
    return (AlterationDetail[])vecAlterationDetails.toArray(new AlterationDetail[
        vecAlterationDetails.size()]);
  }

  /**
   * This method is used to add a new alteration details to the collection
   * @param alterationDetail AlterationDetail
   */
  public void addAlterationDetail(AlterationDetail alterationDetail) {
    if (alterationDetail == null)
      return;
    doAddAlterationDetail(alterationDetail);
  }

  /**
   * This method is used to add a new alteration details to the collection
   * @param alterationDetail AlterationDetail
   */
  public void doAddAlterationDetail(AlterationDetail alterationDetail) {
    vecAlterationDetails.addElement(alterationDetail);
  }
}

