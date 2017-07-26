package cn.edu.nju.cs.seg.dao;

import cn.edu.nju.cs.seg.pojo.Answer;

import java.util.List;

/**
 * Created by fwz on 2017/5/1.
 */
public interface AnswerDao {

    public boolean remove(int id);

    public Answer add(Answer answer);

    public Answer findAnswerById(int id);

    public List<Answer> findAll();

    public List<Answer> findAnswersByQuestionId(int questionId);

    public List<Answer> findAnswersByStudioId(int studioId);

    public List<Answer> findAnswersByUserId(int userId);

}
