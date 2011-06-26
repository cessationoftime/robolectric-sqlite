package com.seventheye.robolectric.sqlite;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.seventheye.robolectric.sqlite.util.PackagedSuite;
import com.seventheye.robolectric.sqlite.util.PackagedSuite.PackagedClasses;

/*
 * Make sure to ONLY run this as a TEST SUITE.
 * If you work with Eclipse and run this as a test suite,
 * and then choose an individual test from the list you will
 * end up running the test with the H2 database instead of SQLite.
 * 
 * robolectric git repository and robolectric-sqlite git repository
 * may need to be located in the same parent folder for this test
 * to succeed otherwise it will not be able to find src/test/resources
 * of robolectric. 
 */
@RunWith(PackagedSuite.class)
@PackagedClasses("com.xtremelabs.robolectric")
public class RunAllRobolectricTestsWithSQLiteTestSuite {
}