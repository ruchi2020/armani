/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */

package com.chelseasystems.cs.config;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: SkillNet Inc</p>
 * @author Sandhya Ajit
 * @version 1.0
 */

import com.chelseasystems.cr.business.BusinessObject;

/**
 * put your documentation comment here
 */
public class ArmDiscountRule extends BusinessObject {
	private static final long serialVersionUID = 1L;

	private String cdDsc;
	private Double startRange;
	private Double endRange;
	private Boolean isDscPercent;
	private Double percent;
	private Double moDsc;
	private String cdNote;
	private String edCo;
	private String edLa;

	/**
	 * put your documentation comment here
	 */
	public ArmDiscountRule() {
		cdDsc = new String();
		startRange = new Double(0.00);
		endRange = new Double(0.00);
		isDscPercent = new Boolean(false);
		percent = new Double(0.00);
		moDsc = new Double(0.00);
		cdNote = new String();
		edCo = new String();
		edLa = new String();
	}

	/**
	 * put your documentation comment here
	 * @param cdDsc
	 */
	public void setCdDsc(String sConfigType) {
		if (cdDsc == null) {
			return;
		}
		doSetCdDsc(cdDsc);
	}

	/**
	 * put your documentation comment here
	 * @param cdDsc
	 */
	public void doSetCdDsc(String sCdDsc) {
		this.cdDsc = sCdDsc;
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public String getCdDsc() {
		return cdDsc;
	}

	/**
	 * put your documentation comment here
	 * @param startRange
	 */
	public void setStartRange(Double startRange) {
		if (startRange == null) {
			return;
		}
		doSetStartRange(startRange);
	}

	/**
	 * put your documentation comment here
	 * @param startRange
	 */
	public void doSetStartRange(Double startRange) {
		this.startRange = startRange;
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public Double getStartRange() {
		return startRange;
	}

	/**
	 * put your documentation comment here
	 * @param endRange
	 */
	public void setEndRange(Double endRange) {
		if (endRange == null) {
			return;
		}
		doSetEndRange(endRange);
	}

	/**
	 * put your documentation comment here
	 * @param endRange
	 */
	public void doSetEndRange(Double endRange) {
		this.endRange = endRange;
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public Double getEndRange() {
		return endRange;
	}

	/**
	 * put your documentation comment here
	 * @param isDscPercent
	 */
	public void setIsDscPercent(Boolean isDscPercent) {
		if (isDscPercent == null) {
			return;
		}
		doSetIsDscPercent(isDscPercent);
	}

	/**
	 * put your documentation comment here
	 * @param isDscPercent
	 */
	public void doSetIsDscPercent(Boolean isDscPercent) {
		this.isDscPercent = isDscPercent;
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public Boolean getIsDscPercent() {
		return isDscPercent;
	}

	/**
	 * put your documentation comment here
	 * @param percent
	 */
	public void setPercent(Double percent) {
		if (percent == null) {
			return;
		}
		doSetPercent(percent);
	}

	/**
	 * put your documentation comment here
	 * @param percent
	 */
	public void doSetPercent(Double percent) {
		this.percent = percent;
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public Double getPercent() {
		return percent;
	}

	/**
	 * put your documentation comment here
	 * @param moDsc
	 */
	public void setMoDsc(Double moDsc) {
		if (moDsc == null) {
			return;
		}
		doSetMoDsc(moDsc);
	}

	/**
	 * put your documentation comment here
	 * @param moDsc
	 */
	public void doSetMoDsc(Double moDsc) {
		this.moDsc = moDsc;
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public Double getMoDsc() {
		return moDsc;
	}

	/**
	 * put your documentation comment here
	 * @param cdNote
	 */
	public void setCdNote(String cdNote) {
		if (cdNote == null) {
			return;
		}
		doSetCdNote(cdNote);
	}

	/**
	 * put your documentation comment here
	 * @param cdNote
	 */
	public void doSetCdNote(String cdNote) {
		this.cdNote = cdNote;
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public String getCdNote() {
		return cdNote;
	}

	/**
	 * put your documentation comment here
	 * @param edCo
	 */
	public void setEdCo(String edCo) {
		if (edCo == null) {
			return;
		}
		doSetEdCo(edCo);
	}

	/**
	 * put your documentation comment here
	 * @param edCo
	 */
	public void doSetEdCo(String edCo) {
		this.edCo = edCo;
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public String getEdCo() {
		return edCo;
	}

	/**
	 * put your documentation comment here
	 * @param edLa
	 */
	public void setEdLa(String edLa) {
		if (edLa == null) {
			return;
		}
		doSetEdLa(edLa);
	}

	/**
	 * put your documentation comment here
	 * @param edLa
	 */
	public void doSetEdLa(String edLa) {
		this.edLa = edLa;
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public String getEdLa() {
		return edLa;
	}
}
