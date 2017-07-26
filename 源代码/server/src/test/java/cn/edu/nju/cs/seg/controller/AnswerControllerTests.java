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
public class AnswerControllerTests {
    private static final int TEST_ANSWER_ID = 1;

    @Autowired
    private AnswerController answerController;

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
        this.mockMvc = MockMvcBuilders.standaloneSetup(answerController)
                .setControllerAdvice(new ExceptionControllerAdvice())
                .build();
    }


    @Test
    public void testGetOneAnswerSuccess() throws Exception {
        List<Answer> answers = AnswerService.findAllAnswers();
        Answer answer = answers.get(0);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/answers/" + answer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(answer.getId()))
                .andExpect(jsonPath("$.answerer").exists())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.question_title").exists())
                .andExpect(jsonPath("$.comments").exists())
                .andExpect(jsonPath("$.url").exists())
                .andExpect(jsonPath("$.question_url").exists())
                .andExpect(jsonPath("$.answerer_url").exists())
                .andExpect(jsonPath("$.answerer_avatar_url").exists())
                .andExpect(jsonPath("$.comments_url").exists())
                .andExpect(jsonPath("$.created_at").exists())
        ;
    }

    @Test
    public void testGetOneAnswerNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/answers/0"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAnswerComments() throws Exception {
        List<Answer> answers = AnswerService.findAllAnswers();
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/answers/" + answers.get(0).getId() + "/comments"))
                .andExpect(status().isOk());
    }

    @Test
    public void testPostAnswerComment() throws Exception {
        List<Answer> answers = AnswerService.findAllAnswers();
        Answer answer = answers.get(0);
        String requestBody = new JsonMapBuilder()
                .append("commenter_email_or_phone", answer.getAnswerer().getEmail())
                .append("commenter_password", answer.getAnswerer().getPassword())
                .append("content", "content")
                .toString();
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/answers/" + answer.getId() + "/comments")
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
    }

    @Test
    public void testDeleteOneAnswer() throws Exception {
        List<Answer> answers = AnswerService.findAllAnswers();
        Answer answer = answers.get(0);
        String requestBody = new JsonMapBuilder()
                .append("answerer_email_or_phone", answer.getAnswerer().getEmail())
                .append("answerer_password", answer.getAnswerer().getPassword())
                .toString();
        this.mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/answers/" + answer.getId())
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }

}
