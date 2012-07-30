package db;

import java.sql.*;
import java.io.*;
import java.util.*;

public class dbConnector
{
	private Statement stmt;
	private String stmtStr;
	private Connection dbConnection;

	public void dbOpen()
	throws ClassNotFoundException, SQLException,
        InstantiationException, IllegalAccessException
	{                
/*
                String userName = "root";
                String password = "is-it,sanity";
                String url = "jdbc:mysql://localhost:3306/lds_local";
*/

                String userName = "granatum_annot";
                String password = "granatum_annot";
                String url = "jdbc:mysql://localhost:3306/lds_local";
                
                Class.forName ("com.mysql.jdbc.Driver").newInstance ();
                dbConnection = DriverManager.getConnection (url, userName, password);
		stmt = dbConnection.createStatement();
	}

	public ResultSet dbQuery(String sqlQuery)
	throws SQLException
	{
		ResultSet rs;
		clearRequestFields();
		this.stmtStr = new String(sqlQuery);
		rs = stmt.executeQuery(stmtStr);
		return(rs);
	}

	public int dbUpdate(String updateString)
	throws SQLException
	{
		int updatesMade;
		clearRequestFields();
		this.stmtStr = new String(updateString);
		updatesMade = stmt.executeUpdate(stmtStr);
		return(updatesMade);
	}

	public void dbClose()
	throws SQLException
	{
		clearRequestFields();
		dbConnection.close();
		return;
	}

	private void clearRequestFields()
	{
		stmtStr = null;
		return;
	}

	public String getDateString(java.util.Date dday){

		// construct date

		String sqlDate = new String("");

		sqlDate += (dday.getMonth()+1)+"/";

		sqlDate += dday.getDate();

		sqlDate += "/"+(1900 + dday.getYear())+" ";

		return sqlDate;
	}

	public String getSQLDate(java.util.Date dday){

		// construct date

		String sqlDate = new String("");

		if(dday.getMonth()<9)
			sqlDate += "0"+(dday.getMonth()+1)+"/";
		else
			sqlDate += (dday.getMonth()+1)+"/";

		if(dday.getDay()<10)
			sqlDate += "0"+dday.getDate();
		else
			sqlDate += dday.getDate();

		sqlDate += "/"+(1900 + dday.getYear())+" ";

		// construct time

		if(dday.getHours()<10)
			sqlDate += "0"+dday.getHours()+":";
		else
			sqlDate += dday.getHours()+":";

		if(dday.getMinutes()<10)
			sqlDate += "0"+dday.getMinutes()+":";
		else
			sqlDate += dday.getMinutes()+":";

		if(dday.getSeconds()<10)
			sqlDate += "0"+dday.getSeconds();
		else
			sqlDate += dday.getSeconds();

		return sqlDate;
	}
}

