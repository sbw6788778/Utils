package com.utils.packageUtile;



import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class ZipUtil {
    /**
     * 压缩目录下的所有文件
     *
     * @param sourceFilePath 需要压缩的目录
     * @param zipFilePath    压缩包存储路径
     * @param fileName       压缩包名
     * @return 压缩包绝对路径
     */
    public static String fileToZip(String sourceFilePath, String zipFilePath, String fileName) {
        File sourceFile = new File(sourceFilePath);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        File zipFile = null;
        if (!sourceFile.exists()) {
            return "";
        }
        try {
            zipFile = new File(zipFilePath + "/" + fileName + ".zip");
            if (zipFile.exists()) {
                return zipFile.getAbsolutePath();
            }
            File[] sourceFiles = sourceFile.listFiles();
            if (null == sourceFiles || sourceFiles.length < 1) {
                return "";
            }
            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(new BufferedOutputStream(fos));
            byte[] bufs = new byte[1024 * 10];
            for (int i = 0; i < sourceFiles.length; i++) {
                //创建ZIP实体，并添加进压缩包
                ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
                zos.putNextEntry(zipEntry);
                //读取待压缩的文件并写进压缩包里
                fis = new FileInputStream(sourceFiles[i]);
                bis = new BufferedInputStream(fis, 1024 * 10);
                int read = 0;
                while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
                    zos.write(bufs, 0, read);
                }
            }

        } catch (Exception e) {
            return "";
        } finally {
            //关闭流
            try {
                if (null != bis) bis.close();
                if (null != zos) zos.close();
            } catch (IOException e) {
            }
        }
        return zipFile.getAbsolutePath();
    }
}
