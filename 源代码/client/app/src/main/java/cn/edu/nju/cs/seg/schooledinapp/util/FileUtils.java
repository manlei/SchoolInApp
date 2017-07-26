package cn.edu.nju.cs.seg.schooledinapp.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

import cn.edu.nju.cs.seg.schooledinapp.AppConfig;

/**
 * 文件管理类
 *
 * Reference: https://github.com/scsfwgy/WeixinRecord
 *
 */
public class FileUtils {

    // 获取文件存放根路径
    public static File getAppDir(Context context) {
        String dirPath = "";

        // SD卡是否存在
        boolean isSdCardExists = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        boolean isRootDirExists = Environment.getExternalStorageDirectory().exists();

        if (isSdCardExists && isRootDirExists) {
            dirPath = String.format("%s/%s/",
                    Environment.getExternalStorageDirectory().getAbsolutePath(),
                    AppConfig.APP_ROOT_PATH);
        } else {
            dirPath = String.format("%s/%s/",
                    context.getApplicationContext().getFilesDir().getAbsolutePath(),
                    AppConfig.APP_ROOT_PATH);
        }

        File appDir = new File(dirPath);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }

        return appDir;
    }

    // 获取录音存放路径
    public static File getAppRecordDir(Context context) {
        File appDir = getAppDir(context);
        File recordDir = new File(appDir.getAbsolutePath(), AppConfig.APP_RECORD_DIR);
        if (!recordDir.exists()) {
            recordDir.mkdir();
        }
        return recordDir;
    }

    // 删除录音文件
    public static void deleteRecordFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return ;
        } else {
            if (file.isFile()) {
                file.delete();
            }
        }
    }

}
