package com.utils.HttpRequestAbstractDemo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * 抖音返参基类
 *
 * @author chenlongjs
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseDouyinResponse implements Serializable {
    private String err_no;
    private String message;
}
