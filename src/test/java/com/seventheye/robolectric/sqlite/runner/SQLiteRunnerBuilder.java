package com.seventheye.robolectric.sqlite.runner;

import org.junit.internal.builders.AllDefaultPossibilitiesBuilder;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runners.model.RunnerBuilder;

import com.seventheye.robolectric.sqlite.util.SQLiteMap;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.util.DatabaseConfig.DatabaseMap;

public class SQLiteRunnerBuilder extends RunnerBuilder {
	@Override
	public Runner runnerForClass(Class<?> testClass) throws Throwable {
		RunWith rw = testClass.getAnnotation(RunWith.class);
		if (rw != null) {
			// Give the runner the new DatabaseMap at construction.
			Class<? extends Runner> runnerClass = rw.value();
			if (RobolectricTestRunner.class.isAssignableFrom(runnerClass)) {

				try {
					return runnerClass.getConstructor(Class.class, DatabaseMap.class)
					.newInstance(new Object[] { testClass, new SQLiteMap() });
				} catch (Exception e) {
					
				}
//				try {
//					RobolectricConfig roboConfig = new RobolectricConfig(
//							resourceFile("TestAndroidManifest.xml"),
//							resourceFile("res"), resourceFile("assets"));
//					return runnerClass.getConstructor(Class.class,RobolectricConfig.class, DatabaseMap.class)
//					.newInstance(new Object[] { testClass, roboConfig,new SQLiteMap() });
//				} catch (Exception e) {
//					throw new RuntimeException(e);
//				}

			}
		}

		// If a RobolectricTestRunner is not specified, use the default runner.
		RunnerBuilder defaultBuilder = new AllDefaultPossibilitiesBuilder(true);
		Runner r = defaultBuilder.runnerForClass(testClass);

		return r;
	}
}
