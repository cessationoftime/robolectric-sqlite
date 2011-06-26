package com.seventheye.robolectric.sqlite.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

import com.xtremelabs.robolectric.WithTestDefaultsRunner;

//TODO: we are using SQLiteRunnerBuilder() in constructors through a bad hack...we should really be supplying this from PackageSuite's calling class.  But I don't know what that is!

public class PackagedSuite extends ParentRunner<Runner> {
	/**
	 * Returns an empty suite.
	 */
	public static Runner emptySuite() {
		try {
			return new PackagedSuite((Class<?>)null, new Class<?>[0]);
		} catch (InitializationError e) {
			throw new RuntimeException("This shouldn't be possible");
		}
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@Inherited
	public @interface PackagedClasses {
		/**
		 * @return the classes to be run
		 */
		public String value();
	}
	
	private static Class<?>[] getAnnotatedPackage(Class<?> klass) throws InitializationError {
		PackagedClasses annotation= klass.getAnnotation(PackagedClasses.class);
		if (annotation == null)
			throw new InitializationError(String.format("class '%s' must have a SuiteClasses annotation", klass.getName()));
		
		String pack = annotation.value();
		return PackageUtils.findTestClasses(WithTestDefaultsRunner.class,pack);
	}

	private final List<Runner> fRunners;

	/**
	 * Called reflectively on classes annotated with <code>@RunWith(PackageSuite.class)</code>
	 * 
	 * @param klass the root class
	 * @param builder builds runners for classes in the suite
	 * @throws InitializationError
	 */
	public PackagedSuite(Class<?> klass, RunnerBuilder builder) throws InitializationError {
		this(new SQLiteRunnerBuilder(), klass, getAnnotatedPackage(klass));
	}

	/**
	 * Call this when there is no single root class (for example, multiple class names
	 * passed on the command line to {@link org.junit.runner.JUnitCore}
	 * 
	 * @param builder builds runners for classes in the suite
	 * @param classes the classes in the suite
	 * @throws InitializationError 
	 */
	public PackagedSuite(RunnerBuilder builder, Class<?>[] classes) throws InitializationError {
		super(null);
		builder = new SQLiteRunnerBuilder();
		fRunners = builder.runners(null, classes);
	}
	
	protected PackagedSuite(Class<?> klass, Class<?>[] suiteClasses) throws InitializationError {
		this(new SQLiteRunnerBuilder(), klass, suiteClasses);
	}
	
	/**
	 * Called by this class and subclasses once the classes making up the suite have been determined
	 * 
	 * @param builder builds runners for classes in the suite
	 * @param klass the root of the suite
	 * @param suiteClasses the classes in the suite
	 * @throws InitializationError
	 */
	protected PackagedSuite(RunnerBuilder builder, Class<?> klass, Class<?>[] suiteClasses) throws InitializationError {
		super(klass);
		builder = new SQLiteRunnerBuilder();
		fRunners = builder.runners(klass, suiteClasses);
	}
	
	@Override
	protected List<Runner> getChildren() {
		return fRunners;
	}
	
	@Override
	protected Description describeChild(Runner child) {
		return child.getDescription();
	}

	@Override
	protected void runChild(Runner runner, final RunNotifier notifier) {
		runner.run(notifier);
	}
	
}
