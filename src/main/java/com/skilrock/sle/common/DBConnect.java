package com.skilrock.sle.common;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

public class DBConnect {
	private static final SLELogger logger = SLELogger.getLogger(DBConnect.class.getName());

	private static DataSource ds = null;
	static {
		try {
			ds = (DataSource) new InitialContext().lookup("java:/MyDataSourceSLE");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		Connection connection = null;
		try {
			connection = ds.getConnection();
		} catch (Exception se) {
			logger.error("Error "+se.getMessage());
		}
		return connection;
	}

	public static UserTransaction startTransaction() throws NamingException, NotSupportedException, SystemException {
		InitialContext ic = new InitialContext();
		UserTransaction userTxn = (UserTransaction) ic.lookup("java:comp/UserTransaction");
		userTxn.begin();
		return userTxn;
	}

	public static void rollBackUserTransaction(UserTransaction utx) {
		try {
			if (utx != null) {
				utx.rollback();
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
	}

	private static String url = null;

	public static Connection getPropFileCon() {
		Connection connection = null;
		try {
			url = "jdbc:mysql://" + "192.168.124.99" + "/" + "SLE_LATEST_September";
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(url, "root", "root");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException se) {
			se.printStackTrace();
		}
		return connection;
	}

	public static void closeConnection(Connection con) {
		try {
			if (con != null)
				con.close();
		} catch (Exception sqe) {
			sqe.printStackTrace();
		}
	}
	
	public static void closeStatements(Statement...statements) {
		int count = statements.length;

		try {
			for (int iLoop = 0; iLoop < count; iLoop++) {
				if(statements[iLoop] != null)
					statements[iLoop].close();
			}
		} catch (Exception e) {
			logger.error("Error "+e.getMessage());
		}
	}
	
	public static void closeStatement(Statement stmt) {
		try {
			stmt.close();
		} catch (Exception e) {
			logger.error("Error "+e.getMessage());
		}
	}
	
	public static void closePrepareStatementStatements(PreparedStatement...preparedStatements) {
		int count = preparedStatements.length;

		try {
			for (int iLoop = 0; iLoop < count; iLoop++) {
				if(preparedStatements[iLoop] != null)
					preparedStatements[iLoop].close();
			}
		} catch (Exception e) {
			logger.error("Error "+e.getMessage());
		}
	}
	
	public static void closePrepareStatementStatement(PreparedStatement pStmt) {
		try {
			pStmt.close();
		} catch (Exception e) {
			logger.error("Error "+e.getMessage());
		}
	}

	public static void closePstmt(PreparedStatement... pstmts) {
		try {
			for(PreparedStatement pstmt : pstmts) {
				if (pstmt != null) {
					pstmt.close();
				}
			}
		} catch (Exception sqe) {
			sqe.printStackTrace();
		}
	}
	
	public static void closePstmt(PreparedStatement pstm) {
		try {
			if (pstm == null)
				logger.info("PreparedStatement Already Closed Or Empty");
			else
				pstm.close();
		} catch (SQLException ex) {
			logger.error("Problem While closing PreparedStatement");
			ex.printStackTrace();

		}
	}

	public static void closeStmt(Statement stmt) {
		try {
			if (stmt == null)
				logger.info("Statement Already Closed Or Empty");
			else
				stmt.close();
		} catch (SQLException ex) {
			logger.error("Problem While closing Statement");
			ex.printStackTrace();
		}
	}

	public static void closeCstmt(CallableStatement cstmt) {
		try {
			if (cstmt == null)
				logger.info("Callable Statement Already Closed Or Empty");
			else
				cstmt.close();
		} catch (SQLException ex) {
			logger.error("Problem While closing Callable Statement");
			ex.printStackTrace();
		}
	}

	public static void closeRs(ResultSet rs) {
		try {
			if (rs == null)
				logger.info("ResultSet Already Closed Or Empty");
			else
				rs.close();
		} catch (SQLException ex) {
			logger.error("Problem While closing ResultSet");
			ex.printStackTrace();
		}
	}
	public static void closeRs(ResultSet... rsts) {
		try {
			for(ResultSet rs:rsts){
				if (rs == null)
					logger.info("ResultSet Already Closed Or Empty");
				else
					rs.close();
			}
			
		} catch (SQLException ex) {
			logger.error("Problem While closing ResultSet");
			ex.printStackTrace();
		}
	}
	public static void closeConnection(Connection con, PreparedStatement pstm, Statement stmt, ResultSet rs) {
		closeRs(rs);
		closePstmt(pstm);
		closeStmt(stmt);
		closeConnection(con);
	}

	public static void closeConnection(Connection con, PreparedStatement pstm, ResultSet rs) {
		closeRs(rs);
		closePstmt(pstm);
		closeConnection(con);
	}

	public static void closeConnection(Connection con, Statement stmt, ResultSet rs) {
		closeRs(rs);
		closeStmt(stmt);
		closeConnection(con);
	}
	public static void closeConnection(Connection con,PreparedStatement pstmt) {
		closePstmt(pstmt);
		closeConnection(con);
	}
	
	public static void closeConnection(PreparedStatement pstmt ,Statement stmt ,ResultSet rs) {
		closeRs(rs);
		closePstmt(pstmt);
		closeStmt(stmt);
	}
	
	public static void closeConnection(PreparedStatement pstmt ,ResultSet rs) {
		closeRs(rs);
		closePstmt(pstmt);
	}

	public static void closeConnection(Statement stmt ,ResultSet rs) {
		closeRs(rs);
		closeStmt(stmt);
	}
}