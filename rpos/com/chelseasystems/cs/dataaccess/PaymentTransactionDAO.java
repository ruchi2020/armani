package com.chelseasystems.cs.dataaccess;

import com.chelseasystems.cr.database.ParametricStatement;
import com.chelseasystems.cr.pos.CompositePOSTransaction;
import com.chelseasystems.cr.pos.PaymentTransaction;
import java.sql.SQLException;

// Referenced classes of package com.chelseasystems.cs.dataaccess:
//            BaseDAO

public interface PaymentTransactionDAO extends BaseDAO {

	public abstract PaymentTransaction selectById(String s) throws SQLException;

	public ParametricStatement getUpdateCustomerSQL(CompositePOSTransaction object)
			throws SQLException;
}
