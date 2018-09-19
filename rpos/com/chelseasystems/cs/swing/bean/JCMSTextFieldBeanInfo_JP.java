/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 */

package  com.chelseasystems.cs.swing.bean;

import  java.beans.*;

/**
 */
public class JCMSTextFieldBeanInfo_JP extends SimpleBeanInfo {
   private Class beanClass = JCMSTextField_JP.class;
   private String iconColor16x16Filename = "jcmstextfield16.GIF";
   private String iconColor32x32Filename = "jcmstextfield32.GIF";

   /**
    */
   public JCMSTextFieldBeanInfo_JP () {
   }

   /**
    * @return
    */
   public PropertyDescriptor[] getPropertyDescriptors () {
      try {
         PropertyDescriptor _appMgr = new PropertyDescriptor("appMgr", beanClass,
               null, "setAppMgr");
         PropertyDescriptor _verifier = new PropertyDescriptor("verifier",
               beanClass, "getVerifier", "setVerifier");
         PropertyDescriptor[] pds = new PropertyDescriptor[] {
            _appMgr, _verifier
         };
         return  pds;
      } catch (IntrospectionException ex) {
         ex.printStackTrace();
         return  null;
      }
   }

   /**
    * @param iconKind
    * @return
    */
   public java.awt.Image getIcon (int iconKind) {
      switch (iconKind) {
         case BeanInfo.ICON_COLOR_16x16:
            return  loadImage(iconColor16x16Filename);
         default:
            return  loadImage(iconColor32x32Filename);
      }
   }

   /**
    * @return
    */
   public BeanInfo[] getAdditionalBeanInfo () {
      Class superclass = beanClass.getSuperclass();
      try {
         BeanInfo superBeanInfo = Introspector.getBeanInfo(superclass);
         return  new BeanInfo[] {
            superBeanInfo
         };
      } catch (IntrospectionException ex) {
         ex.printStackTrace();
         return  null;
      }
   }
}
