package com.seventheye.robolectric.sqlite.util;

import com.xtremelabs.robolectric.util.DatabaseConfig;
//TODO: create tests for each runner to make sure it loads Sqlite
public class SQLiteMap implements DatabaseConfig.DatabaseMap {

	public String getDriverClassName() {
	//	throw new RuntimeException("SQLITE!");
		return "org.sqlite.JDBC";
	}

	public String getConnectionString() {
	//	throw new RuntimeException("SQLITE!");
		return "jdbc:sqlite::memory:";
	}

	public String ScrubSQL(String sql) {
	//	throw new RuntimeException("SQLITE!");
		return sql;
	}

	public String SelectLastInsertIdentity() {
		return "SELECT last_insert_rowid() AS id";
	}
	
}
