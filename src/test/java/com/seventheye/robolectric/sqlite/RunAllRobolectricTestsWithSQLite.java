package com.seventheye.robolectric.sqlite;

import org.junit.runner.RunWith;

import com.seventheye.robolectric.sqlite.runner.PackageSuite;
import com.seventheye.robolectric.sqlite.runner.PackageSuite.PackagedClasses;


/*
 * Make sure to ONLY run this as a TEST SUITE.
 * If you work with Eclipse and run this as a test suite,
 * and then choose an individual test from the list you will
 * end up running the test with the H2 database instead of SQLite
 */
@RunWith(PackageSuite.class)
@PackagedClasses("com.xtremelabs.robolectric")
public class RunAllRobolectricTestsWithSQLite {
}