/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.xml;

import com.chelseasystems.cr.xml.XMLUtil;
import com.chelseasystems.cs.xml.tags.AlterationXMLTags;
import com.chelseasystems.cs.pos.Alteration;
import com.chelseasystems.cs.pos.AlterationDetail;
import com.chelseasystems.cs.pos.AlterationItemGroup;
import com.chelseasystems.cr.business.BusinessObject;
import org.w3c.dom.*;
import java.text.SimpleDateFormat;


/**
 * <p>Title: AlterationXML</p>
 * <p>Description: XML for Alterations.Xml</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Manpreet S Bawa
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                  |
 +------+------------+-----------+-----------+----------------------------------------------+
 | 1    | 04-20-2005 | Manpreet  | N/A       | POS_104665_TS_Alterations_Rev2               |
 --------------------------------------------------------------------------------------------
 */
public class AlterationXML extends XMLObject implements AlterationXMLTags {

  /**
   * Default Constructor
   */
  public AlterationXML() {
  }

  /**
   * Base TagName
   * @return String
   */
  protected String getThisTagName() {
    return "ALTERATION";
  }

  /**
   * Add an object to Document
   * @param doc Document
   * @param parent Element
   * @param obj Object
   * @throws Exception
   */
  protected void addBusinessObject(Document doc, Element parent, BusinessObject obj)
      throws Exception {
    Alteration alteration = (Alteration)obj;
    //<ALTERATION>
    Element element = doc.createElement(ALTERATION_TAG);
    AlterationItemGroup altItmGrps[] = alteration.getAlterationGroupsArray();
    for (int iGrpCtr = 0; iGrpCtr < altItmGrps.length; iGrpCtr++) {
      //<ITEM_CLASS_GROUP>
      Element elmGrp = XMLUtil.addItem(doc, element, ITEM_CLASS_GRP_TAG, null);
      //<ITEM_CLASS_GROUP_NAME>
      XMLUtil.addItem(doc, elmGrp, ITEM_CLASS_GRP_NAME_TAG, altItmGrps[iGrpCtr].getGroupName());
      //<SUB_GROUPS>
      Element elmSubGrp = XMLUtil.addItem(doc, elmGrp, SUBGROUPS_TAG, null);
      String sSubGroups[] = altItmGrps[iGrpCtr].getSubGroupsArray();
      for (int iSubGrpCtr = 0; iSubGrpCtr < sSubGroups.length; iSubGrpCtr++) {
        //<SUB_GROUP>
        XMLUtil.addItem(doc, elmSubGrp, SUBGROUP_TAG, sSubGroups[iSubGrpCtr]);
      }
      //<VALUES>
      Element elmValues = XMLUtil.addItem(doc, elmGrp, VALUES_TAG, null);
      AlterationDetail alterationDetails[] = altItmGrps[iGrpCtr].getAlterationDetailsArray();
      for (int iValueCtr = 0; iValueCtr < alterationDetails.length; iValueCtr++) {
        //<VALUE>
        Element elmValue = XMLUtil.addItem(doc, elmValues, VALUE_TAG, null);
        XMLUtil.addItem(doc, elmValue, CODE_TAG, alterationDetails[iValueCtr].getAlterationCode());
        XMLUtil.addItem(doc, elmValue, DESC_TAG, alterationDetails[iValueCtr].getDescription());
        XMLUtil.addItem(doc, elmValue, PRICE_TAG, alterationDetails[iValueCtr].getEstimatedPrice());
        XMLUtil.addItem(doc, elmValue, ESTIMATED_TIME_TAG
            , alterationDetails[iValueCtr].getEstimatedTime());
      }
    }
    parent.appendChild(element);
  }

  /**
   * Get Object from Document
   * @param element Element
   * @throws Exception
   * @return Object
   */
  protected BusinessObject getObject(Element element)
      throws Exception {
    Alteration alteration = new Alteration();
    Element itemGrps[] = XMLUtil.getChildElements(element, ITEM_CLASS_GRP_TAG);
    for (int iCtr = 0; iCtr < itemGrps.length; iCtr++) {
      AlterationItemGroup altItmGrp = new AlterationItemGroup();
      altItmGrp.doSetGroupName(XMLUtil.getValueAsString(itemGrps[iCtr], ITEM_CLASS_GRP_NAME_TAG));
      addSubGroups(altItmGrp, itemGrps[iCtr]);
      addAlterationDetails(altItmGrp, itemGrps[iCtr]);
      alteration.addAlterationGroup(altItmGrp);
    }
    return alteration;
  }

  /**
   * Add class elements
   * @param alteration Alteration
   * @param element Element
   */
  private void addSubGroups(AlterationItemGroup altItmGrp, Element element) {
    Element elmSubGrps[] = XMLUtil.getChildElements(element, SUBGROUPS_TAG);
    NodeList nodeList = elmSubGrps[0].getElementsByTagName(SUBGROUP_TAG);
    if (nodeList == null)
      return;
    for (int iCtr = 0; iCtr < nodeList.getLength(); iCtr++) {
      if (nodeList.item(iCtr) == null)
        continue;
      altItmGrp.doAddSubGroup(nodeList.item(iCtr).getFirstChild().getNodeValue());
    }
  }

  /**
   * Add AlterationDetails
   * @param alteration Alteration
   * @param element Element
   */
  private void addAlterationDetails(AlterationItemGroup altItmGrp, Element element) {
    int iCtr = 0;
    String sTmp = "";
    Element valuesTags[] = XMLUtil.getChildElements(element, VALUES_TAG);
    Element valueTags[] = XMLUtil.getChildElements(valuesTags[0], VALUE_TAG);
    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
    for (iCtr = 0; iCtr < valueTags.length; iCtr++) {
      AlterationDetail alterationDetail = new AlterationDetail();
      sTmp = XMLUtil.getValueAsString(valueTags[iCtr], CODE_TAG);
      alterationDetail.doSetAlterationCode(sTmp);
      sTmp = XMLUtil.getValueAsString(valueTags[iCtr], DESC_TAG);
      alterationDetail.doSetDescription(sTmp);
      alterationDetail.doSetEstimatedPrice(XMLUtil.getValueAsCurrency(valueTags[iCtr], PRICE_TAG));
      try {
        String sTimeStamp = XMLUtil.getValueAsString(valueTags[iCtr], ESTIMATED_TIME_TAG);
        alterationDetail.doSetEstimatedTime(dateFormat.parse(sTimeStamp));
      } catch (Exception e) {
        alterationDetail.doSetEstimatedTime(XMLUtil.getValueAsDate(valueTags[iCtr]
            , ESTIMATED_TIME_TAG));
      }
      altItmGrp.doAddAlterationDetail(alterationDetail);
    }
  }
}

