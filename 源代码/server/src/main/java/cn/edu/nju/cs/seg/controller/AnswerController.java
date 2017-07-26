package cn.edu.nju.cs.seg.controller;

import cn.edu.nju.cs.seg.ServerConfig;
import cn.edu.nju.cs.seg.exception.BusinessException;
import cn.edu.nju.cs.seg.json.JsonMapBuilder;
import cn.edu.nju.cs.seg.json.JsonMapResponseBuilderFactory;
import cn.edu.nju.cs.seg.pojo.*;
import cn.edu.nju.cs.seg.service.*;
import cn.edu.nju.cs.seg.util.MD5Util;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fwz on 2017/4/25.
 */
@RestController
@RequestMapping("/api")
public class AnswerController {
    private Logger LOG = LoggerFactory.getLogger(AnswerController.class);

    /**
     * GET /answers/{answerId}
     * 描述：获取对某回答
     * 响应成功 状态码
     * 200 OK
     * 响应失败 状态码
     * 404 NOT FOUND
     * 503 SERVICE UNAVAILABLE
     *
     * @param answerId
     * @return
     */
    @RequestMapping(value = "/answers/{answerId}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getAnswer(@PathVariable("answerId") int answerId) {
        Answer answer = AnswerService.findAnswerById(answerId);

        if (answer != null) {
            Map<String, Object> m = JsonMapResponseBuilderFactory
                    .createAnswerJsonMapBuilder(answer)
                    .getComplexMap();
            return new ResponseEntity<Map<String, Object>>(m, HttpStatus.OK);
        } else {
            throw new BusinessException("Answer not found", HttpStatus.NOT_FOUND);
        }

    }

    /**
     * GET /answers/{answerId}/comments
     * 描述：获取对某回答的评论列表
     * 响应成功 状态码
     * 200 OK
     * 响应失败 状态码
     * 404 NOT FOUND
     * 503 SERVICE UNAVAILABLE
     *
     * @param answerId
     * @return
     */

    @RequestMapping(value = "/answers/{answerId}/comments", method = RequestMethod.GET)
    public ResponseEntity<List<Map<String, Object>>> getAnswerComment(@PathVariable("answerId") int answerId) {
        List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();
        Answer answer = AnswerService.findAnswerById(answerId);
        if (answer != null) {
            List<Comment> comments = CommentService.findCommentsByAnswerId(answerId);
            for (Comment comment : comments) {
                Map<String, Object> m = JsonMapResponseBuilderFactory
                        .createCommentJsonMapBuilder(comment)
                        .getSimpleMap();
                l.add(m);
            }

            return new ResponseEntity<List<Map<String, Object>>>(l, HttpStatus.OK);
        } else {
            throw new BusinessException("Answer not found", HttpStatus.NOT_FOUND);
        }
    }


    /**
     * POST /answers
     * 描述：创建一条对某问题的回答
     * 请求体参数：
     * answer_commenter_email_or_phone: String REQUIRED 答主的账号(E-mail或手机号)
     * answer_commenter_password: String REQUIRED 答主的密码
     * question_id: Number REQUIRED 相关问题id
     * content: String REQUIRED 回答内容
     * 响应成功 状态码
     * 201 CREATED
     * 响应失败 状态码
     * 401 UNAUTHORIZED
     * 404 NOT FOUND
     * 503 SERVICE UNAVAILABLE
     *
     * @param description
     * @param files
     * @return
     */

    @RequestMapping(value = "/answers", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> postAnswer(
            HttpServletRequest request,
            @RequestParam("description") String description,
            @RequestParam("photo") List<MultipartFile> files,
            @RequestParam("audio") List<MultipartFile> audio)
            throws Exception {

        System.out.println(description);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> descriptionMap = new HashMap<String, Object>();
        descriptionMap = mapper.readValue(description, descriptionMap.getClass());

        String answererEmailOrPhone = (String) descriptionMap.get("answerer_email_or_phone");
        String answererPassword = (String) descriptionMap.get("answerer_password");
        int questionId = (int) descriptionMap.get("question_id");
        String type = (String) descriptionMap.get("type");

        User user = UserService.findUserByEmailOrPhone(answererEmailOrPhone);
        Question question = QuestionService.findQuestionById(questionId);
        if (question == null) {
            throw new BusinessException("Question not found", HttpStatus.NOT_FOUND);
        }
        Studio directedTo = question.getStudio();
        List<User> members = StudioService.findUsersByStudioId(directedTo.getId());

        if (user != null
                && members.contains(user)
                && answererPassword != null
                && answererPassword.equals(user.getPassword())) {

            Answer answer = null;
            if (type.equals("text")) {


                String content = (String) descriptionMap.get("content");

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
                answer = new Answer(document.toString(), user, question, Answer.TYPE_TEXT);
            } else {
                String audioDir = request.getSession().getServletContext().getRealPath("/")
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


    }

    /**
     * POST /answers/{answerId}/comments
     * 描述：创建一条对该回答的评论
     * 请求体参数
     * commenter_email_or_phone: String REQUIRED 评论者的账号(E-mail或手机号)
     * commenter_password: String REQUIRED 评论者的密码
     * content: String REQUIRED 评论内容
     * 响应成功 状态码
     * 201 CREATED
     * 响应失败 状态码
     * 401 UNAUTHORIZED
     * 404 NOT FOUND
     * 503 SERVICE UNAVAILABLE
     *
     * @param answerId
     * @param bodyMap
     * @return
     */

    @RequestMapping(value = "/answers/{answerId}/comments", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> postAnswerComment(@PathVariable("answerId") int answerId, @RequestBody Map<String, Object> bodyMap) {
        Answer answer = AnswerService.findAnswerById(answerId);
        Map<String, Object> m;
        if (answer != null) {
            String emailOrPhone = (String) bodyMap.get("commenter_email_or_phone");
            if (emailOrPhone != null) {
                User author = null;
                if (emailOrPhone.contains("@")) {
                    author = UserService.findUserByEmail(emailOrPhone);
                } else {
                    author = UserService.findUserByPhone(emailOrPhone);
                }
                if (author != null) {
                    String password = (String) bodyMap.get("commenter_password");
                    if (password != null && password.equals(author.getPassword())) {
                        String content = (String) bodyMap.get("content");
                        Comment comment = new Comment(author, content, answer);
                        int commentId = CommentService.add(comment);
                        m = new JsonMapBuilder()
                                .append("url", ServerConfig.SERVER_BASE_URL + "/comments/" + commentId)
                                .getMap();


                        return new ResponseEntity<Map<String, Object>>(m, HttpStatus.CREATED);
                    } else {
                        throw new BusinessException("invalid password", HttpStatus.UNAUTHORIZED);
                    }
                } else {
                    throw new BusinessException("user not found", HttpStatus.NOT_FOUND);
                }
            } else {
                throw new BusinessException("require user account", HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new BusinessException("Answer not found", HttpStatus.NOT_FOUND);
        }
    }


    /**
     * DELETE /answers/{answerId}
     * 描述：删除一个回答
     * 请求体参数
     * answerer_email_or_phone: String REQUIRED 回答者的账号(E-mail或手机号)
     * answerer_password: String REQUIRED 回答者的密码
     * 响应成功 状态码
     * 204 NO CONTENT
     * 无响应体
     * 响应失败 状态码
     * 401 UNAUTHORIZED
     * 503 SERVICE UNAVAILABLE
     *
     * @param answerId
     * @param bodyMap
     * @return
     */
    @RequestMapping(value = "/answers/{answerId}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<String, Object>> deleteAnswer(@PathVariable("answerId") int answerId,
                                                            @RequestBody Map<String, Object> bodyMap) {
        String answererEmailOrPhone = (String) bodyMap.get("answerer_email_or_phone");
        Answer answer = AnswerService.findAnswerById(answerId);
        Map<String, Object> m = null;
        if (answer != null) {
            User user;
            if (answererEmailOrPhone.contains("@")) {
                user = UserService.findUserByEmail(answererEmailOrPhone);
            } else {
                user = UserService.findUserByPhone(answererEmailOrPhone);
            }

            if (user != null) {
                String answererPassword = (String) bodyMap.get("answerer_password");
                if (answererPassword != null && answer.getAnswerer().getId() == user.getId() && answererPassword.equals(user.getPassword())) {
                    AnswerService.remove(answerId);
                    m = new HashMap<String, Object>();
                    return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
                } else {
                    throw new BusinessException("invalid password", HttpStatus.UNAUTHORIZED);
                }
            } else {
                throw new BusinessException("user not found", HttpStatus.NOT_FOUND);
            }
        } else {
            throw new BusinessException("Answer not found", HttpStatus.NOT_FOUND);
        }

    }

}
