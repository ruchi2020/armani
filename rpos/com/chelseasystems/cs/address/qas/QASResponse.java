package com.chelseasystems.cs.address.qas;

import java.io.Serializable;
import java.util.Hashtable;

import com.chelseasystems.cs.address.Request;
import com.chelseasystems.cs.address.Response;
import com.qas.proweb.FormattedAddress;
import com.qas.proweb.Picklist;
import com.qas.proweb.PicklistItem;
import com.qas.proweb.SearchResult;

/*
History:
+------+------------+-----------+-----------+----------------------------------------------------+
| Ver# | Date       | By        | Defect #  | Description                                        |
+------+------------+-----------+-----------+----------------------------------------------------+ 
| 1    | 04-04-2006 |David Fung | PCR 67    | QAS                                                |
+------+------------+-----------+-----------+----------------------------------------------------+
*/

/**
* <p>Title:QASResponse.java </p>
*
* <p>Description: QASResponse </p>
*
* <p>Copyright: Copyright (c) 2005</p>
*
* <p>Company: Skillnet inc.</p>
*
* @author David Fung
* @version 1.0
*/

public class QASResponse implements Response, Serializable {

    static final long serialVersionUID = -655071407137755112L;

    private QASRequest request = null;

    private FormattedAddress    searchResultAddress = null;
	private Picklist            searchResultPicklist = null;
	private String              searchResultVerifyLevel = null;
    
    
    private Hashtable addressTable = new Hashtable();

    private FormattedAddress refinementAddressFound = null;

    public QASResponse(QASRequest request, SearchResult searchResult) {
        this.request = request;
        this.searchResultAddress = searchResult.getAddress();
        this.searchResultPicklist = searchResult.getPicklist();
        this.searchResultVerifyLevel = searchResult.getVerifyLevel();
    }

    public QASResponse(QASRequest request, FormattedAddress addressFound) {
        this.request = request;
        this.refinementAddressFound = addressFound;
    }

    public Request getRequest() {
        return this.request;
    }

    public FormattedAddress getVerifiedAddress() {
        if (this.request.isForRefinement()) {
            return this.refinementAddressFound;
        }
        if (this.getSearchResultVerifyLevel().equals(SearchResult.Verified)) {
            return this.getSearchResultAddress();
        } else if (this.getSearchResultPicklist() == null) {
            return this.getSearchResultAddress();
        } else {
            return null;
        }
    }

    public PicklistItem[] getPicklistItems() {
        Picklist picklist = this.getSearchResultPicklist();
        if (picklist == null || picklist.getItems() == null || picklist.getItems().length == 0) {
            return null;
        } else {
            return picklist.getItems();
        }
    }

    public boolean isNoMatchFound() {
        if (this.getVerifiedAddress() != null || (this.getPicklistItems() != null && this.getPicklistItems().length > 0)) {
            return false;
        } else {
            return true;
        }
    }

    public void addFormattedAddress(String moniker, FormattedAddress address) {
        this.addressTable.put(moniker, address);
    }

    public FormattedAddress getFormattedAddress(String moniker) {
        return (FormattedAddress) this.addressTable.get(moniker);
    }

    public boolean isForRefinement() {
        return this.request.isForRefinement();
    }
    
    public FormattedAddress getRefinementAddress() {
        return this.refinementAddressFound;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        if (this.isForRefinement()) {
            sb.append("Refined:" + QASUtil.formattedAddressToString(this.getRefinementAddress()));
        } else if (this.getVerifiedAddress() != null){
            sb.append("Verified:" + QASUtil.formattedAddressToString(this.getVerifiedAddress()));
        } else {
            PicklistItem[] items = getPicklistItems();
            for (int i = 0; i < items.length; i++) {
                sb.append("\n<Item[#" + i + "]" + QASUtil.picklistItemToString(this, items[i]) + ">");
            }
        }
        return sb.toString();
    }
    
    /**
     * @return Returns the searchResultAddress.
     */
    public FormattedAddress getSearchResultAddress() {
        return searchResultAddress;
    }
    /**
     * @return Returns the searchResultPicklist.
     */
    public Picklist getSearchResultPicklist() {
        return searchResultPicklist;
    }
    /**
     * @return Returns the searchResultVerifyLevel.
     */
    public String getSearchResultVerifyLevel() {
        return searchResultVerifyLevel;
    }
}