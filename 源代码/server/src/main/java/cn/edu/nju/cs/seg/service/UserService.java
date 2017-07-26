package cn.edu.nju.cs.seg.service;

import cn.edu.nju.cs.seg.dao.UserDaoImpl;
import cn.edu.nju.cs.seg.pojo.*;
import cn.edu.nju.cs.seg.util.LuceneIndexUtil;
import org.apache.lucene.index.IndexWriter;

import java.io.IOException;
import java.util.List;

/**
 * Created by fwz on 2017/4/22.
 */
public class UserService {

    private static UserDaoImpl userDao = new UserDaoImpl();

//    @Autowired
//    private static UserDaoImpl userDao;

    public static boolean remove(int id) {
        return userDao.remove(id);
    }

    public static List<User> findAllUsers() {
        return userDao.findAll();
    }


    public static int add(User user) {
        User u = userDao.add(user);
        if (u != null) {
            IndexWriter indexWriter = null;
            try {
                indexWriter = LuceneIndexUtil.getIndexWriter();
                LuceneIndexUtil.addUser(indexWriter, u);
                indexWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return u.getId();
        }
        return 0;
    }

    public static void addFavoriteQuestion(int userId, int questionId) {
        userDao.addFavoriteQuestion(userId, questionId);
    }

    public static void addFavoriteAnswer(int userId, int answerId) {
        userDao.addFavoriteAnswer(userId, answerId);
    }

    public static void addFavoriteEssays(int userId, int essayId) {
        userDao.addFavoriteEssay(userId, essayId);
    }

    public static void addSupportQuestion(int userId, int questionId) {
        userDao.addSupportQuestion(userId, questionId);
    }

    public static void addSupportEssay(int userId, int essayId) {
        userDao.addSupportEssay(userId, essayId);
    }

    public static void updateUser(User user) throws IOException {
        IndexWriter indexWriter = LuceneIndexUtil.getIndexWriter();
        LuceneIndexUtil.updateUser(indexWriter, user);
        indexWriter.close();
        userDao.updateUser(user);
    }


    public static User findUserById(int userId) {
        return userDao.findUserById(userId);
    }


    public static User findUserByEmail(String email) {
        return userDao.findUserByEmail(email);
    }

    public static User findUserByPhone(String phone) {
        return userDao.findUserByPhone(phone);
    }

    public static User findUserByEmailOrPhone(String emailOrPhone) {
        if (emailOrPhone != null) {
            if (emailOrPhone.contains("@")) {
                return userDao.findUserByEmail(emailOrPhone);
            } else {
                return userDao.findUserByPhone(emailOrPhone);
            }
        }
        return null;
    }

    public static List<Studio> findStudiosByUserId(int userId) {
        return userDao.findStudiosByUserId(userId);
    }

    public static List<Question> findFavoriteQuestionsByUserId(int userId) {
        return userDao.findFavoriteQuestionsByUserId(userId);
    }

    public static List<Answer> findFavoriteAnswersByUserId(int userId) {
        return userDao.findFavoriteAnswersByUserId(userId);
    }

    public static List<Essay> findFavoriteEssayByUserId(int userId) {
        return userDao.findFavoriteEssaysByUserId(userId);
    }

    public static List<Question> findSupportedQuestionsByUserId(int userId) {
        return userDao.findSupportQuestionsByUserId(userId);
    }

    public static List<Essay> findSupportedEssaysByUserId(int userId) {
        return userDao.findSupportEssaysByUserId(userId);
    }


    public static List<Object> findSupportsByUserId(int userId, int offset, int limit) {
        return userDao.findSupportsByUserId(userId, offset, limit);
    }

    public static void removeFavoriteQuestion(int userId, int questionId) {
        userDao.removeFavoriteQuestion(userId, questionId);

    }

    public static void removeFavoriteAnswer(int userId, int answerId) {
        userDao.removeFavoriteAnswers(userId, answerId);
    }

    public static void removeFavoriteEssay(int userId, int essayId) {
        userDao.removeFavoriteEssays(userId, essayId);
    }
}
