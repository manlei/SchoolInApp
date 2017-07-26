package cn.edu.nju.cs.seg.dao;

import cn.edu.nju.cs.seg.pojo.Question;

import java.util.List;

/**
 * Created by fwz on 2017/5/1.
 */
public interface QuestionDao {
    public boolean remove(int id);

    public Question add(Question question);

    public Question findQuestionById(int id);

    public List<Question> findAll();

    public Question findQuestionByTitle(String title);

    public List<Question> findQuestionsByUserId(int userId);

    public List<Question> findQuestionsByStudioId(int studioId);

    public int incrementQuestionHeat(int questionId);

}
