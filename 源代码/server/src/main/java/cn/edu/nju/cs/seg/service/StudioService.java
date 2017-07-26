package cn.edu.nju.cs.seg.service;

import cn.edu.nju.cs.seg.dao.StudioDaoImpl;
import cn.edu.nju.cs.seg.pojo.Studio;

import cn.edu.nju.cs.seg.pojo.User;
import cn.edu.nju.cs.seg.util.LuceneIndexUtil;
import org.apache.lucene.index.IndexWriter;


import java.io.IOException;
import java.util.List;

/**
 * Created by Clypso on 2017/5/2.
 */
public class StudioService {
    private static StudioDaoImpl dao = new StudioDaoImpl();

    public static List<Studio> findAllStudios() {
        return dao.findAll();
    }

    public static boolean remove(int id) {
        Studio studio = StudioService.findStudioById(id);
        IndexWriter indexWriter = null;
        try {
            indexWriter = LuceneIndexUtil.getIndexWriter();
            LuceneIndexUtil.deleteStudio(indexWriter, studio);
            indexWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dao.remove(id);
    }

    public static void removeMember(int studioId, int userId) {
        dao.removeMember(studioId, userId);
    }

    public static int add(Studio studio) {
        Studio s = dao.add(studio);
        if (s != null) {
            IndexWriter indexWriter = null;
            try {
                indexWriter = LuceneIndexUtil.getIndexWriter();
                LuceneIndexUtil.addStudio(indexWriter, s);
                indexWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return s.getId();
        }
        return 0;

    }

    public static void addMember(int studioId, int userId) {
        dao.addMembers(studioId, userId);
    }

    public static Studio findStudioById(int studioId) {
        return dao.findStudioById(studioId);
    }

    public static Studio findStudioByName(String studioName) {
        return dao.findStudioByName(studioName);
    }

    public static void updateStudio(Studio studio) throws IOException {
        IndexWriter indexWriter = LuceneIndexUtil.getIndexWriter();
        LuceneIndexUtil.updateStudio(indexWriter, studio);
        indexWriter.close();
        dao.updateStudio(studio);
    }

    public static List<Studio> findStudiosByUserId(int userId) {
        return dao.findStudiosByUserId(userId);
    }

    public static List<User> findUsersByStudioId(int studioId) {
        return dao.findUsersByStudioId(studioId);
    }
}


