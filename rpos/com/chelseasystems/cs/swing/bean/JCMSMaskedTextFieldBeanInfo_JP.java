/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package  com.chelseasystems.cs.swing.bean;

import  java.beans.*;


/**
 */
public class JCMSMaskedTextFieldBeanInfo_JP extends SimpleBeanInfo {
   private Class beanClass = JCMSMaskedTextField_JP.class;
   private String iconColor16x16Filename = "jcmsmaskedtextfield16.GIF";
   private String iconColor32x32Filename = "jcmsmaskedtextfield32.GIF";

   /**
    */
   public JCMSMaskedTextFieldBeanInfo_JP () {
   }

   /**
    * @return
    */
   public PropertyDescriptor[] getPropertyDescriptors () {
      try {
         PropertyDescriptor _mask = new PropertyDescriptor("mask", beanClass,
               "getMask", "setMask");
         PropertyDescriptor _store = new PropertyDescriptor("store", beanClass,
               "getStore", "setStore");
         PropertyDescriptor _text = new PropertyDescriptor("text", beanClass,
               null, "setText");
         PropertyDescriptor[] pds = new PropertyDescriptor[] {
            _mask, _store, _text
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
