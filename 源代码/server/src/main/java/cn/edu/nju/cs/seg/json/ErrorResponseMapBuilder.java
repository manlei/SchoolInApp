package cn.edu.nju.cs.seg.json;

import cn.edu.nju.cs.seg.ServerConfig;

import java.util.Map;

/**
 * Created by fwz on 2017/5/29.
 */
public class ErrorResponseMapBuilder {
    public static Map<String, Object> build(String msg) {
        Map<String, Object> map = new JsonMapBuilder()
                .append("api_url", ServerConfig.SERVER_BASE_URL)
                .append("message", msg)
                .getMap();
        return map;
    }
}
