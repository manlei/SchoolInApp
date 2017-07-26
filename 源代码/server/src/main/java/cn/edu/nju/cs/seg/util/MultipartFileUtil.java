package cn.edu.nju.cs.seg.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by fwz on 2017/7/14.
 */
public class MultipartFileUtil {
    public String save(MultipartFile file, String baseDir) throws IOException, NoSuchAlgorithmException {
        String md5 = MD5Util.getMultipartFileMD5(file);
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(
                originalFilename.lastIndexOf("."));
//        names.add(md5 + suffix);
        file.transferTo(new File(baseDir + md5 + suffix));
        return md5;
    }
}
