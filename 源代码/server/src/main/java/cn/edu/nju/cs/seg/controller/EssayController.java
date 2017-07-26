package cn.edu.nju.cs.seg.controller;

import cn.edu.nju.cs.seg.ServerConfig;
import cn.edu.nju.cs.seg.exception.BusinessException;
import cn.edu.nju.cs.seg.json.JsonMapBuilder;
import cn.edu.nju.cs.seg.json.JsonMapResponseBuilderFactory;
import cn.edu.nju.cs.seg.pojo.*;
import cn.edu.nju.cs.seg.service.*;
import cn.edu.nju.cs.seg.util.MD5Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Clypso on 2017/5/5.
 */

@RestController
@RequestMapping("/api")
public class EssayController {

    @RequestMapping(value = "/essays/{essayId}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getEssay(@PathVariable("essayId") int essayId) {
        Essay essay = EssayService.findEssayById(essayId);
        if (essay != null) {
            EssayService.incrementEssayHeat(essayId);
            Map<String, Object> m = JsonMapResponseBuilderFactory
                    .createEssayJsonMapBuilder(essay)
                    .getComplexMap();
            return new ResponseEntity<Map<String, Object>>(m, HttpStatus.OK);
        } else {
            throw new BusinessException("Essay not found", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/essays", method = RequestMethod.GET)
    public ResponseEntity<List<Map<String, Object>>> getEssays(HttpServletRequest request) throws ParseException, IOException {

        List<Map<String, Object>> l = new ArrayList<>();
        int limit = 50;
        int offset = 0;
        try {
            limit = Integer.parseInt(request.getParameter("limit"));
        } catch (Exception e) {
            limit = 50;
        }
        try {
            offset = Integer.parseInt(request.getParameter("offset"));
        } catch (Exception e) {
            offset = 0;
        }

        String search = request.getParameter("search");
        if (search != null && search.length() > 0) {
            Analyzer analyzer = new StandardAnalyzer();
            Query q1 = new QueryParser("essayTitle", analyzer).parse(search);
            Query q2 = new QueryParser("essayContent", analyzer).parse(search);
            int hitsPerPage = limit;
            Directory directory = FSDirectory.open(
                    FileSystems.getDefault().getPath(ServerConfig.LUCENE_INDEX_DIRECTORY));
            IndexReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher1 = new IndexSearcher(reader);
            IndexSearcher searcher2 = new IndexSearcher(reader);
            TopDocs docs1 = searcher1.search(q1, hitsPerPage);
            TopDocs docs2 = searcher2.search(q2, hitsPerPage);
            ScoreDoc[] hits1 = docs1.scoreDocs;
            ScoreDoc[] hits2 = docs2.scoreDocs;
            System.out.println(hits1.length);
            System.out.println(hits2.length);
            int i = 0, j = 0;
            List<Essay> essays = new ArrayList<Essay>();
            while (i < hits1.length && j < hits2.length) {
                float score1 = hits1[i].score;
                float score2 = hits2[j].score;
                Essay essay = null;
                org.apache.lucene.document.Document d = null;
                if (score1 >= score2) {
                    int docId = hits1[i].doc;
                    d = searcher1.doc(docId);
                    i++;
                } else {
                    int docId = hits2[j].doc;
                    d = searcher2.doc(docId);
                    j++;
                }
                int essayId = Integer.parseInt(d.get("essayId"));
                essay = EssayService.findEssayById(essayId);
                if (essay != null && !essays.contains(essay)) {
                    System.out.println(essay.getTitle());
                    essays.add(essay);
                }
            }
            System.out.println(i);
            while (i < hits1.length) {
                int docId = hits1[i].doc;
                org.apache.lucene.document.Document d = searcher1.doc(docId);
                int essayId = Integer.parseInt(d.get("essayId"));
                Essay essay = EssayService.findEssayById(essayId);
                i++;
                if (essay != null && !essays.contains(essay)) {
                    essays.add(essay);
                    System.out.println(essay.getTitle());
                }

            }
            System.out.println(j);

            while (j < hits2.length) {
                int docId = hits2[j].doc;
                org.apache.lucene.document.Document d = searcher2.doc(docId);
                int essayId = Integer.parseInt(d.get("essayId"));
                Essay essay = EssayService.findEssayById(essayId);
                if (essay != null && !essays.contains(essay)) {
                    essays.add(essay);
                    System.out.println(essay.getTitle());
                }
                j++;
            }

            for (int k = offset; k < essays.size() && k < offset + limit; k++) {
                Essay essay = essays.get(k);
                if (essay != null) {
                    System.out.println(essay.getId());
                    Map<String, Object> m = JsonMapResponseBuilderFactory
                            .createEssayJsonMapBuilder(essay)
                            .getSimpleMap();
                    l.add(m);
                }
            }
            reader.close();
        } else {
            List<Essay> essays = EssayService.findAllEssays();
            for (int i = offset; i < essays.size() && i < (offset + limit); i++) {
                Map<String, Object> m = JsonMapResponseBuilderFactory
                        .createEssayJsonMapBuilder(essays.get(i))
                        .getSimpleMap();

                l.add(m);

            }
        }
        return new ResponseEntity<List<Map<String, Object>>>(l, HttpStatus.OK);


    }

    @RequestMapping(value = "/essays/{essayId}/comments", method = RequestMethod.GET)
    public ResponseEntity<List<Map<String, Object>>> getEssayComments(
            @PathVariable("essayId") int essayId) {
        Essay essay = EssayService.findEssayById(essayId);
        if (essay != null) {
            List<Map<String, Object>> l = new ArrayList<>();
            List<Comment> comments = CommentService.findCommentsByEssayId(essayId);
            for (Comment comment : comments) {
                Map<String, Object> m;
                m = JsonMapResponseBuilderFactory
                        .createCommentJsonMapBuilder(comment)
                        .getSimpleMap();
                l.add(m);
            }
            return new ResponseEntity<List<Map<String, Object>>>(l, HttpStatus.OK);
        } else {
            throw new BusinessException("Essay not found", HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/essays", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> postEssay(
            HttpServletRequest request,
            @RequestParam("description") String description,
            @RequestParam("photo") List<MultipartFile> files,
            @RequestParam("audio") List<MultipartFile> audio)
            throws IOException, NoSuchAlgorithmException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> descriptionMap = new HashMap<String, String>();
        descriptionMap = mapper.readValue(description, descriptionMap.getClass());
        String studioManagerEmailOrPhone = (String) descriptionMap.get("studio_manager_email_or_phone");
        String studioManagerPassword = (String) descriptionMap.get("studio_manager_password");
        String studioName = (String) descriptionMap.get("studio");
        String essayTitle = (String) descriptionMap.get("essay_title");
        String essayContent = (String) descriptionMap.get("essay_content");
        String type = descriptionMap.get("type");
        Studio studio = StudioService.findStudioByName(studioName);
//        System.out.println(studioName);
        User manager = UserService.findUserByEmailOrPhone(studioManagerEmailOrPhone);
        if (studio != null && manager != null
                && studio.getManager().equals(manager)
                && studioManagerPassword != null
                && studioManagerPassword.equals(manager.getPassword())) {
            if (essayTitle != null) {

                Essay essay = null;
                if ("text".equals(type)) {

                    String imageDir = request.getSession().getServletContext().getRealPath("/")
                            + "images/";
                    List<String> names = new ArrayList<String>();
                    for (MultipartFile file : files) {
                        String md5 = MD5Util.getMultipartFileMD5(file);
                        String originalFilename = file.getOriginalFilename();
                        String suffix = originalFilename.substring(
                                originalFilename.lastIndexOf("."));
                        names.add(md5 + suffix);
                        file.transferTo(new File(imageDir + md5 + suffix));
                    }

                    Document document = Jsoup.parse(essayContent);
                    int i = 0;
                    Elements images = document.getElementsByTag("img");
                    for (Element image : images) {
                        String oldSrc = image.attr("src");
                        image.attr("src",
                                ServerConfig.IMAGES_BASE_URL + names.get(i++));
                    }

                    essay = new Essay(essayTitle, document.toString(), studio, Essay.TYPE_TEXT);
                } else {
                    String imageDir = request.getSession().getServletContext().getRealPath("/")
                            + "audios/";
                    String md5 = MD5Util.getMultipartFileMD5(audio.get(0));
                    String originalFilename = audio.get(0).getOriginalFilename();
                    String suffix = originalFilename.substring(
                            originalFilename.lastIndexOf("."));
                    audio.get(0).transferTo(new File(imageDir + md5 + suffix));
                    essay = new Essay(essayTitle, ServerConfig.AUDIOS_BASE_URL + md5 + suffix,
                            studio, Essay.TYPE_AUDIO);
                }
                int essayId = EssayService.add(essay);
                Map<String, Object> map = new JsonMapBuilder()
                        .append("url", ServerConfig.SERVER_BASE_URL + "/essays/" + essayId)
                        .getMap();
                return new ResponseEntity<Map<String, Object>>(map, HttpStatus.CREATED);
            } else {
                throw new BusinessException("Required essay", HttpStatus.BAD_REQUEST);
            }

        } else {
            throw new BusinessException("Invalid user or password", HttpStatus.UNAUTHORIZED);
        }

    }

    @RequestMapping(value = "/essays/{essayId}/comments", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> postEssayComment(
            @PathVariable("essayId") int essayId,
            @RequestBody Map<String, Object> requestBody) {
        Essay essay = EssayService.findEssayById(essayId);
        if (essay != null) {
            String commenterEmailOrPhone = (String) requestBody.get("commenter_email_or_phone");
            String commenterPassword = (String) requestBody.get("commenter_password");
            String content = (String) requestBody.get("content");
            User commenter = UserService.findUserByEmailOrPhone(commenterEmailOrPhone);
//            System.out.println("username:" + commenter.getUsername());
//            System.out.println(commenterPassword);
            if (commenter != null
                    && commenterPassword != null
                    && commenterPassword.equals(commenter.getPassword())) {
                if (content != null) {
                    Comment comment = new Comment(commenter, content, essay);
                    int commentId = CommentService.add(comment);
                    Map<String, Object> map = new JsonMapBuilder()
                            .append("url", ServerConfig.SERVER_BASE_URL + "/comments/" + commentId)
                            .getMap();
                    return new ResponseEntity<Map<String, Object>>(map, HttpStatus.CREATED);
                } else {
                    throw new BusinessException("Require comment content", HttpStatus.BAD_REQUEST);
                }
            } else {
                throw new BusinessException("Invalid user or password", HttpStatus.UNAUTHORIZED);
            }
        } else {
            throw new BusinessException("Essay not found", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/essays/{essayId}/supports", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> postEssaySupport(
            @PathVariable("essayId") int essayId,
            @RequestBody Map<String, Object> requestBody) {
        Essay essay = EssayService.findEssayById(essayId);
        if (essay != null) {
            String supporterEmailOrPhone = (String) requestBody.get("supporter_email_or_phone");
            String supporterPassword = (String) requestBody.get("supporter_password");
            User supporter = UserService.findUserByEmailOrPhone(supporterEmailOrPhone);
            if (supporter != null
                    && supporterPassword != null
                    && supporterPassword.equals(supporter.getPassword())) {
                UserService.addSupportEssay(supporter.getId(), essayId);
                return new ResponseEntity<Map<String, Object>>(HttpStatus.CREATED);
            } else {
                throw new BusinessException("Invalid user or password", HttpStatus.UNAUTHORIZED);
            }
        } else {
            throw new BusinessException("Essay not found", HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/essays/{essayId}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> deleteEssay(
            @PathVariable("essayId") int essayId,
            @RequestBody Map<String, Object> requestBody) throws IOException {
        Essay essay = EssayService.findEssayById(essayId);
        if (essay != null) {
            String studioManagerEmailOrPhone = (String) requestBody.get("studio_manager_email_or_phone");
            String studioManagerPassword = (String) requestBody.get("studio_manager_password");
            User studioManager = UserService.findUserByEmailOrPhone(studioManagerEmailOrPhone);
            if (studioManager != null
                    && studioManagerPassword != null
                    && studioManagerPassword.equals(studioManager.getPassword())) {
                EssayService.remove(essayId);
                return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
            } else {
                throw new BusinessException("Invalid user or password", HttpStatus.UNAUTHORIZED);
            }
        } else {
            throw new BusinessException("Essay not found", HttpStatus.NOT_FOUND);
        }
    }

}
