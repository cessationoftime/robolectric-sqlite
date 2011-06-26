package com.seventheye.robolectric.sqlite.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import com.xtremelabs.robolectric.WithTestDefaultsRunner;

public class PackageUtils {

/**
 * Find the Jar of a given class, and pull all of the test classes in the
 * given packageName
 * @param referenceClass
 * @param packageName
 * @return
 */
	public static Class<?>[] findTestClasses(Class<?> referenceClass,
			String packageName) {
		
		Class<?>[] classList;
		
		try {
			classList = getClasses(packageName);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		if (classList.length == 0)
			classList = getClassNamesInPackage(referenceClass, packageName);
		System.out.println("Classes found for testing: " + classList.length);
		return filterTests(classList);
	}

	/**
	 * Find the Jar of a given class, and pull all of the classes in the given
	 * packageName
	 * 
	 * @param referenceClass
	 * @param packageName
	 * @return
	 */
	private static Class<?>[] getClassNamesInPackage(Class<?> referenceClass,
			String packageName) {

		URL jarLocation = referenceClass.getProtectionDomain().getCodeSource()
				.getLocation();

		List<Class<?>> classes = new ArrayList<Class<?>>();
		packageName = packageName.replaceAll("\\.", "/");

		try {
			JarInputStream jarFile = new JarInputStream(new FileInputStream(
					jarLocation.getPath()));
			JarEntry jarEntry;

			while (true) {
				jarEntry = jarFile.getNextJarEntry();
				if (jarEntry == null) {
					break;
				}
				if ((jarEntry.getName().startsWith(packageName))
						&& (jarEntry.getName().endsWith(".class"))) {
					String className = jarEntry.getName()
							.replaceAll("/", "\\.").replace(".class", "");

					classes.add(Class.forName(className));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classes.toArray(new Class[classes.size()]);
	}

	/**
	 * Get those classes in the given array that have a name ending with the
	 * word 'Test'
	 * @param classes
	 * @return
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
	 * Scans all classes accessible from the context class loader which belong
	 * to the given package and subpackages.
	 * 
	 * @param packageName
	 *            The base package
	 * @return The classes
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private static Class<?>[] getClasses(String packageName)
			throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
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
	 * Recursive method used to find all classes in a given directory and
	 * subdirs.
	 * 
	 * @param directory
	 *            The base directory
	 * @param packageName
	 *            The package name for classes found inside the base directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 */
	private static List<Class<?>> findClasses(File directory, String packageName)
			throws ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file,
						packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(Class.forName(packageName
						+ '.'
						+ file.getName().substring(0,
								file.getName().length() - 6)));
			}
		}
		return classes;
	}

}
