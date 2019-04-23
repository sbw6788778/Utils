package com.utils.imageUtils;

import org.apache.poi.util.IOUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageUtils {
    /**
     * 判断是否gif图
     * @param urlStr
     * @return
     */
    public static boolean isGifFile(String urlStr) {
        InputStream inputStream = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(4 * 1000);
            //获得字节流
            inputStream = connection.getInputStream();
            int[] flags = new int[4];
            flags[0] = inputStream.read();
            flags[1] = inputStream.read();
            flags[2] = inputStream.read();
            flags[3] = inputStream.read();
            return flags[0] == 0x47 && flags[1] == 0x49 && flags[2] == 0x46 && flags[3] == 0x38;
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return false;
    }
}
