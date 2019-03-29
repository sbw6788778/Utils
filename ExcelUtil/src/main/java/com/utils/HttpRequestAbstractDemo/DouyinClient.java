package com.utils.HttpRequestAbstractDemo;


import com.utils.HttpUtils.HttpsUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * 抖音api请求封装
 */
@Slf4j
public class DouyinClient {

    private String douyinServerUrl = "https://openapi.jinritemai.com";
    private String appKey = "3336519933084480647";
    private String appSecret = "3ac42b97ef30500d04ea787705694084";

    /**
     * @param uri
     * @param req
     * @return
     * @throws Exception
     */

    public String execute(String uri, BaseDouyinRequest req){
        Map<String, String> params = req.getParams();
        params.put("app_key", appKey);
        params.put("sign", getSign(params, appSecret));
        log.info("execute params={}",params);
        return  HttpsUtils.httpsPost(douyinServerUrl + uri,params,"UTF-8");
    }

    private String getSign(Map<String, String> params, String clientSecret) {
        Set<String> SortedParames = new TreeSet<>(params.keySet());
        StringBuilder result = new StringBuilder();
        for (String key : SortedParames) {
            String value = params.get(key).toString();
            result.append(key).append(value);
        }
        return DigestUtils.md5Hex(clientSecret + result + clientSecret);
    }

}
