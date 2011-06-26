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

import com.xtremelabs.robolectric.WithTestDefaultsRunner;
import com.xtremelabs.robolectric.util.DatabaseConfig;

@RunWith(WithTestDefaultsRunner.class)
public class isH2RunningTest {

	Connection connection;
	@Before
    public void setUp() throws Exception {
		connection = DatabaseConfig.OpenMemoryConnection();
    }
	
	@Test
    public void isH2Running() {
		Enumeration<Driver> drivers = DriverManager.getDrivers();
				
		boolean containsH2Driver = false;
		boolean containsSqliteDriver = false;
		while(drivers.hasMoreElements()) {
			Driver d=drivers.nextElement();
			String s = d.toString();
			if (s.toLowerCase().contains("org.sqlite.jdbc")) containsSqliteDriver=true;
			if (s.toLowerCase().contains("org.h2.driver")) containsH2Driver=true;
		}
		//Assert.assertFalse(containsSqliteDriver);
		Assert.assertTrue(containsH2Driver);
		}
	
	@Test(expected = android.database.SQLException.class)
	public void CannotCreateIntAutoIncrement()
	{
		//I want H2 to throw an exception here. since SQLite does.
		//I have mapped the exception in the H2 map
		 SQLiteDatabase   database = SQLiteDatabase.openDatabase("path", null, 0);

	        database.execSQL("CREATE TABLE table_name (\n" +
	                "  id INT PRIMARY KEY AUTOINCREMENT,\n" +
	                "  first_column VARCHAR(255),\n" +
	                "  second_column BINARY,\n" +
	                "  name VARCHAR(255),\n" +
	                "  big_int INTEGER\n" +
	                ");");
    }
	
    }
