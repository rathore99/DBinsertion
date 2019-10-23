package com.worldpay;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TableMetaData {
	public static String[] columnNames;
	public static  String[] columnTypesCode;
	public static void main(String args[]) throws SQLException {
		
		/*Connection con = JDBCConnection.setConnection();
		PreparedStatement ps = con.prepareStatement("INSERT INTO EMP VALUES(?,?,?)");
		ps.setObject(1, "2");
		ps.setObject(2, "rr");
		ps.setObject(3, "2019-07-05");
		ps.execute();*/

		try {
			Connection con = JDBCConnection.setConnection();
			DatabaseMetaData dbmd = con.getMetaData();

			ResultSet rset = dbmd.getColumns(null, null, "EMP", "%");
			rset.last();
			int rowCnt = rset.getRow();
			rset.beforeFirst();
			columnNames = new String [rowCnt];
			columnTypesCode = new String [rowCnt];
			int i=0;
			while (rset.next()) {
				columnNames[i]=rset.getString("COLUMN_NAME");
				columnTypesCode[i]= rset.getString("DATA_TYPE");
				System.out.println(rset.getString("TABLE_NAME") + " | " + rset.getString("COLUMN_NAME") + " | "
						+ rset.getString("DATA_TYPE") + " | " + rset.getString("ORDINAL_POSITION"));
			}

			con.close();

		} catch (Exception e) {
			System.out.println(e);

		}

	}
	
	public void mapper() {
		List<HashMap<String, String>>data = input();
		String [] ColumnValues = new String[columnNames.length]; 
		for(int i=0;i<data.size();++i) {
			for(int j=0;j<columnNames.length;++j) {
				if(data.get(i).containsKey(columnNames[j])) {
					ColumnValues[j]=data.get(i).get(ColumnValues[j]); 
				}
				else
					ColumnValues[j]="null";
			}
		}

	}
	public String prepareRowInserts(String tableName,String[] colmnValues) {
		StringBuilder insertQuery = null ;
		insertQuery.append("INSERT INTO ");
		insertQuery.append(tableName);
		insertQuery.append("VALUES (");
		String val ;
		for(int i=0;i<columnNames.length-1;++i) {
			insertQuery.append("?,");
		}
		insertQuery.append("?)");
		return insertQuery.toString();
	}
	public void insertValues(String tableName,String[] columnValues,int []activeColumnOrder) throws SQLException {
		
		String insertQuery = prepareRowInserts("emp",columnValues);
		
		Connection connection = JDBCConnection.setConnection();
		PreparedStatement ps = connection.prepareStatement(insertQuery);
		for(int i=1;i<=columnNames.length;++i) {
		 ps.setObject(activeColumnOrder[i], columnValues[i]);
		 
		}
		
		ps.executeQuery();
	}
	public List<HashMap<String, String>> input() {
		HashMap<String,String>data = new HashMap<String,String>();
		List<HashMap<String,String> >list = new ArrayList<HashMap<String,String> >();
		
			data.put("ename", "rr");
			data.put("eno", "1");
			data.put("sal", "200");
			list.add(data);
			data.put("ename", "rr");
			data.put("eno", "2");
			data.put("sal", "200");
			list.add(data);
			return list;
	}
	

}
