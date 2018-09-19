/**
 * 
 */
package com.chelseasystems.cs.swing.bean;

import java.awt.Dimension;

import com.chelseasystems.cr.swing.bean.JCMSTextField;

/**
 * Subclass to fix issue 1774 for Japan: Specifically override getDimension()
 * method for use in CustomerManagementApplet and it's components only.
 * @author Tim
 *
 */
public class JCMSTextField_JP extends JCMSTextField {

	private static final long serialVersionUID = 1L;

	public JCMSTextField_JP() {
		super();
	}
	
	  public Dimension getPreferredSize(){
		  if(super.getPreferredSize() == null )
			  return new Dimension(15,50);
		  return super.getPreferredSize();
	  }

}
