package cn.edu.nju.cs.seg.schooledinapp.util;

import android.support.annotation.VisibleForTesting;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Json 形式的 RequestBody 辅助构建类，使用方式如下：
 *
 * RequestBody body = new JsonRequestBodyBuilder()
 *     .append("user_email_or_phone", emailOrPhone)
 *     .append("user_password", password)
 *     .build();
 *
 */
public class JsonRequestBodyBuilder {

    private Map<String, Object> buffer;

    private Gson gson;

    public JsonRequestBodyBuilder() {
        buffer = new HashMap<>();
        gson = builder().create();
    }

    /**
     * 添加域
     *
     * @param key 键
     * @param value 值
     * @return 为支持链状，返回自身
     */
    public JsonRequestBodyBuilder append(String key, Object value) {
        buffer.put(key, value);
        return this;
    }

    /**
     * 获取已存放(key, value)的个数
     *
     * @return 个数
     */
    public int count() {
        return buffer.size();
    }

    /**
     * 构造 RequestBody ，同时清空 buffer
     *
     * @return RequestBody
     */
    public RequestBody build() {
        RequestBody body;

        body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                gson.toJson(buffer));

        buffer = new HashMap<>();

        return body;
    }

    /**
     * 设置好看的输出
     *
     * @return 为支持链状，返回自身
     */
    public JsonRequestBodyBuilder prettify() {
        gson = builder().setPrettyPrinting().create();
        return this;
    }

    /**
     * 设置不好看的输出
     *
     * @return 为支持链状，返回自身
     */
    public JsonRequestBodyBuilder uglify() {
        gson = builder().create();
        return this;
    }

    /**
     * 输出
     *
     * @param writer 输出句柄
     * @return 为支持链状，返回自身
     */
    public JsonRequestBodyBuilder out(Writer writer) {
        gson.toJson(buffer, writer);
        return this;
    }

    /**
     * 标准输出
     *
     * @return 为支持链状，返回自身
     */
    public JsonRequestBodyBuilder out() {
        gson.toJson(buffer, System.out);
        return this;
    }

    /**
     * 初始化一个 GsonBuilder
     *
     * @return GsonBuilder
     */
    private GsonBuilder builder() {
        return new GsonBuilder()
                .serializeNulls()
                .setDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    }

    @VisibleForTesting
    public Map<String, Object> getBuffer() {
        return buffer;
    }

}