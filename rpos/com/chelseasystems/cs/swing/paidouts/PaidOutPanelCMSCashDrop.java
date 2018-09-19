/*
 History:
 +------+------------+-----------+-----------+----------------------------------------------------+
 | Ver# | Date       | By        | Defect #  | Description                                        |
 +------+------------+-----------+-----------+----------------------------------------------------+
 | 1    | 04/19/2005 | Anand     | N/A       | Customizations as per Specifications               |
 --------------------------------------------------------------------------------------------------
 *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package com.chelseasystems.cs.swing.paidouts;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import com.chelseasystems.cs.paidout.CMSCashDropPaidOut;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cr.currency.*;
import com.chelseasystems.cr.swing.bean.*;
import com.chelseasystems.cr.paidout.PaidOutTransaction;
import com.chelseasystems.cr.swing.TextFilter;
import com.chelseasystems.cr.swing.JColorScrollBar;
import com.chelseasystems.cr.swing.event.TextCompFocusListener;
import com.chelseasystems.cr.rules.BusinessRuleException;
import com.chelseasystems.cr.payment.*;
import com.chelseasystems.cr.config.ConfigMgr;
import com.chelseasystems.cs.customer.CMSCustomer;


/**
 * put your documentation comment here
 */
public class PaidOutPanelCMSCashDrop extends PaidOutPanel {
  //    JCMSLabel lblTotalDrop = new JCMSLabel();
  //    JCMSTextField totalDropTextField = new JCMSTextField();
  JTabbedPane tabbedPane = new JTabbedPane();
  ConfigMgr config = new ConfigMgr("currency.cfg");
  CurrencyDisplayPanel[] currencyPanels;
  String baseArmCurrency = config.getString("BASE_CURRENCY_TYPE");

  /**
   * @param    PaidoutApplet owner
   * @param    String displayName
   */
  public PaidOutPanelCMSCashDrop(PaidoutApplet owner, String displayName) {
    super(owner, displayName);
    try {
      myInit();
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * put your documentation comment here
   */
  private void myInit() {
    Vector panels = new Vector();
    String[] currencies = getListFromConfig("SUPPORTED_CURRENCY", baseArmCurrency, ", ");
    for (int idx = 0; idx < currencies.length; idx++) {
      CurrencyDisplayPanel panel = new CurrencyDisplayPanel(currencies[idx], new TriggerSetTotalAmt());
      tabbedPane.addTab(owner.res.getString(config.getString(currencies[idx] + "_DESC")), panel);
      panels.add(panel);
    }
    currencyPanels = (CurrencyDisplayPanel[])panels.toArray(new CurrencyDisplayPanel[panels.size()]);
  }

  /**
   */
  public void jbInit() {
    JPanel allOfIt = new JPanel();
    JPanel tabbedPanel = new JPanel();
    Font font = owner.getAppMgr().getTheme().getLabelFont();
    tabbedPane.setFont(new Font(font.getName(), font.PLAIN, font.getSize() * 2));
    tabbedPane.setBackground(owner.getAppMgr().getBackgroundColor());
    tabbedPanel.setLayout(new BorderLayout());
    tabbedPanel.setBackground(owner.getAppMgr().getBackgroundColor());
    tabbedPanel.add(tabbedPane, BorderLayout.CENTER);
    tabbedPanel.add(acquireSpacerPanel(), BorderLayout.WEST);
    tabbedPanel.add(acquireTotalPanel(), BorderLayout.SOUTH);
    tabbedPanel.setPreferredSize(new Dimension((int)(580 * owner.r), (int)(150 * owner.r)));
    allOfIt.setBackground(owner.getAppMgr().getBackgroundColor());
    allOfIt.setLayout(new GridLayout(0, 1, 5, 5));
    allOfIt.setOpaque(false);
    allOfIt.add(tabbedPanel);
    allOfIt.setPreferredSize(new Dimension((int)(740 * owner.r), (int)(300 * owner.r)));
    this.setLayout(new BorderLayout());
    this.add(allOfIt, BorderLayout.CENTER);
    this.add(acquireSpacerPanel(), BorderLayout.EAST);
    this.add(acquireSpacerPanel(), BorderLayout.WEST);
    this.setPreferredSize(new Dimension((int)(580 * owner.r), (int)(160 * owner.r)));
  }

  /**
   * put your documentation comment here
   * @return
   */
  protected JPanel acquireSpacerPanel() {
    JPanel rtnVal = new JPanel();
    rtnVal.setOpaque(false);
    rtnVal.setLayout(new FlowLayout());
    rtnVal.setBackground(owner.getAppMgr().getBackgroundColor());
    return (rtnVal);
  }

  /**
   * @return
   */
  protected JPanel acquireTotalPanel() {
    JPanel rtnVal = new JPanel();
    rtnVal.setOpaque(false);
    rtnVal.setLayout(new BorderLayout());
    rtnVal.setBackground(owner.getAppMgr().getBackgroundColor());
    rtnVal.add(acquireSpacerPanel(), BorderLayout.NORTH);
    rtnVal.add(fabricateTotalPanel(), BorderLayout.SOUTH);
    return (rtnVal);
  }

  /**
   * @return
   */
  protected JPanel fabricateTotalPanel() {
    JPanel rtnVal = new JPanel();
    GridLayout layout = new GridLayout();
    layout.setVgap(10);
    layout.setHgap(5);
    layout.setColumns(6);
    layout.setRows(0);
    rtnVal.setLayout(layout);
    rtnVal.setOpaque(false);
    rtnVal.add(acquireSpacerPanel());
    rtnVal.add(acquireSpacerPanel());
    rtnVal.add(acquireSpacerPanel());
    rtnVal.add(acquireSpacerPanel());
    return (rtnVal);
  }

  /**
   * put your documentation comment here
   * @param key
   * @param initial
   * @param delim
   * @return
   */
  private String[] getListFromConfig(String key, String initial, String delim) {
    Vector rtnVal = new Vector();
    String values = config.getString(key);
    StringTokenizer toker = new StringTokenizer(values, delim);
    if (initial != null && !initial.equals("")) {
      rtnVal.add(initial);
    } while (toker.hasMoreTokens()) {
      String nextToke = toker.nextToken();
      if (initial != null) {
        if (!nextToke.equalsIgnoreCase(initial)) {
          rtnVal.add(nextToke);
        }
      } else {
        rtnVal.add(nextToke);
      }
    }
    return ((String[])rtnVal.toArray(new String[rtnVal.size()]));
  }

  /**
   * put your documentation comment here
   */
  public void initialFocus() {
    SwingUtilities.invokeLater(new Runnable() {

      /**
       */
      public void run() {
        clearFields();
        currencyPanels[0].getCurrencyDisplays()[0].getTextField().requestFocus();
      }
    });
  }

  /**
   */
  public void clearFields() {
    for (int idx = 0; idx < currencyPanels.length; idx++) {
      CurrencyDisplayPanel panel = currencyPanels[idx];
      CurrencyDisplay[] displays = panel.getCurrencyDisplays();
      for (int currIdx = 0; currIdx < displays.length; currIdx++) {
        displays[currIdx].getTextField().setText("");
      }
    }
    tabbedPane.setSelectedIndex(0);
  }

  /**
   * @param field
   * @param multiplier
   * @return
   */
  protected double deriveCurrencyValue(CurrencyDisplay display) {
    try {
      // MP: In case we change tab, the length < 0. Prev it was giving Number Format Exception.
      if (display.getTextField().getText().length() > 0) {
        return (new ArmCurrency(display.getTextField().getText().trim()).doubleValue()
            * display.getMultiplier());
      }
      return 0.0;
    } catch (Exception e) {
      e.printStackTrace();
      display.getTextField().setText("0");
      return (deriveCurrencyValue(display));
    }
  }

  /**
   * @param fromType
   * @param toType
   * @param amount
   * @return
   */
  protected double convertCurrency(String fromType, String toType, double amount) {
    try {
      if (fromType.equals(toType)) {
        return (amount);
      } else {
        return (new ArmCurrency(CurrencyType.getCurrencyType(fromType)
            , amount).convertTo(CurrencyType.getCurrencyType(toType)).round().doubleValue());
      }
    } catch (Exception e) {
      return (0d);
    }
  }

  /**
   * put your documentation comment here
   * @return
   */
  private double setTotalAmt() {
    double rtnVal = 0;
    for (int idx = 0; idx < currencyPanels.length; idx++) {
      CurrencyDisplayPanel panel = currencyPanels[idx];
      CurrencyDisplay[] displays = panel.getCurrencyDisplays();
      for (int currIdx = 0; currIdx < displays.length; currIdx++) {
        rtnVal +=
            convertCurrency(displays[currIdx].getISOCode(), baseArmCurrency
            , deriveCurrencyValue(displays[currIdx]));
      }
    }
    return (rtnVal);
  }

  /**
   * @return
   * @exception BusinessRuleException
   */
  public PaidOutTransaction exportData()
      throws BusinessRuleException {

// Issue # 1115
    for (int idx = 0; idx < currencyPanels.length; idx++) {
      AmountTxtFld amtFld = currencyPanels[idx].getCurrencyDisplays()[0].getTextField();
      ArmCurrency amount = new ArmCurrency(amtFld.getText().trim());
      if (amount.doubleValue() < 0.00) {
        amtFld.requestFocus();
        owner.getAppMgr().showErrorDlg(owner.res.getString("Amount must be a value greater than zero"));
        return (null);
      }
    }
    if(setTotalAmt() == 0)
    {
        currencyPanels[0].getCurrencyDisplays()[0].getTextField().requestFocus();
        owner.getAppMgr().showErrorDlg(owner.res.getString("Amount must be a value greater than zero"));
        return(null);
    }

    CMSCashDropPaidOut txn = new CMSCashDropPaidOut("CASH_TRANSFER", (CMSStore)owner.theStore);
    try {
      for (int idx = 0; idx < currencyPanels.length; idx++) {
        CurrencyDisplayPanel panel = currencyPanels[idx];
        CurrencyDisplay[] displays = panel.getCurrencyDisplays();
        for (int currIdx = 0; currIdx < displays.length; currIdx++) {
          txn.addCurrencyDrop(displays[currIdx].getISOCode(), displays[currIdx].getLabel().getText()
              , deriveCurrencyValue(displays[currIdx]));
        }
      }
      if (setTotalAmt() > 0) {
        //amount = amount.multiply(-1);
        //txn.setAmount(amount);
        txn.setAmount(new ArmCurrency(-1 * setTotalAmt()));
        txn.setComment(""); //Comments are not required because the reason is understood
        for (int idx = 0; idx < currencyPanels.length; idx++) {
          CurrencyDisplayPanel panel = currencyPanels[idx];
          CurrencyDisplay[] displays = panel.getCurrencyDisplays();
          for (int currIdx = 0; currIdx < displays.length; currIdx++) {
            try {
              double amountStr = deriveCurrencyValue(displays[currIdx]);
              if (amountStr > 0) {
                if (displays[currIdx] == null) {}
                Payment payment = PaymentMgr.getPayment(displays[currIdx].getKey(), true);
                ArmCurrency currency = new ArmCurrency(CurrencyType.getCurrencyType(displays[currIdx].
                    getISOCode())
                    , ( -1 * amountStr)).convertTo(CurrencyType.getCurrencyType(baseArmCurrency));
                    payment.setAmount(currency);
                    txn.addPayment(payment);
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
      }

      CMSCustomer cmsCustomer= (CMSCustomer)owner.getAppMgr().getStateObject("TXN_CUSTOMER");
      txn.setCustomer(cmsCustomer);
      return (txn);
    } catch (NumberFormatException nfe) {
      return (null);
    }
  }

  public class TriggerSetTotalAmt extends FocusAdapter {

    /**
     * put your documentation comment here
     * @param e
     */
    public void focusLost(FocusEvent e) {
      setTotalAmt();
    }
  }


  public class CurrencyDisplayPanel extends JPanel {
    private String code;
    private int columns;
    private int rows;
    private CurrencyDisplay[] currencies;
    private FocusListener focusList;

    /**
     * put your documentation comment here
     * @param     String code
     * @param     FocusListener focusList
     */
    public CurrencyDisplayPanel(String code, FocusListener focusList) {
      this.code = code;
      this.focusList = focusList;
      columns = Integer.parseInt(config.getString(code + "_COLUMNS"));
      rows = Integer.parseInt(config.getString(code + "_ROWS"));
      constructPanel();
    }

    /**
     * put your documentation comment here
     * @return
     */
    public CurrencyDisplay[] getCurrencyDisplays() {
      return (currencies);
    }

    /**
     * put your documentation comment here
     */
    protected void constructPanel() {
      setOpaque(false);
      setLayout(new BorderLayout());
      setBackground(owner.getAppMgr().getBackgroundColor());
      setPreferredSize(new Dimension((int)(580 * owner.r), (int)(160 * owner.r)));
      add(acquireSpacerPanel(), BorderLayout.NORTH);
      add(fabricateCurrencyPanel(), BorderLayout.CENTER);
      add(acquireSpacerPanel(), BorderLayout.SOUTH);
      add(acquireSpacerPanel(), BorderLayout.EAST);
      add(acquireSpacerPanel(), BorderLayout.WEST);
    }

    /**
     * put your documentation comment here
     * @return
     */
    protected JPanel fabricateCurrencyPanel() {
      JPanel rtnVal = new JPanel();
//merged code with JP. No changes in Euro
      //currencies = new CurrencyDisplay[1];
      currencies = new CurrencyDisplay[columns * rows];
      GridLayout layout = new GridLayout();
      layout.setVgap(10);
      layout.setHgap(5);
      layout.setColumns(columns);
      layout.setRows(rows);
      rtnVal.setLayout(layout);
      rtnVal.setOpaque(false);
      for (int idx = 0; idx < columns * rows; idx++) {
        // if(idx < denoms.length)
        {
          String label = owner.res.getString(config.getString(code + "_DESC"));
          double multiplier = Double.parseDouble(config.getString(code + "_MULTIPLIER"));
          String key = config.getString(code + "_KEY");
          CurrencyDisplay display = new CurrencyDisplay(code, label, multiplier, key);
          display.setFocusListener(focusList);
          currencies[idx] = display;
          rtnVal.add(display);
        }
      }
      return (rtnVal);
    }
  }


  public class CurrencyDisplay extends JPanel {
    private JCMSLabel label = new JCMSLabel();
    private AmountTxtFld textfield = new AmountTxtFld(owner.getAppMgr());
    private String code = "";
    private int fieldSize = 0;
    private double multiplier = 0.0;
    private String key;

    /**
     * put your documentation comment here
     * @param     String code
     * @param     String labelText
     * @param     double multiplier
     * @param     String key
     */
    public CurrencyDisplay(String code, String labelText, double multiplier, String key) {
      this.code = code;
      this.multiplier = multiplier;
      this.key = key;
      JPanel jPanel1 = new JPanel();
      JPanel jPanel2 = new JPanel();
      JPanel jPanel3 = new JPanel();
      JLabel jLabel3 = new JLabel();
      FlowLayout flowLayout1 = new FlowLayout();
      JLabel jLabel2 = new JLabel();
      JPanel jPanel5 = new JPanel();
      JPanel jPanel6 = new JPanel();
      JPanel jPanel8 = new JPanel();
      this.setLayout(new BorderLayout());
      jPanel1.setBackground(owner.getAppMgr().getBackgroundColor());
      jPanel1.setLayout(new BorderLayout());
      jPanel2.setBackground(owner.getAppMgr().getBackgroundColor());
      jPanel2.setLayout(new BorderLayout());
      jPanel3.setOpaque(false);
      jPanel3.setLayout(flowLayout1);
      textfield.setFont(owner.theAppMgr.getTheme().getTextFieldFont());
      jLabel3.setFont(owner.theAppMgr.getTheme().getLabelFont());
      jLabel2.setFont(owner.theAppMgr.getTheme().getLabelFont());
      flowLayout1.setAlignment(0);
      jLabel2.setVerticalAlignment(3);
      jPanel5.setOpaque(false);
      jPanel6.setOpaque(false);
      jPanel8.setOpaque(false);
      jLabel3.setVerticalAlignment(3);
      jLabel3.setText(owner.res.getString("Amount") + ":");
      this.setPreferredSize(new Dimension((int)(580 * owner.r), (int)(160 * owner.r)));
      jPanel2.setPreferredSize(new Dimension((int)(10 * owner.r), (int)(75 * owner.r)));
      jPanel3.setPreferredSize(new Dimension((int)(10 * owner.r), (int)(20 * owner.r)));
      textfield.setPreferredSize(new Dimension((int)(170 * owner.r), (int)(25 * owner.r)));
      jLabel2.setPreferredSize(new Dimension((int)(740 * owner.r), (int)(15 * owner.r)));
      jPanel6.setPreferredSize(new Dimension((int)(210 * owner.r), (int)(20 * owner.r)));
      jPanel8.setPreferredSize(new Dimension((int)(340 * owner.r), (int)(10 * owner.r)));
      jLabel3.setPreferredSize(new Dimension((int)(170 * owner.r), (int)(15 * owner.r)));
      this.add(jPanel1, BorderLayout.CENTER);
      jPanel1.add(jPanel6, BorderLayout.NORTH);
      jPanel6.add(jLabel2, null);
      jPanel1.add(jPanel5, BorderLayout.CENTER);
      this.add(jPanel2, BorderLayout.NORTH);
      jPanel2.add(jPanel3, BorderLayout.NORTH);
      jPanel2.add(jPanel8, BorderLayout.WEST);
      jPanel8.add(jLabel3, null);
      jPanel8.add(textfield, null);
    }

    /**
     * put your documentation comment here
     * @param focus
     */
    public void setFocusListener(FocusListener focus) {
      textfield.addFocusListener(focus);
    }

    /**
     * put your documentation comment here
     * @return
     */
    public JCMSLabel getLabel() {
      return (label);
    }

    /**
     * put your documentation comment here
     * @return
     */
    public AmountTxtFld getTextField() {
      return (textfield);
    }

    /**
     * put your documentation comment here
     * @return
     */
    public String getISOCode() {
      return (code);
    }

    /**
     * put your documentation comment here
     * @return
     */
    public double getMultiplier() {
      return (multiplier);
    }

    /**
     * put your documentation comment here
     * @return
     */
    public String getKey() {
      return (key);
    }

    /**
     * put your documentation comment here
     */
    public void setFocus() {
      SwingUtilities.invokeLater(new Runnable() {

        /**
         * put your documentation comment here
         */
        public void run() {
          textfield.requestFocus();
        }
      });
    }
  }
}

