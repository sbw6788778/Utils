package com.utils.HttpRequestAbstractDemo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import org.apache.commons.lang.time.FastDateFormat;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 抖音入参基类
 *
 * @author chenlongjs
 */
@Data
public class BaseDouyinRequest implements Serializable {
    List<String> signParam = Lists.newArrayList("app_key","method","param_json","v","timestamp");
    private FastDateFormat fmt = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
    private String v = "1";
    private String timestamp = fmt.format(new Date());
    protected String param_json = "{}";

    public Map<String, String> getParams() {
        Map<String, String> result = Maps.newTreeMap();
        List<Field> fields = Lists.newArrayList(this.getClass().getSuperclass().getDeclaredFields());
        fields.addAll(Lists.newArrayList(this.getClass().getDeclaredFields()));
        for (Field field : fields) {
            if (!signParam.contains(field.getName())){
                continue;
            }
            try {
                field.setAccessible(true);
                Object obj = field.get(this);
                if (obj != null) {
                    result.put(field.getName(), obj.toString());
                }
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            } finally {
                field.setAccessible(false);
            }
        }
        return result;
    }
}
