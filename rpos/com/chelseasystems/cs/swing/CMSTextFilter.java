package com.chelseasystems.cs.swing;


import javax.swing.text.*;
/**
 * <p>Title:CMSTextFilter.java </p>
 *
 * <p>Description: Restricts number of Characters in a text field </p>
 *
 * <p>Copyright: Copyright Skillnet Solutions Inc.(c) 2006</p>
 *
 * <p>Company: Skillnet Solutions Inc.</p>
 *
 * @author Manpreet S Bawa.
 * @version 1.0
 */
public class CMSTextFilter extends PlainDocument {
    private int iLimitChars=-1;

    public CMSTextFilter(int iLimit) {
        iLimitChars = iLimit;
    }

    public void insertString
      (int offset, String  str, AttributeSet attr)
        throws BadLocationException {
     if (str == null) return;

     if ((getLength() + str.length()) <= iLimitChars) {
       super.insertString(offset, str, attr);
       }
   }
}
