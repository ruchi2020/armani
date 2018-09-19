package com.chelseasystems.cs.address.qas;

import java.io.Serializable;

import com.chelseasystems.cs.address.Request;
import com.qas.proweb.PicklistItem;

/*
History:
+------+------------+-----------+-----------+----------------------------------------------------+
| Ver# | Date       | By        | Defect #  | Description                                        |
+------+------------+-----------+-----------+----------------------------------------------------+ 
| 1    | 04-04-2006 |David Fung | PCR 67    | QAS                                                |
+------+------------+-----------+-----------+----------------------------------------------------+
*/

/**
* <p>Title:QASRequest.java </p>
*
* <p>Description: QASRequest </p>
*
* <p>Copyright: Copyright (c) 2005</p>
*
* <p>Company: Skillnet inc.</p>
*
* @author David Fung
* @version 1.0
*/

public class QASRequest implements Request, Serializable {

    static final long serialVersionUID = -1458063291678166054L;
       
    private String dataId;
    
    private String[] userInput;
    
    private PicklistItem pickedItemForRefinement = null;
    
    private String refinementText = null;
    
    public QASRequest(String dataId, String[] userInput) {
        this.dataId = dataId;
        this.userInput = userInput;
    }
    
    public QASRequest(QASResponse response, PicklistItem pickedItemForRefinement, String refinementText) {
        this.dataId = ((QASRequest)response.getRequest()).getDataId();
        this.pickedItemForRefinement = pickedItemForRefinement;
        this.refinementText = refinementText;
    }

    /**
     * @return Returns the dataId.
     */
    public String getDataId() {
        return dataId;
    }
    /**
     * @param dataId The dataId to set.
     */
    public void setDataId(String dataId) {
        this.dataId = dataId;
    }
    /**
     * @return Returns the userInput.
     */
    public String[] getUserInput() {
        return userInput;
    }
    /**
     * @param userInput The userInput to set.
     */
    public void setUserInput(String[] userInput) {
        this.userInput = userInput;
    }
    /**
     * @return Returns the pickedItemForRefinement.
     */
    public PicklistItem getPickedItemForRefinement() {
        return pickedItemForRefinement;
    }
    /**
     * @param pickedItemForRefinement The pickedItemForRefinement to set.
     */
    public void setPickedItemForRefinement(PicklistItem pickedItemForRefinement) {
        this.pickedItemForRefinement = pickedItemForRefinement;
    }
    /**
     * @return Returns the refinementText.
     */
    public String getRefinementText() {
        return refinementText;
    }
    /**
     * @param refinementText The refinementText to set.
     */
    public void setRefinementText(String refinementText) {
        this.refinementText = refinementText;
    }
    
    public boolean isForRefinement() {
        return this.refinementText != null;
    }
    
}
