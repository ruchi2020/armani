package com.chelseasystems.cs.address.qas;

import java.net.MalformedURLException;

import javax.xml.rpc.ServiceException;

import com.chelseasystems.cr.config.ConfigMgr;
import com.qas.proweb.CanSearch;
import com.qas.proweb.FormattedAddress;
import com.qas.proweb.Picklist;
import com.qas.proweb.PicklistItem;
import com.qas.proweb.PromptSet;
import com.qas.proweb.QasException;
import com.qas.proweb.QuickAddress;
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
* <p>Title:QASEngine.java </p>
*
* <p>Description: QASEngine </p>
*
* <p>Copyright: Copyright (c) 2005</p>
*
* <p>Company: Skillnet inc.</p>
*
* @author David Fung
* @version 1.0
*/

public class QASEngine {
    
    private static QASEngine theInstance = null;
    
    private QuickAddress qas = null;

    private String endpoint = null;
    
    private String layout = null;

    private QASEngine() {
        try {
            ConfigMgr configMgr = new ConfigMgr("addressverify.cfg");
            this.endpoint = configMgr.getString("QAS_ENDPOINT");
            this.layout = configMgr.getString("QAS_ADDRESS_LAYOUT");
            this.qas = new QuickAddress(this.endpoint);
            this.qas.setEngineType(QuickAddress.VERIFICATION);
            this.qas.setFlatten(true);
        } catch (MalformedURLException mre) {
            mre.printStackTrace();
        } catch (ServiceException se) {
            se.printStackTrace();
        } catch (Exception qe) {
            qe.printStackTrace();
        }
    }    

    private static QASEngine getInstance() {
        if (QASEngine.theInstance == null) {
            QASEngine.theInstance = new QASEngine();
        }        
        return QASEngine.theInstance;
    }    
    
    public static String getLayout() {
        return QASEngine.getInstance().layout;
    }
    
    public static String getEndpoint() {
        return QASEngine.getInstance().endpoint;
    }

    public static QASResponse search(QASRequest request) throws Exception {
        if (request.isForRefinement()) {
            return QASEngine.refine(request);
        } else {
            return QASEngine.firstSearch(request);
        }
    }    
    
    private static QuickAddress getQas() {
        return QASEngine.getInstance().qas;
    }
    
    private static QASResponse firstSearch(QASRequest request) throws QasException {
        if (!QASEngine.canSearch(request.getDataId())) {
            return null;
        }
        String dataId = request.getDataId();
        String[] userInput = request.getUserInput();
        SearchResult result = QASEngine.search(dataId, userInput);
        QASResponse response = new QASResponse(request, result);
        PicklistItem[] items = response.getPicklistItems();
        if (items != null && items.length > 0) {
            for (int i = 0; i < items.length; i++) {
                if (!QASEngine.mustRefine(items[i])) {
                    try {
                        FormattedAddress address = QASEngine.findFormattedAddress(items[i].getMoniker());
                        if (address != null) {
                            response.addFormattedAddress(items[i].getMoniker(), address);
                        }
                    } catch (Exception exp) {
                        //no address found do nothing.
                    }
                }
            }
        }
        return response;
    }
    
    private static QASResponse refine(QASRequest request) throws QasException {
        String dataId = request.getDataId();
        if (!QASEngine.canSearch(dataId)) {
            return null;
        }
        PicklistItem item = request.getPickedItemForRefinement();
        String refinementText = request.getRefinementText();
        FormattedAddress address = QASEngine.findFormattedAddress(item, refinementText);
        return new QASResponse(request, address);
    }    
    
    private static boolean canSearch(String dataId) throws QasException {
        String layout = QASEngine.getLayout();
        CanSearch canSearch = QASEngine.getQas().canSearch(dataId, layout);
        if (!canSearch.isOk()) {
            String error = canSearch.getMessage();
            QasException qe = new QasException("Precheck Fail: " + error, -999);
            qe.printStackTrace();
            throw qe;
        }
        return canSearch.isOk();
    }
    
    private static SearchResult search(String dataId, String[] userInput) throws QasException {
        String layout = QASEngine.getLayout();
        return QASEngine.getQas().search(dataId, userInput, PromptSet.DEFAULT, layout);
    }
    
    private static FormattedAddress findFormattedAddress(String moniker) throws QasException {
        String layout = QASEngine.getLayout();
        return QASEngine.getQas().getFormattedAddress(layout, moniker);
    }
    
    private static FormattedAddress findFormattedAddress(PicklistItem item, String refinementText) throws QasException {        
        Picklist picklist = QASEngine.getQas().refine(item.getMoniker(), refinementText);
		if (picklist == null || picklist.getItems() == null) {
		    return null;
		}
		if (picklist.getItems().length == 1) {
		    PicklistItem theItem = picklist.getItems()[0]; //expecting only one
		    return QASEngine.findFormattedAddress(theItem.getMoniker());
		} else {
		    return null;
		}
    }
    
	private static boolean mustRefine(PicklistItem item){
		return item.isIncompleteAddress() || item.isUnresolvableRange() || item.isPhantomPrimaryPoint();
	}
    
    public static String[] getServiceInfo() throws QasException {
        String[] sysInfo = QASEngine.getQas().getSystemInfo();
        //for (int i = 0; i < sysInfo.length; i++) {
        //    System.out.println("" + i + " " + sysInfo[i]);
        //}
        return sysInfo;
    }

}
