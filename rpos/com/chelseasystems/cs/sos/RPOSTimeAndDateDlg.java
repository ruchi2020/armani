package com.chelseasystems.cs.sos;

import java.sql.SQLException;
import java.util.Date;

import com.armani.reports.ArmReceiptDocManager;
import com.chelseasystems.cr.transaction.CommonTransaction;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cs.fiscaldocument.FiscalInterface;
import com.chelseasystems.cr.logging.LoggingServices;


/**
 * @author Aguiari_S
 *
 * Per modificare il modello associato al commento di questo tipo generato, aprire
 * Finestra&gt;Preferenze&gt;Java&gt;Generazione codice&gt;Codice e commenti
 */
public class RPOSTimeAndDateDlg {

  /**
   *
   */

  public RPOSTimeAndDateDlg() {
    ConfigMgr configMgr = new ConfigMgr("fiscal_document.cfg");
    try {
      String sClassName = configMgr.getString("FISCAL_DOCUMENT_PRINTER");
      Class cls = Class.forName(sClassName);
      FiscalInterface fiscalPrinterInterface = (FiscalInterface)cls.newInstance();
      fiscalPrinterInterface.setSystemAndFiscalDate(new Date());
    } catch (Exception e) {
      LoggingServices.getCurrent().logMsg(getClass().getName(), "execute"
          , "Setting System Fiscal Data and Time Failed, see exception.", "N/A", LoggingServices.MAJOR, e);    }
  }

}
