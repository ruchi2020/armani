/*
 * @copyright (c) 2001 Chelsea Market Systems LLC
 */

package  com.chelseasystems.cs.swing.bean;

import java.util.ResourceBundle;
import  java.awt.BorderLayout;
import java.awt.Dimension;
import  java.awt.event.FocusEvent;
import  java.text.SimpleDateFormat;
import  com.chelseasystems.cr.util.StringFormat;
import  com.chelseasystems.cr.swing.CMSSwingConstants;
import com.chelseasystems.cr.swing.bean.JCMSMaskedTextField;

/**
 * Subclass to fix issue 1774 for Japan: Specifically override getDimension()
 * method for use in CustomerManagementApplet and it's components only. In general the
 * GridBagLayout should resize this component as needed. The JVM calls this method only 
 * when it is unable to get the size from the LayoutManager. The issue was not consistently
 * reproducible and occured only on some desktops(did not occur on laptops) in Japan.
 * 
 * @author Tim
 *
 */
public class JCMSMaskedTextField_JP extends JCMSMaskedTextField implements CMSSwingConstants
{
	private static final long serialVersionUID = 1L;
	
	 private int maskType = 0;
   private String phonePrefix;
   private String text;
   private ResourceBundle res = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
	
  public JCMSMaskedTextField_JP ()
  {
      super();
      this.setLayout(new BorderLayout());
      this.addFocusListener(new java.awt.event.FocusAdapter()
                            {
                                public void focusLost (FocusEvent e)
                                {
                                    myFocusLost(e);
                                }
                            });
  }

  /**
   * @param maskType
   */
  public void setMask (int maskType)
  {
      if(maskType <= MAX_VALID_MASK_VALUE && maskType >= 0)
          this.maskType = maskType;
  }

  /**
   * @return
   */
  public int getMask ()
  {
      return(maskType);
  }

  /**
   * @param phonePrefix
   */
// public void setPhonePrefix (String phonePrefix) {
//    this.phonePrefix = phonePrefix;
// }

  /**
   * @return
   */
// public String getPhonePrefix () {
//    return  phonePrefix;
// }

  /**
   * @param text
   */
  public void setText (String text)
  {
      this.text = text;
      switch(maskType)
      {
          case NO_MASK:default:
              super.setText(text);
              break;
          case SSN_MASK:
              super.setText(StringFormat.ssn(text));
              break;
//       case PHONE_MASK:
//          super.setText(StringFormat.phone(text));
//          break;
//       case PHONE_MASK_STORE:
//          super.setText(StringFormat.phone(text, phonePrefix));
//          break;
          case DATE_MASK:
              super.setText(dateFormat(text));
              break;
          case NDC_CODE_MASK:
              super.setText(StringFormat.ndccode(text));
              break;
      }
  }

  /**
   * @return  text before it was masked
   */
  public String getUnmaskedText ()
  {
      return(text);
  }

  /**
   * @param inText
   * @return
   */
  private String dateFormat (String inText)
  {
      SimpleDateFormat df = new SimpleDateFormat(res.getString("MM/dd/yyyy"));
      try
      {
          return(df.format(df.parse(inText)));
      }
      catch(Exception e)
      {
          return(inText);
      }
  }

  /**
   * @param e
   */
  public void myFocusLost (FocusEvent e)
  {
      setText(super.getText());
  }
	
  public Dimension getPreferredSize(){
	  if(super.getPreferredSize() == null )
		  return new Dimension(15,50);
	  return super.getPreferredSize();
  }


}