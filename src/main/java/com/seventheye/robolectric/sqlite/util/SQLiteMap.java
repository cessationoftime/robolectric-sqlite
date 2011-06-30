package com.seventheye.robolectric.sqlite.util;

import java.sql.ResultSet;

import com.xtremelabs.robolectric.util.DatabaseConfig;
//TODO: determine if SQLite always returns lowercase column names the way H2 always returns UPPERCASE ones
public class SQLiteMap implements DatabaseConfig.DatabaseMap {

	public String getDriverClassName() {
	//	throw new RuntimeException("SQLITE!");
		return "org.sqlite.JDBC";
	}

	public String getConnectionString() {
	//	throw new RuntimeException("SQLITE!");
		return "jdbc:sqlite::memory:";
	}

	public String getScrubSQL(String sql) {
	//	throw new RuntimeException("SQLITE!");
		return sql;
	}

	public String getSelectLastInsertIdentity() {
		return "SELECT last_insert_rowid() AS id";
	}

	public int getResultSetType() {
		return ResultSet.TYPE_FORWARD_ONLY;
	}
	
	
	
}
