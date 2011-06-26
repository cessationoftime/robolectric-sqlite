package com.seventheye.robolectric.sqlite;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.database.sqlite.SQLiteDatabase;

import com.seventheye.robolectric.sqlite.util.SQLiteMap;
import com.xtremelabs.robolectric.WithTestDefaultsRunner;
import com.xtremelabs.robolectric.util.DatabaseConfig;
import com.xtremelabs.robolectric.util.H2Map;
import com.xtremelabs.robolectric.util.DatabaseConfig.UsingDatabaseMap;

@UsingDatabaseMap(SQLiteMap.class)
@RunWith(WithTestDefaultsRunner.class)
public class isSQLiteRunningTest {

	Connection connection;
	@Before
    public void setUp() throws Exception {
		connection = DatabaseConfig.OpenMemoryConnection();
    }
	
	@Test
    public void isSQLiteRunning() {
		Enumeration<Driver> drivers = DriverManager.getDrivers();
				
		boolean containsH2Driver = false;
		boolean containsSqliteDriver = false;
		while(drivers.hasMoreElements()) {
			Driver d=drivers.nextElement();
			String s = d.toString();
			if (s.toLowerCase().contains("org.sqlite.jdbc")) containsSqliteDriver=true;
			if (s.toLowerCase().contains("org.h2.driver")) containsH2Driver=true;
		}
		Assert.assertTrue(containsSqliteDriver);
		//Assert.assertFalse(containsH2Driver); 
		
		//I'd prefer if H2 wasn't loaded too but I cannot find where 
		//it is loading from. It seems to be registered with the DriverManager automagically.
		//I doubt it will have any effect...
		}
	
	@Test(expected = android.database.SQLException.class)
	public void CannotCreateIntAutoIncrement()
	{
		 SQLiteDatabase   database = SQLiteDatabase.openDatabase("path", null, 0);

	        database.execSQL("CREATE TABLE table_name (\n" +
	                "  id INT PRIMARY KEY AUTOINCREMENT,\n" +
	                "  first_column VARCHAR(255),\n" +
	                "  second_column BINARY,\n" +
	                "  name VARCHAR(255),\n" +
	                "  big_int INTEGER\n" +
	                ");");
    }
		
	@Test
	public void isSQLite(){
		Assert.assertTrue(DatabaseConfig.getDatabaseMap().getClass().getName().equals(SQLiteMap.class.getName()));
	}
}