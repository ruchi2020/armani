/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.pos;

import com.chelseasystems.cr.business.BusinessObject;
import java.util.Vector;


/**
 * <p>Title:Alteration </p>
 * <p>Description: This class is used to add and get alteration group</p>
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
 | 1    | 04-21-2005 | Manpreet  | N/A       | POS_104665_TS_Alterations_Rev2                     |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class Alteration extends BusinessObject {
  /**
   * This store the object of alteration group
   */
  private Vector vecAlterationGrps;

  /**
   * Default Constructor
   */
  public Alteration() {
    vecAlterationGrps = new Vector();
  }

  /**
   * This method used to get the collection of alteration groups
   * @return AlterationItemGroup[]
   */
  public AlterationItemGroup[] getAlterationGroupsArray() {
    return (AlterationItemGroup[])vecAlterationGrps.toArray(new AlterationItemGroup[
        vecAlterationGrps.size()]);
  }

  /**
   * This method is used to add the alteration item group to the collection
   * @param altItmGrp AlterationItemGroup
   */
  public void addAlterationGroup(AlterationItemGroup altItmGrp) {
    vecAlterationGrps.addElement(altItmGrp);
  }

  /**
   * put your documentation comment here
   * @param altItmGrp[]
   */
  public void setAlterationGroups(AlterationItemGroup altItmGrp[]) {
    vecAlterationGrps = new Vector();
    for (int iCtr = 0; iCtr < altItmGrp.length; iCtr++)
      vecAlterationGrps.addElement(altItmGrp[iCtr]);
  }
}

