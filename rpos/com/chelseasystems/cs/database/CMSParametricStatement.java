/**
 * 
 */
package com.chelseasystems.cs.database;

import java.util.List;

import com.chelseasystems.cr.database.ParametricStatement;

/**
 * @author gupta
 *
 */
public class CMSParametricStatement extends ParametricStatement {

	/**
	 * @param sqlStatement
	 * @param parameters
	 */
	protected boolean caseSensitive=false;
	
	public CMSParametricStatement(String sqlStatement, List parameters) {
		super(sqlStatement, parameters);

	}
	
	public CMSParametricStatement(String sqlStatement, List parameters, boolean caseSensitive) {
		super(sqlStatement, parameters);
		this.caseSensitive = caseSensitive;

	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

}
