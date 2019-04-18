package com.utils;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class Test {
    private static final Logger logger = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) {
        Map<Integer, String> map = Maps.newHashMap();
        map.put(1, "http://img3x9.ddimg.cn/50/26/23835389-1_e.jpg");
        map.put(3, "http://img3x9.ddimg.cn/50/26/23835389-1_e.jpg");
        map.put(4, "http://img3x9.ddimg.cn/50/26/23835389-1_e.jpg");
        map.put(6, "http://img3x9.ddimg.cn/50/26/23835389-1_e.Png");
        map.put(5, "http://img3x9.ddimg.cn/50/26/23835389-1_e.gif");
        map.put(2, "http://img3x9.ddimg.cn/50/26/23835389-2_e.png");
        String imageUrl = Joiner.on("|").join(map.values().stream().filter(e -> filterImage(e)).limit(5L).collect(Collectors.toList()));

        System.out.println("a是".length());


    }

    private static boolean filterImage(String img) {
        if (StringUtils.isBlank(img)) {
            return false;
        }
        String fomate = img.substring(img.lastIndexOf(".") + 1).toLowerCase();
        return fomate.equals("png") || fomate.equals("jpg") || fomate.equals("jpeg");
    }

}
