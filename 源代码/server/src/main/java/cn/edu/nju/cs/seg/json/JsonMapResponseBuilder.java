package cn.edu.nju.cs.seg.json;

import java.util.Map;

/**
 * Created by fwz on 2017/5/30.
 */
public interface JsonMapResponseBuilder {
    public Map<String, Object> getSimpleMap();
    public Map<String, Object> getComplexMap();

}
