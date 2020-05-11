 package com.fasten.wp4.template.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

/**
 * Adds support for UTF-8 based bundles for admin i18n messages
 * 
 * Taken from: https://stackoverflow.com/a/3646601/1260910
 *
 * @author rafael-pestano
 */
public class MyAdminUTF8Bundle extends ResourceBundle {

    protected static final String BUNDLE_NAME = "admin";
    protected static final String BUNDLE_EXTENSION = "properties";

    public MyAdminUTF8Bundle() {
        Locale locale = Locale.getDefault();
        if (FacesContext.getCurrentInstance() != null && FacesContext.getCurrentInstance().getViewRoot() != null) {
            locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        }
        setParent(ResourceBundle.getBundle(BUNDLE_NAME, locale, new UTF8Control()));
    }

    @Override
    protected Object handleGetObject(String key) {
    	String val = parent.getString(key); 
		return new String(val.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    @Override
    public Enumeration<String> getKeys() {
        return parent.getKeys();
    }

    protected static class UTF8Control extends Control {

        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
                throws IllegalAccessException, InstantiationException, IOException {
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, BUNDLE_EXTENSION);
            ResourceBundle bundle = null;
            InputStream stream = null;
            if (reload) {
                URL url = loader.getResource(resourceName);
                if (url != null) {
                    URLConnection connection = url.openConnection();
                    if (connection != null) {
                        connection.setUseCaches(false);
                        stream = connection.getInputStream();
                    }
                }
            } else {
                stream = loader.getResourceAsStream(resourceName);
            }
            if (stream != null) {
                try {
                    bundle = new PropertyResourceBundle(new InputStreamReader(stream, StandardCharsets.UTF_8));
                } finally {
                    stream.close();
                }
            }
            return bundle;
        }
    }
}
