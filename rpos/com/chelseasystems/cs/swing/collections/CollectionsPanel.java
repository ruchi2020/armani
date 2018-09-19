/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 04/19/2005 | Anand     | N/A       | Customizations as per Specifications               |
 |      |            |           |           | (No modifications from base)                       |
 --------------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.collections;

import javax.swing.*;
import com.chelseasystems.cs.collection.*;
import com.chelseasystems.cr.collection.*;
import com.chelseasystems.cr.rules.*;


/**
 * put your documentation comment here
 */
public abstract class CollectionsPanel extends JPanel {
  protected CollectionsApplet owner = null;
  protected String displayName = null;
  protected String collectionType;

  /**
   * put your documentation comment here
   */
  public CollectionsPanel() {
  }

  /**
   * put your documentation comment here
   * @param   CollectionsApplet owner
   * @param   String displayName
   */
  public CollectionsPanel(CollectionsApplet owner, String displayName) {
    this.owner = owner;
    this.displayName = displayName;
  }

  /**
   * put your documentation comment here
   * @return
   * @exception BusinessRuleException
   */
  public abstract CollectionTransaction exportData()
      throws BusinessRuleException;

  /**
   * put your documentation comment here
   */
  public abstract void initialFocus();

  /**
   * put your documentation comment here
   * @return
   */
  public String getDisplayName() {
    return displayName;
  }

  /**
   * put your documentation comment here
   * @return
   */
  public String getCollectionType() {
    return collectionType;
  }

  /**
   * put your documentation comment here
   * @param collectionType
   */
  public void setCollectionType(String collectionType) {
    this.collectionType = collectionType;
  }
}

