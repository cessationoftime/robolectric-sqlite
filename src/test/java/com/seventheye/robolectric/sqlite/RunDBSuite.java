package com.seventheye.robolectric.sqlite;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import com.seventheye.robolectric.sqlite.util.PackagedSuite;
import com.seventheye.robolectric.sqlite.util.PackagedSuite.PackagedClasses;

///*
// * Make sure to ONLY run this as a TEST SUITE.
// * If you work with Eclipse and run this as a test suite,
// * and then choose an individual test from the list you will
// * end up running the test with the H2 database instead of SQLite
// */
//@RunWith(Suite.class)
//@SuiteClasses({isH2RunningTest.class,isSQLiteRunningTest.class})
//public class RunDBSuite {
//}