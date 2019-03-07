package com.utils.JsonUtils;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

public class JsonUtils {
    @Test
    public void parseJson(){
        String json="{" +
                "\t\"call_time\": \"1534817004000\",\n" +
                "\t\"method\": \"getBookBang\",\n" +
                "\t\"sid\": \"1002\",\n" +
                "\t\"source\": \"f7323830aeddc20fbecb254561375d12\",\n" +
                "\t\"topNum\": \"10\"" +
                "}";
        JSONObject jsonObject = JSONObject.parseObject(json);
        String call_time = jsonObject.getString("call_time");
        System.out.println(call_time);

    }
}
