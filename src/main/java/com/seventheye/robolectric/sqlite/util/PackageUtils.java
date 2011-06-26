package com.seventheye.robolectric.sqlite.util;

import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class PackageUtils {
 public static Class<?>[] getClassNamesInPackage
     (Class<?> referenceClass, String packageName){
	 
	 URL jarLocation = referenceClass.getProtectionDomain().getCodeSource().getLocation();
	 
   List<Class<?>> classes = new ArrayList<Class<?>> ();
   packageName = packageName.replaceAll("\\." , "/");

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
         String className = jarEntry.getName().replaceAll("/", "\\.").replace(".class", "");        
         
         classes.add (Class.forName(className));
       }
     }
   }
   catch( Exception e){
     e.printStackTrace ();
   }
   return classes.toArray(new Class[classes.size()]);
}
}
