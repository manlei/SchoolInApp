package cn.edu.nju.cs.seg.json;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

/**
 * Created by fwz on 2017/5/29.
 */
public class JsonMapBuilder {
    private Map<String, Object> buffer;
    private ObjectMapper mapper;

    public JsonMapBuilder() {
        buffer = new HashMap<String, Object>();
        mapper = new ObjectMapper();
    }

    public Map<String, Object> getMap() {
        return buffer;
    }

    public JsonMapBuilder append(String key, Object value) {
        buffer.put(key, value);
        return this;
    }

    public int count() {
        return buffer.size();
    }

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(buffer);
        } catch (JsonProcessingException e) {
//            e.printStackTrace();
            return "";
        }
    }
}
