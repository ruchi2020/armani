/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.panel;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.im.InputContext;
import java.util.Locale;

import com.chelseasystems.cr.appmgr.IApplicationManager;
import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.swing.CMSInputVerifier;
import com.chelseasystems.cr.swing.CMSSwingConstants;
import com.chelseasystems.cr.swing.IFocusTraversable;
import com.chelseasystems.cr.swing.IValidatable;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cr.swing.event.TextCompFocusListener;
import com.chelseasystems.cs.swing.CMSTextFilter;
import com.chelseasystems.cs.swing.bean.JCMSTextField_JP;
import com.chelseasystems.cs.util.Version;

/**
 * <p>Title:CustomerBasicPanel_JP.java </p>
 *
 * <p>Description: Displays customer's basic information </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: SkillNet Inc. </p>
 *
 * @author Sandhya Ajit
 * @version 1.0
 */
/*
 History:
 +------+------------+-----------+--------------------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #           | Description                                        |
 +------+------------+-----------+--------------------+----------------------------------------------------+
 | 1    | 03-06-2006 | Sandhya   | PCR1325, PCR1358   | Displays customer's basic information              |
 +------+------------+-----------+--------------------+----------------------------------------------------+
 */
public class CustomerBasicPanel_JP extends CustomerBasicPanel{

	static final long serialVersionUID = 0;

	/**
	 * Default constructor
	 */
	public CustomerBasicPanel_JP() {
		try {
			jbInit();
			setEnabled(false);
			reset();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Initialize and Layout components
	 * @throws Exception
	 */
	private void jbInit() throws Exception {
		java.util.ResourceBundle resBundle = com.chelseasystems.cr.util.ResourceManager.getResourceBundle();
		lblFirstNameJP = new JCMSLabel();
		txtFirstNameJP = new IMETextField();
		lblLastNameJP = new JCMSLabel();
		txtLastNameJP = new IMETextField();
		lblID = new JCMSLabel();
		txtID = getJCMSTextField();
		lblTitle = new JCMSLabel();
		lblFirstName = new JCMSLabel();
		lblMiddleName = new JCMSLabel();
		lblLastName = new JCMSLabel();
		lblSuffix = new JCMSLabel();
		txtTitle = getJCMSTextField();
		txtTitle.setName("Title");
		txtFirstName = new IMETextField();
		txtFirstName.setName("FIRST_NAME");
		txtMidName = getJCMSTextField();
		txtLastName = new IMETextField();
		txtLastName.setName("SECOND_NAME");
		txtSuffix = getJCMSTextField();
		txtSuffix.setName("SUFFIX");
		lblPrimaryEmail = new JCMSLabel();
		lblSecondEmail = new JCMSLabel();
		txtPrimaryEmail = getJCMSTextField();
		txt2ndEmail = getJCMSTextField();
		chbReturnMail = new JCMSCheckBox();
		chbReturnMail.setEnabled(false);
		GridBagLayout gridBagLayout1 = new GridBagLayout();
		this.setLayout(gridBagLayout1);
		//TD
		this.setPreferredSize(new Dimension(844, 155));
		lblTitle.setText(resBundle.getString("Title"));
		lblFirstName.setText(resBundle.getString("*First Name"));
		lblMiddleName.setText(resBundle.getString("Middle Name"));
		lblLastName.setText(resBundle.getString("*Last Name"));
		lblSuffix.setText(resBundle.getString("Suffix"));
		lblPrimaryEmail.setText(resBundle.getString("Primary Email/SMS Address"));
		lblSecondEmail.setText(resBundle.getString("2nd Email/SMS Address"));
		chbReturnMail.setHorizontalTextPosition(SwingConstants.LEADING);
		chbReturnMail.setText(resBundle.getString("Return Mail"));
		lblFirstNameJP.setText(resBundle.getString("First Name (JP)"));
		lblLastNameJP.setText(resBundle.getString("Last Name (JP)"));
		lblID.setText(resBundle.getString("ID"));
		txtID.setBorder(null);
		txtID.setEnabled(false);
		txtID.setEditable(false);
		this.add(txtPrimaryEmail, new GridBagConstraints(0, 6, 2, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 5, 5), 165, 0));
		this.add(lblPrimaryEmail, new GridBagConstraints(0, 5, 2, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 2, 3, 5), 22, -1));
		this.add(txt2ndEmail,     new GridBagConstraints(2, 6, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 5), 151, 0));
		this.add(lblSecondEmail,  new GridBagConstraints(2, 5, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 3, 5), 29, -1));
		this.add(chbReturnMail,   new GridBagConstraints(3, 5, 2, 2, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(9, 13, 12, 33), 21, -5));
		this.add(txtLastNameJP,   new GridBagConstraints(0, 4, 2, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 3, 5), 165, 0));
		this.add(txtFirstNameJP,  new GridBagConstraints(2, 4, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 3, 5), 151, 0));
		this.add(lblLastNameJP,   new GridBagConstraints(0, 3, 2, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 2, 3, 5), 90, -1));
		this.add(lblFirstNameJP,  new GridBagConstraints(2, 3, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(3, 0, 3, 5), 76, -1));
		this.add(txtLastName,     new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 2, 0, 5), 133, 0));
		this.add(lblLastName,     new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(7, 2, 3, 5), 78, -2));
		this.add(txtFirstName,    new GridBagConstraints(2, 2, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 137, 0));
		this.add(lblFirstName,    new GridBagConstraints(2, 1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(7, 0, 3, 5), 82, -2));
		this.add(lblID,           new GridBagConstraints(3, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(-5, 50, 0, 0), 7, 0));
		this.add(txtID,           new GridBagConstraints(4, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(-5, 0, 0, 0), 25, 5));
		// Set weights of all columns and rows to 1
		double[][] weights = gridBagLayout1.getLayoutWeights();
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < weights[i].length; j++) {
				weights[i][j] = 1;
			}
		}
		gridBagLayout1.columnWeights = weights[0];
		gridBagLayout1.rowWeights = weights[1];
		/**
		 * TextFilters Implemented
		 */
		// MSB 02/10/2006
		// To resolve Double byte issues
		// we need to make sure text isn't filtered
		// and only length restriction is imposed on input.
		txtPrimaryEmail.setDocument(new CMSTextFilter(40));
		txt2ndEmail.setDocument(new CMSTextFilter(40));

		// PCR1358 IME Control fix for Armani Japan
		txtLastName.setDocument(new CMSTextFilter(30));
		txtFirstName.setDocument(new CMSTextFilter(30));
		txtLastNameJP.setDocument(new CMSTextFilter(30));
		txtFirstNameJP.setDocument(new CMSTextFilter(30));

		txtLastName.addFocusListener(new TextFieldFocusAdapter(CMSApplet.theAppMgr.getEditAreaColor()));
		txtFirstName.addFocusListener(new TextFieldFocusAdapter(CMSApplet.theAppMgr.getEditAreaColor()));
		txtLastNameJP.addFocusListener(new TextFieldFocusAdapter(CMSApplet.theAppMgr.getEditAreaColor()));
		txtFirstNameJP.addFocusListener(new TextFieldFocusAdapter(CMSApplet.theAppMgr.getEditAreaColor()));
	} // End of jbInit

/*	public void focusGained(FocusEvent e) {
		// java.awt.im.InputSubset.HALFWIDTH_KATAKANA for single byte character 
		// java.awt.im.InputSubset.KANJI for double byte character 
		// following code is to set double byte characters as a defaut input when a textfield got focus 
		Character.Subset[] subsets = null;
		if (e.getSource() == txtLastNameJP || e.getSource() == txtFirstNameJP) {
			subsets = new Character.Subset[] { java.awt.im.InputSubset.KANJI };
		} else {
			subsets = new Character.Subset[] { java.awt.im.InputSubset.HALFWIDTH_KATAKANA };
		}

		JCMSTextField component = (JCMSTextField) (e.getSource());
		component.getInputContext().setCharacterSubsets(subsets);
	}

	public void focusLost(FocusEvent e) {
		// When focus is moved from a textfield, set back the IME control to English 
		Character.Subset[] subsets = null;
		JCMSTextField component = (JCMSTextField) (e.getSource());
		if (component.getInputContext() != null)
			component.getInputContext().setCharacterSubsets(subsets);
	}*/

	/**
	 * put your documentation comment here
	 * 
	 * @param bEnabled
	 */
	public void setEnabled(boolean bEnabled) {
		txtTitle.setEnabled(bEnabled);
		txtFirstName.setEnabled(bEnabled);
		txtMidName.setEnabled(bEnabled);
		txtLastName.setEnabled(bEnabled);
		txtSuffix.setEnabled(bEnabled);
		txtPrimaryEmail.setEnabled(bEnabled);
		txt2ndEmail.setEnabled(bEnabled);
		txtFirstNameJP.setEnabled(bEnabled);
		txtLastNameJP.setEnabled(bEnabled);
		chbReturnMail.setEnabled(false);
	}
	
	private JCMSTextField getJCMSTextField(){
		if("JP".equalsIgnoreCase(Version.CURRENT_REGION)){
			return new JCMSTextField_JP();
		}
		
		return  new JCMSTextField();
	}
	
	/**
	 * Private inner class to respond to text area focus changes
	 * Some handlers were moved from TextCompFocusListener
	 */
	private class TextFieldFocusAdapter extends FocusAdapter {

		   private Color idleBackground;

		   public TextFieldFocusAdapter () {
		      this(Color.white);
		   }

		   public TextFieldFocusAdapter (Color idleBackground) {
		      this.idleBackground = idleBackground;
		   }

		   public Color getIdleBackground () {
		      return  idleBackground;
		   }


		   public void setIdleBackground (Color idleBackground) {
		      this.idleBackground = idleBackground;
		   }


		public void focusGained(FocusEvent e) {			
			JTextComponent src = (JTextComponent)e.getSource();
		      src.setBackground(Color.white);
		      if (!(src instanceof JTextArea))
		         src.selectAll();
		      if (src.getParent() != null && src.getParent() instanceof JComponent)
		         ((JComponent)src.getParent()).scrollRectToVisible(new Rectangle(src.getParent().getLocation()));       //, new Dimension(src.getX(), src.getY()/2)));

		      Character.Subset[] subsets = null;
			if (e.getSource() == txtLastNameJP || e.getSource() == txtFirstNameJP) {
				subsets = new Character.Subset[] { java.awt.im.InputSubset.KANJI };
			} else {
				subsets = new Character.Subset[] { java.awt.im.InputSubset.HALFWIDTH_KATAKANA };
			}
			IMETextField component = (IMETextField) (e.getSource());
			InputContext ic = component.getInputContext();
			if (ic != null) {
				ic.selectInputMethod(new Locale("ja", "JP"));
				component.enableInputMethods(true);
				component.getInputContext().setCharacterSubsets(subsets);
			}
		}

		/**
		 * put your documentation comment here
		 * @param e
		 */
		public void focusLost(FocusEvent e) {
			((JTextComponent)e.getSource()).setBackground(idleBackground);
			//System.out.println("IN FOCUS LOST OF CUSTOMER LOOK UP...");

			/* When focus is moved from a textfield, set back the IME control to English */
			IMETextField component = (IMETextField) (e.getSource());
			if (component.getInputContext() != null) {
				component.getInputContext().setCharacterSubsets(null);
			}else {
				CMSApplet.theAppMgr.getAppletManager().getCurrentCMSApplet().getInputContext().setCharacterSubsets(null);
			}
		}
	}// End of TextFieldFocusAdaptor
	
	private class IMETextField extends JCMSTextField_JP
    implements IValidatable, IFocusTraversable, CMSSwingConstants {

		  private CMSInputVerifier verifier;

		  /**
		   * Contruct a new JCMSTextField.  This constrcutor sets the name to be TEXT,
		   * the font to be bold Helvetica point 12, black as the disabled text color
		   * and lastly calls setTextUI().
		   */
		  public IMETextField() {
		    super();
		  }

		  public InputContext getInputContext() {
			  if(this.getParent() != null){
				  return  this.getParent().getInputContext();
			  }
			  else {
				  return InputContext.getInstance();
			  }
		  }
		  
		  /**
		   * This method will set the background to be the Theme's edit area color
		   * as well as apply a FocusListener that will change the background white
		   * when this component has focus.  Also, the font will be set to the theme's
		   * text field font.
		   * @param theAppMgr
		   */
		  public void setAppMgr (IApplicationManager theAppMgr) {
		    if (theAppMgr != null) {
		      Color idleBackground = theAppMgr.getEditAreaColor();
		      this.setBackground(idleBackground);
		      this.setFont(theAppMgr.getTheme().getTextFieldFont());
		    }
		  }		  
		}
}
