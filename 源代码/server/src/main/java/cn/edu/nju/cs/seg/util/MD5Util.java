package cn.edu.nju.cs.seg.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fwz on 2017/6/9.
 */
public class MD5Util {


    public static String getMultipartFileMD5(MultipartFile file)
            throws IOException, NoSuchAlgorithmException {
        if (file == null && !file.isEmpty()) {
            return null;
        }
        byte[] fileBytes = file.getBytes();
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] digest = md5.digest(fileBytes);
        System.out.println(digest);
        String hashString = new BigInteger(1, digest).toString(16);
        return hashString;
    }

    /**
     * 根据文件计算出文件的MD5
     * @param file
     * @return
     */
    public static String getFileMD5(File file) {
        if (file != null && !file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());

        return bigInt.toString(16);
    }

    /**
     * 获取文件夹中的文件的MD5值
     * @param file
     * @param listChild
     * @return
     */
    public static Map<String,String> getDirMD5(File file, boolean listChild){
        if(! file.isDirectory()){
            return null;
        }

        Map<String, String> map = new HashMap<String, String>();
        String md5;

        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file2 = files[i];
            if(file2.isDirectory() && listChild){
                map.putAll(getDirMD5(file2, listChild));
            }else{
                md5 = getFileMD5(file2);
                if(md5 != null){
                    map.put(file2.getPath(), md5);
                }
            }
        }
        return map;
    }
}
