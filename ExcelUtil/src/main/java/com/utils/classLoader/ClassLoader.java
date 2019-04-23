package com.utils.classLoader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

public class ClassLoader {
    public static void main(String[] args) {
        URL[] urls = new URL[1];
        URLStreamHandler streamHandler = null;
        try {
            urls[0] = new URL(null, "file:E:\\code\\Utils\\simple_web_server\\src\\main\\resources", streamHandler);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URLClassLoader loader = new URLClassLoader(urls);
        try {
            Class aClass = loader.loadClass("PrimitiveServlet");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
