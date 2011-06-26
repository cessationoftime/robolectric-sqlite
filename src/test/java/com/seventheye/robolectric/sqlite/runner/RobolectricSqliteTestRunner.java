package com.seventheye.robolectric.sqlite.runner;

import java.io.File;

import org.junit.runners.model.InitializationError;

import com.xtremelabs.robolectric.RobolectricConfig;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.bytecode.ClassHandler;
import com.xtremelabs.robolectric.bytecode.RobolectricClassLoader;

public class RobolectricSqliteTestRunner  extends RobolectricTestRunner {

	public RobolectricSqliteTestRunner(Class<?> testClass,
			ClassHandler classHandler, RobolectricClassLoader classLoader,
			RobolectricConfig robolectricConfig) throws InitializationError {
		super(testClass, classHandler, classLoader, robolectricConfig);
	}

	public RobolectricSqliteTestRunner(Class<?> testClass,
			ClassHandler classHandler, RobolectricConfig robolectricConfig)
			throws InitializationError {
		super(testClass, classHandler, robolectricConfig);
	}

	public RobolectricSqliteTestRunner(Class<?> testClass,
			File androidManifestPath, File resourceDirectory)
			throws InitializationError {
		super(testClass, androidManifestPath, resourceDirectory);
	}

	public RobolectricSqliteTestRunner(Class<?> testClass,
			File androidProjectRoot) throws InitializationError {
		super(testClass, androidProjectRoot);
	}

	public RobolectricSqliteTestRunner(Class<?> testClass,
			RobolectricConfig robolectricConfig) throws InitializationError {
		super(testClass, robolectricConfig);
	}

	public RobolectricSqliteTestRunner(Class<?> testClass)
			throws InitializationError {
		super(testClass);
	}
	
//	@Override
//	protected void setupDatabaseDriver() {
//		DBConfig.LoadSQLiteDriver(new SqliteMap());
//	}
	
}
