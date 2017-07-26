package cn.edu.nju.cs.seg.controller;

import cn.edu.nju.cs.seg.ServerConfig;
import cn.edu.nju.cs.seg.ServerContext;
import cn.edu.nju.cs.seg.exception.BusinessException;
import cn.edu.nju.cs.seg.json.JsonMapBuilder;
import cn.edu.nju.cs.seg.json.JsonMapResponseBuilderFactory;
import cn.edu.nju.cs.seg.pojo.*;
import cn.edu.nju.cs.seg.service.*;
import cn.edu.nju.cs.seg.util.JPushUtil;
import cn.jpush.api.push.model.PushPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by fwz on 2017/5/1.
 */
@RestController
@RequestMapping("/api")
public class OtherController {

    protected static final Logger LOG =
            LoggerFactory.getLogger(OtherController.class);


    /**
     * GET /api
     * 描述：获取api
     * 响应成功 状态码
     * 200 OK
     *
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> getApi() {
        Map<String, Object> responseMap = new JsonMapBuilder()
                .append("url", ServerConfig.SERVER_BASE_URL)
                .append("users_url", ServerConfig.SERVER_BASE_URL + "/users?offset={offset}&limit={limit}{&search}")
                .append("user_url", ServerConfig.SERVER_BASE_URL + "/users/{userId}")
                .append("studios_url", ServerConfig.SERVER_BASE_URL + "/studios?offset={offset}&limit={limit}{&search}")
                .append("studio_url", ServerConfig.SERVER_BASE_URL + "/studios/{studioId}")
                .append("questions_url", ServerConfig.SERVER_BASE_URL + "/questions?offset={offset}&limit={limit}{&search,heat}")
                .append("question_url", ServerConfig.SERVER_BASE_URL + "/questions/{questionId}")
                .append("essays_url", ServerConfig.SERVER_BASE_URL + "/essays?offset={offset}&limit={limit}{&search,heat}")
                .append("essay_url", ServerConfig.SERVER_BASE_URL + "/essays/{essayId}")
                .getMap();
        return responseMap;

    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Map<String, Object>> getHeatsIndex(HttpServletRequest request) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
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
        List<Question> questions = QuestionService.findAllQuestions();
        List<Essay> essays = EssayService.findAllEssays();
        List<Question> latestQuestions = new ArrayList<Question>();
        List<Essay> latestEssays = new ArrayList<Essay>();
        for (int i = 0; i < 20 && i < questions.size(); i++) {
            latestQuestions.add(questions.get(questions.size() - i - 1));
        }
        for (int i = 0; i < 20 && i < essays.size(); i++) {
            latestEssays.add(essays.get(essays.size() - i - 1));
        }
        Collections.sort(questions, new Comparator<Question>() {
            @Override
            public int compare(Question o1, Question o2) {
                return new Integer(o2.getHeat()).compareTo(o1.getHeat());
            }
        });
        Collections.sort(essays, new Comparator<Essay>() {
            @Override
            public int compare(Essay o1, Essay o2) {
                return new Integer(o2.getHeat()).compareTo(o1.getHeat());
            }
        });
        int i = 0, j = 0, n = 0;
        List<Object> l = new ArrayList<Object>();
        Random random = new Random(System.currentTimeMillis());
        while (i < questions.size() && j < essays.size() && i + j + n < offset + limit) {
            if (random.nextInt(100) < 70) {
                Question question = questions.get(i);
                Essay essay = essays.get(j);
                if (question.getHeat() >= essay.getHeat()) {
                    l.add(question);
                    i++;
                } else {
                    l.add(essay);
                    j++;
                }
            } else {
                if (random.nextInt(10) < 4) {
                    Essay essay = latestEssays.get(random.nextInt(latestEssays.size()));
                    if (!l.contains(essay)) {
                        l.add(essay);
                    }
                } else {
                    Question question = latestQuestions.get(random.nextInt(latestQuestions.size()));
                    if (!l.contains(question)) {
                        l.add(question);
                    }
                }
                n++;
            }
        }
        while (i < questions.size() && i + j + n < offset + limit) {
            if (random.nextInt(100) < 70) {
                Question question = questions.get(i);
                l.add(question);
                i++;
            } else {
                if (random.nextInt(10) < 4) {
                    Essay essay = latestEssays.get(random.nextInt(latestEssays.size()));
                    if (!l.contains(essay)) {
                        l.add(essay);
                    }
                } else {
                    Question question = latestQuestions.get(random.nextInt(latestQuestions.size()));
                    if (!l.contains(question)) {
                        l.add(question);
                    }
                }
                n++;
            }
        }
        while (j < essays.size() && i + j + n < offset + limit) {
            if (random.nextInt(100) < 70) {
                Essay essay = essays.get(j);
                l.add(essay);
                j++;
            } else {
                if (random.nextInt(10) < 4) {
                    Essay essay = latestEssays.get(random.nextInt(latestEssays.size()));
                    if (!l.contains(essay)) {
                        l.add(essay);
                    }
                } else {
                    Question question = latestQuestions.get(random.nextInt(latestQuestions.size()));
                    if (!l.contains(question)) {
                        l.add(question);
                    }
                }
                n++;
            }
        }

        for (int k = offset; k < l.size() && k < offset + limit; k++) {
            Map<String, Object> map;
            if (l.get(k) instanceof Question) {
                map = JsonMapResponseBuilderFactory
                        .createQuestionJsonMapBuilder((Question) l.get(k))
                        .getSimpleMap();
                map.put("type", "question");
            } else {
                map = JsonMapResponseBuilderFactory
                        .createEssayJsonMapBuilder((Essay) l.get(k))
                        .getSimpleMap();
                map.put("type", "essay");
            }

            list.add(map);
        }
        return list;
    }

    /**
     * POST /login
     * 描述：用户登录
     * 请求体参数
     * user_email_or_phone: String REQUIRE 邮箱
     * user_password: String REQUIRE 密码
     * 响应成功 状态码
     * 200 OK
     * 响应失败 状态码
     * 400 BAD REQUEST
     * 401 UNAUTHORIZED
     * 503 SERVICE UNAVAILABLE
     *
     * @param requestMap
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> postLogin(
            @RequestBody Map<String, Object> requestMap) {
        HttpStatus errorStatus = HttpStatus.SERVICE_UNAVAILABLE;
        Map<String, Object> m;
        String userEmailOrPhone = (String) requestMap.get("user_email_or_phone");

        User user = UserService.findUserByEmailOrPhone(userEmailOrPhone);
        String userPassword = (String) requestMap.get("user_password");
        if (user != null && userPassword != null && userPassword.length() > 0 && userPassword.equals(user.getPassword())) {
            m = JsonMapResponseBuilderFactory
                    .createUserJsonMapBuilder(user)
                    .getComplexMap();
            m.put("password", user.getPassword());
            m.put("alias", "" + user.getId());
            List<String> tags = new ArrayList<>();
            List<Studio> studios = StudioService.findStudiosByUserId(user.getId());
            for (Studio s : studios) {
                tags.add("" + s.getId());
            }
            m.put("tags", tags);

            return new ResponseEntity<Map<String, Object>>(m, HttpStatus.OK);

        } else {
            throw new BusinessException("Invalid email/phone or password", HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/login/confirm", method = RequestMethod.POST)
    public ResponseEntity<String> postLoginConfirm(@RequestBody Map<String, Object> requestMap) {

        String alias = (String) requestMap.get("alias");
        List<String> tags = (List<String>) requestMap.get("tags");
        Integer userId = (Integer) requestMap.get("user_id");
        if (alias == null || tags == null || userId == null) {
            throw new BusinessException("Bad Request", HttpStatus.BAD_REQUEST);
        }
        ServerContext.logins.put(alias, tags);
        List<Notification> notifications = NotificationService.findNotificationsByUserId(userId);
        if (notifications != null && notifications.size() > 0) {
            boolean hasUnreadNotifications = false;
            for (Notification notification : notifications) {
                if (notification.getIsRead() == 0) {
                    hasUnreadNotifications = true;
                    break;
                }
            }
            if (hasUnreadNotifications) {
                PushPayload payload = JPushUtil.buildPushObject_android_alias_msg(alias);
                JPushUtil.sendPush(payload);
            }

        }
        return new ResponseEntity<String>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResponseEntity<String> postLogout(@RequestBody Map<String, Object> requestMap) {
        String userEmailOrPhone = (String) requestMap.get("user_email_or_phone");
        String userPassword = (String) requestMap.get("user_password");

        if (userEmailOrPhone == null || userPassword == null) {
            throw new BusinessException("Bad Request", HttpStatus.BAD_REQUEST);
        }
        User user = UserService.findUserByEmailOrPhone(userEmailOrPhone);
        if (user == null || !user.getPassword().equals(userPassword)) {
            throw new BusinessException("Unauthrized", HttpStatus.UNAUTHORIZED);
        }
        ServerContext.logins.remove(user.getId() + "");
        return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getTest() {
        LOG.error("1");
        System.out.println("111");
        System.out.println(ServerContext.logins.toString());
        return new ResponseEntity<Map<String, Object>>(HttpStatus.OK);

    }


}
