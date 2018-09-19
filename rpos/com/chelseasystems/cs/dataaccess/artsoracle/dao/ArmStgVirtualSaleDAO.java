package com.chelseasystems.cs.dataaccess.artsoracle.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.chelseasystems.cr.database.DatabaseNull;
import com.chelseasystems.cr.database.ParametricStatement;
import com.chelseasystems.cr.database.connection.ConnectionPool;
import com.chelseasystems.cs.dataaccess.DAOConnectionPool;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.ArmStgVirtualSaleBean;
import com.chelseasystems.cs.dataaccess.artsoracle.databean.BaseOracleBean;
import com.chelseasystems.cs.v12basket.CMSV12Basket;

public class ArmStgVirtualSaleDAO extends BaseOracleDAO implements
		ArmStgVirtualDAO {
	/*
	 * Retrieves the data by using date and storeId
	 */
	public CMSV12Basket[] getBasketDetails(Date date, String storeId)
			throws SQLException {
		String sSelectSQL = ArmStgVirtualSaleBean.selectSql
				+ where("to_char(" + ArmStgVirtualSaleBean.COL_TRN_TIMESTAMP
						+ ",'yyyy-mm-dd')", ArmStgVirtualSaleBean.COL_STORE_ID,
						ArmStgVirtualSaleBean.COL_TRN_STATUS);
		List<Object> params = new ArrayList<Object>();
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		String todayDate = sqlDate.toString();
		params.add(todayDate);
		params.add(storeId);
		params.add("OPEN");
		CMSV12Basket[] basketSale = fromBeansToObjects(query(
				new ArmStgVirtualSaleBean(), sSelectSQL, params));
		return basketSale;
	}

	private CMSV12Basket[] fromBeansToObjects(BaseOracleBean[] beans) {
		if (beans == null || beans.length == 0) {
			return null;
		} else {
			String virtualId = "";
			ArmStgVirtualSaleBean bean;
			HashMap<String, CMSV12Basket> basketMap = new HashMap<String, CMSV12Basket>();
			for (int i = 0; i < beans.length; i++) {
				bean = (ArmStgVirtualSaleBean) beans[i];
				virtualId = bean.getIdVirtualSale();
				if (basketMap.containsKey(virtualId)) {
					CMSV12Basket basket = basketMap.get(virtualId);
					basket.addItem(bean.getBarcode());
					basket.addItemPrice(bean.getBarcode(), bean.getItemPrice());
					basketMap.put(virtualId, basket);
				} else {
					CMSV12Basket basket = new CMSV12Basket();
					basket.setMobileRegisterId(bean.getMobileRegisterId());
					basket.setStoreId(bean.getStoreId());
					basket.setCustomerId(bean.getCustomerId());
					basket.addItem(bean.getBarcode());
					basket.addItemPrice(bean.getBarcode(), bean.getItemPrice());
					basket.setBarcode(bean.getBarcode());
					basket.setEmployeeId(bean.getEmployeeId());
					basket.setTransactionType(bean.getTransactionType());
					basket.setTrnTimestamp(bean.getTrnTimestamp());
					basket.setTrnComment(bean.getTrnComment());
					basket.setIdVirtualSale(bean.getIdVirtualSale());
					basket.setTrnStatus(bean.getTrnStatus());
					basket.setRowNumberId(bean.getRowNumId());
					basketMap.put(virtualId, basket);
				}
			}
			return (CMSV12Basket[]) basketMap.values().toArray(
					new CMSV12Basket[0]);
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param baseBean
	 * @return
	 * @throws SQLException
	 */

	public boolean setBasketStatus(CMSV12Basket cmsV12Basket, String setStatus)
			throws SQLException {
		int number = executeUpdate(getUpdateBasketSQL(cmsV12Basket, setStatus));
		return number >= 1; 
	}

	public ParametricStatement getUpdateBasketSQL(CMSV12Basket cmsV12Basket,
			String status) throws SQLException {
		List<String> params = new ArrayList<String>();
		String sUpdateSQL = "UPDATE " + ArmStgVirtualSaleBean.TABLE_NAME
				+ " SET ";
		sUpdateSQL += ArmStgVirtualSaleBean.COL_TRN_STATUS + " = ? ";
		params.add(status);
		sUpdateSQL += where(ArmStgVirtualSaleBean.COL_ID_VIRTUAL_SALE,
				ArmStgVirtualSaleBean.COL_TRN_STATUS);
		params.add(cmsV12Basket.getIdVirtualSale());
		params.add(cmsV12Basket.getTrnStatus());
		return new ParametricStatement(sUpdateSQL, params);
	}
	
	  /**
	   * put your documentation comment here
	   * @param statements
	   * @exception SQLException
	   */
	  public int executeUpdate (ParametricStatement statement) throws SQLException {
	    Connection connection = null;
	    PreparedStatement pStatement = null;
	    int number = 0;
	    ConnectionPool connectionPool = DAOConnectionPool.getPool();
	    try {
	        connection = connectionPool.getConnection();
	        this.debug("Executing : ", statement);
	        pStatement = this.setupPreparedStatement(connection, statement);
	        number = pStatement.executeUpdate();
	        pStatement.close();
	        connection.commit();
	    } catch (SQLException exp) {
	      try {
	        connection.rollback();
	      } catch (SQLException e) {}
	      try {
	        connection = connectionPool.renewConnection(connection);
	          try {
	            this.debug("Executing : ", statement);
	            number = 0;
	            pStatement = this.setupPreparedStatement(connection, statement);
	            number = pStatement.executeUpdate();
	            pStatement.close();
	          } catch (SQLException sqlExp) {
	            this.printError(sqlExp, statement);
	            throw  sqlExp;
	          }
	        connection.commit();
	      } catch (SQLException se) {
	        pStatement.close();
	        connection.rollback();
	        connectionPool.releaseConnection(connection);
	        throw  se;
	      }
	    } finally {
	      connectionPool.releaseConnection(connection);
	    }
	    return number;
	  }
	  
	  /**
	   * put your documentation comment here
	   * @param connection
	   * @param statement
	   * @return 
	   * @exception SQLException
	   */
	  private PreparedStatement setupPreparedStatement (Connection connection, ParametricStatement statement) throws SQLException {
	    PreparedStatement pStatement = connection.prepareStatement(statement.getSqlStatement());
	    List param = statement.getParameters();
	    if (param != null && param.size() > 0) {
	      for (int i = 0; i < param.size(); i++) {
	        Object obj = param.get(i);
	        if (obj instanceof DatabaseNull) {
	          pStatement.setNull(i + 1, ((DatabaseNull)obj).getType());
	        } 
	        else {
	          if (obj instanceof java.util.Date) {
	            obj = new Timestamp(((java.util.Date)obj).getTime());
	          }
	          pStatement.setObject(i + 1, obj);
	        }
	      }
	    }
	    return  pStatement;
	  }

	  /**
	   * put your documentation comment here
	   * @param exp
	   * @param statment
	   */
	  private void printError (Exception exp, ParametricStatement statment) {
	    try {
	      String prefix = "SQL Fail@" + new Date() + ": ";
	      System.out.println(prefix + " Exception --> " + exp);
	      System.out.println(prefix + this.parametricStatementToString(statment));
	    } catch (Throwable throwable) {
	      System.err.println("" + new Date() + " BaseOracleDAO.printError --> " + throwable);
	      throwable.printStackTrace();
	    }
	  }
	  /**
	   * put your documentation comment here
	   * @param statement
	   * @return 
	   */
	  private String parametricStatementToString (ParametricStatement statement) {
	    try {
	      if (statement == null) {
	        return  "NULL";
	      }
	      String sql = statement.getSqlStatement();
	      List params = statement.getParameters();
	      if (sql == null) {
	        return  "[NULL SQL statement String]";
	      }
	      StringBuffer sb = new StringBuffer(sql);
	      if (params != null) {
	        for (Iterator it = params.iterator(); it.hasNext();) {
	          Object next = it.next();
	          if (next != null) {
	            sb.append("[" + next.toString() + "]");
	          } 
	          else {
	            sb.append("[NULL]");
	          }
	        }
	      }
	      sb.append("\n");
	      return  sb.toString();
	    } catch (Throwable throwable) {
	      throwable.printStackTrace();
	      return  "SQL not printable.";
	    }
	  }
	  

	  /**
	   * put your documentation comment here
	   * @param prefix
	   * @param statment
	   */
	  private void debug (String prefix, ParametricStatement statment) {
	    try {
	      if (dedug_mode) {
	        System.out.println(prefix + this.parametricStatementToString(statment));
	      }
	    } catch (Throwable throwable) {
	      System.out.println("" + new Date() + " BaseOracleDAO.debug --> " + throwable);
	      System.err.println("" + new Date() + " BaseOracleDAO.debug --> " + throwable);
	      throwable.printStackTrace();
	    }
	  }

	  
}
