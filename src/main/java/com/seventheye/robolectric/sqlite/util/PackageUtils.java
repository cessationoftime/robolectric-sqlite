package com.seventheye.robolectric.sqlite.util;

import java.io.FileInputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class PackageUtils {

 private static boolean debug = true;

 public static Class<?>[] getClassNamesInPackage
     (Class<?> referenceClass, String packageName){
	 
	 URL jarLocation = referenceClass.getProtectionDomain().getCodeSource().getLocation();

	 
   List<Class<?>> classes = new ArrayList<Class<?>> ();
   packageName = packageName.replaceAll("\\." , "/");
   if (debug) System.out.println
        ("Jar " + jarLocation.getPath() + " looking for " + packageName);
   try{
     JarInputStream jarFile = new JarInputStream
        (new FileInputStream (jarLocation.getPath()));
     JarEntry jarEntry;

     while(true) {
       jarEntry=jarFile.getNextJarEntry ();
       if(jarEntry == null){
         break;
       }
       if((jarEntry.getName ().startsWith (packageName)) &&
            (jarEntry.getName ().endsWith (".class")) ) {
         if (debug) System.out.println 
           ("Found " + jarEntry.getName().replaceAll("/", "\\."));
         String className = jarEntry.getName().replaceAll("/", "\\.").replace(".class", "");
            
       //  URLClassLoader child = new URLClassLoader (new URL[]{jarLocation}, PackageUtils.class.getClassLoader());
         //Class classToLoad = Class.forName ("com.MyClass", true, child);
//         Method method = classToLoad.getDeclaredMethod ("myMethod");
//         Object instance = classToLoad.newInstance ();
//         Object result = method.invoke (instance);

         //Enumeration<URL> uri = child.(jarEntry.getName());
         
//         while (uri.hasMoreElements()) {
//        	 
//        	 System.out.print(uri.nextElement().toExternalForm());
//         }
         
         
         classes.add (Class.forName(className));
         //classes.add (jarEntry.getName().replaceAll("/", "\\."));
       }
     }
   }
   catch( Exception e){
     e.printStackTrace ();
   }
   return classes.toArray(new Class[classes.size()]);
}
}
