package com.sbw.simple.web.server.v2;

import java.io.File;

public class Constants {
    public static final String WEB_ROOT =
            System.getProperty("user.dir") + File.separator + "simple_web_server" + File.separator + "src"
                    + File.separator + "main" + File.separator + "resources";
    public static final String SERVLET_ROOT =
            System.getProperty("user.dir") + File.separator + "simple_web_server" + File.separator + "src"
                    + File.separator + "main" + File.separator + "java"
                    + File.separator + "com" + File.separator + "sbw"
                    + File.separator + "simple" + File.separator + "web"
                    + File.separator + "server" + File.separator + "servlet";
}