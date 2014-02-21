package com.yonyou.nc.codevalidator.runtime;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Date: 2005-11-14
 * Time: 9:36:41
 */
public class BootstrapClassLoader extends URLClassLoader {

    private BootstrapClassLoader(URL[] urls) {
        super(urls);
    }

    public void addURL(URL url) {
        super.addURL(url);
    }

    public static ClassLoader getBootstrapClassLoader(String ncHome, URL[] urls) {
        BootstrapClassLoader loader = new BootstrapClassLoader(urls == null ? new URL[0] : urls);
        URL nchomeURL;
        File ncHomeFile = new File(ncHome);
        if (ncHomeFile.exists()) {
            try {
                nchomeURL = ncHomeFile.toURI().toURL();
              
                URL[] mwURLs = ClasspathComputer.computeJarCPInLib(new File(ncHome, "middleware"));
                URL[] antURLs = ClasspathComputer.computeJarCPInLib(new File(ncHome, "ant/lib"));
                URL[] frameworkURLs = ClasspathComputer.computeJarCPInLib(new File(ncHome, "framework"));
                URL[] externalURLs = ClasspathComputer.computeStandCP(new URL(nchomeURL, "external"));
                URL[] serverLib = ClasspathComputer.computeStandCP(nchomeURL);
                URL[] ejb = ClasspathComputer.computeJarCPInLib(new File(ncHome, "ejb"));
                URL[] modules = ClasspathComputer.computeModulePublicCP(new File(ncHome, "modules"));
                URL[] langlib =  ClasspathComputer.computeJarCPInLib(new File(ncHome, "langlib"));

                addURLs(mwURLs, loader);
                addURLs(antURLs, loader);
                addURLs(frameworkURLs, loader);
                addURLs(externalURLs, loader);
                addURLs(serverLib, loader);
                addURLs(ejb, loader);
                addURLs(modules, loader);
                addURLs(langlib, loader);
                return loader;

            } catch (MalformedURLException e) {

            }

        }
        return loader;
    }

    public static ClassLoader getBootstrapClassLoader(String ncHome) {
        return getBootstrapClassLoader(ncHome, null);

    }

    private static void addURLs(URL urls[], BootstrapClassLoader classLoader) {
        for (int i = 0; i < urls.length; i++) {
            classLoader.addURL(urls[i]);
        }

    }

}
