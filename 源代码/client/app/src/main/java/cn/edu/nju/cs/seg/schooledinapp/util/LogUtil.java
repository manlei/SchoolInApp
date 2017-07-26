package cn.edu.nju.cs.seg.schooledinapp.util;

import android.util.Log;

public class LogUtil {

    public static final int VERBOSE = 1;

    public static final int DEBUG = 2;

    public static final int INFO = 3;

    public static final int WARN = 4;

    public static final int ERROR = 5;

    public static final int NOTHING = 6;

    // 当前日志级别
    public static int level = VERBOSE;

    /**
     * 输出 VERBOSE 级别的 log
     *
     * @param tag 标签
     * @param msg log信息
     */
    public static void v(String tag, String msg) {
        if (level <= VERBOSE) {
            Log.v(tag, msg);
        }
    }

    /**
     * 输出 DEBUG 级别的 log
     *
     * @param tag 标签
     * @param msg log信息
     */
    public static void d(String tag, String msg) {
        if (level <= DEBUG) {
            Log.d(tag, msg);
        }
    }

    /**
     * 输出 INFO 级别的 log
     *
     * @param tag 标签
     * @param msg log信息
     */
    public static void i(String tag, String msg) {
        if (level <= INFO) {
            Log.i(tag, msg);
        }
    }

    /**
     * 输出 WARN 级别的 log
     *
     * @param tag 标签
     * @param msg log信息
     */
    public static void w(String tag, String msg) {
        if (level <= WARN) {
            Log.w(tag, msg);
        }
    }

    /**
     * 输出 ERROR 级别的 log
     *
     * @param tag 标签
     * @param msg log信息
     */
    public static void e(String tag, String msg) {
        if (level <= ERROR) {
            Log.e(tag, msg);
        }
    }

}
