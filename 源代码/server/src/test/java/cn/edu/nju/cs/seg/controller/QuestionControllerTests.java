package cn.edu.nju.cs.seg.controller;

import cn.edu.nju.cs.seg.json.JsonMapBuilder;
import cn.edu.nju.cs.seg.pojo.*;
import cn.edu.nju.cs.seg.service.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
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
public class QuestionControllerTests {
    private static final int TEST_QUESTION_ID = 1;

    @Autowired
    private QuestionController questionController;

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

        this.mockMvc = MockMvcBuilders
                .standaloneSetup(questionController)
                .setControllerAdvice(new ExceptionControllerAdvice())
                .build();
    }

    @Test
    public void testGetQuestionsSuccess() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/questions"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetOneQuestionSuccess() throws Exception {
        List<Question> questions = QuestionService.findAllQuestions();
        Question question = questions.get(0);
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/api/questions/" + question.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(question.getId()));
    }

    @Test
    public void testGetOneQuestionAnswers() throws Exception {
        List<Question> questions = QuestionService.findAllQuestions();
        Question question = questions.get(0);
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/api/questions/" + question.getId() + "/answers"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetOneQuestionNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/0"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testPostOneQuestionSupport() throws Exception {
        List<Question> questions = QuestionService.findAllQuestions();
        Question question = questions.get(0);
        String requestBody = new JsonMapBuilder()
                .append("supporter_email_or_phone", question.getStudio().getManager().getEmail())
                .append("supporter_password", question.getStudio().getManager().getPassword())
                .toString();
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/questions/" + question.getId() + "/supports")
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
    }

    @Test
    public void testDeleteOneQuestion() throws Exception {
        List<Question> questions = QuestionService.findAllQuestions();
        Question question = questions.get(0);
        String requestBody = new JsonMapBuilder()
                .append("manager_email_or_phone", question.getStudio().getManager().getEmail())
                .append("manager_password", question.getStudio().getManager().getPassword())
                .toString();
        this.mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/questions/" + question.getId())
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }
}
