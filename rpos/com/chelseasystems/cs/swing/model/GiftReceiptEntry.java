
/**
 * Title:        Your Product Name<p>
 * Description:  Your description<p>
 * Copyright:    Copyright (c) 1999<p>
 * Company:      Your Company<p>
 * @author Your Name
 * @version
 */
package com.chelseasystems.cs.swing.model;

public class GiftReceiptEntry implements java.io.Serializable{

   //static final long serialVersionUID = 1959114841563454263L;

                private Boolean selected;
                private String itemId;
                private String description;
                private String itemName;

                /**
                * @param SaleLineItem line
                */

                public GiftReceiptEntry (String itemId, String description) {
                        this.selected = new Boolean(false);
                        this.itemId = itemId;
                        this.description = description;
                }
                
                /**
                 * Add Item Name
                 * @param itemId
                 * @param description
                 * @param itemName
                 * @since IssueNumber: 1887
                 */
                public GiftReceiptEntry (String itemId, String description, String itemName) {
                  this.selected = new Boolean(false);
                  this.itemId = itemId;
                  this.description = description;
                  this.itemName = itemName;
          }                
                
                public String toString() {
                        return (itemId + " : " + description + " : " + selected);
                }

                public void setSelected(Boolean selected) {
                        this.selected = selected;
                }

                /**
                * @return boolean
                */
                public boolean isSelected() {
                        return selected.booleanValue();
                }

                /**
                 * @return Boolean selected
                 */
                 public Boolean getSelected() {
                        return selected;
                }

                /**
                * @return String
                */
                public String getItemId() {
                        return itemId;
                }

                /**
                 * @return String
                 */
                 public String getDescription() {
                        return description;
                }
                 
                 /**
                  * Get Item Name
                  * @return Item Name 
                  * @since Issue#1887
                  */
                public String getItemName()
                {
                	return this.itemName;
                }
                
        }