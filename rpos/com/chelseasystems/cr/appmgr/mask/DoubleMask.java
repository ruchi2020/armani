/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 */


package  com.chelseasystems.cr.appmgr.mask;

import  com.chelseasystems.cr.appmgr.IApplicationManager;


/**
 */
public class DoubleMask extends MaskGenericRenderer implements IMask {

   private IApplicationManager theAppMgr;
   java.util.ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();

   /**
    * @param theAppMgr
    * @param edtValue
    * @return
    */
   public Object validateMask (IApplicationManager theAppMgr, Object edtValue) {
      this.theAppMgr = theAppMgr;
      String edtString = edtValue.toString();
      return  testMask(edtString) ? new Double(edtString) : null;
   }

   /**
    * @param value
    * @return
    */
   private boolean testMask (String value) {
      try {
         double result = Double.parseDouble(value);
         if (result < 0.0) {
            theAppMgr.showErrorDlg(res.getString("Negative numbers are not allowed."));
            return  false;
         }
         return  true;
      } catch (Exception ex) {
         theAppMgr.showErrorDlg(res.getString("The value is not a number."));
         return  false;
      }
   }

   /**
    * @param initialValue
    * @return
    */
   public boolean validateInitialValueType (Object initialValue) {
      if (initialValue instanceof Double)
         return  true;
      else {
         try {
            Double.parseDouble(initialValue.toString());
            return  true;
         } catch (NumberFormatException ex) {
            return  false;
         }
      }
   }
}



