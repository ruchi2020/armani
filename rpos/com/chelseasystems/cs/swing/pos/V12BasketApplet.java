package com.chelseasystems.cs.swing.pos;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cs.employee.CMSEmployee;
import com.chelseasystems.cs.store.CMSStore;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.swing.panel.V12BasketListPanel;
import com.chelseasystems.cs.v12basket.CMSV12Basket;
import com.chelseasystems.cs.v12basket.CMSV12BasketHelper;

public class V12BasketApplet extends CMSApplet {
	private static final long serialVersionUID = 1L;

	private CMSV12Basket cmsV12Basket;

	private boolean clickInProgress = false;

	private V12BasketListPanel pnlV12BasketList = new V12BasketListPanel();

	public V12BasketApplet() {
	}

	public void init() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		JPanel pnlNorth = new JPanel();
		JLabel lblTitle = new JLabel();
		pnlV12BasketList.setOpaque(false);
		pnlV12BasketList.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
		this.getContentPane().add(pnlNorth, BorderLayout.NORTH);
		this.getContentPane().add(pnlV12BasketList, BorderLayout.CENTER);
		pnlNorth.add(lblTitle, null);
		pnlNorth.setOpaque(false);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(theAppMgr.getTheme().getHeaderFont());
		this.setBackground(theAppMgr.getBackgroundColor());
		pnlV12BasketList.setAppMgr(theAppMgr);
		this.setBackground(theAppMgr.getBackgroundColor());
		pnlV12BasketList.setAppMgr(theAppMgr);
		pnlV12BasketList.addMouseListener(new MouseAdapter() {
			/**
			 * put your documentation comment here
			 *
			 * @param e
			 */
			public void mouseClicked(MouseEvent e) {
				clickEvent(e);
			}
		});
	}

	// Start the applet
	public void start() {
		clickInProgress = false;
		pnlV12BasketList.clear();
		theAppMgr.setTransitionColor(theAppMgr.getBackgroundColor());
		theOpr = (CMSEmployee) theAppMgr.getStateObject("OPERATOR");
		//model.clear();
		theAppMgr.showMenu(MenuConst.V12_BASKET, theOpr);
		theAppMgr.setSingleEditArea(res.getString("Select Basket."));
		loadV12Basket();
		theAppMgr.setEditAreaFocus();
	}

	// Stop the applet
	public void stop() {
	}

	/**
	 * @param anEvent
	 */

	/**
	 * Method loads parked transactions into the applet model. Method will first
	 * get local serialized txns, then retrieve others from peers. The local
	 * txns are retreived because when off-line, the host will not see them.
	 */
	private void loadV12Basket() {

		CMSV12Basket[] v12Basket = null;
		theAppMgr.setWorkInProgress(true);

		Date date = (Date) theAppMgr.getGlobalObject("PROCESS_DATE");
		String storeId = ((CMSStore) theAppMgr.getGlobalObject("STORE"))
				.getId();
		try {
			v12Basket = CMSV12BasketHelper.getBasketDetails(theAppMgr, date,
					storeId);
			if (v12Basket != null) {
				for (int i = 0; i < v12Basket.length; i++) {
					pnlV12BasketList.addBasket(v12Basket[i]);
				}
			} else {
				theAppMgr.showErrorDlg(res
						.getString("No V12 Basket is available!"));
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			theAppMgr.setWorkInProgress(false);
			pnlV12BasketList.repaint();
		}
	}

	public String getScreenName() {
		return (res.getString("V12 Basket"));
	}

	public String getVersion() {
		return ("$Revision: 1.1 $");
	}

	/**
	 * @param e
	 */
	public void clickEvent(MouseEvent e) {
		cmsV12Basket = pnlV12BasketList.getSelectedV12Basket();
		if (cmsV12Basket == null)
			return;

		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		if (!clickInProgress) {
			clickInProgress = true;
			theAppMgr.addStateObject("V12BASKET_LOOKUP", cmsV12Basket);
			theAppMgr.fireButtonEvent("BASKET_LOOKUP");
			this.setCursor(Cursor.getDefaultCursor());
		}

	}
	
	/**
	 * callback when page down is pressed
	 */
	public void pageDown(MouseEvent e) {
		pnlV12BasketList.nextPage();
		theAppMgr.showPageNumber(e, pnlV12BasketList.getCurrentPageNumber() + 1, pnlV12BasketList.getTotalPages());
	}

	/**
	 * callback when page up is pressed
	 */
	public void pageUp(MouseEvent e) {
		pnlV12BasketList.prevPage();
		theAppMgr.showPageNumber(e, pnlV12BasketList.getCurrentPageNumber() + 1, pnlV12BasketList.getTotalPages());
	}


}