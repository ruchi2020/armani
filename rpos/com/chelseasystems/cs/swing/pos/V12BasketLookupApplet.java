package com.chelseasystems.cs.swing.pos;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.chelseasystems.cr.swing.CMSApplet;
import com.chelseasystems.cr.swing.bean.JCMSLabel;
import com.chelseasystems.cr.swing.bean.JCMSTextField;
import com.chelseasystems.cr.swing.event.CMSActionEvent;
import com.chelseasystems.cr.swing.layout.RolodexLayout;
import com.chelseasystems.cs.swing.menu.MenuConst;
import com.chelseasystems.cs.swing.model.V12BasketListModel;
import com.chelseasystems.cs.swing.panel.V12BasketLookupPanel;
import com.chelseasystems.cs.v12basket.CMSV12Basket;
import com.chelseasystems.cs.v12basket.CMSV12BasketHelper;

public class V12BasketLookupApplet extends CMSApplet {
	private static final long serialVersionUID = 1L;

	private V12BasketLookupPanel pnlV12BasketList;
	private JCMSLabel lblTimeStamp;
	private JCMSTextField txtTimeStamp;
	private JCMSLabel lblTransType;
	private JCMSTextField txtTransType;
	private JCMSLabel lblTotal;
	private JCMSTextField txtTotal;
	private JCMSLabel lblComment;
	private JCMSTextField txtComment;

	private RolodexLayout cardLayout;
	private RolodexLayout searchPanelCardLayout;

	private JPanel pnlSearch;
	private JPanel pnlMainScreen;

	private JPanel pnlMainScreenNorth;

	private CMSV12Basket cmsV12Basket;

	private CMSV12Basket cmsV12BasketInitial;
	private V12BasketListModel v12BasketListModel;

	CMSV12Basket[] v12Basket;

	// Construct the applet
	public V12BasketLookupApplet() {
	}

	@Override
	public String getScreenName() {
		return "V12 Basket";
	}

	@Override
	public String getVersion() {
		return ("$Revision: 1.57.2.21.4.25 $");
	}

	@Override
	public void init() {
		try {
			jbInit();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void jbInit() throws Exception {

		pnlV12BasketList = new V12BasketLookupPanel();
		pnlSearch = new JPanel();
		pnlMainScreenNorth = new JPanel();
		txtTimeStamp = new JCMSTextField();
		txtTransType = new JCMSTextField();
		txtTotal = new JCMSTextField();
		lblTimeStamp = new JCMSLabel();
		lblTransType = new JCMSLabel();
		lblTotal = new JCMSLabel();
		lblComment = new JCMSLabel();
		txtComment = new JCMSTextField();
		cardLayout = new RolodexLayout();
		searchPanelCardLayout = new RolodexLayout();
		pnlMainScreen = new JPanel();
		setLayout(cardLayout);
		pnlMainScreenNorth.setLayout(searchPanelCardLayout);
		GridBagLayout gridBagLayout1 = new GridBagLayout();
		BorderLayout borderLayout1 = new BorderLayout();
		pnlMainScreen.setLayout(borderLayout1);
		pnlMainScreen.add(pnlMainScreenNorth, BorderLayout.NORTH);
		pnlMainScreen.add(pnlV12BasketList, BorderLayout.CENTER);
		pnlSearch.setPreferredSize(new Dimension(833, 100));
		pnlSearch.setLayout(gridBagLayout1);
		lblTimeStamp.setLabelFor(txtTimeStamp);
		lblTimeStamp.setText(res.getString("TimeStamp"));
		lblTransType.setLabelFor(txtTransType);
		lblTransType.setText(res.getString("Trans Type"));
		lblTotal.setLabelFor(txtTotal);
		lblTotal.setText(res.getString("Total"));
		lblComment.setLabelFor(txtComment);
		lblComment.setText(res.getString("Comment"));

		this.setBackground(theAppMgr.getBackgroundColor());
		pnlSearch.setBackground(theAppMgr.getBackgroundColor());
		pnlMainScreen.setBackground(theAppMgr.getBackgroundColor());
		pnlV12BasketList.setAppMgr(theAppMgr);
		lblTimeStamp.setAppMgr(theAppMgr);
		lblTimeStamp.setFont(theAppMgr.getTheme().getLabelFont());
		txtTimeStamp.setAppMgr(theAppMgr);
		txtTimeStamp.setFont(theAppMgr.getTheme().getTextFieldFont());
		lblTransType.setAppMgr(theAppMgr);
		lblTransType.setFont(theAppMgr.getTheme().getLabelFont());
		txtTransType.setAppMgr(theAppMgr);
		txtTransType.setFont(theAppMgr.getTheme().getTextFieldFont());
		lblTotal.setAppMgr(theAppMgr);
		lblTotal.setFont(theAppMgr.getTheme().getLabelFont());
		txtTotal.setAppMgr(theAppMgr);
		txtTotal.setFont(theAppMgr.getTheme().getTextFieldFont());
		lblComment.setAppMgr(theAppMgr);
		lblComment.setFont(theAppMgr.getTheme().getLabelFont());
		txtComment.setAppMgr(theAppMgr);
		txtComment.setFont(theAppMgr.getTheme().getTextFieldFont());
		this.add(pnlMainScreen, "REGULAR_SEARCH");
		// this.add(pnlAdvanceSearch, "ADVANCED_SEARCH");
		pnlMainScreenNorth.add(pnlSearch, "SEARCH");

		pnlSearch.add(lblTotal, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(6, 6, 3, 0), 0, 0));
		pnlSearch.add(txtTotal, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(6, 6, 3, 0), 0, 0));

		pnlSearch.add(lblComment, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(3, 6, 3, 0), 0, 0));

		pnlSearch.add(txtComment, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				new Insets(3, 6, 3, 0), 0, 0));

		pnlSearch.add(lblTimeStamp, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
				new Insets(18, 6, 0, 0), 0, 0));
		pnlSearch.add(txtTimeStamp, new GridBagConstraints(1, 0, 2, 1, 1.0,
				0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(18, 6, 0, 0), 0, 0));

		pnlSearch.add(txtTransType, new GridBagConstraints(4, 0, 2, 1, 1.0,
				0.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(18, 6, 0, 6), 2, 0));
		pnlSearch.add(lblTransType, new GridBagConstraints(3, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE,
				new Insets(18, 6, 0, 0), 0, 0));
	}

	@Override
	public void start() {
		cmsV12Basket = (CMSV12Basket) theAppMgr
				.getStateObject("V12BASKET_LOOKUP");
		if (cmsV12Basket != null) {
			v12BasketListModel = new V12BasketListModel();
			cmsV12BasketInitial = (CMSV12Basket) cmsV12Basket.clone();
			loadBasket();
		}

		theAppMgr.showMenu(MenuConst.SELECTED_BASKET, theOpr);

	}

	@Override
	public void stop() {

	}

	private void loadBasket() {
		v12BasketListModel.clear();
		txtTimeStamp.setText(cmsV12BasketInitial.getTrnTimestamp().toString());
		txtTransType.setText(cmsV12BasketInitial.getTransactionType());
		txtTotal.setText(cmsV12BasketInitial.getTotalBasketAmount().toString());
		txtComment.setText(cmsV12BasketInitial.getTrnComment());
		v12BasketListModel.addV12Basket(cmsV12BasketInitial);
		populateV12BasketItems(cmsV12BasketInitial);
	}

	private void populateV12BasketItems(CMSV12Basket cmsV12Basket) {
		pnlV12BasketList.clear();
		List<String> itemList = cmsV12Basket.getItemList();
		CMSV12Basket[] basketItem = new CMSV12Basket[itemList.size()];
		int i = 0;
		ArrayList<String> addedItems = new ArrayList<String>();
		for (String itemId : itemList) {
			if (addedItems.contains(itemId))
				continue;
			addedItems.add(itemId);
			basketItem[i] = new CMSV12Basket();
			basketItem[i].setBarcode(itemId);
			basketItem[i].setItemPrice(cmsV12Basket.getItemPriceMap()
					.get(itemId).toString());
			basketItem[i].setItemQty(itemId, itemList);
			pnlV12BasketList.addV12Basket(basketItem[i]);
			i++;
		}

	}

	public void appButtonEvent(CMSActionEvent anEvent) {
		String sAction = anEvent.getActionCommand();
		if (sAction.equalsIgnoreCase("Resume")) {
			try {
				cmsV12BasketInitial = pnlV12BasketList.getSelectedV12Basket();
				Boolean isOpen = CMSV12BasketHelper.setBasketStatus(theAppMgr,
						cmsV12Basket, CMSV12Basket.inProcess);
				
				if (!isOpen) {
					theAppMgr.fireButtonEvent("PREV");
				} else {
					cmsV12Basket.setTrnStatus(CMSV12Basket.inProcess);
					theAppMgr.addStateObject("V12BASKET_INITIAL", cmsV12Basket);
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}
}
