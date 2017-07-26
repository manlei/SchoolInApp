package cn.edu.nju.cs.seg.dao;

import cn.edu.nju.cs.seg.pojo.Avatar;

/**
 * Created by fwz on 2017/7/8.
 */
public interface AvatarDao {
    public Avatar findAvatarById(long id);

    public Avatar findAvatarByMd5(String md5);

    public Avatar add(Avatar avatar);


}
