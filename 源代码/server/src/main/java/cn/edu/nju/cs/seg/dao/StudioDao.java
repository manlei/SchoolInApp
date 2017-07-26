package cn.edu.nju.cs.seg.dao;

import cn.edu.nju.cs.seg.pojo.Studio;

import cn.edu.nju.cs.seg.pojo.User;

import java.util.List;

/**
 * Created by Clypso on 2017/5/1.
 */

public interface StudioDao {

    public boolean remove(int id);

    public void removeMember(int studioId, int memberId);

    public Studio add(Studio studio);

    public void addMembers(int studioId, int userId);

    public Studio findStudioById(int id);

    public Studio findStudioByName(String name);

    public List<Studio> findAll();

    public void updateStudio(Studio studio);

    public List<Studio> findStudiosByUserId(int userId);

    public List<User> findUsersByStudioId(int studioId);


}