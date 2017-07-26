package cn.edu.nju.cs.seg.service;

import cn.edu.nju.cs.seg.dao.AvatarDaoImpl;
import cn.edu.nju.cs.seg.pojo.Avatar;

/**
 * Created by fwz on 2017/7/8.
 */
public class AvatarService {
    private static AvatarDaoImpl dao = new AvatarDaoImpl();

    public static Avatar findAvatarById(long id) {
        return dao.findAvatarById(id);
    }

    public static Avatar findAvatarByMd5(String md5) {
        return dao.findAvatarByMd5(md5);
    }

    public static Avatar add(Avatar avatar) {
        return dao.add(avatar);
    }
}
