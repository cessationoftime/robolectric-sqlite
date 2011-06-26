package com.seventheye.robolectric.sqlite.util;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

import com.xtremelabs.robolectric.WithTestDefaultsRunner;

//TODO: we are using SQLiteRunnerBuilder() in constructors through a bad hack...we should really be supplying this from PackageSuite's calling class.  But I don't know what that is!

/**
 * Using <code>PackagedSuite</code> as a runner allows you to manually
 * build a suite containing tests from many classes. It is the JUnit 4 equivalent of the JUnit 3.8.x
 * static {@link junit.framework.Test} <code>suite()</code> method. To use it, annotate a class
 * with <code>@RunWith(PackagedSuite)</code> and <code>@PackagedClasses({TestClass1.class, ...})</code>.
 * When you run this class, it will run all the tests in all the suite classes.
 */
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
	
	/**
	 * The <code>PackagedClasses</code> annotation specifies the classes to be run when a class
	 * annotated with <code>@RunWith(Suite.class)</code> is run.
	 */
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
		

			try {
			  Class<?>[] classList =	getClasses(pack);
			  
			  if (classList.length==0)  classList = PackageUtils.getClassNamesInPackage(WithTestDefaultsRunner.class,pack);
			  System.out.println("Classes found for testing: " +classList.length);
				return filterTests(classList);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

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
	
	/**
	 * Call this when the default builder is good enough. Left in for compatibility with JUnit 4.4.
	 * @param klass the root of the suite
	 * @param suiteClasses the classes in the suite
	 * @throws InitializationError
	 */
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
	
	/*
	 * Get those classes in the given array that have a name ending with the word 'Test'
	 */
    private static Class<?>[] filterTests(Class<?>[] classes) {
        ArrayList<Class<?>> tests = new ArrayList<Class<?>>();
      
            for (Class<?> c : classes) {
                String name = c.getName();
                if (name.endsWith("Test"))
                    tests.add(c);
            }
   
        return tests.toArray(new Class[0]);
    }

   
    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private static Class<?>[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        List<Class<?>> classes = new ArrayList<Class<?>>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }
    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

	
}
