package cn.edu.nju.cs.seg.controller;

import cn.edu.nju.cs.seg.json.JsonMapBuilder;
import cn.edu.nju.cs.seg.pojo.*;
import cn.edu.nju.cs.seg.service.*;
import com.jayway.jsonpath.JsonPath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Random;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Created by fwz on 2017/7/5.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/mvc-test-config.xml"})
@WebAppConfiguration
public class UserControllerTests {

    private static final int TEST_USER_ID = 1;

    @Autowired
    private UserController userController;

    @Autowired
    private VerificationCodeService verificationCodeService;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        Random random = new Random(System.currentTimeMillis());
        String name = "";
        for (int i = 0; i < 6; i++) {
            name += random.nextInt(10);
        }
        User user = new User(name + "@nju.edu.cn", "1234");
        Studio studio = new Studio(name, user);
        Question question = new Question(
                "title", "content", user, studio, Question.TYPE_TEXT);
        Answer answer = new Answer("content", user, question, Answer.TYPE_TEXT);
        Essay essay = new Essay("title", "content", studio, Essay.TYPE_TEXT);
        UserService.add(user);
        StudioService.add(studio);
        StudioService.addMember(studio.getId(), user.getId());
        QuestionService.add(question);
        AnswerService.add(answer);
        EssayService.add(essay);

        this.mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new ExceptionControllerAdvice())
                .build();
    }

    @Test
    public void testGetUsersSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users"))
                .andExpect(status().isOk())
//                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void testGetOneUserSuccess() throws Exception {
        List<User> users = UserService.findAllUsers();
        User user = users.get(0);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.answers").exists())
                .andExpect(jsonPath("$.questions").exists())
                .andExpect(jsonPath("$.favorite_answers").exists())
                .andExpect(jsonPath("$.favorite_questions").exists())
                .andExpect(jsonPath("$.favorite_essays").exists())
                .andExpect(jsonPath("$.studios").exists())
                .andExpect(jsonPath("$.url").exists())
                .andExpect(jsonPath("$.avatar_url").exists())
                .andExpect(jsonPath("$.questions_url").exists())
                .andExpect(jsonPath("$.answers_url").exists())
                .andExpect(jsonPath("$.favorite_questions_url").exists())
                .andExpect(jsonPath("$.favorite_answers_url").exists())
                .andExpect(jsonPath("$.favorite_essays_url").exists())
                .andExpect(jsonPath("$.studios_url").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.phone").exists())
                .andExpect(jsonPath("$.bio").exists())
                .andExpect(jsonPath("$.sex").exists())
                .andExpect(jsonPath("$.age").exists())
                .andExpect(jsonPath("$.department").exists())
                .andExpect(jsonPath("$.location").exists());
    }

    @Test
    public void testGetOneUserQuestionsSuccess() throws Exception {
        List<User> users = UserService.findAllUsers();
        User user = users.get(0);
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/api/users/"+ user.getId() + "/questions"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetOneUserAnswersSuccess() throws Exception {
        List<User> users = UserService.findAllUsers();
        User user = users.get(0);
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/api/users/"+ user.getId() + "/answers"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetOneUserStudiosSuccess() throws Exception {
        List<User> users = UserService.findAllUsers();
        User user = users.get(0);
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/api/users/"+ user.getId() + "/studios"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetOneUserFavoriteQuestionsSuccess() throws Exception {
        List<User> users = UserService.findAllUsers();
        User user = users.get(0);
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/api/users/"+ user.getId() + "/favorites/questions"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetOneUserFavoriteAnswersSuccess() throws Exception {
        List<User> users = UserService.findAllUsers();
        User user = users.get(0);
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/api/users/"+ user.getId() + "/favorites/answers"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetOneUserFavoriteEssaysSuccess() throws Exception {
        List<User> users = UserService.findAllUsers();
        User user = users.get(0);
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/api/users/"+ user.getId() + "/favorites/essays"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetOneUserNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/0"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testPostOneUserSuccess() throws Exception {
        Random random = new Random();
        String testEmail = "";
        for (int i = 0; i < 8; i++) {
            testEmail += random.nextInt(10);
        }
        testEmail += "@nju.edu.cn";
        VerificationCode code = new VerificationCode(
                testEmail, "111111",
                System.currentTimeMillis() + 6 * 60 * 1000);
        this.verificationCodeService.add(code);
        String requestBody = new JsonMapBuilder()
                .append("email", testEmail)
                .append("password", "123456")
                .append("verification_code", code.getCode())
                .toString();
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.url").exists())
                .andReturn();
        String url = JsonPath.read(result.getResponse().getContentAsString(), "$.url");
        int id = Integer.parseInt(url.substring(url.lastIndexOf("/") + 1));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/users/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    public void testPostOneUserUnauthorized() throws Exception {
//        User user
        String requestBody = new JsonMapBuilder()
                .append("email", "TestUser@nju.edu.cn")
                .append("password", "123456")
                .append("verification_code", "0000000")
                .toString();
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnauthorized());
    }

//     @Test
//     public void testPostOneQuestion() throws Exception {
//         Studio studio = StudioService.findStudioById(1);
//         User user = UserService.findUserById(TEST_USER_ID);
//         String requestBody = new JsonMapBuilder()
//                 .append("question_title", "title")
//                 .append("password", user.getPassword())
//                 .append("directed_to", studio.getName())
//                 .append("type", "text")
//                 .toString();

// //        MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
// //
// //        InputStream inputStream =
// //                this.getClass().getResourceAsStream("/images/ic_launcher.png");
// //        URL url = this.getClass().getResource("/images/ic_launcher.png");
// //        System.out.println(inputStream == null);
// //        File file = new File("");
// //        for (String str : file.list()) {
// //            System.out.println(str);
// //        }
// //        FileInputStream fis = new FileInputStream("/images/ic_laucher.png");
// //        System.out.println(inputStream.toString());
// //        FileInputStream fis = (FileInputStream) inputStream;
// //        MockMultipartFile multipartFile =
// //                new MockMultipartFile("photopath", "/images/ic_laucher.png",
// //                        "image/png", fis);
// //        request.addFile(multipartFile);
// //        request.setMethod("POST");
// //        request.setContentType("multipart/form-data");
// //        request.addHeader("Content-type", "multipart/form-data");
// //        request.setRequestURI("/api/users/" + TEST_USER_ID + "/questions");
// //        request.addParameter("description", requestBody);

//         MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders
//                 .post("/api/users/" + TEST_USER_ID + "/questions")
// //                .content(requestBody)
//                 .contentType(MediaType.MULTIPART_FORM_DATA)
// //                .header("ContentType", "multipart/form-data")
//                 .param("description", requestBody)
//                 .accept(MediaType.APPLICATION_JSON_VALUE)
//                 .contentType(MediaType.APPLICATION_JSON_VALUE))
//                 .andExpect(status().isCreated())
//                 .andExpect(jsonPath("url").exists())
//                 .andReturn();

// //        String url = JsonPath.read(result.getResponse().getContentAsString(), "$.url");
// //        int id = Integer.parseInt(url.substring(url.lastIndexOf("/") + 1));
// //        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/questions/" + id))
// //                .andExpect(status().isOk())
// //                .andExpect(jsonPath("$.id").value(id));

//     }

    @Test
    public void testPostOneFavoriteQuestion() throws Exception {
        List<User> users = UserService.findAllUsers();
        User user = users.get(0);
        List<Question> questions = QuestionService.findAllQuestions();
        Question question = questions.get(0);
        String requestBody = new JsonMapBuilder()
                .append("question_id", question.getId())
                .append("password", user.getPassword())
                .toString();
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/users/" + user.getId() + "/favorites/questions")
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
    }

    @Test
    public void testPostOneFavoriteAnswer() throws Exception {
        List<User> users = UserService.findAllUsers();
        User user = users.get(0);
        List<Answer> answers = AnswerService.findAllAnswers();
        Answer answer = answers.get(0);
        String requestBody = new JsonMapBuilder()
                .append("answer_id", answer.getId())
                .append("password", user.getPassword())
                .toString();
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/users/" + user.getId() + "/favorites/answers")
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
    }

    @Test
    public void testPostOneFavoriteEssay() throws Exception {
        List<User> users = UserService.findAllUsers();
        User user = users.get(0);
        List<Essay> essays = EssayService.findAllEssays();
        Essay essay = essays.get(0);
        String requestBody = new JsonMapBuilder()
                .append("essay_id", essay.getId())
                .append("password", user.getPassword())
                .toString();
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/users/" + user.getId() + "/favorites/essays")
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
    }

    @Test
    public void testPutOneUser() throws Exception {
        User user = UserService.findUserById(TEST_USER_ID);
        String requestBody = new JsonMapBuilder()
                .append("name", "test")
                .append("old_password", user.getPassword())
                .append("new_password", user.getPassword())
                .append("bio", "bio")
                .append("age", user.getAge() + 1)
                .append("sex", "unknown")
                .append("department", "cs")
                .append("location", "shanghai")
                .toString();
        this.mockMvc.perform(MockMvcRequestBuilders
                .put("/api/users/" + TEST_USER_ID)
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());

        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/api/users/" + TEST_USER_ID))
                .andExpect(jsonPath("name").value("test"))
                .andExpect(jsonPath("bio").value("bio"))
                .andExpect(jsonPath("age").value(user.getAge() + 1))
                .andExpect(jsonPath("sex").value("unknown"))
                .andExpect(jsonPath("department").value("cs"))
                .andExpect(jsonPath("location").value("shanghai"));
    }

    @Test
    public void testDeleteOneFavoriteQuestions() throws Exception {
        List<User> users = UserService.findAllUsers();
        User user = users.get(0);
        List<Question> questions = QuestionService.findAllQuestions();
        Question question = questions.get(0);
        String requestBody = new JsonMapBuilder()
                .append("password", user.getPassword())
                .toString();
        this.mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/users/" + user.getId() + "/favorites/questions/1")
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteOneFavoriteAnswers() throws Exception {
        List<User> users = UserService.findAllUsers();
        User user = users.get(0);
        List<Answer> answers = AnswerService.findAllAnswers();
        Answer answer = answers.get(0);
        String requestBody = new JsonMapBuilder()
                .append("password", user.getPassword())
                .toString();
        this.mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/users/" + user.getId() + "/favorites/answers/1")
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteOneFavoriteEssays() throws Exception {
        List<User> users = UserService.findAllUsers();
        User user = users.get(0);
        List<Essay> essays = EssayService.findAllEssays();
        Essay essay = essays.get(0);
        String requestBody = new JsonMapBuilder()
                .append("password", user.getPassword())
                .toString();
        this.mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/users/" + user.getId() + "/favorites/essays/1")
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }



}
