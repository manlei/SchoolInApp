package cn.edu.nju.cs.seg.dao;

import cn.edu.nju.cs.seg.pojo.*;

import java.util.List;

/**
 * Created by fwz on 2017/4/22.
 */
public interface UserDao {

    public boolean remove(int id);

    public User add(User user);

    public void addFavoriteQuestion(int userId, int questionId);

    public void addFavoriteAnswer(int userId, int answerId);

    public void addFavoriteEssay(int userId, int essayId);

    public void addSupportQuestion(int userId, int questionId);

    public void addSupportEssay(int userId, int essayId);

    public void updateUser(User user);

    public User findUserById(int id);

    public List<User> findAll();

    public User findUserByPhone(String phone);

    public User findUserByEmail(String email);

    public List<Studio> findStudiosByUserId(int userId);

    public List<Question> findFavoriteQuestionsByUserId(int userId);

    public List<Answer> findFavoriteAnswersByUserId(int userId);

    public List<Essay> findFavoriteEssaysByUserId(int userId);

    public List<Question> findSupportQuestionsByUserId(int userId);

    public List<Essay> findSupportEssaysByUserId(int userId);

    public List<Object> findSupportsByUserId(int userId, int offset, int limit);

    public void removeFavoriteQuestion(int userId, int questionId);

    public void removeFavoriteAnswers(int userId, int answerId);

    public void removeFavoriteEssays(int userId, int essayId);


}
