package com.chelseasystems.cr.swing;

import javax.swing.ImageIcon;
import java.io.*;
import java.awt.*;
import java.net.*;

/**
 * @author       Dan Reading
 */
public class CMSImageIcons {
	private static final CMSImageIcons CMSImageIconsSingleton = new CMSImageIcons();
	private static final java.util.HashMap hmIcons = new java.util.HashMap(300);

	public static CMSImageIcons getInstance() {
		return CMSImageIconsSingleton;
	}

	private ImageIcon getCachedImageIcon(String url) {
		ImageIcon icon = (ImageIcon) hmIcons.get(url);
		if (icon == null) {
			icon = createImageIcon(url);
			hmIcons.put(url, icon);
		}
		return icon;
	}

	private ImageIcon createImageIcon(String url) {
		if (System.getProperty("DEBUG") != null)
			System.out.println("CMSImageIcons.createImageIcon()->" + url);
		return new ImageIcon(Toolkit.getDefaultToolkit().createImage(getClass().getResource(url)));
	}

	public ImageIcon getOpen() {
		return getCachedImageIcon("/images/Open.gif");
	}

	public ImageIcon getAddElement() {
		return getCachedImageIcon("/images/AddElement.gif");
	}

	public ImageIcon getAddGroup() {
		return getCachedImageIcon("/images/AddGroup.gif");
	}

	public ImageIcon getAddReport() {
		return getCachedImageIcon("/images/AddReport.gif");
	}

	public ImageIcon getDeleteElement() {
		return getCachedImageIcon("/images/DeleteElement.gif");
	}

	public ImageIcon getDeleteGroup() {
		return getCachedImageIcon("/images/DeleteGroup.gif");
	}

	public ImageIcon getDeleteReport() {
		return getCachedImageIcon("/images/DeleteReport.gif");
	}

	public ImageIcon getBinocular() {
		return getCachedImageIcon("/images/Binocular.gif");
	}

	public ImageIcon getBlank20() {
		return getCachedImageIcon("/images/Blank20.gif");
	}

	public ImageIcon getPlugIn() {
		return getCachedImageIcon("/images/PlugIn.gif");
	}

	public ImageIcon getPlugOut() {
		return getCachedImageIcon("/images/PlugOut.gif");
	}

	public ImageIcon getSave() {
		return getCachedImageIcon("/images/Save.gif");
	}

	public ImageIcon getPrint() {
		return getCachedImageIcon("/images/Print.gif");
	}

	public ImageIcon getNew() {
		return getCachedImageIcon("/images/New.gif");
	}

	public ImageIcon getProperties() {
		return getCachedImageIcon("/images/HelpIndex.gif");
	}

	public ImageIcon getAbout() {
		return getCachedImageIcon("/images/About.gif");
	}

	public ImageIcon getClose() {
		return getCachedImageIcon("/images/Close.gif");
	}

	public ImageIcon getExit() {
		return getCachedImageIcon("/images/Exit.gif");
	}

	public ImageIcon getBook() {
		return getCachedImageIcon("/images/Book.gif");
	}

	public ImageIcon getEditBook() {
		return getCachedImageIcon("/images/EditBook.gif");
	}

	public ImageIcon getDocument() {
		return getCachedImageIcon("/images/Document.gif");
	}

	public ImageIcon getDocDiagram() {
		return getCachedImageIcon("/images/DocumentDiagram.gif");
	}

	public ImageIcon getFrames() {
		return getCachedImageIcon("/images/Frames.gif");
	}

	public ImageIcon getMenu() {
		return getCachedImageIcon("/images/Menu.gif");
	}

	public ImageIcon getMenuOpts() {
		return getCachedImageIcon("/images/MenuOpt.gif");
	}

	public ImageIcon getNewComponent() {
		return getCachedImageIcon("/images/NewComponent.gif");
	}

	public ImageIcon getGreenGear() {
		return getCachedImageIcon("/images/GreenGear.gif");
	}

	public ImageIcon getRedGear() {
		return getCachedImageIcon("/images/GearRed.gif");
	}

	public ImageIcon getGearwheel() {
		return getCachedImageIcon("/images/Gearwheel.gif");
	}

	public ImageIcon getLookAndFeel() {
		return getCachedImageIcon("/images/LookAndFeel.gif");
	}

	public ImageIcon getHammer() {
		return getCachedImageIcon("/images/Hammer.gif");
	}

	public ImageIcon getBegin() {
		return getCachedImageIcon("/images/Begin.gif");
	}

	public ImageIcon getEnd() {
		return getCachedImageIcon("/images/End.gif");
	}

	public ImageIcon getCaution() {
		return getCachedImageIcon("/images/Caution.gif");
	}

	public ImageIcon getDelete() {
		return getCachedImageIcon("/images/Delete.gif");
	}

	public ImageIcon getDeleteMulti() {
		return getCachedImageIcon("/images/DeleteMulti.gif");
	}

	public ImageIcon getDown() {
		return getCachedImageIcon("/images/Down.gif");
	}

	public ImageIcon getUp() {
		return getCachedImageIcon("/images/Up.gif");
	}

	public ImageIcon getDocDraw() {
		return getCachedImageIcon("/images/DocumentDraw.gif");
	}

	public ImageIcon getDocErase() {
		return getCachedImageIcon("/images/DocumentErase.gif");
	}

	public ImageIcon getDocDelete() {
		return getCachedImageIcon("/images/DeleteDocument.gif");
	}

	public ImageIcon getDocList() {
		return getCachedImageIcon("/images/List.gif");
	}

	public ImageIcon getNewMenu() {
		return getCachedImageIcon("/images/NewMenu.gif");
	}

	public ImageIcon getNewMenuOpt() {
		return getCachedImageIcon("/images/NewMenuOpt.gif");
	}

	public ImageIcon getGenComponent() {
		return getCachedImageIcon("/images/GenComponent.gif");
	}

	public ImageIcon getGenDoc() {
		return getCachedImageIcon("/images/GenDoc.gif");
	}

	public ImageIcon getGenReceipt() {
		return getCachedImageIcon("/images/GenReceipt.gif");
	}

	public ImageIcon getGenObject() {
		return getCachedImageIcon("/images/GenObject.gif");
	}

	public ImageIcon getGenApplet() {
		return getCachedImageIcon("/images/GenApplet.gif");
	}

	public ImageIcon getLaunchReceipt() {
		return getCachedImageIcon("/images/LaunchReceipt.gif");
	}

	public ImageIcon getLaunchMerch() {
		return getCachedImageIcon("/images/LaunchMerchAdmin.gif");
	}

	public ImageIcon getCalc() {
		return getCachedImageIcon("/images/Calculator.gif");
	}

	public ImageIcon getComputerOut() {
		return getCachedImageIcon("/images/ComputerOut.gif");
	}

	public ImageIcon getWorkstation() {
		return getCachedImageIcon("/images/Workstation.gif");
	}

	public ImageIcon getComponent() {
		return getCachedImageIcon("/images/Component.gif");
	}

	public ImageIcon getComponentView() {
		return getCachedImageIcon("/images/ViewComponent.gif");
	}

	public ImageIcon getComponentConnect() {
		return getCachedImageIcon("/images/ComponentConnection.gif");
	}

	public ImageIcon getGuage() {
		return getCachedImageIcon("/images/Gauge.gif");
	}

	public ImageIcon getLinegraph() {
		return getCachedImageIcon("/images/LineGraph.gif");
	}

	public ImageIcon getPiegraph() {
		return getCachedImageIcon("/images/PieGraph.gif");
	}

	public ImageIcon getPalette() {
		return getCachedImageIcon("/images/Palette.gif");
	}

	public ImageIcon getTrafficGreen() {
		return getCachedImageIcon("/images/TrafficGreen.gif");
	}

	public ImageIcon getTrafficRed() {
		return getCachedImageIcon("/images/TrafficRed.gif");
	}

	public ImageIcon getDuke() {
		return getCachedImageIcon("/images/Duke.gif");
	}

	public ImageIcon getExec() {
		return getCachedImageIcon("/images/Execute.gif");
	}

	public ImageIcon getQuestion() {
		return getCachedImageIcon("/images/Question.gif");
	}

	public ImageIcon getCurrConn() {
		return getCachedImageIcon("/images/Curr_conn.gif");
	}

	public ImageIcon getMaxConn() {
		return getCachedImageIcon("/images/Max_conn.gif");
	}

	public ImageIcon getMemoryWhite() {
		return getCachedImageIcon("/images/Memory_w.gif");
	}

	public ImageIcon getMemoryGray() {
		return getCachedImageIcon("/images/Memory_gy.gif");
	}

	public ImageIcon getZapTxn() {
		return getCachedImageIcon("/images/Zap_txn.gif");
	}

	public ImageIcon getGreenBall() {
		return getCachedImageIcon("/images/GreenBall.gif");
	}

	public ImageIcon getRedBall() {
		return getCachedImageIcon("/images/RedBall.gif");
	}

	public ImageIcon getYellowBall() {
		return getCachedImageIcon("/images/YellowBall.gif");
	}

	public ImageIcon getMail() {
		return getCachedImageIcon("/images/Mail.gif");
	}

	public ImageIcon getWorld() {
		return getCachedImageIcon("/images/World.gif");
	}

	public ImageIcon getTitlebar() {
		//return  getCachedImageIcon("/images/Titlebar_Retek.gif");
		return getCachedImageIcon("/images/Armani.gif");
	}

	public ImageIcon getVCRStop() {
		return getCachedImageIcon("/images/VCRStop.gif");
	}

	public ImageIcon getVCRPlay() {
		return getCachedImageIcon("/images/VCRPlay.gif");
	}

	public ImageIcon getVCRPause() {
		return getCachedImageIcon("/images/VCRPause.gif");
	}

	public ImageIcon getLeft() {
		return getCachedImageIcon("/images/Left.gif");
	}

	public ImageIcon getRight() {
		return getCachedImageIcon("/images/Right.gif");
	}

	public ImageIcon getAppletWizard() {
		return getCachedImageIcon("/images/AppletWizard.gif");
	}

	public ImageIcon getRuleWizard() {
		return getCachedImageIcon("/images/RuleWizard.gif");
	}

	public ImageIcon getResourceWizard() {
		return getCachedImageIcon("/images/ResourceWizard.gif");
	}

	public ImageIcon getCheck() {
		return getCachedImageIcon("/images/Check.gif");
	}

	public ImageIcon getBox() {
		return getCachedImageIcon("/images/Box.gif");
	}

	public ImageIcon getNewBox() {
		return getCachedImageIcon("/images/NewBox.gif");
	}

	public ImageIcon getClock() {
		return getCachedImageIcon("/images/Clock.gif");
	}

	public ImageIcon getCpuPct() {
		return getCachedImageIcon("/images/Cpu_pct.gif");
	}

	public ImageIcon getComponentMulti() {
		return getCachedImageIcon("/images/ComponentMulti.gif");
	}

	public ImageIcon getMemoryChip() {
		return getCachedImageIcon("/images/Memory_chip.gif");
	}

	public ImageIcon getMemoryPct() {
		return getCachedImageIcon("/images/Memory_pct.gif");
	}

	public ImageIcon getCoreObject() {
		return getCachedImageIcon("/images/CoreObject.gif");
	}

	public ImageIcon getBusinessObject() {
		return getCachedImageIcon("/images/BusinessObject.gif");
	}

	public ImageIcon getCMSObject() {
		return getCachedImageIcon("/images/CMSObject.gif");
	}

	public ImageIcon getReceipts() {
		return getCachedImageIcon("/images/Receipts.gif");
	}

	public ImageIcon getFooters() {
		return getCachedImageIcon("/images/Footers.gif");
	}

	public ImageIcon getNewFooter() {
		return getCachedImageIcon("/images/NewFooter.gif");
	}

	public ImageIcon getNewBlueprint() {
		return getCachedImageIcon("/images/NewBlueprint.gif");
	}

}
