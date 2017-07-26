package cn.edu.nju.cs.seg.controller;

import cn.edu.nju.cs.seg.ServerConfig;
import cn.edu.nju.cs.seg.exception.BusinessException;
import cn.edu.nju.cs.seg.json.JsonMapBuilder;
import cn.edu.nju.cs.seg.json.JsonMapResponseBuilderFactory;
import cn.edu.nju.cs.seg.pojo.Answer;
import cn.edu.nju.cs.seg.pojo.Question;
import cn.edu.nju.cs.seg.pojo.Studio;
import cn.edu.nju.cs.seg.pojo.User;
import cn.edu.nju.cs.seg.service.AnswerService;
import cn.edu.nju.cs.seg.service.QuestionService;
import cn.edu.nju.cs.seg.service.StudioService;
import cn.edu.nju.cs.seg.service.UserService;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Created by fwz on 2017/4/25.
 */
@RestController
@RequestMapping("/api")
public class QuestionController {
    private Logger LOG = LoggerFactory.getLogger(AnswerController.class);

    /**
     * GET /questions/{questionId}
     * 描述：获取问题信息
     * 响应成功 状态码
     * 200 OK
     * 响应失败 状态码
     * 404 NOT FOUND
     * 503 SERVICE UNAVAILABLE
     *
     * @return
     */
    @RequestMapping(value = "/questions", method = RequestMethod.GET)
    public ResponseEntity<List<Map<String, Object>>> getQuestions(HttpServletRequest request) throws ParseException, IOException {
        List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();

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
            Query q1 = new QueryParser("questionTitle", analyzer).parse(search);
            Query q2 = new QueryParser("questionContent", analyzer).parse(search);
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
            List<Question> questions = new ArrayList<Question>();
            while (i < hits1.length && j < hits2.length) {
                float score1 = hits1[i].score;
                float score2 = hits2[j].score;
                Question question = null;
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
                int questionId = Integer.parseInt(d.get("questionId"));
                question = QuestionService.findQuestionById(questionId);
                if (question != null && !questions.contains(question)) {
                    System.out.println(question.getTitle());
                    questions.add(question);
                }
            }
            System.out.println(i);
            while (i < hits1.length) {
                int docId = hits1[i].doc;
                org.apache.lucene.document.Document d = searcher1.doc(docId);
                int questionId = Integer.parseInt(d.get("questionId"));
                Question question = QuestionService.findQuestionById(questionId);
                i++;
                if (question != null && !questions.contains(question)) {
                    questions.add(question);
                    System.out.println(question.getTitle());
                }

            }
            System.out.println(j);

            while (j < hits2.length) {
                int docId = hits2[j].doc;
                org.apache.lucene.document.Document d = searcher2.doc(docId);
                int questionId = Integer.parseInt(d.get("questionId"));
                Question question = QuestionService.findQuestionById(questionId);
                if (question != null && !questions.contains(question)) {
                    questions.add(question);
                    System.out.println(question.getTitle());
                }
                j++;
            }

            for (int k = offset; k < questions.size() && k < offset + limit; k++) {
                Question question = questions.get(k);
                if (question != null) {
                    Map<String, Object> m = JsonMapResponseBuilderFactory
                            .createQuestionJsonMapBuilder(question)
                            .getSimpleMap();
                    l.add(m);
                }
            }
            reader.close();
        } else {
            List<Question> questions = QuestionService.findAllQuestions();
            for (int i = offset; i < offset + limit && i < questions.size(); i++) {
                Question q = questions.get(i);
                Map<String, Object> m = JsonMapResponseBuilderFactory
                        .createQuestionJsonMapBuilder(q)
                        .getSimpleMap();
                l.add(m);
            }
        }
        return new ResponseEntity<List<Map<String, Object>>>(l, HttpStatus.OK);

    }


    /**
     * GET /questions/{questionId}/answers
     * 描述：获取问题的答案列表。
     * 响应成功 状态码
     * 200 OK
     * 响应失败 状态码
     * 404 NOT FOUND
     * 503 SERVICE UNAVAILABLE
     *
     * @param questionId
     * @return
     */
    @RequestMapping(value = "/questions/{questionId}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getQuestion(@PathVariable("questionId") int questionId) {
        Map<String, Object> m;
        Question q = QuestionService.findQuestionById(questionId);
//        System.out.println(q.getTitle());
        if (q != null) {
            QuestionService.incrementQuestionHeat(questionId);
            m = JsonMapResponseBuilderFactory
                    .createQuestionJsonMapBuilder(q)
                    .getComplexMap();
            return new ResponseEntity<Map<String, Object>>(m, HttpStatus.OK);
        } else {
            throw new BusinessException("Question not found", HttpStatus.NOT_FOUND);
        }
    }


    /**
     * GET /questions/{questionId}/answers
     * 描述：获取问题的答案列表。
     * 响应成功 状态码
     * 200 OK
     * 响应失败 状态码
     * 404 NOT FOUND
     * 503 SERVICE UNAVAILABLE
     *
     * @param questionId
     * @return
     */
    @RequestMapping(value = "/questions/{questionId}/answers", method = RequestMethod.GET)
    public ResponseEntity<List<Map<String, Object>>> getQuestionAnsewers(@PathVariable("questionId") int questionId) {
        List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();
        Question question = QuestionService.findQuestionById(questionId);
        List<Answer> answers = AnswerService.findAnswersByQuestionId(questionId);
//        System.out.println(answers.size());
        if (question != null) {
            for (Answer a : answers) {
                Map<String, Object> m = JsonMapResponseBuilderFactory
                        .createAnswerJsonMapBuilder(a)
                        .getSimpleMap();
                l.add(m);
            }
            return new ResponseEntity<List<Map<String, Object>>>(l, HttpStatus.OK);

        } else {
            throw new BusinessException("Question not found", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * POST /questions
     * 描述：创建一个问题
     * 请求具体参数
     * asker_email_or_phone: String REQUIRED 提问者的账号(E-mail或手机号)
     * asker_password: String REQUIRED 提问者的密码
     * question_title: String REQUIRED 问题标题
     * directed_to: String REQUIRED 所要咨询的工作室的名称
     * question_content: String OPTIONAL 问题描述 默认为""
     * 响应成功 状态码
     * 201 CREATED
     * 响应失败 状态码
     * 401 UNAUTHORIZED
     * 503 SERVICE UNAVAILABLE
     *
     * @param description
     * @param files
     * @return
     */

    @RequestMapping(value = "/questions", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> postQuestion(
            HttpServletRequest request,
            @RequestParam("description") String description,
            @RequestParam("photo") List<MultipartFile> files,
            @RequestParam("audio") List<MultipartFile> audio)
            throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> descriptionMap = new HashMap<String, Object>();
        descriptionMap = mapper.readValue(description, descriptionMap.getClass());

        String askerEmailOrPhone = (String) descriptionMap.get("asker_email_or_phone");
        String askerPassword = (String) descriptionMap.get("asker_password");
        String questionTitle = (String) descriptionMap.get("question_title");
        String directedTo = (String) descriptionMap.get("directed_to");
        String type = (String) descriptionMap.get("type");

        System.out.println(directedTo);
        User user = UserService.findUserByEmailOrPhone(askerEmailOrPhone);
        if (user != null
                && askerPassword != null
                && askerPassword.equals(user.getPassword())) {
            if (questionTitle != null && questionTitle.length() > 0
                    && directedTo != null && directedTo.length() > 0) {
                Studio studio = StudioService.findStudioByName(directedTo);
                if (studio != null) {
                    Question question = null;
                    if (type.equals("text")) {
                        String questionContent = (String) descriptionMap.get("question_content");
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

                        Document document = Jsoup.parse(questionContent);
                        int i = 0;
                        Elements images = document.getElementsByTag("img");
                        for (Element image : images) {
                            String oldSrc = image.attr("src");
                            image.attr("src",
                                    ServerConfig.IMAGES_BASE_URL + names.get(i++));
                        }

                        question = new Question(questionTitle,
                                document.toString(), user, studio, Question.TYPE_TEXT);
                    } else {
                        String audioDir = request.getSession().getServletContext().getRealPath("/")
                                + "audios/";
                        String md5 = MD5Util.getMultipartFileMD5(audio.get(0));
                        String originalFilename = audio.get(0).getOriginalFilename();
                        String suffix = originalFilename.substring(
                                originalFilename.lastIndexOf("."));
                        audio.get(0).transferTo(new File(audioDir + md5 + suffix));
                        question = new Question(questionTitle,
                                ServerConfig.AUDIOS_BASE_URL + md5 + suffix,
                                user, studio, Question.TYPE_AUDIO);
                    }

                    int questionId = QuestionService.add(question);
                    Map<String, Object> m = new JsonMapBuilder()
                            .append("url", ServerConfig.SERVER_BASE_URL + "/questions/" + questionId)
                            .getMap();


                    return new ResponseEntity<Map<String, Object>>(m, HttpStatus.CREATED);
                } else {
                    throw new BusinessException("studio not found", HttpStatus.NOT_FOUND);
                }
            } else {
                throw new BusinessException("Bad request", HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new BusinessException("Invalid user or password", HttpStatus.UNAUTHORIZED);
        }

    }


    /**
     * 描述：创建一条对该问题的回答
     * 请求体参数：
     * answer_author_email_or_phone: String REQUIRED 答主的账号(E-mail或手机号)
     * answer_author_password: String REQUIRED 答主的密码
     * content: String REQUIRED 回答内容
     * 响应成功 状态码
     * 201 CREATED
     * 响应失败 状态码
     * 401 UNAUTHORIZED
     * 404 NOT FOUND
     * 503 SERVICE UNAVAILABLE
     *
     * @param questionId
     * @param description
     * @param files
     * @return
     */

    @RequestMapping(value = "/questions/{questionId}/answers", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> postQuestionAnswer(
            HttpServletRequest request,
            @PathVariable("questionId") int questionId,
            @RequestParam("description") String description,
            @RequestParam("photo") List<MultipartFile> files,
            @RequestParam("audio") List<MultipartFile> audio)
            throws IOException, NoSuchAlgorithmException {
        Question question = QuestionService.findQuestionById(questionId);

        if (question != null) {

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> descriptionMap = new HashMap<String, Object>();
            descriptionMap = mapper.readValue(description, descriptionMap.getClass());

            String answerAuthorEmailOrPhone = (String) descriptionMap.get("answerer_email_or_phone");
            String content = (String) descriptionMap.get("content");
            String answerAuthorPassword = (String) descriptionMap.get("answerer_password");
            String type = (String) descriptionMap.get("type");
            User user = UserService.findUserByEmailOrPhone(answerAuthorEmailOrPhone);
            Studio directedTo = question.getStudio();
            List<User> members = StudioService.findUsersByStudioId(directedTo.getId());
            if (user != null
                    && members.contains(user)
                    && answerAuthorPassword != null
                    && answerAuthorPassword.equals(user.getPassword())) {
                Answer answer = null;
                if (type.equals("text")) {
                    if (content != null && content.length() > 0) {

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

                        Document document = Jsoup.parse(content);
                        int i = 0;
                        Elements images = document.getElementsByTag("img");
                        for (Element image : images) {
                            String oldSrc = image.attr("src");
                            image.attr("src",
                                    ServerConfig.IMAGES_BASE_URL + names.get(i++));
                        }
                        answer = new Answer(document.toString(),
                                user, question, Answer.TYPE_TEXT);
                    } else {
                        throw new BusinessException("require content", HttpStatus.BAD_REQUEST);
                    }
                } else {
                    String audioDir = request.getSession()
                            .getServletContext().getRealPath("/")
                            + "audios/";
                    String md5 = MD5Util.getMultipartFileMD5(audio.get(0));
                    String originalFilename = audio.get(0).getOriginalFilename();
                    String suffix = originalFilename.substring(
                            originalFilename.lastIndexOf("."));
                    audio.get(0).transferTo(new File(audioDir + md5 + suffix));
                    answer = new Answer(ServerConfig.AUDIOS_BASE_URL + md5 + suffix,
                            user, question, Answer.TYPE_AUDIO);
                }
                int id = AnswerService.add(answer);
                Map<String, Object> m = new JsonMapBuilder()
                        .append("url", ServerConfig.SERVER_BASE_URL + "/answers/" + id)
                        .getMap();
                return new ResponseEntity<Map<String, Object>>(m, HttpStatus.CREATED);
            } else {
                throw new BusinessException("invalid password", HttpStatus.UNAUTHORIZED);
            }

        } else {
            throw new BusinessException("Question not found", HttpStatus.NOT_FOUND);
        }
    }


    /**
     * POST /questions/{questionId}/supports
     * 描述：为该文章点赞
     * 请求体参数
     * supporter_email_or_phone: String REQUIRED 点赞者的账号
     * supporter_password: String REQUIRED 点赞者的密码
     * 响应成功 状态码
     * 201 CREATED
     * 无响应体
     * 响应失败 状态码
     * 401 UNAUTHORIZED
     * 404 NOT FOUND
     * 503 SERVICE UNAVAILABLE
     *
     * @param questionId
     * @param bodyMap
     * @return
     */


    @RequestMapping(value = "/questions/{questionId}/supports", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> postQuestionSupports(
            @PathVariable("questionId") int questionId, @RequestBody Map<String, Object> bodyMap) {

        Question question = QuestionService.findQuestionById(questionId);
        Map<String, Object> m;

        if (question != null) {

            String supporterEmailOrPhone = (String) bodyMap.get("supporter_email_or_phone");
            User user = null;

            if (supporterEmailOrPhone != null) {

                if (supporterEmailOrPhone.contains("@")) {
                    user = UserService.findUserByEmail(supporterEmailOrPhone);
                } else {
                    user = UserService.findUserByEmail(supporterEmailOrPhone);
                }

                if (user != null) {

                    String supporterPassword = (String) bodyMap.get("supporter_password");

                    if (supporterPassword != null && supporterPassword.equals(user.getPassword())) {
                        UserService.addSupportQuestion(user.getId(), questionId);
                        return new ResponseEntity<Map<String, Object>>(HttpStatus.CREATED);
                    } else {
                        throw new BusinessException("invalid password", HttpStatus.UNAUTHORIZED);
                    }
                } else {
                    throw new BusinessException("user", HttpStatus.NOT_FOUND);
                }
            } else {
                throw new BusinessException("require user account", HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new BusinessException("Question not found", HttpStatus.NOT_FOUND);
        }
    }


    /**
     * DELETE /questions/{questionId}
     * 描述：删除一个问题
     * 请求体参数
     * manager_email_or_phone: String REQUIRED 评论者的账号(E-mail或手机号)
     * manager_password: String REQUIRED 评论者的密码
     * 响应成功 状态码
     * 204 NO CONTENT
     * 无响应体
     * 响应失败 状态码
     * 401 UNAUTHORIZED
     * 503 SERVICE UNAVAILABLE
     *
     * @param questionId
     * @param mapBody
     * @return 描述：删除某问题的评论
     */

    @RequestMapping(value = "/questions/{questionId}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> deleteQuestion(
            @PathVariable("questionId") int questionId,
            @RequestBody Map<String, Object> mapBody) throws IOException {
        Question question = QuestionService.findQuestionById(questionId);
        if (question != null) {
            Map<String, Object> m;

            String managerEmailOrPhone = (String) mapBody.get("manager_email_or_phone");
            String managerPassword = (String) mapBody.get("manager_password");
            User manager = UserService.findUserByEmailOrPhone(managerEmailOrPhone);

            if (question.getAnswersNumber() > 0) {
                if (manager != null
                        && question.getStudio().getManager().equals(manager)
                        && managerPassword.equals(manager.getPassword())) {
                    QuestionService.remove(questionId);
                    return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
                } else {
                    throw new BusinessException("invalid user or password", HttpStatus.UNAUTHORIZED);
                }
            } else {
                if (manager != null
                        && question.getUser().equals(manager)
                        && managerPassword.equals(manager.getPassword())) {
                    QuestionService.remove(questionId);
                    return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
                } else {
                    throw new BusinessException("invalid user or password", HttpStatus.UNAUTHORIZED);
                }
            }

        } else {
            throw new BusinessException("user not found", HttpStatus.NOT_FOUND);
        }
    }


}
