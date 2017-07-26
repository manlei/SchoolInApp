package cn.edu.nju.cs.seg.dao;

import cn.edu.nju.cs.seg.pojo.Essay;

import java.util.List;

/**
 * Created by Clypso on 2017/5/1.
 */
public interface EssayDao {

    public Essay findEssayById(int id);


    public List<Essay> findAllEssays();

    public List<Essay> findEssaysByStudioId(int studioId);

    public Essay add(Essay essay);

    public boolean remove(int id);

    public int incrementEssayHeat(int essayId);


}