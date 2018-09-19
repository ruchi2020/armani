/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.util;

import com.chelseasystems.cs.xml.*;
import java.io.*;
import java.util.*;
import com.chelseasystems.cs.pos.Alteration;
import com.chelseasystems.cs.pos.AlterationItemGroup;
import com.chelseasystems.cr.config.*;


/**
 * <p>Title: AlterationLookUpUtil</p>
 * <p>Description: Utility to get objects from Alterations.xml</p>
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
 | 1    | 04-22-2005 | Manpreet  | N/A       | POS_104665_TS_Alterations_Rev2                     |
 +------+------------+-----------+-----------+----------------------------------------------------+
 */
public class AlterationLookUpUtil {
  /**
   * XMLFile Name
   */
  private final String ALTERATION_XML_FILE = FileMgr.getLocalFile("xml", "Alterations.xml");
  /**
   * Vector to hold alterationObject
   */
  private Vector vecAlterations;

  /**
   * Default Constructor
   */
  public AlterationLookUpUtil() {
    if (vecAlterations == null)
      loadAllAlterations();
  }

  /**
   * This method is used to find a alteration group by its classID
   * @param sClassId String
   * @return AlterationItemGroup
   */
  public AlterationItemGroup findBySubGroupId(String sSubGroupId) {
    if (sSubGroupId == null || sSubGroupId.trim().length() < 1)
      return null;
    for (int iCtr = 0; iCtr < vecAlterations.size(); iCtr++) {
      Alteration alteration = (Alteration)vecAlterations.elementAt(iCtr);
      if (alteration == null)
        continue;
      AlterationItemGroup altItmGrps[] = alteration.getAlterationGroupsArray();
      for (int iGrpCtr = 0; iGrpCtr < altItmGrps.length; iGrpCtr++) {
        String alterationSubGroups[] = altItmGrps[iGrpCtr].getSubGroupsArray();
        if (alterationSubGroups == null)
          continue;
        for (int iSubGroupCtr = 0; iSubGroupCtr < alterationSubGroups.length; iSubGroupCtr++) {
          if (alterationSubGroups[iSubGroupCtr].equalsIgnoreCase(sSubGroupId))
            return altItmGrps[iGrpCtr];
        }
      }
    }
    return null;
  }

  /**
   * This method is used to find an alteration Item group by its group name
   * @param sGroupName String
   * @return AlterationItemGroup
   */
  public AlterationItemGroup findByItemGroup(String sGroupName) {
    if (sGroupName == null || sGroupName.trim().length() < 1)
      return null;
    for (int iCtr = 0; iCtr < vecAlterations.size(); iCtr++) {
      Alteration alteration = (Alteration)vecAlterations.elementAt(iCtr);
      if (alteration == null)
        continue;
      AlterationItemGroup altItmGrps[] = alteration.getAlterationGroupsArray();
      for (int iGrpCtr = 0; iGrpCtr < altItmGrps.length; iGrpCtr++) {
        if (altItmGrps[iGrpCtr].getGroupName().equalsIgnoreCase(sGroupName))
          return altItmGrps[iGrpCtr];
      }
    }
    return null;
  }

  /**
   * This method used to get hashtable consisting group names as key and
   * classid as values
   * @return Hashtable
   */
  public Hashtable getAlterationGrpNamesWithDefaultSubGroupId() {
    Hashtable htGrps = new Hashtable();
    for (int iCtr = 0; iCtr < vecAlterations.size(); iCtr++) {
      Alteration alteration = (Alteration)vecAlterations.elementAt(iCtr);
      if (alteration == null)
        continue;
      AlterationItemGroup altItmGrps[] = alteration.getAlterationGroupsArray();
      for (int iGrpCtr = 0; iGrpCtr < altItmGrps.length; iGrpCtr++) {
        htGrps.put(altItmGrps[iGrpCtr].getSubGroupsArray()[0], altItmGrps[iGrpCtr].getGroupName());
      }
      return htGrps;
    }
    return null;
  }

  /**
   * This method is used to get the all alteraion group names
   * @return String[]
   */
  public String[] getAlterationGroupNames() {
    Vector vecGroupNames = new Vector();
    for (int iCtr = 0; iCtr < vecAlterations.size(); iCtr++) {
      Alteration alteration = (Alteration)vecAlterations.elementAt(iCtr);
      if (alteration == null)
        continue;
      AlterationItemGroup altItmGrps[] = alteration.getAlterationGroupsArray();
      for (int iGrpCtr = 0; iGrpCtr < altItmGrps.length; iGrpCtr++) {
        vecGroupNames.addElement(altItmGrps[iGrpCtr].getGroupName());
      }
      return (String[])vecGroupNames.toArray(new String[vecGroupNames.size()]);
    }
    return null;
  }

  /**
   * The method used to load all alterations into Vector
   */
  protected void loadAllAlterations() {
    try {
      vecAlterations = (new AlterationXML()).getObjectsFromFile(ALTERATION_XML_FILE);
    } catch (org.xml.sax.SAXException saxException) {
      saxException.printStackTrace();
    } catch (IOException ie) {
      ie.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

