package com.utils.HttpRequestAbstractDemo;

import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * 商品添加
 */
@Data
public class DouyinProductAdd extends BaseDouyinRequest {

    private String uri = "/product/add";
    private String method = "product.add";

    public DouyinProductAdd(DouyinProduct douyinProduct){
        param_json = JSON.toJSONString(douyinProduct);
    }

    @Data
    public static class DouyinProductAddResponse extends BaseDouyinResponse {
        private DouyinProduct data;
    }

    @Data
    public static class DouyinProduct {
        private String name;
        private String pic;
        private String description;
        private String out_product_id;
        private String market_price;
        private String cos_ratio;
        private String first_cid;
        private String second_cid;
        private String third_cid;
        private String pay_type;
        private String spec_id;
        private String mobile;
        private String weight;
        private String product_format;
    }
}
