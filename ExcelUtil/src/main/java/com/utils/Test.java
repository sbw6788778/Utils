package com.utils;

import com.google.common.collect.Lists;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) {
        String tmall9ProductCategory="\\u7ae5\\u4e66:\\u7ae5\\u4e66;\\u5c0f\\u8bf4:\\u6587\\u827a;\\u9752\\u6625\\u6587\\u5b66:\\u6587\\u827a;\\u6587\\u5b66:\\u6587\\u827a;\\u4f20\\u8bb0:\\u6587\\u827a;\\u827a\\u672f:\\u6587\\u827a;\\u52a8\\u6f2b/\\u5e7d\\u9ed8:\\u6587\\u827a;\\u6210\\u529f/\\u52b1\\u5fd7:\\u7ecf\\u7ba1;\\u7ba1\\u7406:\\u7ecf\\u7ba1;\\u6295\\u8d44\\u7406\\u8d22:\\u7ecf\\u7ba1;\\u7ecf\\u6d4e:\\u7ecf\\u7ba1;\\u5fc3\\u7406\\u5b66:\\u793e\\u79d1;\\u53e4\\u7c4d:\\u793e\\u79d1;\\u54f2\\u5b66/\\u5b97\\u6559:\\u793e\\u79d1;\\u6cd5\\u5f8b:\\u793e\\u79d1;\\u793e\\u4f1a\\u79d1\\u5b66:\\u793e\\u79d1;\\u5386\\u53f2:\\u793e\\u79d1;\\u653f\\u6cbb/\\u519b\\u4e8b:\\u793e\\u79d1;\\u6587\\u5316:\\u793e\\u79d1;\\u6559\\u80b2:\\u6559\\u80b2;\\u5916\\u8bed:\\u6559\\u80b2;\\u4e2d\\u5c0f\\u5b66\\u6559\\u8f85:\\u6559\\u80b2;\\u6559\\u6750:\\u6559\\u80b2;\\u8003\\u8bd5:\\u6559\\u80b2;\\u5de5\\u5177\\u4e66:\\u6559\\u80b2;\\u79d1\\u666e\\u8bfb\\u7269:\\u79d1\\u6280;\\u533b\\u5b66:\\u79d1\\u6280;\\u8ba1\\u7b97\\u673a/\\u7f51\\u7edc:\\u79d1\\u6280;\\u81ea\\u7136\\u79d1\\u5b66:\\u79d1\\u6280;\\u5efa\\u7b51:\\u79d1\\u6280;\\u519c\\u4e1a/\\u6797\\u4e1a:\\u79d1\\u6280;\\u5de5\\u4e1a\\u6280\\u672f:\\u79d1\\u6280;\\u4eb2\\u5b50/\\u5bb6\\u6559:\\u751f\\u6d3b;\\u4fdd\\u5065/\\u517b\\u751f:\\u751f\\u6d3b;\\u98ce\\u6c34/\\u5360\\u535c:\\u751f\\u6d3b;\\u70f9\\u996a/\\u7f8e\\u98df:\\u751f\\u6d3b;\\u4e24\\u6027\\u5173\\u7cfb:\\u751f\\u6d3b;\\u80b2\\u513f/\\u65e9\\u6559:\\u751f\\u6d3b;\\u65f6\\u5c1a/\\u7f8e\\u5986:\\u751f\\u6d3b;\\u5b55\\u4ea7/\\u80ce\\u6559:\\u751f\\u6d3b;\\u65c5\\u6e38/\\u5730\\u56fe:\\u751f\\u6d3b;\\u624b\\u5de5/DIY:\\u751f\\u6d3b;\\u4f11\\u95f2/\\u7231\\u597d:\\u751f\\u6d3b;\\u4f53\\u80b2/\\u8fd0\\u52a8:\\u751f\\u6d3b;\\u751f\\u6d3b:\\u751f\\u6d3b;\\u5bb6\\u5ead/\\u5bb6\\u5c45:\\u751f\\u6d3b;\\u513f\\u7ae5\\u97f3\\u4e50:\\u5176\\u4ed6;\\u529f\\u80fd\\u97f3\\u4e50:\\u5176\\u4ed6;\\u620f\\u5267/\\u7efc\\u827a:\\u5176\\u4ed6;\\u82f1\\u6587\\u539f\\u7248\\u4e66:\\u5176\\u4ed6;\\u534e\\u8bed\\u6d41\\u884c:\\u5176\\u4ed6;\\u7535\\u5f71:\\u5176\\u4ed6;\\u5f71\\u89c6\\u97f3\\u4e50:\\u5176\\u4ed6;\\u4e13\\u9898\\u7247/\\u4e13\\u680f\\u8282\\u76ee:\\u5176\\u4ed6;\\u53e4\\u5178\\u97f3\\u4e50:\\u5176\\u4ed6;\\u7535\\u89c6\\u5267:\\u5176\\u4ed6;\\u5361\\u901a:\\u5176\\u4ed6;\\u6709\\u58f0\\u8bfb\\u7269:\\u5176\\u4ed6;\\u8f7b\\u97f3\\u4e50:\\u5176\\u4ed6;\\u6c11\\u6b4c:\\u5176\\u4ed6;\\u6c7d\\u8f66:\\u5176\\u4ed6;\\u6587\\u6458 \\u6587\\u5b66:\\u5176\\u4ed6;\\u6e2f\\u53f0\\u5716\\u66f8:\\u5176\\u4ed6";
        String[] productCategoryArr = tmall9ProductCategory.split(";");
        Map<String, String> productCategoryMap = new HashMap<>();
        for(String s : productCategoryArr){
            String[] arr = s.split(":");
            productCategoryMap.put(arr[0], arr[1]);
        }
        System.out.println("");
    }

}