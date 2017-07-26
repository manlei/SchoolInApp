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
public class EssayControllerTests {
    private static final int TEST_COMMENT_ID = 1;

    @Autowired
    private EssayController essayController;

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
                .standaloneSetup(essayController)
                .setControllerAdvice(new ExceptionControllerAdvice())
                .build();
    }

    @Test
    public void testGetEssaysSuccess() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/essays"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetOneEssaySuccess() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/api/essays/" + TEST_COMMENT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(TEST_COMMENT_ID))
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.heat").exists())
                .andExpect(jsonPath("$.supports").exists())
                .andExpect(jsonPath("$.comments").exists())
                .andExpect(jsonPath("$.studio").exists())
                .andExpect(jsonPath("$.studio_bio").exists())
                .andExpect(jsonPath("$.url").exists())
                .andExpect(jsonPath("$.studio_url").exists())
                .andExpect(jsonPath("$.studio_avatar_url").exists())
                .andExpect(jsonPath("$.comments_url").exists())
                .andExpect(jsonPath("$.created_at").exists());
    }

    @Test
    public void testGetOneEssayNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/0"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testPostOneEssaySupport() throws Exception {

        List<Essay> essays = EssayService.findAllEssays();
        Essay essay = essays.get(0);
        String requestBody = new JsonMapBuilder()
                .append("supporter_email_or_phone", essay.getStudio().getManager().getEmail())
                .append("supporter_password", essay.getStudio().getManager().getPassword())
                .toString();
        this.mockMvc.perform(MockMvcRequestBuilders
                .post("/api/essays/" + essay.getId() + "/supports")
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
    }

    @Test
    public void testDeleteOneEssay() throws Exception {
        List<Essay> essays = EssayService.findAllEssays();
        Essay essay = essays.get(0);
        String requestBody = new JsonMapBuilder()
                .append("studio_manager_email_or_phone", essay.getStudio().getManager().getEmail())
                .append("studio_manager_password", essay.getStudio().getManager().getPassword())
                .toString();
        this.mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/essays/" + essay.getId())
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }
}
