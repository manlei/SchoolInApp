package cn.edu.nju.cs.seg.service;

import cn.edu.nju.cs.seg.dao.EssayDaoImpl;
import cn.edu.nju.cs.seg.pojo.Essay;
import cn.edu.nju.cs.seg.util.LuceneIndexUtil;
import org.apache.lucene.index.IndexWriter;

import java.io.IOException;
import java.util.List;

/**
 * Created by Clypso on 2017/5/5.
 */
public class EssayService {

    private static EssayDaoImpl dao = new EssayDaoImpl();

    public static Essay findEssayById(int id) {
        return dao.findEssayById(id);
    }

    public static List<Essay> findAllEssays() {
        return dao.findAllEssays();
    }

    public static List<Essay> findEssaysByStudioId(int studioId) {
        return dao.findEssaysByStudioId(studioId);
    }

    public static int add(Essay essay) {
        Essay a = dao.add(essay);
        if (a != null) {
            IndexWriter indexWriter = null;
            try {
                indexWriter = LuceneIndexUtil.getIndexWriter();
                LuceneIndexUtil.addEssay(indexWriter, a);
                indexWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return a.getId();
        }
        return 0;
    }


    public static boolean remove(int id) {
        Essay essay = EssayService.findEssayById(id);
        IndexWriter indexWriter = null;
        try {
            indexWriter = LuceneIndexUtil.getIndexWriter();
            LuceneIndexUtil.deleteEssay(indexWriter, essay);
            indexWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dao.remove(id);
    }

    public static int incrementEssayHeat(int essayId) {
        return dao.incrementEssayHeat(essayId);
    }

}
