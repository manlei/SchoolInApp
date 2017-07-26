package cn.edu.nju.cs.seg.controller;

import cn.edu.nju.cs.seg.ServerConfig;
import cn.edu.nju.cs.seg.exception.BusinessException;
import cn.edu.nju.cs.seg.json.JsonMapBuilder;
import cn.edu.nju.cs.seg.json.JsonMapResponseBuilderFactory;
import cn.edu.nju.cs.seg.pojo.*;
import cn.edu.nju.cs.seg.service.*;
import cn.edu.nju.cs.seg.util.EmailUtil;
import cn.edu.nju.cs.seg.util.MD5Util;
import cn.edu.nju.cs.seg.util.VerificationCodeUtil;
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

import javax.mail.MessagingException;
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
 * Created by fwz on 2017/4/22.
 */
@RestController
@RequestMapping("/api")
public class UserController {
    //    private static StudioService StudioService = new StudioService();
//    private static UserService UserService = new UserService();
//    private static QuestionService QuestionService = new QuestionService();
//    private static AnswerService AnswerService = new AnswerService();
//    private static VerificationCodeService verificationCodeService = new VerificationCodeService();

    /**
     * GET /users
     * 描述：获取用户列表。
     *
     * @return
     */

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<List<Map<String, Object>>> getUsers(HttpServletRequest request) throws ParseException, IOException {
        List<User> users = UserService.findAllUsers();
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
            Query q = new QueryParser("username", analyzer).parse(search);
            int hitsPerPage = 10;
            Directory directory = FSDirectory.open(
                    FileSystems.getDefault().getPath(ServerConfig.LUCENE_INDEX_DIRECTORY));
            IndexReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);
            TopDocs docs = searcher.search(q, hitsPerPage);
            ScoreDoc[] hits = docs.scoreDocs;
            System.out.println("Found " + hits.length + " hits.");
            for (int i = 0; i < hits.length; ++i) {
                int docId = hits[i].doc;
                org.apache.lucene.document.Document d = searcher.doc(docId);
                System.out.println((i + 1) + ". " + d.get("userId") + "\t" + d.get("username"));
                int userId = Integer.parseInt(d.get("userId"));
                User user = UserService.findUserById(userId);
                if (user != null) {
                    Map<String, Object> m = JsonMapResponseBuilderFactory
                            .createUserJsonMapBuilder(user)
                            .getSimpleMap();
                    l.add(m);
                }
            }
            reader.close();
        } else {
            for (int i = offset; i < offset + limit && i < users.size(); i++) {
                User u = users.get(i);
                Map<String, Object> m = JsonMapResponseBuilderFactory
                        .createUserJsonMapBuilder(u)
                        .getSimpleMap();
                l.add(m);
            }
        }
        return new ResponseEntity<List<Map<String, Object>>>(l, HttpStatus.OK);

    }


    /**
     * GET /users/{userId}
     * 描述：获取某用户信息
     * 响应成功 状态码
     * 200 OK
     * 响应失败 状态码
     * 404 NOT FOUND
     * 503 SERVICE UNAVAILABLE
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getUser(
            @PathVariable("userId") int userId) {
        User user = UserService.findUserById(userId);
        if (user != null) {
            Map<String, Object> m = JsonMapResponseBuilderFactory
                    .createUserJsonMapBuilder(user)
                    .getComplexMap();

            return new ResponseEntity<Map<String, Object>>(m, HttpStatus.OK);

        } else {
            throw new BusinessException("Not found", HttpStatus.NOT_FOUND);
        }
    }


    /**
     * GET /users/{userId}/supports?offset={offset}&limit={limit}
     * 描述：获取某用户信息
     * 响应成功 状态码
     * 200 OK
     * 响应失败 状态码
     * 400 BAD REQUEST
     * 404 NOT FOUND
     * 503 SERVICE UNAVAILABLE
     *
     * @param userId
     * @param request
     * @return
     */

    @RequestMapping(value = "/users/{userId}/supports", method = RequestMethod.GET)
    public ResponseEntity<List<Map<String, Object>>> getUserSupports(
            @PathVariable("userId") int userId, HttpServletRequest request) {
        boolean isOk = true;
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
        User user = UserService.findUserById(userId);
        if (user != null) {
            List<Object> supports = UserService.findSupportsByUserId(userId, offset, limit);
            for (int i = 0; i < supports.size(); i++) {
                if (supports.get(i) instanceof Question) {
                    Question question = (Question) supports.get(i);
                    Map<String, Object> map = JsonMapResponseBuilderFactory
                            .createQuestionJsonMapBuilder(question)
                            .getSimpleMap();
                    map.put("type", "question");
                    l.add(map);
                } else if (supports.get(i) instanceof Essay) {
                    Essay essay = (Essay) supports.get(i);
                    Map<String, Object> map = JsonMapResponseBuilderFactory
                            .createEssayJsonMapBuilder(essay)
                            .getSimpleMap();
                    map.put("type", "essay");
                    l.add(map);
                }
            }
            return new ResponseEntity<List<Map<String, Object>>>(l, HttpStatus.OK);
        } else {
            throw new BusinessException("Not found", HttpStatus.NOT_FOUND);
        }
    }


    /**
     * GET /users/{userId}/favorites/questions
     * 描述：获取用户收藏问题列表
     * 响应成功 状态码
     * 200 OK
     * 响应失败 状态码
     * 404 NOT FOUND
     * 503 SERVICE UNAVAILABLE
     *
     * @param userId
     * @return
     */

    @RequestMapping(value = "/users/{userId}/favorites/questions", method = RequestMethod.GET)
    public ResponseEntity<List<Map<String, Object>>> getUserFavoritesQuestions(
            @PathVariable("userId") int userId) {
        User user = UserService.findUserById(userId);
        List<Question> questions = UserService.findFavoriteQuestionsByUserId(user.getId());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (user != null) {
            for (Question q : questions) {
                Map<String, Object> m = JsonMapResponseBuilderFactory
                        .createQuestionJsonMapBuilder(q)
                        .getSimpleMap();
                list.add(m);
            }
            return new ResponseEntity<List<Map<String, Object>>>(list, HttpStatus.OK);
        } else {
            throw new BusinessException("Not found", HttpStatus.NOT_FOUND);
        }
    }


    /**
     * GET /users/{userId}/favorites/answers
     * 描述：获取用户收藏回答列表
     * 响应成功 状态码
     * 200 OK
     * 响应失败 状态码
     * 404 NOT FOUND
     * 503 SERVICE UNAVAILABLE
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/users/{userId}/favorites/answers", method = RequestMethod.GET)
    public ResponseEntity<List<Map<String, Object>>> getUserFavoritesAnswers(
            @PathVariable("userId") int userId) {
        User user = UserService.findUserById(userId);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<Answer> answers = UserService.findFavoriteAnswersByUserId(user.getId());
        if (user != null) {
            for (Answer a : answers) {
                Map<String, Object> m = JsonMapResponseBuilderFactory
                        .createAnswerJsonMapBuilder(a)
                        .getSimpleMap();
                list.add(m);
            }
            return new ResponseEntity<List<Map<String, Object>>>(list, HttpStatus.OK);
        } else {
            throw new BusinessException("Not found", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * GET /users/{userId}/favorites/essays
     * 描述：获取用户收藏文章列表
     * 响应成功 状态码
     * 200 OK
     * 响应失败 状态码
     * 404 NOT FOUND
     * 503 SERVICE UNAVAILABLE
     *
     * @param userId
     * @return
     */

    @RequestMapping(value = "/users/{userId}/favorites/essays", method = RequestMethod.GET)
    public ResponseEntity<List<Map<String, Object>>> getUserFavoritesEssays(
            @PathVariable("userId") int userId) {
        User user = UserService.findUserById(userId);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<Essay> essays = UserService.findFavoriteEssayByUserId(userId);
        if (user != null) {
            for (Essay e : essays) {
                Map<String, Object> m = JsonMapResponseBuilderFactory
                        .createEssayJsonMapBuilder(e)
                        .getSimpleMap();
                list.add(m);
            }
            return new ResponseEntity<List<Map<String, Object>>>(list, HttpStatus.OK);
        } else {
            throw new BusinessException("Not found", HttpStatus.NOT_FOUND);
        }
    }


    /**
     * GET /users/{userId}/question
     * 描述：获取用户提过的问题列表
     * 响应成功 状态码
     * 200 OK
     * 响应失败 状态码
     * 404 NOT FOUND
     * 503 SERVICE UNAVAILABLE
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/users/{userId}/questions", method = RequestMethod.GET)
    public ResponseEntity<List<Map<String, Object>>> getUserQuestions(
            @PathVariable("userId") int userId) {
        List<Map<String, Object>> questionsInfo = new ArrayList<Map<String, Object>>();
        User user = UserService.findUserById(userId);
        if (user != null) {
            List<Question> questions = QuestionService.findQuestionsByUserId(userId);
            for (Question q : questions) {
                Map<String, Object> m = JsonMapResponseBuilderFactory
                        .createQuestionJsonMapBuilder(q)
                        .getSimpleMap();
                questionsInfo.add(m);
            }
            return new ResponseEntity<List<Map<String, Object>>>(questionsInfo, HttpStatus.OK);
        } else {
            throw new BusinessException("Not found", HttpStatus.NOT_FOUND);
        }
    }


    /**
     * GET /users/{userId}/answers
     * 描述：获取用户回答列表
     * 响应成功 状态码
     * 200 OK
     * 响应失败 状态码
     * 404 NOT FOUND
     * 503 SERVICE UNAVAILABLE
     *
     * @param userId
     * @return
     */

    @RequestMapping(value = "/users/{userId}/answers", method = RequestMethod.GET)
    public ResponseEntity<List<Map<String, Object>>> getUserAnswers(
            @PathVariable("userId") int userId) {
        List<Map<String, Object>> answerList = new ArrayList<Map<String, Object>>();
        User user = UserService.findUserById(userId);
        if (user != null) {
            List<Answer> answers = AnswerService.findAnswersByUserId(user.getId());
            for (Answer answer : answers) {
                Map<String, Object> m = JsonMapResponseBuilderFactory
                        .createAnswerJsonMapBuilder(answer)
                        .getSimpleMap();
                answerList.add(m);
            }
            return new ResponseEntity<List<Map<String, Object>>>(answerList, HttpStatus.OK);
        } else {
            throw new BusinessException("Not found", HttpStatus.NOT_FOUND);
        }

    }


    /**
     * GET /users/{userId}/studios
     * 描述：获取用户所在工作室列表
     * 响应成功 状态码
     * 200 OK
     * 响应失败 状态码
     * 404 NOT FOUND
     * 503 SERVICE UNAVAILABLE
     *
     * @param userId
     * @return
     */


    @RequestMapping(value = "/users/{userId}/studios", method = RequestMethod.GET)
    public ResponseEntity<List<Map<String, Object>>> getUserStudios(
            @PathVariable("userId") int userId) {
        User user = UserService.findUserById(userId);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (user != null) {
            List<Studio> studios = UserService.findStudiosByUserId(userId);
            for (Studio s : studios) {
                Map<String, Object> m = JsonMapResponseBuilderFactory
                        .createStudioJsonMapBuilder(s)
                        .getSimpleMap();
                list.add(m);

            }
            return new ResponseEntity<List<Map<String, Object>>>(list, HttpStatus.OK);
        } else {
            throw new BusinessException("Not found", HttpStatus.NOT_FOUND);
        }
    }


    @RequestMapping(value = "/users/{userId}/notifications", method = RequestMethod.GET)
    public ResponseEntity<List<Map<String, Object>>> getUserNotifications(
            @PathVariable("userId") int userId) {
        User user = UserService.findUserById(userId);
        if (user == null) {
            throw new BusinessException("User not found", HttpStatus.NOT_FOUND);
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<Notification> notifications = NotificationService.findNotificationsByUserId(userId);
        for (int i = notifications.size() - 1; i >= 0; i--) {

            Map<String, Object> map = JsonMapResponseBuilderFactory
                    .createNotificationJsonMapBuilder(notifications.get(i))
                    .getSimpleMap();
            list.add(map);
        }
        return new ResponseEntity<List<Map<String, Object>>>(list, HttpStatus.OK);
    }

    /**
     * POST /users/requestVerifyEmail
     * 描述：向用户邮箱发送验证码，以请求创建用户；返回验证码持续到的时间
     * 请求体参数
     * email: String REQUIRE 欲创建用户的邮箱
     * 响应成功 状态码
     * 201 CREATED
     * 响应失败 状态码
     * 400 BAD REQUEST
     * 409 CONFLICT
     * 503 SERVICE UNAVAILABLE
     *
     * @param requestMap
     * @return
     */

    @RequestMapping(value = "/users/requestVerifyEmail", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> getUserRequestVerifyEmail(
            @RequestBody Map<String, Object> requestMap) {
        String email = (String) requestMap.get("email");
        if (email != null && email.length() > 0 && email.contains("@")) {
            String code = VerificationCodeUtil.getVerificationCode();
            long retainUntil = System.currentTimeMillis() + 1000 * 60 * 10;
            try {
                EmailUtil.sendEmail(email, "校园问答验证码", code);
                VerificationCode verificationCode = VerificationCodeService.findCodeByEmailOrPhone(email);
                if (verificationCode == null) {
                    verificationCode = new VerificationCode(email, code, retainUntil);
                    VerificationCodeService.add(verificationCode);
                } else {
                    verificationCode.setCode(code);
                    verificationCode.setRetainUtil(retainUntil);
                    VerificationCodeService.update(verificationCode);
                }
                Map<String, Object> m = new JsonMapBuilder()
                        .append("retain_until", System.currentTimeMillis() + 1000 * 60 * 10)
                        .getMap();
                return new ResponseEntity<Map<String, Object>>(m, HttpStatus.CREATED);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            throw new BusinessException("Server is busy", HttpStatus.SERVICE_UNAVAILABLE);
        } else {
            throw new BusinessException("Invalid NJU email address", HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * POST /users/requestVerifyPhone
     * 描述：向用户手机发送验证码，以请求绑定手机；返回验证码持续到的时间
     * 请求体参数
     * phone: String REQUIRE 欲创建用户的邮箱
     * 响应成功 状态码
     * 201 CREATED
     * 响应失败 状态码
     * 400 BAD REQUEST
     * 503 SERVICE UNAVAILABLE
     *
     * @param requestMap
     * @return
     */

    //TODO: send sms
    @RequestMapping(value = "/users/requestVerifyPhone", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> getUserRequestVerifyPhone(
            @RequestBody Map<String, Object> requestMap) {
        return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
    }

    /**
     * POST /users
     * 描述：用户注册时创建一个用户
     * 请求体参数
     * email: String REQUIRE 欲创建用户的邮箱
     * password: String REQUIRE 欲创建用户的密码
     * verification_code: Number REQUIRED 获取到的验证码
     * name: String OPTIONAL 欲创建用户的用户名
     * bio: String OPTIONAL 欲创建用户的个性签名，默认为""
     * age: Number OPTIONAL 欲创建用户的年龄，默认为0
     * sex: String OPTIONAL 欲创建用户的性别，默认为unknown，只能为male或者female
     * department: String OPTIONAL 欲创建用户的专业，默认为""
     * location: String OPTIONAL 欲创建用户的地点，默认为""
     * avatar: String OPTIONAL 欲创建用户的头像地址(URL)，默认为系统提供的头像
     * 响应成功 状态码
     * 201 CREATED
     * 响应失败 状态码
     * 401 UNAUTHORIZED
     * 409 CONFLICT
     * 503 SERVICE UNAVAILABLE
     *
     * @param bodyMap
     * @return
     */

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> postUser(
            @RequestBody Map<String, Object> bodyMap) throws IOException {
        String email = (String) bodyMap.get("email");
        String password = (String) bodyMap.get("password");
        if (email != null && email.length() > 0
                && email.contains("@")
                && email.contains("nju.edu.cn")
                && password != null && password.length() > 0) {
            VerificationCode code = VerificationCodeService.findCodeByEmailOrPhone(email);
            String verificationCode = (String) bodyMap.get("verification_code");
//            System.out.println(verificationCode);
//            System.out.println(code.getCode());
            if (code != null
                    && verificationCode != null
                    && verificationCode.length() > 0
                    && verificationCode.equals(code.getCode())
                    && code.getRetainUtil() > System.currentTimeMillis()) {
                if (UserService.findUserByEmail(email) == null) {


                    User user = new User(email, password);
                    String name = (String) bodyMap.get("name");
                    String bio = (String) bodyMap.get("bio");
                    if (bodyMap.get("age") != null) {
                        int age = (int) bodyMap.get("age");
                        user.setAge(age);
                    }

                    String sex = (String) bodyMap.get("sex");
                    String department = (String) bodyMap.get("location");
                    String avatar = (String) bodyMap.get("avatar");


                    user.setBio(bio != null ? bio : "");
                    user.setUsername(name != null ? name : "");

                    user.setSex(sex != null ? sex : "unknown");
                    user.setDepartment(department != null ? department : "");
                    if (avatar != null && avatar.contains("/avatars/")) {
                        String substr = avatar.substring(
                                avatar.indexOf("/avatars/") + "/avatars/".length());
                        user.setAvatar(Integer.parseInt(substr));
                    }
                    int userId = UserService.add(user);
                    Map<String, Object> m = new JsonMapBuilder()
                            .append("url", ServerConfig.SERVER_BASE_URL + "/user/" + userId)
                            .getMap();


                    return new ResponseEntity<Map<String, Object>>(m, HttpStatus.CREATED);
                } else {
                    throw new BusinessException("Email has been used", HttpStatus.CONFLICT);
                }
            } else {
                throw new BusinessException("Invalid validation code", HttpStatus.UNAUTHORIZED);
            }
        } else {
            throw new BusinessException("Invalid email", HttpStatus.BAD_REQUEST);

        }
    }

    /**
     * POST /users/{userId}/questions
     * 描述：用户提出问题
     * 请求体参数
     * password: String REQUIRED 该用户的密码
     * question_title: String REQUIRED 问题标题
     * directed_to: String REQUIRED 所要咨询的工作室的名称
     * question_content: String OPTIONAL 问题描述 默认为""
     * 响应成功 状态码
     * 201 CREATED
     * 响应失败 状态码
     * 401 UNAUTHORIZED
     * 404 NOT FOUND
     * 503 SERVICE UNAVAILABLE
     *
     * @param userId
     * @param description
     * @param files
     * @return 描述：用户提出问题
     * 请求
     * 请求体参数
     */
    @RequestMapping(value = "/users/{userId}/questions", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> postUserQuestions(
            HttpServletRequest request,
            @PathVariable("userId") int userId,
            @RequestParam("description") String description,
            @RequestParam("photo") List<MultipartFile> files,
            @RequestParam("audio") List<MultipartFile> audio)
            throws IOException, NoSuchAlgorithmException {
        User u = UserService.findUserById(userId);
        if (u != null) {

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> descriptionMap = new HashMap<String, Object>();
            descriptionMap = mapper.readValue(description, descriptionMap.getClass());
            String password = (String) descriptionMap.get("password");
            String questionTitle = (String) descriptionMap.get("question_title");
            String directTo = (String) descriptionMap.get("directed_to");
            String questionContent = (String) descriptionMap.get("question_content");
            String type = (String) descriptionMap.get("type");

            if (password != null
                    && password.equals(u.getPassword())) {

                Studio studio = StudioService.findStudioByName(directTo);
                if (studio != null) {

                    Question question = null;
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

                        Document document = Jsoup.parse(questionContent);
                        int i = 0;
                        Elements images = document.getElementsByTag("img");
                        for (Element image : images) {
                            String oldSrc = image.attr("src");
                            image.attr("src",
                                    ServerConfig.IMAGES_BASE_URL + names.get(i++));
                        }


                        question = new Question(questionTitle,
                                document.toString(), u, studio, Question.TYPE_TEXT);
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
                                u, studio, Question.TYPE_AUDIO);
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
                throw new BusinessException("invalid password", HttpStatus.UNAUTHORIZED);
            }
        } else {
            throw new BusinessException("User not found", HttpStatus.NOT_FOUND);
        }
    }


    /**
     * POST /users/{userId}/favorites/questions
     * 描述：该用户收藏某问题
     * 请求体参数
     * password: String REQUIRED 该用户的密码
     * question_id: Number REQUIRED 问题id
     * 响应成功 状态码
     * 201 CREATED
     * 无响应体
     * 响应失败 状态码
     * 401 UNAUTHORIZED
     * 404 NOT FOUND
     * 503 SERVICE UNAVAILABLE
     *
     * @param userId
     * @param requestBody
     * @return
     */

    @RequestMapping(value = "/users/{userId}/favorites/questions", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> postUserFavoritesQuestions(
            @PathVariable("userId") int userId, @RequestBody Map<String, Object> requestBody) {
        Map<String, Object> m = null;
        User user = UserService.findUserById(userId);
        if (user != null) {
            String password = (String) requestBody.get("password");
            if (password != null && password.length() > 0 && password.equals(user.getPassword())) {

                if (requestBody.get("question_id") != null) {
                    int questionId = (int) requestBody.get("question_id");
                    if (QuestionService.findQuestionById(questionId) != null) {
                        UserService.addFavoriteQuestion(userId, questionId);
                        return new ResponseEntity<Map<String, Object>>(HttpStatus.CREATED);
                    } else {
                        throw new BusinessException("question not found", HttpStatus.NOT_FOUND);
                    }
                } else {
                    throw new BusinessException("require question id", HttpStatus.BAD_REQUEST);
                }
            } else {
                throw new BusinessException("invalid password", HttpStatus.UNAUTHORIZED);
            }
        } else {
            throw new BusinessException("User not found", HttpStatus.NOT_FOUND);
        }

    }

    /**
     * POST /users/{userId}/favorites/answers
     * 描述：该用户收藏某回答
     * 请求体参数
     * password: String REQUIRED 该用户的密码
     * password: String REQUIRED 该用户的密码
     * answer_id: Number REQUIRED 回答id
     * 响应成功 状态码
     * 201 CREATED
     * 无响应体
     * 响应失败 状态码
     * 401 UNAUTHORIZED
     * 404 NOT FOUND
     * 503 SERVICE UNAVAILABLE
     *
     * @param userId
     * @param requestBody
     * @return
     */

    @RequestMapping(value = "/users/{userId}/favorites/answers", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> postUserFavoritesAnswers(
            @PathVariable("userId") int userId, @RequestBody Map<String, Object> requestBody) {
        User user = UserService.findUserById(userId);
        if (user != null) {
            String password = (String) requestBody.get("password");
            if (password != null && password.length() > 0
                    && password.equals(user.getPassword())) {

                if (requestBody.get("answer_id") != null) {
                    int answerId = (int) requestBody.get("answer_id");
                    if (AnswerService.findAnswerById(answerId) != null) {
                        UserService.addFavoriteAnswer(userId, answerId);
                        return new ResponseEntity<Map<String, Object>>(HttpStatus.CREATED);
                    } else {
                        throw new BusinessException("answer not found", HttpStatus.NOT_FOUND);
                    }
                } else {
                    throw new BusinessException("require answer id", HttpStatus.BAD_REQUEST);
                }
            } else {
                throw new BusinessException("invalid password", HttpStatus.UNAUTHORIZED);
            }
        } else {
            throw new BusinessException("User not found", HttpStatus.NOT_FOUND);
        }
    }


    /**
     * POST /users/{userId}/favorites/essays
     * 描述：该用户收藏某收藏
     * 请求体参数
     * password: String REQUIRED 该用户的密码
     * essay_id: Number REQUIRED 文章id
     * 响应成功 状态码
     * 201 CREATED
     * 无响应体
     * 响应失败 状态码
     * 401 UNAUTHORIZED
     * 404 NOT FOUND
     * 503 SERVICE UNAVAILABLE
     *
     * @param userId
     * @param requestBody
     * @return
     */

    @RequestMapping(value = "/users/{userId}/favorites/essays", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> postUserFavoritesEssays(
            @PathVariable("userId") int userId, @RequestBody Map<String, Object> requestBody) {
        Map<String, Object> m = null;
        User user = UserService.findUserById(userId);
        if (user != null) {
            String password = (String) requestBody.get("password");
            if (password != null && password.length() > 0
                    && password.equals(user.getPassword())) {

                if (requestBody.get("essay_id") != null) {
                    int essayId = (int) requestBody.get("essay_id");
                    UserService.addFavoriteEssays(userId, essayId);

                    return new ResponseEntity<Map<String, Object>>(HttpStatus.CREATED);
                } else {
                    throw new BusinessException("essay not found", HttpStatus.NOT_FOUND);
                }
            } else {
                throw new BusinessException("invalid password", HttpStatus.UNAUTHORIZED);
            }
        } else {
            throw new BusinessException("User not found", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Map<String, Object>> updateUser(
            int userId, Map<String, Object> requestBody) throws IOException {
        Map<String, Object> m = null;
        User user = UserService.findUserById(userId);
        if (user != null) {
            String oldPassword = (String) requestBody.get("old_password");
            if (oldPassword == null) {
                oldPassword = (String) requestBody.get("password");
            }
            if (oldPassword != null && oldPassword.length() > 0 && oldPassword.equals(user.getPassword())) {
                String newPassword = (String) requestBody.get("new_password");
                if (newPassword != null) {
                    user.setPassword(newPassword);
                }
                String name = (String) requestBody.get("name");
                if (name != null) {
                    user.setUsername(name);
                }
                String bio = (String) requestBody.get("bio");
                if (bio != null) {
                    user.setBio(bio);
                }

                if (requestBody.get("age") != null) {
                    int age = (int) requestBody.get("age");
                    user.setAge(age);
                }
                String sex = (String) requestBody.get("sex");
                if (sex != null) {
                    user.setSex(sex);
                }
                String department = (String) requestBody.get("department");
                if (department != null) {
                    user.setDepartment(department);
                }
                String location = (String) requestBody.get("location");
                if (location != null) {
                    user.setLocation(location);
                }
                String avatar = (String) requestBody.get("avatar");
                if (avatar != null && avatar.contains("/avatars/")) {
                    String substr = avatar.substring(
                            avatar.indexOf("/avatars/") + "/avatars/".length());
                    user.setAvatar(Integer.parseInt(substr));
                }
                String phone = (String) requestBody.get("phone");
                if (phone != null) {
                    user.setPhone(phone);
                }
                String verificationCode = (String) requestBody.get("verification_code");

                UserService.updateUser(user);
                return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);

            } else {
                throw new BusinessException("invalid password", HttpStatus.UNAUTHORIZED);
            }
        } else {
            throw new BusinessException("User not found", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * PUT /users/{userId}
     * 请求体参数
     * name: String REQUIRED 用户的新名称
     * old_password: String REQUIRED 用户的原密码
     * new_password: String REQUIRED 用户的新密码
     * bio: String OPTIONAL 用户的新个性签名，默认为""
     * age: Number OPTIONAL 用户的新年龄，默认为0
     * sex: String OPTIONAL 用户的新性别，默认为unknown，只能为male或者female
     * department: String OPTIONAL 用户的新专业，默认为""
     * location: String OPTIONAL 用户的新地点，默认为""
     * avatar: String OPTIONAL 用户的新头像地址(URL)，默认为系统提供的头像
     * phone: String OPTIONAL 用户的新绑定的手机号。若有此项，则verification_code为REQUIRED
     * verification_code: Number OPTIONAL 手机验证码
     * 响应成功 状态码
     * 204 NO CONTENT
     * 无响应体
     * 响应失败 状态码
     * 401 UNAUTHORIZED
     * 404 NOT FOUND
     * 503 SERVICE UNAVAILABLE
     *
     * @param userId
     * @param requestBody
     * @return
     */

    @RequestMapping(value = "/users/{userId}", method = RequestMethod.PUT)
    public ResponseEntity<Map<String, Object>> putUser(
            @PathVariable("userId") int userId, @RequestBody Map<String, Object> requestBody) throws IOException {
        return updateUser(userId, requestBody);
    }


    @RequestMapping(value = "/users/{userId}/avatar", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> putUserAvatar(
            HttpServletRequest request,
            @PathVariable("userId") int userId,
            @RequestParam("description") String description,
            @RequestParam("avatar") MultipartFile img)
            throws IOException, NoSuchAlgorithmException {
        User user = UserService.findUserById(userId);
        if (user == null) {
            throw new BusinessException("User not found", HttpStatus.NOT_FOUND);
        }
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> descriptionMap = new HashMap<String, String>();
        descriptionMap = mapper.readValue(description, descriptionMap.getClass());
        String password = (String) descriptionMap.get("password");
        if (password == null || !password.equals(user.getPassword())) {
            throw new BusinessException("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        String md5 = MD5Util.getMultipartFileMD5(img);
        Avatar avatar1 = AvatarService.findAvatarByMd5(md5);
        long avatarId = 1;
        if (avatar1 != null) {
            avatarId = avatar1.getId();
        } else {

            String avatarDir = request.getSession().getServletContext().getRealPath("/")
                    + "avatars/";
            String originalFilename = img.getOriginalFilename();
            String suffix = originalFilename.substring(
                    originalFilename.lastIndexOf(".") + 1);
            Avatar avatar = new Avatar(md5, suffix);
            AvatarService.add(avatar);
            avatarId = avatar.getId();
            img.transferTo(new File(avatarDir + md5 + "." +  suffix));

        }
        user.setAvatar((int) avatarId);
        UserService.updateUser(user);
        Map<String, Object> map = new JsonMapBuilder()
                .append("url", ServerConfig.SERVER_BASE_URL + "/avatars/" + avatarId)
                .getMap();
        return new ResponseEntity<Map<String, Object>>(map, HttpStatus.NO_CONTENT);
    }

    /**
     * PATCH /users/{userId}
     * 请求体参数
     * name: String REQUIRED 用户的新名称
     * old_password: String REQUIRED 用户的原密码
     * new_password: String REQUIRED 用户的新密码
     * bio: String OPTIONAL 用户的新个性签名，默认为""
     * age: Number OPTIONAL 用户的新年龄，默认为0
     * sex: String OPTIONAL 用户的新性别，默认为unknown，只能为male或者female
     * department: String OPTIONAL 用户的新专业，默认为""
     * location: String OPTIONAL 用户的新地点，默认为""
     * avatar: String OPTIONAL 用户的新头像地址(URL)，默认为系统提供的头像
     * phone: String OPTIONAL 用户的新绑定的手机号。若有此项，则verification_code为REQUIRED
     * verification_code: Number OPTIONAL 手机验证码
     * 响应成功 状态码
     * 204 NO CONTENT
     * 无响应体
     * 响应失败 状态码
     * 401 UNAUTHORIZED
     * 404 NOT FOUND
     * 503 SERVICE UNAVAILABLE
     *
     * @param userId
     * @param requestBody
     * @return
     */
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.PATCH)
    public ResponseEntity<Map<String, Object>> patchUser(
            @PathVariable("userId") int userId, @RequestBody Map<String, Object> requestBody) throws IOException {
        return updateUser(userId, requestBody);
    }


    /**
     * DELETE /users/{userId}/favorites/questions/{questionId}
     * 描述：删除用户收藏过的某个问题
     * 请求体参数
     * password: String REQUIRED 用户的密码
     * questions_id: Array REQUIRED 用户收藏的问题列表
     * 响应成功 状态码
     * 204 NO CONTENT
     * 无响应体
     * 响应失败 状态码
     * 401 UNAUTHORIZED
     * 503 SERVICE UNAVAILABLE
     *
     * @param userId
     * @param requestMap
     * @return
     */
    @RequestMapping(value = "/users/{userId}/favorites/questions", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> deleteUserFavoritesQuestions(
            @PathVariable("userId") int userId, @RequestBody Map<String, Object> requestMap) {
        User user = UserService.findUserById(userId);
        Map<String, Object> m = null;
        if (user != null) {
            String password = (String) requestMap.get("password");
            if (password != null && password.length() > 0
                    && password.equals(user.getPassword())) {
                if (requestMap.get("questions_id") != null) {
                    List<Integer> questionIds = (List<Integer>) requestMap.get("questions_id");

                    for (Integer id : questionIds) {
                        UserService.removeFavoriteQuestion(userId, id);

                    }
                    m = new HashMap<String, Object>();
                    return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
                } else {
                    throw new BusinessException("require question id", HttpStatus.BAD_REQUEST);
                }
            } else {
                throw new BusinessException("invalid password", HttpStatus.UNAUTHORIZED);
            }
        } else {
            throw new BusinessException("User not found", HttpStatus.NOT_FOUND);
        }
    }


    /**
     * DELETE /users/{userId}/favorites/questions
     * 描述：删除用户收藏过的问题列表
     * 请求体参数
     * password: String REQUIRED 用户的密码
     * 响应成功 状态码
     * 204 NO CONTENT
     * 无响应体
     * 响应失败 状态码
     * 401 UNAUTHORIZED
     * 503 SERVICE UNAVAILABLE
     *
     * @param userId
     * @param questionId
     * @param requestMap
     * @return
     */
    @RequestMapping(value = "/users/{userId}/favorites/questions/{questionId}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> deleteUserFavoritesQuestion(
            @PathVariable("userId") int userId, @PathVariable("questionId") int questionId, @RequestBody Map<String, Object> requestMap) {
        User user = UserService.findUserById(userId);
        Map<String, Object> m = null;
        if (user != null) {
            String password = (String) requestMap.get("password");
            if (password != null && password.length() > 0 && password.equals(user.getPassword())) {

                UserService.removeFavoriteQuestion(userId, questionId);
                m = new HashMap<String, Object>();
                return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);

            } else {
                throw new BusinessException("invalid password", HttpStatus.UNAUTHORIZED);
            }
        } else {
            throw new BusinessException("User not found", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * DELETE /users/{userId}/favorites/answers
     * 描述：删除用户收藏过的回答列表
     * 请求体参数
     * password: String REQUIRED 用户的密码
     * answers_id: Array REQUIRED 用户收藏的回答列表
     * 响应成功 状态码
     * 204 NO CONTENT
     * 无响应体
     * 响应失败 状态码
     * 401 UNAUTHORIZED
     * 503 SERVICE UNAVAILABLE
     *
     * @param userId
     * @param requestMap
     * @return
     */
    @RequestMapping(value = "/users/{userId}/favorites/answers", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> deleteUserFavoritesAnswers(
            @PathVariable("userId") int userId, @RequestBody Map<String, Object> requestMap) {
        User user = UserService.findUserById(userId);
        Map<String, Object> m = null;
        if (user != null) {
            String password = (String) requestMap.get("password");
            if (password != null && password.length() > 0
                    && password.equals(user.getPassword())
                    && requestMap.get("answers_id") != null) {
                List<Integer> answerIds = (List<Integer>) requestMap.get("answers_id");

                for (Integer id : answerIds) {
                    UserService.removeFavoriteAnswer(userId, id);
                }
                m = new HashMap<String, Object>();
                return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
            } else {
                throw new BusinessException("invalid password", HttpStatus.UNAUTHORIZED);
            }
        } else {
            throw new BusinessException("User not found", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * DELETE /users/{userId}/favorites/answers/{answerId}
     * 描述：删除用户收藏过的某个回答
     * 请求体参数
     * password: String REQUIRED 用户的密码
     * 响应成功 状态码
     * 204 NO CONTENT
     * 无响应体
     * 响应失败 状态码
     * 401 UNAUTHORIZED
     * 503 SERVICE UNAVAILABLE
     *
     * @param userId
     * @param answerId
     * @param requestMap
     * @return
     */
    @RequestMapping(value = "/users/{userId}/favorites/answers/{answerId}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> deleteUserFavoritesAnswer(
            @PathVariable("userId") int userId,
            @PathVariable("answerId") int answerId,
            @RequestBody Map<String, Object> requestMap) {
        User user = UserService.findUserById(userId);
        Map<String, Object> m = null;
        if (user != null) {
            String password = (String) requestMap.get("password");
            if (password != null && password.length() > 0 && password.equals(user.getPassword())) {
                UserService.removeFavoriteAnswer(userId, answerId);
                m = new HashMap<String, Object>();

                return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
            } else {
                throw new BusinessException("invalid password", HttpStatus.UNAUTHORIZED);
            }
        } else {
            throw new BusinessException("User not found", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * DELETE /users/{userId}/favorites/essays
     * 描述：删除用户收藏过的文章列表
     * 请求体参数
     * password: String REQUIRED 用户的密码
     * essays_id: Array REQUIRED 用户收藏的文章列表
     * 响应成功 状态码
     * 204 NO CONTENT
     * 无响应体
     * 响应失败 状态码
     * 401 UNAUTHORIZED
     * 503 SERVICE UNAVAILABLE
     *
     * @param userId
     * @param requestMap
     * @return
     */
    @RequestMapping(value = "/users/{userId}/favorites/essays", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> deleteUserFavoritesEssays(
            @PathVariable("userId") int userId, @RequestBody Map<String, Object> requestMap) {
        User user = UserService.findUserById(userId);
        Map<String, Object> m = null;
        if (user != null) {
            String password = (String) requestMap.get("password");
            if (password != null && password.length() > 0
                    && password.equals(user.getPassword())
                    && requestMap.get("essays_id") != null) {
                List<Integer> essayIds = (List<Integer>) requestMap.get("essays_id");
                for (Integer id : essayIds) {

                    UserService.removeFavoriteEssay(userId, id);
                }
                m = new HashMap<String, Object>();
                return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
            } else {
                throw new BusinessException("invalid password", HttpStatus.UNAUTHORIZED);
            }
        } else {
            throw new BusinessException("User not found", HttpStatus.NOT_FOUND);
        }
    }


    /**
     * DELETE /users/{userId}/favorites/essays/{essayId}
     * 描述：删除用户收藏过的某个文章
     * 请求体参数
     * password: String REQUIRED 用户的密码
     * 响应成功 状态码
     * 204 NO CONTENT
     * 无响应体
     * 响应失败 状态码
     * 401 UNAUTHORIZED
     * 503 SERVICE UNAVAILABLE
     *
     * @param userId
     * @param essayId
     * @param requestMap
     * @return
     */

    @RequestMapping(value = "/users/{userId}/favorites/essays/{essayId}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> deleteUserFavoritesEssay(
            @PathVariable("userId") int userId,
            @PathVariable("essayId") int essayId,
            @RequestBody Map<String, Object> requestMap) {
        User user = UserService.findUserById(userId);
        Map<String, Object> m = null;
        if (user != null) {
            String password = (String) requestMap.get("password");
            if (password != null && password.length() > 0
                    && password.equals(user.getPassword())) {
                UserService.removeFavoriteEssay(userId, essayId);
                m = new HashMap<String, Object>();
                return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
            } else {
                throw new BusinessException("invalid password", HttpStatus.UNAUTHORIZED);
            }
        } else {
            throw new BusinessException("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/users/{userId}/notifications", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> deleteUserNotification(
            @PathVariable("userId") int userId,
            @RequestBody Map<String, Object> requestMap) {
        User user = UserService.findUserById(userId);
        if (user == null) {
            throw new BusinessException("User not found", HttpStatus.NOT_FOUND);
        }
        if (requestMap.get("notifications_id") == null) {
            throw new BusinessException("Bad request", HttpStatus.BAD_REQUEST);
        }

        List<Integer> ids = (List<Integer>) requestMap.get("notifications_id");
        for (Integer id : ids) {
            Notification notification = NotificationService.findNotificationById(id);
            if (notification != null) {
                notification.setIsRead(1);
                NotificationService.update(notification);
            }
        }

        return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/users/{userId}/notifications/{notificationId}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> deleteUserNotification(
            @PathVariable("userId") int userId,
            @PathVariable("notificationId") int notificationId,
            @RequestBody Map<String, Object> requestMap) {

        User user = UserService.findUserById(userId);
        if (user == null) {
            throw new BusinessException("User not found", HttpStatus.NOT_FOUND);
        }
        Notification notification = NotificationService.findNotificationById(notificationId);
        if (notification == null) {
            throw new BusinessException("Notification not found", HttpStatus.NOT_FOUND);
        }
        String password = (String) requestMap.get("password");
        if (password == null || !password.equals(user.getPassword())) {
            throw new BusinessException("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        notification.setIsRead(1);
        NotificationService.update(notification);
        return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
    }


}
