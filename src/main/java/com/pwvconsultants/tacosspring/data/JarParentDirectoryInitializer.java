package com.pwvconsultants.tacosspring.data;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import com.pwvconsultants.tacosspring.TacosSpringApplication;

public class JarParentDirectoryInitializer {

    public static String getThisJarsParentDirectory() {
        URL url = TacosSpringApplication.class.getProtectionDomain().getCodeSource().getLocation();
        return getParentDirectoryFromUrl(url);
    }

    private static String getParentDirectoryFromUrl(URL url) {
        if (url != null) {
            URI uri = getUriFromUrl(url);
            return getParentDirectoryFromUri(uri);
        }
        return "";
    }

    private static URI getUriFromUrl(URL url) {
        try {
            return url.toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getParentDirectoryFromUri(URI uri) {
        if (uri != null) {
            String uriPath = uri.getPath();
            return getParentDirectoryFromUriPath(uriPath);
        }
        return "";
    }

    private static String getParentDirectoryFromUriPath(String uriPath) {
        if (uriPath != null) {
            File file = new File(uriPath);
            return getParentDirectoryFromFile(file);
        }
        return "";
    }

    private static String getParentDirectoryFromFile(File file) {
        if (file != null)
            return file.getParent();
        return "";
    }
}
