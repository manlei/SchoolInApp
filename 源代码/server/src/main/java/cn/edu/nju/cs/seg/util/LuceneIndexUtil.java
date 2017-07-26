package cn.edu.nju.cs.seg.util;

import cn.edu.nju.cs.seg.ServerConfig;
import cn.edu.nju.cs.seg.pojo.Essay;
import cn.edu.nju.cs.seg.pojo.Question;
import cn.edu.nju.cs.seg.pojo.Studio;
import cn.edu.nju.cs.seg.pojo.User;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.nio.file.FileSystems;

/**
 * Created by fwz on 2017/6/26.
 */
public class LuceneIndexUtil {

    public static IndexWriter getIndexWriter() throws IOException {
        StandardAnalyzer analyzer = new StandardAnalyzer();

        // 1. create the index
        Directory directory = FSDirectory.open(
                FileSystems.getDefault().getPath(ServerConfig.LUCENE_INDEX_DIRECTORY));
//        Directory index = new RAMDirectory();

        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        return new IndexWriter(directory, config);

    }

    public static void addUser(IndexWriter indexWriter, User user) throws IOException {
        if (indexWriter != null && user != null) {
            Document doc = new Document();
            doc.add(new StringField("userId", user.getId() + "", Field.Store.YES));
            doc.add(new TextField("username", user.getUsername(), Field.Store.YES));
            indexWriter.addDocument(doc);
        }
    }

    public static void updateUser(IndexWriter indexWriter, User user) throws IOException {
        if (indexWriter != null && user != null) {
            Document doc = new Document();
            doc.add(new StringField("userId", user.getId() + "", Field.Store.YES));
            doc.add(new TextField("username", user.getUsername(), Field.Store.YES));
            indexWriter.updateDocument(new Term("userId", user.getId() + ""), doc);
//            indexWriter.flush();
            indexWriter.commit();
//            indexWriter.close();
        }
    }


    public static void addStudio(IndexWriter indexWriter, Studio studio) throws IOException {
        if (indexWriter != null && studio != null) {
            Document doc = new Document();
            doc.add(new StringField("studioId", studio.getId() + "", Field.Store.YES));
            doc.add(new TextField("studioName", studio.getName(), Field.Store.YES));
            indexWriter.addDocument(doc);
        }
    }

    public static void updateStudio(IndexWriter indexWriter, Studio studio) throws IOException {
        if (indexWriter != null && studio != null) {
            Document doc = new Document();
            doc.add(new StringField("studioId", studio.getId() + "", Field.Store.YES));
            doc.add(new TextField("studioName", studio.getName(), Field.Store.YES));
            indexWriter.updateDocument(new Term("studioId", studio.getId() + ""), doc);
//            indexWriter.flush();
            indexWriter.commit();
//            indexWriter.close();
        }
    }

    public static void deleteStudio(IndexWriter indexWriter, Studio studio) throws IOException {
        if (indexWriter != null && studio != null) {
            indexWriter.deleteDocuments(new Term("studioId", studio.getId() + ""));
            indexWriter.commit();
//        indexWriter.close();
        }
    }

    public static void addQuestion(IndexWriter indexWriter, Question question) throws IOException {
        if (indexWriter != null && question != null) {
            Document doc = new Document();
            doc.add(new StringField("questionId", question.getId() + "", Field.Store.YES));
            doc.add(new TextField("questionTitle", question.getTitle(), Field.Store.YES));
            String content = question.getContent();
            if (content != null && content.length() > 0) {
                org.jsoup.nodes.Document document = Jsoup.parse(question.getContent());
                doc.add(new TextField(
                        "questionContent", document.body().text(), Field.Store.YES));
            }
            indexWriter.addDocument(doc);
        }
    }

    public static void deleteQuestion(IndexWriter indexWriter, Question question) throws IOException {
        if (indexWriter != null && question != null) {
            indexWriter.deleteDocuments(new Term("questionId", question.getId() + ""));
            indexWriter.commit();
        }
    }

    public static void addEssay(IndexWriter indexWriter, Essay essay) throws IOException {
        if (indexWriter != null && essay != null) {
            Document doc = new Document();
            doc.add(new StringField("essayId", essay.getId() + "", Field.Store.YES));
            doc.add(new TextField("essayTitle", essay.getTitle(), Field.Store.YES));
            String content = essay.getContent();
            if (content != null && content.length() > 0) {
                org.jsoup.nodes.Document document = Jsoup.parse(essay.getContent());
                doc.add(new TextField(
                        "essayContent", document.body().text(), Field.Store.YES));
            }
            indexWriter.addDocument(doc);
        }
    }

    public static void deleteEssay(IndexWriter indexWriter, Essay essay) throws IOException {
        if (indexWriter != null && essay != null) {
            indexWriter.deleteDocuments(new Term("essayId", essay.getId() + ""));
            indexWriter.commit();
        }
    }
}
